$('#profiledataModal').modal('hide');
function testCodesProfileView(profileId) {
	var result = asynchronousAjaxCall(mainUrl
			+ "/grouping/testCodesProfileView/" + profileId);
	if (result != 'undefined' && result != '') {
		$("#profileData").html(result);
		$('#profiledataModal').modal('show');
	} else
		alert("Unable to display Profile details");
}
function changeProfileStatus(){
	debugger;
	var flag = false;
	$("input:checkbox[name='profileId']:checked").each(function(){
		flag = true;
		return false;
	});
	if(flag){
		$("#profileForm").submit();
	}else{
		alert("Please, select atleat one Standard Parameter List");
	}
	
}

function acceptCheckBox(id) {
	$('#' + id).modal('hide');
}
function instrumentChange(instumentId) {
	$("#instrumentMsg").html("");
	$("#profileNameMsg").html("");
	$("#testCodesTd").html("");
	if (instumentId != -1) {
		debugger;
		var result = asynchronousAjaxCall(mainUrl
				+ "/grouping/insturmentSelection/" + instumentId);
		if (result != '' || result != 'undefined') {
			$("#testCodesTd").html(result);
		}
	} else {
		$("#instrumentMsg").html("Required Field");
	}
}
function checkaProfileName(value) {
	if (checkMandatory($("#profileName").val(), "profileName", "profileNameMsg")) {
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
					url : $("#mainUrl").val() + '/grouping/checkProfileName',
					type : 'POST',
					data : studyCreationDto,
					success : function(e) {
						console.log(e.success)
						if (e.Success === 'true' || e.Success === true) {
							$('#profileNameMsg').html(
									value + " aleredy avilable.");
							$('#profileName').val("");
						} else {
							$('#profileNameMsg').html('');
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

function testCodeSelect(id) {
	// debugger;
	if (tcs[id] == 0) {
		tcs[id] = 1;
		$('#' + id + "Div").css('background-color', '#334FFF')
	} else {
		tcs[id] = 0;
		$('#' + id + "Div").css('background-color', '#FFFFFF')
	}
}

function moveTcsToRight(instumentName) {
	$("#rigthtbody").html("");
//	debugger;
	var tcIds = insturmentcs[instumentName];
	var resultTable = "";
	for (var i = 0; i < tcIds.length; i++) {
		if (tcs[tcIds[i]] == 1) {
			debugger;
			var evalue = "";
			if (rigthTestCodesOrder[tcIds[i]] != ''
					&& rigthTestCodesOrder[tcIds[i]] != tcOrder[tcIds[i]]) {
				evalue = rigthTestCodesOrder[tcIds[i]];
			} else {
				evalue = tcOrder[tcIds[i]];
			}
			var testCode = $("#" + tcIds[i] + "Div").html();
			var element = "<tr  id=\"" + tcIds[i] + "Div\">"
			element += "<td>" + tcName[tcIds[i]] + "</td>";
			element += "<td>" + tcOrder[tcIds[i]] + "</td>";
			element += "<td><input type=\"text\" name=\""
					+ tcIds[i]
					+ "_tcOrder\" id=\""
					+ tcIds[i]
					+ "_tcOrder\" onchange=\"checkvalidation('"
					+ tcIds[i]
					+ "')\" value=\""
					+ evalue
					+ "\" "
					+ " onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" ><font color=\"red\" id=\""
					+ tcIds[i] + "_tcOrderMsg\"  ></font></td></tr>";
			resultTable += element;
			// alert(element);
			onlyRigthElements[tcIds[i]] = evalue;
			$("#" + tcIds[i] + "Div").remove();
			rigthTestCodesOrder[tcIds[i]] = evalue;
		}
	}
	$("#" + instumentName + "_DivRightTable").append(resultTable)
}
function checkvalidation(id) {
	var tctempid = id;
	id = id+"_tcOrder";
	var value = $("#" + id).val();
	var flag = checkDuplicate(id);
	debugger;
	if (flag) {
		rigthTestCodesOrder[tctempid] = value;
		onlyRigthElements[tctempid] = value;
	} else {
		rigthTestCodesOrder[tctempid] = "";
		onlyRigthElements[tctempid] = "";
	}

}
function checkDuplicate(id) {
	debugger;
	var flag = true;
	var value = $("#" + id).val();
	$("#" + id + "Msg").html("");
	$.each(onlyRigthElements, function(k, v) {
		k = k+"_tcOrder";
		if (id != k) {
			if (v == value) {
				$("#" + id + "Msg").html("Duplicate Order");
				$("#" + id).val("");
				flag = false;
				return false;
			}
		}
	});
	return flag;
}
function checkMandatory(value, id, messageId) {
	debugger;
	$("#" + messageId).html("");
	if (value != '') {
		return true;
	} else {
		$("#" + messageId).html("Required Field");
		return false;
	}
}
function saveProfile() {
	
	var flag = true;
	$("#instrumentMsg").html("");
	if (!checkMandatory($("#profileName").val(), "profileName",
			"profileNameMsg")) {
		flag = false;
	}
	if ($("#instrument").val() == -1) {
		$("#instrumentMsg").html("Required Field");
		flag = false;
	}
	if (flag) {
		var addedTcIds = [];
		var tccount = 0;
		debugger;
		$.each(onlyRigthElements, function(k, v) {
			$("#" + k + "_tcOrderMsg").html("");
			debugger;
			if (v == ""  || v == 0) {
				flag = false;
				$("#" + k + "_tcOrderMsg").html("Required Field");
			}else{
				if(checkDuplicate(k+"_tcOrder")){
					tccount++;
					addedTcIds.push(k);					
				}else{
					flag = false;
				}
			}
			$("#addedTcIds").val(addedTcIds);
		});
		if (tccount == 0) {
			alert("Please, Select at lest one Test Code");
			flag = false;
		}
	}

	if (flag) {
//		alert("ok")
		$("#saveform").submit();
	}

}