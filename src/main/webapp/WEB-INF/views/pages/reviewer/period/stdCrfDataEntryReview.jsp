<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script type="text/javascript">
var rowsMaxInfo = {};
var rowsMinInfo = {};
var rowsCurrentInfo = {};
var rowsLibInfo = {};
</script>
	<div class="box-header with-border">
	<table id="example1" class="table table-bordered table-striped">
    	<tr><td>CRF Data Review For the CRF : ${crf.title}</td><td>Study : ${sm.studyNo }</td><td>Phase : ${period.name }</td><td>Subject : ${vol.volId }</td></tr>
    </table>
	</div>
	<div class="box-body">
	<c:url value="/studyexe/studyCrfSave" var="studyCrfSave" />		
	<sf:form action="${studyCrfSave}" method="POST" modelAttribute="crfpojo"
   		id="studyCrfSave" enctype="multipart/form-data">
   		<input type="hidden" name="vpcId" id="vpcId" value="${vpcId }">
   		<input type="hidden" name="crfId" id="crfId" value="${crf.id }">
		<c:forEach items = "${crf.sections}" var="sec" >
			<c:if test="${sec.active}">
				<c:if test="${sec.containsGroup eq false}">
					<c:set var="srows" >${sec.maxRows}</c:set>
					<c:set var="scol" >${sec.maxColumns}</c:set>
					<table class="table table-bordered table-striped">
						<c:if test="${sec.hedding ne ''}"><tr><td colspan="${scol}"><h3><b>${sec.hedding}</b></h3></td></tr></c:if>
						<c:if test="${sec.subHedding ne ''}"><tr><td colspan="${scol}"><h5><b>${sec.subHedding}</b></h5></td></tr></c:if>
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
														<td>${item.leftDesc}</td>
														<td>
														<c:choose>
														<c:when test="${item.type eq 'text'}">
															<c:if test="${item.pattren eq ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																 onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<c:if test="${item.pattren ne ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																placeholder="${item.pattren }"
																onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'textArea'}">
															<c:if test="${item.pattren eq ''}">
																<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}" 
																onchange="textOnChange('${itemkey}', this.value)">${itemvalue}</textarea>
															</c:if>
															<c:if test="${item.pattren ne ''}">
																<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}" 
																placeholder="${item.pattren }"
																onchange="textOnChange('${itemkey}', this.value)">${itemvalue}</textarea>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'radio'}">
															<c:if test="${item.display eq 'horizantal'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:if test="${itemvalue eq itemval.value }">
																		<input type="radio" name="${itemkey}" id="${itemkey}" 
																	 	checked="checked" value="${itemval.value}"> ${itemval.value}
																	</c:if>
																	<c:if test="${itemvalue ne itemval.value }">
																		<input type="radio" name="${itemkey}" id="${itemkey}" 
																	 	 value="${itemval.value}"> ${itemval.value}
																	</c:if>
																</c:forEach>
															</c:if>
															<c:if test="${item.display eq 'vertical'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:if test="${itemvalue eq itemval.value }">
																		<input type="radio" name="${itemkey}" id="${itemkey}" 
																	 	checked="checked" value="${itemval.value}"> ${itemval.value}<br/>
																	</c:if>
																	<c:if test="${itemvalue ne itemval.value }">
																		<input type="radio" name="${itemkey}" id="${itemkey}" 
																	 	 value="${itemval.value}"> ${itemval.value}<br/>
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
																			<input type="checkbox" name="${itemkey}" id="${itemkey}"
																			  checked value="${itemval.value}"> ${itemval.value}
																		</c:when>
																		<c:otherwise>
																			<input type="checkbox" name="${itemkey}" id="${itemkey}"
																			  value="${itemval.value}"> ${itemval.value}
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
															<c:if test="${item.display eq 'vertical'}">
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:choose>
																		<c:when test="${fn:contains(itemvalue, itemval.value)}">
																			<input type="checkbox" name="${itemkey}" id="${itemkey}"
																			  checked value="${itemval.value}"> ${itemval.value}<br/>
																		</c:when>
																		<c:otherwise>
																			<input type="checkbox" name="${itemkey}" id="${itemkey}"
																			  value="${itemval.value}"> ${itemval.value}<br/>
																		</c:otherwise>
																	</c:choose>
																</c:forEach>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'select'}">
															<select class="form-control" name="${itemkey}" id="${itemkey}">
																<option value="-1">--Select--</option>
																<c:forEach items="${item.elementValues}" var="itemval">
																	<c:if test="${itemvalue ne itemval.value }">
																		<option value="${itemval.value}" selected>${itemval.value}</option>
																	</c:if>
																	<c:if test="${itemvalue ne itemval.value }">
																		<option value="${itemval.value}">${itemval.value}</option>
																	</c:if>
																</c:forEach>
															</select>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'date'}">
															<c:if test="${item.pattren eq ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																 onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<c:if test="${item.pattren ne ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																placeholder="${item.pattren }"
																onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:when test="${item.type eq 'dateAndTime'}">
															<c:if test="${item.pattren eq ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
																 onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<c:if test="${item.pattren ne ''}">
																<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
																placeholder="${item.pattren }"
																onchange="textOnChange('${itemkey}', this.value)"/>
															</c:if>
															<font id="${itemkey}_msg" color="red"></font>
														</c:when>
														<c:otherwise>
<%-- 														${item.type} --%>
														</c:otherwise>
														</c:choose>
														<c:if test="${item.type ne 'non'}">
														<br/>													
														<img src='<c:url value="/static/template/dist/img/flag_wite.png"/>' 
														class="img-circle" title="New Discrepancy" width="16" height="16" 
														onclick="createDiscrepency('${itemkey}', '${item.id }')">
														<c:forEach items="${descrepencyReviewer}" var="rew">
														    <c:if test="${rew == itemkey }">
														    	<img src='<c:url value="/static/template/dist/img/flag_red.png"/>' 
																class="img-circle" title="Reviewer Rized" width="16" height="16"
																onclick="createDiscrepencySec('${itemkey}', '${item.id }','reviewer')">
														    </c:if>
														</c:forEach>
														<c:forEach items="${descrepencyUser}" var="dis">
														    <c:if test="${dis == itemkey }">
														    	<img src='<c:url value="/static/template/dist/img/flag_blue.png"/>' 
																class="img-circle" title="User Rized" width="16" height="16"
																onclick="createDiscrepencySec('${itemkey}', '${item.id }','user')">
														    </c:if>
														</c:forEach>
														<c:forEach items="${descrepencyOnHold}" var="oh">
														    <c:if test="${oh == itemkey }">
														    	<img src='<c:url value="/static/template/dist/img/flag_black.png"/>' 
																class="img-circle" title="On Hold" width="16" height="16"
																onclick="createDiscrepencySec('${itemkey}', '${item.id }','onHold')">
														    </c:if>
														</c:forEach>
														<c:forEach items="${descrepencyClosed}" var="clo">
														    <c:if test="${clo == itemkey }">
														    	<img src='<c:url value="/static/template/dist/img/flag_green.png"/>' 
																class="img-circle" title="Closed" width="16" height="16"
																onclick="createDiscrepencySec('${itemkey}', '${item.id }','closed')">
														    </c:if>
														</c:forEach>
														<c:forEach items="${descrepencyAll}" var="all">
														    <c:if test="${all == itemkey }">
															    	<img src='<c:url value="/static/template/dist/img/flag_brown.png"/>' 
																	class="img-circle" title="All" width="16" height="16"
																	onclick="createDiscrepencySec('${itemkey}', '${item.id }','all')">
														    </c:if>
														</c:forEach>
														<c:forEach items="${descrepencyOpen}" var="open">
														    <c:if test="${open == itemkey }">
															    	<img src='<c:url value="/static/template/dist/img/flag_brown.png"/>' 
																	class="img-circle" title="Opened" width="16" height="16"
																	onclick="createDiscrepencySec('${itemkey}', '${item.id }','open')">
														    </c:if>
														</c:forEach>
														</c:if>
														</td>
														<td>${item.rigtDesc}</td>
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
					<c:if test="${sec.hedding ne ''}"><tr><td colspan="${gcol}"><h3><b>${sec.hedding}</b></h3></td></tr></c:if>
					<c:if test="${sec.subHedding ne ''}"><tr><td colspan="${gcol}"><h5><b>${sec.subHedding}</b></h5></td></tr></c:if>
					<c:if test="${sec.group.hedding ne ''}"><tr><td colspan="${gcol}"><h3><b>${sec.group.hedding}</b></h3></td></tr></c:if>
					<c:if test="${sec.group.subHedding ne ''}"><tr><td colspan="${gcol}"><h5><b>${sec.group.subHedding}</b></h5></td></tr></c:if>
					<tr><td colspan="${gcol}">
						<input type="button" value="Add" id="addid" 
							onclick="addGroup('${sec.group.id}')">
						<input type="button" value="Remove" id="removeid" 
							onclick="removeGroup('${sec.group.id}')">
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
							<c:set var="itemid" >${item.id}</c:set>
							<c:set var="itemtype" >${item.type}</c:set>
							<c:set var="itemkey" >g_${item.id}_${item.name}_${row}</c:set>
							<c:set var="itemvalue" >${crfData[itemkey]}</c:set>
								<c:if test="${item.columnNo eq col }">
									<c:choose>
										<c:when test="${item.type eq 'text'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
												 onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
<%-- 											<input type="text" value="${itemvalue}" name="${itemkey}" id="${itemkey}"/> --%>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'textArea'}">
											<c:if test="${item.pattren eq ''}">
												<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}"
												onchange="textOnChange('${itemkey}', this.value)">${itemvalue}</textarea>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<textarea class="form-control" rows="3" cols="10" name="${itemkey}" id="${itemkey}"
												placeholder="${item.pattren }"
												onchange="textOnChange('${itemkey}', this.value)">${itemvalue}</textarea>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'radio'}">
											<c:if test="${item.display eq 'horizantal'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:if test="${itemvalue eq itemval.value }">
														<input type="radio" name="${itemkey}" id="${itemkey}"
													 	 checked="checked" value="${itemval.value}"> ${itemval.value}
													</c:if>
													<c:if test="${itemvalue ne itemval.value }">
														<input type="radio" name="${itemkey}" id="${itemkey}"
													 	  value="${itemval.value}"> ${itemval.value}
													</c:if>
												</c:forEach>
											</c:if>
													
											<c:if test="${item.display eq 'vertical'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:if test="${itemvalue eq itemval.value }">
														<input type="radio" name="${itemkey}" id="${itemkey}"
													 	 checked="checked" value="${itemval.value}"> ${itemval.value}<br/>
													</c:if>
													<c:if test="${itemvalue ne itemval.value }">
														<input type="radio" name="${itemkey}" id="${itemkey}"
													 	  value="${itemval.value}"> ${itemval.value}<br/>
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
															<input type="checkbox" name="${itemkey}" id="${itemkey}"
															  checked value="${itemval.value}"> ${itemval.value}
														</c:when>
														<c:otherwise>
															<input type="checkbox" name="${itemkey}" id="${itemkey}"
															  value="${itemval.value}"> ${itemval.value}
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
											<c:if test="${item.display eq 'vertical'}">
												<c:forEach items="${item.elementValues}" var="itemval">
													<c:choose>
														<c:when test="${fn:contains(itemvalue, itemval.value)}">
															<input type="checkbox" name="${itemkey}" id="${itemkey}"
															  checked value="${itemval.value}"> ${itemval.value}<br/>
														</c:when>
														<c:otherwise>
															<input type="checkbox" name="${itemkey}" id="${itemkey}"
															  value="${itemval.value}"> ${itemval.value}<br/>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'select'}">
											<select class="form-control" name="${itemkey}" id="${itemkey}">
												<option value="-1">--Select--</option>
												<c:forEach items="${item.elementValues}" var="itemval">
													<option value="${itemval.value}">${itemval.value}</option>
												</c:forEach>
											</select>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'date'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
												 onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:when test="${item.type eq 'dateAndTime'}">
											<c:if test="${item.pattren eq ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}" 
												 onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
											<c:if test="${item.pattren ne ''}">
												<input type="text" class="form-control" value="${itemvalue}" name="${itemkey}" id="${itemkey}"  
												placeholder="${item.pattren }"
												onchange="textOnChange('${itemkey}', this.value)"/>
											</c:if>
											<font id="${itemkey}_msg" color="red"></font>
										</c:when>
										<c:otherwise>
