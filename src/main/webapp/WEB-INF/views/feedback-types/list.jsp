<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Feedback Types" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Feedback Types</h4>
        <a href="/feedback-types/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <table class="table table-bordered">
            <thead class="thead-light"><tr><th>ID</th><th>Name</th><th>Description</th><th>Actions</th></tr></thead>
            <tbody>
            <c:forEach var="item" items="${feedbackTypes}">
                <tr>
                    <td>${item.feedbackTypeID}</td><td>${item.name}</td><td>${item.description}</td>
                    <td>
                        <a href="/feedback-types/edit/${item.feedbackTypeID}" class="btn btn-sm btn-info">Edit</a>
                        <a href="/feedback-types/delete/${item.feedbackTypeID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>