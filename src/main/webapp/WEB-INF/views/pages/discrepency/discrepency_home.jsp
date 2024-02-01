<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<script type="text/javascript">
	var availableTags = [];
</script>
<body>
	<c:forEach items="${staticData}" var="sd">
		<script type="text/javascript">
			availableTags.push('${sd.code},${sd.sign},${sd.description}');
		</script>
	</c:forEach>
<div class="card">
	<div class="card-header">
		<h3 class="card-title">${sm.studyNo }: Discrepency's</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
	<table style="width:100%;margin-bottom:20px">	<tr><th >Accession Discrepency's</th></tr></table>
		<div style="width: 100%; overflow: scroll">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
				
					<tr>
						<th>Observation Name</th>
						<th>Animal No</th>
						<th>Field</th>
						<th>Field Type</th>
						<tH>Old Value</th>
						<tH>Value</th>
						<th>Status</th>
						<th>Raised By</th>
						<th>Comment</th>
						<th>Update Reason</th>
						<th>Updated</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${accessionDescrpencys}" var="v">
						<tr>
							<td>${v.crf.observationName }</td>
							<td>${v.accAnimalDataEntryDetails.animal.animalNo }</td>
							<td>${v.secElement.name }</td>
							<td>${v.secElement.type }</td>
							<td>${v.oldValue }</td>
							<td>${v.value }</td>
							<td>${v.status }</td>
							<td>${v.risedBy }</td>
							<td>${v.comment }</td>
							<td>${v.updateReason }</td>
							<td><c:choose>
									<c:when test="${v.status eq 'closed'}">
										Closed
										</c:when>
									<c:otherwise>
										<a href='#' onclick="descUpdate(${v.id})"
											class="btn btn-primary">Update</a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<table style="width:100%;margin-bottom:20px;margin-top:20px">	<tr><th >Observation Discrepency's</th></tr></table>
		<div style="width: 100%; overflow: scroll">
			<table id="example2" class="table table-bordered table-striped">
				<thead>
					<!-- <tr><td colspan="11">Observation Discrepency's</td></tr> -->
					<tr>
						<th>Observation Name</th>
						<th>Animal No</th>
						<th>Field</th>
						<th>Field Type</th>
						<tH>Old Value</th>
						<tH>Value</th>
						<th>Status</th>
						<th>Raised By</th>
						<th>Comment</th>
						<th>Update Reason</th>
						<th>Updated</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="v">
						<tr>
							<td>${v.crf.observationName }</td>
							<td>${v.secEleData.subjectDataEntryDetails.animal.permanentNo }</td>
							<td>${v.secElement.name }</td>
							<td>${v.secElement.type }</td>
							<td>${v.oldValue }</td>
							<td>${v.value }</td>
							<td>${v.status }</td>
							<td>${v.risedBy }</td>
							<td>${v.comment }</td>
							<td>${v.updateReason }</td>
							<td><c:choose>
									<c:when test="${v.status eq 'closed'}">
										Closed
										</c:when>
									<c:otherwise>
										<a href='#' onclick="obcdescUpdate(${v.id})"
											class="btn btn-primary">Update</a>
									</c:otherwise>
								</c:choose></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<!-- /.card-body -->
</div>
<!-- /.card -->



<div class="modal fade" id="discDataModal" tabindex="-1" role="dialog"
	aria-labelledby="discDataModal" aria-hidden="true"
	data-backdrop="static" data-keyboard="false">
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title" id="myModalLabel">Discrepancy Update</h4>
			</div>
			<div class="modal-body">
				<div id="discData"></div>
			</div>
			<div class="modal-footer">
				<div style="text-align: left;">
					<label class="checkbox-inline"><input type="checkbox"
						value="true" id="acceptCheckId" />&nbsp;&nbsp;&nbsp;Do You want
						to close page.</label>
				</div>
				<div style="text-align: right;">
					<input type="button" class="btn btn-primary"
						onclick="acceptCheckBox()" value="Close" />
				</div>
			</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	$('#discDataModal').modal('hide');
	$('#crfrulestable').DataTable();
});
$(function() {
	$("#newValue").autocomplete({
		source : availableTags
	});
});
function descUpdate(id){
	var result = asynchronousAjaxCall(mainUrl+"/discrepancy/discrepancyUpdate/"+id);
	if(result != 'undefined' && result != ''){
		$("#discData").html(result);
		$('#discDataModal').modal('show');
	}else
		alertify.error("Unable to display discrepency page");
}

function obcdescUpdate(id){
	var result = asynchronousAjaxCall(mainUrl+"/discrepancy/objservationDiscrepancyUpdate/"+id);
	if(result != 'undefined' && result != ''){
		$("#discData").html(result);
		$('#discDataModal').modal('show');
	}else
		alertify.error("Unable to display discrepency page");
}
function acceptCheckBox() {
	if ($('#acceptCheckId').is(':checked')) {
		$('#discDataModal').modal('hide');
  	}else{
		alertify.alert("Please check checkbox...");
  	}
}
</script>
</html>