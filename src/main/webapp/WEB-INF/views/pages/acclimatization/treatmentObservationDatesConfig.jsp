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
</script>
</head>
<body>
	<c:set var="genderCheck" value="0"></c:set>
	<table id="">
		<thead>
			<tr>
				<th colspan="3">Observation</th>
				<c:if test="${study.gender eq 'Both' && study.splitStudyByGender}">
					<tH>Gender</tH>
					<c:set var="genderCheck" value="1"></c:set>
				</c:if>
				<td>Select Date</td>
			</tr>
			<tr>
				<th colspan="3">${crf.prefix}-${crf.observationName}</th>
				<c:if test="${study.gender eq 'Both' && study.splitStudyByGender}">
					<td><input type="radio" name="obsercationGender" value="Male"
						onchange="genderSpecificDates('obsercationGender', 'obsercationGenderMsg', '')">Male<br />
						<input type="radio" name="obsercationGender" value="Female"
						onchange="genderSpecificDates('obsercationGender', 'obsercationGenderMsg', '')">Female<br />
						<font class="red" id="obsercationGenderMsg"></font></td>
				</c:if>
				<td><input type="text" id="observationDate"
					name="observationDate" readonly="readonly" /></td>
			</tr>

		</thead>
		<tbody id="tablebody">
			<tr>
				<c:if test="${study.gender eq 'Both' && study.splitStudyByGender}">
					<tH>Gender</tH>
				</c:if>
				<th>Day</th>
				<th>Date</th>
				<th>No of Entries</th>
				<td />
			</tr>
			<c:forEach items="${dbacclimatizationDates}" var="accdate">
				<tr id="${accdate.id}">
					<c:if test="${study.gender eq 'Both' && study.splitStudyByGender}">
						<td>${accdate.gender}</td>
					</c:if>
					<td>${accdate.dayNo}<script type="text/javascript">
						// 						debugger;
						days.push('${accdate.gender}_${accdate.dateString}');
					</script>
					</td>
					<td>${accdate.dateString}</td>
					<td><input type="number" id="vlaue_${accdate.id}"
						onchange="saveStudyAcclamatizationDatesDetails('${accdate.id}', this.value)"
						value="${accdate.noOfEntry}" /></td>
					<c:choose>
						<c:when
							test="${study.gender eq 'Both' && study.splitStudyByGender}">
							<td><input type="button" value="Remove"
								onclick="removeStudyAcclamatizationDatesDetails('${accdate.id}', '${accdate.dayNo}', '${accdate.dateString}', '${study.gender}')" /></td>
						</c:when>
						<c:otherwise>
							<td><input type="button" value="Remove"
								onclick="removeStudyAcclamatizationDatesDetails('${accdate.id}', '${accdate.dayNo}', '${accdate.dateString}', 'gender')" /></td>
						</c:otherwise>
					</c:choose>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
<!-- <script src='/TOX/static/js/study/studyDesign.js'></script> -->
<script type="text/javascript">
	$(document).ready(function() {
		genderSpecificDates('', '', 'Male');
	});
	function genderSpecificDates(id, msg, gender) {
		// 		debugger;
		if (gender == '') {
			gender = $('input[name=' + id + ']:checked').val();
		}

		var mimumDate = '${acclimatizationStart}';
		var maximumDate = '${acclimatizationEnd}';
		var numberOfMonthsToDisplay = parseInt('${noOfMounths}');
		if (gender == 'Female') {
			mimumDate = '${femaleAcclimatizationStart}';
			maximumDate = '${femaleAcclimatizationEnd}';
			numberOfMonthsToDisplay = parseInt('${femalNoOfMounths}');
		}
		idDates['dayOrWeek_${crf.id}'] = [];
		$("#observationDate")
				.datepicker(
						{
							dateFormat : $("#dateFormatJsp").val(),
							minDate : mimumDate,
							maxDate : maximumDate,
							numberOfMonths : numberOfMonthsToDisplay, // Display two months at once
							//				    selectMultiple: true,
							beforeShow: function (input, inst) {
							        var rect = input.getBoundingClientRect();
							        setTimeout(function () {
								        inst.dpDiv.css({ top: rect.top + 30, left: rect.left + 0 });
							        }, 0);
							    },
							onSelect : function(dateText, inst) {
								var dateFalg = true;

								var genderCheck = '${genderCheck}';
								if (genderCheck == "1") {
									if (requiredValidationForRadio(
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
													+ "/acclimatization/observationDateDetails/${study.id}/${crf.id}/Treatment/"
													+ dateText+"/"
													+ $("#subGroupId").val()
													+ "/true/" + gender);
											if (result != 'undefined'
													&& result != '') {
												$("#tablebody").append(result);
												days.push(gender + "_"
														+ dateText);
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
// 										alert(mainUrl
// 												+ "/acclimatization/observationDateDetails/${study.id}/${crf.id}/Treatment/"
// 												+ dateText +"/"
// 												+ $("#subGroupId").val()
// 												+ "/false/0");
										var result = asynchronousAjaxCall(mainUrl
												+ "/acclimatization/observationDateDetails/${study.id}/${crf.id}/Treatment/"
												+ dateText+"/"
												+ $("#subGroupId").val()
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
</script>
</html>


