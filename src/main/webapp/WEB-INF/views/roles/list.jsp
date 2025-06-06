<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">Roles</h2>

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

<%-- Add New Role button (only for ADMIN) --%>
<sec:authorize access="hasRole('ADMIN')">
    <div class="mb-3">
        <a href="/roles/new" class="btn btn-primary">Add New Role</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Permissions</th>
            <sec:authorize access="hasRole('ADMIN')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty roles}">
                <c:forEach var="role" items="${roles}">
                    <tr>
                        <td>${role.roleID}</td>
                        <td>${role.name}</td>
                        <td>${role.description}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty role.permissionIDs}">
                                    <c:forEach var="permissionId" items="${role.permissionIDs}" varStatus="loop">
                                        ${permissionId}<c:if test="${!loop.last}">, </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <em>No Permissions</em>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td>
                                <a href="/roles/edit/${role.roleID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/roles/delete/${role.roleID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this role? This cannot be undone and may affect associated users.');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5" class="text-center">No roles found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>