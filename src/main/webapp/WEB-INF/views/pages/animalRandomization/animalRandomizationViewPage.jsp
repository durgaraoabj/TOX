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
			<h3 class="card-title">Animal Randomization Details - ${rendamization}- ${randamizationGender}</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<c:if test="${rendamization}">
				<c:choose>
					<c:when test="${randamizationGender eq 'Male'}">
						<div class="row">
							<div class="column">
								<div
									style="font-weight: bold; font-size: medium; text-align: center;">
									1Study No : ${study.studyNo}<br /> ${randamizationGender}
									Randomization Sheet
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<!--<td>Animal No</td>-->
											<th>Animal Accession No</th>
											<th>Body Weight(g)</th>
										</tr>
										<c:forEach items="${maledto.randamizationAssigndDto}"
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
												<td>${maledto.mean}</td>
											</tr>
											<tr>
												<td>Mean + 20%</td>
												<td>${maledto.maxMean}</td>
											</tr>
											<tr>
												<td>Mean - 20%</td>
												<td>${maledto.minMean}</td>
											</tr>
											<tr>
												<td>Minimum</td>
												<td>${maledto.minWeight}</td>
											</tr>
											<tr>
												<td>Maximum</td>
												<td>${maledto.maxWeight}</td>
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
									Study No : ${study.studyNo}<br /> Randomization Sheet<br />${randamizationGender}
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Animal Sequence No</td>
											<th>Accession No</th>
											<th>Animal No</th>
											<th>Body Weight(g)</th>
											<th>Group</th>
											<th>Mean</th>
											<th>SD</th>
										</tr>
										<c:forEach items="${maledto.randamizationGroupDto}"
											var="groupAnimalList">
											<c:set var="sdstatus" value="true"></c:set>
											<c:forEach
												items="${groupAnimalList.randamizationGroupAnimalDto}"
												var="groupAnimal">
												<tr>
													<td>${groupAnimal.order}</td>
													<td>${groupAnimal.animal.animalNo}</td>
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
					<c:when test="${randamizationGender eq 'Female'}">
						<div class="row">
							<div class="column">
								<div
									style="font-weight: bold; font-size: medium; text-align: center;">
									1Study No : ${study.studyNo}<br /> ${randamizationGender}
									Randomization Sheet
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<!--<td>Animal No</td>-->
											<th>Animal Accession No</th>
											<th>Body Weight(g)</th>
										</tr>
										<c:forEach items="${femaledto.randamizationAssigndDto}"
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
												<td>${femaledto.mean}</td>
											</tr>
											<tr>
												<td>Mean + 20%</td>
												<td>${femaledto.maxMean}</td>
											</tr>
											<tr>
												<td>Mean - 20%</td>
												<td>${femaledto.minMean}</td>
											</tr>
											<tr>
												<td>Minimum</td>
												<td>${femaledto.minWeight}</td>
											</tr>
											<tr>
												<td>Maximum</td>
												<td>${femaledto.maxWeight}</td>
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
									Study No : ${study.studyNo}<br /> Randomization Sheet<br />${randamizationGender}
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Animal Sequence No</td>
											<th>Accession No</th>
											<th>Animal No</th>
											<th>Body Weight(g)</th>
											<th>Group</th>
											<th>Mean</th>
											<th>SD</th>
										</tr>
										<c:forEach items="${femaledto.randamizationGroupDto}"
											var="groupAnimalList">
											<c:set var="sdstatus" value="true"></c:set>
											<c:forEach
												items="${groupAnimalList.randamizationGroupAnimalDto}"
												var="groupAnimal">
												<tr>
													<td>${groupAnimal.order}</td>
													<td>${groupAnimal.animalNo}</td>
													<td>${groupAnimal.animal.animalNo}</td>
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
<!-- 						Male Animals -->
						<div class="row">
							<div class="column">
								<div
									style="font-weight: bold; font-size: medium; text-align: center;">
									1Study No : ${study.studyNo}<br /> Male
									Randomization Sheet
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<!--<td>Animal No</td>-->
											<th>Animal Accession No</th>
											<th>Body Weight(g)</th>
										</tr>
										<c:forEach items="${maledto.randamizationAssigndDto}"
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
												<td>${maledto.mean}</td>
											</tr>
											<tr>
												<td>Mean + 20%</td>
												<td>${maledto.maxMean}</td>
											</tr>
											<tr>
												<td>Mean - 20%</td>
												<td>${maledto.minMean}</td>
											</tr>
											<tr>
												<td>Minimum</td>
												<td>${maledto.minWeight}</td>
											</tr>
											<tr>
												<td>Maximum</td>
												<td>${maledto.maxWeight}</td>
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
											<td>Animal Sequence No</td>
											<th>Accession No</th>
											<th>Animal No</th>
											<th>Body Weight(g)</th>
											<th>Group</th>
											<th>Mean</th>
											<th>SD</th>
										</tr>
										<c:forEach items="${maledto.randamizationGroupDto}"
											var="groupAnimalList">
											<c:set var="sdstatus" value="true"></c:set>
											<c:forEach
												items="${groupAnimalList.randamizationGroupAnimalDto}"
												var="groupAnimal">
												<tr>
													<td>${groupAnimal.order}</td>
													<td>${groupAnimal.animalNo}</td>
													<td>${groupAnimal.animal.animalNo}</td>
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
					
