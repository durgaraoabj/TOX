<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var rowsMaxInfo = {};
var rowsMinInfo = {};
var rowsCurrentInfo = {};
var requiredElementIdInJsp = {};
var allElementIdsTypesJspStd = {};
var pattrenIdsAndPattren = {};

var rulesStatus = {};
var rulesField = [];
var rulesFieldOtherValues = [];
var rulesFieldOther = {};
var eleType = {};


var caliculationFieldSec  = {};

var studyDates = [];

var subGroupInfoId = "";
var crf = "";
var frElements = [];
var availableTags = [];
var autocompleteFields = {};
</script>
</head>
<body>
	<c:forEach items="${staticData}" var="sd">
		<script type="text/javascript">availableTags.push('${sd.code},${sd.sign},${sd.description}')</script>
	</c:forEach>

	<input type="hidden" name="crf" id="crf" value="${crf.id }" />
	<input type="hidden" name="crfCode" id="crfCode" value="${crf.crfCode}">
	<div class="box-header with-border">
		<div id="formulaMsg"
			style="color: red; text-align: center; font-size: medium; font-weight: bold;"></div>
		<table class="table table-bordered table-striped" style="width: 100%">
			<tr>
				<td>Study : ${sm.studyNo }</td>
				<td colspan="3">Accession Animal Data Entry For : ${crf.name}</td>
				<td><div>
						<input type="button" value="Get Formulas"
							class="btn btn-danger btn-sm"
							onclick="generateFormulas('${crf.id}', '${animal.id}')">
					</div></td>
			</tr>
			<tr>
				<c:choose>
					<c:when test="${type eq 'unscheduled'}">
						<td>Select Animal :</td>
						<td>
							<div style="width: 65%;">
								<select name="animalVal" id="animalVal" class="form-control"
									onchange="animalNumberFunction('animalVal', 'animalValMsg')">
									<option value="">----Select----</option>
									<c:forEach items="${unscheduledAnimals}" var="ani">
										<option value="${ani.id}">${ani.animalNo}</option>
									</c:forEach>
								</select>
							</div>
							<div id="animalValMsg" style="color: red;"></div>
						</td>
					</c:when>
					<c:otherwise>
						<td colspan="2">Animal No : ${animal.animalNo}</td>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
	</div>
	<div class="box-body">
		<%-- 		<c:url value="/studyexe/studyCrfSave" var="studyCrfSave" /> --%>
		<c:url value="/accession/saveStudyAccessionCrfData"
			var="saveStudyAccCrf" />
		<sf:form action="${saveStudyAccCrf}" method="POST"
			modelAttribute="crfpojo" id="studyCrfSave"
			enctype="multipart/form-data">
			<input type="hidden" name="studyAcclamatizationDateId"
				id="studyAcclamatizationDateId"
				value="${studyAcclamatizationDateId}" />
			<input type="hidden" name="studyAcclamatizationDataId"
				id="studyAcclamatizationDataId"
				value="${studyAcclamatizationDataId}" />
			<input type="hidden" name="selecteDate" id="selecteDate"
				value="${selecteDate}" />
			<input type="hidden" name="noOfEntry" id="noOfEntry"
				value="${noOfEntry}" />
			<input type="hidden" name="seletedGender" id="seletedGender"
				value="${seletedGender}" />
			<input type="hidden" name="discrebencyFields" id="discrebencyFields"
				value="" />
			<input type="hidden" name="stdSubGroupObservationCrfsId"
				id="stdSubGroupObservationCrfsId"
				value="${stdSubGroupObservationCrfsId}">
			<input type="hidden" name="deviationMessage" id="deviationMessage"
				value="">
			<input type="hidden" name="frequntlyMessage" id="frequntlyMessage"
				value="">
			<input type="hidden" name="crfId" id="crfId" value="${crf.id }">
			<input type="hidden" name="animalId" id="animalId"
				value="${animal.id}">
			<input type="hidden" name="studyId" id="studyId" value="${sm.id}">
			<input type="hidden" name="formulaVals" id="formulaVals" value="0">
			<input type="hidden" name="type" id="type" value="${type}">
			<c:forEach items="${crf.sections}" var="sec">
				<c:if test="${sec.active}">
					<c:choose>
						<c:when test="${sec.allowDataEntry}">
							<c:set var="secview">yes</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="secview">no</c:set>
						</c:otherwise>
					</c:choose>
					<c:if test="${sec.containsGroup eq false}">
						<c:set var="srows">${sec.maxRows}</c:set>
						<c:set var="scol">${sec.maxColumns}</c:set>
						<table class="table table-bordered table-striped"
							style="width: 100%">
							<c:if test="${sec.hedding ne ''}">
								<tr>
									<td colspan="${scol}"><b>${sec.hedding} </b></td>
								</tr>
							</c:if>
							<c:if test="${sec.subHedding ne ''}">
								<tr>
									<td colspan="${scol}"><b>${sec.subHedding}</b></td>
								</tr>
							</c:if>
							<c:forEach begin="1" step="1" end="${srows}" var="row">
								<tr>
									<c:forEach begin="1" step="1" end="${scol}" var="col">
										<td><c:set var="itemkey">${sec.name},${row},${col}</c:set>
											<c:forEach items="${sec.element}" var="item">
												<c:set var="itemkey">${item.id}_${item.name}</c:set>
												<c:if test="${item.rowNo eq row }">
													<c:if test="${item.columnNo eq col }">
														<table class="table table-bordered table-striped">
															<c:if test="${item.topDesc ne ''}">
																<tr>
																	<td colspan="3">${item.topDesc}</td>
																</tr>
															</c:if>
															<tr>
																<c:if test="${item.totalDesc ne ''}">
																	<td colspan="3">${item.totalDesc}</td>
																</c:if>
																<c:if test="${item.totalDesc eq ''}">
																	<c:if test="${item.leftDesc ne ''}">
																		<td>${item.leftDesc}</td>
																	</c:if>
																	<td><c:choose>
																			<c:when test="${item.type eq 'staticData'}">
																				<c:if test="${secview eq 'yes'}">
																					<textarea class="form-control" rows="5" cols="30"
																						name="${itemkey}" id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')"></textarea>
																					<script type="text/javascript">
																						$(function() {
																						    $( "#${itemkey}" ).autocomplete({
																						      source: availableTags
																						    });
																						  });
																					</script>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<input type="text" class="form-control" value=""
																						name="${itemkey}" id="${itemkey}" />
																				</c:if>
																			</c:when>
																			<c:when test="${item.type eq 'time'}">
																				<c:if test="${secview eq 'yes'}">
																					<div class="input-group clockpicker-with-callbacks">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}"
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')"
																							class="form-control" autocomplete="off"
																							placeholder="time" />
																					</div>
																					<font id="${itemkey}_msg" color="red"></font>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<input type="text" class="form-control" value=""
																						name="${itemkey}" id="${itemkey}" />
																				</c:if>
																			</c:when>
																			<c:when test="${item.type eq 'text'}">
																				<c:if test="${secview eq 'yes'}">
																					<c:if test="${item.pattren eq ''}">
																						<c:choose>
																							<c:when test="${item.clinicalCode ne ''}">
																								<input type="text" class="form-control" value=""
																									name="${itemkey}" id="${itemkey}"
																									onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text');addInDesc(this.value, '${itemkey}_desc');"
																									onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																									onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																								<div id="${itemkey}_desc"></div>
																								<script type="text/javascript">
																								autocompleteFields['${itemkey}'] = '${item.clinicalCode}';
																							</script>
																							</c:when>
																							<c:otherwise>

																								<input type="text" class="form-control"
																									value="${item.values}" name="${itemkey}"
																									id="${itemkey}"
																									onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																									onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																									onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																							</c:otherwise>
																						</c:choose>


																					</c:if>
																					<c:if test="${item.pattren ne ''}">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}"
																							placeholder="${item.pattren }"
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<input type="text" class="form-control" value=""
																						name="${itemkey}" id="${itemkey}" />
																				</c:if>
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'textArea'}">
																				<c:if test="${secview eq 'yes'}">
																					<c:if test="${item.pattren eq ''}">
																						<textarea class="form-control" rows="3" cols="10"
																							name="${itemkey}" id="${itemkey}"
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')"></textarea>
																					</c:if>
																					<c:if test="${item.pattren ne ''}">
																						<textarea class="form-control" rows="3" cols="10"
																							name="${itemkey}" id="${itemkey}" placeholder=""
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')"></textarea>
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<textarea class="form-control" rows="3" cols="10"
																						name="${itemkey}" id="${itemkey}"></textarea>
																				</c:if>

																				<%-- 																				<font id="${itemkey}_msg" color="red"></font> --%>
																			</c:when>
																			<c:when test="${item.type eq 'radio'}">
																				<c:if test="${secview eq 'yes'}">
																					<c:if test="${item.display eq 'horizantal'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="radio" name="${itemkey}"
																								id="${itemkey}"
																								onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																								value="${itemval.value}"> ${itemval.value}
																						</c:forEach>
																					</c:if>
																					<c:if test="${item.display eq 'vertical'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="radio" name="${itemkey}"
																								id="${itemkey}"
																								onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																								value="${itemval.value}"> ${itemval.value}<br />
																						</c:forEach>
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<c:if test="${item.display eq 'horizantal'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="radio" name="${itemkey}"
																								id="${itemkey}" value="${itemval.value}"> ${itemval.value}
																						</c:forEach>
																					</c:if>
																					<c:if test="${item.display eq 'vertical'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="radio" name="${itemkey}"
																								id="${itemkey}" value="${itemval.value}"> ${itemval.value}<br />
																						</c:forEach>
																					</c:if>
																				</c:if>
																				<%-- 																				<font id="${itemkey}_msg" color="red"></font> --%>
																			</c:when>
																			<c:when test="${item.type eq 'checkBox'}">
																				<c:if test="${secview eq 'yes'}">
																					<c:if test="${item.display eq 'horizantal'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="checkbox" name="${itemkey}"
																								id="${itemkey}"
																								onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																								value="${itemval.value}"> ${itemval.value}
																					</c:forEach>
																					</c:if>
																					<c:if test="${item.display eq 'vertical'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="checkbox" name="${itemkey}"
																								id="${itemkey}"
																								onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																								value="${itemval.value}"> ${itemval.value}<br>
																						</c:forEach>
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<c:if test="${item.display eq 'horizantal'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="checkbox" name="${itemkey}"
																								id="${itemkey}" value="${itemval.value}"> ${itemval.value}
																					</c:forEach>
																					</c:if>
																					<c:if test="${item.display eq 'vertical'}">
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<input type="checkbox" name="${itemkey}"
																								id="${itemkey}" value="${itemval.value}"> ${itemval.value}<br>
																						</c:forEach>
																					</c:if>
																				</c:if>
																				<%-- 																				<font id="${itemkey}_msg" color="red"></font> --%>
																			</c:when>
																			<c:when test="${item.type eq 'select'}">
																				<c:if test="${secview eq 'yes'}">
																					<select class="form-control" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'select')">
																						<option value="-1">--Select--</option>
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<option value="${itemval.value}">${itemval.value}</option>
																						</c:forEach>
																					</select>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<select class="form-control" name="${itemkey}"
																						id="${itemkey}">
																						<option value="-1">--Select--</option>
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<option value="${itemval.value}">${itemval.value}</option>
																						</c:forEach>
																					</select>
																				</c:if>
																				<%-- 																				<font id="${itemkey}_msg" color="red"></font> --%>
																			</c:when>
																			<c:when test="${item.type eq 'selectTable'}">selectTable
																				<c:if test="${secview eq 'yes'}">
																					<select class="form-control" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'select')">
																						<option value="-1">--Select--</option>
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<option value="${itemval.value}">${itemval.value}</option>
																						</c:forEach>
																					</select>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<select class="form-control" name="${itemkey}"
																						id="${itemkey}">
																						<option value="-1">--Select--</option>
																						<c:forEach items="${item.elementValues}"
																							var="itemval">
																							<option value="${itemval.value}">${itemval.value}</option>
																						</c:forEach>
																					</select>
																				</c:if>
																				<%-- 																				<font id="${itemkey}_msg" color="red"></font> --%>
																			</c:when>
																			<c:when test="${item.type eq 'date'}">
																				<c:if test="${secview eq 'yes'}">
																					<input type="text" class="form-control input-sm"
																						value="" name="${itemkey}" id="${itemkey}"
																						autocomplete="off"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'date')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																					<script type="text/javascript">
																							$(function(){
																								$("#${itemkey}").datepicker({
																									dateFormat: $("#dateFormatJsp").val(),
																									changeMonth:true,
																									changeYear:true
																								});
																							});
																						</script>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<input type="text" class="form-control input-sm"
																						value="" name="${itemkey}" id="${itemkey}"
																						autocomplete="off" />
																				</c:if>

																			</c:when>
																			<c:when test="${item.type eq 'datetime'}">
																				<c:if test="${secview eq 'yes'}">
																					<c:if test="${item.pattren eq ''}">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}"
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																					</c:if>
																					<c:if test="${item.pattren ne ''}">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}"
																							placeholder="${item.pattren }"
																							onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')"
																							onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																							onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>
																				<c:if test="${secview ne 'yes'}">
																					<c:if test="${item.pattren eq ''}">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}" />
																					</c:if>
																					<c:if test="${item.pattren ne ''}">
																						<input type="text" class="form-control" value=""
																							name="${itemkey}" id="${itemkey}" />
																					</c:if>
																					<%-- 																					<font id="${itemkey}_msg" color="red"></font> --%>
																				</c:if>

																			</c:when>
																			<c:otherwise>
																				<%-- 																			${item.type} --%>
																			</c:otherwise>
																		</c:choose> <font id="${itemkey}_msg" color="red"></font></td>
																	<c:if test="${item.rigtDesc ne ''}">
																		<td>${item.rigtDesc}</td>
																	</c:if>
																</c:if>
															</tr>
															<c:if test="${item.bottemDesc ne ''}">
																<tr>
																	<td colspan="3">${item.topDesc}</td>
																</tr>
															</c:if>
														</table>
													</c:if>
												</c:if>
											</c:forEach></td>
									</c:forEach>
								</tr>
							</c:forEach>
						</table>
					</c:if>
					<c:if test="${sec.containsGroup}">
						<c:set var="secname">${sec.name}</c:set>
						<c:set var="grows">${sec.group.maxRows}</c:set>
						<c:if test="${sec.group.displayedRows eq 0}">
							<c:set var="gminrows">${sec.group.minRows}</c:set>
						</c:if>
						<c:if test="${sec.group.displayedRows ne 0}">
							<c:set var="gminrows">${sec.group.displayedRows}</c:set>
						</c:if>
						<c:set var="gcol">${sec.group.maxColumns}</c:set>
						<script type="text/javascript">
							rowsMaxInfo['${sec.group.id}'] = ${grows};
							rowsMinInfo['${sec.group.id}'] = ${gminrows};
							rowsCurrentInfo['${sec.group.id}'] = ${gminrows};
						</script>
						<table class="table table-bordered table-striped"
							id="group_${sec.group.id}">
							<thead>
								<c:if test="${sec.hedding ne ''}">
									<tr>
										<td colspan="${gcol}"><b>${sec.hedding}</b></td>
									</tr>
								</c:if>
								<c:if test="${sec.subHedding ne ''}">
									<tr>
										<td colspan="${gcol}"><b>${sec.subHedding}</b></td>
									</tr>
								</c:if>
								<c:if test="${sec.group.hedding ne ''}">
									<tr>
										<td colspan="${gcol}"><b>${sec.group.hedding}</b></td>
									</tr>
								</c:if>
								<c:if test="${sec.group.subHedding ne ''}">
									<tr>
										<td colspan="${gcol}"><b>${sec.group.subHedding}</b></td>
									</tr>
								</c:if>
								<tr>
									<td colspan="${gcol}"><c:if test="${secview eq 'yes'}">
											<input type="button" value="Add" id="addid"
												onclick="addGroup('${sec.group.id}')">
											<input type="button" value="Remove" id="removeid"
												onclick="removeGroup('${sec.group.id}')">
										</c:if> <c:if test="${secview ne 'yes'}">
											<input type="button" value="Add" id="addid">
											<input type="button" value="Remove" id="removeid">
										</c:if></td>
								</tr>
							</thead>
							<tbody>
								<tr id="fixed">
									<c:forEach items="${sec.group.element}" var="item">
										<td><b>${item.title}</b></td>
									</c:forEach>
								</tr>
								<c:forEach begin="1" step="1" end="${gminrows}" var="row">
									<tr id="fixed">
										<c:forEach begin="1" step="1" end="${gcol}" var="col">
											<td><c:forEach items="${sec.group.element}" var="item">
													<c:set var="itemkey">g_${item.id}_${item.name}_${row}</c:set>
													<c:set var="itemname">${item.name}</c:set>
													<c:if test="${item.columnNo eq col }">
														<c:choose>
															<c:when test="${item.type eq 'time'}">
																<c:if test="${secview eq 'yes'}">
																	<div class='input-group clockpicker-with-callbacks'>
																		<input type="text" class="form-control" value=""
																			name="${itemkey}" id="${itemkey}"
																			class="form-control" autocomplete="off"
																			placeholder="time" />
																	</div>
																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<input type="text" class="form-control" value=""
																		name="${itemkey}" id="${itemkey}" />
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'text'}">
																<c:if test="${secview eq 'yes'}">
																	<c:if test="${item.pattren eq ''}">
																		<input type="text" class="form-control" value=""
																			name="${itemkey}" id="${itemkey}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${item.id}','${itemkey}')"
																			onfocus="checkCaliculationg(${item.id}','${itemkey}')" />
																	</c:if>
																	<c:if test="${item.pattren ne ''}">
																		<input type="text" class="form-control" value=""
																			name="${itemkey}" id="${itemkey}"
																			placeholder="${item.pattren }"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																			onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')" />
																	</c:if>
																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<input type="text" class="form-control" value=""
																		name="${itemkey}" id="${itemkey}" />
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'textArea'}">
																<c:if test="${secview eq 'yes'}">
																	<c:if test="${item.pattren eq ''}">
																		<textarea class="form-control" rows="3" cols="10"
																			name="${itemkey}" id="${itemkey}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea' , '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																			onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"></textarea>
																	</c:if>
																	<c:if test="${item.pattren ne ''}">
																		<textarea class="form-control" rows="3" cols="10"
																			name="${itemkey}" id="${itemkey}"
																			placeholder="${item.pattren }"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea', '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																			onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"></textarea>
																	</c:if>
																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<textarea class="form-control" rows="3" cols="10"
																		name="${itemkey}" id="${itemkey}"></textarea>
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'radio'}">
																<c:if test="${item.display eq 'horizantal'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}"
																				value="${itemval.value}"
																				onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio', '${itemname}', '${row}')"> ${itemval.value}
																		</c:if>
																		<c:if test="${secview ne 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}"
																				value="${itemval.value}"> ${itemval.value}
																		</c:if>
																	</c:forEach>
																</c:if>
																<c:if test="${item.display eq 'vertical'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}"
																				value="${itemval.value}"
																				onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio', '${itemname}', '${row}')"> ${itemval.value}<br />
																		</c:if>
																		<c:if test="${secview ne 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}"
																				value="${itemval.value}"> ${itemval.value}<br />
																		</c:if>
																	</c:forEach>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'checkBox'}">
																<c:if test="${item.display eq 'horizantal'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<c:if test="${secview eq 'yes'}">
																			<input type="checkbox" name="${itemkey}"
																				id="${itemkey}" value="${itemval.value}"
																				onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox', '${itemname}', '${row}')"> ${itemval.value}
																		</c:if>
																		<c:if test="${secview ne 'yes'}">
																			<input type="checkbox" name="${itemkey}"
																				id="${itemkey}"> ${itemval.value}
																		</c:if>
																	</c:forEach>
																</c:if>
																<c:if test="${item.display eq 'vertical'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<c:if test="${secview eq 'yes'}">
																			<input type="checkbox" name="${itemkey}"
																				id="${itemkey}" value="${itemval.value}"
																				onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox', '${itemname}', '${row}')"> ${itemval.value}<br>
																		</c:if>
																		<c:if test="${secview ne 'yes'}">
																			<input type="checkbox" name="${itemkey}"
																				id="${itemkey}" value="${itemval.value}"> ${itemval.value}<br>
																		</c:if>
																	</c:forEach>
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'select'}">
																<c:if test="${secview eq 'yes'}">
																	<select class="form-control" name="${itemkey}"
																		id="${itemkey}"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'select')">
																		<option value="-1">--Select--</option>
																		<c:forEach items="${item.elementValues}" var="itemval">
																			<option value="${itemval.value}">${itemval.value}</option>
																		</c:forEach>
																	</select>
																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<select class="form-control" name="${itemkey}"
																		id="${itemkey}">
																		<option value="-1">--Select--</option>
																		<c:forEach items="${item.elementValues}" var="itemval">
																			<option value="${itemval.value}">${itemval.value}</option>
																		</c:forEach>
																	</select>
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'date'}">
																<c:if test="${secview eq 'yes'}">
																	<input type="text" class="form-control" value=""
																		name="${itemkey}" id="${itemkey}" autocomplete="off"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'date', '${itemname}', '${row}')"
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')" />
																	<script type="text/javascript">
																							$(function(){
																								$("#${itemkey}").datepicker({
																									dateFormat: $("#dateFormatJsp").val(),
																									changeMonth:true,
																									changeYear:true
																								});
																							});
																						</script>

																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<input type="text" class="form-control" value=""
																		name="${itemkey}" id="${itemkey}" />
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'datetime'}">
																<c:if test="${secview eq 'yes'}">
																	<c:if test="${item.pattren eq ''}">
																		<input type="text" class="form-control" value=""
																			name="${itemkey}" id="${itemkey}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																			onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')" />
																	</c:if>
																	<c:if test="${item.pattren ne ''}">
																		<input type="text" class="form-control" value=""
																			name="${itemkey}" id="${itemkey}"
																			placeholder="${item.pattren }"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')"
																			onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																			onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')" />
																	</c:if>
																	<font id="${itemkey}_msg" color="red"></font>
																</c:if>
																<c:if test="${secview ne 'yes'}">
																	<input type="text" class="form-control" value=""
																		name="${itemkey}" id="${itemkey}" />
																</c:if>
															</c:when>
															<c:otherwise>
																<%-- 															${item.type} --%>
															</c:otherwise>
														</c:choose>
													</c:if>
												</c:forEach></td>
										</c:forEach>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
				</c:if>
			</c:forEach>
			<input type="hidden" name="groupCountInfo" id="groupCountInfo" />
			<br />
			<div style="text-align: center">
				<%-- 				<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}"> --%>
				<!-- 								<input type="checkbox" name="inReview" id="inReview" -->
				<!-- 									value="IN REVIEW">Sent to Review  -->
				<input type="button" value="SAVE" id="formSubmitBtn"
					class="btn btn-primary">
				<%-- 				</c:if> --%>
				<%-- <a
					href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${vol.id}/${period.id}" />'
					class="btn btn-primary">EXIT</a> --%>
				<a href="#" class="btn btn-primary" onclick="redirctPageFunction()">EXIT</a>
			</div>
		</sf:form>
	</div>


	<c:forEach items="${requiredElementIdInJsp}" var="v">
		<script type="text/javascript">requiredElementIdInJsp['${v.key}']='${v.value}'</script>
	</c:forEach>
	<c:forEach items="${allElementIdsTypesJspStd}" var="v">
		<script type="text/javascript">allElementIdsTypesJspStd['${v.key}']='${v.value}'</script>
	</c:forEach>
	<c:forEach items="${pattrenIdsAndPattren}" var="v">
		<script type="text/javascript">pattrenIdsAndPattren['${v.key}']='${v.value}'</script>
	</c:forEach>

	<c:forEach items="${rulesFieldAll}" var="v">
		<script type="text/javascript">
			rulesField.push('${v.key}');
			rulesStatus['${v.key}'] = false;
			rulesFieldOtherValues = [];
		</script>
		<c:forEach items="${v.oeles}" var="oele">
			<script type="text/javascript">
			rulesFieldOtherValues.push('${oele}');</script>
		</c:forEach>
		<script type="text/javascript">rulesFieldOther['${v.key}']=rulesFieldOtherValues;</script>
		<c:forEach items="${v.eleType}" var="etype">
			<script type="text/javascript">eleType['${etype.key}']='${etype.value}';</script>
		</c:forEach>
	</c:forEach>

	<c:forEach items="${caliculationFieldSec}" var="v">
		<script type="text/javascript">caliculationFieldSec['${v.key}']='${v.value}'</script>
	</c:forEach>

	<c:forEach items="${weightData}" var="v">
		<script type="text/javascript">
