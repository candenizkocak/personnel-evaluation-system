<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<h2 class="mb-4">${pageTitle}</h2>
<form:form action="/goal-statuses/save" method="post" modelAttribute="goalStatus">
    <form:hidden path="statusID"/>
    <div class="form-group">
        <label>Name</label>
        <form:input path="name" class="form-control" required="true"/>
        <form:errors path="name" cssClass="text-danger"/>
    </div>
    <div class="form-group">
        <label>Description</label>
        <form:textarea path="description" class="form-control" rows="3"/>
    </div>
    <button type="submit" class="btn btn-success">Save</button>
    <a href="/goal-statuses" class="btn btn-secondary">Cancel</a>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>
<%@ include file="../layout/footer.jsp" %>