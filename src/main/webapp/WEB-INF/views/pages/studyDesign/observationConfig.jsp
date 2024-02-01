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
			<h3 class="card-title">Treatment Configuration</h3>
			<br />
			<%-- 			<h5 class="card-title">Study : ${study.studyNo }</h5> --%>
		</div>
		<div class="box-body">
			<div id="reviewMessage"
				style="color: red; text-align: center; font-size: medium; font-weight: bold;"></div>
			<c:set value="false" var="animalFlag"></c:set>
			<table class="table table-bordered table-striped">
				<tr>
					<th>Group</th>
					<!-- 	    			<th style="width: 15">Dose(mg/kg b.w)</th> -->
					<!-- 	    			<th style="width: 15">Conc. (mg/ml)</th> -->
					<th style="width: 10%">No.Of Animals</th>
					<th style="width: 10%">Sex</th>
					<td colspan="2">
						<table class="table table-bordered table-striped">
							<tr>
								<th colspan="2" style="text-align: center"><font
									style="text-align: center">Animal Number</font></th>
							</tr>
							<tr>
								<th style="width: 10%">From</th>
								<th style="width: 10%">To</th>
							</tr>
						</table>
					</td>
					<th style="width: 10%">Configuration</th>
				</tr>
				<c:forEach items="${group}" var="g">
					<tr>
						<th colspan="7">${g.groupName}</th>
					</tr>
					<c:forEach items="${g.subGroupInfo }" var="s">
						<tr>
							<td>${s.name}</td>
							<%-- 		    				<td>${s.dose}</td> --%>
							<%-- 		    				<td>${s.conc}</td> --%>

							<c:choose>
								<c:when test="${s.animalInfo.size() eq 1}">
									<c:forEach items="${s.animalInfo }" var="animals">
										<td>${animals.count}</td>
										<td>${animals.gender}</td>
										<td>${animals.formId}</td>
										<td>${animals.toId}</td>
										<c:if test="${animals.formId != null && animals.formId ne ''}">
											<c:set value="true" var="animalFlag"></c:set>
										</c:if>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<td colspan="4">
										<table class="table table-bordered table-striped"
											style="width: 100%">
											<c:forEach items="${s.animalInfo }" var="animals">
												<tr>
													<td>${animals.count}</td>
													<td>${animals.gender}</td>
													<td>${animals.formId}</td>
													<td>${animals.toId}</td>
													<c:if
														test="${animals.formId != null && animals.formId ne ''}">
														<c:set value="true" var="animalFlag"></c:set>
													</c:if>
												</tr>
											</c:forEach>
										</table>
									</td>
								</c:otherwise>
							</c:choose>
							<td><c:if test="${animalFlag eq 'true'}">
									<input type="button" id="${s.id}_divshow"
										onclick="submitBtnbut('${s.id}')" value="Config"
										class="btn btn-primary" style="width: 100px;">
									<input type="button" id="${s.id}_divhide"
										onclick="hideDiv('${s.id}', '${s.id}_divhide', '${s.id}_divshow')"
										value="Config Hide" class="btn btn-primary"
										style="width: 100px; display: none;">
									<%-- 		    					<input type="button" id="Config" onclick="submitBtnbut2('${s.id}')" value="Config 2" class="btn btn-primary" style="width:100px;"> --%>
								</c:if></td>
						</tr>
						<tr id="${s.id}_SubGroupObservationsTr" style="display: none;">
							<td colspan="6" id="${s.id}_SubGroupObservationsTd"></td>
						</tr>
					</c:forEach>
				</c:forEach>
				<c:if
					test="${sds.status.statusCode eq 'EDOROD' && animalFlag eq 'true'}">
					<tr align="center">
						<td colspan="8"><input type="button" value="Send To Review"
							class="btn btn-primary btn-sm" onclick="sendtoReviewFunction()"></td>
					</tr>
				</c:if>
			</table>
		</div>
		<br />
		<c:choose>
			<c:when test="${roleName eq 'SD'}">
				<c:choose>
					<c:when
						test="${sm.studyStatus eq 'In-Design' and roleName eq 'SD'}">
						<table>
							<tr>
								<td>Comment : <input type="text" name="directorComment"
									id="directorComment" value=""/>
									<font color="red" id="directorCommentMsg"></font>
								</td>
								<td><input type="button" class="btn btn-primary btn-sm"
									value="Send to Reivew" onclick="studySendToReivew('directorComment', 'directorCommentMsg')" />
							</tr>
						</table>
					</c:when>
					<c:when
						test="${sm.studyStatus eq 'Comments Required' and roleName eq 'SD'}">
						<table>
							<tr>
								<td>Comment : <input type="text" name="directorComment"
									id="directorComment" />
									<font color="red" id="directorCommentMsg"></font>
								</td>
								<td><input type="button" class="btn btn-primary btn-sm"
									value="Send to Reivew" onclick="studySendToReivew('directorComment', 'directorCommentMsg')" />
							</tr>
						</table>
					</c:when>
				</c:choose>
			</c:when>
			<c:when test="${roleName eq 'QA'}">
				<c:if test="${sm.studyStatus eq 'In-Review' and roleName eq 'QA'}">
					<table>
						<tr>
							<td><input type="button" class="btn btn-primary btn-sm"
								value="Approve" onclick="approveOrRject('Approve')" />
							<td><input type="button" class="btn btn-primary btn-sm"
								value="Send Comments" onclick="approveOrRject('Reject')" />
						</tr>
					</table>
				</c:if>
			</c:when>
		</c:choose>

	</div>
	<c:url value="/expermentalDesign/createExpermentalDesignEFrom"
		var="url" />
	<form:form action="${url}" method="post" id="form">
		<input type="hidden" id="studyId" name="studyId"
			value="${studyDesignId}" />
		<input type="hidden" id="sibGroupId" name="sibGroupId" />
	</form:form>
	<c:url value="/expermentalDesign/observationConfig2" var="url" />
	<form:form action="${url}" method="get" id="form2">
		<input type="hidden" id="studyId" name="studyId"
			value="${studyDesignId}" />
		<input type="hidden" id="sibGroupId2" name="sibGroupId" />
	</form:form>
	<c:url value="/designReview/observationDesingSendToReview"
		var="obdsrUrl" />
	<form:form action="${obdsrUrl}" method="POST" id="obsvDstr">
		<input type="hidden" id="studyId" name="studyId"
			value="${studyDesignId}" />
		<input type="hidden" id="studyIdVal" name="studyIdVal"
			value="${studyDesignId}" />
	</form:form>

	<div class="modal fade" id="configDataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="configDataModalHeadding"></h4>
				</div>
				<div class="modal-body" style="width: 100%;">
					<div id="configDataModalData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox()" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:url value="/administration/sendStudyFroReivew" var="sentReview" />
	<form:form action="${sentReview}" method="POST" id="sentReviewId">
		<input type="hidden" id="studyId" name="studyId" value="${sm.id}" />
		<input type="hidden" id="commentForReview" name="commentForReview" />
	</form:form>

	<c:url value="/studyReview/reivewStudy" var="saveStudyReview" />
	<form:form action="${saveStudyReview}" method="POST" id="reviewFrom">
		<input type="hidden" id="studyId" name="studyId" value="${sm.id}" />
		<input type="hidden" id="reviewComment" name="reviewComment" value="" />
		<input type="hidden" id="reviewType" name="reviewType" value="" />
	</form:form>


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
							onclick="acceptsignCheckBox()" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
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

			if (signcheck == "yes") {
				debugger;
				$("#reviewComment").val(comment);
				$('#signacherModal').modal('hide');
				$('#reviewFrom').submit();
			} else {
				$("#tpw").val("");
				$("#tpwmsg").html("In-valied password");
			}
		}

	}
	function acceptsignCheckBox() {
		if ($('#acceptsignCheckId').is(':checked')) {
			$('#signacherModal').modal('hide');
		} else {
			aler("Please check checkbox...");
		}
	}
	function approveOrRject(reivewType) {
		if (reivewType == 'Approve')
			$("#reviewType").val("Approve");
		else
			$("#reviewType").val("Reject");
		$('#signacherModal').modal('show');

	}
	function submitBtnbut(sibGroupId) {
		// 		<tr id="${s.id}_SubGroupObservationsTr" style="display: none;">
		// 		<td colspan="5" id="${s.id}_SubGroupObservationsTd"></td>
		var result = asynchronousAjaxCall(mainUrl
				+ "/expermentalDesign/createExpermentalDesignEFrom/"
				+ sibGroupId);
		if (result != "undefined" && result != '') {
			$("#" + sibGroupId + "_SubGroupObservationsTd").html(result);
			$("#" + sibGroupId + "_SubGroupObservationsTr").show(1500);
			$("#" + sibGroupId + "_divhide").show();
			$("#" + sibGroupId + "_divshow").hide();
		}
		// 		$("#sibGroupId").val(sibGroupId);
		// 		$("#form").submit();
	}
	function hideDiv(sibGroupId, hide, show) {
		debugger;
		$("#" + sibGroupId + "_SubGroupObservationsTr").hide(500);
		$("#" + hide).hide();
		$("#" + show).show();
	}
	function submitBtnbut2(sibGroupId) {
		$("#sibGroupId2").val(sibGroupId);
		$("#form2").submit();
	}
	function sendtoReviewFunction() {
		var result = asynchronousAjaxCall(mainUrl
				+ "/designReview/checkReviewProcess/${studyDesignId}");
		if (result != "undefined") {
			if (result == "Stop")
				$('#reviewMessage')
						.html(
								"Configure At Least One Observation  For All Subgroups");
			else {
				$('#reviewMessage').html("");
				$('#obsvDstr').submit();
			}
		}

	}
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#configDataModal').modal('hide');
		$('#configData').html("");
	});
	function selectDatesAndCount(crfId, crfFrom, obserVationFor, sibGroupId,
			subGroupId) {
		debugger;
		var url = "";
		if (obserVationFor == 'Acclimatization') {
			url = mainUrl
					+ "/acclimatization/observationConfig/${studyDesignId}"
					+ "/" + crfId + "/" + crfFrom + "/" + obserVationFor + "/0";
		} else {
			url = mainUrl
					+ "/acclimatization/observationConfig/${studyDesignId}"
					+ "/" + crfId + "/" + crfFrom + "/" + obserVationFor + "/"
					+ subGroupId;
		}
		var result = asynchronousAjaxCall(url);
		if (result != 'undefined' && result != '') {
			if (obserVationFor == 'Acclimatization') {
				$("#configDataModalHeadding").html(
						"Acclimatization Configuration");
			} else {
				$("#configDataModalHeadding").html("Treatment Configuration");
			}
			$("#configDataModalData").html(result);
			$('#configDataModal').modal('show');
		} else
			alertify.error("Unable to display page");
	}

	function saveStudyAcclamatizationDatesDetails(observationDateId, value,
			obserVationFor) {
		debugger;
		var result = asynchronousAjaxCall(mainUrl
				+ "/acclimatization/updateStudyAcclamatizationDatesDetails/"
				+ observationDateId + "/" + value + "/" + obserVationFor);
		if (result != 'undefined' && result != '') {
		} else
			$("#vlaue_" + id).val("");

	}
	function removeStudyAcclamatizationDatesDetails(observationDateId, dayNo,
			day, gender, obserVationFor) {
		var result = asynchronousAjaxCall(mainUrl
				+ "/acclimatization/removeStudyAcclamatizationDatesDetails/"
				+ observationDateId + "/" + obserVationFor);
		if (result != 'undefined' && result != '') {
			$("#" + observationDateId).remove();
			days = $.grep(days, function(val) {
				return val !== day;
			});
		}
	}
	function acceptCheckBox() {
		$('#configDataModal').modal('hide');
	}
	function dayOrWeekValidation(id, sibGroupId, messageId, obserVationFor,
			subGroupId) {
		debugger;
		if ($('input[name="dayOrWeekRadio_' + id + sibGroupId + '"]:checked').length > 0) {
			$('#' + messageId).html("");
		} else {
			$('#' + messageId).html("Required Field.");
		}
		console.log($(
				'input[name="dayOrWeekRadio_' + id + sibGroupId + '"]:checked')
				.val());
		changeAcclimatizationConfigValue(id, sibGroupId, $(
				'input[name="dayOrWeekRadio_' + id + sibGroupId + '"]:checked')
				.val(), "type", obserVationFor, subGroupId);
	}
	function signValidation(id, sibGroupId, messageId, obserVationFor,
			subGroupId) {
		debugger;
		if ($("#sign_" + id + sibGroupId).val() != '-1') {
			$('#' + messageId).html("");
		} else {
			$('#' + messageId).html("Required Field.");
		}
		var value = $("#sign_" + id + sibGroupId).val();
		if (value == '+')
			value = 'plus';
		else if (value == '-')
			value = 'mines';
		else if (value == '+/-')
			value = 'bouth';
		changeAcclimatizationConfigValue(id, sibGroupId, value,
				"deviationSign", obserVationFor, subGroupId);
	}
	function windowValidation(id, sibGroupId, messageId, obserVationFor,
			subGroupId) {
		if ($("#window_" + id + sibGroupId).val() == '') {
			$("#window_" + id + sibGroupId).val(0)
		}
		changeAcclimatizationConfigValue(id, sibGroupId, $(
				"#window_" + id + sibGroupId).val(), "deviation",
				obserVationFor, subGroupId);
	}

	function changeAcclimatizationConfigValue(crfId, sibGroupId, value, field,
			obserVationFor, subGroupId) {
		var url = "";
		debugger;
		if (obserVationFor == 'Acclimatization') {
			url = mainUrl
					+ "/acclimatization/changeAcclimatizationConfigValue/${studyDesignId}/"
					+ crfId + "/" + value + "/" + obserVationFor + "" + "/"
					+ field + "/0"
		} else {
			url = mainUrl
					+ "/acclimatization/changeAcclimatizationConfigValue/${studyDesignId}/"
					+ crfId + "/" + value + "/" + obserVationFor + "" + "/"
					+ field + "/" + subGroupId;
		}
		var result = asynchronousAjaxCall(url);
		if (result != 'undefined' && result != '') {
			return true;
		} else {
			return false;
		}
	}
	function checkBoxValidation(id, sibGroupId, obserVationFor, subGroupId) {
		// 		$('#dayOrWeekRadio_' + id + sibGroupId + "_Msg").html("");
		debugger;

		if ($('#crfCheckBox_' + id + sibGroupId).is(":checked")) {
			if (changeAcclimatizationConfig(id, sibGroupId, 'active',
					obserVationFor, subGroupId)) {
				enableRow(id, sibGroupId);
			}
		}
		if ($('#crfCheckBox_' + id + sibGroupId).is(":checked") == false) {
			if (changeAcclimatizationConfig(id, sibGroupId, 'inactive',
					obserVationFor, subGroupId)) {
				disableRow(id, sibGroupId);
			}
		}
	}
	function enableRow(id, sibGroupId) {
		debugger;
		$("#dayRadio_" + id + sibGroupId).prop("disabled", false);
		$("#weekRadio_" + id + sibGroupId).prop("disabled", false);
		$("#button_" + id + sibGroupId).prop("disabled", false);
		$("#sign_" + id + sibGroupId).prop("disabled", false);
		$("#window_" + id + sibGroupId).prop("disabled", false);
	}
	function disableRow(id, sibGroupId) {
		debugger;
		$("#dayRadio_" + id + sibGroupId).prop("disabled", true);
		$("#weekRadio_" + id + sibGroupId).prop("disabled", true);
		$("#button_" + id + sibGroupId).prop("disabled", true);
		$("#sign_" + id + sibGroupId).prop("disabled", true);
		$("#window_" + id + sibGroupId).prop("disabled", true);
	}
	function changeAcclimatizationConfig(crfId, sibGroupId, status,
			obserVationFor, subGroupId) {
		debugger;
		var url = '';
		if (obserVationFor == 'Acclimatization') {
			url = mainUrl
					+ "/acclimatization/changeAcclimatizationConfig/${studyDesignId}/"
					+ +crfId + "/" + status + "/" + obserVationFor + "/0";
		} else {
			url = mainUrl
					+ "/acclimatization/changeAcclimatizationConfig/${studyDesignId}/"
					+ +crfId + "/" + status + "/" + obserVationFor + "/"
					+ subGroupId;
		}
		// 		alert(url);
		if (url != '') {
			var result = asynchronousAjaxCall(url);
			if (result != 'undefined' && result != '') {
				return true;
			} else {
				return false;
			}
		}

	}

	function studySendToReivew(id, msgId) {
		$("#"+msgId).html("");
		var comment  = $("#"+id).val();
		comment = comment.trim();
		if(comment != ''){
			if (confirm("Are you sure you want to Send Study For Review?")){
				$("#commentForReview").val(comment);
				$("#sentReviewId").submit();		
			}
		}else{
			$("#"+msgId).html("Required Field");	
		}

	}
</script>
</html>