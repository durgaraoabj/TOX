<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Study Creation</title>

</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Clinical Path Parameters</b>
			</h3>
		</div>
		<div class="card-body">
			<c:url value="/grouping/saveIntrumentAndPerameters" var="savestudy"></c:url>
			<form:form action="${savestudy}" method="POST"
				id="saveFrom${observationInturmentConfigurationId}">
				<input type="hidden" name="observationDatesId"
					id="observationDatesId"
					value="${observationDatesId}" />
				<input type="hidden" name="observationInturmentConfigurationId"
					id="observationInturmentConfigurationId"
					value="${observationInturmentConfigurationId}" />
				<c:forEach items="${instumentMap}" var="instument">
					<script type="text/javascript">
						var insleftTest = [];
						var insleftTestOrder = {};
						var insleftTestDislayString = {};
						var insRigthTest = [];
						var insRigthTestOldOrder = {};
						var insRigthTestNewOrder = {};
						var insRightTestDislayString = {};
					</script>
					<table style="width: 100%">
						<tr style="background-color: #3C8DBC;">
							<th colspan="3">${instument.value.instrumentName}</th>
						</tr>
						<tr>
							<td align="left" class="ui-widget-content">
								<table style="width: 100%"
									id="${observationDatesId}_${instument.value.id}_tableLeft">
									<thead style="background-color: #3C8DBC;">
										<Tr>
											<th colspan="2">Non Selected Parameters</th>
										</tr>
										<c:if test="${profileMaptemp[instument.value.id].size() gt 0}">
											<Tr>
												<Td>Standard Parameter List</Td>
												<td><select class="form-control" class='form-control'
													id="${observationDatesId}_${instument.value.id}_Profile"
													onchange="insturmentProfileSelection(this.value, '${observationDatesId}', '${instument.value.id}')">
														<option value="-1">--Select--</option>
														<option value="0">Select All</option>
														<c:forEach items="${profileMaptemp[instument.value.id]}"
															var="profile">
															<option value="${profile.id}">${profile.profileName}</option>
														</c:forEach>
												</select></td>
											</Tr>
										</c:if>
										<tr>
											<th>Parameter</th>
											<th>Order</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${insturmentTestCodes[instument.value.id]}"
											var="tcv">
											<tr id="${observationDatesId}_${tcv.id}DivLeft"
												onclick="sendTestCodeToRight('${tcv.id}', '${tcv.orderNo}', '${tcv.disPalyTestCode}', '${instument.value.id}', '${observationDatesId}')">
												<td>${tcv.disPalyTestCode}</td>
												<td>${tcv.orderNo}</td>
											</tr>
											<script type="text/javascript">
												insleftTest.push('${tcv.id}');
												insleftTestOrder['${tcv.id}'] = '${tcv.orderNo}';
												insleftTestDislayString['${tcv.id}'] = '${tcv.disPalyTestCode}';
											</script>
										</c:forEach>
									</tbody>
								</table>
							</td>
							<td align="left" class="ui-widget-content">
								<table style="width: 100%"
									id="${observationDatesId}_${instument.value.id}_table">
									<thead style="background-color: #3C8DBC;">
										<Tr>
											<th colspan="3">Selected Parameters</th>
										</tr>
										<tr>
											<th>Parameter - ${instument.value.id}</th>
											<th>Old Order</th>
											<th>Order</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${observationInsturmentTestCodes}" var="map">
											<c:if test="${map.key eq  instument.value.id}">
												<c:forEach items="${map.value}" var="tcv">
													<tr
														id="${observationDatesId}_${tcv.testCode.id}DivRigth">
														<td
															onclick="sendTestCodeToLeft('${tcv.testCode.id}', '${tcv.orderNo}', '${tcv.testCode.disPalyTestCode}', '${instument.value.id}', '${observationDatesId}')">${tcv.testCode.disPalyTestCode}</td>
														<td
															onclick="sendTestCodeToLeft('${tcv.testCode.id}', '${tcv.orderNo}', '${tcv.testCode.disPalyTestCode}', '${instument.value.id}', '${observationDatesId}')">${tcv.orderNo}</td>
														<td><input type="text" value="${tcv.orderNo}"
															id="${observationDatesId}_${tcv.testCode.id}_Order"
															class='form-control'
															id="${observationDatesId}_${tcv.testCode.id}_Order"
															onchange="fieldValidation('${observationDatesId}_${tcv.testCode.id}_Order', '${instument.value.id}','${observationDatesId}','${tcv.testCode.id}' )" />
															<font color='red'
															id="${observationDatesId}_${tcv.testCode.id}_OrderMsg"></font>
														</td>
													</tr>
													<script type="text/javascript">
														debugger;
														insRigthTest
																.push('${tcv.testCode.id}');
														insRigthTestOldOrder['${tcv.testCode.id}'] = '${tcv.orderNo}';
														insRigthTestNewOrder['${tcv.testCode.id}'] = '${tcv.orderNo}';
														insRightTestDislayString['${tcv.testCode.id}'] = '${tcv.testCode.disPalyTestCode}';
													</script>
												</c:forEach>

											</c:if>
										</c:forEach>
									</tbody>
								</table>
							</td>
						</tr>
					</table>
					<br />
					<script>
						insturments['${instument.value.id}'] = '${instument.value.instrumentName}';
						obJinsleftTest['${instument.value.id}_${observationDatesId}'] = insleftTest;
						obJinsleftTestOrder['${instument.value.id}_${observationDatesId}'] = insleftTestOrder;
						obJinsleftTestDislayString['${instument.value.id}_${observationDatesId}'] = insleftTestDislayString;
						obJinsRigthTest['${instument.value.id}_${observationDatesId}'] = insRigthTest;
						obJinsRigthTestOldOrder['${instument.value.id}_${observationDatesId}'] = insRigthTestOldOrder;
						obJinsRigthTestNewOrder['${instument.value.id}_${observationDatesId}'] = insRigthTestNewOrder;
						obJinsRightTestDislayString['${instument.value.id}_${observationDatesId}'] = insRightTestDislayString;
					</script>
				</c:forEach>
				<table class="table table-bordered table-striped">
					<tr>
						<th colspan="5"><input type="button"
							value="Conform Parameters" class="btn btn-new"
							onclick="saveInsturmentParemtersData('${observationDatesId}', '${obserVationFor}')" /></th>
					</tr>
				</table>
			</form:form>

		</div>
	</div>



