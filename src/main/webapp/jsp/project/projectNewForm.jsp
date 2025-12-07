<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создание проекта</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Создание проекта</h2>
    </div>

    <form action="${pageContext.request.contextPath}/project/create" method="post">
        <div class="form-grid-1">
            <div class="form-group">
                <label class="form-label">Имя:</label>
                <input class="form-input" type="text" name="name" required>
            </div>

            <div class="form-group">
                <label class="form-label">Описание:</label>
                <input class="form-input" type="text" name="description">
            </div>

            <div class="form-group">
                <label class="form-label">Дата начала:</label>
                <input class="form-input" type="date" name="startDate" required>
            </div>

            <div class="form-group">
                <label class="form-label">Дата окончания:</label>
                <input class="form-input" type="date" name="endDate" required>
            </div>

            <div class="form-group">
                <label class="form-label">Статус:</label>
                <select class="form-select" name="status" required>
                    <option value="активен">активен</option>
                    <option value="на паузе">на паузе</option>
                    <option value="завершён">завершён</option>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Менеджер:</label>
                <select class="form-select" name="managerId" required>
                    <c:forEach var="manager" items="${managers}">
                        <option value="${manager.userId}">${manager.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="Создать" class="btn btn-primary">
        </div>
    </form>
</div>
</body>
</html>