// 				alert('${v.key} ---- ${v.value}')
				var va = $("#${v.key}").val();
				if(va != undefined){
					$("#${v.key}").val('${v.value}');
					$("#${v.key}").attr("readonly", true); 
				}
			</script>
	</c:forEach>
	<c:forEach items="${frEleList}" var="fre">
		<script type="text/javascript">
				frElements.push('${fre}');
		</script>
	</c:forEach>

	<c:url value="/studyExecution/subgroupSubjects" var="dataEntryForm"></c:url>
	<sf:form action="${dataEntryForm}" method="POST" id="dataEntryFromPage">
		<input type="hidden" name="subGroupId" id="subGroupId"
			value="${subGroupId }">
		<input type="hidden" name="type" id="type" value="${type }">
	</sf:form>
	<div id="ww"></div>
	<div id="calcForm"></div>
</body>
<script type="text/javascript">
var collectionCodesIdObj = {};
$(document).ready(function() {	
	var result = asynchronousAjaxCall(mainUrl+"/studyExecution/clinicalCodes");
	if(result != '' || result != 'undefined'){
		debugger;
		$.each(result, function(key, obj){
				console.log(obj.id+"  - " +obj)
				collectionCodesIdObj[obj.id] = obj;		
		});
	}
	
	$.each(autocompleteFields, function(itemkey, collectionCodeName){
		if(collectionCodeName == "Animal Number"){
			$("#"+itemkey).autocomplete({
			    source: function( request, response ) {
			    $.ajax( {
			    	url: mainUrl+"/studyExecution/animalNumber/"+ request.term+"/Acclamatization",
			        success: function( data ) {
			        	response($.map(data, function (item) {
			                return {
			                	label: item,
			                    value: item
			                };
			            }));
			        }
			      });
			    },
			    select: function( event, ui ) {
			    }
			  });
		}else{
			$("#"+itemkey).autocomplete({
			    source: function( request, response ) {
			    $.ajax( {
			    	url: mainUrl+"/studyExecution/clinicalCodesWithCode/" +collectionCodeName+"/"+ request.term,
			        success: function( data ) {
			        	response($.map(data, function (item) {
			                return {
			                	label: item.clinicalCode + " - " + item.clinicalSign + " - " + item.description,
			                    value: item.id
			                };
			            }));
			        }
			      });
			    },
			    select: function( event, ui ) {
			    }
			  });

		}

		
	});	
});
function addInDesc(value, id){
	var item = collectionCodesIdObj[value];
	$("#"+id).html(item.clinicalCode + " - " + item.clinicalSign + " - " + item.description);
}


