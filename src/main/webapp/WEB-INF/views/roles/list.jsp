<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Roles" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Roles</h4>
        <sec:authorize access="hasRole('ADMIN')">
            <a href="/roles/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New Role</a>
        </sec:authorize>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr>
                    <th>ID</th><th>Name</th><th>Description</th><th>Permission IDs</th>
                    <sec:authorize access="hasRole('ADMIN')"><th>Actions</th></sec:authorize>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="role" items="${roles}">
                    <tr>
                        <td>${role.roleID}</td>
                        <td>${role.name}</td>
                        <td>${role.description}</td>
                        <td>
                            <c:forEach var="permissionId" items="${role.permissionIDs}" varStatus="loop">
                                <span class="badge badge-secondary">${permissionId}</span>${!loop.last ? ' ' : ''}
                            </c:forEach>
                        </td>
                        <sec:authorize access="hasRole('ADMIN')">
                            <td>
                                <a href="/roles/edit/${role.roleID}" class="btn btn-sm btn-info">Edit</a>
                                <a href="/roles/delete/${role.roleID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
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