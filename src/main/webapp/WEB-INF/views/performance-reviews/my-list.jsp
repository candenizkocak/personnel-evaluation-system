// src/main/webapp/WEB-INF/views/performance-reviews/my-list.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">${pageTitle}</h2>

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="table-responsive">
    <table class="table table-hover">
        <thead class="thead-light">
        <tr>
            <th>ID</th>
            <th>Evaluator</th>
            <th>Period</th>
            <th>Status</th>
            <th>Final Score</th>
            <th>Actions</th>
        </tr>
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
        <c:if test="${empty reviews}">
            <tr><td colspan="6" class="text-center">You have no performance reviews.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>