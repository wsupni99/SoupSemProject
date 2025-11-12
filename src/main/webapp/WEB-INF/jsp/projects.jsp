<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Проекты</title>
</head>
<body>
<p><a href="${pageContext.request.contextPath}/project/new">Создать проект</a></p>
<h2>Список проектов</h2>
<table>
    <tr>
        <th>ID</th><th>Имя</th><th>Описание</th><th>Дата начала</th>
        <th>Дата окончания</th><th>Статус</th><th>Менеджер</th>
        <th>Действия</th>
    </tr>
    <c:forEach var="project" items="${projects}">
        <tr>
            <td>${project.projectId}</td>
            <td>${project.name}</td>
            <td>${project.description}</td>
            <td>${project.startDate}</td>
            <td>${project.endDate}</td>
            <td>${project.status}</td>
            <td>${project.managerId}</td>
            <td>
                <button onclick="deleteProject(${project.projectId})">Удалить</button>
            </td>
            <td>
                <a href="project/edit?id=${project.projectId}">Редактировать</a>
            </td>
        </tr>
    </c:forEach>
</table>
<script>
    function deleteProject(id) {
        if (!confirm('Удалить проект?')) return;
        fetch(`${location.origin}${pageContext.request.contextPath}/project/` + id, {
            method: 'DELETE'
        }).then(resp => {
            if (resp.status === 204) {
                location.reload();
            } else {
                alert('Ошибка удаления');
            }
        }).catch(() => alert('Запрос не выполнен'));
    }
</script>
</body>
</html>
