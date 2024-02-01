<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	var groups = [];
	var subgroup = [];
	var gender = [];
	var groupId = [];
</script>
</head>
<body>
	<font color="red" id="principalInvestigatormsg"></font>
	<font color="red" id="doseVolumeMsg"></font>
	<font color="red" id="concentrationMsg"></font>

	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Study MetaData</h3>
		</div>
		<div class="box-body">
			<input type="hidden" name="groupNumber" id="groupNumber"
				value="${sm.noOfGroups}">
			<c:url value="/administration/saveUpdatedMetaDataDetails"
				var="saveUpdatedMetaData" />
			<form:form action="${saveUpdatedMetaData}" method="post"
				modelAttribute="sm" id="saveMetaDataForm">
				<table class="table table-bordered table-striped">
					<form:hidden path="id" />
					<%-- <form:hidden path="sponsorname" value="${sm.sponsorname}"/> --%>
					<form:hidden path="sdUserLable" value="${sm.sdUserLable}" />
					<form:hidden path="asdUserLable" value="${sm.asdUserLable}" />
					<form:hidden path="experimentalDesign"
						value="${sm.experimentalDesign}" />
					<form:hidden path="principalInvestigator"
						id="principalInvestigator" />
					<form:hidden path="testItemCode" value="${sm.testItemCode}" />
					<form:hidden path="glpNonGlp" value="${sm.glpNonGlp}" />
					<form:hidden path="sponsorMasterId.id"
						value="${sm.sponsorMasterId.id}" />
					<form:hidden path="doseMgKg" value="${sm.doseMgKg}" />
					<form:hidden path="radamizationStartDate"
						value="${sm.radamizationStartDate}" />
					<form:hidden path="species.id" value="${sm.species.id}" />
					<form:hidden path="acclimatizationStarDate"
						value="${sm.acclimatizationStarDate}" />
					<form:hidden path="acclimatizationEndDate"
						value="${sm.acclimatizationEndDate}" />
					<form:hidden path="treatmentStarDate"
						value="${sm.treatmentStarDate}" />
					<form:hidden path="treatmentEndDate" value="${sm.treatmentEndDate}" />
					<%-- <form:hidden path="startDate" value="${sm.startDate}"/> --%>

					<form:hidden path="doseVolume" id="doseVolume" />
					<form:hidden path="concentration" id="concentration" />
					<tr>
						<td>Study Number :</td>
						<td><input type="text" name="stdName" class="form-control"
							value="${sm.studyNo}" disabled="disabled"></td>
						<td>Title :</td>
						<td><input type="text" name="stdName" class="form-control"
							value="${sm.studyDesc}" disabled="disabled"></td>
					</tr>
					<tr>
						<td>No. Of Groups</td>
						<td><form:input path="noOfGroups"
								class="form-control input-sm" id="noOfGroups"
								placeholder="count"
								onkeypress="return event.charCode >= 48 && event.charCode <= 57"
								onchange="groupInfo(this.value)" /> <font color="red"
							id="noOfGroupsmsg"></font></td>
						<td>No Of Animals</td>
						<td><form:input path="subjects" type="text"
								class="form-control input-sm" id="subjects"
								oncopy="return false" onpaste="return false"
								onkeypress="return event.charCode >= 48 && event.charCode <= 57" /><font
							color="red" id="subjectsmsg"></font></td>
					</tr>
					<tr>
						<td colspan="4"><c:choose>
								<c:when test="${sm.requiredSubGroup}">
    					Subgroup <input type='checkbox' name='subgroupRequired'
										value='NA' onchange='desableOrEnableSubgroup()'>NA		
    				</c:when>
								<c:otherwise>
    					Subgroup <input type='checkbox' name='subgroupRequired'
										value='NA' onchange='desableOrEnableSubgroup()'
										checked="checked">NA
    				</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<td colspan="4" id="groupInfo"></td>
					</tr>

					<%-- <tr>
<!--     			<td>Principal Investigator</td> -->
<!--     			<td> -->
    				<form:input path="principalInvestigator" type="text" class="form-control input-sm" id="principalInvestigator"/>