<%-- 										${item.type} --%>
										</c:otherwise>
									</c:choose>
									<c:if test="${itemtype ne 'non'}">
										<br/>														
										<img src='<c:url value="/static/template/dist/img/flag_wite.png"/>' 
										class="img-circle" title="New Discrepancy" width="16" height="16" 
										onclick="createDiscrepencyGroup('${itemkey}', '${itemid}')">
										<c:forEach items="${descrepencyReviewer}" var="rew">
										    <c:if test="${rew == itemkey }">
										    	<img src='<c:url value="/static/template/dist/img/flag_red.png"/>' 
												class="img-circle" title="Reviewer Rized" width="16" height="16"
												onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','reviewer')">
										    </c:if>
										</c:forEach>
										<c:forEach items="${descrepencyUser}" var="dis">
										    <c:if test="${dis == itemkey }">
										    	<img src='<c:url value="/static/template/dist/img/flag_blue.png"/>' 
												class="img-circle" title="User Rized" width="16" height="16"
												onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','user')">
										    </c:if>
										</c:forEach>
										<c:forEach items="${descrepencyOnHold}" var="oh">
										    <c:if test="${oh == itemkey }">
										    	<img src='<c:url value="/static/template/dist/img/flag_black.png"/>' 
												class="img-circle" title="On Hold" width="16" height="16"
												onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','onHold')">
										    </c:if>
										</c:forEach>
										<c:forEach items="${descrepencyClosed}" var="clo">
										    <c:if test="${clo == itemkey }">
										    	<img src='<c:url value="/static/template/dist/img/flag_green.png"/>' 
												class="img-circle" title="Closed" width="16" height="16"
												onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','closed')">
										    </c:if>
										</c:forEach>
										<c:forEach items="${descrepencyAll}" var="all">
										    <c:if test="${all == itemkey }">
											    	<img src='<c:url value="/static/template/dist/img/flag_brown.png"/>' 
													class="img-circle" title="All" width="16" height="16"
													onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','all')">
										    </c:if>
										</c:forEach>
										<c:forEach items="${descrepencyOpen}" var="open">
										    <c:if test="${open == itemkey }">
											    	<img src='<c:url value="/static/template/dist/img/flag_brown.png"/>' 
													class="img-circle" title="Opened" width="16" height="16"
													onclick="createDiscrepencyGroupView('${itemkey}', '${item.id }','open')">
										    </c:if>
										</c:forEach>			
									</c:if>
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
		    	<c:if test="${CrfStatus ne 'COMPLETED'}">
		    		<c:if test="${descrepencyAllClosed eq 'Yes' }">
