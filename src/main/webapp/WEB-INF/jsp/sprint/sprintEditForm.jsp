<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактирование спринта</title>
</head>
<body>
<h2>Редактирование спринта</h2>
<div id="error-message" style="color: red; display: none;"></div>
<form action="${pageContext.request.contextPath}/sprint/update" method="post">
    <input type="hidden" name="sprintId" value="${sprint.sprintId}">
    <label>Проект:</label><br>
    <select name="projectId">
        <c:forEach var="project" items="${projects}">
            <option value="${project.projectId}" ${project.projectId == sprint.projectId ? 'selected' : ''}>${project.name}</option>
        </c:forEach>
    </select>
    <br>
    <label>Название:</label><br>
    <input type="text" name="name" value="${sprint.name}" required><br>
    <label>Дата начала:</label><br>
    <input type="date" name="startDate" value="${sprint.startDate}" required><br>
    <label>Дата окончания:</label><br>
    <input type="date" name="endDate" value="${sprint.endDate}" required><br>
    <input type="submit" value="Сохранить">
</form>
<form id="delete-form" action="${pageContext.request.contextPath}/sprint/delete" method="post" style="margin-top:15px;">
    <input type="hidden" name="id" value="${sprint.sprintId}">
    <button type="button" onclick="deleteSprint()">Удалить спринт</button>
</form>
<script>
    function deleteSprint() {
        if (!confirm("Удалить спринт?")) return;
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
