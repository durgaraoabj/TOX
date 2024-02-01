function testCodesProfileView(profileId) {
	var result = asynchronousAjaxCall(mainUrl
			+ "/grouping/testCodesProfileView/" + profileId);
	if (result != 'undefined' && result != '') {
		$("#profileData").html(result);
		$('#profiledataModal').modal('show');
	} else
		alert("Unable to display Profile details");
}
function acceptCheckBox(id) {
	$('#' + id).modal('hide');
}
var stdNoFlag = false;
function checkStudyNumber(id, messageId, backed) {
	$('#' + messageId).html('');
	var value = $('#' + id).val();
	if (value == null || value == "" || value == "undefined") {
		$('#' + messageId).html('Required Field.');
		stdNoFlag = false;
	} else {

		if (backed) {
			var studyCreationDto = [];
			$.ajax({
				url : $("#mainUrl").val() + '/administration/getCsrfToken',
				type : 'GET',
				success : function(data) {
					studyCreationDto.push({
						name : data.parameterName,
						value : data.token
					});
					studyCreationDto.push({
						name : "studyId",
						value : value
					});
					studyCreationDto.push({
						name : "id",
						value : 0
					});
					$.ajax({
						url : $("#mainUrl").val()
								+ '/administration/checkStudyNumber',
						type : 'POST',
						data : studyCreationDto,
						success : function(e) {
							console.log(e.success)
							if (e.Success === 'true' || e.Success === true) {
								$('#' + messageId).html(
										value + " aleredy avilable.");
								stdNoFlag = false;
								$('#' + id).val("");
							} else {
								$('#' + messageId).html('');
								stdNoFlag = true;
							}
						},
						error : function(er) {
							debugger;
						}
					});
				},
				error : function(er) {
					debugger;
				}
			});

		}

	}
	return stdNoFlag;
}

var genderselement = "<option value=\"M\">M</option><option value=\"F\">F</option>";
var rows = {};
var count = 0;
$(document)
		.ready(
				function() {
					// debugger;
					$('#profiledataModal').modal('hide');
					genderselement = "<option value=\"M\">M</option><option value=\"F\">F</option>";
					rows = {};
					count = 0;
				});

function addRow() {
	// debugger;
	var flag = 0;
	$.each(rows, function(k, v) {
		if (v == 0) {
			flag = k;
			return false;
		}
	})
	if (flag == 0) {
		count++;
		flag = count;
	}
	rows[flag] = flag;
	var row = "<tr id=\"" + flag
			+ "Tr\"><td><input type=\"text\" class='form-control' name=\""
			+ flag + "_groupName\" id=\"" + flag
			+ "_groupName\" /><font color='red' id=\"" + flag
			+ "_groupNameMsg\"></font></td>";
	row += "<td><select id=\"" + flag
			+ "_gender\" class='form-control' name=\"" + flag + "_gender\" > "
			+ genderselement + "</select>" + "<font color='red' id=\"" + flag
			+ "_genderMsg\"></font></td>";
	row += "<td id=\"" + flag + "_prefixtd\"></td>"
	row += "<td>"
			+ "<input type=\"text\" class='form-control' name=\""
			+ flag
			+ "_from\" size=\"3\" maxlength=\"3\" id=\""
			+ flag
			+ "_from\" onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" /><font color='red' id=\""
			+ flag + "_fromMsg\" ></font></td>";
	row += "<td>"
			+ "<input type=\"text\" class='form-control' name=\""
			+ flag
			+ "_to\" size=\"3\" maxlength=\"3\" id=\""
			+ flag
			+ "_to\" onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" /><font color='red' id=\""
			+ flag + "_toMsg\"></font></td>";
	row += "<td><input type=\"button\" value=\"Remove\" class='btn btn-new' onclick=\"removeRow('"
			+ flag + "')\"/></td>";
	// debugger;
	$("#groupTable").append(row);
}
function removeRow(trId) {
	rows[trId] = 0;
	$("#" + trId + "Tr").remove();
}
function studyCreateFormSubmitBtnbut() {
	if (stdNoFlag == false)
		stdNoFlag = checkStudyNumber('studyNo', 'studyNoMsg', false);

}

