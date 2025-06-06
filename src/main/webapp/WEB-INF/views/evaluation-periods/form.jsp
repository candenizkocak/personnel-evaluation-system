<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} Evaluation Period</h2>

<%-- Flash messages --%>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>

<%-- Display validation errors --%>
<c:if test="${not empty org.springframework.validation.BindingResult.evaluationPeriod}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.evaluationPeriod.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/evaluation-periods/save" method="post" modelAttribute="evaluationPeriod">
    <form:hidden path="periodID" />

    <div class="form-group">
        <label for="name">Name</label>
        <form:input path="name" class="form-control" id="name" required="true" />
        <form:errors path="name" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="startDate">Start Date</label>
        <form:input type="date" path="startDate" class="form-control" id="startDate" required="true" />
        <form:errors path="startDate" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="endDate">End Date</label>
        <form:input type="date" path="endDate" class="form-control" id="endDate" required="true" />
        <form:errors path="endDate" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isActive" class="form-check-input" id="isActive" />
        <label class="form-check-label" for="isActive">Is Active</label>
        <form:errors path="isActive" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/evaluation-periods" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>