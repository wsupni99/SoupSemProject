<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head><title>Выбор проекта</title></head>
<body>
<form action="${pageContext.request.contextPath}/task/chooseProject" method="post">
    <label for="projectId">Проект:</label>
    <select name="projectId" id="projectId" required>
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}">${project.name}</option>
        </c:forEach>
    </select>
    <input type="submit" value="Дальше">
</form>
</body>
</html>
