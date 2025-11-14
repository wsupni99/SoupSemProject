<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Регистрация</title></head>
<body>
<form method="post" action="${pageContext.request.contextPath}/register">
    Email: <input type="text" name="email"><br>
    Имя: <input type="text" name="name"><br>
    Пароль: <input type="password" name="password"><br>
    <button type="submit">Зарегистрироваться</button>
</form>
<c:if test="${not empty error}">
    <div style="color:red">${error}</div>
</c:if>
</body>
</html>
