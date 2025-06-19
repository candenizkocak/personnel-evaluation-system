<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Employee Competency Assessments" scope="request" />

<div class="card shadow-sm">
    <div class="card-header d-flex justify-content-between align-items-center">
        <h4 class="mb-0">Employee Competency Assessments</h4>
        <a href="/employee-competencies/new" class="btn btn-light btn-sm"><i class="fas fa-plus"></i> Add New Assessment</a>
    </div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-hover">
                <thead class="thead-light">
                <tr><th>Employee</th><th>Competency</th><th>Level Description</th><th>Assessment Date</th><th>Assessed By</th><th>Action</th></tr>
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
                            <a href="/employee-competencies/delete/${item.employeeID}/${item.competencyID}/${item.assessmentDate}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>