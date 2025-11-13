<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Создание задачи</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/task/create" method="post">
    <input type="hidden" name="projectId" value="${projectId}">
    <div>
        <label for="sprintId">Спринт:</label><br>
        <select name="sprintId" id="sprintId" required>
            <c:choose>
                <c:when test="${not empty sprints}">
                    <c:forEach var="sprint" items="${sprints}">
                        <option value="${sprint.sprintId}">${sprint.name}</option>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <option value="">Нет спринтов</option>
                </c:otherwise>
            </c:choose>
        </select>
    </div>
    <div>
        <label for="name">Название задачи:</label><br>
        <input type="text" name="name" id="name" required>
    </div>
    <div>
        <label for="description">Описание:</label><br>
        <textarea name="description" id="description"></textarea>
    </div>
    <div>
        <label for="parentTaskId">Родительская задача (опционально):</label><br>
        <input type="number" name="parentTaskId" id="parentTaskId">
    </div>
    <div>
        <label for="userId">Пользователь:</label><br>
        <input type="text" id="user-search" placeholder="Поиск пользователя">
        <button type="button" onclick="showUsers()">Найти</button>
        <input type="hidden" id="userIdInput" name="userId" required>
        <div id="users-block" style="display:none;">
            <select id="userList" size="5" style="margin-top:8px;" onclick="selectUser()">
                <c:forEach var="user" items="${users}">
                    <option value="${user.userId}">${user.name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div>
        <input type="submit" value="Создать">
    </div>
</form>
<script>
    function showUsers() {
        document.getElementById('users-block').style.display = "block";
        filterUsers();
    }
    document.getElementById('user-search').addEventListener('input', filterUsers);

    function filterUsers() {
        var input = document.getElementById('user-search').value.toLowerCase();
        var select = document.getElementById('userList');
        for (var i = 0; i < select.options.length; i++) {
            var option = select.options[i];
            option.style.display = option.text.toLowerCase().includes(input) ? '' : 'none';
        }
    }

    function selectUser() {
        var select = document.getElementById('userList');
        var chosen = select.options[select.selectedIndex];
        document.getElementById('user-search').value = chosen.text;
        document.getElementById('userIdInput').value = chosen.value;
        document.getElementById('users-block').style.display = "none";
    }
</script>
</body>
</html>
