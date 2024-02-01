<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<script type="text/javascript">
	var animalIds = [];
</script>
</head>
<body>
	<!--Data take for audit  -->
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Data Entry View</h3>
			<table class="table table-bordered table-striped" style="width:50%">
				<tr>
					<td>Study : ${aadDto.study.studyNo }</td>
					<td colspan="4">Form Data Entry For : ${aadDto.crf.prefix}</td>
				</tr>
			</table>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<div style="width: 100%; overflow: scroll">
				<%-- <c:if test="${data.allowDataEntry}">
            			<input type="button" class="btn btn-primary" onclick="approveObservations()" value="Approve"/>
            		</c:if> --%>
				<c:url value="/studyReview/approveObservationNew"
					var="saveObservationReview" />
				<sf:form action="${saveObservationReview}" method="POST"
					id="saveReview">
					<input type="hidden" name="stdSubGroupObservationCrfsId"
						id="stdSubGroupObservationCrfsId" value="${aadDto.crf.name}">
<!-- 					<input type="button" id="approveBtn" value="Send To Review" -->
<!-- 						class="btn btn-primary btn-md"> -->
					<table class="table table-bordered table-striped" id="example2">
					<thead>
						<tr>
<!-- 							<th><input type="checkbox" name="allCheckBox" -->
<!-- 								id="allCheckBox" onclick="allCheckboxFunction()">Select</th> -->
							<th>Animal ID</th>
							<th>Status</th>
							<th>Review Status</th>
							<th>Entry Type</th>
							<th>Entered By</th>
							<th>Entered On</th>

							<!-- 							<th>Date Deviation</th> -->
							<%-- <th>${data.crfOrObservation.dayType }</th> --%>
							<c:forEach items="${aadDto.elements}" var="ele">
								<th><c:choose>
										<c:when
											test="${ele.value.leftDesc ne null and ele.value.leftDesc ne ''}">
											${ele.value.leftDesc}
										</c:when>
										<c:when
											test="${ele.value.rigtDesc ne null and ele.value.rigtDesc ne ''}">
											${ele.value.rigtDesc}
										</c:when>
										<c:when
											test="${ele.value.topDesc ne null and ele.value.topDesc ne ''}">
											${ele.value.topDesc}
										</c:when>
										<c:when
											test="${ele.value.bottemDesc ne null and ele.value.bottemDesc ne ''}">
											${ele.value.bottemDesc}
										</c:when>
										<c:when
											test="${ele.value.middeDesc ne null and ele.value.middeDesc ne ''}">
											${ele.value.middeDesc}
										</c:when>
										<c:otherwise>
											${ele.value.totalDesc}
										</c:otherwise>
									</c:choose> <%-- 								${ele.value.leftDesc } --%></th>
							</c:forEach>
						</tr>
						</thead>

						<c:forEach items="${aadDto.animalData }" var="ele" varStatus="st">
							<tr>
								<%-- 								<td>${ele.animalno}</td> --%>
<%-- 								<td><c:choose> --%>
<%-- 										<c:when test="${ele.statusCode == 'DATAENTRY'}"> --%>
<%-- 											<input type="checkbox" name="chkBox_${ele.id}" --%>
<%-- 												value="${ele.id}" id="chkBox_${ele.id}" /> --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<%-- 											<input type="checkbox" name="descElement" value="${ele.id}" --%>
<%-- 												id="${ele.id}" checked="checked" disabled="disabled" /> --%>
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> <script type="text/javascript"> --%>
<%-- // 										animalIds.push('${ele.id}'); --%>
<!-- 									</script></td> -->
								<td>${ele.animalId}</td>
								<td>${ele.status}</td>
								<th><c:choose>
										<c:when test="${ele.reviewed}">
										Done
									</c:when>
										<c:otherwise>No Done</c:otherwise>
									</c:choose></th>
								<td>${ele.entryType }</td>
								<%-- 								<td>${ele.deviationMessage}</td> --%>
								<td>${ele.userName }</td>
								<td>${ele.date }</td>
								<%-- <td>${ele.dayOrWeek }</td>	 --%>
								<c:forEach items="${ele.elementData }" var="eleData">
									<td>
										<div id="eleValue_${eleData.value.id}">${eleData.value.value }</div>
										
