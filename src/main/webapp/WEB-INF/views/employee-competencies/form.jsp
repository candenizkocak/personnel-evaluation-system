<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Assess Employee Competency" scope="request" />

<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">${pageTitle}</h4></div>
    <div class="card-body">
        <form:form action="/employee-competencies/save" method="post" modelAttribute="assessment">
            <form:errors path="*" cssClass="alert alert-danger" element="div"/>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Employee</label>
                    <form:select path="employeeID" class="form-control" required="true">
                        <option value="">-- Select Employee --</option>
                        <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6">
                    <label>Competency</label>
                    <form:select path="competencyID" id="competencySelect" class="form-control" required="true">
                        <option value="">-- Select Competency --</option>
                        <c:forEach var="comp" items="${competenciesWithLevels}"><form:option value="${comp.competencyID}" label="${comp.name}"/></c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label>Competency Level</label>
                <form:select path="levelID" id="levelSelect" class="form-control" required="true">
                    <option value="">-- Select a Competency First --</option>
                </form:select>
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Assessed By</label>
                    <form:select path="assessedByID" class="form-control" required="true">
                        <c:forEach var="emp" items="${employees}"><form:option value="${emp.employeeID}" label="${emp.firstName} ${emp.lastName}"/></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6">
                    <label>Assessment Date</label>
                    <form:input type="date" path="assessmentDate" class="form-control" required="true"/>
                </div>
            </div>
            <button type="submit" class="btn btn-success">Save Assessment</button>
            <a href="/employee-competencies" class="btn btn-secondary">Cancel</a>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>

<script>
    const competenciesData = {
        <c:forEach var="comp" items="${competenciesWithLevels}" varStatus="loop">
        "${comp.competencyID}": [
            <c:forEach var="level" items="${comp.levels}" varStatus="levelLoop">
            { id: "${level.levelID}", text: "Level ${level.level} - ${level.description}" }
            ${!levelLoop.last ? ',' : ''}
            </c:forEach>
        ]
        ${!loop.last ? ',' : ''}
        </c:forEach>
    };
    document.addEventListener('DOMContentLoaded', function() {
        const competencySelect = document.getElementById('competencySelect');
        const levelSelect = document.getElementById('levelSelect');
        competencySelect.addEventListener('change', function() {
            const selectedCompetencyId = this.value;
            levelSelect.innerHTML = '<option value="">-- Select Level --</option>';
            if (selectedCompetencyId && competenciesData[selectedCompetencyId]) {
                const levels = competenciesData[selectedCompetencyId];
                levels.forEach(function(level) {
                    const option = document.createElement('option');
                    option.value = level.id;
                    option.textContent = level.text;
                    levelSelect.appendChild(option);
                });
            }
        });
    });
</script>
<%@ include file="../layout/footer.jsp" %>