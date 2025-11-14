<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создать спринт</title>
</head>
<body>
<h2>Создать спринт</h2>
<form action="${pageContext.request.contextPath}/sprint/create" method="post">
    <label>Проект:</label><br>
    <select name="projectId">
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}" ${project.projectId == sprint.projectId ? 'selected' : ''}>${project.name}</option>
        </c:forEach>
    </select>
    <br>

    <label>Название спринта:</label><br>
    <label>
        <input type="text" name="name" required>
    </label><br>

    <label>Дата начала:</label><br>
    <label>
        <input type="date" name="startDate" required>
    </label><br>
    <label>Дата окончания:</label><br>
    <label>
        <input type="date" name="endDate" required>
    </label><br>
    <input type="submit" value="Создать">
</form>
<br>
<a href="${pageContext.request.contextPath}/sprints">Назад к списку</a>
</body>
</html>
