<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sub Group Observations</title>
</head>
<body>
	<input type="hidden" name="gid" id="gid" value="${subjects.group.id}"/>
	<input type="hidden" name="sgid" id="sgid" value="${subjects.id}"/>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Subgroup Observations</b>
			</h3>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped">
			<tr>
				
			</tr>
			  <tr><th colspan="6">Observation Name</th></tr>
<!-- 			 </table> -->
			  <c:forEach items="${obvList}" var="obv">
			  		<!-- <tr>
						<td> -->
							<div id="subject_${obv.subGroupInfo.id }">
<!-- 								<table class="table table-bordered table-striped"> -->
										<tr>
												<td>${obv.crf.prefix }</td>
<%-- 												<c:if test="${obv.crfConfig}"> --%>
<!-- 													<td> -->
<%-- 														<c:choose> --%>
<%-- 															<c:when test="${obv.allowDataEntry}"> --%>
<%-- 																<input type="button" id="crfDataEntry" value="Scheduled Entry" onclick="crfDataEntry('${obv.subGroupInfo.id}', '${obv.id}', 'Scheduled')" --%>
<!-- 														class="btn btn-primary"/> -->
<%-- 															</c:when> --%>
<%-- 															<c:otherwise> --%>
<!-- 															<input type="button" id="crfDataEntry" value="Scheduled Entry" disabled="disabled" class="btn btn-primary"/> -->
<%-- 															</c:otherwise> --%>
<%-- 														</c:choose> --%>
<!-- 													</td> -->
													
													<td>
														<input type="button" id="crfDataEntry" value="Un-Scheduled Entry" onclick="crfDataEntry('${obv.subGroupInfo.id}', '${obv.id}', 'Unscheduled')"
														class="btn btn-primary"/>
													</td>
													
<%-- 												</c:if> --%>
												<c:if test="${obv.templeteConfig}">
													<td>
														<input type="button" id="crfDataEntry" value="Template File" onclick="templateViewFunction('${obv.subGroupInfo.id}', '${obv.crf.templete.id}', '${obv.crf.templete.obserVationName}')"
														class="btn btn-warning btn-sm" style="width: 150px;" />
													</td>
												</c:if>
												<td>
												<a
												href="<c:url value="/studyReview/reviewObservationForm/${obv.id}/view"/>"><input
													type="button" value="View" class="btn btn-primary btn-sm"></a>
<%-- 													<input type="button" id="crfDataView" value="View" onclick="crfDataView2('${obv.subGroupInfo.id}', '${obv.id}')" --%>
<!-- 													class="btn btn-primary" style="width: 150px;" /> -->
												</td>
											<td>
												<input type="button" id="crfDataExport" value="Export" onclick="crfDataExport('${obv.id}')"
													class="btn btn-primary" style="width: 150px;" />
											</td>
												
											
										</tr>
