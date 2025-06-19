<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} User" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <form:form action="/users/save" method="post" modelAttribute="user">
            <form:hidden path="userID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Username</label>
                <form:input path="username" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Password ${isEdit ? '(Leave blank to keep current)' : ''}</label>
                <form:password path="password" class="form-control" required="${!isEdit}" />
            </div>
            <div class="form-group">
                <label>Associated Employee</label>
                <form:select path="employeeID" class="form-control" required="true">
                    <option value="">-- Select Employee --</option>
                    <c:forEach var="emp" items="${availableEmployees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                </form:select>
            </div>
            <div class="form-group">
                <label>Roles:</label>
                <div>
                    <c:forEach var="role" items="${allRoles}">
                        <div class="form-check form-check-inline">
                            <form:checkbox path="roleIDs" value="${role.roleID}" class="form-check-input" id="role-${role.roleID}" />
                            <label class="form-check-label" for="role-${role.roleID}">${role.name}</label>
                        </div>
                    </c:forEach>
                </div>
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isLocked" class="form-check-input" />
                <label class="form-check-label">Is Locked</label>
            </div>
            <button type="submit" class="btn btn-success">Save User</button>
            <a href="/users" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>