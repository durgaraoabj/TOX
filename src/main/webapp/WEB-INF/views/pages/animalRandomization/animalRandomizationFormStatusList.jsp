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
			<h3 class="card-title">Animal Randomization Details </h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<div class="row">
				<c:if test="${rdmDto.randamizaitonSheetMale ne null}">
					<div class="column">
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet - Male - ${rdmDto.randamizaitonSheetMale.animals.size()}-</th>
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
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
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
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
						<div style="height: 450px; overflow: auto">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2">Randomization Sheet <br /> Male
										(Ascending)
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
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
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
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
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
<!-- 						<div -->
<!-- 							style="font-weight: bold; font-size: medium; text-align: center;">Study -->
<%-- 							No : ${rdmDto.study.studyNo}</div> --%>
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
									<th>Permanent <br />No.
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

				<c:if test="${sendToReview}">
					<div class="column">
						<input type="button" value="Send to Review"
							class="btn btn-primary btn-md" onclick="submitForm()" />
					</div>
				</c:if>

			</div>

		</div>
	</div>

	<c:url value="/animalRadomization/savegenerateGropRandomizationData"
		var="saveGropRandomization"></c:url>
	<form:form action="${saveGropRandomization}" method="POST"
		id="gropForm">
		<input type="hidden" name="studyId" id="studyId" value="${stuydId}">
	</form:form>
</body>

<script type="text/javascript">
	function submitForm() {
		$('#gropForm').submit();
	}
</script>
</html>

