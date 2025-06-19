<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Personnel Evaluation System</title> <%-- Dynamic page title --%>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body { display: flex; flex-direction: column; min-height: 100vh; }
        .navbar-brand { font-weight: bold; }
        .navbar-nav .nav-link { margin-right: 15px; } /* Spacing between nav items */
        .content { flex: 1; padding-bottom: 60px; } /* Main content wrapper for footer spacing */
        .footer { background-color: #f8f9fa; padding: 20px 0; text-align: center; margin-top: auto; }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="/">PersonnelEval</a> <%-- Changed to '/' for general home --%>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mr-auto">
                <%-- Always show Dashboard link if authenticated --%>
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item">
                        <a class="nav-link" href="/dashboard">Dashboard <span class="sr-only">(current)</span></a>
                    </li>
                </sec:authorize>

                <%-- Admin/HR Management (only for authenticated ADMIN or HR_SPECIALIST) --%>
                <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                    <li class="nav-item">
                        <a class="nav-link" href="/departments">Departments</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/employees">Employees</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/positions">Positions</a> <%-- ENSURED LINK IS HERE --%>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="evaluationDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Evaluations
                        </a>
                        <div class="dropdown-menu" aria-labelledby="evaluationDropdown">
                            <a class="dropdown-item" href="/evaluation-periods">Periods</a>
                            <a class="dropdown-item" href="/evaluation-types">Types</a>
                            <a class="dropdown-item" href="/question-types">Question Types</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="/evaluation-forms">Forms</a>
                            <a class="dropdown-item" href="/performance-reviews">Reviews</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="competencyDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Competencies
                        </a>
                        <div class="dropdown-menu" aria-labelledby="competencyDropdown">
                            <a class="dropdown-item" href="/competency-categories">Categories</a>
                            <a class="dropdown-item" href="/competencies">Competencies</a>
                            <a class="dropdown-item" href="/competency-levels">Levels</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="/employee-competencies">Employee Competencies</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="goalDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Goals
                        </a>
                        <div class="dropdown-menu" aria-labelledby="goalDropdown">
                            <a class="dropdown-item" href="/goal-types">Types</a>
                            <a class="dropdown-item" href="/goal-statuses">Statuses</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="/goals">Goals</a>
                        </div>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="feedbackDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Feedback
                        </a>
                        <div class="dropdown-menu" aria-labelledby="feedbackDropdown">
                            <a class="dropdown-item" href="/feedback-types">Types</a>
                                <%-- THIS IS THE CORRECTED LINE --%>
                            <a class="dropdown-item" href="/feedback/list">All Feedback</a>
                        </div>
                    </li>
                </sec:authorize>

                <%-- Admin-only sections --%>
                <sec:authorize access="hasRole('ADMIN')">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="adminDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Admin
                        </a>
                        <div class="dropdown-menu" aria-labelledby="adminDropdown">
                            <a class="dropdown-item" href="/users">Manage Users</a>
                        </div>
                    </li>
                </sec:authorize>

                    <%-- General Employee Features (for any authenticated EMPLOYEE, including Managers/HR/Admin who also have EMPLOYEE role) --%>
                    <sec:authorize access="hasRole('EMPLOYEE')">
                        <li class="nav-item">
                            <a class="nav-link" href="/profile/my-profile">My Profile</a> <%-- MODIFIED --%>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/performance-reviews/my-reviews">My Reviews</a> <%-- MODIFIED --%>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/goals">My Goals</a> <%-- MODIFIED --%>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/feedback/give">Give Feedback</a> <%-- MODIFIED --%>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/notifications">Notifications</a>
                        </li>
                    </sec:authorize>

            </ul>
            <ul class="navbar-nav ml-auto">
                <%-- User Info / Logout for Authenticated Users --%>
                <sec:authorize access="isAuthenticated()">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarUserDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Welcome, <sec:authentication property="principal.username"/>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarUserDropdown">
                            <a class="dropdown-item" href="/profile/settings">Settings</a> <%-- MODIFIED --%>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="#" onclick="document.getElementById('logoutForm').submit();">Logout</a>
                            <form id="logoutForm" action="/logout" method="post" style="display: none;">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            </form>
                        </div>
                    </li>
                </sec:authorize>
                <%-- Login / Register for Unauthenticated Users --%>
                <sec:authorize access="!isAuthenticated()">
                    <li class="nav-item">
                        <a class="nav-link" href="/login">Login</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/register">Register</a>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>
<div class="container mt-4 content"> <%-- Main content wrapper, opened here, closed in footer --%>
<%-- Page specific content will be included here --%>