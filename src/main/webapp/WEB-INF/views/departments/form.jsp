<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} Department" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

        <form:form action="/departments/save" method="post" modelAttribute="department">
            <form:hidden path="departmentID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label for="name">Name</label>
                <form:input path="name" class="form-control" id="name" required="true" />
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <form:textarea path="description" class="form-control" id="description" rows="3" />
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isActive" class="form-check-input" id="isActive" />
                <label class="form-check-label" for="isActive">Is Active</label>
            </div>
            <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
            <a href="/departments" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>