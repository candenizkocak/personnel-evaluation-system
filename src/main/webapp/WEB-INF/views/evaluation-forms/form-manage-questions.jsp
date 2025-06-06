<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>

<%-- Flash messages --%>
<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<%-- Display list of existing questions --%>
<div class="card mb-4">
    <div class="card-header">Current Questions</div>
    <div class="card-body">
        <c:if test="${empty evaluationForm.questions}">
            <p class="text-muted">No questions have been added to this form yet.</p>
        </c:if>
        <c:if test="${not empty evaluationForm.questions}">
            <table class="table table-sm">
                <thead>
                <tr>
                    <th>Order</th>
                    <th>Question Text</th>
                    <th>Type</th>
                    <th>Weight</th>
                    <th>Required</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="q" items="${evaluationForm.questions}">
                    <tr>
                        <td>${q.orderIndex}</td>
                        <td>${q.questionText}</td>
                        <td>${q.questionTypeID}</td> <%-- You can enrich this later --%>
                        <td>${q.weight}</td>
                        <td>${q.isRequired ? 'Yes' : 'No'}</td>
                        <td>
                            <a href="/evaluation-forms/${evaluationForm.formID}/questions/${q.questionID}/delete"
                               class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">
                                Delete
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

<%-- Form to add a NEW question --%>
<div class="card">
    <div class="card-header">Add New Question</div>
    <div class="card-body">
        <c:if test="${questionError}"><form:errors path="newQuestion.*" cssClass="alert alert-danger" element="div"/></c:if>
        <form:form action="/evaluation-forms/${evaluationForm.formID}/questions/add" method="post" modelAttribute="newQuestion">
            <div class="form-group">
                <label>Question Text</label>
                <form:textarea path="questionText" class="form-control" rows="2" required="true"/>
                <form:errors path="questionText" cssClass="text-danger"/>
            </div>
            <div class="form-row">
                <div class="form-group col-md-4">
                    <label>Question Type</label>
                    <form:select path="questionTypeID" class="form-control" required="true">
                        <option value="">-- Select Type --</option>
                        <c:forEach var="qType" items="${questionTypes}"><form:option value="${qType.questionTypeID}" label="${qType.name}"/></c:forEach>
                    </form:select>
                    <form:errors path="questionTypeID" cssClass="text-danger"/>
                </div>
                <div class="form-group col-md-3">
                    <label>Weight</label>
                    <form:input type="number" path="weight" class="form-control" value="1" min="0" required="true"/>
                    <form:errors path="weight" cssClass="text-danger"/>
                </div>
                <div class="form-group col-md-3">
                    <label>Order Index</label>
                    <form:input type="number" path="orderIndex" class="form-control" value="${evaluationForm.questions.size()}" min="0" required="true"/>
                    <form:errors path="orderIndex" cssClass="text-danger"/>
                </div>
                <div class="form-group col-md-2 d-flex align-items-center pt-3">
                    <div class="form-check">
                        <form:checkbox path="isRequired" class="form-check-input" checked="true"/>
                        <label class="form-check-label">Is Required</label>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Add Question</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<div class="mt-4">
    <a href="/evaluation-forms" class="btn btn-secondary">Back to Forms List</a>
</div>

<%@ include file="../layout/footer.jsp" %>