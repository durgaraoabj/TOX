$(document).ready(function() {
	$('#dataModal').modal('hide');
});
function dispalyeAnimala(value) {
	// $("#dataDiv").html();
	// if(value != -1){
	//		
	// }
}

function selectResultPage(rowId, animalNo, studyId, studyNo, testName, tdId, animalId) {
	// alert(animalNo + " " + studyId);
	debugger;
	$("#dataSelectionDiv").html("");
	var eleData = [];
	$
			.ajax({
				url : $("#mainUrl").val() + '/administration/getCsrfToken',
				type : 'GET',
				success : function(data) {
					eleData.push({
						name : data.parameterName,
						value : data.token
					});
					eleData.push({
						name : "studyId",
						value : studyId
					});
					eleData.push({
						name : "animalNo",
						value : animalNo
					});
					eleData.push({
						name : "testName",
						value : testName
					});
					eleData.push({
						name : "animalId",
						value : animalId
					});
					
					$
							.ajax({
								url : $("#mainUrl").val()
										+ '/sysmex/stagoResultSelection',
								type : 'POST',
								data : eleData,
								success : function(e) {
									debugger;									
									console.log(e)
									var v = "<table class=\"table table-bordered table-striped\"><tr><th>"
											+ studyNo
											+ "</th><th>"
											+ animalNo
											+ "</th></tr>";
									v += "<tr><td>Select Result : </td><td>"
									for (var index = 0; index < e.length; index++) {
										if (e[index].selectedResult)
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e[index].id
													+ "\" checked=\"checked\">"
													+ e[index].testResult
													+ "&nbsp;";
										else
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e[index].id
													+ "\">"
													+ e[index].testResult
													+ "&nbsp;";
									}
									v += "</td></tr>";
									v += "<tr><td colspan=\"2\"><input type=\"button\" value=\"Save\" onclick=\"stagoResultSelectionSave('"
											+ studyId
											+ "', '"
											+ animalNo
											+ "', '"
											+ testName
											+ "', '"
											+ rowId + "', '"+animalId+"', '"+tdId+"')\"/></td></table>";
									$("#dataSelectionDiv").html(v);
									
									$('#dataModal').modal('show');
								},
								error : function(er) {

								}
							});
				},
				error : function(er) {

				}
			});

}

