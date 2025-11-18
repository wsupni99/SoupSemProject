<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Просмотр задачи</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body class="page">
<div class="card task-view-layout">
    <div class="task-view-main">
        <div class="card-header">
            <h2 class="card-title">Просмотр задачи</h2>
        </div>

        <c:if test="${not empty task}">
            <p class="field-row"><span class="field-label">ID:</span> ${task.taskId}</p>
            <p class="field-row"><span class="field-label">Название:</span> ${task.name}</p>
            <p class="field-row"><span class="field-label">Описание:</span> ${task.description}</p>
            <p class="field-row"><span class="field-label">Статус:</span> ${task.status}</p>
            <p class="field-row"><span class="field-label">Приоритет:</span> ${task.priority}</p>
            <p class="field-row">
                <span class="field-label">Проект:</span>
                    ${projectName != null && !projectName.isEmpty() ? projectName : 'Не указан'}
            </p>
            <p class="field-row">
                <span class="field-label">Исполнитель:</span>
                    ${taskUserName != null && !taskUserName.isEmpty() ? taskUserName : 'Не назначен'}
            </p>
            <p class="field-row">
                <span class="field-label">Спринт:</span>
                    ${sprintName != null && !sprintName.isEmpty() ? sprintName : 'Не указан'}
            </p>
        </c:if>

        <c:if test="${empty task}">
            <p class="field-row">Задача не найдена.</p>
        </c:if>

        <div class="link-wrapper">
            <a href="${pageContext.request.contextPath}/tasks" class="btn-link">Вернуться к списку задач</a>
        </div>
    </div>

    <c:if test="${not empty task}">
        <div class="task-view-comments">
            <div class="task-comments-card">
                <h3 class="comments-title">Комментарии</h3>

                <div class="task-comments-list">
                    <c:if test="${empty comments}">
                        <p style="font-size: 13px; color: #6b778c; margin: 0;">Нет комментариев</p>
                    </c:if>

                    <c:forEach var="comment" items="${comments}">
                        <c:set var="commentAuthor" value="Неизвестный"/>
                        <c:forEach var="user" items="${users}">
                            <c:if test="${comment.userId == user.userId}">
                                <c:set var="commentAuthor" value="${user.name}"/>
                            </c:if>
                        </c:forEach>

                        <div class="task-comment-item">
                            <div class="task-comment-author">${commentAuthor}</div>
                            <div class="task-comment-date">${comment.createdAt}</div>
                            <div class="task-comment-text">${comment.text}</div>
                        </div>
                    </c:forEach>
                </div>

                <h3 class="comment-form-title">Добавить комментарий</h3>
                <form action="${pageContext.request.contextPath}/task/addComment" method="post" class="task-comment-form">
                    <input type="hidden" name="taskId" value="${task.taskId}">
                    <textarea class="textarea" name="text" rows="4"
                              placeholder="Введите комментарий..." required></textarea>
                    <br>
                    <input type="submit" value="Добавить комментарий" class="btn btn-primary btn-comment-submit">
                </form>
            </div>
        </div>
    </c:if>
</div>
</body>
</html>
