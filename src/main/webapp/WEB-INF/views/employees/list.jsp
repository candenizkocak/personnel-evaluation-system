<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <%-- For date formatting --%>

<h2 class="mb-4">Employees</h2>

<%-- Flash messages --%>
<c:if test="${not empty successMessage}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${successMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>
<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${errorMessage}
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">×</span>
        </button>
    </div>
</c:if>

<%-- Add New Employee button --%>
<sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
    <div class="mb-3">
        <a href="/employees/new" class="btn btn-primary">Add New Employee</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Hire Date</th>
            <th>Position</th>
            <th>Manager</th>
            <th>Active</th>
            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty employees}">
                <c:forEach var="emp" items="${employees}">
                    <tr>
                        <td>${emp.employeeID}</td>
                        <td>${emp.firstName}</td>
                        <td>${emp.lastName}</td>
                        <td>${emp.email}</td>
                        <td>${emp.phone}</td>
                        <td>${emp.hireDate}</td>
                        <td>${emp.positionTitle}</td>
                        <td>${emp.managerFullName}</td>
                        <td>
                            <c:if test="${emp.isActive}">Yes</c:if>
                            <c:if test="${not emp.isActive}">No</c:if>
                        </td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/employees/edit/${emp.employeeID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/employees/delete/${emp.employeeID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this employee? This cannot be undone if other entities reference them.');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="9" class="text-center">No employees found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>