function redirctPageFunction(){
// 	if(subgId != null && subgId != "" && subgId != "undefined"){
// 		$('#subGroupId').val(subgId);
		$('#dataEntryFromPage').submit();
// 	}
}
</script>
<script type="text/javascript">
var subGroupInfoId = "";
var crf = "";

function textOnChange( dbid, id, value , eletype){
	$("#"+id+"_msg").html("");
	rulesStatus[id] = false;
// 	alert(dbid + " : " + id + " : " + value + " : " + eletype+"  ggg");
// ('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
	var dp = true;
	if(value.trim() != ''){
		var flag  = false;
		//check dbid abilabe in map, if-Yes -> resval= currentCrf reamin ids[id_name]
		$.each(rulesField, function(k, v) {
			if(v == id){
				flag = true;
				return false;
			}
		});
		var f = true;
		if(flag){
			var stringbuilder = true;
			var values = "";
			var rulesFieldOtherValues = rulesFieldOther[id];
			$.each(rulesFieldOtherValues, function(k, v) {
				var value = $("#"+v).val();
// 				alert(eleType[v])
				if(eleType[v] == 'checkBox' || eleType[v] == 'radio')
					value = $('input[name='+v+']:checked').val();
				if(typeof(value) == 'undefined' || value == null)
					value = "";
				if(value != ""){
					if(stringbuilder){
						values = v + "@@" + value; 
						stringbuilder= false;
					}else
						values = values + "@@@" + v + "@@" + value;	
				}
			});
				if(values == '') values = 'nill';
// 				alert(mainUrl+"/studyexe/crfEleRuelCheck/"+$("#crfId").val()+"/"+dbid+"/"+id+"/"+value+"/"+values)
// 				var result = asynchronousAjaxCall(mainUrl+"/crfsample/crfEleRuelCheck/"+$("#crfId").val()+"/"+$("#test").val()+"/"+dbid+"/"+id+"/"+value+"/"+values);
				var result = asynchronousAjaxCall(mainUrl+"/studyexe/crfEleRuelCheck/"+$("#crfId").val()+"/"+dbid+"/"+id+"/"+value+"/"+values);
				if(result != '' || result != 'undefined'){
					result = result.trim();
					if(result != ''){
// 						alert(result);
						$("#"+id+"_msg").html(result);
						rulesStatus[id] = true;
// 						alert("sdf")	
					}else{
						$("#"+id+"_msg").html();
						rulesStatus[id] = false;
					}
				}else{
					$("#"+id+"_msg").html(result);
					rulesStatus[id] = false;
				}
		}
	}
}
function textOnChangeSec( dbid, id, value , eletype){
	textOnChange(dbid, id, value , eletype);
}
function textOnChangeGroup( dbid, id, value , eletype, ele, row){
	textOnChange(dbid, id, value , eletype);
}


