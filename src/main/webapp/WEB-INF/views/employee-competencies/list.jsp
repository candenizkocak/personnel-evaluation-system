<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">Employee Competency Assessments</h2>
<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<a href="/employee-competencies/new" class="btn btn-primary mb-3">Add New Assessment</a>

<div class="table-responsive">
    <table class="table table-hover">
        <thead class="thead-light">
        <tr>
            <th>Employee</th>
            <th>Competency</th>
            <th>Level Description</th>
            <th>Assessment Date</th>
            <th>Assessed By</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${assessments}">
            <tr>
                <td>${item.employeeName}</td>
                <td>${item.competencyName}</td>
                <td>${item.levelDescription}</td>
                <td>${item.assessmentDate}</td>
                <td>${item.assessedByName}</td>
                <td>
                    <a href="/employee-competencies/delete/${item.employeeID}/${item.competencyID}/${item.assessmentDate}"
                       class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this assessment?')">
                        Delete
                    </a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty assessments}">
            <tr><td colspan="6" class="text-center">No assessments found.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>