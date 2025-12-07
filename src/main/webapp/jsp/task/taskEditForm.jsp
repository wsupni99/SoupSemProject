<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <title>Редактирование задачи</title>
    <meta charset="UTF-8">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body class="form-page">
<div class="form-card task-edit-layout">
    <div class="task-edit-main">
        <div class="form-header">
            <h2 class="form-header-title">Редактирование задачи</h2>
        </div>

        <c:if test="${not empty error}">
            <div class="form-error">${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/task/update" method="post">
            <input type="hidden" name="taskId" value="${task.taskId}">
            <input type="hidden" name="projectId" value="${task.projectId}">
            <input type="hidden" name="createdAt" value="${task.createdAt}">
            <input type="hidden" name="updatedAt" value="${task.updatedAt}">

            <div class="form-grid-2">
                <div class="form-group">
                    <label class="form-label">Спринт:</label>
                    <select class="form-select" name="sprintId" required>
                        <c:forEach var="sprint" items="${sprints}">
                            <option value="${sprint.sprintId}"
                                    <c:if test="${sprint.sprintId == task.sprintId}">selected</c:if>>
                                    ${sprint.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Пользователь:</label>
                    <select class="form-select" name="userId" required>
                        <c:forEach var="user" items="${users}">
                            <option value="${user.userId}"
                                    <c:if test="${user.userId == task.userId}">selected</c:if>>
                                    ${user.name}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Название задачи:</label>
                    <input class="form-input" type="text" name="name" value="${task.name}" required>
                </div>

                <div class="form-group">
                    <label class="form-label">Приоритет:</label>
                    <select class="form-select" name="priority" required>
                        <option value="низкий"    <c:if test="${task.priority == 'низкий'}">selected</c:if>>Низкий</option>
                        <option value="средний"   <c:if test="${task.priority == 'средний'}">selected</c:if>>Средний</option>
                        <option value="высокий"   <c:if test="${task.priority == 'высокий'}">selected</c:if>>Высокий</option>
                        <option value="критичный" <c:if test="${task.priority == 'критичный'}">selected</c:if>>Критичный</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Статус:</label>
                    <select class="form-select" name="status" required>
                        <option value="новая"     <c:if test="${task.status == 'новая'}">selected</c:if>>Новая</option>
                        <option value="в работе"  <c:if test="${task.status == 'в работе'}">selected</c:if>>В работе</option>
                        <option value="тест"      <c:if test="${task.status == 'тест'}">selected</c:if>>Тест</option>
                        <option value="готова"    <c:if test="${task.status == 'готова'}">selected</c:if>>Готова</option>
                        <option value="отложена"  <c:if test="${task.status == 'отложена'}">selected</c:if>>Отложена</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Дедлайн:</label>
                    <input class="form-input" type="date" name="deadline" value="${task.deadline}">
                </div>

                <div class="form-group">
                    <label class="form-label">Родительская задача:</label>
                    <select class="form-select" name="parentTaskId">
                        <option value="0"
                                <c:if test="${task.parentTaskId == null || task.parentTaskId == 0}">selected</c:if>>
                            Нет
                        </option>
                        <c:forEach var="t" items="${projectTasks}">
                            <c:if test="${t.taskId != task.taskId}">
                                <option value="${t.taskId}"
                                        <c:if test="${t.taskId == task.parentTaskId}">selected</c:if>>
                                    [${t.taskId}] ${t.name}
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">Описание:</label>
                    <textarea class="form-textarea" name="description" required>${task.description}</textarea>
                </div>
            </div>

            <div class="form-actions">
                <button type="button" class="btn btn-secondary" onclick="history.back()">Назад</button>
                <input type="submit" value="Сохранить" class="btn btn-primary">
            </div>
        </form>
    </div>
</div>

<div class="task-edit-comments">
    <div class="task-comments-card">
        <h3 class="comments-header">Комментарии</h3>

        <c:if test="${empty comments}">
            <p class="comment-text">Нет комментариев</p>
        </c:if>

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
                        <p class="comment-text task-comment-text">${comment.text}</p>
                    </li>
                </c:forEach>
            </ul>
        </div>

        <h3 class="comments-header" style="margin-top: 12px;">Добавить комментарий</h3>
        <form method="post" action="${pageContext.request.contextPath}/task/addComment" class="task-comment-form">
            <input type="hidden" name="taskId" value="${task.taskId}">
            <div class="form-group">
                <label>
                    <textarea class="form-textarea" name="text" required placeholder="Текст комментария"></textarea>
                </label>
            </div>
            <div class="form-actions" style="justify-content: flex-end;">
                <input type="submit" value="Добавить комментарий" class="btn btn-primary btn-comment-submit">
            </div>
        </form>
    </div>
</div>
</body>
</html>
