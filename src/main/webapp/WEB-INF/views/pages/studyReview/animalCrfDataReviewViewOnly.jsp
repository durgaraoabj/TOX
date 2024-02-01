<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1" --%>
<%-- 	pageEncoding="ISO-8859-1"%> --%>
<%-- <%@ page isELIgnored="false" %> --%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<%-- <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%> --%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> --%>
<!-- <html> -->
<!-- 	<body> -->
		
<!-- 		<div class="card"> -->
<!--             <div class="card-header"> -->
<!--               <h3 class="card-title">Observation :</h3> -->
<!--               <table class="table table-bordered table-striped"> -->
<%-- 				<tr><td>Study : ${data.study.studyNo }</td><td colspan="4">Observation Data Entry For : ${data.crfOrObservation.observationName}</td></tr> --%>
<!-- 			</table> -->
			
			
			
<!--             </div> -->
<!--             /.card-header -->
<!--             <div class="card-body"> -->
<!--             	<div style="width: 100%; overflow: scroll"> -->
            		
<%--             		<c:url value="/studyReview/approveObservation" var="saveObservationReview" /> --%>
<%-- 			    	<sf:form action="${saveObservationReview}" method="POST"  id="saveReview"> --%>
<%-- 		        	<input type="hidden" name="subGroupInfoId" id="subGroupInfoId" value="${subGroupInfoId}"> --%>
<%-- 		        	<input type="hidden" name="stdSubGroupObservationCrfsId" id="stdSubGroupObservationCrfsId" value="${stdSubGroupObservationCrfsId}"> --%>
<!-- 		        	<table class="table table-bordered table-striped"> -->
<!-- 						<tr> -->
<!-- 							<th>Count  -->
<!-- 								</th> -->
<!-- 							<th>Date Deviation</th> -->
<!-- 							<th>Frequently Deviation</th> -->
<!-- 							<th>Entered By</th> -->
<!-- 							<th>Entered On</th> -->
<!-- 							<th>Entry NO</th> -->
							
<!-- 							<th>Animal No</th> -->
<!-- 							<th>Group</th> -->
<!-- 							<th>Sub Group</th> -->
<!-- 							<th>Dose</th> -->
<!-- 							<th>Gender</th> -->
<%-- 							<th>${data.crfOrObservation.dayType }</th> --%>
<%-- 							<c:forEach items="${data.elements }" var="ele"> --%>
<!-- 								<th> -->
<%-- 									<c:choose> --%>
<%-- 										<c:when test="${ele.value.leftDesc ne null and ele.value.leftDesc ne ''}"> --%>
<%-- 											${ele.value.leftDesc} --%>
<%-- 										</c:when> --%>
<%-- 										<c:when test="${ele.value.rigtDesc ne null and ele.value.rigtDesc ne ''}"> --%>
<%-- 											${ele.value.rigtDesc} --%>
<%-- 										</c:when> --%>
<%-- 										<c:when test="${ele.value.topDesc ne null and ele.value.topDesc ne ''}"> --%>
<%-- 											${ele.value.topDesc} --%>
<%-- 										</c:when> --%>
<%-- 										<c:when test="${ele.value.bottemDesc ne null and ele.value.bottemDesc ne ''}"> --%>
<%-- 											${ele.value.bottemDesc} --%>
<%-- 										</c:when> --%>
<%-- 										<c:when test="${ele.value.middeDesc ne null and ele.value.middeDesc ne ''}"> --%>
<%-- 											${ele.value.middeDesc} --%>
<%-- 										</c:when> --%>
<%-- 										<c:otherwise> --%>
<%-- 											${ele.value.totalDesc} --%>
<%-- 										</c:otherwise> --%>
<%-- 									</c:choose> --%>
<%-- <%-- 								${ele.value.leftDesc } --%> --%>
<!-- 								</th> -->
<%-- 							</c:forEach> --%>
<!-- 						</tr> -->
						
<%-- 						<c:forEach items="${data.animalData }" var="ele" varStatus="st"> --%>
<!-- 							<tr> -->
<!-- 								<td> -->
<%-- 									${st.count } --%>
									
