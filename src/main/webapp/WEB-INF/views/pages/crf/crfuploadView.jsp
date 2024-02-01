<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
	<h5>Crf view &nbsp;<a href='<c:url value="/admini/crf/" />' class="btn btn-primary">EXIT</a></h5> 
		<h6 class="box-title">
    	
            <div class="box-body">
            <table>
			<tr>
				<th>Type : </th><th>${crf.type}</th>
				<th>Sub Type : </th><th>${crf.type}</th>
			</tr>
			<tr>
				<th>Prefix : </th><th>${crf.prefix}</th>
				<th>Observation Name : </th><th>${crf.observationName}</th>
			</tr>
			<tr>
				<th>Observation Desc : </th><th>${crf.observationDesc}</th>
				<th>From : </th><th>${crf.configurationFor}</th>
			</tr>
		</table>
            	<c:forEach items="${crf.sections}" var="sec">
            		<c:if test="${sec.active}">
					<c:if test="${sec.containsGroup eq false}">
						<c:set var="srows" >${sec.maxRows}</c:set>
						<c:set var="scol" >${sec.maxColumns}</c:set>
						<table class="table table-condensed" style="width: 100%">
							<c:if test="${sec.hedding ne ''}">
								<tr>
									<td colspan="${scol}"><h6><b>${sec.hedding}</b></h6></td>
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
																			<c:when test="${item.type eq 'text'}">
																				<c:if test="${item.pattren eq ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />

																				</c:if>
																				<c:if test="${item.pattren ne ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}" placeholder="${item.pattren }"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'text')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																				</c:if>

																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'textArea'}">
																				<c:if test="${item.pattren eq ''}">
																					<textarea class="form-control" rows="3" cols="10" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')"></textarea>
																				</c:if>
																				<c:if test="${item.pattren ne ''}">
																					<textarea class="form-control" rows="3" cols="10" name="${itemkey}"
																						id="${itemkey}" placeholder=""
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'textarea')"
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')"></textarea>
																				</c:if>
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'radio'}">
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
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'checkBox'}">
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
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'select'}">
																				<select class="form-control" name="${itemkey}" id="${itemkey}"
																					onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'select')">
																					<option value="-1">--Select--</option>
																					<c:forEach items="${item.elementValues}"
																						var="itemval">
																						<option value="${itemval.value}">${itemval.value}</option>
																					</c:forEach>
																				</select>
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'date'}">
																				<c:if test="${item.pattren eq ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'date')" 
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																				</c:if>
																				<c:if test="${item.pattren ne ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}" placeholder="${item.pattren }"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'date')" 
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																				</c:if>
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:when test="${item.type eq 'datetime'}">
																				<c:if test="${item.pattren eq ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')" 
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																				</c:if>
																				<c:if test="${item.pattren ne ''}">
																					<input type="text" class="form-control" value="" name="${itemkey}"
																						id="${itemkey}" placeholder="${item.pattren }"
																						onchange="textOnChangeSec('${item.id}','${itemkey}', this.value, 'datetime')" 
																						onmouseover="checkCaliculation('${item.id}','${itemkey}')"
																						onfocus="checkCaliculation('${item.id}','${itemkey}')" />
																				</c:if>
																				<font id="${itemkey}_msg" color="red"></font>
																			</c:when>
																			<c:otherwise>${item.type}</c:otherwise>
																		</c:choose></td>
																	<c:if test="${item.rigtDesc ne ''} ">
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
							<c:set var="secname" >${sec.name}</c:set>
							<c:set var="grows" >${sec.group.maxRows}</c:set>
							<c:set var="gminrows" >${sec.group.minRows}</c:set>
							<c:set var="gcol" >${sec.group.maxColumns}</c:set>
							<c:set var="secname">${sec.name}</c:set>
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
									<td colspan="${gcol}"><input type="button" value="Add"
										id="addid" onclick="addGroup('${sec.group.id}')"> <input
										type="button" value="Remove" id="removeid"
										onclick="removeGroup('${sec.group.id}')"></td>
								</tr>
							</thead>
							<tbody>
							<tr id="fixed">
									<c:forEach items="${sec.group.element}" var="item">
										<td><b>${item.title}</b></td>
									</c:forEach>
								</tr>
							<c:forEach begin="1" step="1" end="${gminrows}" var="row">
								<tr>
									<c:forEach begin="1" step="1" end="${gcol}" var="col">
										<td>
										<c:set var="itemkey" >${sec.name},${row},${col}</c:set>
										<c:forEach items="${sec.group.element}" var="item">
													<c:set var="itemkey">g_${item.id}_${item.name}_${row}</c:set>
													<c:set var="itemname">${item.name}</c:set>
													<c:if test="${item.columnNo eq col }">
														<c:choose>
															<c:when test="${item.type eq 'text'}">
																<c:if test="${item.pattren eq ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
																		onmouseover="checkCaliculationg('${item.id}','${itemkey}')"
																		onfocus="checkCaliculationg(${item.id}','${itemkey}')" />
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}" placeholder="${item.pattren }"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'text', '${itemname}', '${row}')"
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')" />
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'textArea'}">
																<c:if test="${item.pattren eq ''}">
																	<textarea class="form-control" rows="3" cols="10" name="${itemkey}"
																		id="${itemkey}"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea' , '${itemname}', '${row}')"
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"></textarea>
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<textarea class="form-control" rows="3" cols="10" name="${itemkey}"
																		id="${itemkey}" placeholder="${item.pattren }"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'textArea', '${itemname}', '${row}')"
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"></textarea>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'radio'}">
																<c:if test="${item.display eq 'horizantal'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<input type="radio" name="${itemkey}" id="${itemkey}"
																			value="${itemval.value}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio', '${itemname}', '${row}')"> ${itemval.value}
												</c:forEach>
																</c:if>
																<c:if test="${item.display eq 'vertical'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<input type="radio" name="${itemkey}" id="${itemkey}"
																			value="${itemval.value}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'radio', '${itemname}', '${row}')"> ${itemval.value}<br />
																	</c:forEach>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'checkBox'}">
																<c:if test="${item.display eq 'horizantal'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<input type="checkbox" name="${itemkey}"
																			id="${itemkey}" value="${itemval.value}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox', '${itemname}', '${row}')"> ${itemval.value}
												</c:forEach>
																</c:if>
																<c:if test="${item.display eq 'vertical'}">
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<input type="checkbox" name="${itemkey}"
																			id="${itemkey}" value="${itemval.value}"
																			onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'checkBox', '${itemname}', '${row}')"> ${itemval.value}<br>
																	</c:forEach>
																</c:if>
															</c:when>
															<c:when test="${item.type eq 'select'}">
																<select class="form-control" name="${itemkey}" id="${itemkey}"
																	onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'select')">
																	<option value="-1">--Select--</option>
																	<c:forEach items="${item.elementValues}" var="itemval">
																		<option value="${itemval.value}">${itemval.value}</option>
																	</c:forEach>
																</select>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'date'}">
																<c:if test="${item.pattren eq ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'date', '${itemname}', '${row}')" 
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}" placeholder="${item.pattren }"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'date', '${itemname}', '${row}')" 
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:when test="${item.type eq 'datetime'}">
																<c:if test="${item.pattren eq ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')" 
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
																</c:if>
																<c:if test="${item.pattren ne ''}">
																	<input type="text" class="form-control" value="" name="${itemkey}"
																		id="${itemkey}" placeholder="${item.pattren }"
																		onchange="textOnChangeGroup('${item.id}','${itemkey}', this.value, 'datetime', '${itemname}', '${row}')" 
																		onmouseover="checkCaliculationg('${row}','${item.id}','${itemname}','${itemkey}')"
																		onfocus="checkCaliculationg('${row}',${item.id}','${itemname}','${itemkey}')"/>
																</c:if>
																<font id="${itemkey}_msg" color="red"></font>
															</c:when>
															<c:otherwise>${item.type}</c:otherwise>
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
            </div>
		<br/>
		<div style="text-align:center"><a href='<c:url value="/admini/crf/" />' class="btn btn-primary">EXIT</a></div>
</body>
<script type="text/javascript">
	function formSubmidtBtn(){
		$('#crfuploadsave').submit();
	}
	$('#formSubmitBtn').click(function(){
		$('#crfuploadsave').submit();
	});
</script>
</script>