<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Пользователи</title></head>
<body>
<p>Jsp работает</p>
<h2>Список пользователей</h2>
<table>
    <tr><th>ID</th><th>Имя</th><th>Email</th></tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.userId}</td>
            <td>${user.name}</td>
            <td>${user.email}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
