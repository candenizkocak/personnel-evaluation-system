<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Personnel Evaluation System</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
    <style>
        body {
            background-color: #f4f7f6;
            margin: 0;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .wrapper {
            display: flex;
            width: 100%;
            min-height: 100vh;
        }
        .sidebar {
            width: 250px;
            background: #2c3e50; /* Dark sidebar */
            color: white;
            transition: all 0.3s;
            position: fixed;
            height: 100%;
            overflow-y: auto;
            z-index: 1000;
        }
        .sidebar-header {
            padding: 20px;
            background: #1a252f; /* Even darker header */
            text-align: center;
        }
        .sidebar-header h3 {
            color: #ecf0f1;
            margin: 0;
            font-weight: 600;
        }
        .sidebar ul.components {
            padding: 0;
            list-style: none;
        }
        .sidebar ul li a {
            padding: 15px 20px;
            font-size: 1.1em;
            display: block;
            color: #bdc3c7;
            text-decoration: none;
            transition: all 0.2s;
            border-bottom: 1px solid #34495e;
        }
        .sidebar ul li a:hover {
            color: #ffffff;
            background: #34495e;
        }
        .sidebar ul li.active > a, a[aria-expanded="true"] {
            color: #fff;
            background: #1e88e5; /* Active link blue */
        }
        .sidebar ul li a i {
            margin-right: 10px;
        }
        /* Sub-menu styling */
        .sidebar ul ul a {
            font-size: 1em !important;
            padding-left: 30px !important;
            background: #23313f;
            border-bottom: 1px solid #2c3e50;
        }
        .main-content {
            flex-grow: 1;
            padding: 20px;
            margin-left: 250px; /* Same as sidebar width */
            transition: all 0.3s;
        }
        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 10px 20px;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 1px 4px rgba(0,0,0,0.1);
            margin-bottom: 25px;
        }
        .top-bar .page-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #333;
        }
        .top-bar .user-info {
            display: flex;
            align-items: center;
        }
        .top-bar .welcome-text {
            margin-right: 15px;
            color: #555;
        }
        .logout-btn {
            border: 1px solid #e74c3c;
            color: #e74c3c;
            background-color: transparent;
        }
        .logout-btn:hover {
            background-color: #e74c3c;
            color: white;
        }
        .card {
            border: none;
            border-radius: 8px;
        }
        .card-header {
            background-color: #003366; /* Dark corporate blue */
            color: white;
            font-weight: bold;
            border-top-left-radius: 8px;
            border-top-right-radius: 8px;
        }
    </style>
</head>
<body>
<div class="wrapper">
    <!-- Sidebar -->
    <nav class="sidebar">
        <div class="sidebar-header">
            <h3>PersonnelEval</h3>
        </div>

        <ul class="list-unstyled components">
            <li><a href="/dashboard"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>

            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <li><a href="/departments"><i class="fas fa-building"></i> Departments</a></li>
                <li><a href="/employees"><i class="fas fa-users"></i> Employees</a></li>
                <li><a href="/positions"><i class="fas fa-user-tie"></i> Positions</a></li>

                <li>
                    <a href="#evaluationSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><i class="fas fa-clipboard-list"></i> Evaluations</a>
                    <ul class="collapse list-unstyled" id="evaluationSubmenu">
                        <li><a href="/evaluation-periods">Periods</a></li>
                        <li><a href="/evaluation-types">Types</a></li>
                        <li><a href="/question-types">Question Types</a></li>
                        <li><a href="/evaluation-forms">Forms</a></li>
                        <li><a href="/performance-reviews">Reviews</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#competencySubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><i class="fas fa-star"></i> Competencies</a>
                    <ul class="collapse list-unstyled" id="competencySubmenu">
                        <li><a href="/competency-categories">Categories</a></li>
                        <li><a href="/competencies">Competencies</a></li>
                        <li><a href="/competency-levels">Levels</a></li>
                        <li><a href="/employee-competencies">Employee Assessments</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#goalSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><i class="fas fa-bullseye"></i> Goals</a>
                    <ul class="collapse list-unstyled" id="goalSubmenu">
                        <li><a href="/goal-types">Types</a></li>
                        <li><a href="/goal-statuses">Statuses</a></li>
                        <li><a href="/goals">All Goals</a></li>
                    </ul>
                </li>
                <li>
                    <a href="#feedbackSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><i class="fas fa-comments"></i> Feedback</a>
                    <ul class="collapse list-unstyled" id="feedbackSubmenu">
                        <li><a href="/feedback-types">Types</a></li>
                        <li><a href="/feedback/list">All Feedback</a></li>
                    </ul>
                </li>
            </sec:authorize>

            <sec:authorize access="hasRole('ADMIN')">
                <li><a href="/users"><i class="fas fa-user-cog"></i> Manage Users</a></li>
            </sec:authorize>

            <sec:authorize access="isAuthenticated()">
                <li>
                    <a href="#myStuffSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle"><i class="fas fa-user-circle"></i> My Area</a>
                    <ul class="collapse list-unstyled" id="myStuffSubmenu">
                        <li><a href="/profile/my-profile">My Profile</a></li>
                        <li><a href="/performance-reviews/my-reviews">My Reviews</a></li>
                        <li><a href="/goals">My Goals</a></li>
                        <li><a href="/feedback/give">Give Feedback</a></li>
                    </ul>
                </li>
                <li><a href="/notifications"><i class="fas fa-bell"></i> Notifications</a></li>
            </sec:authorize>
        </ul>
    </nav>

    <!-- Page Content -->
    <div class="main-content">
        <div class="top-bar">
            <div class="page-title">${pageTitle}</div>
            <div class="user-info">
                <span class="welcome-text">Welcome, <sec:authentication property="principal.username"/></span>
                <form action="/logout" method="post" class="d-inline">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button type="submit" class="btn btn-sm logout-btn">Logout</button>
                </form>
            </div>
        </div>
        <div class="container-fluid">