<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">Positions</h2>

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

<%-- Add New Position button (only for authorized users) --%>
<sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
    <div class="mb-3">
        <a href="/positions/new" class="btn btn-primary">Add New Position</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Department ID</th> <%-- Display raw ID for now, can be enriched later --%>
            <th>Management</th>
            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty positions}">
                <c:forEach var="pos" items="${positions}">
                    <tr>
                        <td>${pos.positionID}</td>
                        <td>${pos.title}</td>
                        <td>${pos.description}</td>
                        <td>${pos.departmentID}</td>
                        <td>
                            <c:if test="${pos.isManagement}">Yes</c:if>
                            <c:if test="${not pos.isManagement}">No</c:if>
                        </td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/positions/edit/${pos.positionID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/positions/delete/${pos.positionID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this position? This cannot be undone if other entities reference them (e.g., employees).');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="6" class="text-center">No positions found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>