<%-- 									<input type="hidden" name="discrepencyClosed" value="${ele.discrepencyClosed}" id="desc_${ele.id}"/> --%>
<!-- 								</td> -->
<%-- 								<td>${ele.deviationMessage}</td> --%>
<%-- 								<td>${ele.frequntlyMessage}</td> --%>
<%-- 								<td>${ele.userName }</td> --%>
<%-- 								<td>${ele.date }</td> --%>
<%-- 								<td>${ele.count }</td> --%>
<%-- 								<td>${data.animalAll.animalNo}</td> --%>
<%-- 								<td>${data.group.groupName }</td> --%>
<%-- 								<td>${data.subGroup.name}</td> --%>
<%-- 								<td>${data.subGroup.dose }</td> --%>
<%-- 								<td>${data.animal.gender}</td> --%>
<%-- 								<td>${ele.dayOrWeek }</td>			 --%>
<%-- 								<c:forEach items="${ele.elementData }" var="eleData"> --%>
<!-- 									<td> -->
<%-- 										${eleData.value.value } --%>
<%-- <%-- 										<br/>${eleData.value.id}						 --%> --%>
<%-- 										<c:choose> --%>
<%-- 											<c:when test="${eleData.value.discripency eq 0 and eleData.value.discripencyClose eq 0}"> --%>
<%-- 												<c:if test="${ele.reviewed eq false }"> --%>
<%-- 													<img src='<c:url value="/static/template/dist/img/flag_wite.png"/>'  --%>
<!-- 													class="img-circle" title="New Discrepancy" width="16" height="16"  -->
<%-- 													onclick="createDiscrepency('${eleData.value.id }')">												 --%>
<%-- 												</c:if> --%>

<%-- 											</c:when> --%>
<%-- 											<c:when test="${eleData.value.discripency gt 0}"> --%>
<%-- 												<img src='<c:url value="/static/template/dist/img/flag_red.png"/>'  --%>
<!-- 													class="img-circle" title="Open" width="16" height="16" -->
<%-- 													onclick="createDiscrepency('${eleData.value.id }','reviewer')"> --%>
<%-- 											</c:when> --%>
<%-- 											<c:when test="${eleData.value.discripencyClose gt 0}"> --%>
<%-- 												<img src='<c:url value="/static/template/dist/img/flag_black.png"/>'  --%>
<!-- 														class="img-circle" title="Closed" width="16" height="16" -->
<%-- 														onclick="createDiscrepency('${eleData.value.id }','all')"> --%>
<%-- 											</c:when> --%>
<%-- 										</c:choose> --%>
<!-- 									</td> -->
<%-- 								</c:forEach> --%>
<!-- 							</tr> -->
<%-- 						</c:forEach> --%>
<!-- 					</table> -->
<%-- 					</sf:form> --%>
<!--             	</div> -->
<!--             </div> -->
<!--             /.card-body -->
<!--           </div> -->
<!--           /.card -->
          
<!--           <div class="modal fade" id="discDataModal" tabindex="-1" role="dialog" aria-labelledby="discDataModal" aria-hidden="true" data-backdrop="static" data-keyboard="false"> -->
<!-- 			<div class="modal-dialog modal-lg"> -->
<!-- 		    	<div class="modal-content"> -->
<!-- 		      		<div class="modal-header"> -->
<!-- 		        		<h4 class="modal-title" id="myModalLabel">New Discrepancy</h4>  -->
<!-- 		      		</div> -->
<!-- 		      		<div class="modal-body"> -->
<!-- 		      			<div id="discData"> -->
<!-- 		      			</div> -->
<!-- 		        	</div> -->
<!-- 		      		<div class="modal-footer"> -->
<!-- 		      			<div style="text-align: left;"> -->
<!-- 		      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptCheckId" />&nbsp;&nbsp;&nbsp;Do You want to close page.</label> -->
<!-- 		      			</div> -->
<!-- 		      			<div style="text-align: right;"> -->
<!-- 		        		<input type="button" class="btn btn-primary" onclick="acceptCheckBox()" value="Close"/> -->
<!-- 		        		</div> -->
<!-- 		        	</div> -->
<!-- 		    	</div> -->
<!-- 		  	</div> -->
<!-- 		</div> -->
		
