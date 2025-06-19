<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Create New Evaluation Form" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <form:form action="/evaluation-forms/save-shell" method="post" modelAttribute="evaluationForm">
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Title</label>
                <form:input path="title" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Description</label>
                <form:textarea path="description" class="form-control" rows="3" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Evaluation Type</label>
                    <form:select path="typeID" class="form-control" required="true">
                        <option value="">-- Select Type --</option>
                        <c:forEach var="type" items="${evaluationTypes}"><form:option value="${type.typeID}" label="${type.name}" /></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6 d-flex align-items-center pt-3">
                    <div class="form-check">
                        <form:checkbox path="isActive" class="form-check-input" checked="true"/>
                        <label class="form-check-label">Is Active</label>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Create and Add Questions</button>
            <a href="/evaluation-forms" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>