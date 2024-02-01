function sendTestCodeToRight(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
	$("#" + studyAcclamatizationDatesId + "_" + tcId + "DivLeft").remove();
	var functionData = "onclick=\"sendTestCodeToLeft('" + tcId + "', '"
			+ orderNo + "', '" + disPalyTestCode + "', '" + instumentId
			+ "', '" + studyAcclamatizationDatesId + "')\"";
	var ele = studyAcclamatizationDatesId + "_" + tcId + "_Order";
	var id = "id=\"" + studyAcclamatizationDatesId + "_" + tcId + "_Order\"";
	var messageEle = "<font color='red' id=\"" + studyAcclamatizationDatesId
			+ "_" + tcId + "_OrderMsg\"></font>";
	var onchangeOrder = " onchange=\"fieldValidation('" + ele + "', '"
			+ instumentId + "', '" + studyAcclamatizationDatesId + "','" + tcId
			+ "' )\" ";
	var row = "<tr id=\"" + studyAcclamatizationDatesId + "_" + tcId
			+ "DivRigth\" " + ">" + "<td " + functionData + ">" + disPalyTestCode
			+ "</td>" + "<td " + functionData + ">" + orderNo + "</td>"
			+ "<td><input type=\"text\" value=\"" + orderNo
			+ "\" class='form-control' " + id + " " + onchangeOrder + "/>"
			+ messageEle + "</td></tr>"
	$("#" + studyAcclamatizationDatesId + "_" + instumentId + "_table").append(
			row);
	// debugger;
	addElemtnToRigthArray(tcId, orderNo, disPalyTestCode, instumentId,
			studyAcclamatizationDatesId);
	removeElemtnToLeftArray(tcId, orderNo, disPalyTestCode, instumentId,
			studyAcclamatizationDatesId);

}
function sendTestCodeToLeft(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
	$("#" + studyAcclamatizationDatesId + "_" + tcId + "DivRigth").remove();
	var functionData = "onclick=\"sendTestCodeToRight('" + tcId + "', '"
			+ orderNo + "', '" + disPalyTestCode + "', '" + instumentId
			+ "', '" + studyAcclamatizationDatesId + "')\"";
	var row = "<tr id=\"" + studyAcclamatizationDatesId + "_" + tcId
			+ "DivLeft\" " + functionData + "><td>" + disPalyTestCode
			+ "</td><td>" + orderNo + "</td></tr>"
	$("#" + studyAcclamatizationDatesId + "_" + instumentId + "_tableLeft")
			.append(row);
	// debugger;
	addElemtnToLeftArray(tcId, orderNo, disPalyTestCode, instumentId,
			studyAcclamatizationDatesId);
	removeElemtnToRigthArray(tcId, orderNo, disPalyTestCode, instumentId,
			studyAcclamatizationDatesId)
}

