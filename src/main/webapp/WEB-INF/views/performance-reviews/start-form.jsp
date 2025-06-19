<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Start New Performance Review" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/performance-reviews/create-draft" method="post" modelAttribute="reviewStartDto">
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Employee to Review</label>
                <form:select path="employeeID" class="form-control" required="true">
                    <option value="">-- Select Employee --</option>
                    <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <label>Evaluator</label>
                <form:select path="evaluatorID" class="form-control" required="true">
                    <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <label>Evaluation Period</label>
                <form:select path="periodID" class="form-control" required="true">
                    <option value="">-- Select Period --</option>
                    <c:forEach var="p" items="${periods}"><form:option value="${p.periodID}" label="${p.name}"/></c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <label>Evaluation Form</label>
                <form:select path="formID" class="form-control" required="true">
                    <option value="">-- Select Form --</option>
                    <c:forEach var="f" items="${forms}"><form:option value="${f.formID}" label="${f.title}"/></c:forEach>
                </form:select>
            </div>
            <button type="submit" class="btn btn-primary">Create Draft and Proceed</button>
            <a href="/performance-reviews" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>