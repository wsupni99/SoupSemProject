<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Tasks List</title>
</head>
<body>
<h2>Tasks List</h2>

<!-- Ссылка на создание, только для admin/manager -->
<c:if test="${isAdmin || isManager}">
    <a href="${pageContext.request.contextPath}/taskNewForm">Создать задачу</a><br><br>
</c:if>

<a href="${pageContext.request.contextPath}/home">Вернуться домой</a>

<form method="get" action="${pageContext.request.contextPath}/tasks">
    <select name="user_id">
        <option value="">Все пользователи</option>
        <c:forEach var="user" items="${users}">
            <option value="${user.userId}" <c:if test="${param.user_id == user.userId}">selected</c:if>>
                    ${user.name}
            </option>
        </c:forEach>
    </select>

    <select name="project_id">
        <option value="">Все проекты</option>
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}" <c:if test="${param.project_id == project.projectId}">selected</c:if>>
                    ${project.name}
            </option>
        </c:forEach>
    </select>

    <select name="sprint_id">
        <option value="">Все спринты</option>
        <c:forEach var="sprint" items="${sprints}">
            <option value="${sprint.sprintId}" <c:if test="${param.sprint_id == sprint.sprintId}">selected</c:if>>
                    ${sprint.name}
            </option>
        </c:forEach>
    </select>

    <select name="status">
        <option value="">Все статусы</option>
        <option value="новая" <c:if test="${param.status == 'новая'}">selected</c:if>>новая</option>
        <option value="в работе" <c:if test="${param.status == 'в работе'}">selected</c:if>>в работе</option>
        <option value="готова" <c:if test="${param.status == 'готова'}">selected</c:if>>готова</option>
        <option value="отложена" <c:if test="${param.status == 'отложена'}">selected</c:if>>отложена</option>
        <option value="тест" <c:if test="${param.status == 'тест'}">selected</c:if>>тест</option>
    </select>

    <input type="submit" value="Применить фильтры" />
</form>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>Имя</th>
        <th>Проект</th>
        <th>Пользователь</th>
        <th>Спринт</th>
        <th>Статус</th>
        <c:if test="${isAdmin || isManager}">
            <th>Действия</th>
        </c:if>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="task" items="${tasks}">
        <tr>
            <td>${task.taskId}</td>
            <td><a href="${pageContext.request.contextPath}/task?id=${task.taskId}">${task.name}</a></td>
            <td>
                <c:forEach var="project" items="${projects}">
                    <c:if test="${project.projectId == task.projectId}">${project.name}</c:if>
                </c:forEach>
            </td>
            <td>
                <c:forEach var="user" items="${users}">
                    <c:if test="${user.userId == task.userId}">${user.name}</c:if>
                </c:forEach>
            </td>
            <td>
                <c:forEach var="sprint" items="${sprints}">
                    <c:if test="${sprint.sprintId == task.sprintId}">${sprint.name}</c:if>
                </c:forEach>
            </td>
            <td>${task.status}</td>
            <c:if test="${isAdmin || isManager}">
                <td>
                    <a href="${pageContext.request.contextPath}/task/delete?id=${task.taskId}" onclick="return confirm('Удалить задачу?');">Удалить</a>
                </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
