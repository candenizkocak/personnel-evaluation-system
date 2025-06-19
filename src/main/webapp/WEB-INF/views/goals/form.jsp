<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${pageTitle}" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/goals/save" method="post" modelAttribute="goal">
            <form:hidden path="goalID"/>
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>
            <div class="form-group">
                <label>Title</label>
                <form:input path="title" class="form-control" required="true"/>
            </div>
            <div class="form-group">
                <label>Description</label>
                <form:textarea path="description" class="form-control" rows="4"/>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Employee</label>
                    <form:select path="employeeID" class="form-control" required="true">
                        <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6">
                    <label>Goal Type</label>
                    <form:select path="goalTypeID" class="form-control" required="true">
                        <c:forEach var="type" items="${goalTypes}"><form:option value="${type.goalTypeID}" label="${type.name}"/></c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="form-row">
                <div class="form-group col-md-4"><label>Start Date</label><form:input type="date" path="startDate" class="form-control"/></div>
                <div class="form-group col-md-4"><label>Target Date</label><form:input type="date" path="targetDate" class="form-control"/></div>
                <div class="form-group col-md-4"><label>Completion Date</label><form:input type="date" path="completionDate" class="form-control"/></div>
            </div>
            <div class="form-group">
                <label>Status</label>
                <form:select path="statusID" class="form-control" required="true">
                    <c:forEach var="status" items="${goalStatuses}"><form:option value="${status.statusID}" label="${status.name}"/></c:forEach>
                </form:select>
            </div>
            <button type="submit" class="btn btn-success">Save Goal</button>
            <a href="/goals" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>