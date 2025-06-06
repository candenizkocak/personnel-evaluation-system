<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Personnel Evaluation System</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .login-container {
            max-width: 400px;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,.05);
            background-color: #fff;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2 class="text-center mb-4">Login</h2>

    <c:if test="${param.error != null}">
        <div class="alert alert-danger" role="alert">
            Invalid username or password.
        </div>
    </c:if>

    <c:if test="${param.logout != null}">
        <div class="alert alert-info" role="alert">
            You have been logged out.
        </div>
    </c:if>

    <form action="/perform_login" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" class="form-control" id="username" name="username" required autofocus>
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary btn-block">Login</button>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> <%-- <--- ADD THIS LINE --%>
    </form>
    <p class="mt-3 text-center">
        Don't have an account? <a href="/register">Register here</a>
    </p>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>