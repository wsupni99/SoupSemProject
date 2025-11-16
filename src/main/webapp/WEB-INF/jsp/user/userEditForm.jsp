<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Редактировать пользователя</title>
</head>
<body>
<h2>Пользователь: ${user.name}</h2>
<p>Email: ${user.email}</p>

<form action="${pageContext.request.contextPath}/user/update/${user.userId}" method="post">
    <label>Роль:</label>
    <select name="roleId" required>
        <c:forEach var="role" items="${roles}">
            <option value="${role.roleId}" <c:if test="${role.roleId == user.roleId}">selected</c:if>>${role.name}</option>
        </c:forEach>
    </select><br>
    <input type="submit" value="Сохранить роль">
</form>

<form action="${pageContext.request.contextPath}/user/delete/${user.userId}" method="post" onsubmit="return confirm('Удалить пользователя?');">
    <input type="submit" value="Удалить">
</form>

<a href="${pageContext.request.contextPath}/users">Назад</a>
</body>
</html>
