<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Animal Randomization Issue Details</title>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Animal Randomization Issue Details</h3>
			<font color="blue">${rdmDto.displayMessage}</font>
				<c:if test="${rdmDto.reviewed && rdmDto.allowToReview}">
					<input type="button" id="approveBtn" value="Approve"
						class="btn btn-primary btn-md">&nbsp;&nbsp; <input
						type="button" id="rejectBtn" value="Reject"
						class="btn btn-primary btn-md">
				</c:if>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<div class="row">
				<c:if test="${rdmDto.randamizaitonSheetMale ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet - Male</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
								</Tr>
								<c:forEach items="${rdmDto.randamizaitonSheetMale.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
									</tr>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>
				<c:if test="${rdmDto.randamizaitonSheetFemale ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet - Female</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
								</Tr>
								<c:forEach items="${rdmDto.randamizaitonSheetFemale.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</c:if>

				<c:if test="${rdmDto.randamizaitonSheetMaleAscedning ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet <br /> Male
										(Ascedning)
									</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
									<th>G No.</th>
								</Tr>
								<c:forEach
									items="${rdmDto.randamizaitonSheetMaleAscedning.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
										<td>${animal.groupInfo.groupNo}</td>
									</tr>
								</c:forEach>
								<tr>
									<td>Mean</td>
									<td>${rdmDto.randamizaitonSheetMaleAscedning.mean}</td>
								</tr>
								<tr>
									<td>Mean + 20%</td>
									<td>${rdmDto.randamizaitonSheetMaleAscedning.start}</td>
								</tr>
								<tr>
									<td>Mean - 20%</td>
									<td>${rdmDto.randamizaitonSheetMaleAscedning.end}</td>
								</tr>
								<tr>
									<td>Minimum</td>
									<td>${rdmDto.randamizaitonSheetMaleAscedning.minimum}</td>
								</tr>
								<tr>
									<td>Maximum</td>
									<td>${rdmDto.randamizaitonSheetMaleAscedning.maximum}</td>
								</tr>

							</table>
						</div>
					</div>
				</c:if>

				<c:if test="${rdmDto.randamizaitonSheetFemaleAscedning ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet <br /> Female
										(Ascedning)
									</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
									<th>G No.</th>
								</Tr>
								<c:forEach
									items="${rdmDto.randamizaitonSheetFemaleAscedning.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
										<td>${animal.groupInfo.groupNo}</td>
									</tr>
								</c:forEach>
								<tr>
									<td>Mean</td>
									<td>${rdmDto.randamizaitonSheetFemaleAscedning.mean}</td>
								</tr>
								<tr>
									<td>Mean + 20%</td>
									<td>${rdmDto.randamizaitonSheetFemaleAscedning.start}</td>
								</tr>
								<tr>
									<td>Mean - 20%</td>
									<td>${rdmDto.randamizaitonSheetFemaleAscedning.end}</td>
								</tr>
								<tr>
									<td>Minimum</td>
									<td>${rdmDto.randamizaitonSheetFemaleAscedning.minimum}</td>
								</tr>
								<tr>
									<td>Maximum</td>
									<td>${rdmDto.randamizaitonSheetFemaleAscedning.maximum}</td>
								</tr>

							</table>
						</div>
					</div>
				</c:if>


				<c:if
					test="${rdmDto.randamizaitonSheetMaleGruoup ne null and rdmDto.randamizaitonSheetMaleGruoup.size() gt 0}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="5">Randomization Sheet - Male (Grup)</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
									<th>Dose<br />Amount
									</th>
									<th>Group No</th>
									<th>Permanent <br />No.
									</th>

									<th>Mean</th>
									<th>SD</th>
								</Tr>
								<c:forEach items="${rdmDto.randamizaitonSheetMaleGruoup}"
									var="groupWise">
									<c:forEach items="${groupWise.animals}" var="animal">
										<tr>
											<td>${animal.animalNo}</td>
											<td>${animal.weight}</td>
											<td>${animal.doseAmount}</td>
											<td>${groupWise.gorupNo}</td>

											<td>${animal.permanentNo}</td>
											<c:if test="${animal.groupCount ne 0}">
												<td rowspan="${animal.groupCount}">${groupWise.groupMean}</td>
												<td rowspan="${animal.groupCount}">${groupWise.grouoSD}</td>
											</c:if>
										</tr>
									</c:forEach>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>


				<c:if
					test="${rdmDto.randamizaitonSheetFemaleGruoup ne null and rdmDto.randamizaitonSheetFemaleGruoup.size() gt 0}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDto.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="5">Randomization Sheet - Female (Grup)</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDto.study.weightUnits.fieldValue})
									</th>
									<th>Dose<br />Amount
									</th>
									<th>Group No</th>
									<th>Mean</th>
									<Th>SD</Th>
								</Tr>
								<c:forEach items="${rdmDto.randamizaitonSheetFemaleGruoup}"
									var="groupWise">
									<c:forEach items="${groupWise.animals}" var="animal">
										<tr>
											<td>${animal.animalNo}</td>
											<td>${animal.weight}</td>
											<td>${animal.doseAmount}</td>
											<td>${groupWise.gorupNo}</td>
											<c:if test="${animal.groupCount ne 0}">
												<td rowspan="${animal.groupCount}">${groupWise.groupMean}</td>
												<td rowspan="${animal.groupCount}">${groupWise.grouoSD}</td>
											</c:if>

										</tr>
									</c:forEach>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>
			</div>

		</div>
	</div>

	<c:if test="${rdmDtoOld ne null}">
		<div class="card">
			<div class="card-body">
			<div class="row">
				<c:if test="${rdmDtoOld.randamizaitonSheetMale ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet - Male</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
								</Tr>
								<c:forEach items="${rdmDtoOld.randamizaitonSheetMale.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
									</tr>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>
				<c:if test="${rdmDtoOld.randamizaitonSheetFemale ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet - Female</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
								</Tr>
								<c:forEach items="${rdmDtoOld.randamizaitonSheetFemale.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</div>
				</c:if>

				<c:if test="${rdmDtoOld.randamizaitonSheetMaleAscedning ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet <br /> Male
										(Ascedning)
									</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
									<th>G No.</th>
								</Tr>
								<c:forEach
									items="${rdmDtoOld.randamizaitonSheetMaleAscedning.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
										<td>${animal.groupInfo.groupNo}</td>
									</tr>
								</c:forEach>
								<tr>
									<td>Mean</td>
									<td>${rdmDtoOld.randamizaitonSheetMaleAscedning.mean}</td>
								</tr>
								<tr>
									<td>Mean + 20%</td>
									<td>${rdmDtoOld.randamizaitonSheetMaleAscedning.start}</td>
								</tr>
								<tr>
									<td>Mean - 20%</td>
									<td>${rdmDtoOld.randamizaitonSheetMaleAscedning.end}</td>
								</tr>
								<tr>
									<td>Minimum</td>
									<td>${rdmDtoOld.randamizaitonSheetMaleAscedning.minimum}</td>
								</tr>
								<tr>
									<td>Maximum</td>
									<td>${rdmDtoOld.randamizaitonSheetMaleAscedning.maximum}</td>
								</tr>

							</table>
						</div>
					</div>
				</c:if>

				<c:if test="${rdmDtoOld.randamizaitonSheetFemaleAscedning ne null}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet <br /> Female
										(Ascedning)
									</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
									<th>G No.</th>
								</Tr>
								<c:forEach
									items="${rdmDtoOld.randamizaitonSheetFemaleAscedning.animals}"
									var="animal">
									<tr>
										<td>${animal.animalNo}</td>
										<td>${animal.weight}</td>
										<td>${animal.groupInfo.groupNo}</td>
									</tr>
								</c:forEach>
								<tr>
									<td>Mean</td>
									<td>${rdmDtoOld.randamizaitonSheetFemaleAscedning.mean}</td>
								</tr>
								<tr>
									<td>Mean + 20%</td>
									<td>${rdmDtoOld.randamizaitonSheetFemaleAscedning.start}</td>
								</tr>
								<tr>
									<td>Mean - 20%</td>
									<td>${rdmDtoOld.randamizaitonSheetFemaleAscedning.end}</td>
								</tr>
								<tr>
									<td>Minimum</td>
									<td>${rdmDtoOld.randamizaitonSheetFemaleAscedning.minimum}</td>
								</tr>
								<tr>
									<td>Maximum</td>
									<td>${rdmDtoOld.randamizaitonSheetFemaleAscedning.maximum}</td>
								</tr>

							</table>
						</div>
					</div>
				</c:if>


				<c:if
					test="${rdmDtoOld.randamizaitonSheetMaleGruoup ne null and rdmDtoOld.randamizaitonSheetMaleGruoup.size() gt 0}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="5">Randomization Sheet - Male (Grup)</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
									<th>Dose<br />Amount
									</th>
									<th>Group No</th>
									<th>Permanent <br />No.
									</th>

									<th>Mean</th>
									<th>SD</th>
								</Tr>
								<c:forEach items="${rdmDtoOld.randamizaitonSheetMaleGruoup}"
									var="groupWise">
									<c:forEach items="${groupWise.animals}" var="animal">
										<tr>
											<td>${animal.animalNo}</td>
											<td>${animal.weight}</td>
											<td>${animal.doseAmount}</td>
											<td>${groupWise.gorupNo}</td>

											<td>${animal.permanentNo}</td>
											<c:if test="${animal.groupCount ne 0}">
												<td rowspan="${animal.groupCount}">${groupWise.groupMean}</td>
												<td rowspan="${animal.groupCount}">${groupWise.grouoSD}</td>
											</c:if>
										</tr>
									</c:forEach>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>


				<c:if
					test="${rdmDtoOld.randamizaitonSheetFemaleGruoup ne null and rdmDtoOld.randamizaitonSheetFemaleGruoup.size() gt 0}">
					<div class="column">
						<div
							style="font-weight: bold; font-size: medium; text-align: center;">Study
							No : ${rdmDtoOld.study.studyNo}</div>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="5">Randomization Sheet - Female (Grup)</th>
								</tr>
								<Tr>
									<th>Animal<br />Accession<br />No
									</th>
									<th>Body<br />Weight<br />(${rdmDtoOld.study.weightUnits.fieldValue})
									</th>
									<th>Dose<br />Amount
									</th>
									<th>Group No</th>
									<th>Mean</th>
									<Th>SD</Th>
								</Tr>
								<c:forEach items="${rdmDtoOld.randamizaitonSheetFemaleGruoup}"
									var="groupWise">
									<c:forEach items="${groupWise.animals}" var="animal">
										<tr>
											<td>${animal.animalNo}</td>
											<td>${animal.weight}</td>
											<td>${animal.doseAmount}</td>
											<td>${groupWise.gorupNo}</td>
											<c:if test="${animal.groupCount ne 0}">
												<td rowspan="${animal.groupCount}">${groupWise.groupMean}</td>
												<td rowspan="${animal.groupCount}">${groupWise.grouoSD}</td>
											</c:if>

										</tr>
									</c:forEach>
								</c:forEach>

							</table>
						</div>
					</div>
				</c:if>
			</div>

		</div>
		</div>
	</c:if>
	<div class="modal fade" id="signacherModal" tabindex="-1" role="dialog"
		aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Electronic Signature</h4>
					<input type="hidden" id="signcheck" />
				</div>

				<div class="modal-body">
					<table>
						<tr>
							<th>User Name :</th>
							<td>${userName}</td>
						</tr>
						<tr>
							<th>Transaction Password</th>
							<td><input type="password" id="tpw" /><font color="red"
								id="tpwmsg"></font></td>
						</tr>
						<tr>
							<th>Comment</th>
							<td><input type="text" id="rcomment" /><font color="red"
								id="commentmsg"></font></td>
						</tr>
						<tr>
						<tr>
							<td />
							<td><input type="button" class="btn btn-primary"
								onclick="animalAccessionCrfsDataReview()" value="Submit" /></td>
						</tr>
					</table>
				</div>

				<div class="modal-footer">
					<div style="text-align: left;">
						<label class="checkbox-inline"><input type="checkbox"
							value="true" id="acceptsignCheckId" />&nbsp;&nbsp;&nbsp;Do You
							want to close page.</label>
					</div>
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptsignCheckBox()" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<c:url value="/studyReview/saveRandamizationData" var="saveApproveDataForm" />
	<form:form action="${saveApproveDataForm}" method="POST"
		id="saveApprData">
		<input type="hidden" id="redamizationId" name="redamizationId"
			value="${rdmDto.id}" />
		<input type="hidden" id="reviewType" name="reviewType" />
		<input type="hidden" id="reviewComment" name="reviewComment" />
	</form:form>
