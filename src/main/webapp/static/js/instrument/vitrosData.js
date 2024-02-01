function viewVitrosData() {
	debugger;
	$("#dataDiv").html("");
	var updateEleData = [];
	$.ajax({
		url : $("#mainUrl").val() + '/administration/getCsrfToken',
		type : 'GET',
		success : function(data) {
			debugger;
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
			$.ajax({
				url : $("#mainUrl").val() + '/sysmex/vitorsDataExportTable',
				type : 'POST',
				data : updateEleData,
				success : function(e) {
					debugger;
					console.log(e)
					$("#dataDiv").html(e)
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

function exportData() {
	$("#dataViewId").submit();
}

function vistroDataExportPdf(studyId, animalno) {
	$("#pdfStudyId").val(studyId);
	$("#pdfanimalId").val(animalno);
	$("#dataViewpdfId").submit();
}

function viewRepetedValuesUpdtePage(animalNumber, columnIndex, studyId,
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
										+ '/sysmex/vitrosResultSelection',
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
													+ e.result[index].result
													+ "&nbsp;";
										else
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e.result[index].id
													+ "\">"
													+ e.result[index].result
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
								+ '/sysmex/vitrosResultSelectionSave',
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