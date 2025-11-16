<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
<h1>Error</h1>
<p>${error}</p>
<a href="${pageContext.request.contextPath}/users">Back to Users</a>
</body>
</html>
