# Exception

`Exception`: 체크 예외
- 애플리케이션 로직에서 사용할 수 있는 실질적 최상위 예외이다. 
- `Exception`과 그 하위 예외는 모두 컴파일러가 체크하는 체크 예외다.(단, `RuntimeException`은 아니다.)

`RuntimeException`: 언체크 예외, 런타임 예외
- 컴파일러가 체크하지 않는 예외
- `RuntimeException`과 그 하위 예외는 모두 언체크이고 `런타임 예외`라고 많이 부른다.

## 기본 규칙
예외는 폭탄돌리기와 같다. 잡아서 처리하거나, 밖으로 던져야 한다.  
-> 예외를 처리하지 못하면 호출한 곳으로 예외를 계속 던지게 된다.  

예외를 잡거나 던질때 지정한 예외뿐만 아니라 하위의 모든 예외도 함께 처리된다.  

어플리케이션에서는 프로그램이 다운되면 안되기 때문에 개발자가 지정한 페이지를 보여주도록 WAS가 처리한다.  

> 기본적으로는 언체크 예외를 사용하자.  
> 체크예외는 비즈니스 로직상 의도적으로 던지는 예외에만 사용하자.  
> **ex) 계좌 이체 실패 예외, 결제시 포인트 부족 예외, 로그인 정보 불일치 예외**

## 체크 예외

`throws`를 통해서 던지거나 catch로 통해서 잡거나 둘중 하나를 무조건 해야하는 예외. 
컴파일안에서 잡을 수 있는 예외이다.

### 장단점
`장점`: 개발자의 실수로 예외를 누락하지 않도록 컴파일러를 통해 잡아주는 훌륭한 안전 장치이다.  
`단점`: 모든 예외에 대해서 처리해야하기 때문에 번거롭다. 크게 신경쓰지 않은 예외까지도 챙겨야하기 때문이다.  

### 문제점
**대부분의 예외는 복구가 불가능하다.**
1. 만약 하나의 service에서 2곳의 exception을 받았다고 생각해보자.(SQLException, ConnectException)   
2. 서비스단에서 오는 에러에 대해서 어플리케이션에서 해결해줄수 없다.  
3. 그러면 밖으로 던져야 한다.  
4. 그러면 클라이언트와 맨 마지막에 처리하면 결국 throws로 던지게 된다.
5. `ControllerAdvice`에서 이런 예외를 공통적으로 처리한다.
6. 클라이언트에겐 결국 서비스가 안된다는 메세지밖에 보여줄 수 없다.(개발자를 위해서는 로그를 남기고 알람을 보내게 된다.)
  

**의존관계에 대한 문제가 있다.**
예를 들어 서비스 로직에 `SQLException`이 있다면 JDBC에 의존관계가 생기게 된다.   
어차피 처리할 수도 없는 예외에 대해서 의존관계가 잡혀서 추후 수정시에 문제가 생기게 된다.  

Exception이라는 최상위를 보내준다고 해도 중요한 체크 예외를 던져도 모르고 넘어가게 된다.

> 이러한 체크 예외의 문제는 언체크 예외를 통해서 잡는 것이 요즘 트렌드이다.


## 언체크 예외

예외를 잡아서 처리하지 않아도 `throws`를 사용할 필요가 없다.  

### 장단점
`장점`: 신경쓰지 않고 싶을때는 예외를 무시할 수 있다. 꼭 `throws`를 사용할 필요가 없기 때문이다.
`단점`: 언체크 예외는 개발자가 실수로 예외를 누락할 수 있다는 점이 있다.

### 체크 예외를 언체크 예외로 돌릴 경우
기존의 해결할수 없는 문제(SQLException, ConnectException 등)에 대해서 개발자가 신경쓰지 않아도 된다.  
또한, `service`나 `controller`에 의존관계가 생기지 않게 된다.  
추후 수정시에도 공통적인 곳에서만 수정이 가능하게 된다.  

이러한 이유들 때문에 예외는 문서화를 잘 해두는 것이 중요하다.

## 예외 포함과 스택 트레이스

로그를 출력할때 마지막 파라미터에 예외를 넣어주면 로그에 스택 트레이스를 출력할 수 있다.  
실무에서는 로그를 사용하는 것이 좋기 때문에 `e.printStackTrace()`를 사용하는 것을 권장하지 않는다.  

## 데이터 예외 직접 복구하기

DB 오류에 따라서 특정 예외는 복구하고 싶을 때가 있다.  
예를 들어서 key값 중복에 대해서 변형해서 다시 insert하고 싶을 수가 있다.(unique key error)  

해당 문제에 대해서 SQLException에는 데이터베이스가 제공하는 `error code`를 활용하면 해결할 수 있다.
> **h2 DB**
> `23505`: 키 중복 오류
> `42000`: SQL 문법 오류
> DB마다 모두 다르다.

## Spring Exception

최상위의 경우에는 `DataAccessException` 이다.  
그 것중 크게 2가지로 구분된다. `Transient`와 `NonTransient`가 있다.
