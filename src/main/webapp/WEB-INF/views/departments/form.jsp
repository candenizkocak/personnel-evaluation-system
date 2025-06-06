<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> <%-- For Spring Form tags --%>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} Department</h2>

<%-- Display flash messages --%>
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

<%-- Display validation errors if any --%>
<c:if test="${not empty org.springframework.validation.BindingResult.department}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.department.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<%-- Spring Form for handling DTO and validation errors --%>
<form:form action="/departments/save" method="post" modelAttribute="department">
    <form:hidden path="departmentID" /> <%-- Hidden field for ID (important for edit) --%>

    <div class="form-group">
        <label for="name">Name</label>
        <form:input path="name" class="form-control" id="name" required="true" />
        <form:errors path="name" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="description">Description</label>
        <form:textarea path="description" class="form-control" id="description" rows="3" />
        <form:errors path="description" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isActive" class="form-check-input" id="isActive" />
        <label class="form-check-label" for="isActive">Is Active</label>
        <form:errors path="isActive" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/departments" class="btn btn-secondary">Cancel</a>

    <%-- CSRF token for POST requests --%>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>