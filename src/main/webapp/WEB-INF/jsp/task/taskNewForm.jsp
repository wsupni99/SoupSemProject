<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создание задачи</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/task/create" method="post" onsubmit="return checkTaskFields();">
    <input type="hidden" name="projectId" value="${projectId}" />
    <label>Спринт:</label>
    <select name="sprintId" required>
        <c:forEach var="sprint" items="${sprints}">
            <option value="${sprint.sprintId}">${sprint.name}</option>
        </c:forEach>
    </select><br>
    <label>Название задачи:</label>
    <input type="text" name="name" required><br>
    <label>Описание:</label>
    <textarea name="description" required></textarea><br>
    <label>Приоритет:</label>
    <input type="text" name="priority" required><br>
    <label>Статус:</label>
    <input type="text" name="status" required><br>
    <label>Дедлайн:</label>
    <input type="date" name="deadline" required><br>
    <label>Родительская задача (опционально):</label>
    <input type="number" name="parentTaskId" value="0"><br>
    <label>Пользователь:</label>
    <select name="userId" required>
        <c:forEach var="user" items="${users}">
            <option value="${user.userId}">${user.name}</option>
        </c:forEach>
    </select><br>
    <input type="submit" value="Создать">
</form>
<script>
    function checkTaskFields() {
        const required = ['sprintId', 'name', 'description', 'priority', 'status', 'deadline', 'userId'];
        for (let f of required) {
            let el = document.forms[0][f];
            if (!el.value) {
                alert('Заполните все обязательные поля!');
                el.focus();
                return false;
            }
        }
        return true;
    }
</script>
</body>
</html>
