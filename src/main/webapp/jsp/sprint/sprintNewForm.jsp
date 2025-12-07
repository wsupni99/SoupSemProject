<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta charset="UTF-8">
    <title>Создать спринт</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Создать спринт</h2>
    </div>

    <form action="${pageContext.request.contextPath}/sprint/create" method="post">
        <div class="form-grid-1">
            <div class="form-group">
                <label class="form-label">Проект:</label>
                <select class="form-select" name="projectId">
                    <c:forEach var="project" items="${projects}">
                        <option value="${project.projectId}" ${project.projectId == sprint.projectId ? 'selected' : ''}>${project.name}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Название спринта:</label>
                <label>
                    <input class="form-input" type="text" name="name" required>
                </label>
            </div>

            <div class="form-group">
                <label class="form-label">Дата начала:</label>
                <label>
                    <input class="form-input" type="date" name="startDate" required>
                </label>
            </div>

            <div class="form-group">
                <label class="form-label">Дата окончания:</label>
                <label>
                    <input class="form-input" type="date" name="endDate" required>
                </label>
            </div>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/sprints" class="btn btn-secondary">Назад к списку</a>
            <input type="submit" value="Создать" class="btn btn-primary">
        </div>
    </form>
</div>
</body>
</html>
