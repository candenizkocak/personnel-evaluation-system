<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%-- Removed the fmt taglib as it's causing the error --%>

<h2 class="mb-4">${pageTitle}</h2>

<%-- Flash messages for success/error notifications --%>
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

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>Date</th>
            <th>Sender</th>
            <th>Receiver</th>
            <th>Type</th>
            <th style="width: 40%;">Content</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="fb" items="${feedbackList}">
            <tr>
                    <%--
                      THE FIX: Directly output the LocalDateTime object.
                      It will call .toString() which produces a standard ISO format (e.g., 2024-02-01T09:00).
                      This avoids the problematic conversion to java.util.Date.
                    --%>
                <td>${fb.submissionDate}</td>
                <td>${fb.senderFullName}</td>
                <td>${fb.receiverFullName}</td>
                <td><span class="badge badge-primary">${fb.feedbackTypeName}</span></td>
                <td><c:out value="${fb.content}"/></td>
                <td>
                        <%-- Delete button visible only to authorized users --%>
                    <sec:authorize access="hasAnyRole('ADMIN', 'HR_SPECIALIST')">
                        <a href="/feedback/delete/${fb.feedbackID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this feedback entry?');">
                            <i class="fas fa-trash"></i> Delete
                        </a>
                    </sec:authorize>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty feedbackList}">
            <tr><td colspan="6" class="text-center">No feedback has been submitted yet.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>

<div class="mt-3">
    <a href="/dashboard" class="btn btn-secondary">Back to Dashboard</a>
</div>

<%@ include file="../layout/footer.jsp" %>