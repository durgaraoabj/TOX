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
