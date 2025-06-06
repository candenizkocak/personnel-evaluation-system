<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2 class="mb-4">${isEdit ? 'Edit' : 'Create New'} Evaluation Form</h2>

<%-- Flash messages --%>
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

<%-- Display general validation errors (e.g., from reject() in controller) --%>
<c:if test="${not empty org.springframework.validation.BindingResult.evaluationForm}">
    <div class="alert alert-danger">
        <ul>
            <c:forEach var="error" items="${org.springframework.validation.BindingResult.evaluationForm.globalErrors}">
                <li><c:out value="${error.defaultMessage}"/></li>
            </c:forEach>
        </ul>
    </div>
</c:if>

<form:form action="/evaluation-forms/save" method="post" modelAttribute="evaluationForm">
    <form:hidden path="formID" />

    <div class="form-group">
        <label for="title">Title</label>
        <form:input path="title" class="form-control" id="title" required="true" />
        <form:errors path="title" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="description">Description</label>
        <form:textarea path="description" class="form-control" id="description" rows="3" />
        <form:errors path="description" cssClass="text-danger" />
    </div>

    <div class="form-group">
        <label for="typeID">Evaluation Type</label>
        <form:select path="typeID" class="form-control" id="typeID" required="true">
            <option value="">-- Select Type --</option>
            <c:forEach var="type" items="${evaluationTypes}">
                <form:option value="${type.typeID}" label="${type.name}" />
            </c:forEach>
        </form:select>
        <form:errors path="typeID" cssClass="text-danger" />
    </div>

    <div class="form-group form-check">
        <form:checkbox path="isActive" class="form-check-input" id="isActive" />
        <label class="form-check-label" for="isActive">Is Active</label>
        <form:errors path="isActive" cssClass="text-danger" />
    </div>

    <hr>
    <h4>Questions</h4>
    <div id="questionsContainer">
            <%-- Pre-existing questions (on edit or after validation error) --%>
        <c:forEach var="question" items="${evaluationForm.questions}" varStatus="i">
            <div class="card mb-3 question-block" data-index="${i.index}">
                <div class="card-header d-flex justify-content-between align-items-center">
                    Question #${i.index + 1}
                    <button type="button" class="btn btn-danger btn-sm remove-question">Remove</button>
                </div>
                <div class="card-body">
                    <form:hidden path="questions[${i.index}].questionID" /> <%-- Keep ID for existing questions --%>

                    <div class="form-group">
                        <label for="questions[${i.index}].questionText">Question Text</label>
                        <form:textarea path="questions[${i.index}].questionText" class="form-control" rows="2" required="true"/>
                        <form:errors path="questions[${i.index}].questionText" cssClass="text-danger" />
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label for="questions[${i.index}].questionTypeID">Question Type</label>
                            <form:select path="questions[${i.index}].questionTypeID" class="form-control" required="true">
                                <option value="">-- Select Type --</option>
                                <c:forEach var="qType" items="${questionTypes}">
                                    <form:option value="${qType.questionTypeID}" label="${qType.name}" />
                                </c:forEach>
                            </form:select>
                            <form:errors path="questions[${i.index}].questionTypeID" cssClass="text-danger" />
                        </div>
                        <div class="form-group col-md-4">
                            <label for="questions[${i.index}].weight">Weight</label>
                            <form:input type="number" path="questions[${i.index}].weight" class="form-control" value="0" min="0" required="true"/> <%-- Set default value to 0 for numeric --%>
                            <form:errors path="questions[${i.index}].weight" cssClass="text-danger" />
                        </div>
                        <div class="form-group col-md-4">
                            <label for="questions[${i.index}].orderIndex">Order Index</label>
                            <form:input type="number" path="questions[${i.index}].orderIndex" class="form-control" value="0" min="0" required="true"/> <%-- Set default value to 0 for numeric --%>
                            <form:errors path="questions[${i.index}].orderIndex" cssClass="text-danger" />
                        </div>
                    </div>
                    <div class="form-group form-check">
                        <form:checkbox path="questions[${i.index}].isRequired" class="form-check-input" />
                        <label class="form-check-label" for="questions[${i.index}].isRequired">Is Required</label>
                        <form:errors path="questions[${i.index}].isRequired" cssClass="text-danger" />
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <button type="button" class="btn btn-secondary mb-3" id="addQuestion">Add Question</button>
    <hr>

    <button type="submit" class="btn btn-success">${isEdit ? 'Update' : 'Create'}</button>
    <a href="/evaluation-forms" class="btn btn-secondary">Cancel</a>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form:form>

