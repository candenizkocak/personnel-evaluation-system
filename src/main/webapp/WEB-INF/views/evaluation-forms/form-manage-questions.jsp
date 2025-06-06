<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${pageTitle}</h2>

<%-- Flash messages --%>
<c:if test="${not empty successMessage}"><div class="alert alert-success alert-dismissible fade show" role="alert">${successMessage}<button type="button" class="close" data-dismiss="alert">×</button></div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert-danger alert-dismissible fade show" role="alert">${errorMessage}<button type="button" class="close" data-dismiss="alert">×</button></div></c:if>

<%-- Form to Edit Main Details --%>
<div class="card mb-4">
    <div class="card-header">Edit Form Details</div>
    <div class="card-body">
        <form:form action="/evaluation-forms/update-details/${evaluationForm.formID}" method="post" modelAttribute="evaluationForm">
            <form:errors path="*" cssClass="alert alert-danger" element="div" />
            <div class="form-group">
                <label for="title">Title</label>
                <form:input path="title" class="form-control" id="title" required="true" />
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <form:textarea path="description" class="form-control" id="description" rows="2" />
            </div>
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="typeID">Evaluation Type</label>
                    <form:select path="typeID" class="form-control" id="typeID" required="true">
                        <c:forEach var="type" items="${evaluationTypes}"><form:option value="${type.typeID}" label="${type.name}" /></c:forEach>
                    </form:select>
                </div>
                <div class="form-group col-md-6 d-flex align-items-center pt-3">
                    <div class="form-check">
                        <form:checkbox path="isActive" class="form-check-input" id="isActive"/>
                        <label class="form-check-label" for="isActive">Is Active</label>
                    </div>
                </div>
            </div>
            <button type="submit" class="btn btn-primary">Update Details</button>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form:form>
    </div>
</div>


<%-- Display list of existing questions --%>
<div class="card mb-4">
    <div class="card-header">Current Questions</div>
    <div class="card-body">
        <c:if test="${empty evaluationForm.questions}"><p class="text-muted">No questions have been added.</p></c:if>
        <c:if test="${not empty evaluationForm.questions}">
            <table class="table table-sm table-hover">
                <thead class="thead-light">
                <tr>
                    <th>Order</th><th>Question Text</th><th>Type</th><th>Weight</th><th>Required</th><th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="q" items="${evaluationForm.questions}">
                    <tr id="question-row-${q.questionID}"
                        data-question-id="${q.questionID}"
                        data-question-text="${q.questionText}"
                        data-question-type-id="${q.questionTypeID}"
                        data-weight="${q.weight}"
                        data-order-index="${q.orderIndex}"
                        data-is-required="${q.isRequired}">
                        <td>${q.orderIndex}</td>
                        <td><c:out value="${q.questionText}" /></td>
                        <td>${q.questionTypeID}</td>
                        <td>${q.weight}</td>
                        <td>${q.isRequired ? 'Yes' : 'No'}</td>
                        <td>
                            <button class="btn btn-info btn-sm edit-question-btn" data-toggle="modal" data-target="#editQuestionModal">Edit</button>
                            <a href="/evaluation-forms/${evaluationForm.formID}/questions/${q.questionID}/delete" class="btn btn-danger btn-sm" onclick="return confirm('Are you sure?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</div>

<%-- Form to add a NEW question --%>
<div class="card">
    <div class="card-header">Add New Question</div>
    <div class="card-body">
        <c:if test="${questionError}"><form:errors path="newQuestion.*" cssClass="alert alert-danger" element="div"/></c:if>
        <form:form action="/evaluation-forms/${evaluationForm.formID}/questions/add" method="post" modelAttribute="newQuestion">
            <div class="form-group">
                <label>Question Text</label>
                <form:textarea path="questionText" class="form-control" rows="2" required="true"/>
            </div>
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
<div class="modal fade" id="editQuestionModal" tabindex="-1" role="dialog" aria-labelledby="editQuestionModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form id="editQuestionForm" method="post">
                <div class="modal-header">
                    <h5 class="modal-title" id="editQuestionModalLabel">Edit Question</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="formId" value="${evaluationForm.formID}">
                    <div class="form-group">
                        <label for="editQuestionText">Question Text</label>
                        <textarea id="editQuestionText" name="questionText" class="form-control" rows="3" required></textarea>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="editQuestionTypeID">Question Type</label>
                            <select id="editQuestionTypeID" name="questionTypeID" class="form-control" required>
                                <c:forEach var="qType" items="${questionTypes}"><option value="${qType.questionTypeID}">${qType.name}</option></c:forEach>
                            </select>
                        </div>
                        <div class="form-group col-md-6">
                            <label for="editWeight">Weight</label>
                            <input type="number" id="editWeight" name="weight" class="form-control" min="0" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="editOrderIndex">Order Index</label>
                            <input type="number" id="editOrderIndex" name="orderIndex" class="form-control" min="0" required>
                        </div>
                        <div class="form-group col-md-6 d-flex align-items-center pt-3">
                            <div class="form-check">
                                <input type="checkbox" id="editIsRequired" name="isRequired" value="true" class="form-check-input"/>
                                <label class="form-check-label" for="editIsRequired">Is Required</label>
                            </div>
                        </div>
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
        // Handle modal population when an edit button is clicked
        $('#editQuestionModal').on('show.bs.modal', function (event) {
            var button = $(event.relatedTarget); // Button that triggered the modal
            var questionRow = button.closest('tr');

            // Extract info from data-* attributes
            var questionId = questionRow.data('question-id');
            var questionText = questionRow.data('question-text');
            var questionTypeId = questionRow.data('question-type-id');
            var weight = questionRow.data('weight');
            var orderIndex = questionRow.data('order-index');
            var isRequired = questionRow.data('is-required');

            // Update the modal's content.
            var modal = $(this);
            modal.find('.modal-title').text('Edit Question #' + questionId);
            modal.find('#editQuestionText').val(questionText);
            modal.find('#editQuestionTypeID').val(questionTypeId);
            modal.find('#editWeight').val(weight);
            modal.find('#editOrderIndex').val(orderIndex);
            modal.find('#editIsRequired').prop('checked', isRequired);

            // Set the form's action URL dynamically
            var formAction = '/evaluation-questions/update/' + questionId;
            modal.find('#editQuestionForm').attr('action', formAction);
        });
    });
</script>