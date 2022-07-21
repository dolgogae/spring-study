<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // request, response 사용 가능

    MemberRepository memberRepository = MemberRepository.getInstance();

    String username = req.getParameter("username");
    int age = Integer.parseInt(req.getParameter("age"));

    Member member = new Member(username,age);
    memberRepository.save(member);
%>

<html>
<head>
    <title>Title</title>
</head>
<body>
    성공
    <ul>
        <!-- <li>id=<%=((Member)request.getAttribute("member")).getId()%></li>
        <li>username=<%=((Member)request.getAttribute("username)).getUsername()%></li>
        <li>age=<%=((Member)request.getAttribute("age")).getAge()%></li> -->
        <li>id=${member.id}</li>
        <li>username=${member.username}</li>
        <li>age=${member.age}</li>
    </ul>
    <a href="/index.html">메인</a>
</body>
</html>