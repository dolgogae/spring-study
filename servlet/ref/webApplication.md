# Web Application

대부분을 HTTP를 이용해서 전송한다.

## Web server
HTTP를 기반으로 동작.  
정적 리소스 제공 및 기타 부가기능.  
HTTP 프로토콜로 서로 데이터를 주고 받는다.  

## WAS
프로그램 코드를 통해서 애플리케이션 로직을 수행한다.  
ex) tomcat, nginx

간단하게 생각하면 정적 리소스는 web server, 로직 수행은 WAS 이다.

WAS가 너무 많은 역할을 담고하고 서버 과부하가 우려된다.  
따라서 일반적으로 정적 리소스를 web server로 분리하게 된다.  
이렇게 하면 WAS는 효율적인 리소스 관리과 비즈니스 로직을 순수하게 분리할 수 있다.

## 서블릿
POST전송 -> 웹브라우져가 생성한 요청 HTTP 메시지 -(연결 및 해석)-> 비즈니스 로직 -> 응답 메시지 생성 -> 전달

비즈니스 로직이 가장 중요한데 전후 과정이 너무 복잡하다.  
이것을 서블릿이 모두 간단하게 해결해준다.  

1. 서블릿 컨테이너가 request, response 객체를 생성
2. 개발자는 request객체에서 HTTP 요청 정보를 편리하게 꺼내 사용
3. response객체 또한 HTTP 응답 정보 편리하게 입력
4. WAS는 response 내용 전달

### 서블릿 컨테이너
서블릿을 지원하는 WAS.  
서블릿 객체 생성, 초기화, 호출, 종료의 생명주기를 관리  
싱글톤으로 생성되어 있다.(컨테이너의 경우에는 계속 생성할 필요는 없다.)  
공유변수 사용시 주의해야 한다.  
동시 요청을 위한 멀티 스레드 처리 지원해준다.  

## 동시요청 - 멀티 스레드

### 스레드
애플리케이션 코드를 하나하나 순차적으로 실행하는 것은 스레드이다.  
처음 main이라는 이름의 스레드가 생성된다.  

새로운 요청이 있을 때 새로운 스레드를 만들면 된다.  
하지만 요청 할때마다 생성한다면 리소스가 많이 낭비될 것이다.  
스레드가 너무 많이 생성된다면 CPU, 메모리 임계점을 넘어 서버가 죽을 수도 있다.  
이는 `스레드 풀`을 이용해서 해결할 수 있다.  

> WAS의 주요 튜닝은 max thread pool을 잘 설정하는 것이다.

## 정적 리소스
고정된 HTML, CSS, JS 이미지.  
동적으로 생성된 HTML을 생성해서 웹브라우져에 전달.  

## HTTP API
데이터만 주고 받음. UI 화면이 필요하면 클라이언트가 별도로 처리한다.  
앱, 웹 클라이언트, 서버 to 서버

## SSR(server side rendering)
서버에서 최종 HTML을 생성해서 클라이언트에 전달

## CSR(client side rendering)
HTML 결과를 자바스크립트를 사용해 웹 브라우저에서 동적으로 생성해서 사용