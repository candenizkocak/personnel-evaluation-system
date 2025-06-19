<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Levels for: ${competency.name}" scope="request" />

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<div class="card shadow-sm mb-4">
    <div class="card-header"><h4 class="mb-0">Existing Levels</h4></div>
    <div class="card-body">
        <c:if test="${empty levels}"><p class="text-muted">No levels defined for this competency.</p></c:if>
        <c:if test="${not empty levels}">
            <ul class="list-group">
                <c:forEach var="level" items="${levels}">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div><strong>Level ${level.level}:</strong> ${level.description}</div>
                        <a href="/competency-levels/delete/${level.levelID}?competencyId=${competency.competencyID}" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Delete</a>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
</div>

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">Add New Level</h4></div>
    <div class="card-body">
        <form:form action="/competency-levels/add/${competency.competencyID}" method="post" modelAttribute="newLevel">
            <div class="form-row">
                <div class="form-group col-md-3">
                    <label>Level</label>
                    <form:input type="number" path="level" class="form-control" required="true" min="1"/>
                    <form:errors path="level" cssClass="text-danger"/>
                </div>
                <div class="form-group col-md-9">
                    <label>Description</label>
                    <form:input path="description" class="form-control" required="true"/>
                    <form:errors path="description" cssClass="text-danger"/>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Add Level</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<a href="/competencies" class="btn btn-secondary mt-3">Back to Competencies</a>
<%@ include file="../layout/footer.jsp" %>