<!-- 					//Female Animals -->
						<div class="row">
							<div class="column">
								<div
									style="font-weight: bold; font-size: medium; text-align: center;">
									1Study No : ${study.studyNo}<br /> Female
									Randomization Sheet
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<!--<td>Animal No</td>-->
											<th>Animal Accession No</th>
											<th>Body Weight(g)</th>
										</tr>
										<c:forEach items="${femaledto.randamizationAssigndDto}"
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
												<td>${femaledto.mean}</td>
											</tr>
											<tr>
												<td>Mean + 20%</td>
												<td>${femaledto.maxMean}</td>
											</tr>
											<tr>
												<td>Mean - 20%</td>
												<td>${femaledto.minMean}</td>
											</tr>
											<tr>
												<td>Minimum</td>
												<td>${femaledto.minWeight}</td>
											</tr>
											<tr>
												<td>Maximum</td>
												<td>${femaledto.maxWeight}</td>
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
									Study No : ${study.studyNo}<br /> Randomization Sheet<br />Female
								</div>
								<div style="height: 450px; overflow: auto">
									<table class="table table-bordered table-striped">
										<tr>
											<td>Animal Sequence No</td>
											<th>Accession No</th>
											<th>Animal No</th>
											<th>Body Weight(g)</th>
											<th>Group</th>
											<th>Mean</th>
											<th>SD</th>
										</tr>
										<c:forEach items="${femaledto.randamizationGroupDto}"
											var="groupAnimalList">
											<c:set var="sdstatus" value="true"></c:set>
											<c:forEach
												items="${groupAnimalList.randamizationGroupAnimalDto}"
												var="groupAnimal">
												<tr>
													<td>${groupAnimal.order}</td>
													<td>${groupAnimal.animalNo}</td>
													<td>${groupAnimal.animal.animalNo}</td>
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
				<div align="center">
					<input type="button" value="Submit" class="btn btn-primary btn-md"
						onclick="submitForm()">
				</div>
			</c:if>
	<c:if test="${rendamization eq false}">
	
	

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
								3Study No : ${study.studyNo}<br /> Randomization Sheet<br />${tempRandamization}
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<td>Animal Sequence No</td>
										<th>Accession No</th>
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
												<td>${groupAnimal.animal.animalId}</td>
												<td>${groupAnimal.animalNo}</td>
												<td>${groupAnimal.animal.animalNo}</td>
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
								4Study No : ${study.studyNo}<br /> ${tempRandamization}
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
								5Study No : ${study.studyNo}<br /> Randomization Sheet<br />${tempRandamization}
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<td>Animal Sequence No</td>
										<th>Accession No</th>
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
												<td>${groupAnimal.animal.animalNo}</td>
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
								3Study No : ${study.studyNo}<br /> Randomization Sheet<br />Male
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<td>Animal Sequence No</td>
										<th>Accession No</th>
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
												<td>${groupAnimal.animal.animalNo}</td>
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
								4Study No : ${study.studyNo}<br /> Female
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
								5Study No : ${study.studyNo}<br /> Randomization Sheet<br />Female
							</div>
							<div style="height: 450px; overflow: auto">
								<table class="table table-bordered table-striped">
									<tr>
										<td>Animal Sequence No</td>
										<th>Accession No</th>
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
												<td>${groupAnimal.animal.animalNo}</td>
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
			</c:if>
		</div>
	</div>
	<c:url value="/animalRadomization/randomizationSendToReview"
		var="gropRandomization"></c:url>
	<form:form action="${gropRandomization}" method="POST" id="gropForm">

	</form:form>
</body>
<script type="text/javascript">
	function submitForm() {
		$('#gropForm').submit();
	}
</script>
</html>