<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Задачи</title>
    <meta charset="UTF-8">
</head>
<body>
    <p><a href="${pageContext.request.contextPath}/home">На главную</a>
        <a href="${pageContext.request.contextPath}/task/new">Создать задачу</a>
    </p>
    <h2>Список задач</h2>
    <table>
        <tr><th>ID</th><th>Имя</th><th>Проект</th><th>Пользователь</th><th>Статус</th></tr>
        <c:forEach var="task" items="${tasks}">
            <tr>
                <td>${task.taskId}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/task/edit?id=${task.taskId}">
                            ${task.name}
                    </a>
                </td>
                <td>${task.projectId}</td>
                <td>${task.userId}</td>
                <td>${task.status}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
