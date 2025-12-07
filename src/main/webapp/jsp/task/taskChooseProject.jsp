<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Выбор проекта</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Выбор проекта</h2>
    </div>

    <form action="${pageContext.request.contextPath}/task/newWithProject" method="get">
        <div class="form-group">
            <label class="form-label" for="projectId">Проект:</label>
            <select class="form-select" name="id" id="projectId" required>
                <c:forEach var="project" items="${projects}">
                    <option value="${project.projectId}">${project.name}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <input type="submit" value="Дальше" class="btn btn-primary">
            <button type="button" class="btn btn-secondary" onclick="history.back()">Назад</button>
        </div>
    </form>
</div>
</body>
</html>
