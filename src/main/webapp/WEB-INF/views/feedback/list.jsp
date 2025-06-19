<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="All Submitted Feedback" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">All Submitted Feedback</h4></div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="thead-light">
                <tr><th>Date</th><th>Sender</th><th>Receiver</th><th>Type</th><th style="width: 40%;">Content</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="fb" items="${feedbackList}">
                    <tr>
                        <td>${fb.submissionDate}</td>
                        <td>${fb.senderFullName}</td>
                        <td>${fb.receiverFullName}</td>
                        <td><span class="badge badge-primary">${fb.feedbackTypeName}</span></td>
                        <td><c:out value="${fb.content}"/></td>
                        <td>
                            <a href="/feedback/delete/${fb.feedbackID}" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure?');"><i class="fas fa-trash"></i> Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>