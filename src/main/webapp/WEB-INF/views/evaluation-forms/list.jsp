<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">Evaluation Forms</h2>

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
        <a href="/evaluation-forms/new" class="btn btn-primary">Create New Form</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Description</th>
            <th>Type</th> <%-- CHANGED FROM Type ID --%>
            <th>Active</th>
            <th>Questions Count</th>
            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty evaluationForms}">
                <c:forEach var="form" items="${evaluationForms}">
                    <tr>
                        <td>${form.formID}</td>
                        <td>${form.title}</td>
                        <td>${form.description}</td>
                        <td>${form.typeName}</td> <%-- CHANGED FROM typeID --%>
                        <td>
                            <c:if test="${form.isActive}">Yes</c:if>
                            <c:if test="${not form.isActive}">No</c:if>
                        </td>
                        <td>${form.questions != null ? form.questions.size() : 0}</td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/evaluation-forms/edit/${form.formID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/evaluation-forms/delete/${form.formID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this evaluation form and all its questions?');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="7" class="text-center">No evaluation forms found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>