<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="layout/header.jsp" %>
<%-- Set the page title for the header --%>
<c:set var="pageTitle" value="Home" scope="request" />

<div class="container mt-5">
    <h1 class="text-center">Welcome to Personnel Evaluation System</h1>
    <p class="text-center">This is the homepage. Please login to access the system features.</p>
    <div class="text-center">
        <a href="/login" class="btn btn-primary">Login</a>
        <a href="/register" class="btn btn-secondary">Register</a>
    </div>
</div>

<%@ include file="layout/footer.jsp" %>