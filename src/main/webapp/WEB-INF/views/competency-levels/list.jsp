<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">Manage Competency Levels</h2>
<p class="text-muted">Select a competency to view and manage its proficiency levels.</p>

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="list-group">
    <c:forEach var="competency" items="${competencies}">
        <a href="/competency-levels/manage/${competency.competencyID}" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">
            <div>
                <h5 class="mb-1">${competency.name}</h5>
                <small>Category: ${competency.categoryName}</small>
            </div>
            <span class="badge badge-primary badge-pill">${competency.levels.size()} Levels</span>
        </a>
    </c:forEach>
    <c:if test="${empty competencies}">
        <div class="list-group-item">No competencies found. Please create a competency first.</div>
    </c:if>
</div>

<%@ include file="../layout/footer.jsp" %>