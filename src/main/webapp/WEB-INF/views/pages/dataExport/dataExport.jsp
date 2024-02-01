<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Acclamatization Observations</h3>
		</div>
		<div class="card-body">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>Select</th>
						<th>Type</th>
						<th>Sub Type</th>
						<th>Prefix</th>
						<th>Observation</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${accessionObservations}" var="observation"
						varStatus="st">
						<tr>
							<td><input type="checkbox" name="crfCheckBox"
								id="crfCheckBox_${observation.crf.id}"
								value="${observation.crf.id}"></td>
							<td>${observation.crf.type}</td>
							<td>${observation.crf.subType}</td>
							<td>${observation.crf.prefix}</td>
							<td>${observation.crf.observationName}</td>
							<td><input type="button" id="button_${crf.id}"
								value="Export" class="btn btn-primary btn-sm"
								onclick="exportAcclamatizationPdfData('${observation.id}')" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</div>
	</div>

	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Treatment Observations</h3>
		</div>
		<div class="card-body">

			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>Select</th>
						<th>Type</th>
						<th>Sub Type</th>
						<th>Prefix</th>
						<th>Observation</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${unicaTreatmentObservations}" var="observation"
						varStatus="st">
						<tr>
							<td><input type="checkbox" name="crfCheckBox"
								id="crfCheckBox_${observation.crf.id}"
								value="${observation.crf.id}"></td>
							<td>${observation.crf.type}</td>
							<td>${observation.crf.subType}</td>
							<td>${observation.crf.prefix}</td>
							<td>${observation.crf.observationName}</td>
							<td><input type="button" id="button_${crf.id}"
								value="Export" class="btn btn-primary btn-sm"
								onclick="exportTreatmentPdfData('${observation.id}')" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<c:url value="/dataExport/acclamatigationObservatinExport"
		var="acclamatization"></c:url>
	<form:form action="${acclamatization}" method="POST"
		id="acclamatizationExp">
		<input type="hidden" name="observationId"
			id="acclamatizationObservation" />
	</form:form>

	<c:url value="/dataExport/treatmentObservatinExport" var="treatment"></c:url>
	<form:form action="${treatment}" method="POST" id="treatmentExp">
		<input type="hidden" name="observationId" id="treatmentObservation" />
	</form:form>
</body>
<script type="text/javascript">
	function exportTreatmentPdfData(id) {
		$("#treatmentObservation").val(id);
		$("#treatmentExp").submit();
	}
	function exportAcclamatizationPdfData(id) {
		$("#acclamatizationObservation").val(id);
		$("#acclamatizationExp").submit();
	}
</script>
</html>