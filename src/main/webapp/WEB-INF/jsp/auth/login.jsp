<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Вход</title></head>
<body>
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