<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Performance Review for ${reviewDetails.employeeFullName}" scope="request" />

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="alert alert-secondary">
    <strong>Form:</strong> ${reviewDetails.formTitle} | <strong>Period:</strong> ${reviewDetails.periodName} | <strong>Status:</strong> ${reviewDetails.status}
</div>

<form:form action="/performance-reviews/submit/${reviewDetails.reviewID}" method="post" modelAttribute="reviewSubmission">
    <c:forEach var="response" items="${reviewSubmission.reviewResponses}" varStatus="status">
        <div class="card mb-3 shadow-sm">
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
        <button type="submit" name="action" value="submit" class="btn btn-primary" onclick="return confirm('Submit this review?');">Submit Final Review</button>
        <a href="/performance-reviews" class="btn btn-light">Back to List</a>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>