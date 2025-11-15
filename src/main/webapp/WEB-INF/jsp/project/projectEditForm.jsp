<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование проекта</title>
</head>
<body>
<h2>Редактирование проекта</h2>
<div id="error-message" style="color: red; display: none;"></div>
<form action="${pageContext.request.contextPath}/project/update" method="post">
    <input type="hidden" name="id" value="${projectId}">
    <label>Имя:</label><br>
    <input type="text" name="name" value="${projectName}" required><br>
    <label>Описание:</label><br>
    <input type="text" name="description" value="${projectDescription}"><br>
    <label>Дата начала:</label><br>
    <input type="date" name="startDate" value="${projectStartDate}" required><br>
    <label>Дата окончания:</label><br>
    <input type="date" name="endDate" value="${projectEndDate}" required><br>
    <label>Статус:</label><br>
    <select name="status" required>
        <option value="активен">активен</option>
        <option value="на паузе">на паузе</option>
        <option value="завершён">завершён</option>
    </select><br>
    <select name="managerId" required>
        <c:forEach var="manager" items="${managers}">
            <option value="${manager.userId}" ${manager.userId == projectManagerId ? 'selected' : ''}>${manager.name}</option>
        </c:forEach>
    </select><br>
    <input type="submit" value="Сохранить">
</form>
<form id="delete-form" action="${pageContext.request.contextPath}/project/delete" method="post" style="margin-top:15px;">
    <input type="hidden" name="id" value="${projectId}">
    <button type="button" onclick="deleteProject()">Удалить проект</button>
</form>
<script>
    function deleteProject() {
        if (!confirm("Удалить проект?")) return;
        const form = document.getElementById("delete-form");
        const formData = new FormData(form);
        const params = new URLSearchParams();
        for (let [key, value] of formData.entries()) {
            params.append(key, value);
        }
        fetch(form.action, {
            method: "POST",
            body: params,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
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
