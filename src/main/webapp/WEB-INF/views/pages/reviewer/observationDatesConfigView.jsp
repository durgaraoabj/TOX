<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script type="text/javascript">
	var days = [];
	var insturments = {};
	var obJinsleftTest = {};
	var obJinsleftTestOrder = {};
	var obJinsleftTestDislayString = {};
	var obJinsRigthTest = {};
	var obJinsRigthTestOldOrder = {};
	var obJinsRigthTestNewOrder = {};
	var obJinsRightTestDislayString = {};
</script>
</head>
<body>
	<c:set var="genderCheck" value="0"></c:set>
	<table>
		<tr>
			<td colspan="5">
				<table 
					style="width: 100%">
					<thead>
						<tr>
							<c:if
								test="${study.gender eq 'Both' && !study.splitStudyByGender}">
								<tH>Gender</tH>
							</c:if>
							<th>Day</th>
							<th>Date</th>
							<th>No of Entries</th>
							<c:if test="${crf.type eq 'CP'}">
								<th>Parameters Configure</th>
							</c:if>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${observationDates}" var="observationDate">
							<tr id="${observationDate.id}">
								<c:if
									test="${study.gender eq 'Both' && !study.splitStudyByGender}">
									<td>${observationDate.gender}</td>
								</c:if>
								<td>${observationDate.dayNo}<script type="text/javascript">
									days
											.push('${observationDate.gender}_${observationDate.dateString}');
								</script>
								</td>
								<td>${observationDate.dateString}</td>
								<td><c:choose>
												<c:when test="${crf.type eq 'CP'}">
													<input type="number" id="vlaue_${observationDate.id}"
														disabled="disabled" class="form-control" />
												</c:when>
												<c:otherwise>
													<input type="number" id="vlaue_${observationDate.id}"
														onchange="saveStudyAcclamatizationDatesDetails('${observationDate.id}', this.value, '${obserVationFor}')"
														class="form-control" value="${observationDate.noOfEntry}"
														disabled="disabled" />
												</c:otherwise>
											</c:choose></td>

							</tr>
							<c:if test="${crf.type eq 'CP'}">
								<tr id="${observationDate.id}_clinpath" style="display: none">
									<td colspan="6" id="${observationDate.id}_clinpath_td">
										<script type="text/javascript">
										parameterConfig('${observationDate.id}', 'buttonTdId_${observationDate.id}', '${obserVationFor}')
										</script>
									</td>
									
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</td>
		</tr>
	</table>

