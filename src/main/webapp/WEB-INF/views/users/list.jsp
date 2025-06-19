<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Manage Users" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">User Accounts</h4>
        <a href="/users/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New User</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr>
                    <th>Username</th><th>Employee Name</th><th>Roles</th><th>Last Login</th><th>Locked</th><th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="user" items="${users}">
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.employeeName}</td>
                        <td>
                            <c:forEach var="roleName" items="${user.roles}">
                                <span class="badge badge-info">${roleName}</span>
                            </c:forEach>
                        </td>
                        <td>${user.lastLogin}</td>
                        <td><span class="badge ${user.isLocked ? 'badge-danger' : 'badge-success'}">${user.isLocked ? 'Yes' : 'No'}</span></td>
                        <td>
                            <a href="/users/edit/${user.userID}" class="btn btn-sm btn-info">Edit</a>
                            <a href="/users/delete/${user.userID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>