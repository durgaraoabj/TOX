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
	// debugger;
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
			element += "<td><input type=\"text\" name=\""
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
function saveData() {
	debugger;
	var flag = true;
	var addedTcIds = [];
	$.each(rigthSideElements, function(insturmentName, testCodeIds) {
		$.each(testCodeIds, function(index, testCodeId) {

			var fgg = checkDuplicate(testCodeId, $(
					"#" + testCodeId + "_tcOrder").val(), insturmentName);
			// debugger;
			if (fgg) {
				addedTcIds
						.push("<input type='hidden' name='addedTcIds' value='"
								+ testCodeId + "'/>");
			} else {
				flag = false;
			}
		});
	});
	$("#addedTcIdsDiv").html(addedTcIds);

	if (flag) {
		// debugger;
		var profileIds = [];
		$.each(instrumentProfile, function(instrumentName, profileId) {
			if (profileId != -1) {
				profileIds
						.push("<input type='hidden' name='profileIds' value='"
								+ profileId + "'/>");
			}
		});
		$("#profileIdsDiv").html(profileIds);
		debugger;
		$("#groupRows").val(newRows);

		$("#savestudyform").submit();
		// alert("sumited");
	}

}
