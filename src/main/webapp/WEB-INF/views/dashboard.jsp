<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jsp" %> <%-- INCLUDE THE HEADER --%>

<%-- Set the page title for the header --%>
<c:set var="pageTitle" value="Dashboard" scope="request" />

<h1 class="text-center">Welcome to your Dashboard!</h1>
<p class="text-center">You are logged in as: <strong><sec:authentication property="principal.username"/></strong></p>

<div class="text-center my-4">
    <h3>Your Roles:</h3>
    <ul class="list-unstyled">
        <sec:authentication property="principal.authorities" var="authorities"/>
        <c:forEach var="authority" items="${authorities}">
            <li><span class="badge badge-secondary">${authority.authority}</span></li>
        </c:forEach>
    </ul>
</div>

<div class="text-center">
    <form action="/logout" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit" class="btn btn-danger">Logout</button>
    </form>
</div>

<h3 class="mt-5">Available Features (based on your roles):</h3>
<div class="list-group">
    <%-- Example of role-based UI element display on dashboard --%>
    <sec:authorize access="hasRole('ADMIN')">
        <a href="#" class="list-group-item list-group-item-action list-group-item-dark"><strong>Administrator Functions:</strong></a>
        <a href="/users" class="list-group-item list-group-item-action">Manage User Accounts</a>
        <a href="/roles" class="list-group-item list-group-item-action">Manage Roles</a>
        <a href="/permissions" class="list-group-item list-group-item-action">Manage Permissions</a>
        <div class="list-group-item list-group-item-dark mt-3"><strong>Core Organizational Management:</strong></div>
    </sec:authorize>
    <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
        <a href="/departments" class="list-group-item list-group-item-action">View/Manage Departments</a>
        <a href="/positions" class="list-group-item list-group-item-action">View/Manage Positions</a>
        <a href="/employees" class="list-group-item list-group-item-action">View/Manage Employees</a>
        <div class="list-group-item list-group-item-info mt-3"><strong>Evaluation & Performance Management:</strong></div>
    </sec:authorize>
    <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER')">
        <a href="/evaluation-periods" class="list-group-item list-group-item-action">Evaluation Periods</a>
        <a href="/evaluation-types" class="list-group-item list-group-item-action">Evaluation Types</a>
        <a href="/question-types" class="list-group-item list-group-item-action">Question Types</a>
        <a href="/evaluation-forms" class="list-group-item list-group-item-action">Evaluation Forms</a>
        <a href="/performance-reviews" class="list-group-item list-group-item-action">Performance Reviews</a>
        <div class="list-group-item list-group-item-info mt-3"><strong>Competency Management:</strong></div>
        <a href="/competency-categories" class="list-group-item list-group-item-action">Competency Categories</a>
        <a href="/competencies" class="list-group-item list-group-item-action">Competencies</a>
        <a href="/competency-levels" class="list-group-item list-group-item-action">Competency Levels</a>
        <a href="/employee-competencies" class="list-group-item list-group-item-action">Employee Competencies</a>
        <div class="list-group-item list-group-item-info mt-3"><strong>Goal Management:</strong></div>
        <a href="/goal-types" class="list-group-item list-group-item-action">Goal Types</a>
        <a href="/goal-statuses" class="list-group-item list-group-item-action">Goal Statuses</a>
        <a href="/goals" class="list-group-item list-group-item-action">Goals</a>
        <div class="list-group-item list-group-item-info mt-3"><strong>Feedback & Notifications:</strong></div>
        <a href="/feedback-types" class="list-group-item list-group-item-action">Feedback Types</a>
        <a href="/feedback-list" class="list-group-item list-group-item-action">All Feedback</a>
    </sec:authorize>
    <sec:authorize access="hasRole('EMPLOYEE')">
        <a href="/notifications" class="list-group-item list-group-item-action">Notifications</a>
    </sec:authorize>

    <sec:authorize access="!hasAnyRole('ADMIN', 'HR_SPECIALIST', 'MANAGER', 'EMPLOYEE')">
        <div class="alert alert-warning mt-3">No specific features are assigned to your role.</div>
    </sec:authorize>
</div>

<%@ include file="layout/footer.jsp" %> <%-- INCLUDE THE FOOTER --%>