</body>

<script type="text/javascript">
	$(document).ready(function() {
		//		$('#discDataModal').modal('hide');
		$('#signacherModal').modal('hide');
	});
	function signacherPopup() {
		$('#signacherModal').modal('show');
	}

	function acceptsignCheckBox() {
		if ($('#acceptsignCheckId').is(':checked')) {
			$('#signacherModal').modal('hide');
		} else {
			aler("Please check checkbox...");
		}
	}

	$('#approveBtn').click(function() {
		approveOrRject('Approve');
	});
	$('#rejectBtn').click(function() {
		approveOrRject('Reject');
	});

	function approveOrRject(reivewType) {
		if (reivewType == 'Approve')
			$("#reviewType").val("Approve");
		else
			$("#reviewType").val("Reject");
		signacherPopup();
// 		$('#saveApprData').submit();
	}
	
	
	function animalAccessionCrfsDataReview(){
		$("commentmsg").html("");
		$("#tpwmsg").html("");	
		var reviewType = $("#reviewType").val();
		var access = true;
		var comment = $("#rcomment").val().trim();
		if(reviewType = 'Reject'){
			if(comment == ''){
				access = false;
				$("#commentmsg").html("Required Field");
			}	
		}
		if(access){
			var signcheck = asynchronousAjaxCall(mainUrl+"/studyReview/signcheck/"+$("#tpw").val());
//	 		alert($("#signcheck").val());
			$("#signcheck").val(signcheck.trim());
			if($("#signcheck").val() == "yes"){
				$("#reviewComment").val(comment);
				$('#signacherModal').modal('hide');
				$('#saveApprData').submit();
			}else{
				$("#tpw").val("");
				$("#tpwmsg").html("In-valied password");
			}			
		}
			
	}
</script>
</html>

