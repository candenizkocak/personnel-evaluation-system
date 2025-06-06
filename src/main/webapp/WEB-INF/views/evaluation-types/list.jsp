<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">Evaluation Types</h2>

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

<%-- Add New button --%>
<sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
    <div class="mb-3">
        <a href="/evaluation-types/new" class="btn btn-primary">Add New Type</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty evaluationTypes}">
                <c:forEach var="type" items="${evaluationTypes}">
                    <tr>
                        <td>${type.typeID}</td>
                        <td>${type.name}</td>
                        <td>${type.description}</td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/evaluation-types/edit/${type.typeID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/evaluation-types/delete/${type.typeID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this evaluation type?');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="4" class="text-center">No evaluation types found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>