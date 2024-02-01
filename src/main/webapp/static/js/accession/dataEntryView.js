var dataView;
function updateValue(id, updateStatus, activityType) {

	$("#elementUpdateData").html("");
	// eleValue_${eleData.value.id}
	var jsonData = asynchronousAjaxCall(mainUrl
			+ "/studyExecution/obsercatoinActivityUpdateView/" + id + "/"
			+ activityType);
	if (activityType == 'acesstion') {
		jsonData = asynchronousAjaxCall(mainUrl
				+ "/accession/accessionActivityUpdateView/" + id + "/"
				+ activityType);
	}
	if (jsonData != null && jsonData != "undefined" && jsonData != "") {
		dataView = jsonData;
		console.log(dataView);
		var content = "<table class=\"table table-bordered table-striped\">"
				+ "<tr><th>Study : </th><th>" + dataView.studyNumber
				+ "</th></tr>" + "<tr><th>Animal Number : </th><th>"
				+ dataView.tempararayAnimalNumber + "</th></tr>"
				+ "<tr><th>Activity : </th><th>" + dataView.activityName
				+ "</th></tr>" + "<tr><th>Field : </th><th>";
		if (dataView.element.leftDesc != null) {
			content += dataView.element.leftDesc;
		} else if (dataView.element.totalDesc != null) {
			content += dataView.element.totalDesc;
		} else if (dataView.element.rigtDesc != null) {
			content += dataView.element.rigtDesc;
		} else if (dataView.element.bottemDesc != null) {
			content += dataView.element.bottemDesc;
		}

		content += "</th></tr>";
		content += "<tr><th>Current Value : </th><th>" + dataView.value
				+ "</th>" + "<tr><th>Entry Type : </th><th>"
				+ dataView.entryType + "</th>" + "<tr><th>Done By : </th><th>"
				+ dataView.createdBy + "</th>"
				+ "<tr><th>Done Date : </th><th>" + dataView.createdOn;

		if (updateStatus) {
			content += "</th>"
					+ "<tr><th>New Value :</th><th id=\"newEleValueTd\">";
			if (dataView.element.type == 'text') {
				if (dataView.element.dataType = 'Number')
					content += "<input type=\"text\" id=\"newEleValue\" value=\"\" onkeypress=\"return event.charCode >= 48 && event.charCode <= 57\"/>";
				else
					content += "<input type=\"text\" id=\"newEleValue\" value=\"\" />";
			} else if (dataView.element.type == 'textArea') {
				content += +"<textarea id=\"newEleValue\" value=\"\" ></textarea>";
			} else if (dataView.element.type == 'select'
					|| dataView.element.type == 'selectTable') {
				content += "<select id=\"newEleValue\"><option value=\"-1\">--Select--</option>";
				for (var index = 0; index < dataView.element.elementValues.length; index++) {
					content += "<option value=\""
							+ dataView.element.elementValues[index] + "\">"
							+ dataView.element.elementValues[index]
							+ "</option>";
				}
				content += "</select>";
			} else if (dataView.element.type == 'radio') {
				for (var index = 0; index < dataView.element.elementValues.length; index++) {
					content += "<input type=\"readio\" name=\"newEleValue\" value=\""
							+ dataView.element.elementValues[index]
							+ "\" >"
							+ dataView.element.elementValues[index];
				}
			} else if (dataView.element.type == 'checkBox') {
				for (var index = 0; index < dataView.element.elementValues.length; index++) {
					content += "<input type=\"checkbox\" name=\"newEleValue\" value=\""
							+ dataView.element.elementValues[index]
							+ "\" >"
							+ dataView.element.elementValues[index];
				}
			} else if (dataView.element.type == 'date') {
				content += "<input type=\"text\" id=\"newValueDateTime\" value=\"\" />";
			} else if (dataView.element.type == 'dateAndTime') {
				content += "<input type=\"text\" id=\"newValueDate\" value=\"\" />";
			} else if (dataView.element.type == 'date') {
				content += "<input type=\"text\" id=\"newEleValue\" value=\"\" />";
			} else if (dataView.element.type == 'dateAndTime') {
				content += "<textarea id=\"newValueDate\" value=\"\" ></textarea>";
			} else if (dataView.element.type == 'instrumentData') {
				var instrumentData = "<table class=\"table table-bordered table-striped\">";
				instrumentData += "<tr><th/><th>Weight</th><th>Done By</th><th>Date</th>";
				for (var index = 0; index < dataView.element.instumentDat.length; index++) {
					instrumentData += "<tr><td><input type=\"radio\" name=\"newEleValue\"  value=\""
							+ dataView.element.instumentDat[index].weight
							+ "\" ></td>";
					instrumentData += "<td>"
							+ dataView.element.instumentDat[index].weight
							+ "</td><td>"
							+ dataView.element.instumentDat[index].userName
							+ "</td><td>"
							+ dataView.element.instumentDat[index].creationDate
							+ "</td></tr>";
				}
				instrumentData += "</table>";
				content += instrumentData;
			}
			content += "<font color='red' id='newEleValueMsg'></font>";
			content += "</tr>"
					+ "<tr><td>Comment :</td><td><input type=\"text\" id=\"newEleValueComment\" value=\"\"/>"
					+ "<font color='red' id='newEleValueCommentMsg'></font></td></tr>"
					+ "<tr><td/><td><input type=\"button\" value=\"Update\" onclick=\"updateElementValue('"
					+ id + "', '" + dataView.element.type + "', '"
					+ activityType + "')\"/></td>";

		}
		content += "</table>";
		if (dataView.audit.length > 0) {
			content += "<table class=\"table table-bordered table-striped\"><tr><th colspan=\"6\">Data Log</th></tr>";
			content += "<tr>";
			if (updateStatus) {
				content += "<th/>";
			}
			content += "<th>Old Value</th><th>New Value</th>"
					+ "<th>Done By</th><th>Done Date</th>"
					+ "<th>Comment</th><th>New/Update</th></tr>";
			for (var index = 0; index < dataView.audit.length; index++) {
				content += "<tr>";
				if (updateStatus) {
					if (dataView.audit[index].allowToSelect) {
						content += "<td><input type=\"checkbox\" name=\"auditValue\" id=\"auditValue"
								+ dataView.audit[index].id
								+ "\" value=\""
								+ dataView.audit[index].elementValue
								+ "\" onchange=\"checkWithOldValue('"
								+ dataView.audit[index].id + "')\"></td>";
					} else {
						content += "<td><input type=\"checkbox\" name=\"auditValue\" id=\"auditValue\" disabled=\"disabled"
								+ dataView.audit[index].id
								+ "\" value=\""
								+ dataView.audit[index].elementValue
								+ "\" onchange=\"checkWithOldValue('"
								+ dataView.audit[index].id + "')\"></td>";
					}
				}
				content += "<td>" + dataView.audit[index].elementOldValue
						+ "</td>" + "<td>" + dataView.audit[index].elementValue
						+ "</td>" + "<td>"
						+ dataView.audit[index].loginUserName + "</td>"
						+ "<td>";// + dataView.audit[index].createdOn
				if (dataView.audit[index].createdOn != null) {
					content += dataView.audit[index].createdOn;
				}
				content += "</td>" + "<td>" + dataView.audit[index].userComment
						+ "</td>" + "<td>"
						+ dataView.audit[index].dataEntryType + "</td></tr>";
			}
			content += "</table>";
		}
		$("#elementUpdateData").html(content)
		$('#updateDataModal').modal('show');
	}
}

