<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2 class="mb-4">All Submitted Feedback</h2>
<div class="table-responsive">
    <table class="table table-striped">
        <thead class="thead-light">
        <tr>
            <th>Sender</th>
            <th>Receiver</th>
            <th>Type</th>
            <th>Date</th>
            <th>Content</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="fb" items="${feedbackList}">
            <tr>
                <td>${fb.senderFullName}</td>
                <td>${fb.receiverFullName}</td>
                <td>${fb.feedbackTypeName}</td>
                <td>${fb.submissionDate}</td>
                <td><c:out value="${fb.content}"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty feedbackList}">
            <tr><td colspan="5" class="text-center">No feedback has been submitted yet.</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
<%@ include file="../layout/footer.jsp" %>