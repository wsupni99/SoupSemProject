<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Пользователи</title>
</head>
<body>
<h2>Список пользователей</h2>
<table border="1">
    <tr>
        <th>Имя</th>
        <th>Роль</th>
        <th>Инфо (Email)</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.name}</td>
            <td>${user.role.name}</td>
            <td>${user.email}</td>
            <td>
                <a href="${pageContext.request.contextPath}/user/edit/${user.userId}">Редактировать</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