function testCodeSelect(id) {
	// debugger;
	if (tcs[id] == 0) {
		tcs[id] = 1;
		$('#' + id + "DivLeft").css('background-color', '#334FFF')
	} else {
		tcs[id] = 0;
		$('#' + id + "DivLeft").css('background-color', '#FFFFFF')
	}
}
function profileSelection(instrumentName, profileId) {
	// debugger;
	resetLeftTestCodes(instrumentName);
	if (profileId != -1) {
		var data = profileData(profileId);
		for (var index = 0; index < data.parameters.length; index++) {
			tcs[data.parameters[index].testCodeId] = 1;
			rigthTestCodesOrder[data.parameters[index].testCodeId] = data.parameters[index].orderNo;
		}
		moveTcsToRight(instrumentName);
	}
	instrumentProfile[instrumentName] = profileId;
}
function resetLeftTestCodes(instrumentName) {
	$("#" + instrumentName + "_RightTableBody").html("");
	$("#" + instrumentName + "_leftTableBody").html("");
	var tcIds = insturmentcs[instrumentName];
	var resultTable = "";
	rigthSideElements[instrumentName] = [];
	for (var i = 0; i < tcIds.length; i++) {
		rigthTestCodesOrder[tcIds[i]] = "";
		onlyRigthElements[tcIds[i] + "_" + instrumentName] = "";

		$('#' + tcIds[i] + "DivLeft").css('background-color', '#FFFFFF')
		tcs[tcIds[i]] = 0;
		var element = "<tr  id=\"" + tcIds[i]
				+ "DivLeft\" onclick=\"testCodeSelect('" + tcIds[i] + "')\">"
		element += "<td>" + tcName[tcIds[i]] + "</td>";
		element += "<td>" + tcOrder[tcIds[i]] + "</td></tr>";
		resultTable += element;
	}
	$("#" + instrumentName + "_leftTableBody").append(resultTable);
}
function profileData(profileId) {

	var url = mainUrl + "/grouping/profileData/" + profileId;
	var jsonData = asynchronousAjaxCall(url);
	// debugger;
	return jsonData;
}

function moveTcsToRight(instumentName) {
	$("#" + instumentName + "_RightTableBody").html("");
	// $("#rigthtbody").html("");
	 debugger;
	var tcIds = insturmentcs[instumentName];
	var resultTable = "";

	var rightSideElementIds = [];
	for (var i = 0; i < tcIds.length; i++) {
		if (tcs[tcIds[i]] == 1) {
			// debugger;
			var evalue = "";
			if (rigthTestCodesOrder[tcIds[i]] != ''
					&& rigthTestCodesOrder[tcIds[i]] != tcOrder[tcIds[i]]) {
				evalue = rigthTestCodesOrder[tcIds[i]];
			} else {
				evalue = tcOrder[tcIds[i]];
			}
			var element = "<tr  id=\"" + tcIds[i] + "DivRigth\">"
			element += "<td>" + tcName[tcIds[i]] + "</td>";
			element += "<td>" + tcOrder[tcIds[i]] + "</td>";
			element += "<td><input type=\"text\" class='form-control' name=\""
					+ tcIds[i]
					+ "_tcOrder\" id=\""
					+ tcIds[i]
					+ "_tcOrder\" onchange=\"checkvalidation('"
					+ tcIds[i]
					+ "', '"
					+ instumentName
					+ "')\" value=\""
					+ evalue
					+ "\" "
					+ " onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" ><font color=\"red\" id=\""
					+ tcIds[i] + "_tcOrderMsg\"  ></font></td></tr>";
			resultTable += element;
			// alert(element);
			onlyRigthElements[tcIds[i] + "_" + instumentName] = evalue;
			$("#" + tcIds[i] + "DivLeft").remove();
			rigthTestCodesOrder[tcIds[i]] = evalue;
			rightSideElementIds.push(tcIds[i]);
		}
	}
	rigthSideElements[instumentName] = rightSideElementIds;
	$("#" + instumentName + "_RightTableBody").append(resultTable);
}

