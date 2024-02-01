<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	var instrumentName = {};
</script>
</head>
<body>
	<%-- 	<input type="hidden" id="observationId" value="${observation.id}"/> --%>
	<div class="row">
		<div class="col-12">
			<h2><c:choose>
				<c:when test="${observation.studyAcclamatizationDates ne null}">
					Acclimatization
				</c:when>
				<c:when test="${observation.studyTreatmentDataDates ne null }">
					Treatment 
				</c:when>
				<c:otherwise></c:otherwise>
			</c:choose> Data Collection View From instrument</h2>
			<table id="example1" class="table table-bordered table-striped"
				style="align-content: flex-start;">
				<c:choose>
					<c:when test="${observation.studyAcclamatizationDates ne null }">
						<tr>
							<td>Study :</td>
							<td>${observation.study.studyNo}</td>
							<td>Date :</td>
							<th>${observation.studyAcclamatizationDates.dateString}</th>
						</tr>
						<tr>
							<td>Type :</td>
							<td>${observation.studyAcclamatizationDates.crf.type}</td>
							<td>Sub Type :</td>
							<td>${observation.studyAcclamatizationDates.crf.subType}</td>
						</tr>
						<tr>
							<td>Observation :</td>
							<td>${observation.studyAcclamatizationDates.crf.observationName}</td>
							<th colspan="2">Acclimatization</th>
						</tr>
					</c:when>
					<c:when test="${observation.studyTreatmentDataDates ne null}">
						<tr>
							<td>Study :</td>
							<td>${observation.study.studyNo}</td>
							<td>Date :</td>
							<td>${observation.studyTreatmentDataDates.dateString}</td>
						</tr>
						<tr>
							<td>Type :</td>
							<td>${observation.studyTreatmentDataDates.crf.type}</td>
							<td>Sub Type :</td>
							<td>${observation.studyTreatmentDataDates.crf.subType}</td>
						</tr>
						<tr>
							<td>Observation :</td>
							<td>${observation.studyTreatmentDataDates.crf.observationName}</td>
							<th colspan="2">Observation</th>
						</tr>
					</c:when>
					<c:otherwise>
					</c:otherwise>
				</c:choose>
			</table>
		</div>
	</div>
	<!-- ./row -->
	<div class="row">
		<div class="card card-primary" style="width: 98%">
			<div class="card-header p-0 pt-1">
				<ul class="nav nav-tabs" id="custom-tabs-one-tab" role="tablist">
					<c:forEach items="${instruments}" var="instrument" varStatus="st">
						<li class="nav-item"
							onclick="callInstument('${instrument.id}', '${observation.id}')"><a
							class="nav-link" id="custom-tabs-one-${instrument.id}-tab"
							data-toggle="pill" href="#custom-tabs-one-${instrument.id}" role="tab"
							aria-controls="custom-tabs-one-${instrument.id}" aria-selected="false">${instrument.instrumentName}</a></li>
						<script>
							instrumentName['${instrument.id}'] = '${instrument.instrumentName}';
						</script>
					</c:forEach>
				</ul>
			</div>
			<div class="card-body">
				<div class="tab-content" id="custom-tabs-one-tabContent"
					style="overflow: auto;">
					<c:forEach items="${instruments}" var="instrument" varStatus="st">
						<c:choose>
							<c:when test="${instrument.instrumentName eq 'STAGO'}">
								<div class="tab-pane fade" id="custom-tabs-one-${instrument.id}"
									role="tabpanel"
									aria-labelledby="custom-tabs-one-${instrument.id}-tab">
									<table>
										<tr>
											<td>Sample Type.</td>
											<td><select name="sampleType" id="sampleType"
												class="form-control"
												onchange="callStagoService(); sampleTypeChange()">
													<option value="-1" selected="selected">--Select--</option>
													<option value="QC">QC</option>
													<option value="Animal">Animal</option>
											</select> <font color='red' id="sampleTypeMsg"></font></td>
											<td>Lot No.</td>
											<td><input type="text" name="loatNo" id="loatNo"
												class="form-control" disabled="disabled"
												onchange="callStagoService()" /><font color='red'
												id="loatNoMsg"></font></td>
											<td>Test :</td>
											<td><select name="test" id="test" class="form-control"
												onchange="callStagoService()">
													<option value="-1" selected="selected">--Select--</option>
													<c:forEach items="${stagoTestCodes}" var="tc">
														<option value="${tc.testCode.testCode}">${tc.testCode.disPalyTestCode}</option>
													</c:forEach>
											</select> <font color='red' id="testMsg"></font></td>

											<td><input type='button' value="Reset" id="restId"
												onclick="resetStago()" disabled="disabled"></td>
											<td><input type='button' value="Goto Report Page"
												id="restId" onclick="gotoReportPage('STAGO')"></td>
										</tr>
									</table>
									<div id="custom-tabs-two-${instrument.id}"></div>

								</div>
							</c:when>
							<c:when test="${instrument.instrumentName eq 'VITROS'}">
								<div class="tab-pane fade" id="custom-tabs-one-${instrument.id}"
									role="tabpanel"
									aria-labelledby="custom-tabs-one-${instrument.id}-tab">${instrument.instrumentName}</div>
							</c:when>
							<c:when test="${instrument.instrumentName eq 'STAGO'}">
								<div class="tab-pane fade" id="custom-tabs-one-${instrument.id}"
									role="tabpanel"
									aria-labelledby="custom-tabs-one-${instrument.id}-tab">${instrument.instrumentName}</div>
							</c:when>
							<c:otherwise>
								<div class="tab-pane fade" id="custom-tabs-one-${instrument.id}"
									role="tabpanel"
									aria-labelledby="custom-tabs-one-${instrument.id}-tab">${instrument.instrumentName}</div>
							</c:otherwise>
						</c:choose>

					</c:forEach>
				</div>
			</div>
			<!-- /.card -->
		</div>



	</div>
