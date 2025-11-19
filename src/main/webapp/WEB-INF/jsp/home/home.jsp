<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SOUP - Главная</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/styles.css?v=1">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-white border-bottom fixed-top shadow-sm">
    <div class="container-fluid">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/home">SOUP</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent"
                aria-controls="navbarContent" aria-expanded="false" aria-label="Переключить навигацию">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/home">Главная</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/tasks">Задачи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/projects">Проекты</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/sprints">Спринты</a>
                </li>
                <c:if test="${isAdmin}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/users">Пользователи</a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav ms-auto">
                <c:if test="${not empty sessionScope.user}">
                    <li class="nav-item">
                        <span class="navbar-text me-3">Твой ID: ${sessionScope.user.userId}</span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/logout">Выход</a>
                    </li>
                </c:if>
                <c:if test="${empty sessionScope.user}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Вход</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/register">Регистрация</a>
                    </li>
                </c:if>
            </ul>
        </div>
    </div>
</nav>

<div class="container mt-5 pt-3">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-sm">
                <div class="card-body text-center py-5">
                    <h1 class="mb-4">Добро пожаловать в SOUP!</h1>

                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <p class="lead mb-4">Привет, <strong>${sessionScope.user.name}</strong>!</p>
                        </c:when>
                        <c:otherwise>
                            <p class="lead mb-4">Система Управления Проектами и задачами</p>
                        </c:otherwise>
                    </c:choose>

                    <p class="text-muted mb-4">
                        Организуйте работу команды, управляйте проектами, задачами и спринтами.
                        Присоединяйтесь к эффективному управлению вашими проектами!
                    </p>

                    <div class="row g-3 mb-4">
                        <div class="col-12 col-md-6">
                            <a href="${pageContext.request.contextPath}/tasks" class="btn btn-primary btn-lg mb-3 w-100">
                                <i class="bi bi-check-circle me-2"></i>К задачам
                            </a>
                        </div>
                        <div class="col-12 col-md-6">
                            <a href="${pageContext.request.contextPath}/projects" class="btn btn-outline-primary btn-lg mb-3 w-100">
                                <i class="bi bi-folder me-2"></i>К проектам
                            </a>
                        </div>
                    </div>

                    <div class="nav-section">
                        <h5 class="text-center mb-3">Навигация</h5>
                        <div class="row">
                            <div class="col-md-6">
                                <ul class="list-unstyled text-start">
                                    <li class="mb-2"><a href="${pageContext.request.contextPath}/tasks">📋 Задачи</a></li>
                                    <li class="mb-2"><a href="${pageContext.request.contextPath}/projects">📁 Проекты</a></li>
                                </ul>
                            </div>
                            <div class="col-md-6">
                                <ul class="list-unstyled text-start">
                                    <li class="mb-2"><a href="${pageContext.request.contextPath}/sprints">⏱️ Спринты</a></li>
                                    <c:if test="${isAdmin}">
                                        <li class="mb-2"><a href="${pageContext.request.contextPath}/users">👥 Пользователи</a></li>
                                    </c:if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
