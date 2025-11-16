<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Регистрация</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h2>Регистрация</h2>
<c:if test="${not empty error}">
    <p style="color: red;">${error}</p>
</c:if>
<form accept-charset="UTF-8" action="${pageContext.request.contextPath}/register" method="post">
    <label>Email:</label>
    <input type="email" name="email" required><br><br>

    <label>Name:</label>
    <input type="text" name="name" required><br><br>

    <label>Password:</label>
    <input type="password" name="password" required><br><br>

    <label>Role:</label>
    <select name="role" id="roleSelect" required>
        <option value="">Select role</option>
        <option value="DEVELOPER">Developer</option>
        <option value="TESTER">Tester</option>
        <option value="MANAGER">Manager</option>
    </select><br><br>

    <div id="projectDiv" style="display: none;">
        <label>Select project:</label>
        <select name="projectId">
            <c:forEach var="project" items="${projects}">
                <option value="${project.projectId}">${project.name}</option>
            </c:forEach>
        </select><br><br>
    </div>

    <input type="submit" value="Register">
</form>

<script>
    document.getElementById('roleSelect').addEventListener('change', function() {
        const projectDiv = document.getElementById('projectDiv');
        if (this.value === 'MANAGER') {
            projectDiv.style.display = 'block';
        } else {
            projectDiv.style.display = 'none';
        }
    });
</script>
</body>
</html>
