<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование спринта</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="form-card">
    <div class="form-header">
        <h2 class="form-header-title">Редактирование спринта</h2>
    </div>

    <div id="error-message" class="form-error" style="display: none;"></div>

    <form action="${pageContext.request.contextPath}/sprint/update" method="post">
        <input type="hidden" name="sprintId" value="${sprint.sprintId}">

        <div class="form-grid-1">
            <div class="form-group">
                <label class="form-label">Проект:</label>
                <select class="form-select" name="projectId">
                    <c:forEach var="project" items="${projects}">
                        <option value="${project.projectId}"
                            ${project.projectId == sprint.projectId ? 'selected' : ''}>
                                ${project.name}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label">Название:</label>
                <input class="form-input" type="text" name="name" value="${sprint.name}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Дата начала:</label>
                <input class="form-input" type="date" name="startDate" value="${sprint.startDate}" required>
            </div>

            <div class="form-group">
                <label class="form-label">Дата окончания:</label>
                <input class="form-input" type="date" name="endDate" value="${sprint.endDate}" required>
            </div>
        </div>

        <div class="form-actions">
            <input type="submit" value="Сохранить" class="btn btn-primary">
            <a href="${pageContext.request.contextPath}/sprints" class="btn btn-secondary">Отмена</a>
            <form id="delete-form"
                  action="${pageContext.request.contextPath}/sprint/delete"
                  method="post"
                  style="margin-top:15px;">
                <input type="hidden" name="id" value="${sprint.sprintId}">
                <button type="button" onclick="deleteSprint()" class="btn btn-danger">
                    Удалить спринт
                </button>
            </form>
        </div>
    </form>
</div>

<script>
    function deleteSprint() {
        if (!confirm("Удалить спринт?")) return;
        const form = document.getElementById("delete-form");
        const formData = new FormData(form);
        const params = new URLSearchParams();
        for (const [key, value] of formData.entries()) {
            params.append(key, value);
        }
        fetch(form.action, {
            method: "POST",
            body: params,
            headers: {
                "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
            }
        }).then(async resp => {
            if (resp.redirected) {
                window.location.href = resp.url;
            } else {
                const errorMsg = document.getElementById("error-message");
                errorMsg.textContent = await resp.text();
                errorMsg.style.display = "block";
            }
        });
    }
</script>
</body>
</html>
