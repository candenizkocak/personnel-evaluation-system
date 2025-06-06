<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} Position</h2>

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
<c:if test="${not empty org.springframework.validation.BindingResult.position}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.position.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/positions/save" method="post" modelAttribute="position">
    <form:hidden path="positionID" />

    <div class="form-group">
        <label for="title">Title</label>
        <form:input path="title" class="form-control" id="title" required="true" />
        <form:errors path="title" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="description">Description</label>
        <form:textarea path="description" class="form-control" id="description" rows="3" />
        <form:errors path="description" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="departmentID">Department</label>
        <form:select path="departmentID" class="form-control" id="departmentID" required="true">
            <option value="">-- Select Department --</option>
            <c:forEach var="dept" items="${departments}">
                <form:option value="${dept.departmentID}" label="${dept.name}" />
            </c:forEach>
        </form:select>
        <form:errors path="departmentID" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isManagement" class="form-check-input" id="isManagement" />
        <label class="form-check-label" for="isManagement">Is Management Position</label>
        <form:errors path="isManagement" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/positions" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>