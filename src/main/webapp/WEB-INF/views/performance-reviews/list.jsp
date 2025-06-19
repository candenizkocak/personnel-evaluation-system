<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Performance Reviews" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Performance Reviews</h4>
        <a href="/performance-reviews/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Start New Review</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <table class="table table-hover">
            <thead class="thead-light">
            <tr><th>ID</th><th>Employee</th><th>Evaluator</th><th>Period</th><th>Status</th><th>Final Score</th><th>Actions</th></tr>
            </thead>
            <tbody>
            <c:forEach var="review" items="${reviews}">
                <tr>
                    <td>${review.reviewID}</td>
                    <td>${review.employeeFullName}</td>
                    <td>${review.evaluatorFullName}</td>
                    <td>${review.periodName}</td>
                    <td><span class="badge badge-secondary">${review.status}</span></td>
                    <td>${review.finalScore}</td>
                    <td>
                        <a href="/performance-reviews/fill/${review.reviewID}" class="btn btn-sm btn-info">
                                ${review.status == 'Draft' ? 'Continue' : 'View'}
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>