<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="page-shell">
<form method="post" action="${pageContext.request.contextPath}/login">
    Email: <input type="text" name="email"><br>
    Пароль: <input type="password" name="password"><br>
    <button type="submit">Войти</button>
</form>
<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>

</body>
</html>