</script>



<script type="text/javascript">
function pattrenValidation(){
	var errorCount = 0;
	$.each(pattrenIdsAndPattren, function(k, val) {
		if(val != ''){
			$("#"+k+"_msg").html("");
			var v = allElementIdsTypesJspStd[k];
			if(v == 'text' || v == 'textArea' || v == 'datetime'){
				if($("#"+k).val() != undefined){
					var flag = 0;
					var compareVal = $("#"+k).val();
					if(val.length == compareVal.length){
						for(var i = 0; i< val.length; i++){
							if(val[i] == 'A'){
								if (compareVal[i].match(/^[a-zA-Z]+$/) == null){
									flag = 1;
								}
							}else if(val[i] == 'N'){
								if (compareVal[i].match(/^[0-9]+$/) == null){
									flag = 1;
								}
							}else{
								if(val[i] != compareVal[i]){
									flag = 1;
								}
							}
						}
						if(flag === 1){
							$("#"+k+"_msg").html("Pattren Field. Required : " + val);
							errorCount ++;
						}
						
					}else{
						$("#"+k+"_msg").html("Pattren Field. Required : " + val);
						errorCount ++;
					}
				}
			}
		}
	});		
	return errorCount;
}
</script>
<script type="text/javascript">
function requiredValidation(){
	var errorCount = 0;
	$.each(requiredElementIdInJsp, function(k, v) {
		if(v != ''){
			if(v == 'text' || v == 'textArea' || v == 'date'|| v == 'datetime'){
				if($("#"+k).val() != undefined){
					if($("#"+k).val().trim()  == ''){
// 						alert(k)
						$("#"+k+"_msg").html("Required Field");
						errorCount += 1;
					}else{
						$("#"+k+"_msg").html("");
					}					
				}
			}
// 			else if(v == 'select'){
// 				if($("#"+k).val() != undefined){
// 					if($("#"+k).val() == '-1'){
// 						$("#"+k+"_msg").html("Required Field");
// 						errorCount += 1;
// 					}else{
// 						$("#"+k+"_msg").html("");
// 					}
// 				}
// 			}
			else if(v == 'radio' || v == 'checkBox'){
				if($("#"+k).val() != undefined){
				var nn ="input[name='"+k+"']:checked";
				if($(nn).length != undefined){
					if ($(nn).length === 0) {
// 						alert(k)
						$("#"+k+"_msg").html("Required Field");
						errorCount += 1;
					}else{
						$("#"+k+"_msg").html("");
					}					
				}
				}
			}				
		}
	});		
	return errorCount;
}
</script>
<script type="text/javascript">
function addGroup(ele){
	if(rowsCurrentInfo[ele] < rowsMaxInfo[ele]){
		var groupRowId = rowsCurrentInfo[ele];
		var ide = "#group_"+ele+" tbody";
		var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfGroupElement/"+ele+"/"+(groupRowId+1));
		if(result != '' || result != 'undefined'){
			var reuiedEleIdsresult = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfGroupElementReuiedEleIds/"+ele+"/"+(groupRowId+1));
			if(reuiedEleIdsresult != null || result != 'undefined'){
				//requiredElementIdInJsp-----['${v.key}']='${v.value}'
				var array = reuiedEleIdsresult.split("#");
				reuiedEleIdsresult.split("#").forEach(function (item, index) {
					var ca = item.split("@");
					requiredElementIdInJsp[ca[0]] = ca[1];
				});
			}
			groupRowId ++;
			rowsCurrentInfo[ele] = groupRowId;
			$(ide).append("<tr id=AddRowgroup"+ele+"_"+groupRowId+">"+result+"</tr>");
		}		
	}else{
		alert("Reaches Max rows");
	}
}
function removeGroup(ele){
	var groupRowId = rowsCurrentInfo[ele];
	if(rowsMinInfo[ele] < groupRowId ){
		$('#AddRowgroup'+ele+'_'+groupRowId).remove();
		groupRowId --;
		rowsCurrentInfo[ele] = groupRowId;
	}else{
		alert("No more elements to remove");
	}
}	


