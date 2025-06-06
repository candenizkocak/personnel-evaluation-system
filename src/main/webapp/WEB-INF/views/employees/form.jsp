<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} Employee</h2>

<%-- Flash messages --%>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>

<%-- Display validation errors --%>
<c:if test="${not empty org.springframework.validation.BindingResult.employee}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.employee.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/employees/save" method="post" modelAttribute="employee">
    <form:hidden path="employeeID" />

    <div class="form-group">
        <label for="firstName">First Name</label>
        <form:input path="firstName" class="form-control" id="firstName" required="true" />
        <form:errors path="firstName" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="lastName">Last Name</label>
        <form:input path="lastName" class="form-control" id="lastName" required="true" />
        <form:errors path="lastName" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="email">Email</label>
        <form:input type="email" path="email" class="form-control" id="email" required="true" />
        <form:errors path="email" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="phone">Phone</label>
        <form:input path="phone" class="form-control" id="phone" />
        <form:errors path="phone" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="hireDate">Hire Date</label>
            <%-- Use HTML5 date input type. Value needs to be in yyyy-MM-dd format --%>
        <form:input type="date" path="hireDate" class="form-control" id="hireDate" required="true" />
        <form:errors path="hireDate" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="positionID">Position</label>
        <form:select path="positionID" class="form-control" id="positionID" required="true">
            <option value="">-- Select Position --</option>
            <c:forEach var="pos" items="${positions}">
                <form:option value="${pos.positionID}" label="${pos.title}" />
            </c:forEach>
        </form:select>
        <form:errors path="positionID" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="managerID">Manager (Optional)</label>
        <form:select path="managerID" class="form-control" id="managerID">
            <option value="">-- No Manager --</option>
            <c:forEach var="mgr" items="${potentialManagers}">
                <form:option value="${mgr.employeeID}" label="${mgr.firstName} ${mgr.lastName}" />
            </c:forEach>
        </form:select>
        <form:errors path="managerID" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isActive" class="form-check-input" id="isActive" />
        <label class="form-check-label" for="isActive">Is Active</label>
        <form:errors path="isActive" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/employees" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>