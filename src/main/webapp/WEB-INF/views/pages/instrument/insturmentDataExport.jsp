<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<link rel="stylesheet" href='<c:url value="/static/css/tab.css"/>'>
<script type="text/javascript">
	var instrumentName = {};
</script>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Data Export From</b>
			</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<div class="row">
				<div class="card card-primary" style="width: 98%">
					<div class="card-header p-0 pt-1">
						<ul class="nav nav-tabs" id="custom-tabs-one-tab" role="tablist">
							<c:forEach items="${instruments}" var="instrument" varStatus="st">
								<li class="nav-item"
									onclick="callInstument('${instrument.id}', '${observation.id}')"><a
									class="nav-link" id="custom-tabs-one-${instrument.id}-tab"
									data-toggle="pill" href="#custom-tabs-one-${instrument.id}"
									role="tab" aria-controls="custom-tabs-one-${instrument.id}"
									aria-selected="false">${instrument.instrumentName}</a></li>
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
										<div class="tab-pane fade"
											id="custom-tabs-one-${instrument.id}" role="tabpanel"
											aria-labelledby="custom-tabs-one-${instrument.id}-tab">
											<c:url value="/sysmex/stagoDataExport" var="dataView" />
											<form:form action="${dataView}" method="post"
												id="stagoDataViewId">
												<table>
													<tr>
														<th>Study Number</th>
														<th>Test Run Date</th>
														<th>Sample Type</th>
														<th></th>
														<th>Export Type</th>
														<th>Observation</th>
														<th></th>
													</tr>
													<tr>
														<td><select name="studyNumbers"
															id="stagostudyNumbers"
															onchange="getTestRunDates(this.value, 'stagostDates', 'stDate', 'stagostDate', 'Stago')"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<c:forEach items="${studyList}" var="sno">
																	<option value="${sno.id}">${sno.studyNo}</option>
																</c:forEach>
														</select><font color='red' id="stagostudyNumbersMsg"></font></td>
														<td><div id="stagostDates">
																<input name="stDate" id="stagostDate" type="text"
																	class="form-control input-sm" autocomplete="off" />
																<script type="text/javascript">
																	$(function() {
																		$(
																				"#stagostDate")
																				.datepicker(
																						{
																							dateFormat : $(
																									"#dateFormatJsp")
																									.val(),
																							changeMonth : true,
																							changeYear : true
																						});
																	});
																</script>
															</div>
															<div id="stDateMsg" style="color: red;"></div></td>
														<td><select name="sampleType" id="stagosampleType"
															class='form-control' onchange="lotnoProperty(this.value)">
																<!-- 									<option value="Both" selected="selected">Both</option> -->
																<option value="Animal">Animal</option>
																<option value="QC">QC</option>
														</select></td>
														<td>
														<select name="observation" id="observation"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<option value="Acclimatization">Acclimatization</option>
																<option value="Treatment">Treatment</option>
														</select>
															<font color="red" id="observationMsg"></font>
														</td>
														<td><input type="button" onclick="viewStagoData()"
															value="View" class="btn btn-primary"
															style="width: 200px;"></td>
														<td><select name="exportType" id="stagoExportType"
															class='form-control'>
																<option value="Pdf">Pdf</option>
																<option value="Excel" selected="selected">Excel</option>
														</select></td>
														<td><a href='javascript:void(0)'
															onclick="stagoexportData()" class="btn btn btn-primary"><strong>Export</strong></a></td>
													</tr>
												</table>
											</form:form>
											<div id="stagoDataDiv" style="overflow: scroll;"></div>
											<c:url value="/sysmex/stagoDataExportPdf"
												var="stagoDataViewPdf" />
											<form:form action="${stagoDataViewPdf}" method="post"
												id="stagoDataPdfId">
												<input type="hidden" id="stagoSampleTypeValue"
													name="sampleType" />
												<input type="hidden" id="stagoStartDate" name="startDate" />
												<input type="hidden" id="stagoStudyId" name="studyId" />
												<input type="hidden" id="stagoAnimalId" name="animalId" />
												<input type="hidden" id="stagoMultipleAnimals"
													name="multipleAnimals" />
												<input type="hidden" id="stagoObservationPdf" name="observation" />
											</form:form>
											<div id="custom-tabs-two-${instrument.id}"></div>

										</div>
									</c:when>
									<c:when test="${instrument.instrumentName eq 'VITROS'}">
										<div class="tab-pane fade"
											id="custom-tabs-one-${instrument.id}" role="tabpanel"
											aria-labelledby="custom-tabs-one-${instrument.id}-tab">
											<c:url value="/sysmex/vitorsDataExport" var="vitrosdataView" />
											<form:form action="${vitrosdataView}" method="post"
												id="vitrosdataViewId">
												<table>
													<tr>
														<th>Study Number</th>
														<th>Test Run Date</th>
														<th>Sample Type</th>
														<th></th>
														<th>Export Type</th>
														<th></th>
													</tr>
													<tr>
														<td><select name="studyNumbers"
															id="vitrosStudyNumbers"
															onchange="getTestRunDates(this.value, 'vitrosStDates', 'stDate', 'vitrosStDate', 'VITROS')"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<c:forEach items="${studyList}" var="std">
																	<option value="${std.id}">${std.studyNo}</option>
																</c:forEach>
														</select><font color='red' id="vitrosStudyNumbersMsg"></font></td>
														<td>
															<div id="vitrosStDates">
																<input name="stDate" id="vitrosStDate" type="text"
																	class="form-control input-sm" autocomplete="off" />
																<script type="text/javascript">
																	$(function() {
																		$(
																				"#vitrosStDate")
																				.datepicker(
																						{
																							dateFormat : $(
																									"#dateFormatJsp")
																									.val(),
																							changeMonth : true,
																							changeYear : true
																						});
																	});
																</script>
															</div>
															<div id="vitrosStDateMsg" style="color: red;"></div>
														</td>
														<td><select name="sampleType" id="vitrosSampleType"
															class='form-control'>
																<!-- 									<option value="Both" selected="selected">Both</option> -->
																<option value="Animal">Animal</option>
																<option value="QC">QC</option>
														</select></td>
														<td>
														<select name="observation" id="vitrosObservation"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<option value="Acclimatization">Acclimatization</option>
																<option value="Treatment">Treatment</option>
														</select>
															<font color="red" id="vitrosObservationMsg"></font>
														</td>
														<td><input type="button"
															id="studyCreateFormSubmitBtn" onclick="viewVitrosData()"
															value="View" class="btn btn-primary"
															style="width: 200px;"></td>
														<td><select name="exportType" id="vitrosExportType"
															class='form-control'>
																<option value="Pdf">Pdf</option>
																<option value="Excel" selected="selected">Excel</option>
														</select></td>
														<td><a href='javascript:void(0)'
															onclick="vitrosExportData()" class="btn btn btn-primary"><strong>Export</strong></a></td>
													</tr>
												</table>
											</form:form>
											<c:url value="/sysmex/vistroDataExportPdf"
												var="vistrosdataViewpdf" />
											<form:form action="${vistrosdataViewpdf}" method="post"
												id="vitrosdataViewpdfId">
												<input type="hidden" name="studyId" id="vitrospdfStudyId"
													value="" />
												<input type="hidden" name="animalId" id="vitrospdfanimalId"
													value="" />
												<input type="hidden" name="observation" id="vitrosPdfObservation"
													value="" />
											</form:form>
											<div id="vtrosSataDiv" style="overflow: scroll;"></div>
										</div>
									</c:when>
									<c:when test="${instrument.instrumentName eq 'SYSMEX'}">
										<div class="tab-pane fade"
											id="custom-tabs-one-${instrument.id}" role="tabpanel"
											aria-labelledby="custom-tabs-one-${instrument.id}-tab">
											<c:url value="/sysmex/sysmexDataExport" var="sysmexdataView" />
											<form:form action="${sysmexdataView}" method="post"
												id="sysmexDataViewId">
												<table>
													<tr>
														<th>Study Number</th>
														<th>Test Run Date</th>
														<th>Sample Type</th>
														<th></th>
														<th>Export Type</th>
														<th></th>
													</tr>
													<tr>
														<td><select name="studyNumbers"
															id="sysmexStudyNumbers"
															onchange="getTestRunDates(this.value, 'sysmexstDates', 'stDate', 'sysmexstDate', 'Sysmex')"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<c:forEach items="${studyList}" var="sno">
																	<option value="${sno.id}">${sno.studyNo}</option>
																</c:forEach>
														</select><font color='red' id="sysmexStudyNumbersMsg"></font></td>
														<td>
															<%-- 							<input type="hidden" name="sysmexStudyNumbers" id="sysmexStudyNumbers" value="${activeStudyId}"/><font color='red' id="sysmexStudyNumbersMsg"></font> --%>
															<div id="sysmexstDates">
																<input name="stDate" id="sysmexstDate" type="text"
																	class="form-control input-sm" autocomplete="off" />
																<script type="text/javascript">
																	$(function() {
																		$(
																				"#sysmexstDate")
																				.datepicker(
																						{
																							dateFormat : $(
																									"#dateFormatJsp")
																									.val(),
																							changeMonth : true,
																							changeYear : true
																						});
																	});
																</script>
															</div>
															<div id="sysmexstDateMsg" style="color: red;"></div>

														</td>
														<td><select name="sampleType" id="sysmexsampleType"
															class='form-control'>
																<!-- 									<option value="Both" selected="selected">Both</option> -->
																<option value="Animal">Animal</option>
																<option value="QC">QC</option>
														</select></td>
														<td>
														<select name="observation" id="sysmexObservation"
															class='form-control'>
																<option value="-1" selected="selected">--Select--</option>
																<option value="Acclimatization">Acclimatization</option>
																<option value="Treatment">Treatment</option>
														</select>
															<font color="red" id="sysmexObservationMsg"></font>
														</td>
														<td><input type="button" onclick="viewSysmexData()"
															value="View" class="btn btn-primary"></td>
														<td><select name="exportType" id="sysmexExportType"
															class='form-control'>
																<option value="Pdf">Pdf</option>
																<option value="Excel" selected="selected">Excel</option>
														</select></td>
														<td><a href='javascript:void(0)'
															onclick="sysmexExportData()" class="btn btn btn-primary"><strong>Export</strong></a></td>
													</tr>
												</table>
											</form:form>
											<c:url value="/sysmex/sysmexDataExportPdf"
												var="sysmexdataViewPdf" />
											<form:form action="${sysmexdataViewPdf}" method="post"
												id="sysmexDataPdfId">
												<input type="hidden" id="sysmexStudyId" name="sysmexStudyId" />
												<input type="hidden" id="sysmexAnimalNum"
													name="sysmexAnimalNum" />
												<input type="hidden" name="observation" id="sysmexObservationPdf" />
													
											</form:form>
											<div id="sysmexDataDiv" style="overflow: scroll;"></div>
										</div>
									</c:when>
									<c:otherwise>
										<div class="tab-pane fade"
											id="custom-tabs-one-${instrument.id}" role="tabpanel"
											aria-labelledby="custom-tabs-one-${instrument.id}-tab">${instrument.instrumentName}</div>
									</c:otherwise>
								</c:choose>

							</c:forEach>
						</div>
					</div>
					<!-- /.card -->
				</div>
			</div>
		</div>

		<div class="modal fade" id="dataModal" tabindex="-1" role="dialog"
			aria-labelledby="dataModal" aria-hidden="true" data-backdrop="static"
			data-keyboard="false">
			<div class="modal-dialog modal-lg">
				<div class="modal-content">
					<div class="modal-header">
						<h4 class="modal-title" id="myModalLabel"></h4>
					</div>
					<div class="modal-body">
						<div id="dataSelectionDiv"></div>
					</div>
					<div class="modal-footer">
						<div style="text-align: right;">
							<input type="button" class="btn btn-primary"
								onclick="acceptCheckBox()" value="Close" />
						</div>
					</div>
				</div>
			</div>
		</div>
</body>
<script src='/TOX/static/js/instrument/dataExport.js'></script>
</html>

