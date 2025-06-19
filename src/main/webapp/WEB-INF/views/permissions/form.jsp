<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} Permission" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/permissions/save" method="post" modelAttribute="permission">
            <form:hidden path="permissionID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Name</label>
                <form:input path="name" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Description</label>
                <form:textarea path="description" class="form-control" rows="3" />
            </div>
            <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
            <a href="/permissions" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>