</body>

<script src='/TOX/static/js/grouping/createStudyAndGrouping.js'></script>
<script type="text/javascript">
// 	function gotoStudyDesignPage() {
// 		$("#gotostudyDesignPage").submit();
// 	}
	function acceptCheckBox() {
		$('#configDataModal').modal('hide');
	}
// 	$(document)
// 			.ready(
// 					function() {

// 						var url = mainUrl
// 								+ "/grouping/dbIntrumentAndPerametersForStudyAcclamatizationDates/"
// 								+ $("#observationDatesId").val();
// 						var result = asynchronousAjaxCall(mainUrl
// 								+ "/grouping/dbIntrumentAndPerametersForStudyAcclamatizationDates/"
// 								+ $("#observationDatesId").val());
// 						// 						debugger;
// 						if (result != 'undefined' && result != '') {

// 							$
// 									.each(
// 											result,
// 											function(key, eachInstrument) {
// 												var resultTable = "";
// 												var instumentName = eachInstrument.insturment;
// 												rightSideElementIds = [];
// 												// 								${f.key} - ${c.key} - ${c.value.instrument.instrumentName } - ${c.value.testCode.testCode }<br />
// 												$
// 														.each(
// 																eachInstrument.testCodes,
// 																function(index,
// 																		tc) {
// 																	var tcId = tc.testCode.id;
// 																	var evalue = tc.orderNo;
// 																	var element = "<tr  id=\"" + tcId + "DivRigth\">"
// 																	element += "<td>"
// 																			+ instumentName
// 																			+ "</td>";
// 																	element += "<td>"
// 																			+ tc.testCode.orderNo
// 																			+ "</td>";
// 																	element += "<td><input type=\"text\" class='form-control' name=\""
// 																			+ tcId
// 																			+ "_tcOrder\" id=\""
// 																			+ tcId
// 																			+ "_tcOrder\" onchange=\"checkvalidation('"
// 																			+ tcId
// 																			+ "', '"
// 																			+ instumentName
// 																			+ "')\" value=\""
// 																			+ evalue
// 																			+ "\" "
// 																			+ " onkeypress=\"return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44\" ><font color=\"red\" id=\""
// 											+ tcId + "_tcOrderMsg\"  ></font></td></tr>";
// 																	resultTable += element;
// 																	// alert(element);
// 																	onlyRigthElements[tcId
// 																			+ "_"
// 																			+ instumentName] = evalue;
// 																	$(
// 																			"#"
// 																					+ tcId
// 																					+ "DivLeft")
// 																			.remove();
// 																	rigthTestCodesOrder[tcId] = evalue;
// 																	rightSideElementIds
// 																			.push(tcId);
// 																});
// 												rigthSideElements[instumentName] = rightSideElementIds;
// 												$(
// 														"#"
// 																+ instumentName
// 																+ "_RightTableBody")
// 														.append(resultTable);
// 											});
// 						}
// 					});
</script>
</html>