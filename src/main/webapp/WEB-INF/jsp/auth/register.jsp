<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
</head>
<body>
<h2>Регистрация</h2>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<c:if test="${not empty debugRole}">
    <p style="color: blue;">Debug role: '${debugRole}'</p>
</c:if>
<form accept-charset="UTF-8" action="${pageContext.request.contextPath}/register" method="post">
    <label>Email:</label>
    <input type="email" name="email" required><br><br>

    <label>Имя:</label>
    <input type="text" name="name" required><br><br>

    <label>Пароль:</label>
    <input type="password" name="password" required><br><br>

    <label>Роль:</label>
    <select name="role" id="roleSelect" required>
        <option value="">Выберите роль</option>
        <option value="Разработчик">Разработчик</option>
        <option value="Тестировщик">Тестировщик</option>
        <option value="Менеджер">Менеджер</option>
    </select><br><br>

    <div id="projectDiv" style="display: none;">
        <label>Выберите проект:</label>
        <select name="projectId">
            <c:forEach var="project" items="${projects}">
                <option value="${project.projectId}">${project.name}</option>
            </c:forEach>
        </select><br><br>
    </div>

    <input type="submit" value="Зарегистрироваться">
</form>

<script>
    document.getElementById('roleSelect').addEventListener('change', function() {
        var projectDiv = document.getElementById('projectDiv');
        if (this.value === 'Менеджер') {
            projectDiv.style.display = 'block';
        } else {
            projectDiv.style.display = 'none';
        }
    });
</script>
</body>
</html>
