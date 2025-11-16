<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Проекты</title>
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
    <h2>Список проектов</h2>
    <a href="${pageContext.request.contextPath}/project/new">Создать проект</a></p>
    <table>
        <tr>
            <th>ID</th><th>Имя</th><th>Описание</th><th>Дата начала</th>
            <th>Дата окончания</th><th>Статус</th><th>Менеджер</th>
        </tr>
        <c:forEach var="project" items="${projects}">
            <tr>
                <td>${project.projectId}</td>
                <td>
                    <a href="project/edit?id=${project.projectId}">${project.name}</a>
                </td>
                <td>${project.description}</td>
                <td>${project.startDate}</td>
                <td>${project.endDate}</td>
                <td>${project.status}</td>
                <td>${managerNames[project.managerId]}</td>
            </tr>
        </c:forEach>
    </table>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