<%-- 		<c:url value="/studyReview/createDiscrepency" var="createDiscrepency" /> --%>
<%--     	<sf:form action="${createDiscrepency}" method="POST" modelAttribute="crfpojo"  --%>
<%--     		id="secDescSave" enctype="multipart/form-data"> --%>
<!--             <input type="hidden" id="dataId3" name="dataId"/> -->
<!--             <input type="hidden" id="userId3" name="userId"/> -->
<!--             <input type="hidden" id="comment3" name="comment"/> -->
<%--             <input type="hidden" name="subGroupInfoId" value="${subGroupInfoId}"/> --%>
<%-- 			<input type="hidden" name="stdSubGroupObservationCrfsId" value="${stdSubGroupObservationCrfsId}"/> --%>
<%--     	</sf:form> --%>
    	
<!-- 	</body> -->
<!-- 	<script type="text/javascript"> -->
// 	function approveObservations(){
// 		if($("#descall").is(":checked")){
// 			var desc = $("#descall").val();
// 			if(desc > 0){
// 				alert("Observation's Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
// 			}else{
// 				$("#saveReview").submit();
// 			}
// 		}else{
// 			var checked = 0;
// 			var descs = 0;
// 			$.each($("input[name='descElement']:checked"), function() {
// 				checked ++;
// 				descs += $("#desc_"+this.id).val();
// 			});
// 			if(checked > 0){
// 				if(descs > 0){
// 					alert("Observation Contains Un-closed Descrepancy's. Untill close those all, can't Approve.")
// 				}else{
// 					$("#saveReview").submit();
// 				}				
// 			}else{
// 				alsert("Select atleast one checkbox.")
// 			}

// 		}
// 	}
// 	function selectAll(){
// 		if($("#descall").is(":checked")){
// 			$("input[name='descElement']").each(function() {
// 				$("#"+this.id).attr('checked', true); // Checks it
// 			});
// 		}else{
// 			$("input[name='descElement']").each(function() {
// 				$("#"+this.id).attr('checked', false); // Checks it
// 			});
// 		}
// 	}
// 	function acceptCheckBox() {
// 		if ($('#acceptCheckId').is(':checked')) {
// 			$('#discDataModal').modal('hide');
// 	  	}else{
// 			aler("Please check checkbox...");
// 	  	}
// 	}
// 		function createDiscrepency(id){
// 			var result = asynchronousAjaxCall(mainUrl+"/studyReview/discDataNew/"+id);
// 			if(result != 'undefined' && result != ''){
// 				$("#discData").html(result);
// 				$('#discDataModal').modal('show');
// 			}else
// 				alert("Unable to display discrepency page");
// 		}
		
// 		function saveDisc() {
// // 			alert("asdf")
// 			$("#userIdmsg").html("");
// 			$("#commentdescmsg").html("");
// 			var flag = true;
// 			if($("#userId").val() == -1 ){
// 				$("#userIdmsg").html("Required Field"); flag =false;
// 			}
// 			if($("#commentdesc").val().trim() == "" ){
// 				$("#commentdescmsg").html("Required Field"); flag =false;
// 			}
// 			if(flag){
// 				var dataId = $("#dataId").val();
// 				var userId = $("#userId").val();
// 				var comment = $("#commentdesc").val();
				
// 				$("#dataId3").val(dataId);
// 				$("#userId3").val(userId);
// 				$("#comment3").val(comment);
// 				$("#secDescSave").submit();
				
// // 				alert(mainUrl+"/studyReview/createDiscrepency/")
// 	/* 			var result = asynchronousAjaxCall(mainUrl+"/reviewer/createDiscrepency/"
// 						+vpcId+"/"+crfId+"/"+eleId+"/"+dataId+"/"+keyName+"/"+username+"/"+userId+"/"+comment);
// 				if(result != '' || result != 'undefined'){
// 			    	$('#discDataModal').modal('hide');
// 				} */
// 			}
// 		}
<!-- 	</script> -->
<!-- </html> -->

		
          