function checkvalidation(id, instumentName) {
	$("#" + id + "_tcOrderMsg").html("");
	var tctempid = id;
	var eleid = id + "_tcOrder";
	var value = $("#" + eleid).val();
	var flag = checkDuplicate(id, value, instumentName);
	// debugger;
	if (flag) {
		rigthTestCodesOrder[tctempid] = value;
		onlyRigthElements[tctempid + "_" + instumentName] = value;
	} else {
		rigthTestCodesOrder[tctempid] = "";
		onlyRigthElements[tctempid + "_" + instumentName] = "";
	}

}
function checkDuplicate(id, value, instumentName) {
	// debugger;
	var flag = true;
	// onlyRigthElements[tcIds[i] + "_" + instumentName] = evalue;
	// var value = $("#" + id+"+"+instumentName).val();
	$("#" + id + "_tcOrderMsg").html("");
	$.each(onlyRigthElements, function(k, v) {
		if (value != '') {
			var sc = k.split("_");
			k = sc[0];
			if (sc[1] == instumentName) {
				if (v != '' && id != k) {
					// k = k + "_tcOrder";
					if (v == value) {
						$("#" + id + "_tcOrderMsg").html("Duplicate Order");
						$("#" + id + "_tcOrder").val("");
						onlyRigthElements[k + "_" + instumentName] = '';
						flag = false;
						return false;
					}
				}
			}

		} else {
			$("#" + id + "_tcOrderMsg").html("Required Field");
		}

	});
	return flag;
}

function checkMandatory(value, id, messageId) {
	$("#" + messageId).html("");
	if (value != '') {
		return true;
	} else {
		$("#" + messageId).html("Required Field");
		return false;
	}
}
function checkStudyDate() {
	$("#studyNoMsg").html("");
	if ($("#startDate").val() == '') {
		$("#studyNoMsg").html("Required Field");
		return false;
	} else
		return true;
}
function saveData(dataId) {
	// debugger;
	var vrows = rows;
	vrows[0] = 1;
	var flag = true;
	var count = 0;
	var newRows = [];
	$.each(vrows, function(k, v) {
		if (v != 0) {
			if (!checkMandatory($("#" + k + "_groupName").val(), k
					+ "_groupName", k + "_groupNameMsg")) {
				flag = false;
			}
			if (!checkMandatory($("#" + k + "_gender").val(), k + "_gender", k
					+ "_genderMsg")) {
				flag = false;
			}
			if (!checkMandatory($("#" + k + "_from").val(), k + "_from", k
					+ "_fromMsg")) {
				flag = false;
			}
			if (!checkMandatory($("#" + k + "_to").val(), k + "_to", k
					+ "_toMsg")) {
				flag = false;
			}
			count++;
			newRows.push(k);
		}
	});

	// Group Animal no from to validation
	if (flag) {
		flag = duplicateCheck(vrows);
		if (flag) {
			flag = minMaxCompare(vrows);
			if (flag) {
				flag = duplicateRageValidation(vrows);
			}
		}
	}
	// if (flag) {
	var addedTcIds = [];
	$.each(rigthSideElements, function(insturmentName, testCodeIds) {
		$.each(testCodeIds, function(index, testCodeId) {

			var fgg = checkDuplicate(testCodeId, $(
					"#" + testCodeId + "_tcOrder").val(), insturmentName);
			// debugger;
			if (fgg) {
				// addedTcIds.push("<input type='hidden' name='addedTcIds'
				// value='"+testCodeId+"'/>");
				addedTcIds.push(testCodeId);
			} else {
				flag = false;
			}
		});
	});
	// $("#addedTcIdsDiv").html(addedTcIds);
	// }

	if (flag) {
		// debugger;
		var profileIds = [];
		$.each(instrumentProfile, function(instrumentName, profileId) {
			if (profileId != -1) {
				// profileIds.push("<input type='hidden' name='profileIds'
				// class='form-control' value='"+profileId+"'/>");
				profileIds.push(profileId);
			}
		});
		// $("#profileIdsDiv").html(profileIds);
		// debugger;
		$("#groupRows").val(newRows);
		// comments this data move to study meta deta
		// $("#saveClinpathPerameterform").submit();
		saveObservationClinpathData(addedTcIds, profileIds);
	}

}
function saveObservationClinpathData(addedTcIds, profileIds) {
	var observationClinpathCreationDto = [];
	$.ajax({
		url : $("#mainUrl").val() + '/administration/getCsrfToken',
		type : 'GET',
		success : function(data) {
			observationClinpathCreationDto.push({
				name : data.parameterName,
				value : data.token
			});
			observationClinpathCreationDto.push({
				name : "studyAcclamatizationDatesId",
				value : $("#studyAcclamatizationDatesId").val()
			});
			$.each(profileIds, function(index, profileId) {
				observationClinpathCreationDto.push({
					name : "profileIds",
					value : profileId
				});
			});
			$.each(addedTcIds, function(index, tcId) {
				observationClinpathCreationDto.push({
					name : "addedTcIds",
					value : tcId
				});
				observationClinpathCreationDto.push({
					name : tcId + "_tcOrder",
					value : $("#" + tcId + "_tcOrder").val()
				});
			});

			$.ajax({
				url : $("#mainUrl").val()
						+ '/grouping/saveIntrumentAndPerameters',
				type : 'POST',
				data : observationClinpathCreationDto,
				success : function(e) {
					console.log(e.success)
					if (e.Success === 'true' || e.Success === true) {
						alert(e.Message);
						hideParameterConfiguration($(
								"#studyAcclamatizationDatesId").val(), $(
								"#studyAcclamatizationDatesId").val()
								+ "_clinpath_td")
					} else {
						alert(e.Message);
					}
				},
				error : function(er) {
					debugger;
				}
			});
		},
		error : function(er) {
			debugger;
		}
	});

}

