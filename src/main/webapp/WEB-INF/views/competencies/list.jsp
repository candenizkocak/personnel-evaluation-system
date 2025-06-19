<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Competencies" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Competencies</h4>
        <a href="/competencies/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add Competency</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr><th>ID</th><th>Name</th><th>Category</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${competencies}">
                    <tr>
                        <td>${item.competencyID}</td>
                        <td>${item.name}</td>
                        <td>${item.categoryName}</td>
                        <td>
                            <a href="/competency-levels/manage/${item.competencyID}" class="btn btn-sm btn-secondary">Levels</a>
                            <a href="/competencies/edit/${item.competencyID}" class="btn btn-sm btn-info">Edit</a>
                            <a href="/competencies/delete/${item.competencyID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>