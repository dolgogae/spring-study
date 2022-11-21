package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired MemberRepository memberRepository;
    @Autowired LogRepository logRepository;
    @Autowired MemberService memberService;


    @Autowired
    PlatformTransactionManager txManager;

    /**
     * memberService        @Transactional:OFF
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON
     */
    @Test
    void outerTxOff_success(){
        String username = "outerTxOff_success";

        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService        @Transactional:OFF
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON_EXCEPTION
     */
    @Test
    void outerTxOff_fail(){
        String username = "outerTxOff_fail_로그예외";

        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:OFF
     * logRepository        @Transactional:OFF
     */
    @Test
    void singleTx(){
        String username = "singleTx";

        // memberService에는 @Transactional을 붙혀준다.
        memberService.joinV1(username);

        // repository의 @Transactional을 제거해야한다.
        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }


    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON
     */
    @Test
    void outerTxOn_success(){
        String username = "outerTxOn_success";

        // memberService에는 @Transactional을 붙혀준다.
        memberService.joinV1(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isPresent());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON_EXCEPTION
     */
    @Test
    void outerTxOn_fail(){
        String username = "outerTxOn_fail_로그예외";

        // memberService에는 @Transactional을 붙혀준다.
        assertThatThrownBy(() -> memberService.joinV1(username))
                .isInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON_EXCEPTION
     *
     * 예외 발생시에 예외를 던지고 memberSerivce에서 정상흐름으로 복구 시켜준다면?
     * 당연히 rollback-only 마킹이 되어있기 때문에 물리 트랜잭션이 모두 rollback 된다.
     */
    @Test
    void recovery(){
        String username = "outerTxOn_fail_로그예외";

        // memberService에는 @Transactional을 붙혀준다.
        assertThatThrownBy(() -> memberService.joinV2(username))
                .isInstanceOf(RuntimeException.class);

        assertTrue(memberRepository.find(username).isEmpty());
        assertTrue(logRepository.find(username).isEmpty());
    }

    /**
     * memberService        @Transactional:ON
     * memberRepository     @Transactional:ON
     * logRepository        @Transactional:ON_EXCEPTION - REQUIRES_NEW
     *
     * REQUIRED_NEW 옵션을 주면 Log에서 예외가 발생해도 Member는 커밋이 된다.
     * 물리 트랜잭션을 분리해주는 옵션이기 때문이다.
     *
     * 별도로 하고 싶은 논리 트랜잭션을 @Transactional(propagation = Propagation.REQUIRES_NEW) 옵션을 주면 된다.
     */
    @Test
    void recoveryException_success(){
        String username = "outerTxOn_fail_로그예외";

        memberService.joinV2(username);

        assertTrue(memberRepository.find(username).isPresent());
        assertTrue(logRepository.find(username).isEmpty());
    }
}