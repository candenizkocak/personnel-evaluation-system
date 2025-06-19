<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Goals" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Goals</h4>
        <a href="/goals/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Set New Goal</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr><th>Title</th><th>Employee ID</th><th>Type</th><th>Status</th><th>Target Date</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="goal" items="${goals}">
                    <tr>
                        <td>${goal.title}</td>
                        <td>${goal.employeeID}</td>
                        <td>${goal.goalTypeName}</td>
                        <td><span class="badge badge-info">${goal.statusName}</span></td>
                        <td>${goal.targetDate}</td>
                        <td>
                            <a href="/goals/edit/${goal.goalID}" class="btn btn-sm btn-info">Update</a>
                            <a href="/goals/delete/${goal.goalID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>