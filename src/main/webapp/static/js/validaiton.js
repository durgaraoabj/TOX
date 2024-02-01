function checkRequired(id, msgId, compareValue) {
	debugger;
	$("#" + msgId).html("");
	id = id.trim();
	if ($("#" + id).val() == compareValue) {
		$("#" + msgId).html("Required Field");
		return true;
	} else
		return false;
}

function checkCheckboxRequired(name, msgId) {
	$("#" + msgId).html("");
//	var flag = false;
//	$('input[name="'+name+'"]').each(function(){
//	      if($(this).is(':checked')){
//	        flag = true;
//	      }
//	    });
	
	if ($('input[name="' + name + '"]').is(':checked')) {
		return false;
	}else{
		$("#" + msgId).html("Required Field");
		return true;
		
	}
	
		
}

function checkOrUnchekAll(allChecboxName, checkboxName) {
	if ($('input[name="' + allChecboxName + '"]').is(':checked')) {
		$('input[name="' + checkboxName + '"]').prop('checked', true);
	} else {
		$('input[name="' + checkboxName + '"]').prop('checked', false);
	}
}

function fileValidatation(id, msgId, allowedFileType) {
	$("#"+msgId).html("");
	var fileInput = $('#' + id);
	var filePath = fileInput.val();
	var allowedExtensions = '';
	if (allowedFileType == 'xml')
		allowedExtensions = /(\.xml)$/i;
	else if (allowedFileType == 'xlsx')
		allowedExtensions = /(\.xlsx)$/i;
	else if (allowedFileType == 'pdf')
		allowedExtensions = /(\.pdf)$/i;
	else if (allowedFileType == 'png')
		allowedExtensions = /(\.png)$/i;
	else if (allowedFileType == 'jpeg' || allowedFileType == 'JPEG')
		allowedExtensions = /(\.jpeg)$/i;
	else if (allowedFileType == 'jpg' || allowedFileType == 'JPG')
		allowedExtensions = /(\.jpg)$/i;
	else if (allowedFileType == 'gif')
		allowedExtensions = /(\.gif)$/i;

	// Check if a file has been selected
	if (filePath != '') {
		// Check if the file has an allowed extension
		if (!allowedExtensions.exec(filePath)) {
			$("#"+msgId).html('Please upload file having extensions .'+allowedFileType+' only.');
			fileInput.val('');
			return true;
		}
	}
	return false;
}