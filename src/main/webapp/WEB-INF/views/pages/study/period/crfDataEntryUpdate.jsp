<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
var rowsMaxInfo = {};
var rowsMinInfo = {};
var rowsCurrentInfo = {};
var rowsLibInfo = {};

var allElementIdsTypesJspStd = {};
var requiredElementIdInJsp = {};
var pattrenIdsAndPattren = {};

var rulesFieldSec = {};
var rulesFieldGroup = {};
var rulesFieldSecdate = {};
var rulesFieldGroupdate = {};
var rulesElementIddate = {};

var eles = "";
var rulesIdElementsdate = {};

var caliculationFieldSec = {};
var nameValue = {};
var groupNameValue = {};


</script>
</head>
<body>
	<div class="box-header with-border">
		<table class="table table-bordered table-striped">
			<tr><td colspan="4">CRF Data Update For the CRF : ${crf.title}</td></tr>
			<tr><td>Study : ${sm.studyNo }</td>
				<td>Phase : ${period.name }</td>
				<td>Subject : ${vol.volId }</td></tr>
		</table>
	</div>
	<div class="box-body">
	<c:url value="/studyexe/studyCrfSave" var="studyCrfSave" />		
	<sf:form action="${studyCrfSave}" method="POST" modelAttribute="crfpojo"
   		id="studyCrfSave" enctype="multipart/form-data">
   		<input type="hidden" name="crfId" id="crfId" value="${crf.id }">
   		<input type="hidden" name="vpcId" id="vpcId" value="${vpcId }">
   		<c:forEach items = "${crf.sections}" var="sec" >
			<c:if test="${sec.active}">
				<c:choose>
					<c:when test="${sec.allowDataEntry}"><c:set var="secview">yes</c:set></c:when>
					<c:otherwise><c:set var="secview">no</c:set></c:otherwise>
				</c:choose>
<%-- 			<c:choose> --%>
<%-- 				<c:when test="${sec.roles eq 'ALL'}"><c:set var="secview">yes</c:set></c:when> --%>
<%-- 				<c:when test="${sec.roles eq userRole}"><c:set var="secview">yes</c:set></c:when> --%>
<%-- 				<c:otherwise><c:set var="secview">no</c:set></c:otherwise> --%>
<%-- 			</c:choose> --%>
				<c:if test="${sec.containsGroup eq false}">
					<c:set var="srows" >${sec.maxRows}</c:set>
					<c:set var="scol" >${sec.maxColumns}</c:set>
					<table class="table table-bordered table-striped">