var newEleValueTd = "";
function checkWithOldValue(auditId) {
	// debugger;
	var flag = false;
	$.each($("input[name='auditValue']:checked"),
			function() {
				var $box = $(this);
				console.log(auditId)
				console.log($box.prop('id'))
				if ($box.is(":checked")
						&& $box.prop('id') == ("auditValue" + auditId)) {
					$box.prop("checked", true);
					flag = true;
				} else {
					$box.prop("checked", false);
				}
			});
	if (flag) {
		if (newEleValueTd == "")
			newEleValueTd = $("#newEleValueTd").html();
		$("#newEleValueTd").html("");
	} else {
		$("#newEleValueTd").html(newEleValueTd);
		newEleValueTd = "";
	}
}

function updateElementValue(id, elementType, activityType) {
	if (checkElementUpdateValidation(id, elementType)) {
		var eleval = "";
		var eleValues = [];
		if (newEleValueTd == "") {
			if (elementType == 'text' || elementType == 'textArea'
					|| elementType == 'select' || elementType == 'selectTable') {
				eleval = $("#newEleValue").val();
			} else if (elementType == 'checkBox') {
				$.each($("input[name='newEleValue']:checked"), function() {
					eleValue.push($(this).val());
				});
			} else if (elementType == 'radio' || elementType == 'instrumentData') {
				eleval = $("input[name='newEleValue']:checked").val();
			} else if (elementType == 'date') {
				eleval = $("#newValueDate").val();
			} else {
				eleval = $("#newValueDate").val();
			}
		} else {
			eleval = $("input[name='auditValue']:checked").val();
		}

		var updateEleData = [];
		var createdUrl = $("#mainUrl").val()
				+ '/studyExecution/observationActivityElementUpdate';
		if (activityType == 'acesstion') {
			createdUrl = $("#mainUrl").val()
					+ '/accession/accessionActivityElementUpdate';
		}
		$.ajax({
			url : $("#mainUrl").val() + '/administration/getCsrfToken',
			type : 'GET',
			success : function(data) {
				updateEleData.push({
					name : data.parameterName,
					value : data.token
				});
				updateEleData.push({
					name : "eleId",
					value : dataView.studyAccessionCrfSectionElementDataId
				});
				updateEleData.push({
					name : "newValue",
					value : eleval
				});
				updateEleData.push({
					name : "newValues",
					value : eleValues
				});
				updateEleData.push({
					name : "eleType",
					value : elementType
				});
				updateEleData.push({
					name : "comment",
					value : $("#newEleValueComment").val()
				});
				$.ajax({
					url : createdUrl,
					type : 'POST',
					data : updateEleData,
					success : function(e) {
						debugger;
						console.log(e.success)
						if (e.Success === 'true' || e.Success === true) {
							$("#eleValue_" + id).html(e.vlaue);
							$(".alert alert-success").html(e.Message);
						} else {
							$(".alert alert-danger").html(e.Message)
						}
						acceptCheckBox('updateDataModal');
						newEleValueTd = "";
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

function checkElementUpdateValidation(id, elementType) {
	$("#newEleValueMsg").html("");
	$("#newEleValueCommentMsg").html("");
	var flag = true;
	debugger;
	if (newEleValueTd == "") {
		if (elementType == 'text' || elementType == 'textArea'
				|| elementType == 'select' || elementType == 'selectTable'
				|| elementType == 'date') {
			if ($("#newEleValue").val().trim() == '') {
				$("#newEleValueMsg").html("Required Field");
				flag = false;
			}
		} else if (elementType == 'checkBox') {
			var count = 0;
			$.each($("input[name='newEleValue']:checked"), function() {
				count++;
			});
			if (count == 0) {
				$("#newEleValueMsg").html("Required Field");
				flag = false;
			}
		} else if (elementType == 'radio' || elementType == 'instrumentData') {
			if (!$("input:radio[name='newEleValue']").is(":checked")) {
				$("#newEleValueMsg").html("Required Field");
				flag = false;
			}
		}
	}

	if ($("#newEleValueComment").val().trim() == '') {
		$("#newEleValueCommentMsg").html("Required Field");
		flag = false;
	}
	return flag;
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
$('#approveBtn').click(function() {
	approveOrRject('Send To Review');
});

$('#reivewBtn').click(function() {
	approveOrRject('Approve');
});
$('#rejectBtn').click(function() {
	approveOrRject('Reject');
});
function approveOrRject(reivewType) {
	debugger;
	if (reivewType == 'Send To Review')
		$("#reviewType").val("Send To Review");
	else if (reivewType == 'Approve')
		$("#reviewType").val("Approve");
	else
		$("#reviewType").val("Reject");
	$('#chkMsg').html("");
	var chkArr = [];
	if (animalIds.length > 0) {
		for (var i = 0; i < animalIds.length; i++) {
			if ($("#chkBox_" + animalIds[i]).prop("checked")) {
				chkArr.push(animalIds[i]);
			}
		}
	}
	if (chkArr.length > 0)
		$('#chkckedIds').val(chkArr);
	var chkVal = $('#chkckedIds').val();
	debugger;
	if (chkVal != null && chkVal != "" && chkVal != "undefined") {
		if (reivewType == 'Send To Review') {
			$('#saveApprData').submit();
		} else
			signacherPopup();

	} else {
		$('#chkMsg').html("Please Check At Least One CheckBox.");
	}
}
function signacherPopup() {
	$('#signacherModal').modal('show');
}
function acceptCheckBox(id) {
	$('#' + id).modal('hide');
}