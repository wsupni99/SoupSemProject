<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Задачи</title></head>
<body>
<p>Jsp работает</p>
<h2>Список задач</h2>
<table>
    <tr><th>ID</th><th>Имя</th><th>Проект</th><th>Пользователь</th><th>Статус</th></tr>
    <c:forEach var="task" items="${tasks}">
        <tr>
            <td>${task.taskId}</td>
            <td>${task.name}</td>
            <td>${task.projectId}</td>
            <td>${task.userId}</td>
            <td>${task.status}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
