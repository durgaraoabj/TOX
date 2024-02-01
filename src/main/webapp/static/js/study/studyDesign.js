$(document).ready(function() {
	$("#startAndEnd2").hide();
});
function desableOrEnableSubgroup() {
	var checkedValue = $('input[name="subgroupRequired"]:checked').val();
	var noOfGroups = $("#noOfGroups").val();
	if (checkedValue === undefined) {
		$("#hedding")
				.html(
						"<table width='95%'><tr><th width='5%'></th><th width='20%'>Sub Group Name</th><th width='20%'>Individual Animals</th><th width='15%'>Gender<th>"
								+ "<th width='20%'>Dose <select name='doseMgkg' ><option value='mg/kg'>mg/kg</option><option value='unit1'>unit1</option></select></th><th width='20%'> Dose Volume</th></tr></table></th></tr>");
		for (var i = 1; i <= noOfGroups; i++) {
			$("#subGroupCount" + i).removeAttr('disabled');
			$("#ainimalDetails" + i).html("");
		}
	} else {
		groupInfo($("#noOfGroups").val());
	}
}

$("#noOfGroups").val("");
$("#subjects").val("");
function groupInfo() {
	if (!requiredValidation("noOfGroups", "noOfGroupsmsg", "")) {
		var value = $("#noOfGroups").val();
		$("#groupInfo").html("");
		$("#noOfGroupsmsg").html("");
		var batch = value.trim();
		if (batch != '' && batch > 0) {
			var gender = $('input[name="groupGender"]:checked').val();
			if (typeof gender == "undefined")
				gender = "";
			$("#groupInfo").html(generateHedder(batch, gender, ''));
			$("#subgroupRequired").removeAttr('disabled');
		} else {
			$("#subgroupRequired").prop('checked', true);
			if (batch <= 0)
				$("#noOfGroupsmsg").html("Value must more than 0");
			else
				$("#noOfGroupsmsg").html("Required Field.");
		}
	}
}

function generateHedder(batch, gender, sugroups) {
	var ele = "<table class='table table-bordered table-striped'><tr><th>Group Name</th><th>No. of Sub Groups </th>"
			+ "<th colspan='4' id='hedding'><table width='95%'><tr>";
	ele += "<th width='30%'>Individual Animals</th><th width='10%'>Gender<th><th width='30%'>Dose <select name='doseMgkg' ><option value='mg/kg'>mg/kg</option><option value='unit1'>unit1</option></select></th><th width='30%'>Dose Volume</th></tr></table></th></tr>";
	ele += generateMiddelCols(batch, gender, sugroups);
	return ele;
}
function generateMiddelCols(batch, gender, sugroups) {
	var ele = "";
	for (var i = 1; i <= batch; i++) {
		var id = i;
		if (gender != '')
			id = i + "_" + gender
		var subele = "<tr><td><input type='text' name='groupName" + i
				+ "'  id='groupName" + i
				+ "' class='form-control'/><font color='red' id='groupName" + i
				+ "msg' /></font></td>";
		subele += "<td><input type='text' size='2'  name='subGroupCount"
				+ i
				+ "'  id='subGroupCount"
				+ i
				+ "' onchange='generateSubGroups(this.value, "
				+ i
				+ ")' "
				+ "class='form-control' disabled='disabled' onkeypress='return event.charCode >= 48 && event.charCode <= 57' />"
				+ "<font color='red' id='subGroupCount" + i
				+ "msg' /></font></td>";
		subele += "<th colspan='4' id='ainimalDetails" + i
				+ "'><table width='95%'>";
		if (gender != "Both") {
			subele += generategenderRow(batch, gender, i);
		} else {
			subele += generategenderRow(batch, "Male", i);
			subele += generategenderRow(batch, "Female", i);
		}
		subele += "</tr></table></th></tr>";
		ele += subele;
	}
	ele += "<table>";
	return ele;
}

