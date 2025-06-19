<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="My Profile" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${employee.firstName} ${employee.lastName}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <c:if test="${not empty employee}">
            <dl class="row">
                <dt class="col-sm-3">Position</dt>
                <dd class="col-sm-9">${employee.positionTitle}</dd>

                <dt class="col-sm-3">Manager</dt>
                <dd class="col-sm-9">${empty employee.managerFullName ? 'N/A' : employee.managerFullName}</dd>

                <dt class="col-sm-3">Email</dt>
                <dd class="col-sm-9">${employee.email}</dd>

                <dt class="col-sm-3">Phone</dt>
                <dd class="col-sm-9">${empty employee.phone ? 'N/A' : employee.phone}</dd>

                <dt class="col-sm-3">Hire Date</dt>
                <dd class="col-sm-9">${employee.hireDate}</dd>
            </dl>
        </c:if>
        <c:if test="${empty employee && empty errorMessage}">
            <div class="alert alert-warning">No profile information available.</div>
        </c:if>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>