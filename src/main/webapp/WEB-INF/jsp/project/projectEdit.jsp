<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Редактировать проект</title>
    <meta charset="UTF-8">
</head>
<body>
<h2>Редактировать проект</h2>
<form method="post" action="projects/<%= request.getAttribute("projectId") %>?_method=PUT">
    <input type="hidden" name="id" value="<%= request.getAttribute("projectId") %>"/>

    <label for="name">Имя:</label>
    <input type="text" name="name" id="name" value="<%= request.getAttribute("projectName") %>" required><br>

    <label for="description">Описание:</label>
    <input type="text" name="description" id="description" value="<%= request.getAttribute("projectDescription") %>"><br>

    <label for="startDate">Дата начала:</label>
    <input type="date" name="startDate" id="startDate" value="<%= request.getAttribute("projectStartDate") %>"><br>

    <label for="endDate">Дата окончания:</label>
    <input type="date" name="endDate" id="endDate" value="<%= request.getAttribute("projectEndDate") %>"><br>

    <label>Статус:
        <select name="status">
            <option>активен</option>
            <option>на паузе</option>
            <option>завершён</option>
        </select>
    </label>

    <label for="managerId">Менеджер:</label>
    <select name="managerId" id="managerId" required>
        <c:forEach var="manager" items="${managers}">
            <option value="${manager.userId}" <c:if test="${manager.userId == projectManagerId}">selected</c:if>>
                    ${manager.name}
            </option>
        </c:forEach>
    </select>

    <button type="submit">Сохранить</button>
</form>
</body>
</html>