function checkCaliculation(id, name){
	
	var resultFiled = name.replace(id+"_", "");
	var flag  = false;
	var value = "";
	$.each(caliculationFieldSec, function(k, v) {
		if(k  == resultFiled){
			flag= true;
			value = v.split(",");
		}
			
	});
	
	if(flag){
		var fieldAndVales = "";
		var flag2 = true;
		for(var i=0; i<value.length; i++)
			if(flag2){
				fieldAndVales = value[i] +"-"+$("#"+value[i]).val();
				flag2 = false;
			}else
				fieldAndVales = fieldAndVales + "," + value[i] +"-"+$("#"+value[i]).val();
		
		var crfid = $("#crfId").val();
// 		alert(mainUrl+"/studyexe/studyCrfElementCalculationValue/"+$("#crfId").val()+"/"+resultFiled+"/"+fieldAndVales)
// 		var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfElementCalculationValue/"+$("#crfId").val()+"/"+resultFiled+"/"+fieldAndVales);
		var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfElementCalculationValue/"+$("#crfId").val()+"/"+resultFiled+"/"+fieldAndVales);
		if(result != '' || result != 'undefined'){
			$("#"+name).val(result);
			nameValue[resultFiled] = result;
		}
	}
	
}
function checkCaliculationg(row,id,elename, name){
	var resultFiled = elename;
	var flag  = false;
	var value = "";
	$.each(caliculationFieldSec, function(k, v) {
		if(k  == resultFiled){
			flag= true;
			value = v.split(",");
		}	
	});
	if(flag){
		var fieldAndVales = "";
		var flag2 = true;
		for(var i=0; i<value.length; i++){
			if(flag2){
				fieldAndVales = value[i] +"-"+groupNameValue["g_"+value[i]+"_"+row];
				flag2 = false;
			}else
				fieldAndVales = fieldAndVales + "," + value[i] +"-"+groupNameValue["g_"+value[i]+"_"+row];
		
		}
		var crfid = $("#crfId").val();
		var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfElementCalculationValue/"+$("#crfId").val()+"/"+resultFiled+"/"+fieldAndVales);
		if(result != '' || result != 'undefined'){
			$("#"+name).val(result);
			groupNameValue["g_"+resultFiled+"_"+row] = result;
		}
	}
	
}