<%-- HIDDEN TEMPLATE FOR NEW QUESTIONS (PROCESSED BY SERVER) --%>
<template id="questionTemplate">
    <div class="card mb-3 question-block">
        <div class="card-header d-flex justify-content-between align-items-center">
            Question #PLACEHOLDER_INDEX
            <button type="button" class="btn btn-danger btn-sm remove-question">Remove</button>
        </div>
        <div class="card-body">
            <input type="hidden" name="questions[PLACEHOLDER_INDEX].questionID" value="" />
            <div class="form-group">
                <label for="questions[PLACEHOLDER_INDEX].questionText">Question Text</label>
                <textarea name="questions[PLACEHOLDER_INDEX].questionText" class="form-control" rows="2" required></textarea>
                <span id="questions[PLACEHOLDER_INDEX].questionText.errors" class="text-danger"></span>
            </div>
            <div class="form-row">
                <div class="form-group col-md-4">
                    <label for="questions[PLACEHOLDER_INDEX].questionTypeID">Question Type</label>
                    <select name="questions[PLACEHOLDER_INDEX].questionTypeID" class="form-control" required>
                        <option value="">-- Select Type --</option>
                        <c:forEach var="qType" items="${questionTypes}">
                            <option value="${qType.questionTypeID}">${qType.name}</option>
                        </c:forEach>
                    </select>
                    <span id="questions[PLACEHOLDER_INDEX].questionTypeID.errors" class="text-danger"></span>
                </div>
                <div class="form-group col-md-4">
                    <label for="questions[PLACEHOLDER_INDEX].weight">Weight</label>
                    <input type="number" name="questions[PLACEHOLDER_INDEX].weight" class="form-control" value="0" min="0" required />
                    <span id="questions[PLACEHOLDER_INDEX].weight.errors" class="text-danger"></span>
                </div>
                <div class="form-group col-md-4">
                    <label for="questions[PLACEHOLDER_INDEX].orderIndex">Order Index</label>
                    <input type="number" name="questions[PLACEHOLDER_INDEX].orderIndex" class="form-control" value="0" min="0" required />
                    <span id="questions[PLACEHOLDER_INDEX].orderIndex.errors" class="text-danger"></span>
                </div>
            </div>
            <div class="form-group form-check">
                <input type="checkbox" name="questions[PLACEHOLDER_INDEX].isRequired" class="form-check-input" value="true" id="questions[PLACEHOLDER_INDEX].isRequired"/>
                <input type="hidden" name="_questions[PLACEHOLDER_INDEX].isRequired" value="on"/> <%-- Hidden input for checkbox to send 'false' if unchecked --%>
                <label class="form-check-label" for="questions[PLACEHOLDER_INDEX].isRequired">Is Required</label>
                <span id="questions[PLACEHOLDER_INDEX].isRequired.errors" class="text-danger"></span>
            </div>
        </div>
    </div>
</template>

<%@ include file="../layout/footer.jsp" %>

