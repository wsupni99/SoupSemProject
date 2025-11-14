<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Спринты</title>
</head>
<body>
    <p><a href="${pageContext.request.contextPath}/home">На главную</a> <a href="${pageContext.request.contextPath}/sprint/new">Создать спринт</a></p>
    <h2>Список спринтов</h2>
    <table>
        <thead>
            <tr>
                <th>Name</th>
                <th>Project</th>
                <th>Start Date</th>
                <th>End Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="sprint" items="${sprints}">
                <tr>
                    <td>
                        <a href="${pageContext.request.contextPath}/sprint/edit?id=${sprint.sprintId}">
                                ${sprint.name}
                        </a>
                    </td>
                    <td>${projectNames[sprint.sprintId]}</td>
                    <td>${sprint.startDate}</td>
                    <td>${sprint.endDate}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
