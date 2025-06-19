// src/main/webapp/WEB-INF/views/notifications/list.jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<div class="list-group">
    <c:choose>
        <c:when test="${not empty notifications}">
            <c:forEach var="notification" items="${notifications}">
                <div class="list-group-item list-group-item-action flex-column align-items-start ${notification.isRead ? '' : 'list-group-item-info'}">
                    <div class="d-flex w-100 justify-content-between">
                        <p class="mb-1">${notification.message}</p>
                        <small>${notification.created}</small>
                    </div>
                    <div class="mt-2">
                        <c:if test="${!notification.isRead}">
                            <a href="/notifications/mark-read/${notification.notificationID}" class="btn btn-sm btn-outline-primary">Mark as Read</a>
                        </c:if>
                        <a href="/notifications/delete/${notification.notificationID}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure you want to delete this notification?');">Delete</a>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info" role="alert">
                You have no notifications.
            </div>
        </c:otherwise>
    </c:choose>
</div>

<div class="mt-3">
    <a href="/dashboard" class="btn btn-secondary">Back to Dashboard</a>
</div>

<%@ include file="../layout/footer.jsp" %>