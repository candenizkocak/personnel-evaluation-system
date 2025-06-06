<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>
<form:form action="/employee-competencies/save" method="post" modelAttribute="assessment">
    <form:errors path="*" cssClass="alert alert-danger" element="div"/>

    <div class="form-row">
        <div class="form-group col-md-6">
            <label>Employee</label>
            <form:select path="employeeID" class="form-control" required="true">
                <option value="">-- Select Employee --</option>
                <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
            </form:select>
        </div>
        <div class="form-group col-md-6">
            <label>Competency</label>
            <form:select path="competencyID" class="form-control" required="true">
                <option value="">-- Select Competency --</option>
                <c:forEach var="comp" items="${competencies}"><form:option value="${comp.competencyID}" label="${comp.name}"/></c:forEach>
            </form:select>
        </div>
    </div>

    <div class="form-group">
        <label>Competency Level</label>
        <form:select path="levelID" class="form-control" required="true">
            <option value="">-- Select Level --</option>
            <%-- This is a simple implementation. A better UI would filter this with JavaScript. --%>
            <c:forEach var="level" items="${levels}"><form:option value="${level.levelID}" label="Level ${level.level} - ${level.description}"/></c:forEach>
        </form:select>
    </div>

    <div class="form-row">
        <div class="form-group col-md-6">
            <label>Assessed By</label>
            <form:select path="assessedByID" class="form-control" required="true">
                <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
            </form:select>
        </div>
        <div class="form-group col-md-6">
            <label>Assessment Date</label>
            <form:input type="date" path="assessmentDate" class="form-control" required="true"/>
        </div>
    </div>

    <button type="submit" class="btn btn-success">Save Assessment</button>
    <a href="/employee-competencies" class="btn btn-secondary">Cancel</a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>