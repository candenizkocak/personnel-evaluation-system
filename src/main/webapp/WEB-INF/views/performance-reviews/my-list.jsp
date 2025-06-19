<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="My Performance Reviews" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr><th>ID</th><th>Evaluator</th><th>Period</th><th>Status</th><th>Final Score</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="review" items="${reviews}">
                    <tr>
                        <td>${review.reviewID}</td>
                        <td>${review.evaluatorFullName}</td>
                        <td>${review.periodName}</td>
                        <td><span class="badge badge-info">${review.status}</span></td>
                        <td>${review.finalScore != null ? review.finalScore : 'N/A'}</td>
                        <td>
                            <a href="/performance-reviews/fill/${review.reviewID}" class="btn btn-sm btn-info">
                                    ${review.status == 'Draft' ? 'View/Edit Draft' : 'View Submitted'}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>