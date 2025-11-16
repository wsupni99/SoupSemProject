<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Редактирование задачи</title>
    <meta charset="UTF-8">
</head>
<body>
<h2>Редактирование задачи</h2>
<c:if test="${not empty error}">
    <div style="color:red;">${error}</div>
</c:if>
<form action="${pageContext.request.contextPath}/task/update" method="post">
    <input type="hidden" name="taskId" value="${task.taskId}">
    <input type="hidden" name="projectId" value="${task.projectId}">
    <input type="hidden" name="createdAt" value="${task.createdAt}">
    <input type="hidden" name="updatedAt" value="${task.updatedAt}">

    <label>Спринт:</label><br>
    <select name="sprintId" required>
        <c:forEach var="sprint" items="${sprints}">
            <option value="${sprint.sprintId}" <c:if test="${sprint.sprintId == task.sprintId}">selected</c:if>>${sprint.name}</option>
        </c:forEach>
    </select><br>

    <label>Название задачи:</label><br>
    <input type="text" name="name" value="${task.name}" required><br>

    <label>Описание:</label><br>
    <textarea name="description" required>${task.description}</textarea><br>

    <label>Приоритет:</label><br>
    <select name="priority" required>
        <option value="низкий" <c:if test="${task.priority == 'низкий'}">selected</c:if>>Низкий</option>
        <option value="средний" <c:if test="${task.priority == 'средний'}">selected</c:if>>Средний</option>
        <option value="высокий" <c:if test="${task.priority == 'высокий'}">selected</c:if>>Высокий</option>
        <option value="критичный" <c:if test="${task.priority == 'критичный'}">selected</c:if>>Критичный</option>
    </select><br>

    <label>Статус:</label><br>
    <select name="status" required>
        <option value="новая" <c:if test="${task.status == 'новая'}">selected</c:if>>Новая</option>
        <option value="в работе" <c:if test="${task.status == 'в работе'}">selected</c:if>>В работе</option>
        <option value="тест" <c:if test="${task.status == 'тест'}">selected</c:if>>Тест</option>
        <option value="готова" <c:if test="${task.status == 'готова'}">selected</c:if>>Готова</option>
        <option value="отложена" <c:if test="${task.status == 'отложена'}">selected</c:if>>Отложена</option>
    </select><br>

    <label>Дедлайн:</label><br>
    <input type="date" name="deadline" value="${task.deadline}"><br>

    <label>Родительская задача (опционально):</label><br>
    <input type="number" name="parentTaskId" value="${task.parentTaskId != null ? task.parentTaskId : ''}"><br>

    <label>Пользователь:</label><br>
    <select name="userId" required>
        <c:forEach var="user" items="${users}">
            <option value="${user.userId}" <c:if test="${user.userId == task.userId}">selected</c:if>>${user.name}</option>
        </c:forEach>
    </select><br>

    <input type="submit" value="Сохранить">
    <input type="button" value="Назад" onclick="history.back();">
</form>


<h3>Комментарии</h3>
<c:if test="${empty comments}">
    <p>Нет комментариев</p>
</c:if>
<ul>
    <c:forEach var="comment" items="${comments}">
        <li>
            <p>
                <b>
                    <c:forEach var="user" items="${users}">
                        <c:if test="${user.userId == comment.userId}">
                            ${user.name}
                        </c:if>
                    </c:forEach>
                </b> (${comment.createdAt})
            </p>
            <p>${comment.text}</p>
        </li>
    </c:forEach>
</ul>

<h3>Добавить комментарий</h3>
<form method="post" action="${pageContext.request.contextPath}/task/addComment">
    <input type="hidden" name="taskId" value="${task.taskId}">
    <textarea name="text" required placeholder="Текст комментария"></textarea><br>
    <input type="submit" value="Добавить комментарий">
</form>



<script>
    document.getElementById('parentTaskId').addEventListener('input', function () {
        if (this.value === '0') this.value = '';
    });
    function selectUser() {
        const select = document.getElementById('userList');
        const chosen = select.options[select.selectedIndex];
        document.getElementById('user-search').value = chosen.text;
        document.getElementById('userIdInput').value = chosen.value;
    }
</script>
</body>
</html>
