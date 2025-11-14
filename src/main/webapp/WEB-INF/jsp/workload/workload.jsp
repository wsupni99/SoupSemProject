<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<html>
<head><title>Workload</title></head>
<body>
<p>Jsp работает</p>
<h2>Статистика нагрузки</h2>
<ul>
    <li>Пользователь: ${summary.userId}</li>
    <li>Проект: ${summary.projectId}</li>
    <li>Спринт: ${summary.sprintId}</li>
    <li>Открыто задач: ${summary.openTasksCount}</li>
    <li>Закрыто задач: ${summary.closedTasksCount}</li>
</ul>
</body>
</html>
