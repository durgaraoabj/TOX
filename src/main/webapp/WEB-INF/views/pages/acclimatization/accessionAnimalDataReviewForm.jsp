<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<script type="text/javascript">
	var animalIds = [];
</script>
</head>
<body>

	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Form Review:</h3>
			<table class="table table-bordered table-striped">
				<tr>
					<td>Study : ${aadDto.study.studyNo }</td>
					<td colspan="4">Data Entry Review Form : ${aadDto.crf.prefix}</td>
				</tr>
			</table>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<div style="width: 100%; overflow: scroll">

				<c:url value="/studyReview/approveObservation"
					var="saveObservationReview" />
				<sf:form action="${saveObservationReview}" method="POST"
					id="saveReview">
					<input type="hidden" name="stdSubGroupObservationCrfsId"
						id="stdSubGroupObservationCrfsId" value="${aadDto.crf.id}">
					<input type="hidden" name="studyId" id="studyId"
						value="${aadDto.study.id}">
					<input type="button" id="reivewBtn" value="Approve"
						class="btn btn-primary btn-md">&nbsp;&nbsp;
		        	<input type="button" id="rejectBtn" value="Reject"
						class="btn btn-primary btn-md">
					<div id="chkMsg"
						style="color: red; text-align: center; font-size: medium; font-weight: bold;"></div>
					<table class="table table-bordered table-striped" style="width: 100%;">
						<tr>
							<th><input type="checkbox" name="allCheckBox"
								id="allCheckBox" onclick="allCheckboxFunction()">Select</th>
							<th>Animal No</th>
							<th>Gender</th>
							<th>Entry Type</th>
							<!-- 							<th>Date Deviation</th> -->
							<th>Entered By</th>
							<th>Entered On</th>
							<c:forEach items="${aadDto.elements}" var="ele">
								<th><c:choose>
										<c:when
											test="${ele.value.leftDesc ne null and ele.value.leftDesc ne ''}">
											${ele.value.leftDesc}
										</c:when>
										<c:when
											test="${ele.value.rigtDesc ne null and ele.value.rigtDesc ne ''}">
											${ele.value.rigtDesc}
										</c:when>
										<c:when
											test="${ele.value.topDesc ne null and ele.value.topDesc ne ''}">
											${ele.value.topDesc}
										</c:when>
										<c:when
											test="${ele.value.bottemDesc ne null and ele.value.bottemDesc ne ''}">
											${ele.value.bottemDesc}
										</c:when>
										<c:when
											test="${ele.value.middeDesc ne null and ele.value.middeDesc ne ''}">
											${ele.value.middeDesc}
										</c:when>
										<c:otherwise>
											${ele.value.totalDesc}
										</c:otherwise>
									</c:choose> <%-- 								${ele.value.leftDesc } --%></th>
							</c:forEach>
						</tr>

						<c:forEach items="${aadDto.animalData }" var="ele" varStatus="st">
							<tr>
								<td>
									<%-- 									<c:if test="${ele.allowDataEntry}"> --%> <c:choose>
										<c:when test="${ele.reviewed}">
											<input type="checkbox" name="descElement" value="${ele.id}"
												id="${ele.id}" checked="checked" disabled="disabled" />
										</c:when>
										<c:when
											test="${ele.reviewed eq false and ele.discrepencyOpend eq 0 && ele.allowToReview}">
											<input type="checkbox" name="chkBox_${ele.id}"
												value="${ele.id}" id="chkBox_${ele.id}" />
										</c:when>
										<c:when
											test="${ele.discrepencyOpend gt 0 or ele.allowToReview eq false}">
											<input type="checkbox" name="descElement" value="${ele.id}"
												id="${ele.id}" disabled="disabled" />
											<%-- 											<input type="checkbox" name="chkBox_${ele.id}" --%>
											<%-- 													value="${ele.id}" id="chkBox_${ele.id}" /> --%>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="descElement" value="${ele.id}"
												id="${ele.id}" checked="checked" disabled="disabled" />
										</c:otherwise>
									</c:choose> <%-- 									</c:if> --%> <input type="hidden"
									name="discrepencyClosed" value="${ele.discrepencyClosed}"
									id="desc_${ele.id}" /> <script type="text/javascript">
										animalIds.push('${ele.id}');
									</script>
								</td>
								<%-- 								<td>${ele.animalno}</td> --%>
								<td>${ele.animalId }</td>
								<td>${ele.gender }</td>
								<td>${ele.entryType }</td>
								<%-- 								<td>${ele.deviationMessage}</td> --%>
								<td>${ele.userName }</td>
								<td>${ele.date }</td>
								<%-- 								<td>${ele.dayOrWeek }</td>			 --%>
								<c:forEach items="${ele.elementData }" var="eleData">
									<c:choose>
										<c:when test="${eleData.value.dataUpdate}">
											<td id="eleDataTd_${eleData.value.id}">
										</c:when>
										<c:otherwise>
											<td>
										</c:otherwise>
									</c:choose>
									<div id="eleValue_${eleData.value.id}">
										<c:choose>
											<c:when test="${eleData.value.dataUpdate}">
												<a href="javascript:void(0);"
													style="background-color: green"
													onclick="updateValue('${eleData.value.id }', false, 'acesstion')"><b>${eleData.value.value }</b></a>
											</c:when>
											<c:otherwise>${eleData.value.value }</c:otherwise>
										</c:choose>
									</div>
									<c:choose>
										<c:when
											test="${eleData.value.discripency eq 0 and eleData.value.discripencyClose eq 0}">
											<c:if test="${ele.reviewed eq false }">
												<img
													src='<c:url value="/static/template/dist/img/flag_wite.png"/>'
													class="img-circle" title="New Discrepancy" width="16"
													height="16"
													onclick="createDiscrepency('${eleData.value.id }','new', '${ele.gender }')">
											</c:if>
										</c:when>
										<c:when test="${eleData.value.discripency gt 0}">
											<img
												src='<c:url value="/static/template/dist/img/flag_red.png"/>'
												class="img-circle" title="Open" width="16" height="16"
												onclick="createDiscrepency('${eleData.value.id }','reviewer', '${ele.gender }')">
										</c:when>
										<c:when test="${eleData.value.discripencyClose gt 0}">
											<img
												src='<c:url value="/static/template/dist/img/flag_black.png"/>'
												class="img-circle" title="Closed" width="16" height="16"
												onclick="createDiscrepency('${eleData.value.id }','all','${ele.gender }' )">
										</c:when>
									</c:choose>
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</sf:form>
			</div>
		</div>
		<!-- /.card-body -->
	</div>
	<!-- /.card -->

	<div class="modal fade" id="discDataModal" tabindex="-1" role="dialog"
		aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">New Discrepancy</h4>
				</div>
				<div class="modal-body">
					<div id="discData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: left;">
						<label class="checkbox-inline"><input type="checkbox"
							value="true" id="acceptCheckId" />&nbsp;&nbsp;&nbsp;Do You want
							to close page.</label>
					</div>
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('discDataModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:url value="/studyReview/createAccessionDiscrepency"
		var="createAccessionDiscrepencyVar" />
	<sf:form action="${createAccessionDiscrepencyVar}" method="POST"
		id="accessionDescSave">
		<input type="hidden" id="dateString" name="dateString"
			value="${dateString}" />
		<input type="hidden" id="gender" name="gender"
			value="${gender}" />
		<input type="hidden" id="studyAcclamatizationDatesId" name="studyAcclamatizationDatesId"
			value="${studyAcclamatizationDatesId}" />
		<input type="hidden" id="accessioncrfId" name="accessioncrfId"
			value="${crfId}" />
		<input type="hidden" id="accessionstuydId" name="accessionstuydId"
			value="${aadDto.study.id}" />
		<input type="hidden" id="accessiondataId" name="accessiondataId" />
		<input type="hidden" id="accessionuserId" name="accessionuserId" />
		<input type="hidden" id="accessioncomment" name="accessioncomment" />
		<input type="hidden" id="accessionGender" name="gender" />
	</sf:form>

	<c:url value="/studyReview/createDiscrepency" var="createDiscrepency" />
	<sf:form action="${createDiscrepency}" method="POST"
		modelAttribute="crfpojo" id="secDescSave"
		enctype="multipart/form-data">
		<input type="hidden" id="dataId3" name="dataId" />
		<input type="hidden" id="userId3" name="userId" />
		<input type="hidden" id="comment3" name="comment" />
		<input type="hidden" name="subGroupInfoId" value="${subGroupInfoId}" />
		<input type="hidden" name="stdSubGroupObservationCrfsId"
			value="${stdSubGroupObservationCrfsId}" />
	</sf:form>
	<c:url value="/studyReview/saveApprovedData" var="saveApproveDataForm" />
	<sf:form action="${saveApproveDataForm}" method="POST"
		id="saveApprData">
		<input type="hidden" name="dateString"
			value="${dateString}" />
		<input type="hidden"name="gender"
			value="${gender}" />
		<input type="hidden" name="studyAcclamatizationDatesId"
			value="${studyAcclamatizationDatesId}" />
		<input type="hidden" id="crfId" name="crfId" value="${crfId}" />
		<input type="hidden" id="stuydId" name="stuydId"
			value="${aadDto.study.id}" />
		<input type="hidden" id="chkckedIds" name="chkckedIds" />
		<input type="hidden" id="reviewType" name="reviewType" />
		<input type="hidden" id="reviewComment" name="reviewComment" />
	</sf:form>




	<div class="modal fade" id="signacherModal" tabindex="-1" role="dialog"
		aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Electronic Signature</h4>
					<input type="hidden" id="signcheck" />
				</div>

				<div class="modal-body">
					<table>
						<tr>
							<th>User Name :</th>
							<td>${userName}</td>
						</tr>
						<tr>
							<th>Transaction Password</th>
							<td><input type="password" id="tpw" /><font color="red"
								id="tpwmsg"></font></td>
						</tr>
						<tr>
							<th>Comment</th>
							<td><input type="text" id="rcomment" /><font color="red"
								id="commentmsg"></font></td>
						</tr>
						<tr>
						<tr>
							<td />
							<td><input type="button" class="btn btn-primary"
								onclick="animalAccessionCrfsDataReview()" value="Submit" /></td>
						</tr>
					</table>
				</div>

				<div class="modal-footer">
					<div style="text-align: left;">
						<label class="checkbox-inline"><input type="checkbox"
							value="true" id="acceptsignCheckId" />&nbsp;&nbsp;&nbsp;Do You
							want to close page.</label>
					</div>
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptsignCheckBox('signacherModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>


	<div class="modal fade" id="updateDataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Animal Accession
						Data Audit</h4>
				</div>
				<div class="modal-body">
					<div class="card">
						<div class="card-body">
							<div id="elementUpdateData"></div>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('updateDataModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		// 		$('#discDataModal').modal('hide');
		$('#signacherModal').modal('hide');
	});
	function signacherPopup() {
		$('#signacherModal').modal('show');
	}
	function acceptsignCheckBox() {
		if ($('#acceptsignCheckId').is(':checked')) {
			$('#signacherModal').modal('hide');
		} else {
			aler("Please check checkbox...");
		}
	}
	function animalAccessionCrfsDataReview() {
		$("commentmsg").html("");
		$("#tpwmsg").html("");
		var reviewType = $("#reviewType").val();
		var access = true;
		var comment = $("#rcomment").val().trim();
		if (reviewType = 'Reject') {
			if (comment == '') {
				access = false;
				$("#commentmsg").html("Required Field");
			}
		}
		if (access) {
			var signcheck = asynchronousAjaxCall(mainUrl
					+ "/studyReview/signcheck/" + $("#tpw").val());
			//	 		alert($("#signcheck").val());
			$("#signcheck").val(signcheck.trim());
			if ($("#signcheck").val() == "yes") {
				$("#reviewComment").val(comment);
				$('#signacherModal').modal('hide');
				$('#saveApprData').submit();
			} else {
				$("#tpw").val("");
				$("#tpwmsg").html("In-valied password");
			}
		}

	}
	function approveObservations() {
		if ($("#descall").is(":checked")) {
			var desc = $("#descall").val();
			if (desc > 0) {
				alert("Observation's Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
			} else {
				$("#saveReview").submit();
			}
		} else {
			var checked = 0;
			var descs = 0;
			$.each($("input[name='descElement']:checked"), function() {
				checked++;
				descs += $("#desc_" + this.id).val();
			});
			if (checked > 0) {
				if (descs > 0) {
					alert("Observation Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
				} else {
					$("#saveReview").submit();
				}
			} else {
				alsert("Select atleast one checkbox.")
			}

		}
	}
	function selectAll() {
		if ($("#descall").is(":checked")) {
			$("input[name='descElement']").each(function() {
				$("#" + this.id).attr('checked', true); // Checks it
			});
		} else {
			$("input[name='descElement']").each(function() {
				$("#" + this.id).attr('checked', false); // Checks it
			});
		}
	}
var discrepencyGender;
	function createDiscrepency(id,type, gender) {
// 		alert(gender)
	discrepencyGender = gender;
		var result = asynchronousAjaxCall(mainUrl + "/studyReview/discDataNew/"
				+ id);
		if (result != 'undefined' && result != '') {
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		} else
			alert("Unable to display discrepency page");
	}

	function saveDisc() {
// 					alert(discrepencyGender)
		debugger;
		$("#userIdmsg").html("");
		$("#commentdescmsg").html("");
		var flag = true;
		if ($("#userId").val() == -1) {
			$("#userIdmsg").html("Required Field");
			flag = false;
		}
		if ($("#commentdesc").val().trim() == "") {
			$("#commentdescmsg").html("Required Field");
			flag = false;
		}
		if (flag) {
			var dataId = $("#dataId").val();
			var userId = $("#userId").val();
			var comment = $("#commentdesc").val();

			$("#accessiondataId").val(dataId);
			$("#accessionuserId").val(userId);
			$("#accessioncomment").val(comment);
			$("#accessionGender").val(discrepencyGender);
			$("#accessionDescSave").submit();

			// 				alert(mainUrl+"/studyReview/createDiscrepency/")
			/* 			var result = asynchronousAjaxCall(mainUrl+"/reviewer/createDiscrepency/"
								+vpcId+"/"+crfId+"/"+eleId+"/"+dataId+"/"+keyName+"/"+username+"/"+userId+"/"+comment);
						if(result != '' || result != 'undefined'){
					    	$('#discDataModal').modal('hide');
						} */
		}
	}
	function allCheckboxFunction() {
		if ($('#allCheckBox').prop("checked")) {
			if (animalIds.length > 0) {
				for (var i = 0; i < animalIds.length; i++) {
					$('#chkBox_' + animalIds[i]).prop("checked", true);
				}
			}
		} else {
			if (animalIds.length > 0) {
				for (var i = 0; i < animalIds.length; i++) {
					$('#chkBox_' + animalIds[i]).prop("checked", false);
				}
			}
		}
	}
</script>
<script src='/TOX/static/js/accession/dataEntryView.js'></script>
</html>



