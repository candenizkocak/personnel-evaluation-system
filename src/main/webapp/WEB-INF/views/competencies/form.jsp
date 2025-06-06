<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h2 class="mb-4">${pageTitle}</h2>
<form:form action="/competencies/save" method="post" modelAttribute="competency">
    <form:hidden path="competencyID"/>
    <form:errors path="*" cssClass="alert alert-danger" element="div"/>
    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" required="true"/>
    </div>
    <div class="form-group">
        <label>Description</label>
        <form:textarea path="description" class="form-control" rows="3"/>
    </div>
    <div class="form-group">
        <label>Category</label>
        <form:select path="categoryID" class="form-control" required="true">
            <option value="">-- Select Category --</option>
            <c:forEach var="cat" items="${categories}"><form:option value="${cat.categoryID}" label="${cat.name}"/></c:forEach>
        </form:select>
    </div>
    <button type="submit" class="btn btn-success">Save</button>
    <a href="/competencies" class="btn btn-secondary">Cancel</a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>
<%@ include file="../layout/footer.jsp" %>