function stagoResultSelectionSave(studyId, animalNo, testName, rowId, animalId, tdId) {
	//	
	var finalResult = $('input[name="finalResult"]:checked').val();
	$("#dataSelectionDiv").html("");
	var eleData = [];
	$.ajax({
		url : $("#mainUrl").val() + '/administration/getCsrfToken',
		type : 'GET',
		success : function(data) {
			eleData.push({
				name : data.parameterName,
				value : data.token
			});
			eleData.push({
				name : "studyId",
				value : studyId
			});
			eleData.push({
				name : "animalNo",
				value : animalNo
			});
			eleData.push({
				name : "testName",
				value : testName
			});
			eleData.push({
				name : "finalResultId",
				value : finalResult
			});
			eleData.push({
				name : "animalId",
				value : animalId
			});
			$.ajax({
				url : $("#mainUrl").val() + '/sysmex/stagoResultSelectionSave',
				type : 'POST',
				data : eleData,
				success : function(e) {
					
					console.log(e)
//					var tdId = "data_" + studyId + "_" + animalNo;
//					console.log(e.value)
//					console.log($("#" + rowId).html());
					debugger;
					$("#"+tdId).html(e.value);
//					$("#" + rowId + "td").html(e.value);
					$("#dataSelectionDiv").html("");
					$('#dataModal').modal('hide');
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

function acceptCheckBox() {
	$('#dataModal').modal('hide');
}

function exportData() {
	$("#dataViewId").submit();
}
function checSysmexViewInputValidation(){
//	debugger;
	var stdflag = true;
	$("#studyNumbersMsg").html("");
	$("#stDateMsg").html("");
	if($("#studyNumbers").val() == -1){
		stdflag = false;
	}
	var dateflag = true;
	$("stDateMsg").html("");
	if($("#stDate").val() == ''){
		dateflag = false;
	}
	
	if(stdflag || dateflag){
		return true;
	}else{
		$("#studyNumbersMsg").html("Required Field");
		$("#stDateMsg").html("Required Field");
		return false;
	}
}
function viewSysmexData() {
	
	$("#dataDiv").html("");
	if(checSysmexViewInputValidation()){
		var updateEleData = [];
		$.ajax({
			url : $("#mainUrl").val() + '/administration/getCsrfToken',
			type : 'GET',
			success : function(data) {
				//			
				updateEleData.push({
					name : data.parameterName,
					value : data.token
				});
				updateEleData.push({
					name : "studyNumbers",
					value : $("#studyNumbers").val()
				});
				updateEleData.push({
					name : "stDate",
					value : $("#stDate").val()
				});
				updateEleData.push({
					name : "sampleType",
					value : $("#sampleType").val()
				});
				$.ajax({
					url : $("#mainUrl").val() + '/sysmex/sysmexDataExportTable',
					type : 'POST',
					data : updateEleData,
					success : function(e) {
						//					
						console.log(e)
						$("#dataDiv").html(e);
					},
					error : function(er) {

					}
				});
			},
			error : function(er) {

			}
		});
	}
	
	
}

function viewStagoData() {
	// 
	$("#dataDiv").html("");
	var updateEleData = [];
	$.ajax({
		url : $("#mainUrl").val() + '/administration/getCsrfToken',
		type : 'GET',
		success : function(data) {
			//			
			updateEleData.push({
				name : data.parameterName,
				value : data.token
			});
			updateEleData.push({
				name : "studyNumbers",
				value : $("#studyNumbers").val()
			});
			updateEleData.push({
				name : "stDate",
				value : $("#stDate").val()
			});
			updateEleData.push({
				name : "sampleType",
				value : $("#sampleType").val()
			});
			$.ajax({
				url : $("#mainUrl").val() + '/sysmex/stagoDataExportTable',
				type : 'POST',
				data : updateEleData,
				success : function(e) {
					//					
					console.log(e)
					$("#dataDiv").html(e)
				},
				error : function(er) {

				}
			});
		},
		error : function(er) {

		}
	});
}

function diaplaySysmexReport(studyId, animalNum) {
	$("#sysmexStudyId").val(studyId);
	$("#sysmexAnimalNum").val(animalNum);
	$("#dataPdfId").submit();
}
function diaplayStagoReport(studyId, animalId) {
	$("#stagoStudyId").val($("#studyNumbers").val());
	$("#stagoAnimalId").val(animalId);
	$("#multipleAnimals").val()
//	debugger;
	$("#stagoDataPdfId").submit();
	
}
function selectAllStagoAnimals() {
	debugger;
	if ($("#allStagoAnimals").is(":checked")) {
		$("input:checkbox[name='stagoAnimals']").each(function() {
			$(this).prop('checked', true);
		});
	} else {
		$("input:checkbox[name='stagoAnimals']:checked").each(function() {
			$(this).prop('checked', false);
		});
	}
}
function selectStagoAnimals(id){
	$("#allStagoAnimals").prop('checked', false);
	if ($("#"+id).is(":checked")) {
		$("#"+id).prop('checked', false);
	}else
		$("#"+id).prop('checked', true);
}
function getAnimalPdf(){
//	debugger;
	$("#stagoAnimalId").val(0)
	$("#pdfMsg").html("");
	var checkboxVaues = [];
	var flag = false;
	$("input:checkbox[name='stagoAnimals']:checked").each(function() {
		checkboxVaues.push($(this).val());
		flag = true;
	});
	if(checkboxVaues.length > 0){
			$("#stagoStudyId").val($("#studyNumbers").val());
			$("#multipleAnimals").val(checkboxVaues)
			$("#stagoDataPdfId").submit();
	}else{
		$("#pdfMsg").html("Select atleat one Animal");
	}
}


function viewSysmexRepetedValuesUpdtePage(animalNumber, columnIndex, studyId,
		resultCount) {
	// alert(animalNo + " " + studyId);
	debugger;
	$("#dataSelectionDiv").html("");
	var eleData = [];
	$
			.ajax({
				url : $("#mainUrl").val() + '/administration/getCsrfToken',
				type : 'GET',
				success : function(data) {
					eleData.push({
						name : data.parameterName,
						value : data.token
					});
					eleData.push({
						name : "studyId",
						value : studyId
					});
					eleData.push({
						name : "animalNumber",
						value : animalNumber
					});
					eleData.push({
						name : "columnIndex",
						value : columnIndex
					});
					$
							.ajax({
								url : $("#mainUrl").val()
										+ '/sysmex/sysmexResultSelection',
								type : 'POST',
								data : eleData,
								success : function(e) {
									console.log(e)
									var v = "<table class=\"table table-bordered table-striped\"><tr><th>"
											+ e.studyNumber
											+ "</th><th>"
											+ animalNumber + "</th></tr>";
									v += "<tr><td>Select Result : </td><td>"
									for (var index = 0; index < e.result.length; index++) {
										if (e.result[index].finalize)
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e.result[index].id
													+ "\" checked=\"checked\">"
													+ e.result[index].value
													+ "&nbsp;";
										else
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e.result[index].id
													+ "\">"
													+ e.result[index].value
													+ "&nbsp;";
									}
									v += "</td></tr>";
									v += "<tr><td colspan=\"2\"><input type=\"button\" value=\"Save\" onclick=\"viewRepetedValuesUpdte('"
											+ studyId
											+ "', '"
											+ animalNumber
											+ "', '"
											+ e.testName
											+ "', '"
											+ columnIndex
											+ "', '"
											+ resultCount
											+ "')\"/></td></table>";
									$("#dataSelectionDiv").html(v);
									$('#dataModal').modal('show');
								},
								error : function(er) {

								}
							});
				},
				error : function(er) {

				}
			});
}
function viewRepetedValuesUpdte(studyId, animalNo, testName, columnIndex, resultCount) {
	//	
	var finalResult = $('input[name="finalResult"]:checked').val();
	$("#dataSelectionDiv").html("");
	var eleData = [];
	$.ajax({
		url : $("#mainUrl").val() + '/administration/getCsrfToken',
		type : 'GET',
		success : function(data) {
			eleData.push({
				name : data.parameterName,
				value : data.token
			});
			eleData.push({
				name : "studyId",
				value : studyId
			});
			eleData.push({
				name : "animalNo",
				value : animalNo
			});
			eleData.push({
				name : "testName",
				value : testName
			});
			eleData.push({
				name : "columnIndex",
				value : columnIndex
			});
			eleData.push({
				name : "finalResultId",
				value : finalResult
			});
			$
					.ajax({
						url : $("#mainUrl").val()
								+ '/sysmex/sysmexResultSelectionSave',
						type : 'POST',
						data : eleData,
						success : function(e) {
							debugger;
							console.log(e)
							console.log(e.value)
							$("#" + resultCount).html(e.value);
							$("#dataSelectionDiv").html("");
							$('#dataModal').modal('hide');
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

function acceptCheckBox() {
	$('#dataModal').modal('hide');
}