<!--     				<font color="red" id="principalInvestigatormsg"></font> -->
<!--     			</td> -->
    			
    			
    			<td>Start Date</td> 
    			<td>
    				<input name="stDate" id="stDate" type="text" class="form-control input-sm" value='<fmt:formatDate value="${sm.startDate}" pattern = "yyyy-MM-dd"/>' onchange="stDateValidation('stDate', 'stDateMsg')" autocomplete="off"/>
	    				<script type="text/javascript">
						$(function(){
								$("#stDate").datepicker({
									dateFormat: $("#dateFormatJsp").val(),
									changeMonth:true,
									changeYear:true
								});
							});
						</script>
						<div id="stDateMsg" style="color: red;"></div></td>
    		</tr> --%>

					<tr>
						<td>Calculation Based :</td>
						<td><c:choose>
								<c:when test="${sm.calculationBased}">
									<input type="radio" name="calculationBased"
										id="calculationBasedYes" onchange="checkCalculation()"
										checked="checked" value="Yes">Yes
									<input type="radio" name="calculationBased"
										id="calculationBasedNo" onchange="checkCalculation()"
										value="No">No	
								</c:when>
								<c:otherwise>
									<input type="radio" name="calculationBased"
										id="calculationBasedYes" onchange="checkCalculation()"
										value="Yes">Yes
									<input type="radio" name="calculationBased"
										id="calculationBasedNo" onchange="checkCalculation()"
										checked="checked" value="No">No	
								</c:otherwise>
							</c:choose> <font color="red" id="calculationBasedmsg"></font></td>

						<td>Units of Measure</td>
						<td><select name="weightUnits.id" id="weightUnits"
							class="form-control input-sm" disabled="disabled">
								<c:forEach items="${units}" var="u">
									<option value="${u.id}">${u.fieldValue}</option>
								</c:forEach>

						</select>

							<div id="weightUnitsMsg" style="color: red;"></div></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
					</tr>

				</table>

				<table class="table table-bordered table-striped">
					<thead>
						<tr>
							<th colspan="4">Study Information</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="4"><b>Study Number :</b>${studyDto.studyNo}</td>
						</tr>
						<!-- 			<tr> -->
						<!-- 				<th>Group Name</th> -->
						<!-- 				<th>Gender</th> -->
						<!-- 				<th>Animal From</th> -->
						<!-- 				<th>Animal To</th> -->
						<!-- 			</tr> -->
						<%-- 			<c:forEach items="${studyDto.groups}" var="group"> --%>
						<!-- 				<tr> -->
						<%-- 					<td>${group.group.groupName}</td> --%>
						<%-- 					<td>${group.gender}</td> --%>
						<%-- 					<td>${group.from}</td> --%>
						<%-- 					<td>${group.to}</td> --%>
						<!-- 				</tr>			 -->
						<%-- 			</c:forEach> --%>
				</table>
				<table class="table table-bordered table-striped">
					<c:forEach items="${studyDto.insturmentViewTestCodes}"
						var="insturmentTc">
						<tr>
							<th colspan="2">${insturmentTc.key}</th>
						</tr>
						<tr>
							<th>Parameter</th>
							<th>Order</th>
							<c:forEach items="${insturmentTc.value}" var="tc">
								<tr>
									<td>${tc.testCode.disPalyTestCode}</td>
									<td>${tc.orderNo}</td>
								</tr>
							</c:forEach>
					</c:forEach>
					</tbody>
				</table>

				<c:if test="${type eq 'update'}">
					<div align="center">
						<input type="button" id="studyCreateFormSubmitBtn"
							onclick="studyCreateFormSubmitBtnbut()" value="Update"
							class="btn btn-primary" style="width: 200px;">
					</div>
				</c:if>
			</form:form>
		</div>
		<c:forEach items="${sm.groupInfo}" var="gi" varStatus="st">
			<script type="text/javascript">
				/* groups.push('${gi.groupName}'); 
				subgroup.push('${gi.groupTest}');
				gender.push('${gi.gender}'); */
				groupId['${st.count}'] = '${gi.id}';
				groups['${st.count}'] = '${gi.groupName}';
				subgroup['${st.count}'] = '${gi.groupTest}';
				gender['${st.count}'] = '${gi.gender}';
			</script>
		</c:forEach>
	</div>

	<c:if test="${observationConfig}">
		<jsp:include
			page="/WEB-INF/views/pages/acclimatization/acclimatizationPage.jsp" />
		<jsp:include
			page="/WEB-INF/views/pages/studyDesign/observationConfig.jsp" />
	</c:if>
</body>
<script src='/TOX/static/js/validaiton.js'></script>
<script type="text/javascript">

	$(document).ready(function() {
		$("#weightUnits").val('${sm.weightUnits.id}');
		desableOrEnableSubgroup();
		checkCalculation();
	});
	function checkCalculation() {
		$("#calculationBasedmsg").html("");
		var v = $('input[name="calculationBased"]:checked').val();
		if (v == 'Yes') {
			$("#doseVolume").removeAttr('disabled');
			$("#concentration").removeAttr('disabled');
			$("#weightUnits").removeAttr('disabled');
		} else {
			$("#doseVolume").prop('disabled', true);
			$("#concentration").prop('disabled', true);
			$("#weightUnits").prop('disabled', true);
		}
	}
	function desableOrEnableSubgroup() {
		var checkedValue = $('input[name="subgroupRequired"]:checked').val();
		// 		alert(checkedValue)
		var noOfGroups = $("#noOfGroups").val();
		if (checkedValue === undefined) {
			for (var i = 1; i <= noOfGroups; i++)
				$("#groupTest" + i).removeAttr('disabled');
		} else {
			for (var i = 1; i <= noOfGroups; i++)
				$("#groupTest" + i).prop('disabled', true);
		}
	}
