<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Start New Performance Review" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/performance-reviews/create-draft" method="post" modelAttribute="reviewStartDto">
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>

            <div class="form-group">
                <label>Employee to Review</label>
                <form:select path="employeeID" class="form-control" required="true">
                    <option value="">-- Select Employee --</option>
                    <c:forEach var="emp" items="${employees}">
                        <%-- Prevent selecting self for review, unless an admin is reviewing themselves for some reason (edge case) --%>
                        <c:if test="${currentEvaluator == null || emp.employeeID != currentEvaluator.employeeID}">
                            <form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/>
                        </c:if>
                    </c:forEach>
                </form:select>
                <form:errors path="employeeID" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <label>Evaluator</label>
                <c:choose>
                    <c:when test="${reviewStartDto.evaluatorID != null && currentEvaluator != null}">
                        <input type="text" class="form-control" value="${currentEvaluator.firstName} ${currentEvaluator.lastName}" readonly>
                        <form:hidden path="evaluatorID" /> <%-- This carries the ID --%>
                    </c:when>
                    <c:otherwise>
                        <%-- Fallback if evaluator couldn't be set (e.g., admin not linked to employee, though unlikely for this role) --%>
                        <%-- Or if you want to allow ADMIN/HR to select an evaluator manually in some scenarios --%>
                        <p class="text-danger">Evaluator could not be automatically determined. Please ensure your user profile is linked to an employee.
                            If you are an Admin/HR and need to select an evaluator, this feature needs to be implemented.
                        </p>
                        <%-- If you want to allow Admin/HR to select: --%>
                        <%--
                        <form:select path="evaluatorID" class="form-control" required="true">
                            <option value="">-- Select Evaluator --</option>
                            <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                        </form:select>
                        --%>
                        <form:hidden path="evaluatorID" /> <%-- Ensure it's still there, even if empty, to avoid binding issues --%>
                    </c:otherwise>
                </c:choose>
                <form:errors path="evaluatorID" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <label>Evaluation Period</label>
                <form:select path="periodID" class="form-control" required="true">
                    <option value="">-- Select Period --</option>
                    <c:forEach var="p" items="${periods}"><form:option value="${p.periodID}" label="${p.name}"/></c:forEach>
                </form:select>
                <form:errors path="periodID" cssClass="text-danger"/>
            </div>

            <div class="form-group">
                <label>Evaluation Form</label>
                <form:select path="formID" class="form-control" required="true">
                    <option value="">-- Select Form --</option>
                    <c:forEach var="f" items="${forms}"><form:option value="${f.formID}" label="${f.title}"/></c:forEach>
                </form:select>
                <form:errors path="formID" cssClass="text-danger"/>
            </div>

            <button type="submit" class="btn btn-primary">Create Draft and Proceed</button>
            <a href="/performance-reviews" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>