<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<h2 class="mb-4">Question Types</h2>

<%-- REMOVED Flash messages as there are no actions to trigger them --%>

<%-- REMOVED "Add New" button --%>

<div class="table-responsive">
    <table class="table table-striped table-bordered">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <%-- REMOVED Actions column header --%>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty questionTypes}">
                <c:forEach var="type" items="${questionTypes}">
                    <tr>
                        <td>${type.questionTypeID}</td>
                        <td>${type.name}</td>
                        <td>${type.description}</td>
                            <%-- REMOVED Actions column data --%>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="3" class="text-center">No question types found.</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%@ include file="../layout/footer.jsp" %>