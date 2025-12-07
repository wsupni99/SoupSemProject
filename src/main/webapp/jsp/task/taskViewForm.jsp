<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Просмотр задачи</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="form-card task-edit-layout">
    <div class="task-edit-main">
        <div class="form-header">
            <h2 class="form-header-title">Просмотр задачи</h2>
        </div>

        <c:if test="${not empty task}">
            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Спринт:</label>
                    <div class="field-static">
                        <c:choose>
                            <c:when test="${not empty sprintName}">
                                ${sprintName}
                            </c:when>
                            <c:otherwise>Не указан</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Пользователь:</label>
                    <div class="field-static">
                        <c:choose>
                            <c:when test="${not empty taskUserName}">
                                ${taskUserName}
                            </c:when>
                            <c:otherwise>Не назначен</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Название задачи:</label>
                    <div class="field-static">${task.name}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">Приоритет:</label>
                    <div class="field-static">${task.priority}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">Статус:</label>
                    <div class="field-static">${task.status}</div>
                </div>

                <div class="form-group">
                    <label class="form-label">Проект:</label>
                    <div class="field-static">
                        <c:choose>
                            <c:when test="${not empty projectName}">
                                ${projectName}
                            </c:when>
                            <c:otherwise>Не указан</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Дедлайн:</label>
                    <div class="field-static">
                        <c:choose>
                            <c:when test="${not empty task.deadline}">
                                ${task.deadline}
                            </c:when>
                            <c:otherwise>Не указан</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Родительская задача:</label>
                    <div class="field-static">
                        <c:choose>
                            <c:when test="${task.parentTaskId != null && task.parentTaskId != 0}">
                                ${task.parentTaskId}
                            </c:when>
                            <c:otherwise>Нет</c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">Описание:</label>
                    <div class="field-static multiline">
                        <c:out value="${task.description}"/>
                    </div>
                </div>
            </div>
            <a href="${pageContext.request.contextPath}/tasks" class="btn btn-secondary">Назад</a>
        </c:if>

        <c:if test="${empty task}">
            <p class="field-static">Задача не найдена.</p>
        </c:if>
    </div>
</div>

<div class="task-edit-comments">
    <div class="task-comments-card">
        <h3 class="comments-header">Комментарии</h3>

        <c:if test="${empty comments}">
            <p class="comment-text">Нет комментариев</p>
        </c:if>

        <c:if test="${not empty comments}">
            <div class="task-comments-list">
                <ul class="comments-list">
                    <c:forEach var="comment" items="${comments}">
                        <li class="task-comment-item">
                            <p class="comment-meta">
                                <b>
                                    <c:forEach var="user" items="${users}">
                                        <c:if test="${user.userId == comment.userId}">
                                            ${user.name}
                                        </c:if>
                                    </c:forEach>
                                </b>
                                <span class="task-comment-date">(${comment.createdAt})</span>
                            </p>
                            <p class="comment-text task-comment-text">
                                    ${comment.text}
                            </p>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <h3 class="comments-header" style="margin-top: 12px;">Добавить комментарий</h3>
        <form method="post"
              action="${pageContext.request.contextPath}/task/addComment"
              class="task-comment-form">
            <input type="hidden" name="taskId" value="${task.taskId}">
            <div class="form-group">
                    <textarea class="form-textarea"
                              name="text"
                              required
                              placeholder="Текст комментария"></textarea>
            </div>
            <div class="form-actions" style="justify-content: flex-end;">
                <input type="submit"
                       value="Добавить комментарий"
                       class="btn btn-primary btn-comment-submit">
            </div>
        </form>
    </div>
</div>
</body>
</html>
