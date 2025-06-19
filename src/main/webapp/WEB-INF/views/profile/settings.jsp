<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Settings" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">Change Your Password</h4></div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

        <form:form action="/profile/settings/change-password" method="post" modelAttribute="passwordChangeDTO">
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>
            <div class="form-group">
                <label>Old Password</label>
                <form:password path="oldPassword" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>New Password</label>
                <form:password path="newPassword" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Confirm New Password</label>
                <form:password path="confirmPassword" class="form-control" required="true" />
            </div>
            <button type="submit" class="btn btn-primary">Change Password</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>