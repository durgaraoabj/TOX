<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>

</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				Experimental Design
			</h3>
		</div>
		<font color="red" id="principalInvestigatormsg"></font>
		<div class="card-body">
			<c:url value="/administration/saveMetaDataDetails" var="saveMetaData" />
			<form:form action="${saveMetaData}" method="post" modelAttribute="sm"
				id="saveMetaDataForm">

				<input type="hidden" name="studyId" id="studyId" value="${sm.id}" />
				<input type="hidden" name="studyNo" id="studyNo"
					value="${sm.studyNo}" />
				<input type="hidden" name="groupRows" id="groupRows" value="0" />
				<table class="table table-bordered table-striped" style="width:80%">
					<form:hidden path="id" />
					<form:hidden path="sdUserLable" value="${sm.sdUserLable}" />
					<form:hidden path="testItemCode" value="${sm.testItemCode}" />
					<form:hidden path="glpNonGlp" value="${sm.glpNonGlp}" />
					<form:hidden path="sponsorMasterId.id"
						value="${sm.sponsorMasterId.id}" />
					<tr>
						<td>Study Number</td>
						<td><input type="text" name="stdName" class="form-control"
							value="${sm.studyNo}" disabled="disabled"></td>
						<td>Title</td>
						<td><input type="text" name="studyDesc" class="form-control"
							value="${sm.studyDesc}" disabled="disabled"></td>
						<td>No. Of Groups</td>
						<td><form:input path="noOfGroups" type="text" maxlength="2"
								size="2" class="form-control input-sm" id="noOfGroups"
								placeholder="count"
								onkeypress="return event.charCode >= 48 && event.charCode <= 57"
								onchange="groupInfo()" /> <font color="red" id="noOfGroupsmsg"></font></td>
					</tr>
					<tr>
						<td>Total No Of Animals</td>
						<td><form:input path="subjects" type="text"
								class="form-control input-sm" id="subjects"
								oncopy="return false" onpaste="return false"
								onchange="requiredValidation('subjects','subjectsmsg', '')"
								onkeypress="return event.charCode >= 48 && event.charCode <= 57" /><font
							color="red" id="subjectsmsg"></font></td>
						<td>Species</td>
						<td><form:select path="species.id" id="speciesId"
								onchange="requiredValidation('speciesId', 'speciesIdMsg', '-1')"
								class="form-control">
								<form:option value="-1">--Select--</form:option>
								<c:forEach items="${speciesList}" var="spac">
									<form:option value="${spac.id}">${spac.name}</form:option>
								</c:forEach>
							</form:select> <font id="speciesIdMsg" style="color: red;"></font>
						<td>Gender</td>
						<td><input type='radio' name='groupGender' value='Male'
							onchange="groupInfo(); genderValidation();" />Male <input
							type='radio' name='groupGender'
							onchange="groupInfo(); genderValidation();" value='Female' />Female
							<input type='radio' name='groupGender'
							onchange="groupInfo(); genderValidation();" value='Both' />Both
							<font id="groupGenderMsg" style="color: red;"></font></td>
					</tr>
					<tr>
						<td>Calculation Based</td>
						<td><input type="radio" name="calculationBased"
							id="calculationBasedYes" value="Yes">Yes <input
							type="radio" name="calculationBased" id="calculationBasedNo"
							value="No">No <font color="red" id="calculationBasedmsg"></font></td>

						<!-- 					</tr> -->
						<!-- 					<tr> -->
						<td>Units of Measure</td>
						<td><form:select path="weightUnits.id" id="weightUnits"
								class="form-control input-sm">
								<form:option value="-1">--Select--</form:option>
								<c:forEach items="${units}" var="u">
									<form:option value="${u.id}">${u.fieldValue}</form:option>
								</c:forEach>
							</form:select>
							<div id="weightUnitsMsg" style="color: red;"></div></td>
