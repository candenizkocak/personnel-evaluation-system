<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Permissions" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Permissions</h4>
        <sec:authorize access="hasRole('ADMIN')">
            <a href="/permissions/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New Permission</a>
        </sec:authorize>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr>
                    <th>ID</th><th>Name</th><th>Description</th>
                    <sec:authorize access="hasRole('ADMIN')"><th>Actions</th></sec:authorize>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="perm" items="${permissions}">
                    <tr>
                        <td>${perm.permissionID}</td>
                        <td>${perm.name}</td>
                        <td>${perm.description}</td>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td>
                                <a href="/permissions/edit/${perm.permissionID}" class="btn btn-sm btn-info">Edit</a>
                                <a href="/permissions/delete/${perm.permissionID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
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