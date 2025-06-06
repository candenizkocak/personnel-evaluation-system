<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %> <%-- Include the header layout --%>

<h2 class="mb-4">Departments</h2>

<%-- Display flash messages --%>
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

<%-- Add New Department button (only for authorized users) --%>
<sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
    <div class="mb-3">
        <a href="/departments/new" class="btn btn-primary">Add New Department</a>
    </div>
</sec:authorize>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Active</th>
            <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                <th>Actions</th>
            </sec:authorize>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty departments}">
                <c:forEach var="dept" items="${departments}">
                    <tr>
                        <td>${dept.departmentID}</td>
                        <td>${dept.name}</td>
                        <td>${dept.description}</td>
                        <td>
                            <c:if test="${dept.isActive}">Yes</c:if>
                            <c:if test="${not dept.isActive}">No</c:if>
                        </td>
                        <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                            <td>
                                <a href="/departments/edit/${dept.departmentID}" class="btn btn-sm btn-info mr-1">Edit</a>
                                <a href="/departments/delete/${dept.departmentID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this department?');">Delete</a>
                            </td>
                        </sec:authorize>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="5" class="text-center">No departments found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %> <%-- Include the footer layout --%>