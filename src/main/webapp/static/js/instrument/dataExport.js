$(document).ready(function() {
	hideallexports();
});
function hideallexports() {
	$("#SYSMEX").hide();
	$("#STAGO").hide();
	$("#VITROS").hide();
	$("#SYSMEXbutton").css("background-color", "black");
	$("#STAGObutton").css("background-color", "black");
	$("#VITROSbutton").css("background-color", "black");
	$('#dataModal').modal('hide');
}
function getTestRunDates(value, divId, name, id, instrument) {
	$("#" + divId).html("");
	if (value != '-1') {
		var result = asynchronousAjaxCall(mainUrl
				+ "/sysmex/sysmexStudyRunDates/" + value + "/" + instrument);
		if (result != '' || result != 'undefined') {
			var v = "<select name=\"" + name + "\" id=\"" + id
					+ "\" class=\"form-control\">";
			v = v + "<option value=\"-1\">--Select--</option>";
			$.each(result, function(k, value) {
				v = v + "<option value=\"" + value + "\">" + value
						+ "</option>";
			});
			v = v + "</select>";
			$("#" + divId).html(v);
		}
	}
}

function acceptCheckBox() {
	$('#dataModal').modal('hide');
}
function displayInstrumentSelection(instrument) {
	hideallexports();
	$("#" + instrument + "button").css("background-color", "green");
	$("#" + instrument).show();
}

function viewSysmexData() {
	$("#sysmexStudyNumbersMsg").html("");
	if ($("#sysmexStudyNumbers").val() != -1) {
		$("#sysmexDataDiv").html("");
		if (checSysmexViewInputValidation()) {
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
						value : $("#sysmexStudyNumbers").val()
					});
					updateEleData.push({
						name : "stDate",
						value : $("#sysmexstDate").val()
					});
					updateEleData.push({
						name : "sampleType",
						value : $("#sysmexsampleType").val()
					});
					updateEleData.push({
						name : "observation",
						value : $("#sysmexObservation").val()
					});
					$.ajax({
						url : $("#mainUrl").val()
								+ '/sysmex/sysmexDataExportTable',
						type : 'POST',
						data : updateEleData,
						success : function(e) {
							//					
							console.log(e)
							$("#sysmexDataDiv").html(e);
						},
						error : function(er) {

						}
					});
				},
				error : function(er) {

				}
			});
		}
	} else {
		$("#sysmexStudyNumbersMsg").html("Required Field");
	}

}

function checSysmexViewInputValidation() {
	debugger;
	$("#sysmexstDateMsg").html("");
	if ($("#sysmexstDate").val() == '' || $("#sysmexstDate").val() == -1) {
		$("#sysmexstDateMsg").html("Required Field");
		return false;
	} else if ($("#sysmexObservation").val() == -1) {
		$("#sysmexObservationMsg").html("Required Field");
		return false;
	} else
		return true;
}

function diaplaySysmexReport(studyId, animalNum) {
	$("#sysmexStudyId").val(studyId);
	$("#sysmexAnimalNum").val(animalNum);
	$("#sysmexObservationPdf").val($("#sysmexObservation").val());
	$("#sysmexDataPdfId").submit();
}

