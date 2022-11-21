package hello.springtx.order;

/**
 * 이 비즈니스 예외 발생시에는 롤백이 아닌 대기를 하고 싶다.
 */
public class NotEnoughMoneyException extends Exception{

    public NotEnoughMoneyException(String message){
        super(message);
    }
}
