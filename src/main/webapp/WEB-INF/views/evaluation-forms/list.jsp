<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Evaluation Forms" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Evaluation Forms</h4>
        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
            <a href="/evaluation-forms/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Create New Form</a>
        </sec:authorize>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Type</th>
                    <th>Active</th>
                    <th>Questions</th>
                    <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                        <th>Actions</th>
                    </sec:authorize>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="form" items="${evaluationForms}">
                    <tr>
                        <td>${form.formID}</td>
                        <td>${form.title}</td>
                        <td>${form.typeName}</td>
                        <td><span class="badge ${form.isActive ? 'badge-success' : 'badge-danger'}">${form.isActive ? 'Yes' : 'No'}</span></td>
                        <td>${form.questions.size()}</td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/evaluation-forms/edit/${form.formID}" class="btn btn-sm btn-info">Edit</a>
                                <a href="/evaluation-forms/delete/${form.formID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>