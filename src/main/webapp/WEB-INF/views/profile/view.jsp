// src/main/webapp/WEB-INF/views/profile/view.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>

<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<c:if test="${not empty employee}">
    <div class="card">
        <div class="card-header">
            <h4>${employee.firstName} ${employee.lastName}</h4>
        </div>
        <div class="card-body">
            <p><strong>Position:</strong> ${employee.positionTitle}</p>
            <p><strong>Manager:</strong> ${employee.managerFullName != null ? employee.managerFullName : 'N/A'}</p>
            <hr>
            <p><strong>Email:</strong> ${employee.email}</p>
            <p><strong>Phone:</strong> ${employee.phone}</p>
            <p><strong>Hire Date:</strong> ${employee.hireDate}</p>
        </div>
    </div>
</c:if>

<c:if test="${empty employee && empty errorMessage}">
    <div class="alert alert-warning">No profile information available.</div>
</c:if>

<div class="mt-3">
    <a href="/dashboard" class="btn btn-secondary">Back to Dashboard</a>
</div>

<%@ include file="../layout/footer.jsp" %>