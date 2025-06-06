<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2 class="mb-4">Goals</h2>
<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
<a href="/goals/new" class="btn btn-primary mb-3">Set New Goal</a>
<div class="table-responsive">
    <table class="table table-hover">
        <thead class="thead-light">
        <tr>
            <th>Title</th><th>Employee</th><th>Type</th><th>Status</th><th>Target Date</th><th>Progress</th><th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="goal" items="${goals}">
            <tr>
                <td>${goal.title}</td>
                <td>${goal.employeeID}</td> <%-- Enrich later with employee name --%>
                <td>${goal.goalTypeID}</td> <%-- Enrich later --%>
                <td>${goal.statusID}</td> <%-- Enrich later --%>
                <td>${goal.targetDate}</td>
                <td>
                    <div class="progress">
                        <div class="progress-bar" role="progressbar" style="width: ${goal.progress}%;" aria-valuenow="${goal.progress}" aria-valuemin="0" aria-valuemax="100">${goal.progress}%</div>
                    </div>
                </td>
                <td>
                    <a href="/goals/edit/${goal.goalID}" class="btn btn-sm btn-info">Update</a>
                    <a href="/goals/delete/${goal.goalID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?')">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@ include file="../layout/footer.jsp" %>