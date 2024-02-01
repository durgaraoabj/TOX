var rows = {};
var count = 0;
function addRow() {
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
	var row = "<tr id=\"" + flag + "Tr\"><td/><td><input type=\"text\" name=\""
			+ flag + "_newunit\" class='form-control' id=\"" + flag
			+ "_newunit\" onchange=\"testCodeUnits(this.value, '" + flag
			+ "_newunit', '" + flag
			+ "_newunitMsg')\" /><font color='red' id=\"" + flag
			+ "_newunitMsg\"></font></td>";
	row += "<td><input type=\"text\" class='form-control' name=\"" + flag
			+ "_newdispalyUnit\" id=\"" + flag + "_newdispalyUnit\"/></td>";
	row += "<td><input type=\"button\" class='btn btn-gre' value=\"Remove\" onclick=\"removeRow('"
			+ flag + "')\"/></td>";
	// debugger;
	$("#unitsTable").append(row);
}
function removeRow(trId) {
	rows[trId] = 0;
	$("#" + trId + "Tr").remove();
}

function testCodeUnits(value, id, messagId) {
//	debugger;
	var newunits = [];
	value = value.trim();
	if (checkMandatory(value, id, messagId)) {
		$.each(rows, function(k, v) {
			if (v != 0 && ((k + "_newunit") != id)) {
				newunits.push($("#" + k + "_newunit").val());
			}
		});
//		debugger;
		var flag = true;
		$.each(newunits, function(k, v) {
			if (value == v) {
				$("#" + messagId).html("Duplicate Unit");
				$("#" + id).val("");
				flag = false;
			}
		});
		if (flag) {
			$("input:checkbox[name='unitId']:checked").each(function() {
				console.log($("#" + $(this).val()+"_tcUnit").val());
				console.log($(this).val()+"_tcUnit" + "          " + id);
				debugger;
				if (($(this).val()+"_tcUnit" != id) && value == $("#" + $(this).val()+"_tcUnit").val()) {
					$("#" + messagId).html("Duplicate Unit");
					$("#" + id).val("");
					flag = false;
				}
			});
		}

//		if (flag) {
//			var updateEleData = [];
//			$.ajax({
//				url : $("#mainUrl").val() + '/administration/getCsrfToken',
//				type : 'GET',
//				success : function(data) {
//					//			
//					updateEleData.push({
//						name : data.parameterName,
//						value : data.token
//					});
//					updateEleData.push({
//						name : "unit",
//						value : value
//					});
//					$.ajax({
//						url : $("#mainUrl").val() + '/grouping/checkUintName',
//						type : 'POST',
//						data : updateEleData,
//						success : function(e) {
//							console.log(e)
//							if (e == 'Valied') {
//								$("#" + messagId).html("Duplicate Unit");
//								$("#" + id).val("");
//							}
//						},
//						error : function(er) {
//							
//						}
//					});
//				},
//				error : function(er) {
//
//				}
//			});
//		}
	}

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
	var flag = true;
	var count = 0;
	$("input:checkbox[name='unitId']:checked").each(
			function() {
				if (!checkMandatory($("#" + $(this).val()).val(),
						$(this).val(), $(this).val() + "_Msg")) {
					flag = false;
				}
				count++;
			});
	var newRows = [];
	$.each(rows, function(k, v) {
		if (v != 0) {
			if (!checkMandatory($("#" + k + "_newunit").val(), k + "_newunit",
					k + "_newunitMsg")) {
				flag = false;
			}
			count++;
			newRows.push(k);
		}
	});
	if (flag) {
		$("#newunits").val(newRows);
		$("#saveunitsform").submit();
	}
}