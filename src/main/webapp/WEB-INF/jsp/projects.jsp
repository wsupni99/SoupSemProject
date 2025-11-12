<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
    <head>
        <title>Проекты</title>
    </head>
    <body>
        <h3>projects.jsp отрендерен</h3>
        <h2>Список проектов</h2>
        <table>
            <tr><th>ID</th><th>Имя</th><th>Описание</th><th>Дата начала</th><th>Дата окончания</th><th>Статус</th></tr>
            <c:forEach var="project" items="${projects}">
                <tr>
                    <td>${project.projectId}</td>
                    <td>${project.name}</td>
                    <td>${project.description}</td>
                    <td>${project.startDate}</td>
                    <td>${project.endDate}</td>
                    <td>${project.status}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