<%-- 						<tr><td>${sec.id}</td></tr> --%>
						<c:if test="${sec.hedding ne ''}"><tr><td colspan="${scol}"><b>${sec.hedding}-${sec.id}-${sec.roles }</b></td></tr></c:if>
						<c:if test="${sec.subHedding ne ''}"><tr><td colspan="${scol}"><b>${sec.subHedding}</b></td></tr></c:if>
						<c:forEach begin="1" step="1" end="${srows}" var="row">
							<tr>
								<c:forEach begin="1" step="1" end="${scol}" var="col">
									<td>
									<c:forEach items = "${sec.element}" var="item" >
										<c:set var="itemkey" >${item.id}_${item.name}</c:set>
										<c:set var="itemvalue" >${crfData[itemkey]}</c:set>
										<c:if test="${item.rowNo eq row }">
											<c:if test="${item.columnNo eq col }">
												<table class="table table-bordered table-striped">
													<c:if test="${item.topDesc ne ''}"><tr><td colspan="3">${item.topDesc}</td> </tr></c:if>
													<tr>
													<c:if test="${item.totalDesc ne ''}"><td colspan="3">${item.totalDesc}</td></c:if>
													<c:if test="${item.totalDesc eq ''}">
														<c:if test="${item.leftDesc ne ''}"><td>${item.leftDesc}</td></c:if>
														<td>
														<c:choose>
														<c:when test="${item.type eq 'time'}">
															<c:if test="${secview eq 'yes'}">
																<div class="input-group clockpicker-with-callbacks">
																	<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																	 	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																		onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																		onfocus="checkCaliculation('${item.id}','${itemkey}')"
																		class="form-control"  
																		autocomplete="off"
																		readonly="readonly"
																		placeholder="time"
																	 />
																</div>														
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	 disabled="disabled"  />
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'text'}">
															<c:if test="${secview eq 'yes'}">
																<c:if test="${item.pattren eq ''}">
																	<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																	 	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																		onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																		onfocus="checkCaliculation('${item.id}','${itemkey}')"
																	 />
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	placeholder="${item.pattren }"
																	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																	onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																	onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																</c:if>															
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	 disabled="disabled"  />
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'textArea'}">
															<c:if test="${secview eq 'yes'}">
																<c:if test="${item.pattren eq ''}">
																	<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}" 
																	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																	onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																	onfocus="checkCaliculation('${item.id}','${itemkey}')">${itemvalue}</textarea>
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}" 
																	placeholder="${item.pattren }"
																	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																	onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																	onfocus="checkCaliculation('${item.id}','${itemkey}')">${itemvalue}</textarea>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}" 
																	 disabled="disabled">${itemvalue}</textarea>
															</c:if>
														</c:when>
														<c:when test="${item.type eq 'radio'}">
															<c:if test="${item.display eq 'horizantal'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:if test="${itemvalue eq itemval.value }">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}" 
																			onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																		 	checked="checked" value="${itemval.value}"> ${itemval.value}
																	 	</c:if>
																	 	<c:if test="${secview ne 'yes'}">
																	 		<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}
																	 	</c:if>
																	</c:if>
																	<c:if test="${itemvalue ne itemval.value }">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}" 
																			onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																		 	 value="${itemval.value}"> ${itemval.value}
																	 	 </c:if>
																	 	 <c:if test="${secview ne 'yes'}">
																	 		<input type="radio" name="${itemkey}" id="${itemkey}" value="${itemval.value}" disabled="disabled"> ${itemval.value}
																	 	</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
															<c:if test="${item.display eq 'vertical'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:if test="${itemvalue eq itemval.value }">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}" 
																			onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																		 	checked="checked" value="${itemval.value}"> ${itemval.value}<br/>
																	 	</c:if>
																	 	<c:if test="${secview ne 'yes'}">
																	 		<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled" checked="checked" value="${itemval.value}"> ${itemval.value}<br/>
																	 	</c:if>
																	</c:if>
																	<c:if test="${itemvalue ne itemval.value }">
																		<c:if test="${secview eq 'yes'}">
																			<input type="radio" name="${itemkey}" id="${itemkey}" 
																			onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'radio')"
																		 	 value="${itemval.value}"> ${itemval.value}<br/>
																	 	</c:if>
																	 	<c:if test="${secview ne 'yes'}">
																	 		<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled" value="${itemval.value}"> ${itemval.value}<br/>
																	 	</c:if>
																	</c:if>
																</c:forEach>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'checkBox'}">
															<c:if test="${item.display eq 'horizantal'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:choose>
																		<c:when test="${fn:contains(itemvalue, itemval.value)}">
																			<c:if test="${secview eq 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}"
																				onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																				  checked value="${itemval.value}"> ${itemval.value}
																			</c:if>
																			<c:if test="${secview ne 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}" 
																				 disabled="disabled"
																				 checked value="${itemval.value}"> ${itemval.value}
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			<c:if test="${secview eq 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}"
																				onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																				  value="${itemval.value}"> ${itemval.value}
																			</c:if>  
																			<c:if test="${secview ne 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}" 
																				 disabled="disabled" value="${itemval.value}"> ${itemval.value}
																			</c:if>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
															<c:if test="${item.display eq 'vertical'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:choose>
																		<c:when test="${fn:contains(itemvalue, itemval.value)}">
																			<c:if test="${secview eq 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}"
																				  onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																				  checked value="${itemval.value}"> ${itemval.value}<br/>
																			</c:if>
																			<c:if test="${secview ne 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"
																				  checked value="${itemval.value}"> ${itemval.value}<br/>
																			</c:if>
																		</c:when>
																		<c:otherwise>
																			<c:if test="${secview eq 'yes'}">
																				<input type="checkbox" name="${itemkey}" id="${itemkey}"
																				onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'checkbox')"
																				  value="${itemval.value}"> ${itemval.value}<br/>
																			  </c:if>
																			  <c:if test="${secview ne 'yes'}">
																			  	<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"
																					value="${itemval.value}"> ${itemval.value}<br/>
																			  </c:if>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'select'}">
															<c:if test="${secview eq 'yes'}">
																<select class="form-control"name="${itemkey}" id="${itemkey}" 
																onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'select')">
																	<option value="-1">--Select--</option>
																	<c:forEach items="${item.elementValues}" var="itemval" >
																		<c:if test="${itemvalue ne itemval.value }">
																			<option value="${itemval.value}" selected>${itemval.value}</option>
																		</c:if>
																		<c:if test="${itemvalue ne itemval.value }">
																			<option value="${itemval.value}">${itemval.value}</option>
																		</c:if>
																	</c:forEach>
																</select>
																<font id="${itemkey}_msg" color="red"></font>
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<select class="form-control"name="${itemkey}" id="${itemkey}" disabled="disabled">
																	<option value="-1">--Select--</option>
																	<c:forEach items="${item.elementValues}" var="itemval" >
																		<c:if test="${itemvalue ne itemval.value }">
																			<option value="${itemval.value}" selected>${itemval.value}</option>
																		</c:if>
																		<c:if test="${itemvalue ne itemval.value }">
																			<option value="${itemval.value}">${itemval.value}</option>
																		</c:if>
																	</c:forEach>
																</select>
															</c:if>
															
														</c:when>
														<c:when test="${item.type eq 'date'}">
															<c:if test="${secview eq 'yes'}">
																
																<input type="text" class="form-control input-sm" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																		onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'date')" autocomplete="off"
																		onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																		onfocus="checkCaliculation('${item.id}','${itemkey}')"/>
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
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	disabled="disabled"/>
															</c:if>
															
														</c:when>
														<c:when test="${item.type eq 'datetime'}">
															<c:if test="${secview eq 'yes'}">
																<c:if test="${item.pattren eq ''}">
																	<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																	 	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')" 
																	 	onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																		onfocus="checkCaliculation('${item.id}','${itemkey}')"/>
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	placeholder="${item.pattren }"
																	onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')" 
																	onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																	onfocus="checkCaliculation('${item.id}','${itemkey}')"/>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																	disabled="disabled"/>
															</c:if>
															
														</c:when>
														<c:otherwise>
