<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
	<c:url value="/grouping/intrumentAndPerameters" var="clinpathPerams"></c:url>
	<form:form action="${clinpathPerams}" method="POST"
 		id="clinpathPerameters"> 
 		<input type="hidden" name="studyId" id="studyId" value="${sm.id }" /> 
 	</form:form> 


	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Study Design</b>
				<c:choose>
					<c:when test="${roleName eq 'SD'}">
						<c:choose>
							<c:when test="${sm.studyStatus eq 'Reviewed' or sm.studyStatus eq 'In-Progress'}">
								<font color='green'>Study has Reviewed on : <fmt:formatDate value="${sm.reviewedOn}" pattern="${dateFormatJsp}"/></font>
							</c:when>
							<c:when test="${sm.studyStatus eq 'Comments Required' }">
								<font color='green'>Study  has comments from Reviewer ${sm.reviewedBy}  on: <fmt:formatDate value="${sm.reviewedOn}" pattern="${dateFormatJsp}"/></font>
								<font>${sm.reviewComment }</font>
							</c:when>
							<c:when test="${sm.studyStatus eq 'In-Review' }">
								<font color='green'>Study is waiting for review</font>
							</c:when>
							<c:otherwise>
								<font color='green'>Study in ${sm.studyStatus} state</font>
							</c:otherwise>
						</c:choose>		
					</c:when>
					<c:when test="${roleName eq 'QA'}">
						<c:choose>
							<c:when test="${sm.studyStatus eq 'Reviewed' or sm.studyStatus eq 'In-Progress'}">
								<font color='green'>Study has Reviewed on : <fmt:formatDate value="${sm.reviewedOn}" pattern="${dateFormatJsp}"/></font>
							</c:when>
							<c:when test="${sm.studyStatus eq 'Comments Required' }">
								<font color='green'>Study  has sent for comments on : <fmt:formatDate value="${sm.reviewedOn}" pattern="${dateFormatJsp}"/></font>
							</c:when>
							<c:when test="${sm.studyStatus eq 'In-Review' }">
								<font color='green'>Study  waiting for review</font>
							</c:when>
						</c:choose>
								
					</c:when>			
				</c:choose>
				
			</h3>
		</div>
		<font color="red" id="principalInvestigatormsg"></font>
		<div class="box-body">
			<table class="table table-bordered table-striped">
				<tr>
					<td>Study Number</td>
					<td><input type="text" name="studyNo" class="form-control"
						value="${sm.studyNo}" disabled="disabled"></td>
					<td>Title</td>
					<td><input type="text" name="studyDesc" class="form-control"
						value="${sm.studyDesc}" disabled="disabled"></td>
					<td>No. Of Groups</td>
					<td><input type="text" name="noOfGroups" class="form-control"
						value="${sm.noOfGroups}" disabled="disabled"></td>
				</tr>
				<tr>
					<td>Total No Of Animals</td>
					<td><input type="text" name="subjects" class="form-control"
						value="${sm.subjects}" disabled="disabled"></td>
					<td>Species</td>
					<td><input type="text" name="species" class="form-control"
						value="${sm.species.name}" disabled="disabled"></td>
					<td>Gender</td>
					<td><input type="text" name="gender" class="form-control"
						value="${sm.gender}" disabled="disabled"></td>
				</tr>
				<tr>
					<td>Calculation Based</td>
					<td><c:choose>
							<c:when test="${sm.calculationBased}">
								<input type="text" name="calculationBased" class="form-control"
									value="Yes" disabled="disabled">
							</c:when>
							<c:otherwise>
								<input type="text" name="calculationBased" class="form-control"
									value="No" disabled="disabled">
							</c:otherwise>
						</c:choose></td>
					<td>Units of Measure</td>
					<td><input type="text" name="studyDesc" class="form-control"
						value="${sm.weightUnits}" disabled="disabled"></td>
					<td colspan="2"></td>