function sysmexExportData() {
	$("#sysmexStudyNumbersMsg").html("");
	$("#sysmexstDateMsg").html("");
	debugger;
	if ($("#sysmexStudyNumbers").val() != -1) {
		if ($("#sysmexstDate").val() == '' || $("#sysmexstDate").val() == -1) {
			$("#sysmexstDateMsg").html("Required Field");
		} else if ($("#sysmexObservation").val() == -1) {
			$("#sysmexObservationMsg").html("Required Field");
		} else {

			$("#sysmexDataViewId").submit();
		}

	} else {
		$("#sysmexStudyNumbersMsg").html("Required Field");
	}

}
function viewSysmexRepetedValuesUpdtePage(animalNumber, columnIndex, studyId,
		resultCount) {
	// alert(animalNo + " " + studyId);
	debugger;
	$("#dataSelectionDiv").html("");
	$("#myModalLabel").html("Sysmex Data Selection");
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
					eleData.push({
						name : "observation",
						value : $("#sysmexObservation").val()
					});
					eleData.push({
						name : "sysmexstDate",
						value : $("#sysmexstDate").val()
					});
					$
							.ajax({
								url : $("#mainUrl").val()
										+ '/sysmex/sysmexResultSelection',
								type : 'POST',
								data : eleData,
								success : function(e) {
									console.log(e)
									var v = "<table class=\"table table-bordered table-striped\"><tr><th>Study :"
											+ e.studyNumber
											+ "</th><th>Animal :"
											+ animalNumber + "</th></tr>";
									v += "<tr><td>Select Result : </td><td><table><tr><th>Value</th><th>Date</th></tr>"
									for (var index = 0; index < e.resultDto.length; index++) {
										v += "<tr><td>"
										debugger;
										// alert(e.result[index].finalValue)
										if (e.resultDto[index].finalValue)
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e.resultDto[index].id
													+ "\" checked=\"checked\">"
													+ e.resultDto[index].value
													+ "&nbsp;";
										else
											v += "<input type=\"radio\" name=\"finalResult\" value=\""
													+ e.resultDto[index].id
													+ "\">"
													+ e.resultDto[index].value
													+ "&nbsp;";
										v += "</td><td>"
												+ e.resultDto[index].displayRunTime;
										v += "</td></tr>";
									}
									v += "</table></td></tr>";
									v += "<tr><td>Comment:</td><td><input id=\"rerunCommnet\" name=\"rerunCommnet\"/><font color=\"red\" id=\"rerunCommnetMsg\"></font></td></Tr>";
									v += "<tr><td colspan=\"2\"><input type=\"button\" value=\"Save\" onclick=\"viewSysmexRepetedValuesUpdte('"
											+ studyId
											+ "', '"
											+ animalNumber
											+ "', '"
											+ e.testName
											+ "', '"
											+ columnIndex
											+ "', '"
											+ resultCount
											+ "')\"/></td></tr></table>";
									$("#dataSelectionDiv").html(v);
									$('#dataModal').modal('show');
								},
								error : function(er) {
									debugger;
									console.log(er);
								}
							});
				},
				error : function(er) {

				}
			});
}

