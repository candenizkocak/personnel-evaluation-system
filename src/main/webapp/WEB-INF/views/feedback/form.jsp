<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Give Feedback" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/feedback/save" method="post" modelAttribute="feedback">
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>
            <form:hidden path="senderID"/>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Receiver</label>
                    <form:select path="receiverID" class="form-control" required="true">
                        <option value="">-- Select Employee --</option>
                        <c:forEach var="emp" items="${receivers}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6">
                    <label>Feedback Type</label>
                    <form:select path="feedbackTypeID" class="form-control" required="true">
                        <c:forEach var="type" items="${feedbackTypes}"><form:option value="${type.feedbackTypeID}" label="${type.name}"/></c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label>Feedback Content</label>
                <form:textarea path="content" class="form-control" rows="6" required="true" placeholder="Provide specific examples..."/>
            </div>
            <div class="form-group form-check">
                <form:checkbox path="isAnonymous" class="form-check-input"/>
                <label class="form-check-label">Submit Anonymously</label>
            </div>
            <button type="submit" class="btn btn-primary">Submit Feedback</button>
            <a href="/dashboard" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>