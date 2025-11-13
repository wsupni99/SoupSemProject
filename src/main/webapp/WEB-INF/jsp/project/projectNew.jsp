<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>Создать проект</title>
    <meta charset="UTF-8">
</head>
<body>
<h2>Создание нового проекта</h2>
<form method="post" action="${pageContext.request.contextPath}/projects">
    <label>Имя: <input type="text" name="name" required></label><br><br>
    <label>Описание: <input type="text" name="description" required></label><br><br>
    <label>Дата начала: <input type="date" name="startDate" required></label><br><br>
    <label>Дата окончания: <input type="date" name="endDate" required></label><br><br>
    <label>Статус:
        <select name="status">
            <option>активен</option>
            <option>на паузе</option>
            <option>завершён</option>
        </select>
    </label>
    <label>Менеджер:
        <select name="managerId">
            <c:forEach var="manager" items="${managers}">
                <option value="${manager.userId}">${manager.name}</option>
            </c:forEach>
        </select>
    </label><br><br>
    <button type="submit">Создать</button>
</form>
<br>
<a href="${pageContext.request.contextPath}/projects">Назад к списку</a>
</body>
</html>