<!-- 					<td >Clinpath Parameters</td> -->
<!-- 					<td> -->
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${sm.clinpathPerameters}"> --%>
<!-- 								<input type="button" id="Config" onclick="gotoClinpathPage()" -->
<!-- 									value="Configure" class="btn btn-primary"> -->
<!-- 																<input type="text" name="calculationBased" class="form-control" -->
<!-- 																	value="Yes" disabled="disabled"> -->
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<!-- 								<input type="text" name="calculationBased" class="form-control" -->
<!-- 									value="No" disabled="disabled"> -->
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>
<!-- 					</td> -->
				</tr>
				<tr>
					<td>Sub Group</td>
					<td><c:choose>
							<c:when test="${sm.requiredSubGroup}">
								<input type="text" name="calculationBased" class="form-control"
									value="Yes" disabled="disabled">
							</c:when>
							<c:otherwise>
								<input type="text" name="calculationBased" class="form-control"
									value="NA" disabled="disabled">
							</c:otherwise>
						</c:choose></td>
					<td>Split Study By Gender : ${sm.splitStudyByGender}</td>
					<td>
					<c:choose>
							<c:when test="${!sm.splitStudyByGender}">
								<input type="text" name="calculationBased" class="form-control"
									value="Yes" disabled="disabled">
							</c:when>
							<c:otherwise>
								<input type="text" name="calculationBased" class="form-control"
									value="No" disabled="disabled">
							</c:otherwise>
						</c:choose>
						</td>
				</tr>
				<!-- 			</table> -->
				<!-- 			<table  class="table table-bordered table-striped"> -->
				<tr>
					<th>Gender</th>
					<th style="text-align: center">Acclimatization Start Date</th>
					<th style="text-align: center">Acclimatization End Date</th>
					<th style="text-align: center">Treatment Start Date</th>
					<th style="text-align: center">Treatment End Date</th>
				</tr>
				<tr>
					<td><c:choose>
							<c:when test="${sm.gender eq 'Both' && !sm.splitStudyByGender}">
									Male
								</c:when>
							<c:otherwise>
									${sm.gender}
								</c:otherwise>
						</c:choose></td>
					<td><input type="text" name="acclimatizationStarDate"
						class="form-control" value="${sm.acclimatizationStarDate}"
						disabled="disabled"></td>
					<td><input type="text" name="acclimatizationEndDate"
						class="form-control" value="${sm.acclimatizationEndDate}"
						disabled="disabled"></td>
					<td><input type="text" name="treatmentStarDate"
						class="form-control" value="${sm.treatmentStarDate}"
						disabled="disabled"></td>
					<td><input type="text" name="treatmentEndDate"
						class="form-control" value="${sm.treatmentEndDate}"
						disabled="disabled"></td>
				</tr>
				<c:if test="${sm.gender eq 'Both' && !sm.splitStudyByGender}">
					<tr>
						<td>Female</td>
						<td><input type="text" name="acclimatizationStarDateFemale"
							class="form-control" value="${sm.acclimatizationStarDateFemale}"
							disabled="disabled"></td>
						<td><input type="text" name="acclimatizationEndDateFemale"
							class="form-control" value="${sm.acclimatizationEndDateFemale}"
							disabled="disabled"></td>
						<td><input type="text" name="treatmentStarDateFemale"
							class="form-control" value="${sm.treatmentStarDateFemale}"
							disabled="disabled"></td>
						<td><input type="text" name="treatmentEndDateFemale"
							class="form-control" value="${sm.treatmentEndDateFemale}"
							disabled="disabled"></td>
					</tr>
				</c:if>
			</table>
		</div>

	</div>

	<c:if test="${observationConfig}">
		<jsp:include
			page="/WEB-INF/views/pages/acclimatization/acclimatizationPage.jsp" />
		<jsp:include
			page="/WEB-INF/views/pages/studyDesign/observationConfig.jsp" />
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${instumentConfigPage eq 'addIntrumentAndPerameters' }"> --%>
<%-- 				<jsp:include --%>
<%-- 					page="/WEB-INF/views/pages/grouping/addIntrumentAndPerameters.jsp" /> --%>
<%-- 			</c:when> --%>
<%-- 			<c:otherwise> --%>
<%-- 				<jsp:include --%>
<%-- 					page="/WEB-INF/views/pages/grouping/clinpahtPeramsView.jsp" /> --%>
<%-- 			</c:otherwise> --%>
<%-- 		</c:choose> --%>
	</c:if>
	
</body>
<script src='/TOX/static/js/study/studyDesignUpdate.js'></script>
<script type="text/javascript">
	function gotoClinpathPage() {
		$("#clinpathPerameters").submit();
	}
</script>
</html>