</body>
<script type="text/javascript">
	var activeInstrument = false;
	var instumentid = 0;
	function callInstument(instumentId, observationId) {
// 				debugger;
		var flag = true;
		if (activeInstrument) {
			if (!confirm("Curently Istrument : " + instrumentName[instumentid]
					+ " is Running.\n"
					+ " Want to stop and Access insturmen : "
					+ instrumentName[instumentId] + "?")) {
				flag = false;
			} else {
				$("#custom-tabs-one-" + instumentid).html("");
			}
		}
		if (flag) {
			if (instrumentName[instumentId] == 'STAGO')
				stagoInstument(instumentId, observationId, instumentid);
			else if (instrumentName[instumentId] == 'SYSMEX')
				sysmaxInstument(instumentId, observationId, instumentid);
			else if (instrumentName[instumentId] == 'VITROS')
				vitrosInstument(instumentId, observationId, instumentid);

		}
	}

	function stagoInstument(instumentId, observationId, instumentid) {
		stagoInstumentId = instumentId;
		stagoObservationId = observationId;
		stagoInstumentid = instumentid;

	}

	function checkStagoValidation() {
		$("#sampleTypeMsg").html("");
		$("#testMsg").html("");
		$("#loatNoMsg").html("");
		var flag = true;
		if ($("#sampleType").val() == -1) {
			$("#sampleTypeMsg").html("Required Field");
			flag = false;
		} else if ($("#sampleType").val() == 'QC'
				&& $("#loatNo").val().trim() == '') {
			$("#loatNoMsg").html("Required Field");
			flag = false;
		}
		if ($("#test").val() == -1) {
			$("#testMsg").html("Required Field");
			flag = false;
		}
		if ($("#study").val() == -1) {
			$("#studyMsg").html("Required Field");
			flag = false;
		}
		return flag;
	}

	function sampleTypeChange() {
		if ($("#sampleType").val() == 'QC')
			$("#loatNo").removeAttr("disabled");
		else
			$("#loatNo").prop("disabled", true);
	}
	function callStagoService() {
		if (checkStagoValidation()) {
			//			var studyId = $("#studyId").val();
			var url = mainUrl + "/instrumentData/callInstumentStagoSerivce/"
					+ stagoInstumentId + "/" + stagoObservationId + "/"
					+ stagoInstumentid + "/" + $("#sampleType").val() + "/NA/"
					+ $("#test").val();
			if ($("#sampleType").val() == 'QC') {
				url = mainUrl + "/instrumentData/callInstumentStagoSerivce/"
						+ stagoInstumentId + "/" + stagoObservationId + "/"
						+ stagoInstumentid + "/" + $("#sampleType").val() + "/"
						+ $("#loatNo").val() + "/" + $("#test").val();
			}
			var result = asynchronousAjaxCall(url);
			if (result != '' || result != undefined) {
				debugger;
				// 								alert(result)
				var databody = $("#custom-tabs-two-" + stagoInstumentId).html();
				// 				databody = databody.trim();
				// alert(databody);
				if (databody == '') {

					var header = '<table  class="table table-bordered table-striped" style="width:90%"><thead><tr>';
					console.log(result.stagoTestCodesMap.length);
					// console.log(result.stagoTestCodesMap.length());
					console.log(result.stagoTestCodesMap.size);
					// console.log(result.stagoTestCodesMap.size());
					// 					var map = result.stagoTestCodesMap;
					// 					$.each(map, function(k, value) {
					// 						header += '<th>' + value + '</td>';
					// 					});
					header += '<th>Study Number</td>';
					header += '<th>Animal/Lot Number</td>';
					header += '<th>Sample Type</td>';
					header += '<th>Recived Time</td>';
					header += '<th>Test</td>';
					header += '<th>Result</td>';
					header += "</tr><tr>";
					//					map = result.stagoTestCodesUnitsMap;
					//					$.each(map, function(k, value) {
					//						header += '<th>' + value + '</td>';
					//					});
					header += '</tr></thead><tbody id=\"instrumentDataTableBody\"></tbody><table>';
					$("#custom-tabs-two-" + stagoInstumentId).html(header);
					$("#restId").removeAttr("disabled");
					debugger;
					// 					stagoWebSocket();

					var url = mainUrl + "/stagoWebSocket/fatchStagoData";
					var eventSource = new EventSource(url);
					eventSource.addEventListener("stagoinsData",
							function(event) {

								var articalData = JSON.parse(event.data);
								console.log("Event : " + event.data);
								// alert(articalData.id +" - "+ articalData.collected +" - "+
								// articalData.color)
								debugger;
								if (articalData.id != '0'
										&& articalData.status == '200') {

									$("#instrumentDataTableBody").append(
											articalData.dataFrom);
									// var old = $("#insturmetnData").html();
									// $("#stagoData").append("");
									// $("#insturmetnData").html(old + "<br/>" + dataFrom);
								}
							});
				}
			}
		}
	}

	function sysmaxInstument(instumentId, observationId, instumentid) {
		debugger;
		instument(instumentId, observationId, instumentid);
		var url = mainUrl + "/sysmexWebSocket/fatchData";
		var eventSource = new EventSource(url);
		eventSource.addEventListener("sysmesinsData", function(event) {
			var articalData = JSON.parse(event.data);
			//					alert("Event : " + event.data);
			//					 alert(articalData.id +" - "+ articalData.collected +" - "+
			//					 articalData.color)
			if (articalData.id != '0' && articalData.status == '200') {
				console.clear();
				//			alert(dataFrom);
				// var old = $("#insturmetnData").html();
				debugger;
				var v = $("#instrumentDataTableBody").html();
				$("#instrumentDataTableBody").empty("");
				$("#instrumentDataTableBody").append(articalData.dataFrom);
				$("#instrumentDataTableBody").append(v);
				// $('#instrumentDataTable tr:last').after(dataFrom);
			}
		});

	}

	function vitrosInstument(instumentId, observationId, instumentid) {
// 		debugger;
		// 		var result = asynchronousAjaxCall(mainUrl + "/sysmex/callVistrosService");
		instument(instumentId, observationId, instumentid);
		var url = mainUrl + "/vistrosWebSocket/fatchVistorsData";
		var eventSource = new EventSource(url);
		eventSource.addEventListener("vistrosData", function(event) {
			var articalData = JSON.parse(event.data);
			console.log("Event : " + event.data);
			// alert(articalData.id +" - "+ articalData.collected +" - "+
			// articalData.color)
			if (articalData.id != '0' && articalData.status == '200') {
				$("#instrumentDataTableBody").append(articalData.dataFrom);
			}
		});

	}
	function instument(instumentId, observationId, instumentid) {
		//		var result = asynchronousAjaxCall(mainUrl + "/sysmex/callSysmexService/0");
		var result = asynchronousAjaxCall(mainUrl
				+ "/instrumentData/callInstument/" + instumentId + "/"
				+ observationId + "/" + instumentid);
		if (result != 'undefined' && result != '') {
			var data = result;
			console.log(data);
			alert(data.message);
			debugger;
			var header = '<table id="instrumentDataTable" class="table table-bordered table-striped" style=" width: auto;margin-left: 1%"><thead><tr>';
			// header += "<th id='studyNo'>Study No.</th>";
			// header += "<th id='animalNo'>Animal No.</th>";
			// header += "<th id='startTime'>Start Time</th>";
			// console.log(data.length);

			var testCodesMap = data.testCodesMap[observationId];
			$.each(testCodesMap, function(ind, value) {
				console.log(ind + "   " + value);
				header += "<th id=\"" + ind + "\">" + value + "</th>";
			});
			header += "</tr><tr>";
			var testCodesUnitsMap = data.testCodesUnitsMap[observationId];
			$.each(testCodesUnitsMap, function(ind, value) {
				console.log(ind + "   " + value);
				header += "<th id=\"" + ind + "\">" + value + "</th>";
			});
			header += "</tr></thead><tbody id='instrumentDataTableBody'></tbody></table>";
			$("#custom-tabs-one-" + instumentId).html(header);
		}

	}
</script>
</html>