<!-- 						<td>Clinpath Parameters</td> -->
<!-- 						<td><input type='radio' name='clinpathPerameters' value='Yes'>Yes -->
<!-- 							<input type='radio' name='clinpathPerameters' value='No'>No -->
<!-- 							<font id="clinpathPerametersMsg" style="color: red;"></font></td> -->
					
						<td>Sub Group</td>
						<td><input type='checkbox' name='subgroupRequired'
							id='subgroupRequired' value='NA' disabled='disabled'
							onchange='desableOrEnableSubgroup()' checked='checked'>NA
						</td>
					</tr>
					<tr>
						<td>Split Study By Gender</td>
						<td><input type='checkbox' name='splitStudyGender'
							onchange="addStartAndEndDates()" id='splitStudyGender'
							value='NA' disabled='disabled'
							onchange='desableOrEnableSubgroup()' checked='checked'>NA
						</td>
						<td colspan="5" />
					</tr>
					<tr>
						<td colspan="6" id="groupInfo"></td>
					</tr>



				</table>
				<table id="example3" class="table table-bordered table-striped" style="width:80%;margin-top:20%"
					id="startAndEndTable">
					<thead>
						<tr style="text-align: center">
							<th>Gender</th>
							<th style="text-align: center">Acclimatization Start Date</th>
							<th style="text-align: center">Acclimatization End Date</th>
							<th style="text-align: center">Treatment Start Date</th>
							<th style="text-align: center">Treatment End Date</th>
					</thead>
					<tr>
						<td id="genderRow" style="text-align: center"></td>
						<td><input name="acclimatizationStDate"
							id="acclimatizationStDate" type="text"
							class="form-control input-sm"
							onchange="acclimatizationValidation('acclimatizationStDate', 'acclimatizationStDateMsg', 'start',  '1')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#acclimatizationStDate").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										changeMonth : true,
										minDate : -0,
										//maxDate: -6574,
										changeYear : true
									});
								});
							</script>
							<div id="acclimatizationStDateMsg" style="color: red;"></div></td>
						<td><input name="acclimatizationEnDate"
							id="acclimatizationEnDate" type="text"
							class="form-control input-sm"
							onchange="acclimatizationValidation('acclimatizationEnDate', 'acclimatizationEnDateMsg', 'end', '1')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#acclimatizationEnDate").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : -0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="acclimatizationEnDateMsg" style="color: red;"></div></td>
						<td><input name="treatmentStDate" id="treatmentStDate"
							type="text" class="form-control input-sm"
							onchange="requiredValidation('treatmentStDate', 'treatmentStDateMsg', '')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#treatmentStDate").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : 0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="treatmentStDateMsg" style="color: red;"></div></td>
						<td><input name="treatmentEnDate" id="treatmentEnDate"
							type="text" class="form-control input-sm"
							onchange="requiredValidation('treatmentEnDate', 'treatmentEnDateMsg', '')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#treatmentEnDate").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : -0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="treatmentEnDateMsg" style="color: red;"></div></td>
					</tr>
					<tr id="startAndEnd2">
						<td id="genderRow2" style="text-align: center"></td>
						<td><input name="acclimatizationStDate2"
							id="acclimatizationStDate2" type="text"
							class="form-control input-sm"
							onchange="acclimatizationValidation('acclimatizationStDate2', 'acclimatizationStDate2', 'start',  '2')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#acclimatizationStDate2").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										changeMonth : true,
										minDate : -0,
										//maxDate: -6574,
										changeYear : true
									});
								});
							</script>
							<div id="acclimatizationStDateMsg2" style="color: red;"></div></td>
						<td><input name="acclimatizationEnDate2"
							id="acclimatizationEnDate2" type="text"
							class="form-control input-sm"
							onchange="acclimatizationValidation('acclimatizationEnDate2', 'acclimatizationEnDateMsg2', 'end',  '2')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#acclimatizationEnDate2").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : -0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="acclimatizationEnDateMsg2" style="color: red;"></div></td>
						<td><input name="treatmentStDate2" id="treatmentStDate2"
							type="text" class="form-control input-sm"
							onchange="requiredValidation('treatmentStDate2', 'treatmentStDateMsg2', '')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#treatmentStDate2").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : 0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="treatmentStDateMsg2" style="color: red;"></div></td>
						<td><input name="treatmentEnDate2" id="treatmentEnDate2"
							type="text" class="form-control input-sm"
							onchange="requiredValidation('treatmentEnDate2', 'treatmentEnDateMsg2', '')"
							autocomplete="off" /> <script type="text/javascript">
								$(function() {
									$("#treatmentEnDate2").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										minDate : -0,
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="treatmentEnDateMsg2" style="color: red;"></div></td>
					</tr>

				</table>
				<div align="center">
					<input type="button" id="studyCreateFormSubmitBtn"
						onclick="studyCreateFormSubmitBtnbut()" value="Save"
						class="btn btn-primary" style="width: 200px;">
					<div id="submitIdMsg" style="color: red;"></div>
				</div>
			</form:form>
		</div>
	</div>
	

</body>
<script src='/TOX/static/js/study/studyDesign.js'></script>
<!-- <script src='/TOX/static/js/grouping/createStudyAndGrouping.js'></script> -->
</html>