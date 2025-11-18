<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Регистрация</title>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body class="auth-page">
<div class="auth-wrapper">
    <div class="auth-card">
        <h1 class="auth-title">Создание аккаунта</h1>
        <p class="auth-subtitle">Подключите команду к системе управления проектами</p>

        <c:if test="${not empty error}">
            <div class="error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="auth-form-row">
                <label class="auth-form-label" for="name">Имя</label>
                <input class="auth-form-input" type="text" id="name" name="name" required>
            </div>

            <div class="auth-form-row">
                <label class="auth-form-label" for="email">Email</label>
                <input class="auth-form-input" type="email" id="email" name="email" required>
            </div>

            <div class="auth-form-row">
                <label class="auth-form-label" for="password">Пароль</label>
                <input class="auth-form-input" type="password" id="password" name="password" required>
            </div>

            <div class="auth-form-row">
                <label class="auth-form-label" for="roleSelect">Роль</label>
                <select class="auth-form-input" id="roleSelect" name="role" required>
                    <option value="TESTER">Тестировщик</option>
                    <option value="DEVELOPER">Разработчик</option>
                    <option value="MANAGER">Менеджер</option>
                </select>
            </div>

            <div class="auth-form-row" id="projectDiv" style="display: none;">
                <label class="auth-form-label" for="projectId">Проект</label>
                <select class="auth-form-input" id="projectId" name="projectId">
                    <c:forEach var="project" items="${projects}">
                        <option value="${project.projectId}">${project.name}</option>
                    </c:forEach>
                </select>
            </div>

            <button type="submit" class="auth-submit">Зарегистрироваться</button>

            <p class="auth-note">Уже есть аккаунт?</p>
            <a href="${pageContext.request.contextPath}/login" class="auth-link">Войти</a>
        </form>
    </div>
</div>

<script>
    const roleSelect = document.getElementById('roleSelect');
    const projectDiv = document.getElementById('projectDiv');

    roleSelect.addEventListener('change', function () {
        if (this.value === 'MANAGER') {
            projectDiv.style.display = 'block';
        } else {
            projectDiv.style.display = 'none';
        }
    });

    if (roleSelect.value === 'MANAGER') {
        projectDiv.style.display = 'block';
    }
</script>
</body>
</html>
