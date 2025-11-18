<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создание задачи</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Создание задачи</h2>
    </div>

    <form action="${pageContext.request.contextPath}/task/create" method="post"
          onsubmit="return checkTaskFields();">
        <input type="hidden" name="projectId" value="${projectId}" />

        <div class="form-grid-2">
            <div class="form-group">
                <label class="form-label">Спринт:</label>
                <select class="form-select" name="sprintId" required>
                    <c:forEach var="sprint" items="${sprints}">
                        <option value="${sprint.sprintId}">${sprint.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Пользователь:</label>
                <select class="form-select" name="userId" required>
                    <c:forEach var="user" items="${users}">
                        <option value="${user.userId}">${user.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Название задачи:</label>
                <input class="form-input" type="text" name="name" required>
            </div>

            <div class="form-group">
                <label class="form-label">Приоритет:</label>
                <select class="form-select" name="priority" required>
                    <option value="низкий">Низкий</option>
                    <option value="средний">Средний</option>
                    <option value="высокий">Высокий</option>
                    <option value="критичный">Критичный</option>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Статус:</label>
                <select class="form-select" name="status" required>
                    <option value="новая">Новая</option>
                    <option value="в работе">В работе</option>
                    <option value="тест">Тест</option>
                    <option value="готова">Готова</option>
                    <option value="отложена">Отложена</option>
                </select>
            </div>

            <div class="form-group form-small">
                <label class="form-label">Дедлайн:</label>
                <input class="form-input" type="date" name="deadline" required>
            </div>

            <div class="form-group form-small">
                <label class="form-label">Родительская задача (опционально):</label>
                <input class="form-input" type="number" name="parentTaskId" value="0">
            </div>
        </div>

        <div class="form-grid-1">
            <div class="form-group">
                <label class="form-label">Описание:</label>
                <textarea class="form-textarea" name="description" required></textarea>
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="Создать" class="btn btn-primary">
        </div>
    </form>
</div>

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
