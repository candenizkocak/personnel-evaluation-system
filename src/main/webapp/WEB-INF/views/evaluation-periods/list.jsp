<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Evaluation Periods" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Evaluation Periods</h4>
        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
            <a href="/evaluation-periods/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New</a>
        </sec:authorize>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr>
                    <th>ID</th><th>Name</th><th>Start Date</th><th>End Date</th><th>Active</th>
                    <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')"><th>Actions</th></sec:authorize>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="period" items="${evaluationPeriods}">
                    <tr>
                        <td>${period.periodID}</td>
                        <td>${period.name}</td>
                        <td>${period.startDate}</td>
                        <td>${period.endDate}</td>
                        <td><span class="badge ${period.isActive ? 'badge-success' : 'badge-danger'}">${period.isActive ? 'Yes' : 'No'}</span></td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/evaluation-periods/edit/${period.periodID}" class="btn btn-sm btn-info">Edit</a>
                                <a href="/evaluation-periods/delete/${period.periodID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
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