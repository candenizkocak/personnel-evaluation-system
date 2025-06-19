<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} Employee" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <form:form action="/employees/save" method="post" modelAttribute="employee">
            <form:hidden path="employeeID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>First Name</label>
                    <form:input path="firstName" class="form-control" required="true" />
                </div>
                <div class="form-group col-md-6">
                    <label>Last Name</label>
                    <form:input path="lastName" class="form-control" required="true" />
                </div>
            </div>
            <div class="form-group">
                <label>Email</label>
                <form:input type="email" path="email" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Phone</label>
                <form:input type="tel" path="phone" class="form-control" pattern="[0-9\-]+" title="Please enter only numbers and dashes."/>
            </div>
            <div class="form-group">
                <label>Hire Date</label>
                <form:input type="date" path="hireDate" class="form-control" required="true" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Position</label>
                    <form:select path="positionID" class="form-control" required="true">
                        <option value="">-- Select Position --</option>
                        <c:forEach var="pos" items="${positions}"><form:option value="${pos.positionID}" label="${pos.title}" /></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6">
                    <label>Manager (Optional)</label>
                    <form:select path="managerID" class="form-control">
                        <option value="">-- No Manager --</option>
                        <c:forEach var="mgr" items="${potentialManagers}"><form:option value="${mgr.employeeID}" label="${mgr.firstName} ${mgr.lastName}" /></c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isActive" class="form-check-input" />
                <label class="form-check-label">Is Active</label>
            </div>
            <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
            <a href="/employees" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>