<%-- 														${item.type} --%>
														</c:otherwise>
														</c:choose>
														</td>
														<c:if test="${item.rigtDesc ne ''}"><td>${item.rigtDesc}</td></c:if>
													</c:if>
													</tr>
													<c:if test="${item.bottemDesc ne ''}">
														<tr><td colspan="3">${item.topDesc}</td> </tr>
													</c:if>
												</table>
											</c:if>
										</c:if>
									</c:forEach>
									</td>
								</c:forEach>
							</tr>	
						</c:forEach>
					</table>				
				</c:if>
				<c:if test="${sec.containsGroup}">
				<c:set var="secname" >${sec.name}</c:set>
				<c:set var="grows" >${sec.group.maxRows}</c:set>
				<c:set var="gminrowsdb" >${sec.group.minRows}</c:set>
				<c:if test="${sec.group.displayedRows eq 0}"><c:set var="gminrows" >${sec.group.minRows}</c:set></c:if>
				<c:if test="${sec.group.displayedRows ne 0}"><c:set var="gminrows" >${sec.group.displayedRows}</c:set></c:if>
				<c:set var="gcol" >${sec.group.maxColumns}</c:set>
				<script type="text/javascript">
					rowsMaxInfo['${sec.group.id}'] = ${grows};
					rowsMinInfo['${sec.group.id}'] = ${gminrows};
					rowsCurrentInfo['${sec.group.id}'] = ${gminrows};
					rowsLibInfo['${sec.group.id}'] = ${gminrowsdb}
				</script>
				<table class="table table-bordered table-striped" id="group_${sec.group.id}">
					<thead>
					<c:if test="${sec.hedding ne ''}"><tr><td colspan="${gcol}"><b>${sec.hedding}</b></td></tr></c:if>
					<c:if test="${sec.subHedding ne ''}"><tr><td colspan="${gcol}"><b>${sec.subHedding}</b></td></tr></c:if>
					<c:if test="${sec.group.hedding ne ''}"><tr><td colspan="${gcol}"><b>${sec.group.hedding}</b></td></tr></c:if>
					<c:if test="${sec.group.subHedding ne ''}"><tr><td colspan="${gcol}"><b>${sec.group.subHedding}</b></td></tr></c:if>
					<tr><td colspan="${gcol}">
						<c:if test="${secview eq 'yes'}">
							<input type="button" value="Add" id="addid" 
								onclick="addGroup('${sec.group.id}')">
							<input type="button" value="Remove" id="removeid" 
								onclick="removeGroup('${sec.group.id}')">
						</c:if>
						<c:if test="${secview ne 'yes'}">
						
						</c:if>
						</td></tr>
					</thead>
					<tbody>
					<tr id="fixed">
						<c:forEach items = "${sec.group.element}" var="item" ><td><b>${item.title}</b></td></c:forEach>
					</tr>
					<c:forEach begin="1" step="1" end="${gminrows}" var="row">
					<tr id="fixed">
						<c:forEach begin="1" step="1" end="${gcol}" var="col">
							<td>
							<c:forEach items = "${sec.group.element}" var="item" >
							<c:set var="itemkey" >g_${item.id}_${item.name}_${row}</c:set>
							<c:set var="itemname">${item.name}</c:set>
							<c:set var="itemvalue" >${crfData[itemkey]}</c:set>
								<c:if test="${item.columnNo eq col }">
									<c:choose>
										<c:when test="${item.type eq 'time'}">
											<c:if test="${secview eq 'yes'}">
												<div class="input-group clockpicker-with-callbacks">
													<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
													 	onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
														onmouseover="checkCaliculationg('${item.id}','${itemkey}')"
														onfocus="checkCaliculationg(${item.id}','${itemkey}')" 
														class="form-control"  
														autocomplete="off"
														readonly="readonly"
														placeholder="time"/>
													<font id="${itemkey}_msg" color="red"></font>
												</div>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }" disabled="disabled"/>
											</c:if>
										</c:when>
										<c:when test="${item.type eq 'text'}">
											<c:if test="${secview eq 'yes'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
												 	onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
													onmouseover="checkCaliculationg('${item.id}','${itemkey}')"
													onfocus="checkCaliculationg(${item.id}','${itemkey}')" />
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
												onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
												onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }" disabled="disabled"/>
											</c:if>
										</c:when>
										<c:when test="${item.type eq 'textArea'}">
											<c:if test="${secview eq 'yes'}">
											<c:if test="${item.pattren eq ''}">
												<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}"
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea' , '${itemname}', '${row}')"
												onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
												onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')">${itemvalue}</textarea>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}"
												placeholder="${item.pattren }"
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea', '${itemname}', '${row}')"
												onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
												onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')">${itemvalue}</textarea>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}"
												placeholder="${item.pattren }" disabled="disabled">${itemvalue}</textarea>
											</c:if>
										</c:when>
										<c:when test="${item.type eq 'radio'}">
										<c:set var="itemvalueg" >${crfData[itemkey]}</c:set>
										
