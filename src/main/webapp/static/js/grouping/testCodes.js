var unitselement = "";
var selectFlag = false;
$.each(unitdata, function(id, unit) {
	if (selectFlag)
		unitselement += "<option value=\"" + id + "\">" + unit + "</option>";
	else {
		selectFlag = true;
		unitselement += "<option value=\"" + id + "\" selected>" + unit
				+ "</option>";
	}
});

var rows = {};
var count = 0;
function addRow() {
	debugger;
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
	var row = "<tr id=\"" + flag + "Tr\" style='text-align:center;'><td/><td style='width:190px;'><input type=\"text\" name=\""
			+ flag + "_newTestCode\" id=\"" + flag
			+ "_newTestCode\" class='form-control'  onchange=\"testCodeUnits(this.value, '" + flag
			+ "_newTestCode', '" + flag
			+ "_newTestCodeMsg')\" /><font color='red' id=\"" + flag
			+ "_newTestCodeMsg\"></font></td>";
	row += "<td style='width:190px;'><input type=\"text\" class='form-control'  name=\"" + flag
			+ "_newDispalyTestCode\" id=\"" + flag
			+ "_newDispalyTestCode\"/><font color='red' id=\"" + flag
			+ "_newDispalyTestCodeMsg\"></font></td>";
	row += "<td style='width:190px;'><input type=\"text\" class='form-control'  name=\""
			+ flag
			+ "_newOrder\" "
			+ " id=\""
			+ flag
			+ "_newOrder\" "
			+ " onchange=\"checkDuplicate('"
			+ flag
			+ "_newOrder', this.value)\" onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" id=\""
			+ flag + "_newOrder\"/><font color='red' id=\"" + flag
			+ "_newOrderMsg\"></font></td>";
	row += "<td style='width:190px;'><select id=\"" + flag + "_newType\" class='form-control'  name=\"" + flag
			+ "_newType\" > " + unitselement + "</select>"
			+ "<font color='red' id=\"" + flag + "_newTypeMsg\"></font></td>";
	row += "<td><input type=\"button\" value=\"Remove\" class='btn btn-gre' onclick=\"removeRow('"
			+ flag + "')\"/></td>";
	// debugger;
	tecodeOrders["'"+flag+"'_order'"] = '';
	$("#tcTable").append(row);
}
function removeRow(trId) {
	rows[trId] = 0;
	$("#" + trId + "Tr").remove();
}

function testCodeUnits(value, id, messagId) {
	// debugger;
	var newunits = [];
	value = value.trim();
	if (checkMandatory(value, id, messagId)) {
		$.each(rows, function(k, v) {
			if (v != 0 && ((k + "_newTestCode") != id)) {
				newunits.push($("#" + k + "_newTestCode").val());
			}
		});
		// debugger;
		var flag = true;
		$.each(newunits, function(k, v) {
			if (value == v) {
				$("#" + messagId).html("Duplicate Parameter");
				$("#" + id).val("");
				flag = false;
			}
		});
		if (flag) {
			$("input:checkbox[name='testCodeIds']:checked")
					.each(
							function() {
								console
										.log($(
												"#" + $(this).val()
														+ "_testCode").val());
								console.log($(this).val() + "_testCode"
										+ "          " + id);
								debugger;
								if (($(this).val() + "_testCode" != id)
										&& value == $(
												"#" + $(this).val()
														+ "_testCode").val()) {
									$("#" + messagId).html(
											"Duplicate Parameter");
									$("#" + id).val("");
									flag = false;
								}
							});
		}
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
	$("input:checkbox[name='testCodeIds']:checked").each(
			function() {
				if (!checkMandatory($("#" + $(this).val() + "_testCode").val(),
						$(this).val() + "_testCode", $(this).val()
								+ "_testCodeMsg")) {
					flag = false;
				}
				if (!checkMandatory($("#" + $(this).val() + "_dispalyTestCode")
						.val(), $(this).val() + "_dispalyTestCode", $(this)
						.val()
						+ "_dispalyTestCodeMsg")) {
					flag = false;
				}
				if (!checkMandatory($("#" + $(this).val() + "_order").val(), $(
						this).val()
						+ "_order", $(this).val() + "_orderMsg")) {
					flag = false;
				}
				// else{
				// flag = checkDuplicate($(this).id(), $(this).val());
				// }
				count++;
			});
	var newRows = [];
	$.each(rows, function(k, v) {
		if (v != 0) {
			if (!checkMandatory($("#" + k + "_newTestCode").val(), k
					+ "_newTestCode", k + "_newTestCodeMsg")) {
				flag = false;
			}
			if (!checkMandatory($("#" + k + "_newDispalyTestCode").val(), k
					+ "_newDispalyTestCode", k + "_newDispalyTestCodeMsg")) {
				flag = false;
			}
			if (!checkMandatory($("#" + k + "_newOrder").val(),
					k + "_newOrder", k + "_newOrderMsg")) {
				flag = false;
			}
			// else{
			// flag = checkDuplicate($(this).id(), $(this).val());
			// }
			count++;
			newRows.push(k);
		}
	});

	if (flag) {
		$("#newtcrows").val(newRows);
		// alert("no errors")
		$("#savetcform").submit();
	}
}
function checkDuplicate(id, value) {
	// debugger;
	$("#" + id + "Msg").html("");
	$.each(tecodeOrders, function(k, v) {
		if (id != k) {
			if (v == value) {
				$("#" + id + "Msg").html("Duplicate Order");
				$("#" + id).val("");
				return false;
			}
		}
	});
	tecodeOrders[id] = value;
}

function displayTestCodes(value) {
	// debugger;
	if (value != -1) {
		$("#instumentid").val(value);
		$("#gettcsform").submit();
	} else {
		$("#tbodyid").html("");
	}
}