</script>

<script type="text/javascript">
var ruleErrorMessage = 1;
	$('#formSubmitBtn').click(function(){
		$("#discrebencyFields").val("");
		var pattrenFlag  = pattrenValidation();
		if(pattrenFlag > 0)
			alert("No of Patterens Faild :  "+pattrenFlag);
// 		else{
			var requiredFlag  = requiredValidation();
			if(requiredFlag == 0){
				var ruleFlag = false; 
				requiredFlag = 0; 
				$.each(rulesStatus, function(k, v) {
					if(v){
// 						alert(k);
						ruleFlag = true;
						requiredFlag++;
					}		
				});
				if(ruleFlag){
					if(confirm("Crf Contains "+requiredFlag+" Fields Rules violated.\nDo You Want To Continue With Discrebency?")){
						if(ruleErrorMessage == 2){
							var c = "";
							var cflag = false;
							$.each(rulesStatus, function(k, v) {
								if(v){
									if(cflag)
										c = c + "," + k;
									else{a
										c = k; cflag = true;
									}
								}		
							});
							$("#discrebencyFields").val(c);
							finalSubmit();
						}else{
							ruleErrorMessage++;
						}
						
					}
				}else{
					finalSubmit();
				}

			}else{
				alert("Crf Contains "+requiredFlag+" Required Fields");
			}
							
// 		}

	});
	
	function finalSubmit(){
		
        
        var groupInfo = "";
    	var i = 0;
		$.each(rowsCurrentInfo, function (key, value) {
			if(i == 0 ){
				groupInfo = key + "," + value;
			}else
				groupInfo = groupInfo + "@" + key + "," + value;	
			i++;
        });
		$("#groupCountInfo").val(groupInfo);
		
			var type = $('#type').val();
			var animalFlag = true;
			if(type == "unscheduled")
				animalFlag = animalNumberFunction('animalVal', 'animalValMsg');
			if(animalFlag)
        		$('#studyCrfSave').submit();
		
	}