<%-- 			    		<a href='<c:url value="/reviewer/subjectPeriodCrfsDataReview/${vpcId}/" />' --%>
<!-- 			    			class="btn btn-primary">Approve</a> -->
						<a href='<c:url value="#" />' onclick="signacherPopup()"
			    			class="btn btn-primary">Approve</a>
			    	</c:if>
		    	</c:if>	
		    	<a href='<c:url value="/reviewer/viewSubjectPeriodCrfs/${vol.id}/${period.id}" />' 
            	class="btn btn-primary">EXIT</a>	
            </div>
    	</sf:form>
	</div>
	
		<div class="modal fade" id="signacherModal" tabindex="-1" role="dialog" aria-labelledby="discDataModal" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<h4 class="modal-title" id="myModalLabel">Electronic Signature</h4>
		        		<input type="hidden" id="signcheck"/>
		      		</div>
		      		<div class="modal-body">
		      			<table>
		      				<tr>
		      					<th>User Name : </th>
		      					<td>${userName} </td>
		      				</tr>
		      				<tr>
		      					<th>Transaction Password</th>
		      					<td><input type="password" id="tpw"/><font color="red" id="tpwmsg"></font></td>
		      				</tr>
		      				<tr>
		      				<tr><td/><td><input type="button" class="btn btn-primary" onclick="subjectPeriodCrfsDataReview()" value="Submit"/></td></tr>
		      			</table>
		        	</div>
		      		<div class="modal-footer">
		      			<div style="text-align: left;">
		      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptsignCheckId" />&nbsp;&nbsp;&nbsp;Do You want to close page.</label>
		      			</div>
		      			<div style="text-align: right;">
		        		<input type="button" class="btn btn-primary" onclick="acceptsignCheckBox()" value="Close"/>
		        		</div>
		        	</div>
		    	</div>
		  	</div>
		</div>
		
    	<div class="modal fade" id="discDataModal" tabindex="-1" role="dialog" aria-labelledby="discDataModal" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<h4 class="modal-title" id="myModalLabel">New Discrepancyss</h4> 
		      		</div>
		      		<div class="modal-body">
		      			<div id="discData">
		      			</div>
		        	</div>
		      		<div class="modal-footer">
		      			<div style="text-align: left;">
		      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptCheckId" />&nbsp;&nbsp;&nbsp;Do You want to close page.</label>
		      			</div>
		      			<div style="text-align: right;">
		        		<input type="button" class="btn btn-primary" onclick="acceptCheckBox()" value="Close"/>
		        		</div>
		        	</div>
		    	</div>
		  	</div>
		</div>
		
		
		<c:url value="/reviewer/createDiscrepency" var="createDiscrepency" />
    	<sf:form action="${createDiscrepency}" method="POST" modelAttribute="crfpojo" 
    		id="secDescSave" enctype="multipart/form-data">
            <input type="hidden" id="vpcId3" name="vpcId"/>
            <input type="hidden" id="crfId3" name="crfId"/>
            <input type="hidden" id="eleId3" name="eleId"/>
            <input type="hidden" id="dataId3" name="dataId"/>
            <input type="hidden" id="keyName3" name="keyName"/>
            <input type="hidden" id="username3" name="username"/>
            <input type="hidden" id="userId3" name="userId"/>
            <input type="hidden" id="comment3" name="comment"/>
    	</sf:form>
					
    	<c:url value="/reviewer/createDiscrepencyGroup" var="createDiscrepencyGroup" />
    	<sf:form action="${createDiscrepencyGroup}" method="POST" modelAttribute="crfpojo"
    		id="groupDescSave" enctype="multipart/form-data">
            <input type="hidden" id="vpcId4" name="vpcId"/>
            <input type="hidden" id="crfId4" name="crfId"/>
            <input type="hidden" id="eleId4" name="eleId"/>
            <input type="hidden" id="dataId4" name="dataId"/>
            <input type="hidden" id="keyName4" name="keyName"/>
            <input type="hidden" id="username4" name="username"/>
            <input type="hidden" id="userId4" name="userId"/>
            <input type="hidden" id="comment4" name="comment"/>
    	</sf:form>
    	
    	<c:url value="/reviewer/subjectPeriodCrfsDataReview" var="formsubmit" />
    	<sf:form action="${formsubmit}" method="POST" modelAttribute="crfpojo"
    		id="formsubmit" enctype="multipart/form-data">
    		<input type="hidden" name="vpcId" value="${vpcId }">
    	</sf:form>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$('#discDataModal').modal('hide');
		$('#signacherModal').modal('hide');
	});
	function signacherPopup(){
		$('#signacherModal').modal('show');
	}
	function acceptsignCheckBox() {
		if ($('#acceptsignCheckId').is(':checked')) {
			$('#signacherModal').modal('hide');
	  	}else{
			aler("Please check checkbox...");
	  	}
	}
	function subjectPeriodCrfsDataReview(){
		$("#tpwmsg").html("");
		var signcheck = asynchronousAjaxCall(mainUrl+"/reviewer/signcheck/"+$("#tpw").val());
		alert(signcheck);
		$("#signcheck").val(signcheck.trim());
		if($("#signcheck").val() == "yes"){
			$('#signacherModal').modal('hide');
			$("#formsubmit").submit();
		}else{
			$("#tpw").val("");
			$("#tpwmsg").html("In-valied password");
		}
			
	}