<%-- 										${item.id }->${itemval.id }=${itemvalue}--${itemval.value } == ${itemkey} --%>
											<c:if test="${item.display eq 'horizantal'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:if test="${itemvalue eq itemval.value }">
														<c:if test="${secview eq 'yes'}">
														<input type="radio" name="${itemkey}" id="${itemkey}"
														 onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio')" 
													 	 checked="checked" value="${itemval.value}"> ${itemval.value}
													 	 </c:if>
													 	 <c:if test="${secview ne 'yes'}">
													 	 	<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}
													 	 </c:if>
													</c:if>
													<c:if test="${itemvalue ne itemval.value }">
														<c:if test="${secview eq 'yes'}">
															<input type="radio" name="${itemkey}" id="${itemkey}"
															onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio')" 
														 	  value="${itemval.value}"> ${itemval.value} 
														</c:if>
														<c:if test="${secview ne 'yes'}">
															<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}
														</c:if>
													</c:if>
												</c:forEach>
											</c:if>
											<c:if test="${item.display eq 'vertical'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:if test="${itemvalue eq itemval.value }">
														<c:if test="${secview eq 'yes'}">
															<input type="radio" name="${itemkey}" id="${itemkey}"
															onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio')" 
														 	 checked="checked" value="${itemval.value}"> ${itemval.value}<br/>
														 </c:if>
														 <c:if test="${secview ne 'yes'}">
														 	<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}<br/>
														 </c:if>
													</c:if>
													<c:if test="${itemvalue eq itemval.value }">
														<c:if test="${secview eq 'yes'}">
															<input type="radio" name="${itemkey}" id="${itemkey}"
															  onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio')" 
														 	  value="${itemval.value}"> ${itemval.value}<br/>
														 </c:if>
														 <c:if test="${secview ne 'yes'}">
														 	<input type="radio" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}<br/>
														 </c:if>
													</c:if>
												</c:forEach>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'checkBox'}">
											<c:if test="${item.display eq 'horizantal'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:choose>
														<c:when test="${fn:contains(itemvalue, itemval.value)}">
															<c:if test="${secview eq 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}"
																onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox')" 
																  checked value="${itemval.value}"> ${itemval.value}
															</c:if>  
															<c:if test="${secview ne 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if test="${secview eq 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}"
																onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox')" 
																  value="${itemval.value}"> ${itemval.value}
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}
															</c:if>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
											<c:if test="${item.display eq 'vertical'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:choose>
														<c:when test="${fn:contains(itemvalue, itemval.value)}">
															<c:if test="${secview eq 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}"
																onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox')" 
																  checked value="${itemval.value}"> ${itemval.value}<br/>
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}<br/>
															</c:if>
														</c:when>
														<c:otherwise>
															<c:if test="${secview eq 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}"
																onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox')" 
																  value="${itemval.value}"> ${itemval.value}<br/>
															</c:if>
															<c:if test="${secview ne 'yes'}">
																<input type="checkbox" name="${itemkey}" id="${itemkey}" disabled="disabled"> ${itemval.value}<br/>
															</c:if>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'select'}">
											<c:if test="${secview eq 'yes'}">
												<select class="form-control"name="${itemkey}" id="${itemkey}" 
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'select')" >
													<option value="-1">--Select--</option>
													<c:forEach items="${item.elementValues}" var="itemval">
														<option value="${itemval.value}">${itemval.value}</option>
													</c:forEach>
												</select>
												<font id="${itemkey}_msg" color="red"></font>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<select class="form-control"name="${itemkey}" id="${itemkey}" disabled="disabled" >
													<option value="-1">--Select--</option>
												</select>
											</c:if>
										</c:when>
										<c:when test="${item.type eq 'date'}">
											<c:if test="${secview eq 'yes'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
												 	onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'date', '${itemname}', '${row}')" 
													onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
													onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"
												 />
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'date', '${itemname}', '${row}')" 
												onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
												onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }" disabled="disabled"/>
											</c:if>
										</c:when>
										<c:when test="${item.type eq 'datetime'}">
											<c:if test="${secview eq 'yes'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
													onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')" 
													onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
													onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')" 
												onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
												onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
											</c:if>
											<c:if test="${secview ne 'yes'}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }" disabled="disabled"/>
											</c:if>
										</c:when>
										<c:otherwise>
