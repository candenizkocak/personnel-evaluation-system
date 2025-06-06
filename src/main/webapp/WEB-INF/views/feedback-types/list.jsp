<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2 class="mb-4">Feedback Types</h2>
<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
<a href="/feedback-types/new" class="btn btn-primary mb-3">Add New Feedback Type</a>
<table class="table table-bordered">
    <thead class="thead-light"><tr><th>ID</th><th>Name</th><th>Description</th><th>Actions</th></tr></thead>
    <tbody>
    <c:forEach var="item" items="${feedbackTypes}">
        <tr>
            <td>${item.feedbackTypeID}</td>
            <td>${item.name}</td>
            <td>${item.description}</td>
            <td>
                <a href="/feedback-types/edit/${item.feedbackTypeID}" class="btn btn-sm btn-info">Edit</a>
                <a href="/feedback-types/delete/${item.feedbackTypeID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%@ include file="../layout/footer.jsp" %>