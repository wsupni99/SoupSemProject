<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование проекта</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Редактирование проекта</h2>
    </div>

    <div id="error-message" class="form-error" style="display: none;"></div>

    <form action="${pageContext.request.contextPath}/project/update" method="post">
        <input type="hidden" name="id" value="${projectId}">

        <div class="form-grid-1">
            <div class="form-group">
                <label class="form-label">Имя:</label>
                <input class="form-input" type="text" name="name" value="${projectName}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Описание:</label>
                <input class="form-input" type="text" name="description" value="${projectDescription}">
            </div>

            <div class="form-group">
                <label class="form-label">Дата начала:</label>
                <input class="form-input" type="date" name="startDate" value="${projectStartDate}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Дата окончания:</label>
                <input class="form-input" type="date" name="endDate" value="${projectEndDate}" required>
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
                        <option value="${manager.userId}" ${manager.userId == projectManagerId ? 'selected' : ''}>${manager.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="Сохранить" class="btn btn-primary">
        </div>
    </form>
    <form id="delete-form"
          action="${pageContext.request.contextPath}/sprint/delete"
          method="post"
          style="margin-top:15px;">
        <input type="hidden" name="id" value="${sprintId}">
        <button type="button" onclick="deleteSprint()" class="btn btn-danger">
            Удалить спринт
        </button>
    </form>
</div>
<div id="error-message" class="form-error" style="display: none;"></div>
<form id="delete-form"
      action="${pageContext.request.contextPath}/sprint/delete"
      method="post"
      style="margin-top:15px;">
    <input type="hidden" name="id" value="${sprintId}">
    <button type="button" onclick="deleteSprint()" class="btn btn-danger">
        Удалить спринт
    </button>
</form>
</body>
</html>
