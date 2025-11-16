<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Список пользователей</title>
    <script>
        function deleteUser(id) {
            if (confirm("Вы уверены, что хотите удалить этого пользователя?")) {
                fetch("${pageContext.request.contextPath}/user/delete", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "id=" + id
                })
                    .then(response => response.json())
                    .then(data => {
                        if (data.success) {
                            location.reload();
                        } else {
                            const errorDiv = document.getElementById('errorDiv');
                            errorDiv.textContent = data.message;
                            errorDiv.style.display = 'block';
                        }
                    })
                    .catch(error => {
                        const errorDiv = document.getElementById('errorDiv');
                        errorDiv.textContent = 'An error occurred during deletion. Please try again.';
                        errorDiv.style.display = 'block';
                    });
            }
        }
    </script>
</head>
<body>
<div id="errorDiv" style="color: red; display: none; margin: 10px 0; font-weight: bold;"></div>
<h1>Список пользователей</h1>
<table border="1">
    <thead>
    <tr>
        <th>Имя</th>
        <th>Почта</th>
        <th>Роль</th>
        <th>Инфо</th>
        <th>Действия</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.name}</td>
            <td>${user.email}</td>
            <td>${roleMap[user.userId]}</td>
            <td>${user.contactInfo}</td>
            <td><button onclick="deleteUser(${user.userId})">Удалить</button></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
