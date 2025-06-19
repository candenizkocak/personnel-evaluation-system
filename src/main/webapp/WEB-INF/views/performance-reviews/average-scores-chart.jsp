<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%-- For array joining --%>

<c:set var="pageTitle" value="Employee Average Performance Scores" scope="request" />

<div class="card shadow-sm">
    <div class="card-header">
        <h4 class="mb-0">${pageTitle}</h4>
    </div>
    <div class="card-body">
        <c:if test="${not empty averageScoresData}">
            <div style="width: 80%; margin: auto;">
                <canvas id="averageScoresChart"></canvas>
            </div>

            <h5 class="mt-5">Data Table</h5>
            <table class="table table-sm table-striped table-bordered mt-3">
                <thead class="thead-light">
                <tr>
                    <th>Employee</th>
                    <th>Average Score</th>
                    <th>Number of Reviews</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="scoreData" items="${averageScoresData}">
                    <tr>
                        <td>${scoreData.employeeFullName}</td>
                        <td><c:out value="${scoreData.averageScore}" /></td>
                        <td>${scoreData.reviewCount}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </c:if>
        <c:if test="${empty averageScoresData}">
            <div class="alert alert-info">No submitted reviews with scores found to calculate averages.</div>
        </c:if>
    </div>
</div>

<%-- Include Chart.js --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const averageScoresData = [<c:forEach var="data" items="${averageScoresData}" varStatus="loop">'${data.averageScore}'${!loop.last ? ',' : ''}</c:forEach>];
        const employeeNames = [<c:forEach var="data" items="${averageScoresData}" varStatus="loop">"${fn:escapeXml(data.employeeFullName)}"${!loop.last ? ',' : ''}</c:forEach>];

        if (averageScoresData.length > 0) {
            const ctx = document.getElementById('averageScoresChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar', // or 'line', 'pie', etc.
                data: {
                    labels: employeeNames,
                    datasets: [{
                        label: 'Average Final Score',
                        data: averageScoresData,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)', // Blue
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true, // Adjust as needed
                    scales: {
                        y: {
                            beginAtZero: true,
                            suggestedMax: 5 // Or 100 if your scores are 0-100
                        }
                    },
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top',
                        },
                        title: {
                            display: true,
                            text: 'Average Performance Review Scores by Employee'
                        }
                    }
                }
            });
        }
    });
</script>

<%@ include file="../layout/footer.jsp" %>