// 	tranPassword tpw
	function createDiscrepency(key, id){
		var result = asynchronousAjaxCall(mainUrl+"/reviewer/discDataNew/"+key+"/"+id+"/"+
				$("#crfId").val()+"/"+$("#vpcId").val());
		if(result != 'undefined' && result != ''){
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		}else
			alert("Unable to display discrepency page");
	}
	function createDiscrepencySec(key, id, condition){
		var result = asynchronousAjaxCall(mainUrl+"/reviewer/discDataNewSecView/"+key+"/"+id+"/"+
				$("#crfId").val()+"/"+$("#vpcId").val()+"/"+condition);
		if(result != 'undefined' && result != ''){
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		}else
			aler("Unable to display discrepency page");
	}
	
	
	function createDiscrepencyGroup(key, id){
		$('#discDataModal').modal('hide');
		var result = asynchronousAjaxCall(mainUrl+"/reviewer/discDataNewGroup/"+key+"/"+id+"/"+
				$("#crfId").val()+"/"+$("#vpcId").val());
		if(result != 'undefined' && result != ''){
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		}else
			alert("Unable to display discrepency page");
	}
	
	function createDiscrepencyGroupView(key, id, condition){
		$('#discDataModal').modal('hide');
		var result = asynchronousAjaxCall(mainUrl+"/reviewer/discDataGroupView/"+key+"/"+id+"/"+
				$("#crfId").val()+"/"+$("#vpcId").val()+"/"+condition);
		if(result != 'undefined' && result != ''){
			$("#discData").html(result);
			$('#discDataModal').modal('show');
		}else
			alertify.error("Unable to display discrepency page");
	}
	
	
	
	function saveDisc() {
		$("#userIdmsg").html("");
		$("#commentdescmsg").html("");
		var flag = true;
		if($("#userId").val() == -1 ){
			$("#userIdmsg").html("Required Field"); flag =false;
		}
		if($("#commentdesc").val().trim() == "" ){
			$("#commentdescmsg").html("Required Field"); flag =false;
		}
		if(flag){
			var vpcId = $("#vpcId2").val();
			var crfId = $("#crfId2").val();
			var eleId = $("#eleId").val();
			var dataId = $("#dataId").val();
			var keyName = $("#kayName").val();
			var username = $("#username").val();
			var userId = $("#userId").val();
			var comment = $("#commentdesc").val();
			
			$("#vpcId3").val(vpcId);
			$("#crfId3").val(crfId);
			$("#eleId3").val(eleId);
			$("#dataId3").val(dataId);
			$("#keyName3").val(keyName);
			$("#username3").val(username);
			$("#userId3").val(userId);
			$("#comment3").val(comment);
			$("#secDescSave").submit();
			
			alert("asdf")
/* 			var result = asynchronousAjaxCall(mainUrl+"/reviewer/createDiscrepency/"
					+vpcId+"/"+crfId+"/"+eleId+"/"+dataId+"/"+keyName+"/"+username+"/"+userId+"/"+comment);
			if(result != '' || result != 'undefined'){
		    	$('#discDataModal').modal('hide');
			} */
		}
	}
	
	function saveDiscGroup() {
		$("#userIdmsg").html("");
		$("#commentdescmsg").html("");
		var flag = true;
		if($("#userId").val() == -1 ){
			$("#userIdmsg").html("Required Field"); flag =false;
		}
		if($("#commentdesc").val().trim() == "" ){
			$("#commentdescmsg").html("Required Field"); flag =false;
		}
		if(flag){
			var vpcId = $("#vpcId2").val();
			var crfId = $("#crfId2").val();
			var eleId = $("#eleId").val();
			var dataId = $("#dataId").val();
			var keyName = $("#kayName").val();
			var username = $("#username").val();
			var userId = $("#userId").val();
			var comment = $("#commentdesc").val();
			
			$("#vpcId4").val(vpcId);
			$("#crfId4").val(crfId);
			$("#eleId4").val(eleId);
			$("#dataId4").val(dataId);
			$("#keyName4").val(keyName);
			$("#username4").val(username);
			$("#userId4").val(userId);
			$("#comment4").val(comment);
	        $("#groupDescSave").submit();    
// 			var result = asynchronousAjaxCall(mainUrl+"/reviewer/createDiscrepencyGroup/"
// 					+vpcId+"/"+crfId+"/"+eleId+"/"+dataId+"/"+keyName+"/"+username+"/"+userId+"/"+comment);
// 			if(result != '' || result != 'undefined'){
// 		    	$('#discDataModal').modal('hide');
// 			}
		}
	}
	function acceptCheckBox() {
		if ($('#acceptCheckId').is(':checked')) {
			$('#discDataModal').modal('hide');
	  	}else{
			aler("Please check checkbox...");
	  	}
	}
</script>
</html>