<%-- 										${item.type} --%>
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:forEach>			
							</td>
						</c:forEach>
					</tr>	
					</c:forEach>
					</tbody>
				</table>							
				</c:if>
			</c:if>
	      </c:forEach>
	      <input type="hidden" name="groupCountInfo" id="groupCountInfo"/>
		<br/>
		    <div style="text-align:center">
		    	<c:if test="${vpc.crfStatus eq 'IN PROGRESS' }">
		    		<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
		    			<input type="hidden" name="inReview" id="inReview" value="IN REVIEW">
<!-- 			    		<input type="checkbox" name="inReview" id="inReview" value="IN REVIEW">Sent to Review  -->
						<input type="button" value="Update" id="formSubmitBtn" class="btn btn-primary">
					</c:if>
		    	</c:if>
<%-- 		    	<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${vol.id}/${period.id}" />'  --%>
<!--             	class="btn btn-primary">EXIT</a>	 -->
            </div>
    	</sf:form>
	</div>
	<c:if test="${vpc.crfStatus eq 'IN PROGRESS'}">
	
	
		<c:forEach items="${requiredElementIdInJsp}" var="v">
			<script type="text/javascript">requiredElementIdInJsp['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${allElementIdsTypesJspStd}" var="v">
			<script type="text/javascript">allElementIdsTypesJspStd['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${pattrenIdsAndPattren}" var="v">
			<script type="text/javascript">pattrenIdsAndPattren['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${rulesFieldSec}" var="v">
			<script type="text/javascript">rulesFieldSec['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${rulesFieldGroup}" var="v">
			<script type="text/javascript">rulesFieldGroup['${v.key}']='${v.value}'</script>
		</c:forEach>
		
		
		<c:forEach items="${rulesFieldSecdate}" var="v" varStatus="loop">
			<script type="text/javascript">rulesFieldSecdate['${loop.index}']='${v}'</script>
		</c:forEach>
		<c:forEach items="${rulesFieldGroupdate}" var="v" varStatus="loop">
			<script type="text/javascript">rulesFieldGroupdate['${loop.index}']='${v}'</script>
		</c:forEach>
		
		
		<c:forEach items="${map2}" var="v">
			<script type="text/javascript">rulesElementIddate['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${map}" var="v">
			<script type="text/javascript">eles = "";</script>
			<c:forEach items="${v.value}" var="v1">
				<script type="text/javascript">eles = eles+","+"${v1}";</script>
			</c:forEach>
			<script type="text/javascript">rulesIdElementsdate['${v.key}']=eles</script>
		</c:forEach>
		
		<c:forEach items="${caliculationFieldSec}" var="v">
			<script type="text/javascript">caliculationFieldSec['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${tempSecdata}" var="v">
			<script type="text/javascript">nameValue['${v.key}']='${v.value}'</script>
		</c:forEach>
		<c:forEach items="${tempGropdata}" var="v">
			<script type="text/javascript">groupNameValue['${v.key}']='${v.value}'</script>
		</c:forEach>
	</c:if>
</body>

<script type="text/javascript">
function textOnChangeGroup( dbid, id, value , eletype, ele, row){
	$("#"+id+"_msg").html("");
// 	alert(ele  + " : " + row + " : " + value);
	keyvaluPareOfGroup(ele, row, value);
		$("#"+id+"_msg").html("");
		if(value.trim() != ''){
			var fg = true;
			if(eletype == 'select'){
				if(value == -1){
					fg = false;
				}
			}
			if(fg){
				var flag  = false;
				var resval = "";
				//check dbid abilabe in map, if-Yes -> resval= currentCrf reamin ids[id_name]
				$.each(rulesFieldGroup, function(k, v) {
					var res = k.split("##");
					for(var i=0; i<res.length; i++){
						if(res[i] ==  dbid){
							//split v with ## and send values with id-value
							var resval  = v.split("##");
							flag = true;
							break;
						}
					}				
				});
				
				
				var f = true;
				if(flag){
					var values = "";
					for(var i=0; i<resval.length; i++){
						if(i == 0){
							values = resval[i] + "@@##" + $("#"+resval[i]).val();	f = false;
						}else{
							values = values + "@@@##" +resval[i] + "@@##" + $("#"+resval[i]).val();	f = false;
						}
					}
					if(f) values = id;
					var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfGroupEleRuelCheck/"+$("#crfId").val()+"/"+$("#vpcId").val()+"/"+dbid+"/"+id+"/"+value+"/"+values);
					if(result != '' || result != 'undefined'){
						$("#"+id+"_msg").html(result);
					}
				}			
			}	
		}
		checkDateComparesion(dbid, id, value , eletype);
	}	
	
	function checkDateComparesion(dbid, id, value , eletype){
		$.each(rulesFieldSecdate, function(k, v) {
			if(id == v){
				var dateRuleDbId = rulesElementIddate[id];  
				var eles = rulesIdElementsdate[dateRuleDbId];
				var elearray = eles.split(",")
				var values = "";
				for(var i=1; i<elearray.length; i++){
					var va = elearray[i].trim();
					if(i == 1)	values =  va + "@@" + $("#"+va).val().trim();
					else values =  values + "@@@" + va + "@@" + $("#"+va).val().trim();
				}
				var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfSecEleDateRuelCheck/"+$("#crfId").val()+"/"+$("#vpcId").val()+"/"+dateRuleDbId+"/"+values);
				if(result != '' || result != 'undefined'){
					var r = result.split("@@");
					$("#"+r[0]+"_msg").html(r[1]);
				}
			}
		});
	}

	function textOnChangeSec( dbid, id, value , eletype){
		$("#"+id+"_msg").html("");
		keyvaluPare(dbid, id, value);
		if(value.trim() != ''){
			var fg = true;
			if(eletype == 'select'){
				if(value == -1){
					fg = false;
				}
			}
			if(fg){
				var flag  = false;
				var resval = "";
				//check dbid abilabe in map, if-Yes -> resval= currentCrf reamin ids[id_name]
				$.each(rulesFieldSec, function(k, v) {
					var res = k.split("##");
					for(var i=0; i<res.length; i++){
						if(res[i] ==  dbid){
							//split v with ## and send values with id-value
							var resval  = v.split("##");
							flag = true;
							break;
						}
					}				
				});
				var f = true;
				if(flag){
					var values = "";
					for(var i=0; i<resval.length; i++){
						if(i == 0){
							values = resval[i] + "@@##" + $("#"+resval[i]).val();	f = false;
						}else{
							values = values + "@@@##" +resval[i] + "@@##" + $("#"+resval[i]).val();	f = false;
						}
					}
					if(f) values = id;
					var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfSecEleRuelCheck/"+$("#crfId").val()+"/"+$("#vpcId").val()+"/"+dbid+"/"+id+"/"+value+"/"+values);
					if(result != '' || result != 'undefined'){
						$("#"+id+"_msg").html(result);
					}
				}			
			}	
		}
		checkDateComparesion(dbid, id, value , eletype);
	}	
	function checkDateComparesion(dbid, id, value , eletype){
		$.each(rulesFieldSecdate, function(k, v) {
			if(id == v){
				var dateRuleDbId = rulesElementIddate[id];  
				var eles = rulesIdElementsdate[dateRuleDbId];
				var elearray = eles.split(",")
				var values = "";
				for(var i=1; i<elearray.length; i++){
					var va = elearray[i].trim();
					if(i == 1)	values =  va + "@@" + $("#"+va).val().trim();
					else values =  values + "@@@" + va + "@@" + $("#"+va).val().trim();
				}
				var result = asynchronousAjaxCall(mainUrl+"/studyexe/studyCrfSecEleDateRuelCheck/"+$("#crfId").val()+"/"+$("#vpcId").val()+"/"+dateRuleDbId+"/"+values);
				if(result != '' || result != 'undefined'){
					var r = result.split("@@");
					$("#"+r[0]+"_msg").html(r[1]);
				}
			}
		});
	}
</script>

<script type="text/javascript">
	$('#formSubmitBtn').click(function(){
		var pattrenFlag  = pattrenValidation();
		if(pattrenFlag > 0)
			alert("No of Patterens Faild :  "+pattrenFlag);
		var requiredFlag  = requiredValidation();
		if(requiredFlag == 0){
			var message = "Are you sure to update Crf Data?";
	        if($("#inReview").prop("checked") == true){
	        	message = "Are you sure to update Crf Data and Sent to Review ?";
	        }
	        var groupInfo = "";
        	var i = 0;
			$.each(rowsCurrentInfo, function (key, value) {
				if(i == 0 ){
					groupInfo = key + "," + value;
				}else{
					groupInfo = groupInfo + "@" + key + "," + value;	
				}
				i++;
	        });  
			$("#groupCountInfo").val(groupInfo);
    		$('#studyCrfSave').submit();
    				
		}else
			alert("Crf Contains "+requiredFlag+" Required Fields");
	});
</script>
<script type="text/javascript">
function pattrenValidation(){
	var errorCount = 0;
	$.each(pattrenIdsAndPattren, function(k, val) {
		if(val != ''){
			$("#"+k+"_msg").html("");
			var v = allElementIdsTypesJspStd[k];
			if(v == 'text' || v == 'textArea' || v == 'date'|| v == 'datetime'){
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
						$("#"+k+"_msg").html("Required Field");
						errorCount += 1;
					}else{
						$("#"+k+"_msg").html("");
					}					
				}
			}else if(v == 'select'){
				if($("#"+k).val() != undefined){
					if($("#"+k).val() == '-1'){
						$("#"+k+"_msg").html("Required Field");
						errorCount += 1;
					}else{
						$("#"+k+"_msg").html("");
					}
				}
			}else if(v == 'radio' || v == 'checkBox'){
				if($("#"+k).val() != undefined){
					var nn ="input[name='"+k+"']:checked";
					if($(nn).length != undefined){
						if ($(nn).length === 0) {
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
// rowsLibInfo
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

function keyvaluPare(dbid, id, value){
// 	alert(id.replace(dbid+"_", ""));
	nameValue[id.replace(dbid+"_", "")] = value;
}
function keyvaluPareOfGroup(ele, row, value){
// 	alert("g_"+ele+"_"+row);
	groupNameValue["g_"+ele+"_"+row] = value;	
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
				fieldAndVales = value[i] +"-"+nameValue[value[i]];
				flag2 = false;
			}else
				fieldAndVales = fieldAndVales + "," + value[i] +"-"+nameValue[value[i]];
		var crfid = $("#crfId").val();
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
</html>