function fieldValidation(id, insturmentId, studyAcclamatizationDatesId,
		testCodeId) {
	var value = $("#" + id).val();
	var insRigthTestNewOrder = obJinsRigthTestNewOrder[insturmentId + "_"
			+ studyAcclamatizationDatesId];
	insRigthTestNewOrder[testCodeId] = value;
	obJinsRigthTestNewOrder[insturmentId + "_" + studyAcclamatizationDatesId] = insRigthTestNewOrder;
	fieldValidationCheck(value, insturmentId, studyAcclamatizationDatesId,
			testCodeId);
}
function fieldValidationCheck(value, insturmentId, studyAcclamatizationDatesId,
		testCodeId) {
	// var messageEle = "<font color='red'
	// id=\""+studyAcclamatizationDatesId+"_"+tcId+"_OrderMsg\"></font>";
	var insTestCodeOrder = obJinsRigthTestNewOrder[insturmentId + "_"
			+ studyAcclamatizationDatesId];
	var flag = true;
	if(value != ''){
		$.each(insTestCodeOrder, function(testCodeIdloc, orderNo) {
			if (testCodeIdloc != testCodeId && orderNo == value) {
				$(
						"#" + studyAcclamatizationDatesId + "_" + testCodeId
								+ "_OrderMsg").html("Duplicate Order");
				$("#" + studyAcclamatizationDatesId + "_" + testCodeId + "_Order")
						.val("");
				flag = false;
				return true;
			}
		});	
	}else{
		$(
				"#" + studyAcclamatizationDatesId + "_" + testCodeId
						+ "_OrderMsg").html("Required Field");
		flag = false;
	}
	
	if (flag) {
		$("#" + studyAcclamatizationDatesId + "_" + testCodeId + "_OrderMsg")
				.html("");
		insTestCodeOrder[testCodeId] = value;
		obJinsRigthTestNewOrder[insturmentId + "_"
				+ studyAcclamatizationDatesId] = insTestCodeOrder;
	}
}
function addElemtnToRigthArray(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
	// debugger;
	var insRigthTest = obJinsRigthTest[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insRigthTest.push(tcId);
	obJinsRigthTest[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTest;

	var insRigthTestOldOrder = obJinsRigthTestOldOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insRigthTestOldOrder[tcId] = orderNo;
	obJinsRigthTestOldOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestOldOrder;

	var insRigthTestNewOrder = obJinsRigthTestNewOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insRigthTestNewOrder[tcId] = orderNo;
	obJinsRigthTestNewOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestNewOrder;

	var insRightTestDislayString = obJinsRightTestDislayString[instumentId
			+ "_" + studyAcclamatizationDatesId];
	insRightTestDislayString[tcId] = orderNo;
	obJinsRightTestDislayString[instumentId + "_" + studyAcclamatizationDatesId] = insRightTestDislayString;
}
function addElemtnToLeftArray(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
	// debugger;
	var insleftTest = obJinsleftTest[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insleftTest.push(tcId);
	obJinsleftTest[instumentId + "_" + studyAcclamatizationDatesId] = insleftTest;

	var insleftTestOrder = obJinsleftTestOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insleftTestOrder[tcId] = orderNo;
	obJinsleftTestOrder[instumentId + "_" + studyAcclamatizationDatesId] = insleftTestOrder;

	var insleftTestDislayString = obJinsleftTestDislayString[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insleftTestDislayString[tcId] = orderNo;
	obJinsleftTestDislayString[instumentId + "_" + studyAcclamatizationDatesId] = insleftTestDislayString;
}

function removeElemtnToRigthArray(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
	debugger;
	var insRigthTest = obJinsRigthTest[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insRigthTest = $.grep(insRigthTest, function(value) {
		return value != tcId;
	});
	obJinsRigthTest[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTest;

	var insRigthTestOldOrder = obJinsRigthTestOldOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	delete insRigthTestOldOrder[tcId];
	obJinsRigthTestOldOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestOldOrder;

	var insRigthTestNewOrder = obJinsRigthTestNewOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	delete insRigthTestNewOrder[tcId];
	obJinsRigthTestNewOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestNewOrder;

	var insRightTestDislayString = obJinsRightTestDislayString[instumentId
			+ "_" + studyAcclamatizationDatesId];
	delete insRightTestDislayString[tcId];
	obJinsRightTestDislayString[instumentId + "_" + studyAcclamatizationDatesId] = insRightTestDislayString;
}

function removeElemtnToLeftArray(tcId, orderNo, disPalyTestCode, instumentId,
		studyAcclamatizationDatesId) {
//	debugger;
	var insleftTest = obJinsleftTest[instumentId + "_"
			+ studyAcclamatizationDatesId];
	insleftTest = $.grep(insleftTest, function(value) {
		return value != tcId;
	});
	obJinsleftTest[instumentId + "_" + studyAcclamatizationDatesId] = insleftTest;

	var insleftTestOrder = obJinsleftTestOrder[instumentId + "_"
			+ studyAcclamatizationDatesId];
	delete insleftTestOrder[tcId];
	obJinsleftTestOrder[instumentId + "_" + studyAcclamatizationDatesId] = insleftTestOrder;

	var insleftTestDislayString = obJinsleftTestDislayString[instumentId + "_"
			+ studyAcclamatizationDatesId];
	delete insleftTestDislayString[tcId];
	obJinsleftTestDislayString[instumentId + "_" + studyAcclamatizationDatesId] = insleftTestDislayString;
}
function insturmentProfileSelection(standerdParamId,
		studyAcclamatizationDatesId, instumentId) {
	// debugger;
	if (standerdParamId == '0') {
		var array = obJinsleftTest[instumentId + "_"
				+ studyAcclamatizationDatesId];
		var insRigthTest = [];
		var insRigthTestOldOrder = {};
		var insRigthTestNewOrder = {};
		var insRightTestDislayString = {};
		$
				.each(
						array,
						function(index2, testCodeId) {
							sendTestCodeToRight(
									testCodeId,
									obJinsleftTestOrder[instumentId + "_"
											+ studyAcclamatizationDatesId][testCodeId],
									obJinsleftTestDislayString[instumentId
											+ "_" + studyAcclamatizationDatesId][testCodeId],
									instumentId, studyAcclamatizationDatesId);
							insRigthTest.push(testCodeId);
							insRigthTestOldOrder[testCodeId] = obJinsleftTestOrder[instumentId
									+ "_" + studyAcclamatizationDatesId][testCodeId];
							insRigthTestNewOrder[testCodeId] = obJinsleftTestOrder[instumentId
									+ "_" + studyAcclamatizationDatesId][testCodeId];
							insRightTestDislayString[testCodeId] = obJinsleftTestDislayString[instumentId
									+ "_" + studyAcclamatizationDatesId][testCodeId];
						});

		obJinsRigthTest[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTest;
		obJinsRigthTestOldOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestOldOrder;
		obJinsRigthTestNewOrder[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTestNewOrder;
		obJinsRightTestDislayString[instumentId + "_"
				+ studyAcclamatizationDatesId] = insRightTestDislayString;
	} else if (standerdParamId != '-1') {
		var insRigthTest = [];
		var insRigthTestOldOrder = {};
		var insRigthTestNewOrder = {};
		var insRightTestDislayString = {};
		var result = asynchronousAjaxCall(mainUrl
				+ "/grouping/sanderedParameterList/" + standerdParamId);
		// debugger;
		if (result != 'undefined' && result != '') {
			$
					.each(
							result,
							function(index, testCodeProfileParameters) {
								$
										.each(
												obJinsleftTest,
												function(index2, testCode) {
													$
															.each(
																	testCode,
																	function(
																			index3,
																			testCodeId) {
																		if (testCodeId == testCodeProfileParameters.testCode.id) {
																			sendTestCodeToRight(
																					testCodeId,
																					testCodeProfileParameters.testCode.orderNo,
																					testCodeProfileParameters.testCode.disPalyTestCode,
																					instumentId,
																					studyAcclamatizationDatesId);
																			insRigthTest
																					.push(testCodeId);
																			insRigthTestOldOrder[testCodeId] = testCodeProfileParameters.testCode.orderNo;
																			insRigthTestNewOrder[testCodeId] = testCodeProfileParameters.testCode.orderNo;
																			insRightTestDislayString[testCodeId] = testCodeProfileParameters.testCode.disPalyTestCode;
																			return true;
																		}
																	});
												});
							});
			obJinsRigthTest[instumentId + "_" + studyAcclamatizationDatesId] = insRigthTest;
			obJinsRigthTestOldOrder[instumentId + "_"
					+ studyAcclamatizationDatesId] = insRigthTestOldOrder;
			obJinsRigthTestNewOrder[instumentId + "_"
					+ studyAcclamatizationDatesId] = insRigthTestNewOrder;
			obJinsRightTestDislayString[instumentId + "_"
					+ studyAcclamatizationDatesId] = insRightTestDislayString;
		} else
			alert("Unable to display  Sandered Parameters");
	} else {
		var array = obJinsRigthTest[instumentId + "_"
				+ studyAcclamatizationDatesId];
		var insleftTest = [];
		var insleftTestOrder = {};
		var insleftTestDislayString = {};
		$
				.each(
						array,
						function(index2, testCodeId) {
							sendTestCodeToLeft(
									testCodeId,
									obJinsRigthTestOldOrder[instumentId + "_"
											+ studyAcclamatizationDatesId][testCodeId],
									obJinsRightTestDislayString[instumentId
											+ "_" + studyAcclamatizationDatesId][testCodeId],
									instumentId, studyAcclamatizationDatesId);
							insleftTest.push(testCodeId);
							insleftTestOrder[testCodeId] = obJinsleftTestOrder[instumentId
									+ "_" + studyAcclamatizationDatesId][testCodeId];
							insleftTestDislayString[testCodeId] = obJinsleftTestDislayString[instumentId
									+ "_" + studyAcclamatizationDatesId][testCodeId];
						});

		obJinsleftTest[instumentId + "_" + studyAcclamatizationDatesId] = insleftTest;
		obJinsleftTestOrder[instumentId + "_" + studyAcclamatizationDatesId] = insleftTestOrder;
		obJinsleftTestDislayString[instumentId + "_"
				+ studyAcclamatizationDatesId] = insleftTestDislayString;
	}
}

function saveInsturmentParemtersData(studyAcclamatizationDatesId, observationFor) {
	debugger;
	if(checkInsturmentParemtersDataValidation(studyAcclamatizationDatesId)){
		debugger;
		var insturmentParemtersDataDto = [];
		var testCodeIdsData  = [];
		var profileIds  = [];
		$.ajax({
			url : $("#mainUrl").val() + '/administration/getCsrfToken',
			type : 'GET',
			success : function(data) {
				insturmentParemtersDataDto.push({
					name : data.parameterName,
					value : data.token
				});
				insturmentParemtersDataDto.push({
					name : "studyAcclamatizationDatesId",
					value : studyAcclamatizationDatesId
				});
				insturmentParemtersDataDto.push({
					name : "observationFor",
					value : observationFor
				});
				
				
				$.each(insturments, function(insturmentId, instrumentName) {
					var insTestCodeIds = obJinsRigthTest[insturmentId + "_"
							+ studyAcclamatizationDatesId];
					var insTestCodeOrder = obJinsRigthTestNewOrder[insturmentId + "_"
							+ studyAcclamatizationDatesId];
					$.each(insTestCodeIds, function(index, testCodeId) {	
						testCodeIdsData.push(testCodeId);
						insturmentParemtersDataDto.push({
							name : testCodeId+"_tcOrder",
							value : insTestCodeOrder[testCodeId]
						});
					});
					profileIds.push($("#"+studyAcclamatizationDatesId+"_"+insturmentId+"_Profile").val());
				});
				insturmentParemtersDataDto.push({
					name : "addedTcIds",
					value : testCodeIdsData
				});
				insturmentParemtersDataDto.push({
					name : "profileIds",
					value : profileIds
				});
				
				$.ajax({
					url : $("#mainUrl").val() + '/grouping/saveIntrumentAndPerameters',
					type : 'POST',
					data : insturmentParemtersDataDto,
					success : function(e) {
						console.log(e.success)
						alert(e.Message);
						if (e.Success === 'true' || e.Success === true) {
							$("#"+studyAcclamatizationDatesId+"_clinpath").hide(500);
							displayedParametesrs = $.grep(displayedParametesrs, function(value) {
								return value != studyAcclamatizationDatesId;
							});
							var buttonTdId = buttonTdIdOfstudyAcclamatizationDatesId[studyAcclamatizationDatesId];
							$("#"+studyAcclamatizationDatesId+"_clinpath_td").html("");
							$("#"+buttonTdId).html("<i class=\"fas fa-plus\" onclick=\"parameterConfig('"+studyAcclamatizationDatesId+"', '"+buttonTdId+"')\"></i>");
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

function checkInsturmentParemtersDataValidation(studyAcclamatizationDatesId){
	var flag = true;
	debugger;
	$.each(insturments, function(insturmentId, instrumentName) {
		var insTestCodeIds = obJinsRigthTest[insturmentId + "_"
				+ studyAcclamatizationDatesId];
		var insTestCodeOrder = obJinsRigthTestNewOrder[insturmentId + "_"
				+ studyAcclamatizationDatesId];
		var order = [];
		$.each(insTestCodeIds, function(index, testCodeId) {
			var testCodeOrder = insTestCodeOrder[testCodeId];
			if (testCodeOrder != '') {
				$.each(order, function(index2, orderNo) {
					if (testCodeOrder == orderNo) {
						$("#" + studyAcclamatizationDatesId + "_" + testCodeId+"_OrderMsg")
								.html("Duplicate Order");
						flag = false;
						return true;
					} else {
						$("#" + studyAcclamatizationDatesId + "_" + testCodeId+"_OrderMsg")
								.html("");
					}
				});
				order.push(testCodeOrder);
			} else {
				$("#" + studyAcclamatizationDatesId + "_" + testCodeId+"_OrderMsg").html("Required Field");
				flag = false;
			}
		});
	});
	return flag;
}