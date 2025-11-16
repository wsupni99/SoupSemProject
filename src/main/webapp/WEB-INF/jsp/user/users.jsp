<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список пользователей</title>
    <script>
        function deleteUser(id) {
            if (confirm("Вы уверены, что хотите удалить этого пользователя?")) {
                fetch("${pageContext.request.contextPath}/user/delete", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "id=" + id
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
                    .catch(error => {
                        const errorDiv = document.getElementById('errorDiv');
                        errorDiv.textContent = 'An error occurred during deletion. Please try again.';
                        errorDiv.style.display = 'block';
                    });
            }
        }
    </script>
</head>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>[Название страницы]</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom">
    <div class="container-fluid">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/home">SOUP</a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="${pageContext.request.contextPath}/home">Главная</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/tasks">Задачи</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/projects">Проекты</a>
            <a class="nav-link" href="${pageContext.request.contextPath}/sprints">Спринты</a>
            <c:if test="${isAdmin || isManager}">
                <a class="nav-link" href="${pageContext.request.contextPath}/users">Пользователи</a>
            </c:if>
            <c:if test="${not empty sessionScope.user}">
                <span class="navbar-text">ID: ${sessionScope.user.userId}</span>
                <a class="nav-link" href="${pageContext.request.contextPath}/logout">Выход</a>
            </c:if>
            <c:if test="${empty sessionScope.user}">
                <a class="nav-link" href="${pageContext.request.contextPath}/login">Вход</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container">
    <h1>Список пользователей</h1>
    <table border="1">
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
                <td><button onclick="deleteUser(${user.userId})">Удалить</button></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

</html>