function viewSysmexRepetedValuesUpdte(studyId, animalNo, testName, columnIndex,
		resultCount) {
	$("#rerunCommnetMsg").html("");
	var commment = $("#rerunCommnet").val().trim();
	if (commment != "") {
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
				eleData.push({
					name : "rerunCommnet",
					value : commment
				});
				eleData.push({
					name : "observation",
					value : $("#sysmexObservation").val()
				});
				eleData.push({
					name : "sysmexstDate",
					value : $("#sysmexstDate").val()
				});
				$.ajax({
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
						console.log(er);
					}
				});
			},
			error : function(er) {
				debugger;
				console.log(er);
			}
		});
	} else {
		$("#rerunCommnetMsg").html("Required Field");
	}

}
// --------------------------------------------Stago-----------------------------------------
function viewStagoData() {
	$("#stagostudyNumbersMsg").html("");
	$("#stDateMsg").html("");
	$("#observationMsg").html("");
	$("#stagoDataDiv").html("");
	if ($("#stagostudyNumbers").val() != -1) {
		if ($("#stagostDate").val() == '' || $("#stagostDate").val() == -1) {
			$("#stDateMsg").html("Required Field");
		} else if ($("#observation").val() == -1) {
			$("#observationMsg").html("Required Field");
		} else {

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
						value : $("#stagostudyNumbers").val()
					});
					updateEleData.push({
						name : "stDate",
						value : $("#stagostDate").val()
					});
					updateEleData.push({
						name : "sampleType",
						value : $("#stagosampleType").val()
					});
					updateEleData.push({
						name : "observation",
						value : $("#observation").val()
					});
					$.ajax({
						url : $("#mainUrl").val()
								+ '/sysmex/stagoDataExportTable',
						type : 'POST',
						data : updateEleData,
						success : function(e) {
							//					
							console.log(e)
							$("#stagoDataDiv").html(e)
						},
						error : function(er) {

						}
					});
				},
				error : function(er) {

				}
			});
		}
	} else {
		$("#stagostudyNumbersMsg").html("Required Field");
	}

}
function stagoexportData() {
	debugger;
	$("#stagostudyNumbersMsg").html("");
	$("#stDateMsg").html("");
	$("#observationMsg").html("");
	// $("#stagoDataDiv").html("");
	if ($("#stagostudyNumbers").val() != -1) {
		if ($("#stagostDate").val() == '' || $("#stagostDate").val() == -1) {
			$("#stDateMsg").html("Required Field");
		} else if ($("#observation").val() != -1) {
			$("#stagoDataViewId").submit();
		} else {
			$("#observationMsg").html("Required Field");
		}
	} else
		$("#stagostudyNumbersMsg").html("Required Field");
}
function diaplayStagoReport(studyId, animalId) {
	$("#stagoStudyId").val(studyId);
	$("#stagoAnimalId").val(animalId);
	debugger;
	$("#stagoStartDate").val($("#stagostDate").val());
	$("#multipleAnimals").val()
	// debugger;
	$("#stagoDataPdfId").submit();

}
function stagoAnimalPdf() {
	// debugger;
	$("#stagostudyNumbersMsg").html("");
	$("#stagoAnimalId").val(0)
	$("#pdfMsg").html("");
	var checkboxVaues = [];
	var flag = false;
	$("input:checkbox[name='stagoAnimals']:checked").each(function() {
		checkboxVaues.push($(this).val());
		flag = true;
	});
	
	if (checkboxVaues.length > 0) {
		if ($("#stagostudyNumbers").val() != -1) {
			// alert($("#stagosampleType").val())
			$("#stagoSampleTypeValue").val($("#stagosampleType").val());
			// alsert($("#stagoSampleTypeValue").val());
			$("#stagoStudyId").val($("#stagostudyNumbers").val());
			debugger;
			$("#stagoMultipleAnimals").val(checkboxVaues)
			$("#stagoStartDate").val($("#stagostDate").val());
			$("#stagoObservationPdf").val($("#observation").val());
			$("#stagoDataPdfId").submit();
		} else {
			$("#stagostudyNumbersMsg").html("Required Field");
		}
	} else {
		$("#stagopdfMsg").html("Select atleat one Animal");
	}
}
function selectStagoResultPage(rowId, animalNo, studyId, studyNo, testName,
		tdId, animalId) {
	// alert(animalNo + " " + studyId);
	debugger;
	var observation = $("#observation").val();
	var stagostDate = $("#stagostDate").val();
	$("#dataSelectionDiv").html("");
	$("#myModalLabel").html("Stago Data Selection");
	var result = asynchronousAjaxCall(mainUrl
			+ "/sysmex/stagoResultSelectionAjax/" + studyId + "/" + testName
			+ "/" + animalId + "/rowId/" + tdId + "/" + $("#sampleType").val()
			+ "/" + observation + "/" + stagostDate);
	if (result != '' || result != 'undefined') {
		$("#dataSelectionDiv").html(result);
		$('#dataModal').modal('show');
	}

	// var eleData = [];
	// $
	// .ajax({
	// url : $("#mainUrl").val() + '/administration/getCsrfToken',
	// type : 'GET',
	// success : function(data) {
	// eleData.push({
	// name : data.parameterName,
	// value : data.token
	// });
	// eleData.push({
	// name : "studyId",
	// value : studyId
	// });
	// eleData.push({
	// name : "animalNo",
	// value : animalNo
	// });
	// eleData.push({
	// name : "testName",
	// value : testName
	// });
	// eleData.push({
	// name : "animalId",
	// value : animalId
	// });
	// $
	// .ajax({
	// url : $("#mainUrl").val()
	// + '/sysmex/stagoResultSelection',
	// type : 'POST',
	// data : eleData,
	// success : function(ek) {
	// debugger;
	// console.log(ek)
	// var v = "<table class=\"table table-bordered table-striped\"><tr><th>"
	// + studyNo
	// + "</th><th>"
	// + animalNo
	// + "</th></tr>";
	// v += "<tr><td>Select Result : </td><td>"
	// var e = ek.list;
	// for (var index = 0; index < e.length; index++) {
	// if (e[index].selectedResult)
	// v += "<input type=\"radio\" name=\"finalResult\" value=\""
	// + e[index].id
	// + "\" checked=\"checked\">"
	// + e[index].testResult
	// + "&nbsp;";
	// else
	// v += "<input type=\"radio\" name=\"finalResult\" value=\""
	// + e[index].id
	// + "\">"
	// + e[index].testResult
	// + "&nbsp;";
	// }
	// v += "</td></tr>";
	// v += "<tr><td>Comment:</td><td><input id=\"rerunCommnet\"
	// name=\"rerunCommnet\"/><font color=\"red\"
	// id=\"rerunCommnetMsg\"></font></td></Tr>";
	// v += "<tr><td colspan=\"2\"><input type=\"button\" value=\"Save\"
	// onclick=\"stagoResultSelectionSave('"
	// + studyId
	// + "', '"
	// + animalNo
	// + "', '"
	// + testName
	// + "', '"
	// + rowId
	// + "', '"
	// + animalId
	// + "', '"
	// + tdId
	// + "')\"/></td><tr></table>";
	// $("#dataSelectionDiv").html(v);
	// $('#dataModal').modal('show');
	// },
	// error : function(er) {
	// debugger;
	// console.log(er)
	// }
	// });
	// },
	// error : function(er) {
	// debugger;
	// console.log(er)
	// }
	// });

}

