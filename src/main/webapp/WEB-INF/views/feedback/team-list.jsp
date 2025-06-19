<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Team Feedback" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

        <c:if test="${empty feedbackList}">
            <div class="alert alert-info">No feedback found for your team.</div>
        </c:if>

        <c:if test="${not empty feedbackList}">
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead class="thead-light">
                    <tr>
                        <th>Date</th>
                        <th>Sender</th>
                        <th>Receiver</th>
                        <th>Type</th>
                        <th style="width: 40%;">Content</th>
                        <th>Anonymous</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="fb" items="${feedbackList}">
                        <tr>
                            <td><fmt:formatDate value="${fb.submissionDate}" pattern="yyyy-MM-dd HH:mm" /></td>
                            <td>${fb.senderFullName}</td>
                            <td>${fb.receiverFullName}</td>
                            <td><span class="badge badge-primary">${fb.feedbackTypeName}</span></td>
                            <td><c:out value="${fb.content}"/></td>
                            <td><span class="badge ${fb.isAnonymous ? 'badge-success' : 'badge-secondary'}">${fb.isAnonymous ? 'Yes' : 'No'}</span></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>