</body>
<script src='/TOX/static/js/insturmentParameterSelection.js'></script>
<script src='/TOX/static/js/validaiton.js'></script>
<script type="text/javascript">
	$(function() {
		$("#tablebody").DataTable({
			"paging" : false,
			"lengthChange" : false,
			"searching" : false,
			"ordering" : false,
			"info" : false,
			"autoWidth" : false,
		});
	});

	var genderCheck = '';
	$(document).ready(
			function() {
				genderCheck = '${genderCheck}';
				if (genderCheck == "0") {
					genderSpecificDates('', '', 'Male', '${obserVationFor}',
							'${subGroupId}');
				} else
					genderSpecificDates('', '', '', '${obserVationFor}',
							'${subGroupId}');

			});
	function genderSpecificDates(id, msg, gender, obserVationFor, subGroupId) {
		// 		debugger;
		var mimumDate = '';
		var maximumDate = '';
		var numberOfMonthsToDisplay = '';
		var dateFormatDispay = $("#dateFormatJsp").val();

		if (gender == '') {
			mimumDate = '${acclimatizationStart}';
			maximumDate = '${acclimatizationEnd}';
			numberOfMonthsToDisplay = parseInt('${noOfMounths}');
		} else if (gender == 'Male') {
			mimumDate = '${acclimatizationStart}';
			maximumDate = '${acclimatizationEnd}';
			numberOfMonthsToDisplay = parseInt('${noOfMounths}');

		} else if (gender == 'Female') {
			// 			debugger;
			mimumDate = '${femaleAcclimatizationStart}';
			maximumDate = '${femaleAcclimatizationEnd}';
			numberOfMonthsToDisplay = parseInt('${femalNoOfMounths}');
		}
		$("#observationDate").datepicker("option", "minDate", mimumDate);
		$("#observationDate").datepicker("option", "maxDate", maximumDate);

		// 		idDates['dayOrWeek_${crf.id}'] = [];
		$("#observationDate")
				.datepicker(
						{
							dateFormat : dateFormatDispay,
							minDate : mimumDate,
							maxDate : maximumDate,
							numberOfMonths : numberOfMonthsToDisplay, // Display two months at once
							//				    selectMultiple: true,
							beforeShow : function(input, inst) {
								var rect = input.getBoundingClientRect();
								setTimeout(function() {
									inst.dpDiv.css({
										top : rect.top + 30,
										left : rect.left + 0
									});
								}, 0);
							},
							onSelect : function(dateText, inst) {
								var dateFalg = true;
								debugger;

								if (genderCheck == "1") {
									if (checkCheckboxRequired(
											'obsercationGender',
											'obsercationGenderMsg')) {
										dateFalg = false;
									} else {
										var gender = $(
												'input[name=obsercationGender]:checked')
												.val();
										var flag = true;
										// 										debugger;
										$
												.each(
														days,
														function(index, value) {
															if ((gender + "_" + dateText) == value) {
																flag = false;
															}
														});
										if (flag) {
											var result = asynchronousAjaxCall(mainUrl
													+ "/acclimatization/observationDateDetails/${study.id}/${crf.id}/"
													+ obserVationFor
													+ "/"
													+ dateText
													+ "/"
													+ subGroupId
													+ "/true/"
													+ gender);
											//						        	debugger;
											if (result != 'undefined'
													&& result != '') {
												$("#tablebody").append(result);
												days.push(gender + "_"
														+ dateText);
												$("#observationDate").val('');
											}
										} else {
											alert("Duplicate Date")
										}
									}
								} else {
									var flag = true;
									// 									debugger;
									$.each(days, function(key, value) {
										if (value == dateText) {
											flag = false;
										}
									});
									if (flag) {
										var result = asynchronousAjaxCall(mainUrl
												+ "/acclimatization/observationDateDetails/${study.id}/${crf.id}/"
												+ obserVationFor
												+ "/"
												+ dateText
												+ "/"
												+ subGroupId
												+ "/false/0");
										//						        	debugger;
										if (result != 'undefined'
												&& result != '') {
											$("#tablebody").append(result);
											days.push("gender_" + dateText);
										}
									} else {
										alert("Duplicate Date")
									}

								}
							}
						});

	}

	var displayedParametesrs = [];
	var buttonTdIdOfstudyAcclamatizationDatesId = [];
	function parameterConfig(studyAcclamatizationDatesId, buttonTdId,
			obserVationFor) {
		debugger;
		var fatch = true;
		$.each(displayedParametesrs, function(key, value) {
			if (value == studyAcclamatizationDatesId) {
				unhideParameterConfiguration(studyAcclamatizationDatesId,
						buttonTdId, obserVationFor);
				fatch = false;
				return true;
			}
		});
		if (fatch) {
			var result = asynchronousAjaxCall(mainUrl
					+ "/grouping/observatoinIntrumentAndPerameters/"
					+ studyAcclamatizationDatesId + "/" + obserVationFor);
			if (result != 'undefined' && result != '') {
				$("#" + studyAcclamatizationDatesId + "_clinpath_td").html(
						result);
				unhideParameterConfiguration(studyAcclamatizationDatesId,
						buttonTdId, obserVationFor);
				displayedParametesrs.push(studyAcclamatizationDatesId);
				buttonTdIdOfstudyAcclamatizationDatesId[studyAcclamatizationDatesId] = buttonTdId;
			}
		}
	}
	function unhideParameterConfiguration(studyAcclamatizationDatesId,
			buttonTdId, obserVationFor) {
		$("#" + studyAcclamatizationDatesId + "_clinpath").show(1500);
		$("#" + buttonTdId).html(
				"<i class=\"fas fa-minus\" onclick=\"hideParameterConfiguration('"
						+ studyAcclamatizationDatesId + "', '" + buttonTdId
						+ "', '" + obserVationFor + "')\"></i>");
	}
	function hideParameterConfiguration(studyAcclamatizationDatesId,
			buttonTdId, obserVationFor) {
		$("#" + studyAcclamatizationDatesId + "_clinpath").hide(500);
		$("#" + buttonTdId).html(
				"<i class=\"fas fa-plus\" onclick=\"parameterConfig('"
						+ studyAcclamatizationDatesId + "', '" + buttonTdId
						+ "' , '" + obserVationFor + "')\"></i>");
	}

	function acceptCheckBox() {
		$('#configDataModal').modal('hide');
	}
</script>
</html>