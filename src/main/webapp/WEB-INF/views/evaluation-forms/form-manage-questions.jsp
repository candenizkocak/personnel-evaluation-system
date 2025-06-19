<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="pageTitle" value="Manage Form: ${evaluationForm.title}" scope="request" />

<c:if test="${not empty successMessage}"><div class="alert alert-success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger">${errorMessage}</div></c:if>

<!-- Form to Edit Main Details -->
<div class="card shadow-sm mb-4">
    <div class="card-header"><h4 class="mb-0">Edit Form Details</h4></div>
    <div class="card-body">
        <form:form action="/evaluation-forms/update-details/${evaluationForm.formID}" method="post" modelAttribute="evaluationForm">
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label>Title</label>
                <form:input path="title" class="form-control" required="true" />
            </div>
            <div class="form-group">
                <label>Description</label>
                <form:textarea path="description" class="form-control" rows="2" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label>Evaluation Type</label>
                    <form:select path="typeID" class="form-control" required="true">
                        <c:forEach var="type" items="${evaluationTypes}"><form:option value="${type.typeID}" label="${type.name}" /></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6 d-flex align-items-center pt-3">
                    <div class="form-check">
                        <form:checkbox path="isActive" class="form-check-input"/>
                        <label class="form-check-label">Is Active</label>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Update Details</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>

<!-- Display list of existing questions -->
<div class="card shadow-sm mb-4">
    <div class="card-header"><h4 class="mb-0">Current Questions</h4></div>
    <div class="card-body">
        <c:if test="${not empty evaluationForm.questions}">
            <table class="table table-sm table-hover">
                <thead class="thead-light">
                <tr><th>Order</th><th>Question Text</th><th>Type ID</th><th>Weight</th><th>Required</th><th>Actions</th></tr>
                </thead>
                <tbody>
                <c:forEach var="q" items="${evaluationForm.questions}">
                    <tr id="question-row-${q.questionID}" data-question-id="${q.questionID}" data-question-text="${q.questionText}" data-question-type-id="${q.questionTypeID}" data-weight="${q.weight}" data-order-index="${q.orderIndex}" data-is-required="${q.isRequired}">
                        <td>${q.orderIndex}</td><td><c:out value="${q.questionText}" /></td><td>${q.questionTypeID}</td>
                        <td>${q.weight}</td><td>${q.isRequired ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn btn-info btn-sm edit-question-btn" data-toggle="modal" data-target="#editQuestionModal">Edit</button>
                            <a href="/evaluation-forms/${evaluationForm.formID}/questions/${q.questionID}/delete" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
        <c:if test="${empty evaluationForm.questions}"><p class="text-muted">No questions have been added.</p></c:if>
    </div>
</div>

<!-- Form to add a NEW question -->
<div class="card shadow-sm">
    <div class="card-header"><h4 class="mb-0">Add New Question</h4></div>
    <div class="card-body">
        <c:if test="${questionError}"><form:errors path="newQuestion.*" cssClass="alert alert-danger" element="div"/></c:if>
        <form:form action="/evaluation-forms/${evaluationForm.formID}/questions/add" method="post" modelAttribute="newQuestion">
            <div class="form-group"><label>Question Text</label><form:textarea path="questionText" class="form-control" rows="2" required="true"/></div>
            <div class="form-row">
                <div class="form-group col-md-4"><label>Question Type</label><form:select path="questionTypeID" class="form-control" required="true"><option value="">-- Select --</option><c:forEach var="qType" items="${questionTypes}"><form:option value="${qType.questionTypeID}" label="${qType.name}"/></c:forEach></form:select></div>
                <div class="form-group col-md-3"><label>Weight</label><form:input type="number" path="weight" class="form-control" value="1" min="0" required="true"/></div>
                <div class="form-group col-md-3"><label>Order Index</label><form:input type="number" path="orderIndex" class="form-control" value="${evaluationForm.questions.size()}" min="0" required="true"/></div>
                <div class="form-group col-md-2 d-flex align-items-center pt-3"><div class="form-check"><form:checkbox path="isRequired" class="form-check-input" checked="true"/><label class="form-check-label">Is Required</label></div></div>
            </div>
            <button type="submit" class="btn btn-primary">Add Question</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>
<div class="mt-4"><a href="/evaluation-forms" class="btn btn-secondary">Back to Forms List</a></div>

<!-- Edit Question Modal -->
<div class="modal fade" id="editQuestionModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form id="editQuestionForm" method="post">
                <div class="modal-header"><h5 class="modal-title">Edit Question</h5><button type="button" class="close" data-dismiss="modal"><span>×</span></button></div>
                <div class="modal-body">
                    <input type="hidden" name="formId" value="${evaluationForm.formID}">
                    <div class="form-group"><label>Question Text</label><textarea name="questionText" class="form-control" rows="3" required></textarea></div>
                    <div class="form-row">
                        <div class="form-group col-md-6"><label>Question Type</label><select name="questionTypeID" class="form-control" required><c:forEach var="qType" items="${questionTypes}"><option value="${qType.questionTypeID}">${qType.name}</option></c:forEach></select></div>
                        <div class="form-group col-md-6"><label>Weight</label><input type="number" name="weight" class="form-control" min="0" required></div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6"><label>Order Index</label><input type="number" name="orderIndex" class="form-control" min="0" required></div>
                        <div class="form-group col-md-6 d-flex align-items-center pt-3"><div class="form-check"><input type="checkbox" name="isRequired" value="true" class="form-check-input"/><label class="form-check-label">Is Required</label></div></div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary">Save changes</button>
                </div>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </div>
    </div>
</div>

<%@ include file="../layout/footer.jsp" %>
<script>
    document.addEventListener('DOMContentLoaded', function () {
        $('#editQuestionModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); var questionRow = button.closest('tr');
            var questionId = questionRow.data('question-id'), questionText = questionRow.data('question-text'),
                questionTypeId = questionRow.data('question-type-id'), weight = questionRow.data('weight'),
                orderIndex = questionRow.data('order-index'), isRequired = questionRow.data('is-required');
            var modal = $(this);
            modal.find('.modal-title').text('Edit Question #' + questionId);
            modal.find('textarea[name="questionText"]').val(questionText);
            modal.find('select[name="questionTypeID"]').val(questionTypeId);
            modal.find('input[name="weight"]').val(weight);
            modal.find('input[name="orderIndex"]').val(orderIndex);
            modal.find('input[name="isRequired"]').prop('checked', isRequired);
            modal.find('#editQuestionForm').attr('action', '/evaluation-questions/update/' + questionId);
        });
    });
</script>