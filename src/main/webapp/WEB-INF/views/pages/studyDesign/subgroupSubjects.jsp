<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
	var subject = [];
</script>
<body>
	<input type="hidden" name="gid" id="gid" value="${subjects.group.id}"/>
	<input type="hidden" name="sgid" id="sgid" value="${subjects.id}"/>
	
	<div class="card">
		<div class="card-header">
			<h3 class="card-title"><b>Animal With Observation</b></h3><br/>
			<table>
				<tr>
					<th>Group Name :</th>
					<th>${subjects.group.groupName }</th>
					<th>Sub Group Name : </th>
					<th>${subjects.name }</th>
				</tr>
			</table>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped">
				<tr><th>Animal Number</th></tr>
				<c:forEach items="${subjects.animalInfoAll }" var="sub">
					<tr>
						<td>
							<div onclick="showSubGroup('${sub.id }')" style="color: blue">${sub.animalNo }</div>
							<div id="subject_${sub.id }">
								<c:if test="${sub.observation.size() gt 0}">
									<table class="table table-bordered table-striped">
										<tr><th colspan="3">Observation Name</th></tr>
										<c:forEach items="${sub.observation}" var="crf">
											<tr>
												<td>${crf.observationName }</td>
												<td>
													<c:if test="${crf.crfConfig}">
														<input type="button" id="crfDataEntry" value="CRF Data Entry" onclick="crfDataEntry('${sub.id}', '${crf.id}')"
														class="btn btn-primary" style="width: 150px;" />
													</c:if>
													<c:if test="${crf.templeteConfig}">
														<input type="button" id="crfDataEntry" value="Template File" onclick="templateViewFunction('${sub.id}', '${crf.crf.templete.id}', '${crf.crf.templete.obserVationName}')"
														class="btn btn-warning btn-sm" style="width: 150px;" />
													</c:if>
													
												</td>
												<td>
													<input type="button" id="crfDataView" value="View" onclick="crfDataView('${sub.id}', '${crf.id}')"
														class="btn btn-primary" style="width: 150px;" />
												</td>
												<td>
													<input type="button" id="crfDataExport" value="Export" onclick="crfDataExport('${sub.id}', '${crf.id}')"
														class="btn btn-primary" style="width: 150px;" />
												</td>
<!-- 												<td> -->
<%-- 													<div onclick="gotoSubjectSelection('${sg.id }')" style="color: blue">${sg.observationName }</div> --%>
<!-- 												</td> -->
											</tr>
										</c:forEach>
									</table>
								</c:if>
							</div>
							
						</td>
					</tr>
					<script >
						subject.push('${sub.id }');
					</script>
				</c:forEach>
			</table>
		</div>
	</div>
<!-- 		<h6 class="box-title"> -->
<%-- 			Observation Name : ${crfviewTest.observationName }&nbsp;&nbsp;&nbsp;&nbsp; --%>
<%-- 	    	Observation Desc : ${crfviewTest.observationDesc }&nbsp;&nbsp;&nbsp;&nbsp; --%>
<!--     	</h6> -->
    	   
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
     		<input type="hidden" name="groupId" id="groupId1"/>
     		<input type="hidden" name="subGroupId" id="subGroupId1"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1"/><!--subejct -->
     		<input type="hidden" name="crfId" id="crfId1"/><!-- observationId -->
     	</sf:form> 
     	
     	<c:url value="/studyExecution/animalCrfDataView" var="datVuewUrl" />		 
 		<sf:form action="${datVuewUrl}" method="POST" id="viewForm">
     		<input type="hidden" name="groupId" id="groupId2"/>
     		<input type="hidden" name="subGroupId" id="subGroupId2"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId2"/><!--subejct -->
     		<input type="hidden" name="crfId" id="crfId2"/><!-- observationId -->
     	</sf:form>
     	<c:url value="/studyExecution/animalCrfDataExport" var="dataExportUrl" />		 
 		<sf:form action="${dataExportUrl}" method="POST" id="crfDataExportForm">
     		<input type="hidden" name="groupId" id="groupId3"/>
     		<input type="hidden" name="subGroupId" id="subGroupId3"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId3"/><!--subejct -->
     		<input type="hidden" name="crfId" id="crfId3"/><!-- observationId -->
     	</sf:form> 
     	
     	
</body>
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
	function crfDataEntry(subId, crfId){
		$("#groupId1").val($("#gid").val());
		$("#subGroupId1").val($("#sgid").val());
		$("#subGroupInfoId1").val(subId);
		$("#crfId1").val(crfId);
		$("#entryForm").submit();
	}
	function crfDataView(subId, crfId){
		$("#groupId2").val($("#gid").val());
		$("#subGroupId2").val($("#sgid").val());
		$("#subGroupInfoId2").val(subId);
		$("#crfId2").val(crfId);
		$("#viewForm").submit();
	}
	function crfDataExport(subId, crfId){
		$("#groupId3").val($("#gid").val());
		$("#subGroupId3").val($("#sgid").val());
		$("#subGroupInfoId3").val(subId);
		$("#crfId3").val(crfId);
		$("#crfDataExportForm").submit();
	}
	
</script>
</html>