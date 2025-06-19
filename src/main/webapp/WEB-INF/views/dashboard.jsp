<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="layout/header.jsp" %>
<c:set var="pageTitle" value="Dashboard" scope="request" />

<div class="card shadow-sm">
    <div class="card-header">
        <h4 class="mb-0">System Overview</h4>
    </div>
    <div class="card-body">
        <h5 class="card-title">Welcome to the Personnel Evaluation System</h5>
        <p class="card-text">Use the sidebar navigation to access different modules of the application based on your assigned roles.</p>
        <h6>Your Roles:</h6>
        <p>
            <sec:authentication property="principal.authorities" var="authorities"/>
            <c:forEach var="authority" items="${authorities}">
                <span class="badge badge-secondary">${authority.authority}</span>
            </c:forEach>
        </p>
        <a href="/profile/my-profile" class="btn btn-primary">View My Profile</a>
    </div>
</div>