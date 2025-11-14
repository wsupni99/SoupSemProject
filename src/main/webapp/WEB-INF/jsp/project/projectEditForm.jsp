<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Редактировать проект</title>
</head>
<body>
<h2>Редактировать проект</h2>
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
    <input type="text" name="status" value="${task.status}" required><br>
    <label>Менеджер:</label><br>
    <select name="managerId" required>
        <c:forEach var="manager" items="${managers}">
            <option value="${manager.userId}" <c:if test="${manager.userId == projectManagerId}">selected</c:if>>
                    ${manager.name}
            </option>
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
        fetch(form.action, {
            method: "POST",
            body: formData
        }).then(async resp => {
            if (resp.redirected) {
                window.location.href = resp.url;
            } else {
                alert(await resp.text());
            }
        });
    }
</script>
</body>
</html>
