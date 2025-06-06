<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} User</h2>

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
<c:if test="${not empty org.springframework.validation.BindingResult.user}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.user.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/users/save" method="post" modelAttribute="user">
    <form:hidden path="userID" />

    <div class="form-group">
        <label for="username">Username</label>
        <form:input path="username" class="form-control" id="username" required="true" />
        <form:errors path="username" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="password">Password ${isEdit ? '(Leave blank to keep current password)' : ''}</label>
            <%-- THIS IS THE CORRECTED LINE --%>
        <form:input type="password" path="password" class="form-control" id="password" required="${!isEdit ? 'true' : 'false'}" />
        <form:errors path="password" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="employeeID">Associated Employee</label>
        <form:select path="employeeID" class="form-control" id="employeeID" required="true">
            <option value="">-- Select Employee --</option>
            <c:forEach var="emp" items="${availableEmployees}">
                <form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName} (${emp.email})" />
            </c:forEach>
        </form:select>
        <form:errors path="employeeID" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label>Roles:</label>
        <div class="form-check-group">
            <c:choose>
                <c:when test="${not empty allRoles}">
                    <c:forEach var="role" items="${allRoles}">
                        <div class="form-check">
                            <form:checkbox path="roleIDs" value="${role.roleID}"
                                           class="form-check-input" id="role-${role.roleID}" />
                            <label class="form-check-label" for="role-${role.roleID}">
                                    ${role.name} - <em>${role.description}</em>
                            </label>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">No roles available. Please add roles first.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <form:errors path="roleIDs" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isLocked" class="form-check-input" id="isLocked" />
        <label class="form-check-label" for="isLocked">Is Locked (Prevent Login)</label>
        <form:errors path="isLocked" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/users" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>