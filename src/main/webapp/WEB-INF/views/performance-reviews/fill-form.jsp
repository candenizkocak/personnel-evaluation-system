<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%-- Ensure JSTL core is imported --%>
<c:set var="pageTitle" value="Performance Review for ${reviewDetails.employeeFullName}" scope="request" />

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="alert alert-secondary">
    <strong>Form:</strong> ${reviewDetails.formTitle} | <strong>Period:</strong> ${reviewDetails.periodName} | <strong>Status:</strong> <span class="badge ${reviewDetails.status == 'Submitted' ? 'badge-success' : 'badge-warning'}">${reviewDetails.status}</span>
</div>

<%-- Determine if the form should be read-only --%>
<c:set var="isReadOnly" value="${reviewDetails.status == 'Submitted'}" />

<form:form action="/performance-reviews/submit/${reviewDetails.reviewID}" method="post" modelAttribute="reviewSubmission">
    <c:forEach var="response" items="${reviewSubmission.reviewResponses}" varStatus="status">
        <div class="card mb-3 shadow-sm">
            <div class="card-header">
                <strong>Question #${status.index + 1}:</strong> <c:out value="${response.questionText}" />
            </div>
            <div class="card-body">
                <form:hidden path="reviewResponses[${status.index}].responseID" />
                <form:hidden path="reviewResponses[${status.index}].questionID" />
                <form:hidden path="reviewResponses[${status.index}].questionTypeID" />

                <c:choose>
                    <c:when test="${response.questionTypeID == 1}"> <%-- Text Question --%>
                        <div class="form-group">
                            <label for="responseText-${status.index}">Your Response</label>
                            <form:textarea path="reviewResponses[${status.index}].responseText" id="responseText-${status.index}" class="form-control" rows="4" readonly="${isReadOnly}"/>
                        </div>
                        <form:hidden path="reviewResponses[${status.index}].numericResponse" value=""/>
                    </c:when>

                    <c:when test="${response.questionTypeID == 2}"> <%-- Numeric (1-5) Question --%>
                        <div class="form-group">
                            <label for="numericResponse-${status.index}">Rating (1-5)</label>
                            <form:select path="reviewResponses[${status.index}].numericResponse" id="numericResponse-${status.index}" class="form-control" disabled="${isReadOnly}">
                                <form:option value="" label="-- Select Rating --"/>
                                <form:option value="1.00" label="1 - Needs Significant Improvement"/>
                                <form:option value="2.00" label="2 - Needs Some Improvement"/>
                                <form:option value="3.00" label="3 - Meets Expectations"/>
                                <form:option value="4.00" label="4 - Exceeds Expectations"/>
                                <form:option value="5.00" label="5 - Outstanding"/>
                            </form:select>
                        </div>
                        <form:hidden path="reviewResponses[${status.index}].responseText" value=""/>
                    </c:when>

                    <c:when test="${response.questionTypeID == 3}"> <%-- Yes/No Question --%>
                        <div class="form-group">
                            <label>Your Response</label>
                            <div>
                                <div class="form-check form-check-inline">
                                    <form:radiobutton path="reviewResponses[${status.index}].numericResponse" id="yes-${status.index}" value="1.00" class="form-check-input" disabled="${isReadOnly}"/>
                                    <label class="form-check-label" for="yes-${status.index}">Yes</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <form:radiobutton path="reviewResponses[${status.index}].numericResponse" id="no-${status.index}" value="0.00" class="form-check-input" disabled="${isReadOnly}"/>
                                    <label class="form-check-label" for="no-${status.index}">No</label>
                                </div>
                                <div class="form-check form-check-inline">
                                    <form:radiobutton path="reviewResponses[${status.index}].numericResponse" id="na-${status.index}" value="" class="form-check-input" disabled="${isReadOnly}"/>
                                    <label class="form-check-label" for="na-${status.index}">N/A</label>
                                </div>
                            </div>
                        </div>
                        <form:hidden path="reviewResponses[${status.index}].responseText" value=""/>
                    </c:when>

                    <c:otherwise> <%-- Fallback --%>
                        <p class="text-danger">
                            <strong>Unsupported Question Type (ID: ${response.questionTypeID})</strong> - Default inputs shown.
                        </p>
                        <div class="form-group">
                            <label for="responseText-${status.index}">Response Text (Fallback)</label>
                            <form:textarea path="reviewResponses[${status.index}].responseText" id="responseText-${status.index}" class="form-control" rows="3" readonly="${isReadOnly}"/>
                        </div>
                        <div class="form-group">
                            <label for="numericResponse-${status.index}">Numeric Score (Fallback)</label>
                            <form:input type="number" path="reviewResponses[${status.index}].numericResponse" id="numericResponse-${status.index}" class="form-control" step="0.01" readonly="${isReadOnly}"/>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </c:forEach>
    <div class="mt-4">
        <c:if test="${!isReadOnly}">
            <button type="submit" name="action" value="save" class="btn btn-secondary">Save Draft</button>
            <button type="submit" name="action" value="submit" class="btn btn-primary" onclick="return confirm('Are you sure you want to submit this review? Once submitted, it might not be editable.');">Submit Final Review</button>
        </c:if>
        <a href="/performance-reviews" class="btn btn-light">Back to List</a>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>