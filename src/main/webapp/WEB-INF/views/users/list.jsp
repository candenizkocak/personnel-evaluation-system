<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%-- Removed @taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" as it causes conversion errors for java.time types --%>

<h2 class="mb-4">Users</h2>

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

<%-- Add New User button (only for ADMIN) --%>
<sec:authorize access="hasRole('ADMIN')">
    <div class="mb-3">
        <a href="/users/new" class="btn btn-primary">Add New User</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Employee Name</th>
            <th>Roles</th>
            <th>Last Login</th>
            <th>Locked</th>
            <sec:authorize access="hasRole('ADMIN')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty users}">
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.userID}</td>
                        <td>${user.username}</td>
                        <td>${user.employeeName}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty user.roles}">
                                    <c:forEach var="roleName" items="${user.roles}" varStatus="loop">
                                        <span class="badge badge-info">${roleName}</span><c:if test="${!loop.last}"> </c:if>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <em>No Roles</em>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${user.lastLogin}</td> <%-- FIXED: Directly display LocalDateTime --%>
                        <td>
                            <c:if test="${user.isLocked}">Yes</c:if>
                            <c:if test="${not user.isLocked}">No</c:if>
                        </td>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td>
                                <a href="/users/edit/${user.userID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/users/delete/${user.userID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this user? This cannot be undone.');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="7" class="text-center">No users found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>