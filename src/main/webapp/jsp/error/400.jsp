<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка 400 — Некорректный запрос</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="form-page">
<div class="error-card">
    <div class="error-icon error-icon-400">400</div>
    <h1 class="error-title">Некорректный запрос</h1>
    <p class="error-subtitle">
        Сервер не может обработать этот запрос. Проверьте корректность адреса
        или введённых данных.
    </p>

    <div class="error-actions">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary">На главную</a>
        <button type="button" class="btn btn-secondary" onclick="history.back()">Назад</button>
    </div>

    <p class="error-code">Код ошибки: 400</p>
</div>
</body>
</html>
