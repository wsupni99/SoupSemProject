<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка 403 — Доступ запрещён</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="error-card">
    <div class="error-icon">!</div>
    <h1 class="error-title">Доступ запрещён</h1>
    <p class="error-subtitle">У вас нет прав для просмотра этой страницы.</p>

    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
        <button type="button" class="btn btn-secondary" onclick="history.back()">Назад</button>
    </div>

    <p class="error-code">Код ошибки: 403</p>
</div>
</body>
</html>
