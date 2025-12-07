<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка 404 — Страница не найдена</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="error-card">
    <div class="error-icon error-icon-404">404</div>
    <h1 class="error-title">Страница не найдена</h1>
    <p class="error-subtitle">
        Такой страницы больше нет или она никогда не существовала.
    </p>

    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
        <button type="button" class="btn btn-secondary" onclick="history.back()">Назад</button>
    </div>

    <p class="error-code">Код ошибки: 404</p>
</div>
</body>
</html>
