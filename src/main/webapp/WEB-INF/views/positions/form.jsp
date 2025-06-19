<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="${isEdit ? 'Edit' : 'Add New'} Position" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <form:form action="/positions/save" method="post" modelAttribute="position">
            <form:hidden path="positionID" />
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Title</label>
                <form:input path="title" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Description</label>
                <form:textarea path="description" class="form-control" rows="3" />
            </div>
            <div class="form-group">
                <label>Department</label>
                <form:select path="departmentID" class="form-control" required="true">
                    <option value="">-- Select Department --</option>
                    <c:forEach var="dept" items="${departments}"><form:option value="${dept.departmentID}" label="${dept.name}" /></c:forEach>
                </form:select>
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isManagement" class="form-check-input" />
                <label class="form-check-label">Is Management Position</label>
            </div>
            <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
            <a href="/positions" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>