<!-- 								</table> -->
							</div>
					<!-- 	</td>
					</tr> -->
			  </c:forEach>
			</table>
		</div>
		 <c:url value="/obtemplate/templateFileDetails" var="templateUrl" />		 
 		<sf:form action="${templateUrl}" method="GET" id="templateForm">
 			<input type="hidden" name="studyId" id="studyId" value="${subjects.study.id}"/>
     		<input type="hidden" name="groupIdVal" id="groupIdVal" value="${subjects.group.id}"/>
     		<input type="hidden" name="subGroupIdVal" id="subGroupIdVal" value="${subjects.id}"/>
     		<input type="hidden" name="subGroupanimalId" id="subGroupanimalId" value="0"/>
     		<input type="hidden" name="templateId" id="templateId" value="0"/>
     		<input type="hidden" name="ObservationName" id="ObservationName" value="0"/>
     	</sf:form> 
 		<c:url value="/studyExecution/animalCrfDataEntry" var="dataEntryUrl" />		 
 		<sf:form action="${dataEntryUrl}" method="POST" id="entryForm">
 		    <input type="hidden" name="cageId" id="cageId"/>
     		<input type="hidden" name="groupId" id="groupId1"/>
     		<input type="hidden" name="subGroupId" id="subGroupId1"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1"/><!--subejct -->
     		<input type="hidden" name="type" id="type">
     		<input type="hidden" name="crfId" id="crfId1"/><!-- observationId -->
     	</sf:form> 
     	
     	<c:url value="/studyExecution/animalCrfDataView" var="datVuewUrl" />		 
 		<sf:form action="${datVuewUrl}" method="POST" id="viewForm">
     		<input type="hidden" name="groupId" id="groupId2"/>
     		<input type="hidden" name="subGroupId" id="subGroupId2"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId2"/><!--subejct -->
     		<input type="hidden" name="crfId" id="crfId2"/><!-- observationId -->
     	</sf:form>
     	<c:url value="/studyExecution/animalCrfDataExportTwo" var="dataExportUrl" />		 
 		<sf:form action="${dataExportUrl}" method="POST" id="crfDataExportForm">
     		<input type="hidden" name="crfId" id="crfId3"/><!-- observationId -->
     	</sf:form> 
     	<c:url value="/crfAudit/allAnimalCrfDataReviewViewAudit" var="dataEntryUrl2" />		 
 		<sf:form action="${dataEntryUrl2}" method="POST" id="entryForm2">
     		<input type="hidden" name="review" id="review4" value="review"/>
     		<input type="hidden" name="groupId" id="groupId4"/>
     		<input type="hidden" name="subGroupId" id="subGroupId4"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId4"/><!--subejct -->
     		<input type="hidden" name="stdSubGroupObservationCrfsId" id="stdSubGroupObservationCrfsId4"/><!-- observationId -->
     	</sf:form> 
	</div>
	<script type="text/javascript">
	var justBefore = "";
	$.each(subject, function(k, v) {
		$("#subject_"+v).hide();
	});
// 	$(document).ready(function(){
// 		$.each(subject, function(k, v) {
// 			$("#subject_"+v).hide();
// 		});
// 	});
	function showSubGroup(id){
		$.each(subject, function(k, v) {
			$("#subject_"+v).hide();
		});
		if(justBefore != id){
			$("#subject_"+id).show();
			justBefore = id;
		}else{
			justBefore = "";
		}
	}
	function templateViewFunction(subId, obsTempId, ObservationName){
		$("#subGroupanimalId").val(subId);
		$("#templateId").val(obsTempId);
		$("#ObservationName").val(ObservationName);
		$("#templateForm").submit();
	}
	function crfDataEntry(subId, crfId, type){
		debugger;
		$("#cagenomsg").html("");
		var cageno = $("#cageno").val();
		
		if(cageno != -1 || type == 'Unscheduled'){
			$("#cageId").val(cageno);
			$("#groupId1").val($("#gid").val());
			$("#subGroupId1").val($("#sgid").val());
			$("#subGroupInfoId1").val(subId);
			$("#crfId1").val(crfId);
			$('#type').val(type);
			$("#entryForm").submit();
		}else{
			$("#cagenomsg").html("Required Field");
		}
		
	}
	function crfDataView(subId, crfId){
		$("#groupId2").val($("#gid").val());
		$("#subGroupId2").val($("#sgid").val());
		$("#subGroupInfoId2").val(subId);
		$("#crfId2").val(crfId);
		$("#viewForm").submit();
	}
	function crfDataExport(crfId){
		$("#crfId3").val(crfId);
		$("#crfDataExportForm").submit();
	}
	function crfDataView2(subId, stdSubGroupObservationCrfsId){
		$("#groupId4").val($("#gid").val());
		$("#subGroupId4").val($("#sgid").val());
		$("#subGroupInfoId4").val(subId);
		$("#stdSubGroupObservationCrfsId4").val(stdSubGroupObservationCrfsId);
		$("#entryForm2").submit();
	}
	
</script>
</body>
</html>