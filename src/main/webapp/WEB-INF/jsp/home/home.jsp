<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Главная страница</title>
</head>
    <body>
        <h2>Добро пожаловать в SOUP!</h2>
        <p>Для работы в системе необходимо авторизоваться!<p>
        <ul>
            <li><a href="${pageContext.request.contextPath}/login">Вход</a></li>
            <li><a href="${pageContext.request.contextPath}/register">Регистрация</a></li>
            <li><a href="${pageContext.request.contextPath}/projects">Проекты</a></li>
            <li><a href="${pageContext.request.contextPath}/sprints">Спринты</a></li>
            <li><a href="${pageContext.request.contextPath}/tasks">Задачи</a></li>
            <li><a href="${pageContext.request.contextPath}/users">Пользователи</a></li>
            <li><a href="${pageContext.request.contextPath}/workload">Нагрузка</a></li>
        </ul>
    </body>
</html>