</script>
<script type="text/javascript">
	var groupVal = $("#noOfGroups").val();
	// alert(groupVal);
	if (groupVal != null && groupVal != "" && groupVal != "undefinde") {
		groupInfo(groupVal);

	}
	function checkSubjectNumber(val) {
		var value = $('#groupTest' + val).val();
		var existsVal = subgroup[val];
		if (parseInt(value) >= parseInt(existsVal)) {
			$('#groupTest' + val + 'msg').html("");
		} else {
			$('#groupTest' + val + 'msg')
					.html(
							"Sub Group Number must be grater than (OR) equal exisiting Sub Group Number.");
			$('#groupTest' + val).val(existsVal);
		}
	}
	function groupInfo(value) {
		var exisGroupVal = $('#groupNumber').val();
		if (parseInt(value) >= parseInt(exisGroupVal)) {
			$('#noOfGroupsmsg').html("");
			var ele = "<table class='table table-bordered table-striped'><tr><th>Group Name</th><th>No. Of Sub Group's</th></tr>";
			for (var i = 1; i <= value; i++) {
				var onchageFun = "checkSubjectNumber('" + i + "')";
				var groupStr = groups[i];
				if (groupStr != null && groupStr != ""
						&& groupStr != "undefined") {
					ele += "<tr><td><input type='hidden' name='gid_"+i+"' value='"+groupId[i]+"'><input type='text' name='groupName"+i+"'  id='groupName"+i+"' value='"+groups[i]+"' class='form-control'/><font color='red' id='groupName"+i+"msg'></font></td>"
							+ "<td><input type='number' name='groupTest"+i+"' id='groupTest"+i+"' value='"+subgroup[i]+"' class='form-control' placeholder='Number' onchange="+onchageFun+" onkeypress='return event.charCode >= 48 && event.charCode <= 57'/><font color='red' id='groupTest"+i+"msg'></font></td>";
					if (gender[i] == "Male") {
						ele += "<td>"
								+ "<input type='radio' name='groupGender"+i+"' value='Male' checked/>Male<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Female'/>Female<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Both'/>Both<br/>"
								+ "<font color='red' id='groupGender"+i+"msg'></font></td></tr>"
					} else if (gender[i] == "Female") {
						ele += "<td>"
								+ "<input type='radio' name='groupGender"+i+"' value='Male'/>Male<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Female' checked/>Female<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Both'/>Both<br/>"
								+ "<font color='red' id='groupGender"+i+"msg'></font></td></tr>"
					} else if (gender[i] == "Both") {
						ele += "<td>"
								+ "<input type='radio' name='groupGender"+i+"' value='Male'/>Male<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Female'/>Female<br/>"
								+ "<input type='radio' name='groupGender"+i+"' value='Both' checked/>Both<br/>"
								+ "<font color='red' id='groupGender"+i+"msg'></font></td></tr>"
					}
				} else {
					ele += "<tr><td><input type='hidden' name='gid_"+i+"' value='0'><input type='text' name='groupName"+i+"'  id='groupName"+i+"' class='form-control'/><font color='red' id='groupName"+i+"msg'></font></td>"
							+ "<td><input type='number' name='groupTest"+i+"' id='groupTest"+i+"' class='form-control' placeholder='Number'  onkeypress='return event.charCode >= 48 && event.charCode <= 57'/><font color='red' id='groupTest"+i+"msg'></font></td>"
							+ "<td>"
							+ "<input type='radio' name='groupGender"+i+"' value='Male'/>Male<br/>"
							+ "<input type='radio' name='groupGender"+i+"' value='Female'/>Female<br/>"
							+ "<input type='radio' name='groupGender"+i+"' value='Both' checked/>Both<br/>"
							+ "<font color='red' id='groupGender"+i+"msg'></font></td></tr>";
				}
			}
			ele += "<table>";
			$("#groupInfo0").html("Group wise Test's");
			$("#groupInfo").html(ele);
		} else {
			$('#noOfGroupsmsg')
					.html(
							"Group Number Must be Grater than (OR) equal to eixisting group Number.");
			$("#noOfGroups").val(exisGroupVal);
		}

	}

	/* function groupInfo(value){
		$("#groupInfo0").html("");
		$("#groupInfo").html("");
		$("#noOfGroupsmsg").html("");
		var batch = value.trim();
		if(batch != '' && batch > 0){
			var ele = "<table class='table table-bordered table-striped'><tr><th>Group Name</th><th>No. Of Sub Group's</th></tr>";
			for(var i = 1; i<= batch; i++){
				ele += "<tr><td><input type='text' name='groupName"+i+"'  id='groupName"+i+"' class='form-control'/><font color='red' id='groupName"+i+"msg'></font></td>"
				+"<td><input type='number' name='groupTest"+i+"' id='groupTest"+i+"' class='form-control' placeholder='Number' onkeypress='return event.charCode >= 48 && event.charCode <= 57'/><font color='red' id='groupTest"+i+"msg'></font></td>"
				+"<td>"
				+"<input type='radio' name='groupGender"+i+"' value='Male'/>Male<br/>"
				+"<input type='radio' name='groupGender"+i+"' value='Female'/>Female<br/>"
				+"<input type='radio' name='groupGender"+i+"' value='Both' checked/>Both<br/>"
				+"<font color='red' id='groupGender"+i+"msg'></font></td></tr>";
			}		
			ele += "<table>";
			$("#groupInfo0").html("Group wise Test's");
			$("#groupInfo").html(ele);	
		}else{
			if(batch <= 0)	$("#noOfGroupsmsg").html("Value must more than 0");
			else $("#noOfGroupsmsg").html("Required Field.");
		}
	} */
	var dateFlag = false;
	function stDateValidation(id, messageId) {
		var value = $('#stDate').val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html("Required Field.");
			dateFlag = false;
		} else {
			$('#' + messageId).html("");
			dateFlag = true;
		}
		return dateFlag;
	}
	function studyCreateFormSubmitBtnbut() {
		$("#studyNomsg").html("");
		$("#studyDescmsg").html("");
		$("#principalInvestigatormsg").html("");
		$("#subjectsmsg").html("");
		$("#noOfGroupsmsg").html("");
		var flag = true;
		if (dateFlag == false)
			dateFlag = stDateValidation('stDate', 'stDateMsg');
		if ($("#principalInvestigator").val().trim() == '') {
			$("#principalInvestigatormsg").html("Required Field");
			flag = false;
		}
		if ($("#noOfGroups").val().trim() == '') {
			$("#noOfGroupsmsg").html("Required Field");
			flag = false;
		} else {

			var value = Number($("#noOfGroups").val().trim());
			if (value > 0) {
				var groupNames = [];
				for (var v = 1; v <= value; v++) {
					// 					alert(v)
					$("#groupName" + v + "msg").html("");
					$("#groupTest" + v + "msg").html("");
					// 					alert($("#groupName"+v).val());
					var groupName = $("#groupName" + v).val().trim();
					if (groupName == '') {
						$("#groupName" + v + "msg").html("Required Field");
						flag = false;
					} else {
						var fg = true;
						$.each(groupNames, function(k, v) {
							if (groupName == v) {
								fg = false;
							}
						});
						if (fg)
							groupNames.push(groupName);
					}
					var groupTest = $("#groupTest" + v).val().trim();
					// 					alert($("#groupName"+v).val());
					if (groupTest == '') {
						$("#groupTest" + v + "msg").html("Required Field");
						flag = false;
					} else {
						var fg = true;
						if (Number(groupTest) < 0) {
							$("#groupTest" + v + "msg").html(
									"Value must more than 0");
							flag = false;
						}
					}
				}
			} else {
				$("#noOfGroupsmsg").html("Value must more than 0");
				flag = false;
			}
		}
		if ($("#subjects").val().trim() == '') {
			$("#subjectsmsg").html("Required Field");
			flag = false;
		}

		var v = $('input[name="calculationBased"]:checked').val();

		if (v === undefined) {
			$("#calculationBasedmsg").html("Required Fields");
			flag = false;
		} else if (v == 'Yes') {
			$("#doseVolumeMsg").html("");
			$("#concentrationMsg").html("");
			if ($("#doseVolume").val() == '') {
				$("#doseVolumeMsg").html("Required Field");
				flag = false;
			}
			if ($("#concentration").val() == '') {
				$("#concentrationMsg").html("Required Field");
				flag = false;
			}
		}
		if (flag) {
			//alert("asdf")
			$('#saveMetaDataForm').submit();
		}
	}
</script>
<script type="text/javascript">
	function sample() {
		$('input').iCheck({
			checkboxClass : 'icheckbox_square-blue',
			radioClass : 'iradio_square-blue',
			increaseArea : '20%' /* optional */
		});
	}
</script>


</html>