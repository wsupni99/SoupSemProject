<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Проекты</title>
    </head>
<body>
    <p><a href="${pageContext.request.contextPath}/home">На главную</a> <a href="${pageContext.request.contextPath}/project/new">Создать проект</a></p>
    <h2>Список проектов</h2>
    <table>
        <tr>
            <th>ID</th><th>Имя</th><th>Описание</th><th>Дата начала</th>
            <th>Дата окончания</th><th>Статус</th><th>Менеджер</th>
        </tr>
        <c:forEach var="project" items="${projects}">
            <tr>
                <td>${project.projectId}</td>
                <td>
                    <a href="project/edit?id=${project.projectId}">${project.name}</a>
                </td>
                <td>${project.description}</td>
                <td>${project.startDate}</td>
                <td>${project.endDate}</td>
                <td>${project.status}</td>
                <td>${managerNames[project.managerId]}</td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
