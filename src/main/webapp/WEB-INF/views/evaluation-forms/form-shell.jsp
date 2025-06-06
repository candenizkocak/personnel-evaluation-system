<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">${errorMessage}</div>
</c:if>

<form:form action="/evaluation-forms/save-shell" method="post" modelAttribute="evaluationForm">
    <div class="card">
        <div class="card-body">
            <div class="form-group">
                <label for="title">Title</label>
                <form:input path="title" class="form-control" id="title" required="true" />
                <form:errors path="title" cssClass="text-danger" />
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <form:textarea path="description" class="form-control" id="description" rows="3" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="typeID">Evaluation Type</label>
                    <form:select path="typeID" class="form-control" id="typeID" required="true">
                        <option value="">-- Select Type --</option>
                        <c:forEach var="type" items="${evaluationTypes}">
                            <form:option value="${type.typeID}" label="${type.name}" />
                        </c:forEach>
                    </form:select>
                    <form:errors path="typeID" cssClass="text-danger" />
                </div>
                <div class="form-group col-md-6 d-flex align-items-center pt-3">
                    <div class="form-check">
                        <form:checkbox path="isActive" class="form-check-input" id="isActive" checked="true"/>
                        <label class="form-check-label" for="isActive">Is Active</label>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="mt-3">
        <button type="submit" class="btn btn-primary">Create and Add Questions</button>
        <a href="/evaluation-forms" class="btn btn-secondary">Cancel</a>
    </div>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>