function duplicateRageValidation(vrows) {
	var fg = true;
	var values = [];
	$.each(vrows, function(k, v) {
		if (v != 0) {
			// debugger;
			var from = parseInt($("#" + k + "_from").val());
			var to = parseInt($("#" + k + "_to").val());
			var ragefg = false;
			while (from <= to) {
				$.each(values, function(index, no) {
					if (no == from) {
						ragefg = true;
						return false;
					}
				});
				if (ragefg) {
					fg = false;
					$("#" + k + "_toMsg").html(
							"Invalied Range / Duplicate Range");
				}
				from++;
			}
		}
	});
	return fg;
}

function minMaxCompare(vrows) {
	var fg = true;
	$.each(vrows, function(k, v) {
		if (v != 0) {
			var from = parseInt($("#" + k + "_from").val());
			var to = parseInt($("#" + k + "_to").val());
			if (from > to) {
				$("#" + k + "_toMsg").html(
						"Value must greater than or equal From");
				fg = false;
			}
		}
	});
	return fg;
}
function duplicateCheck(vrows) {
	var fg = true;
	var ranges = [];
	$.each(vrows, function(k, v) {
		if (v != 0) {
			var duplicateFromFalg = false;
			var newvalue = $("#" + k + "_from").val();
			$.each(ranges, function(index, value) {
				if (newvalue == value) {
					duplicateFromFalg = true;
					return false;
				}
			});
			var duplicateToFalg = false;
			var newvalue = $("#" + k + "_to").val();
			$.each(ranges, function(index, value) {
				if (newvalue == value) {
					duplicateToFalg = true;
					return false;
				}
			});

			if (duplicateFromFalg) {
				$("#" + k + "_fromMsg").html("Duplicate Number");
				flag = false;
			} else
				ranges.push(newvalue);
			if (duplicateToFalg) {
				$("#" + k + "_toMsg").html("Duplicate Number");
				fg = false;
			} else
				ranges.push(newvalue);
		}
	});
	return fg;
}
