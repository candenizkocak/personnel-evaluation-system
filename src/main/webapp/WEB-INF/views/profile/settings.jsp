// src/main/webapp/WEB-INF/views/profile/settings.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="card">
    <div class="card-header">Change Your Password</div>
    <div class="card-body">
        <form:form action="/profile/settings/change-password" method="post" modelAttribute="passwordChangeDTO">
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>
            <div class="form-group">
                <label for="oldPassword">Old Password</label>
                <form:password path="oldPassword" class="form-control" id="oldPassword" required="true" />
            </div>
            <div class="form-group">
                <label for="newPassword">New Password</label>
                <form:password path="newPassword" class="form-control" id="newPassword" required="true" />
            </div>
            <div class="form-group">
                <label for="confirmPassword">Confirm New Password</label>
                <form:password path="confirmPassword" class="form-control" id="confirmPassword" required="true" />
            </div>
            <button type="submit" class="btn btn-primary">Change Password</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>