function stagoResultSelectionSave(studyId, animalNo, testName, rowId, animalId,
		tdId, sampleType) {
	$("#rerunCommnetMsg").html("");
//	 alert(tdId);
	 
	var commment = $("#rerunCommnet").val().trim();
	if (commment != "") {
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
				eleData.push({
					name : "rerunCommnet",
					value : commment
				});
				eleData.push({
					name : "sampleType",
					value : sampleType
				});
				eleData.push({
					name : "observation",
					value : $("#observation").val()
				});
				eleData.push({
					name : "stagostDate",
					value : $("#stagostDate").val()
				});
				$.ajax({
					url : $("#mainUrl").val()
							+ '/sysmex/stagoResultSelectionSave',
					type : 'POST',
					data : eleData,
					success : function(e) {

						console.log(e)
						// var tdId = "data_" + studyId + "_" + animalNo;
						// console.log(e.value)
						// console.log($("#" + rowId).html());
						debugger;
						// alert(tdId+" " +e.value)
						$("#" + tdId).html(e.value);
						// $("#" + rowId + "td").html(e.value);
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
	} else {
		$("#rerunCommnetMsg").html("Required Field");
	}

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

// ----------------------------------------------Vitros Data
// -------------------------------------------------------------
function checVitrosExportVadation() {
	$("#vitrosStudyNumbersMsg").html("");
	$("#vitrosStDateMsg").html("");
	var flag = true;
	if ($("#vitrosStudyNumbers").val() == -1) {
		flag = false;
		$("#vitrosStudyNumbersMsg").html("Required Field");
	}
	if ($("#vitrosStDate").val() == '' || $("#vitrosStDate").val() == -1) {
		flag = false;
		$("#vitrosStDateMsg").html("Required Field");
	}
	if ($("#vitrosObservation").val() == -1) {
		flag = false;
		$("#vitrosObservationMsg").html("Required Field");
	}
	return flag;
}
function viewVitrosData() {
	debugger;
	if (checVitrosExportVadation()) {
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
					value : $("#vitrosStudyNumbers").val()
				});
				updateEleData.push({
					name : "stDate",
					value : $("#vitrosStDate").val()
				});
				updateEleData.push({
					name : "sampleType",
					value : $("#vitrosSampleType").val()
				});
				updateEleData.push({
					name : "observation",
					value : $("#vitrosObservation").val()
				});
				$
						.ajax({
							url : $("#mainUrl").val()
									+ '/sysmex/vitorsDataExportTable',
							type : 'POST',
							data : updateEleData,
							success : function(e) {
								debugger;
								console.log(e)
								$("#vtrosSataDiv").html(e)
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
function vitrosExportData() {
	if (checVitrosExportVadation())
		$("#vitrosdataViewId").submit();
}

function vistroDataExportPdfFunction(studyId, animalno) {
	$("#vitrosStudyNumbersMsg").html("");
	if ($("#vitrosStudyNumbers").val() != -1) {
		$("#vitrospdfStudyId").val(studyId);
		$("#vitrospdfanimalId").val(animalno);
		$("#vitrosPdfObservation").val($("#vitrosObservation").val());
		$("#vitrosdataViewpdfId").submit();
	} else
		$("#vitrosStudyNumbersMsg").html("Required Field");

}
function viewRepetedValuesUpdtePage(animalNumber, columnIndex, studyId,
		resultCount) {
	// alert(animalNo + " " + studyId);
	// debugger;
	$("#dataSelectionDiv").html("");
	$("#myModalLabel").html("Vitros Data Selection");
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
									// debugger;
									v += "</td></tr>";
									v += "<tr><td>Comment:</td><td><input id=\"rerunCommnet\" name=\"rerunCommnet\"/><font color=\"red\" id=\"rerunCommnetMsg\"></font></td></Tr>";
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

function viewRepetedValuesUpdte(studyId, animalNo, testName, columnIndex,
		resultCount) {
	$("#rerunCommnetMsg").html("");
	var commment = $("#rerunCommnet").val().trim();

	if (commment != "") {
		var finalResult = $('input[name="finalResult"]:checked').val();
		$("#dataSelectionDiv").html("");
		var eleData = [];
		// ( Long, String, String ,int ,Long, String)
		// alert("vitrosResultSelectionSave/"+studyId+"/"+animalNo+"/"+testName+"/"+columnIndex+"/"+finalResult+"/"+commment)
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
				eleData.push({
					name : "rerunCommnet",
					value : commment
				});
				$.ajax({
					url : $("#mainUrl").val()
							+ '/sysmex/vitrosResultSelectionSave',
					type : 'POST',
					data : eleData,
					success : function(e) {
						debugger;
						console.log(e)
						console.log(e.value)
						$("#" + resultCount).html(e.value);
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
	} else {
		$("#rerunCommnetMsg").html("Required Field");
	}

}