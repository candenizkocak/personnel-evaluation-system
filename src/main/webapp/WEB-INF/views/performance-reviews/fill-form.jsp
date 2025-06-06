<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>
<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<!-- Review Details Summary (uses reviewDetails object) -->
<div class="card bg-light mb-4">
    <div class="card-body">
        <h5 class="card-title">Review Details</h5>
        <p><strong>Form:</strong> ${reviewDetails.formTitle} | <strong>Period:</strong> ${reviewDetails.periodName} | <strong>Status:</strong> ${reviewDetails.status}</p>
    </div>
</div>

<!-- Main form now binds to 'reviewSubmission' -->
<form:form action="/performance-reviews/submit/${reviewDetails.reviewID}" method="post" modelAttribute="reviewSubmission">
    <c:forEach var="response" items="${reviewSubmission.reviewResponses}" varStatus="status">
        <div class="card mb-3">
            <div class="card-header">
                <strong>Question #${status.index + 1}:</strong> <c:out value="${response.questionText}" />
            </div>
            <div class="card-body">
                <form:hidden path="reviewResponses[${status.index}].questionID" />
                <form:hidden path="reviewResponses[${status.index}].questionText" />

                <div class="form-group">
                    <label>Response Text</label>
                    <form:textarea path="reviewResponses[${status.index}].responseText" class="form-control" rows="3"/>
                </div>
                <div class="form-group">
                    <label>Numeric Score (e.g., 1-5)</label>
                    <form:input type="number" path="reviewResponses[${status.index}].numericResponse" class="form-control" step="0.01"/>
                </div>
            </div>
        </div>
    </c:forEach>

    <div class="mt-4">
        <button type="submit" name="action" value="save" class="btn btn-secondary">Save Draft</button>
        <button type="submit" name="action" value="submit" class="btn btn-primary" onclick="return confirm('Are you sure you want to submit this review? It cannot be edited after submission.')">Submit Final Review</button>
        <a href="/performance-reviews" class="btn btn-light">Back to List</a>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>