</script>
<script type="text/javascript">
/* function generateFormulas(crfId, animalId){
	var result = asynchronousAjaxCall(mainUrl+"/formCalculation/getFormulaCalculationData/"+crfId+"/"+animalId);
	if(result != '' || result != 'undefined'){
		$('#calcForm').html(result);
	    if(eleIdsArr.length > 0){
	    	$('#formulaMsg').html("");
	    	for(var i=0; i<eleIdsArr.length; i++){
	    		$('#'+eleIdsArr[i]).val(eleValArr[i]);
	    	}
	    }else{
	    	$('#formulaMsg').html(msgArr[0]);
	    }
	}
} */
var forArr = [];
function generateFormulas(crfId, animalId){
// 	alert("formula ids : "+frElements);
	$('#formulaVals').val("");
	forArr = [];
	var finalStr = "";
	if(frElements.length > 0){
		for(var i=0; i<frElements.length; i++){
// 			alert(i +"th val is :"+frElements[i]);
			var eleVal = $('#'+frElements[i]).val();
// 			alert("eleVal is :"+eleVal);
			if(eleVal != null && eleVal != "undefined"){
				if(eleVal == "")
					eleVal = "0";
				var temp = frElements[i].split("_");
// 				alert("temp vals :"+temp);
				var crfCode = $('#crfCode').val();
// 				alert("crfCode val is :"+crfCode);
				var frStr = crfCode+"!"+temp[1]+"@@"+eleVal
				if(forArr.indexOf(frStr) == -1){
					if(finalStr == "")
						finalStr = frStr;
					else finalStr = finalStr +","+frStr;
					forArr.push(frStr);
				}
			}
		}
// 		alert("finalStr is :"+finalStr);
		if(finalStr != ""){
			$('#formulaVals').val(finalStr);
// 			alert(mainUrl+"/formCalculation/getFormulaCalculationForCurrentForm/"+crfId+"/"+finalStr);
			var result = asynchronousAjaxCall(mainUrl+"/formCalculation/getFormulaCalculationForCurrentForm/"+crfId+"/"+finalStr);
			if(result != '' || result != "undefined"){
				$('#calcForm').html(result);
// 				alert("eleIdsArr length : "+eleIdsArr.length);
			    if(eleIdsArr.length > 0){
			    	$('#formulaMsg').html("");
			    	for(var i=0; i<eleIdsArr.length; i++){
			    		$('#'+eleIdsArr[i]).val(eleValArr[i]);
// 			    		$('#'+eleIdsArr[i]).attr("disabled", "disabled"); 
			    		$('#'+eleIdsArr[i]).prop("readonly", true);
			    	}
			    }else{
					$('#formulaMsg').html("Provide input for all fields.");
			    }
			}else{
				$('#'+eleIdsArr[i]).val(0);
			}
		}
	}else{
		$('#formulaMsg').html("There is no formulas exist's for this form.");
	}
}
function animalNumberFunction(id, messageId){
	$('#animalId').val("");
	var flag = false;
	var value = $('#'+id).val();
	if(value == null || value == "" || value == "undefined"){
		$('#'+messageId).html("Select Animal.");
		flag = false;
	}else{
		$('#animalId').val(value);
		$('#'+messageId).html("");
		flag = true;
	}
	return flag;
}
</script>
</html>