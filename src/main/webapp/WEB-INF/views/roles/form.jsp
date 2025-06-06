<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Add New'} Role</h2>

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
<c:if test="${not empty org.springframework.validation.BindingResult.role}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.role.allErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/roles/save" method="post" modelAttribute="role">
    <form:hidden path="roleID" />

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

    <div class="form-group">
        <label>Permissions:</label>
        <div class="form-check-group"> <%-- Custom class for styling --%>
            <c:choose>
                <c:when test="${not empty allPermissions}">
                    <c:forEach var="permission" items="${allPermissions}">
                        <div class="form-check">
                            <form:checkbox path="permissionIDs" value="${permission.permissionID}"
                                           class="form-check-input" id="permission-${permission.permissionID}" />
                            <label class="form-check-label" for="permission-${permission.permissionID}">
                                    ${permission.name} - <em>${permission.description}</em>
                            </label>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-muted">No permissions available. Please add permissions first.</p>
                </c:otherwise>
            </c:choose>
        </div>
        <form:errors path="permissionIDs" cssClass="text-danger" />
    </div>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/roles" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%@ include file="../layout/footer.jsp" %>