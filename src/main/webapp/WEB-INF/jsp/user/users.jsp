<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Список пользователей</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom fixed-top shadow-sm navbar-custom">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">SOUP</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#navbarContent" aria-controls="navbarContent"
                aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/tasks">Задачи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/projects">Проекты</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/sprints">Спринты</a>
                </li>
                <c:if test="${isAdmin}">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page"
                           href="${pageContext.request.contextPath}/users">Пользователи</a>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav ms-auto">
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <span class="navbar-text me-3">ID: ${sessionScope.user.userId}</span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Выход</a>
                    </li>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Вход</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/register">Регистрация</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

<div class="container" style="margin-top: 90px;">
    <h1>Список пользователей</h1>

    <div id="errorDiv" class="error" style="display: none;"></div>

    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th>Имя</th>
                <th>Почта</th>
                <th>Роль</th>
                <th>Инфо</th>
                <th>Действия</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td>${user.name}</td>
                    <td>${user.email}</td>
                    <td>${roleMap[user.userId]}</td>
                    <td>${user.contactInfo}</td>
                    <td>
                        <button type="button" class="btn btn-danger btn-sm"
                                onclick="deleteUser(${user.userId})">
                            Удалить
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer>
    <div class="container">
        <p>&copy; 2025 Project Manager. Все права защищены.</p>
    </div>
</footer>

<script>
    function deleteUser(id) {
        if (!confirm("Вы уверены, что хотите удалить этого пользователя?")) {
            return;
        }

        fetch("${pageContext.request.contextPath}/user/delete", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
            body: "id=" + encodeURIComponent(id)
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    location.reload();
                } else {
                    const errorDiv = document.getElementById('errorDiv');
                    errorDiv.textContent = data.message;
                    errorDiv.style.display = 'block';
                }
            })
            .catch(() => {
                const errorDiv = document.getElementById('errorDiv');
                errorDiv.textContent = "Ошибка удаления. Повторите попытку.";
                errorDiv.style.display = 'block';
            });
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