<%-- 										<c:choose> --%>
<%-- 											<c:when test="${ele.statusCode == 'DATAENTRY'}"> --%>
<!-- 												<img -->
<%-- 													src='<c:url value="/static/template/dist/img/flag_wite.png"/>' --%>
<!-- 													class="img-circle" title="Update" width="16" height="16" -->
<%-- 													onclick="updateValue('${eleData.value.id }', true, 'acesstion')"></img> --%>
<%-- 											</c:when> --%>
<%-- 											<c:otherwise> --%>
<!-- 												<img -->
<%-- 													src='<c:url value="/static/template/dist/img/flag_wite.png"/>' --%>
<!-- 													class="img-circle" title="Update" width="16" height="16" -->
<%-- 													onclick="updateValue('${eleData.value.id }', false, 'acesstion')"></img> --%>
<%-- 											</c:otherwise> --%>
<%-- 										</c:choose> --%>
									</td>
								</c:forEach>

							</tr>
						</c:forEach>
					</table>
				</sf:form>
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
					<h4 class="modal-title" id="myModalLabel">New Discrepancy</h4>
				</div>
				<div class="modal-body">
					<div id="discData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('discDataModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="updateDataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Update Animal
						Accession Data</h4>
				</div>
				<div class="modal-body">
					<div id="elementUpdateData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('updateDataModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:url value="/studyReview/createDiscrepencyNew"
		var="createDiscrepency" />
	<sf:form action="${createDiscrepency}" method="POST"
		modelAttribute="crfpojo" id="secDescSave"
		enctype="multipart/form-data">
		<input type="hidden" id="dataId3" name="dataId" />
		<input type="hidden" id="userId3" name="userId" />
		<input type="hidden" id="comment3" name="comment" />
		<input type="hidden" name="subGroupInfoId" value="${subGroupInfoId}" />
		<input type="hidden" name="stdSubGroupObservationCrfsId"
			value="${stdSubGroupObservationCrfsId}" />
		<input type="hidden" name="groupid" value="${data.group.id}" />
		<input type="hidden" name="subGroupId" value="${data.subGroup.id}" />
	</sf:form>

	<c:url value="/studyReview/saveApprovedData" var="saveApproveDataForm" />
	<sf:form action="${saveApproveDataForm}" method="POST"
		id="saveApprData">
		<input type="hidden" id="crfId" name="crfId" value="${crfId}" />
		<input type="hidden" id="stuydId" name="stuydId"
			value="${aadDto.study.id}" />
		<input type="hidden" id="chkckedIds" name="chkckedIds" />
		<input type="hidden" id="reviewType" name="reviewType" />
		<input type="hidden" id="reviewComment" name="reviewComment" />
	</sf:form>
</body>
<script type="text/javascript">
	function approveObservations() {
		if ($("#descall").is(":checked")) {
			var desc = $("#descall").val();
			if (desc > 0) {
				alert("Observation's Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
			} else {
				$("#saveReview").submit();
			}
		} else {
			var checked = 0;
			var descs = 0;
			$.each($("input[name='descElement']:checked"), function() {
				checked++;
				descs += $("#desc_" + this.id).val();
			});
			if (checked > 0) {
				if (descs > 0) {
					alert("Observation Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
				} else {
					$("#saveReview").submit();
				}
			} else {
				alsert("Select atleast one checkbox.")
			}

		}
	}
	function selectAll() {
		if ($("#descall").is(":checked")) {
			$("input[name='descElement']").each(function() {
				$("#" + this.id).attr('checked', true); // Checks it
			});
		} else {
			$("input[name='descElement']").each(function() {
				$("#" + this.id).attr('checked', false); // Checks it
			});
		}
	}
	function acceptCheckBox(id) {
		$('#' + id).modal('hide');
	}
	function createDiscrepency(id) {
		var result = asynchronousAjaxCall(mainUrl + "/studyReview/discDataNew/"
				+ id);
		if (result != 'undefined' && result != '') {
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		} else
			alert("Unable to display discrepency page");
	}

	function saveDisc() {
		// 			alert("asdf")
		$("#userIdmsg").html("");
		$("#commentdescmsg").html("");
		var flag = true;
		if ($("#userId").val() == -1) {
			$("#userIdmsg").html("Required Field");
			flag = false;
		}
		if ($("#commentdesc").val().trim() == "") {
			$("#commentdescmsg").html("Required Field");
			flag = false;
		}
		if (flag) {
			var dataId = $("#dataId").val();
			var userId = $("#userId").val();
			var comment = $("#commentdesc").val();

			$("#dataId3").val(dataId);
			$("#userId3").val(userId);
			$("#comment3").val(comment);
			$("#secDescSave").submit();

			// 				alert(mainUrl+"/studyReview/createDiscrepency/")
			/* 			var result = asynchronousAjaxCall(mainUrl+"/reviewer/createDiscrepency/"
								+vpcId+"/"+crfId+"/"+eleId+"/"+dataId+"/"+keyName+"/"+username+"/"+userId+"/"+comment);
						if(result != '' || result != 'undefined'){
					    	$('#discDataModal').modal('hide');
						} */
		}
	}
	$('#example2').DataTable({
		"paging" : true,
		"lengthChange" : false,
		"searching" : true,
		"ordering" : true,
		"info" : true,
		"autoWidth" : false,
	});
</script>
<script src='/TOX/static/js/accession/dataEntryView.js'></script>
</html>





