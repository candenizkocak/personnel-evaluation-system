<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} Evaluation Period" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <form:form action="/evaluation-periods/save" method="post" modelAttribute="evaluationPeriod">
            <form:hidden path="periodID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Name</label>
                <form:input path="name" class="form-control" required="true" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Start Date</label>
                    <form:input type="date" path="startDate" class="form-control" required="true" />
                </div>
                <div class="form-group col-md-6">
                    <label>End Date</label>
                    <form:input type="date" path="endDate" class="form-control" required="true" />
                </div>
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isActive" class="form-check-input" />
                <label class="form-check-label">Is Active</label>
            </div>
            <button type="submit" class="btn btn-success">Save Period</button>
            <a href="/evaluation-periods" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>