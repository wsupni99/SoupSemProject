<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Спринты</title></head>
<body>
<p>Jsp работает</p>
<h2>Список спринтов</h2>
<table>
    <tr><th>ID</th><th>Название</th><th>Проект</th><th>Дата начала</th><th>Дата окончания</th></tr>
    <c:forEach var="sprint" items="${sprints}">
        <tr>
            <td>${sprint.sprintId}</td>
            <td>${sprint.name}</td>
            <td>${sprint.projectId}</td>
            <td>${sprint.startDate}</td>
            <td>${sprint.endDate}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
