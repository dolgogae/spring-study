<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <!-- action 맨 앞에 /가 있으면 절대경로가 된다. -->
    <!-- 없다면, 상대경로 현재 폴더에서 찾는다. -->
<form action="save" method="post"> 
    username: <input type="text" name="username" /> 
    age: <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>