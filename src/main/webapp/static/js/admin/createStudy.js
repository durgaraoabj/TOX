$(document).ready(function(){
	$("#studyModal").modal('hide');
});
//function viewStudy(studyId){
//	var result = asynchronousAjaxCall(mainUrl+"/grouping/studyAndGroupingView/"+studyId);
//	if(result != 'undefined' && result != ''){
//		$("#studyData").html(result);
//		$('#studyModal').modal('show');
//	}else
//		alert("Unable to display Profile details");
//}

function acceptCheckBox(modelId){
	$('#'+modelId).modal('hide');
}
