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
    <input type="text" name="priority" value="${task.priority}" required><br>

    <label>Статус:</label><br>
    <input type="text" name="status" value="${task.status}" required><br>

    <label>Дедлайн:</label><br>
    <input type="date" name="deadline" value="<c:out value='${task.deadline}'/>"><br>

    <label>Родительская задача (опционально):</label><br>
    <input type="number" name="parentTaskId" id="parentTaskId" min="1" step="1" pattern="[0-9]*"
           value="${task.parentTaskId != null ? task.parentTaskId : ''}"><br>

    <label>Пользователь:</label><br>
    <input type="text" id="user-search" value="${taskUserName}" readonly>
    <input type="hidden" id="userIdInput" name="userId" value="${task.userId}" required>
    <select id="userList" size="5" onclick="selectUser()">
        <c:forEach var="user" items="${users}">
            <option value="${user.userId}" <c:if test="${user.userId == task.userId}">selected</c:if>>
                    ${user.name}
            </option>
        </c:forEach>
    </select>

    <br>
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
        var select = document.getElementById('userList');
        var chosen = select.options[select.selectedIndex];
        document.getElementById('user-search').value = chosen.text;
        document.getElementById('userIdInput').value = chosen.value;
    }
</script>
</body>
</html>
