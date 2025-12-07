<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Список задач</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom fixed-top shadow-sm navbar-custom">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">SOUP</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
                aria-controls="navbarContent" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page"
                       href="${pageContext.request.contextPath}/tasks">Задачи</a>
                </li>
                <c:if test="${isAdmin || isManager}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/projects">Проекты</a>
                    </li>
                </c:if>
                <c:if test="${isAdmin || isManager}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/sprints">Спринты</a>
                    </li>
                </c:if>
                <c:if test="${isAdmin}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users">Пользователи</a>
                    </li>
                    <li class="nav-item">
                </c:if>
            </ul>

            <ul class="navbar-nav ms-auto">
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <span class="navbar-text me-3">Привет, ID: ${sessionScope.user.userId}</span>
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


<div class="container">
    <div class="table-container">
        <h2>Список задач</h2>

        <c:if test="${isAdmin || isManager}">
            <a href="${pageContext.request.contextPath}/task/new" class="btn btn-primary">Создать задачу</a><br><br>
        </c:if>

        <form method="get" action="${pageContext.request.contextPath}/tasks" class="filters">
            <select name="user_id">
                <option value="">Все пользователи</option>
                <c:forEach var="user" items="${users}">
                    <option value="${user.userId}" <c:if test="${param.user_id == user.userId}">selected</c:if>>
                            ${user.name}
                    </option>
                </c:forEach>
            </select>

            <select name="project_id">
                <option value="">Все проекты</option>
                <c:forEach var="project" items="${projects}">
                    <option value="${project.projectId}" <c:if test="${param.project_id == project.projectId}">selected</c:if>>
                            ${project.name}
                    </option>
                </c:forEach>
            </select>

            <select name="sprint_id">
                <option value="">Все спринты</option>
                <c:forEach var="sprint" items="${sprints}">
                    <option value="${sprint.sprintId}" <c:if test="${param.sprint_id == sprint.sprintId}">selected</c:if>>
                            ${sprint.name}
                    </option>
                </c:forEach>
            </select>

            <select name="status">
                <option value="">Все статусы</option>
                <option value="новая" <c:if test="${param.status == 'новая'}">selected</c:if>>новая</option>
                <option value="в работе" <c:if test="${param.status == 'в работе'}">selected</c:if>>в работе</option>
                <option value="готова" <c:if test="${param.status == 'готова'}">selected</c:if>>готова</option>
                <option value="отложена" <c:if test="${param.status == 'отложена'}">selected</c:if>>отложена</option>
                <option value="тест" <c:if test="${param.status == 'тест'}">selected</c:if>>тест</option>
            </select>

            <input type="submit" value="Применить фильтры" />
        </form>

        <table>
            <thead>
            <tr>
                <th>Имя</th>
                <th>Проект</th>
                <th>Пользователь</th>
                <th>Спринт</th>
                <th>Статус</th>
                <c:if test="${isAdmin || isManager}">
                    <th>Действия</th>
                </c:if>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="task" items="${tasks}">
                <tr>
                    <td><a href="${pageContext.request.contextPath}/task?id=${task.taskId}">${task.name}</a></td>
                    <td>
                        <c:forEach var="project" items="${projects}">
                            <c:if test="${project.projectId == task.projectId}">${project.name}</c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="user" items="${users}">
                            <c:if test="${user.userId == task.userId}">${user.name}</c:if>
                        </c:forEach>
                    </td>
                    <td>
                        <c:forEach var="sprint" items="${sprints}">
                            <c:if test="${sprint.sprintId == task.sprintId}">${sprint.name}</c:if>
                        </c:forEach>
                    </td>
                    <td>${task.status}</td>
                    <c:if test="${isAdmin || isManager}">
                        <td>
                            <a href="${pageContext.request.contextPath}/task/delete?id=${task.taskId}"
                               onclick="return confirm('Удалить задачу?');">
                                Удалить
                            </a>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
