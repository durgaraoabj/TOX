<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>

</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title" style="font-weight: bold; font-size: large;">Stago
				Data Export</h3>&nbsp;&nbsp;<a href='<c:url value="/sysmex/readInsturmentDataCapture/STAGO"/>'><b>Go To Collection Page</b></a>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<c:url value="/sysmex/stagoDataExport" var="dataView" />
			<form:form action="${dataView}" method="post" id="dataViewId">
				<table>
					<tr>
						<th></th>
					</tr>
					<tr>
						<td>Study Number :</td>
						<td><select name="studyNumbers" id="studyNumbers"
							onchange="dispalyeAnimala(this.value)" class='form-control'>
								<option value="-1" selected="selected">--Select--</option>
								<c:forEach items="${studyNumbers}" var="sno">
									<option value="${sno.id}">${sno.studyNo}</option>
								</c:forEach>
						</select></td>
						<td>Start Date</td>
						<td><input name="stDate" id="stDate" type="text"
							class="form-control input-sm" autocomplete="off" /> <script
								type="text/javascript">
								$(function() {
									$("#stDate").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
							<div id="stDateMsg" style="color: red;"></div></td>
						<td><select name="sampleType" id="sampleType" class='form-control'>
								<option value="Both" selected="selected">Both</option>
								<option value="Animal">Animal</option>
								<option value="QC">QC</option>
						</select></td>
						<td><input type="button" id="studyCreateFormSubmitBtn"
							onclick="viewStagoData()" value="View" class="btn btn-primary"
							style="width: 200px;"></td>
						<td><a href='javascript:void(0)' onclick="exportData()"
							class="btn btn btn-primary"><strong>Export</strong></a></td>
					</tr>
				</table>
			</form:form>
			<div id="dataDiv" style="overflow:scroll;">
				<%-- <table id="example1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<td>Study</td>
							<td>Animal No.</td>
							<td>PT (Sec)</td>
							<td>APTT (Sec)</td>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${sysmexDataList}" var="row" varStatus="st">
							<tr>
								<th>${row.study.studyNo} - ${row.multipleResult}</th>
								<th>${row.animalNo} - ${row.multipleResult eq true}</th>
								<c:choose>
									<c:when test="${row.multipleResult}">
										<th onclick="selectResultPage('${row.animalNo}', '${row.study.id}','${row.study.studyNo}', 'PT')"><div id="data_${row.id}">${row.ptResult}</div></th>
									</c:when>
									<c:otherwise><th>${row.ptResult}</th></c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${row.multipleResult}">
										<th id="${row.id}td" onclick="selectResultPage('${row.animalNo}', '${row.study.id}', '${row.study.studyNo}','APTT')">${row.apttResult}</th>
									</c:when>
									<c:otherwise><th>${row.apttResult}</th></c:otherwise>
								</c:choose>

							</tr>
						</c:forEach>
					</tbody>
				</table> --%>
			</div>

		</div>
		<!-- /.card-body -->
	</div>
	<!-- /.card -->
	<c:url value="/sysmex/stagoDataExportPdf" var="stagoDataViewPdf" />
	<form:form action="${stagoDataViewPdf}" method="post" id="stagoDataPdfId">
<!-- 		<input type="text" id="stagoSampleTypeValue" name="sampleType"/> -->
		<input type="hidden" id="stagoStudyId" name="studyId"/>
		<input type="hidden" id="stagoAnimalId" name="animalId"/>
		<input type="hidden" id="multipleAnimals" name="multipleAnimals"/>
	</form:form>
	<div class="modal fade" id="dataModal" tabindex="-1" role="dialog"
		aria-labelledby="dataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Stago Data Selection</h4>
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
<script src='/TOX/static/js/instrument/sysmexData.js'></script>
</html>