function generategenderRow(batch, gender, row) {
	$("#subgroupRequired").prop('checked', true);
	var i = row;
	if (gender != '')
		i = row + "_" + gender;
	var subele = "<tr><td width='30%'><input type='text' size='2'  onkeypress='return event.charCode >= 48 && event.charCode <= 57' name='subGroupAnimals"
			+ i
			+ "'  id='subGroupAnimals"
			+ i
			+ "' class='form-control'/><font color='red' id='subGroupAnimals"
			+ i + "msg' /></font></td>";
	subele += "<td width='10%'>" + gender + "</td>"
	subele += "<td width='30%'><input type='text' name='concentration"
			+ i
			+ "'  id='concentration"
			+ i
			+ "' class='form-control' onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46' size='6' />"
			+ "<font color='red' id='concentration" + i + "msg' /></font></td>";
	subele += "<td width='30%'><input type='text' name='doseVolume"
			+ i
			+ "'  id='doseVolume"
			+ i
			+ "' class='form-control' onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46' size='6' />"
			+ "<font color='red' id='doseVolume" + i + "msg' /></font></td>";
	subele += "</tr>";
	return subele;
}

function generateSubGroups(value, rowId) {
	var gender = $('input[name="groupGender"]:checked').val();
	if (typeof gender == "undefined")
		gender = "";
	$("#ainimalDetails" + rowId).html("");
	var no = value.trim();
	if (no != '' && no > 0) {
		var ele = "";
		for (var i = 1; i <= no; i++) {
			ele += generateSubGroupsRow(gender, i, rowId);
		}
		ele += "</table>";
		$("#ainimalDetails" + rowId).html(ele);
	}

}

function generateSubGroupsRow(gender, sugroups, row) {
	var i = row + "_" + sugroups;
	if (gender != '')
		i = row + "_" + sugroups + "_" + gender;
	if (gender != "Both") {
		return subgroupsubRow(gender, i, sugroups);
	} else {
		var ele = subgroupsubRow("Male", i, sugroups);
		ele += subgroupsubRow("Female", i, sugroups);
		return ele;
	}

}
function subgroupsubRow(gender, i, sugroups) {
	var subele = "<tr><td width='5%'>S" + sugroups
			+ "</td><td width='20%'><input  name='subGroupName" + i
			+ "'  id='subGroupName" + i
			+ "' class='form-control'/><font color='red' id='subGroupName" + i
			+ "msg' /></font></td>";
	subele += "<td width='20%'><input type='text' size='2'  onkeypress='return event.charCode >= 48 && event.charCode <= 57' name='subGroupAnimals"
			+ i
			+ "'  id='subGroupAnimals"
			+ i
			+ "' class='form-control'/><font color='red' id='subGroupAnimals"
			+ i + "msg' /></font></td>";
	subele += "<td width='15%'>" + gender + "</td>"
	subele += "<td width='20%'><input type='text' name='doseVolume"
			+ i
			+ "'  id='doseVolume"
			+ i
			+ "' class='form-control' onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46' size='6' />"
			+ "<font color='red' id='doseVolume" + i + "msg' /></font></td>";
	subele += "<td width='20%'><input type='text' name='concentration"
			+ i
			+ "'  id='concentration"
			+ i
			+ "' class='form-control' onkeypress='return event.charCode >= 48 && event.charCode <= 57 || event.charCode == 46' size='6' />"
			+ "<font color='red' id='concentration" + i + "msg' /></font></td>";
	subele += "</tr>";
	return subele;
}
var dateFlag = false;
var dateRanFlag = false;
var spaecFlag = false;
var accSt = false;
var accEn = false;
var treSt = false;
var treEn = false;

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
function radamizationStartDateValidation(id, messageId) {
	var value = $('#radamizationStDate').val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html("Required Field.");
		dateRanFlag = false;
	} else {
		$('#' + messageId).html("");
		dateRanFlag = true;
	}
	return dateRanFlag;
}
function acclimatizationStDateValidation(id, messageId) {
	var value = $('#acclimatizationStDate').val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html("Required Field.");
		accSt = false;
	} else {
		$('#' + messageId).html("");
		accSt = true;
	}
	return accSt;
}
function acclimatizationEnDateValidation(id, messageId) {
	debugger;
	var value = $('#acclimatizationEnDate').val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html("Required Field.");
		accEn = false;
	} else {
		var valueSt = $('#acclimatizationStDate').val();
		if (valueSt != "") {
			if (value >= valueSt) {
				$('#' + messageId).html("");
				accEn = true;
				var dayDiff = Math.ceil((new Date(value) - new Date())
						/ (1000 * 3600 * 24)) + 1;
				$("#treatmentStDate").datepicker("option", "minDate", dayDiff);
				$("#treatmentEnDate").datepicker("option", "minDate", dayDiff);
			} else {
				$('#' + messageId).html("End date greater than start date .");
				accEn = false;
				$('#acclimatizationEnDate').val("");
			}

		} else {
			$('#' + messageId).html(
					"Acclimatization Start Date Required Field.");
			accEn = false;
			$('#acclimatizationEnDate').val("");
		}

	}
	return accEn;
}
function treatmentStDateValidation(id, messageId) {
	var value = $('#treatmentStDate').val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html("Required Field.");
		treSt = false;
	} else {
		var valueEn = $('#acclimatizationEnDate').val();
		if (valueEn != "") {
			if (value > valueEn) {
				$('#' + messageId).html("");
				treSt = true;
			} else {
				$('#' + messageId)
						.html(
								"Treatment Start date greater than acclimatization end date .");
				treSt = false;
				$('#treatmentStDate').val("");
			}

		} else {
			$('#' + messageId).html("Acclimatization End Date Required Field.");
			treSt = false;
			$('#treatmentStDate').val("");
		}
	}
	return treSt;
}
function treatmentEnDateValidation(id, messageId) {
	var value = $('#treatmentEnDate').val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html("Required Field.");
		treEn = false;
	} else {
		var valueSt = $('#treatmentStDate').val();
		if (valueSt != "") {
			if (value >= valueSt) {
				$('#' + messageId).html("");
				treEn = true;
			} else {
				$('#' + messageId).html("End date greater than start date .");
				treEn = false;
				$('#treatmentEnDate').val("");
			}

		} else {
			$('#' + messageId).html("Treatment Start Date Required Field.");
			treEn = false;
			$('#treatmentEnDate').val("");
		}
	}
	return treEn;
}
function requiredValidation(id, msgId, compareValue) {
	$("#" + msgId).html("");
	var value = $("#" + id).val();
	value = value.trim();
	if (value == compareValue) {
		$("#" + msgId).html("Required Field");
		return true;
	} else
		return false;
}
function requiredValidationForRadio(name, msgId) {
	$("#" + msgId).html("");
//	alert($('input[name=' + name + ']:checked').length)
	if ($('input[name=' + name + ']:checked').length == 0) {
		$("#" + msgId).html("Required Field");
		return true;
	} else
		return false;
}

