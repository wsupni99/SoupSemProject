<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создание проекта</title>
</head>
<body>
<h2>Создание проекта</h2>
<form action="${pageContext.request.contextPath}/project/create" method="post">
    <label>Имя:</label><br>
    <input type="text" name="name" required><br>
    <label>Описание:</label><br>
    <input type="text" name="description"><br>
    <label>Дата начала:</label><br>
    <input type="date" name="startDate" required><br>
    <label>Дата окончания:</label><br>
    <input type="date" name="endDate" required><br>
    <label>Статус:</label><br>
    <select name="status" required>
        <option value="активен">активен</option>
        <option value="на паузе">на паузе</option>
        <option value="завершён">завершён</option>
    </select><br>
    <label>Менеджер:</label><br>
    <select name="managerId" required>
        <c:forEach var="manager" items="${managers}">
            <option value="${manager.userId}">${manager.name}</option>
        </c:forEach>
    </select><br>
    <input type="submit" value="Создать">
</form>
</body>
</html>
