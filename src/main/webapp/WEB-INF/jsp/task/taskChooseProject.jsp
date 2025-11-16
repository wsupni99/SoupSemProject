<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Выбор проекта</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/task/newWithProject" method="get">
    <label for="projectId">Проект:</label>
    <select name="id" id="projectId" required>
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}">${project.name}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Дальше">
</form>
</body>
</html>
