<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Регистрация</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body class="form-page">
<div class="page-shell">
    <header class="page-nav">
        <div class="nav-brand">
            <div class="nav-brand-dot"></div>
            <span>PROJECTS • SYSTEM</span>
        </div>
        <div class="nav-actions">
            <a href="${pageContext.request.contextPath}/login" class="nav-btn">Вход</a>
        </div>
    </header>

    <main class="page-main">
        <div class="form-card">
            <div class="form-card-inner">
                <header class="form-header">
                    <h1 class="form-title">Создание аккаунта</h1>
                    <p class="form-subtitle">Подключите команду к системе управления проектами</p>
                </header>

                <c:if test="${not empty error}">
                    <div class="error">${error}</div>
                </c:if>

                <section class="form-body">
                    <form method="post" action="${pageContext.request.contextPath}/register">
                        <div class="form-row">
                            <label class="form-label" for="name">Имя</label>
                            <input class="form-input" type="text" id="name" name="name" required>
                        </div>

                        <div class="form-row">
                            <label class="form-label" for="email">Email</label>
                            <input class="form-input" type="email" id="email" name="email" required>
                        </div>

                        <div class="form-row">
                            <label class="form-label" for="password">Пароль</label>
                            <input class="form-input" type="password" id="password" name="password" required>
                        </div>

                        <div class="form-row">
                            <label class="form-label" for="roleSelect">Роль</label>
                            <select class="form-input" id="roleSelect" name="role" required>
                                <option value="TESTER">Тестировщик</option>
                                <option value="DEVELOPER">Разработчик</option>
                                <option value="MANAGER">Менеджер</option>
                            </select>
                        </div>

                        <div class="form-row" id="projectDiv" style="display: none;">
                            <label class="form-label" for="projectId">Проект</label>
                            <select class="form-input" id="projectId" name="projectId">
                                <c:forEach var="project" items="${projects}">
                                    <option value="${project.projectId}">${project.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-footer">
                            <button type="submit" class="btn-primary form-submit">
                                Зарегистрироваться
                            </button>
                            <p class="form-footer-note">
                                Уже есть аккаунт?
                            </p>
                            <div class="form-nav">
                                <a href="${pageContext.request.contextPath}/login" class="form-nav-primary">
                                    Войти
                                </a>
                            </div>
                        </div>
                    </form>
                </section>
            </div>
        </div>
    </main>
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

    // чтобы при возврате на форму по валидации всё работало
    if (roleSelect.value === 'MANAGER') {
        projectDiv.style.display = 'block';
    }
</script>
</body>
</html>
