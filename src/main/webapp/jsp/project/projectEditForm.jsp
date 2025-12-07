<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование проекта</title>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css">
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
                    <option value="активен" ${projectStatus == 'активен' ? 'selected' : ''}>активен</option>
                    <option value="на паузе" ${projectStatus == 'на паузе' ? 'selected' : ''}>на паузе</option>
                    <option value="завершён" ${projectStatus == 'завершён' ? 'selected' : ''}>завершён</option>
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
    </form>
    <div class="form-actions-row">
        <div class="form-actions-left">
            <button type="button" onclick="deleteProject()" class="btn btn-danger">
                Удалить проект
            </button>
        </div>
        <div class="form-actions-right">
            <input type="submit" value="Сохранить" class="btn btn-primary">
            <a href="${pageContext.request.contextPath}/projects" class="btn btn-secondary">Отмена</a>
        </div>
    </div>

    <form id="delete-form"
          action="${pageContext.request.contextPath}/project/delete"
          method="post"
          style="display:none;">
        <input type="hidden" name="id" value="${projectId}">
    </form>

</div>

<script>
    function deleteProject() {
        if (!confirm("Удалить проект?")) return;
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