<%-- JavaScript for dynamic question addition/removal and re-indexing --%>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        let questionCounter = ${evaluationForm.questions.size()};
        const questionsContainer = document.getElementById('questionsContainer');
        const questionTemplate = document.getElementById('questionTemplate').content;

        function updateQuestionIndices() {
            document.querySelectorAll('#questionsContainer .question-block').forEach((block, idx) => {
                // Update header text for question number
                const headerTextNode = block.querySelector('.card-header').childNodes[0];
                if (headerTextNode && headerTextNode.nodeType === Node.TEXT_NODE) {
                    headerTextNode.nodeValue = `Question #${idx + 1}`;
                }

                // Update all input/select/textarea elements' name and id attributes
                block.querySelectorAll('input, select, textarea').forEach(element => {
                    // Regex to find 'questions[ANY_NUMBER]' and replace with 'questions[CURRENT_INDEX]'
                    const currentName = element.getAttribute('name');
                    const currentId = element.getAttribute('id');

                    if (currentName && currentName.startsWith('questions[')) {
                        element.setAttribute('name', currentName.replace(/questions\[\d+\]/, `questions[${idx}]`));
                    }
                    // For hidden inputs that send `_fieldName`, like checkboxes' hidden fields
                    if (currentName && currentName.startsWith('_questions[')) {
                        element.setAttribute('name', currentName.replace(/_questions\[\d+\]/, `_questions[${idx}]`));
                    }

                    if (currentId && currentId.startsWith('questions[')) {
                        element.setAttribute('id', currentId.replace(/questions\[\d+\]/, `questions[${idx}]`));
                    }
                });

                // Update all label's 'for' attributes
                block.querySelectorAll('label[for^="questions["]').forEach(label => {
                    const currentFor = label.getAttribute('for');
                    if (currentFor && currentFor.startsWith('questions[')) {
                        label.setAttribute('for', currentFor.replace(/questions\[\d+\]/, `questions[${idx}]`));
                    }
                });

                // Update id for Spring error spans
                block.querySelectorAll('span.text-danger').forEach(errorSpan => {
                    const currentId = errorSpan.getAttribute('id');
                    if (currentId && currentId.startsWith('questions[')) {
                        errorSpan.setAttribute('id', currentId.replace(/questions\[\d+\]/, `questions[${idx}]`));
                    }
                });
            });
        }

        document.getElementById('addQuestion').addEventListener('click', function() {
            const clone = document.importNode(questionTemplate, true);
            const newQuestionBlock = clone.firstElementChild; // Get the actual div element (the card)

            // IMPORTANT: Before appending, set the initial data-index attribute
            newQuestionBlock.setAttribute('data-index', questionCounter);

            // Now, update names and IDs of elements within the cloned block
            newQuestionBlock.querySelector('.card-header').childNodes[0].nodeValue = `Question #${questionCounter + 1}`; // Update header text
            newQuestionBlock.querySelectorAll('input, select, textarea').forEach(element => {
                const originalName = element.getAttribute('name');
                const originalId = element.getAttribute('id');

                if (originalName) {
                    element.setAttribute('name', originalName.replace(/PLACEHOLDER_INDEX/g, questionCounter));
                }
                if (originalId) {
                    element.setAttribute('id', originalId.replace(/PLACEHOLDER_INDEX/g, questionCounter));
                }
            });

            newQuestionBlock.querySelectorAll('label[for]').forEach(label => {
                const originalFor = label.getAttribute('for');
                if (originalFor) {
                    label.setAttribute('for', originalFor.replace(/PLACEHOLDER_INDEX/g, questionCounter));
                }
            });

            newQuestionBlock.querySelectorAll('span.text-danger').forEach(errorSpan => {
                const originalId = errorSpan.getAttribute('id');
                if (originalId) {
                    errorSpan.setAttribute('id', originalId.replace(/PLACEHOLDER_INDEX/g, questionCounter));
                }
            });

            questionsContainer.appendChild(finalizedQuestionBlock); // This line is likely from previous iteration, should be questionsContainer.appendChild(newQuestionBlock);

            // Add event listener for the new remove button
            newQuestionBlock.querySelector('.remove-question').addEventListener('click', function() {
                this.closest('.question-block').remove();
                updateQuestionIndices(); // Re-index after removal
            });

            questionCounter++;
            updateQuestionIndices(); // Call to re-index all, including newly added and existing
        });


        // Add event listeners to existing remove buttons (for pre-populated questions)
        document.querySelectorAll('#questionsContainer .remove-question').forEach(button => {
            button.addEventListener('click', function() {
                this.closest('.question-block').remove();
                updateQuestionIndices(); // Re-index after removal
            });
        });

        // Initial re-indexing on load, important for displaying existing validation errors correctly after redirect
        updateQuestionIndices();
    });
</script>