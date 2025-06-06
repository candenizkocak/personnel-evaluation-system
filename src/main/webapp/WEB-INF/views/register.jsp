<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Personnel Evaluation System</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            background-color: #f8f9fa;
        }
        .register-container {
            max-width: 500px;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,.05);
            background-color: #fff;
        }
    </style>
</head>
<body>
<div class="register-container">
    <h2 class="text-center mb-4">Register for an Account</h2>
    <div class="alert alert-warning" role="alert">
        Registration is currently disabled. Please contact an administrator.
    </div>
    <%-- In a real app, you'd have a registration form here --%>
    <p class="text-center">
        Go back to <a href="/login">Login</a>.
    </p>
</div>
</body>
</html>