<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Вход</title>
    <meta charset="UTF-8">
    <title>Список задач</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-card">
        <h1 class="auth-title">Вход в систему</h1>
        <p class="auth-subtitle">Управляйте проектами и задачами в одном месте</p>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="auth-form-row">
                <label class="auth-form-label" for="email">Email</label>
                <input class="auth-form-input" type="email" id="email" name="email" required>
            </div>

            <div class="auth-form-row">
                <label class="auth-form-label" for="password">Пароль</label>
                <input class="auth-form-input" type="password" id="password" name="password" required>
            </div>

            <button type="submit" class="auth-submit">Войти</button>

            <p class="auth-note">Нет аккаунта?</p>
            <a href="${pageContext.request.contextPath}/register" class="auth-link">Создать аккаунт</a>
        </form>
    </div>
</div>
</body>
</html>