function studyCreateFormSubmitBtnbut() {
	debugger;
	$("#principalInvestigatormsg").html("");
	var submitFlag = true;
	if (requiredValidation("noOfGroups", "noOfGroupsmsg", "")) {
		submitFlag = false;
	}
	if (requiredValidation("subjects", "subjectsmsg", "")) {
		submitFlag = false;
	}
	if (requiredValidation("speciesId", "speciesIdMsg", "-1")) {
		submitFlag = false;
	}
	
	if (requiredValidationForRadio("groupGender", "groupGenderMsg")) {
		submitFlag = false;
	}
	if (requiredValidationForRadio("calculationBased", "calculationBasedmsg")) {
		submitFlag = false;
	}
	if (requiredValidation("weightUnits", "weightUnitsMsg", "-1")) {
		submitFlag = false;
	}
//	if (requiredValidationForRadio("clinpathPerameters",
//			"clinpathPerametersMsg")) {
//		submitFlag = false;
//	}

	if (requiredValidation("acclimatizationStDate", "acclimatizationStDateMsg",
			"")) {
		submitFlag = false;
	}
	
	if (requiredValidation("acclimatizationEnDate", "acclimatizationEnDateMsg",
			"")) {
		submitFlag = false;
	}
	if (requiredValidation("treatmentStDate", "treatmentStDateMsg", "")) {
		submitFlag = false;
	}
	if (requiredValidation("treatmentEnDate", "treatmentEnDateMsg", "")) {
		submitFlag = false;
	}

	debugger;
	if (!$('#splitStudyGender').prop('disabled')) {
		var checkedValue = $('input[name="splitStudyGender"]:checked').val();
		var gender = $('input[name="groupGender"]:checked').val();
		if (checkedValue === undefined) {
			if (requiredValidation("acclimatizationStDate2",
					"acclimatizationStDateMsg2", "")) {
				submitFlag = false;
			}
			if (requiredValidation("acclimatizationEnDate2",
					"acclimatizationEnDateMsg2", "")) {
				submitFlag = false;
			}
			if (requiredValidation("treatmentStDate2", "treatmentStDateMsg2", "")) {
				submitFlag = false;
			}
			if (requiredValidation("treatmentEnDate2", "treatmentEnDateMsg2", "")) {
				submitFlag = false;
			}
		}
	}

	var flag = true;
	if ($("#noOfGroups").val().trim() == '') {
		$("#noOfGroupsmsg").html("Required Field");
		flag = false;
	} else {

		var value = Number($("#noOfGroups").val().trim());
		if (value > 0) {
			var groupNames = [];
			for (var v = 1; v <= value; v++) {
				// alert(v)
				$("#groupName" + v + "msg").html("");
				$("#groupTest" + v + "msg").html("");
				// alert($("#groupName"+v).val());
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

				var checkedValue = $('input[name="subgroupRequired"]:checked')
						.val();
				if (checkedValue === undefined) {
					var groupTest = $("#groupTest" + v).val();
					// alert($("#groupName"+v).val());
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

			}
		} else {
			$("#noOfGroupsmsg").html("Value must more than 0");
			flag = false;
		}
	}

	if (flag && submitFlag) {
//		alert("No Errors");
		 $('#saveMetaDataForm').submit();
	}
}

function sample() {
	$('input').iCheck({
		checkboxClass : 'icheckbox_square-blue',
		radioClass : 'iradio_square-blue',
		increaseArea : '20%' /* optional */
	});
}
function genderValidation() {
	var gender = $('input[name="groupGender"]:checked').val();
	$("#genderRow").html(gender);
	if (gender != 'Both') {
		var checkedValue = $('input[name="splitStudyGender"]:checked').val();
		if (checkedValue === undefined) {
			$("#splitStudyGender").prop('checked', true);
			$("#startAndEnd2").hide();
		}
		$("#splitStudyGender").prop('disabled', true);
	} else {
		$("#splitStudyGender").removeAttr('disabled');
	}
}

function addStartAndEndDates() {
	var checkedValue = $('input[name="splitStudyGender"]:checked').val();
	var gender = $('input[name="groupGender"]:checked').val();
	if (checkedValue === undefined) {
		$("#genderRow").html("Male");
		$("#genderRow2").html("Female");
		$("#startAndEnd2").show();
	} else {
		$("#startAndEnd2").hide();
		$("#genderRow").html(gender);
	}
}

function acclimatizationValidation(id, msgId, startOrEnd, count) {
	debugger;
	if (!requiredValidation(id, msgId, "")) {
		
		if (startOrEnd == 'End' || startOrEnd == 'end') {
			var start = "treatmentStDate";
			var end = "treatmentEnDate";
			if (count == 2) {
				start += "2";
				end += "2";
			}
			var value = $('#' + id).val();
			var dayDiff = Math.ceil((new Date(value) - new Date())
					/ (1000 * 3600 * 24)) + 1;
			$("#" + start).datepicker("option", "minDate", dayDiff);
			$("#" + end).datepicker("option", "minDate", dayDiff);
		} else {
			var startid = "acclimatizationStDate";
			var startmsgId = "acclimatizationStDateMsg"
			if (count == 2) {
				startid += "2";
				startmsgId += "2";
			}
			if (requiredValidation(startid, startmsgId, "")) {
				$('#' + id).val("");
			} else {
				var start = "acclimatizationEnDate";
				if (count == 2) {
					start += "2";
				}
				var value = $('#' + id).val();
				var dayDiff = Math.ceil((new Date(value) - new Date())
						/ (1000 * 3600 * 24));
				$("#" + start).datepicker("option", "minDate", dayDiff);
			}
		}
	}
}
