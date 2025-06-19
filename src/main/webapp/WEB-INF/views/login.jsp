<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Personnel Evaluation System</title>
    <%-- We are not using Bootstrap here to match the image, but you can add it if needed --%>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        body, html {
            height: 100%;
            margin: 0;
            font-family: 'Roboto', sans-serif;
            overflow: hidden; /* Prevent scrollbars from animation */
        }

        .login-wrapper {
            display: flex;
            height: 100%;
            width: 100%;
        }

        .login-image-panel {
            flex: 0 0 55%; /* Adjust width percentage as needed */
            background-image: url('<c:url value="/images/background.png"/>');
            background-size: cover; /* Cover the area */
            background-repeat: no-repeat;
            animation: slideBackground 40s linear infinite alternate;
            overflow: hidden; /* Important for background-position animation */
        }

        @keyframes slideBackground {
            0% {
                background-position: 0% 50%;
            }
            100% {
                background-position: 100% 50%;
            }
        }

        .login-form-panel {
            flex: 0 0 45%; /* Adjust width percentage as needed */
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 40px;
            background-color: #ffffff;
            box-sizing: border-box;
        }

        .logo-container {
            text-align: center;
            margin-bottom: 20px;
        }

        .logo-container .eczacibasi-logo {
            max-width: 300px; /* Adjust as needed */
            margin-bottom: 50px;
        }

        .logo-container .vitra-logo {
            max-width: 200px; /* Adjust as needed */
            margin-bottom: 25px;
        }

        .login-form-panel h1 {
            font-size: 1.5em; /* 24px / 16px */
            font-weight: 700;
            color: #333;
            margin-bottom: 35px;
            text-align: center;
        }

        .login-form {
            width: 100%;
            max-width: 360px; /* Max width for the form itself */
        }

        .form-group {
            margin-bottom: 25px;
            position: relative;
        }

        .form-group label {
            font-size: 0.875em; /* 14px */
            color: #888;
            display: block;
            margin-bottom: 8px;
        }

        .form-group input[type="text"],
        .form-group input[type="password"] {
            width: 100%;
            border: none;
            border-bottom: 1px solid #ccc;
            padding: 10px 0;
            font-size: 1em; /* 16px */
            color: #333;
            background-color: transparent;
            box-sizing: border-box;
        }

        .form-group input[type="text"]:focus,
        .form-group input[type="password"]:focus {
            outline: none;
            border-bottom-color: #005f73; /* Darker teal for focus, adjust color */
        }

        .btn-login {
            width: 100%;
            padding: 12px;
            background-color: #005f73; /* Dark teal/blue from image */
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 1em;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s ease;
            margin-top: 10px;
        }

        .btn-login:hover {
            background-color: #004c5d; /* Slightly darker shade on hover */
        }

        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            padding: .75rem 1.25rem;
            margin-bottom: 1rem;
            border: 1px solid #f5c6cb;
            border-radius: .25rem;
            width: 100%;
            box-sizing: border-box;
            text-align: left;
        }
        .alert-info {
            color: #0c5460;
            background-color: #d1ecf1;
            border-color: #bee5eb;
            padding: .75rem 1.25rem;
            margin-bottom: 1rem;
            border: 1px solid transparent;
            border-radius: .25rem;
        }


    </style>
</head>
<body>
<div class="login-wrapper">
    <div class="login-image-panel">
        <%-- Background image is set via CSS --%>
    </div>
    <div class="login-form-panel">
        <div class="logo-container">
            <img src="<c:url value="/images/eczacibasi_logo.png"/>" alt="Eczacıbaşı Logo" class="eczacibasi-logo">
            <br>
            <img src="<c:url value="/images/vitra_logo_black.png"/>" alt="VitrA Logo" class="vitra-logo">
        </div>

        <h1>Personnel Evaluation System</h1>

        <form action="<c:url value="/perform_login"/>" method="post" class="login-form">
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

            <div class="form-group">
                <label for="username">Username</label>
                <input type="text" id="username" name="username" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-login">LOGIN</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>
        <%-- Removed the "Register here" link as it's not in the target design --%>
    </div>
</div>
</body>
</html>