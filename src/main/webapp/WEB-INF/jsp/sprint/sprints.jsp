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
    <p><a href="${pageContext.request.contextPath}/home">На главную</a> <a href="${pageContext.request.contextPath}/sprint/new">Создать спринт</a></p>
    <h2>Список спринтов</h2>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Project</th>
            <th>Start Date</th>
            <th>End Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="sprint" items="${sprints}">
            <tr>
                <td>
                    <a href="${pageContext.request.contextPath}/sprint/edit?id=${sprint.sprintId}">
                            ${sprint.name}
                    </a>
                </td>
                <td>${projectNames[sprint.sprintId]}</td>
                <td>${sprint.startDate}</td>
                <td>${sprint.endDate}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
