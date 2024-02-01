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
			<h3 class="card-title">Animal Randomization Review Details - ${tempRandamization}</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<c:choose>
				<c:when test="${tempRandamization eq 'Male'}">
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> ${tempRandamization}
								Randomization Sheet
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal Accession No</th>
										<th>Body Weight(g)</th>
									</tr>
									<c:forEach items="${tempmaledto.randamizationAssigndDto}"
										var="acession">
										<tr>
											<!--<td>${acession.order}</td>-->
											<td>${acession.animal.animalNo}</td>
											<td>${acession.animal.weight}</td>
										</tr>
									</c:forEach>
								</table>
								<div style="width: 60%; margin-left: 20%;">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Mean :</td>
											<td>${tempmaledto.mean}</td>
										</tr>
										<tr>
											<td>Mean + 20%</td>
											<td>${tempmaledto.maxMean}</td>
										</tr>
										<tr>
											<td>Mean - 20%</td>
											<td>${tempmaledto.minMean}</td>
										</tr>
										<tr>
											<td>Minimum</td>
											<td>${tempmaledto.minWeight}</td>
										</tr>
										<tr>
											<td>Maximum</td>
											<td>${tempmaledto.maxWeight}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Randomization Sheet<br />${tempRandamization}
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal No</th>
										<th>Body Weight(g)</th>
										<th>Group</th>
										<th>Mean</th>
										<th>SD</th>
									</tr>
									<c:forEach items="${tempmaledto.randamizationGroupDto}"
										var="groupAnimalList">
										<c:set var="sdstatus" value="true"></c:set>
										<c:forEach
											items="${groupAnimalList.randamizationGroupAnimalDto}"
											var="groupAnimal">
											<tr>
												<td>${groupAnimal.order}</td>
												<td>${groupAnimal.animalNo}</td>
												<td>${groupAnimal.animal.weight}</td>
												<td>${groupAnimalList.subGroupInfo.group.groupNo}</td>

												<c:if test="${sdstatus eq 'true' }">
													<c:set var="sdstatus" value="false"></c:set>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.mean}</td>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.sd}</td>
												</c:if>

											</tr>
										</c:forEach>
									</c:forEach>
								</table>

							</div>

						</div>
					</div>
				</c:when>
				<c:when test="${tempRandamization eq 'Female'}">
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> ${tempRandamization}
								Randomization Sheet
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal Accession No</th>
										<th>Body Weight(g)</th>
									</tr>
									<c:forEach items="${tempfemaledto.randamizationAssigndDto}"
										var="acession">
										<tr>
											<!--<td>${acession.order}</td>-->
											<td>${acession.animal.animalNo}</td>
											<td>${acession.animal.weight}</td>
										</tr>
									</c:forEach>
								</table>
								<div style="width: 60%; margin-left: 20%;">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Mean :</td>
											<td>${tempfemaledto.mean}</td>
										</tr>
										<tr>
											<td>Mean + 20%</td>
											<td>${tempfemaledto.maxMean}</td>
										</tr>
										<tr>
											<td>Mean - 20%</td>
											<td>${tempfemaledto.minMean}</td>
										</tr>
										<tr>
											<td>Minimum</td>
											<td>${tempfemaledto.minWeight}</td>
										</tr>
										<tr>
											<td>Maximum</td>
											<td>${tempfemaledto.maxWeight}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Randomization Sheet<br />${tempRandamization}
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal No</th>
										<th>Body Weight(g)</th>
										<th>Group</th>
										<th>Mean</th>
										<th>SD</th>
									</tr>
									<c:forEach items="${tempfemaledto.randamizationGroupDto}"
										var="groupAnimalList">
										<c:set var="sdstatus" value="true"></c:set>
										<c:forEach
											items="${groupAnimalList.randamizationGroupAnimalDto}"
											var="groupAnimal">
											<tr>
												<td>${groupAnimal.order}</td>
												<td>${groupAnimal.animalNo}</td>
												<td>${groupAnimal.animal.weight}</td>
												<td>${groupAnimalList.subGroupInfo.group.groupNo}</td>

												<c:if test="${sdstatus eq 'true' }">
													<c:set var="sdstatus" value="false"></c:set>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.mean}</td>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.sd}</td>
												</c:if>

											</tr>
										</c:forEach>
									</c:forEach>
								</table>

							</div>

						</div>
					</div>
				</c:when>
				<c:otherwise>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Male
								Randomization Sheet
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal Accession No</th>
										<th>Body Weight(g)</th>
									</tr>
									<c:forEach items="${tempmaledto.randamizationAssigndDto}"
										var="acession">
										<tr>
											<!--<td>${acession.order}</td>-->
											<td>${acession.animal.animalNo}</td>
											<td>${acession.animal.weight}</td>
										</tr>
									</c:forEach>
								</table>
								<div style="width: 60%; margin-left: 20%;">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Mean :</td>
											<td>${tempfemaledto.mean}</td>
										</tr>
										<tr>
											<td>Mean + 20%</td>
											<td>${tempfemaledto.maxMean}</td>
										</tr>
										<tr>
											<td>Mean - 20%</td>
											<td>${tempfemaledto.minMean}</td>
										</tr>
										<tr>
											<td>Minimum</td>
											<td>${tempfemaledto.minWeight}</td>
										</tr>
										<tr>
											<td>Maximum</td>
											<td>${tempfemaledto.maxWeight}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Randomization Sheet<br />Male
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal No</th>
										<th>Body Weight(g)</th>
										<th>Group</th>
										<th>Mean</th>
										<th>SD</th>
									</tr>
									<c:forEach items="${tempmaledto.randamizationGroupDto}"
										var="groupAnimalList">
										<c:set var="sdstatus" value="true"></c:set>
										<c:forEach
											items="${groupAnimalList.randamizationGroupAnimalDto}"
											var="groupAnimal">
											<tr>
												<td>${groupAnimal.order}</td>
												<td>${groupAnimal.animalNo}</td>
												<td>${groupAnimal.animal.weight}</td>
												<td>${groupAnimalList.subGroupInfo.group.groupNo}</td>

												<c:if test="${sdstatus eq 'true' }">
													<c:set var="sdstatus" value="false"></c:set>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.mean}</td>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.sd}</td>
												</c:if>

											</tr>
										</c:forEach>
									</c:forEach>
								</table>

							</div>

						</div>
					</div>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Female
								Randomization Sheet
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal Accession No</th>
										<th>Body Weight(g)</th>
									</tr>
									<c:forEach items="${tempfemaledto.randamizationAssigndDto}"
										var="acession">
										<tr>
											<!--<td>${acession.order}</td>-->
											<td>${acession.animal.animalNo}</td>
											<td>${acession.animal.weight}</td>
										</tr>
									</c:forEach>
								</table>
								<div style="width: 60%; margin-left: 20%;">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Mean :</td>
											<td>${tempfemaledto.mean}</td>
										</tr>
										<tr>
											<td>Mean + 20%</td>
											<td>${tempfemaledto.maxMean}</td>
										</tr>
										<tr>
											<td>Mean - 20%</td>
											<td>${tempfemaledto.minMean}</td>
										</tr>
										<tr>
											<td>Minimum</td>
											<td>${tempfemaledto.minWeight}</td>
										</tr>
										<tr>
											<td>Maximum</td>
											<td>${tempfemaledto.maxWeight}</td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="column">
							<div
								style="font-weight: bold; font-size: medium; text-align: center;">
								Study No : ${study.studyNo}<br /> Randomization Sheet<br />Famale
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<!--<td>Animal No</td>-->
										<th>Animal No</th>
										<th>Body Weight(g)</th>
										<th>Group</th>
										<th>Mean</th>
										<th>SD</th>
									</tr>
									<c:forEach items="${tempfemaledto.randamizationGroupDto}"
										var="groupAnimalList">
										<c:set var="sdstatus" value="true"></c:set>
										<c:forEach
											items="${groupAnimalList.randamizationGroupAnimalDto}"
											var="groupAnimal">
											<tr>
												<td>${groupAnimal.order}</td>
												<td>${groupAnimal.animalNo}</td>
												<td>${groupAnimal.animal.weight}</td>
												<td>${groupAnimalList.subGroupInfo.group.groupNo}</td>

												<c:if test="${sdstatus eq 'true' }">
													<c:set var="sdstatus" value="false"></c:set>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.mean}</td>
													<td rowspan="${groupAnimalList.subGroupAnimalCount}">${groupAnimalList.sd}</td>
												</c:if>

											</tr>
										</c:forEach>
									</c:forEach>
								</table>

							</div>

						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<c:if test="${allowtoReview}">
			<div align="center">
				<input type="button" id="approveBtn" value="Approve"
					class="btn btn-primary btn-md">&nbsp;&nbsp; <input
					type="button" id="rejectBtn" value="Reject"
					class="btn btn-primary btn-md">
			</div>
		</c:if>

	</div>
	<c:url value="/studyReview/saveRandamizationData" var="gropForm"></c:url>
	<form:form action="${gropForm}" method="POST" id="saveApprData">
		<input type="hidden" id="reviewType" name="reviewType" />
		<input type="hidden" id="reviewComment" name="reviewComment" />
	</form:form>

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
</body>
<script type="text/javascript">
	function submitForm() {
		$('#gropForm').submit();
	}

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

	function animalAccessionCrfsDataReview() {
		$("commentmsg").html("");
		$("#tpwmsg").html("");
		var reviewType = $("#reviewType").val();
		var access = true;
		var comment = $("#rcomment").val().trim();
		if (reviewType = 'Reject') {
			if (comment == '') {
				access = false;
				$("#commentmsg").html("Required Field");
			}
		}
		if (access) {
			var signcheck = asynchronousAjaxCall(mainUrl
					+ "/studyReview/signcheck/" + $("#tpw").val());

			if (signcheck == "yes") {
				debugger;
				$("#reviewComment").val(comment);
				$('#signacherModal').modal('hide');
				$('#saveApprData').submit();
			} else {
				$("#tpw").val("");
				$("#tpwmsg").html("In-valied password");
			}
		}

	}
</script>
</html>