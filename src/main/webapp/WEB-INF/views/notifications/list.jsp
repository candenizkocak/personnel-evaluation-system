<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<c:set var="pageTitle" value="Notifications" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">Notifications</h4></div>
    <div class="card-body">
        <c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
        <c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>
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
                                <a href="/notifications/delete/${notification.notificationID}" class="btn btn-sm btn-outline-danger" onclick="return confirm('Are you sure?');">Delete</a>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">You have no notifications.</div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>
<%@ include file="../layout/footer.jsp" %>