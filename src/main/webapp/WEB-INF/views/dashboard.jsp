<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> <%-- For fn:escapeXml --%>
<%@ include file="layout/header.jsp" %>
<c:set var="pageTitle" value="Dashboard" scope="request" />

<div class="row">
    <div class="col-lg-8"> <%-- Main content area --%>
        <div class="card shadow-sm mb-4">
            <div class="card-header">
                <h4 class="mb-0">System Overview</h4>
            </div>
            <div class="card-body">
                <h5 class="card-title">Welcome to the Personnel Evaluation System, <sec:authentication property="principal.username"/>!</h5>
                <p class="card-text">Use the sidebar navigation to access different modules of the application based on your assigned roles.</p>
                <h6>Your Roles:</h6>
                <p>
                    <sec:authentication property="principal.authorities" var="authorities"/>
                    <c:forEach var="authority" items="${authorities}">
                        <span class="badge badge-secondary">${authority.authority}</span>
                    </c:forEach>
                </p>
                <a href="<c:url value='/profile/my-profile'/>" class="btn btn-primary">View My Profile</a>
            </div>
        </div>

        <%-- Performance Trend Chart for Employee --%>
        <sec:authorize access="isAuthenticated()"> <%-- More general check, controller logic ensures only employee's data is shown --%>
            <c:if test="${pageContext.request.userPrincipal != null && pageContext.request.userPrincipal.principal.employee != null}">
                <div class="card shadow-sm mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">My Performance Trend</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${hasChartData}">
                                <p class="d-none"> <%-- JSTL Debug Output (hidden on page, view source to see) --%>
                                    DEBUG: Has Chart Data is TRUE <br/>
                                    DEBUG Labels: ${reviewLabels} <br/>
                                    DEBUG Scores: ${reviewScores} <br/>
                                </p>
                                <div style="height: 350px; width: 100%;"> <%-- Ensure container has dimensions --%>
                                    <canvas id="performanceTrendChart"></canvas>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">No performance review data available to display a trend yet. Please ensure you have submitted reviews with final scores.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:if>
        </sec:authorize>

    </div>
    <div class="col-lg-4"> <%-- Sidebar-like area for quick links or stats --%>
        <div class="card shadow-sm mb-4">
            <div class="card-header">
                <h5 class="mb-0">Quick Actions</h5>
            </div>
            <div class="list-group list-group-flush">
                <a href="<c:url value='/performance-reviews/my-reviews'/>" class="list-group-item list-group-item-action">My Reviews</a>
                <a href="<c:url value='/goals'/>" class="list-group-item list-group-item-action">My Goals</a>
                <a href="<c:url value='/feedback/give'/>" class="list-group-item list-group-item-action">Give Feedback</a>
                <sec:authorize access="hasRole('MANAGER')">
                    <a href="<c:url value='/performance-reviews/new'/>" class="list-group-item list-group-item-action">Start New Review for Team</a>
                </sec:authorize>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script> <%-- Ensure Chart.js is loaded --%>

<script>
document.addEventListener('DOMContentLoaded', function () {
    const hasChartData = <c:out value="${hasChartData}"/>;
    console.log("Dashboard JS: hasChartData = " + hasChartData);

    if (hasChartData) {
        const reviewLabelsJs = [<c:forEach var="label" items="${reviewLabels}" varStatus="loop">'${fn:escapeXml(label)}'${!loop.last ? ',' : ''}</c:forEach>];
        const reviewScoresJs = [<c:forEach var="score" items="${reviewScores}" varStatus="loop">${score}${!loop.last ? ',' : ''}</c:forEach>];

        console.log("Dashboard JS: reviewLabelsJs = ", reviewLabelsJs);
        console.log("Dashboard JS: reviewScoresJs = ", reviewScoresJs);

        const ctx = document.getElementById('performanceTrendChart');
        if (ctx) {
            console.log("Dashboard JS: Canvas element 'performanceTrendChart' found.");
            try {
                new Chart(ctx.getContext('2d'), {
                    type: 'line',
                    data: {
                        labels: reviewLabelsJs,
                        datasets: [{
                            label: 'My Performance Final Score',
                            data: reviewScoresJs,
                            borderColor: 'rgb(54, 162, 235)', // A common blue
                            backgroundColor: 'rgba(54, 162, 235, 0.2)', // Light blue fill
                            tension: 0.1,
                            fill: true // Changed to true for area under line
                        }]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false, // Allow chart to fill container height
                        scales: {
                            y: {
                                beginAtZero: false, // Set to true if scores can be 0 and you want to show it
                                suggestedMin: 0,    // Start y-axis near the lowest score or 0
                                suggestedMax: 5     // Assuming scores are typically up to 5. Adjust if your scale is different (e.g., 100)
                            },
                            x: {
                                ticks: {
                                    autoSkip: true,
                                    maxRotation: 45, // Rotate labels if they overlap
                                    minRotation: 0,
                                    maxTicksLimit: (reviewLabelsJs.length > 10) ? 10 : reviewLabelsJs.length // Limit ticks if many labels
                                }
                            }
                        },
                        plugins: {
                            legend: {
                                display: true,
                                position: 'top',
                            },
                            tooltip: {
                                mode: 'index',
                                intersect: false,
                                callbacks: {
                                    title: function(tooltipItems) {
                                        return tooltipItems[0].label;
                                    },
                                    label: function(tooltipItem) {
                                        return ' Final Score: ' + tooltipItem.formattedValue;
                                    }
                                }
                            }
                        },
                        interaction: { // Improve hover interaction
                            mode: 'nearest',
                            axis: 'x',
                            intersect: false
                        }
                    }
                });
                console.log("Dashboard JS: Chart initialized successfully.");
            } catch (e) {
                console.error("Dashboard JS: Error initializing chart: ", e);
            }
        } else {
            console.error("Dashboard JS: Canvas element 'performanceTrendChart' NOT found!");
        }
    } else {
        console.log("Dashboard JS: No chart data to render (hasChartData is false).");
    }
});
</script>

<%@ include file="layout/footer.jsp" %>