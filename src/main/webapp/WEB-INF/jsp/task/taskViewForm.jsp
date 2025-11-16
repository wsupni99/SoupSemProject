<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Просмотр задачи</title>
</head>
<body>
<h2>Просмотр задачи</h2>

<c:if test="${not empty task}">
    <p><strong>ID:</strong> ${task.taskId}</p>
    <p><strong>Название:</strong> ${task.name}</p>
    <p><strong>Описание:</strong> ${task.description}</p>
    <p><strong>Статус:</strong> ${task.status}</p>
    <p><strong>Приоритет:</strong> ${task.priority}</p>
    <p><strong>Проект:</strong> ${projectName != null && !projectName.isEmpty() ? projectName : 'Не указан'}</p>
    <p><strong>Исполнитель:</strong> ${taskUserName != null && !taskUserName.isEmpty() ? taskUserName : 'Не назначен'}</p>
    <p><strong>Спринт:</strong> ${sprintName != null && !sprintName.isEmpty() ? sprintName : 'Не указан'}</p>

    <h3>Комментарии</h3>
    <c:forEach var="comment" items="${comments}">
        <c:set var="commentAuthor" value="Неизвестный" />
        <c:forEach var="user" items="${users}">
            <c:if test="${comment.userId == user.userId}">
                <c:set var="commentAuthor" value="${user.name}" />
            </c:if>
        </c:forEach>
        <div>
            <p><strong>${commentAuthor}:</strong> ${comment.text}</p>
            <p><em>(${comment.createdAt})</em></p>
        </div>
        <hr>
    </c:forEach>

    <h3>Добавить комментарий</h3>
    <form action="${pageContext.request.contextPath}/task/addComment" method="post">
        <input type="hidden" name="taskId" value="${task.taskId}">
        <textarea name="text" rows="4" cols="50" placeholder="Введите комментарий..." required></textarea><br><br>
        <input type="submit" value="Добавить комментарий">
    </form>
</c:if>

<c:if test="${empty task}">
    <p>Задача не найдена.</p>
</c:if>

<br>
<a href="${pageContext.request.contextPath}/tasks">Вернуться к списку задач</a>
</body>
</html>
