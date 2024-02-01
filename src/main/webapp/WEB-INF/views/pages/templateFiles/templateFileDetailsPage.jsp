<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Template File Details</title>
<style type="text/css">
.swal-wide2{
    width:500px !important;
/*     height:300px !important; */

}
.swal-wide4{
    width:900px !important;
    height:300px !important;
}
</style>
</head>
<body>
<div class="card">
   <div class="card" style="background-color: rgba(51, 110, 123, 1); border-radius: 20px; border: 3px solid #f3fff0;">
		<h5 class="card-header info-color white-text text-center py-2">
			<strong style="color: white;">Template Details</strong>
		</h5>
	</div>
	<div id="templateMsg" style="color: red; text-align: center; font-weight: bold; font-size: medium;"></div>
	<table class="table table-bordered table-striped">
		<tr>
			<td>Study Name : </td>
			<td>${tfdto.sgaia.subGroup.study.studyNo}</td>
			<td>Group Name : </td>
			<td>${tfdto.sgaia.subGroup.group.groupName}</td>
			<td>Sub Group Name : </td>
			<td>${tfdto.sgaia.subGroup.name}</td>
		</tr>
		<tr>
			<td>Animal Number :</td>
			<td>${tfdto.sgaia.animalNo}</td>
			<td>Observation : </td>
			<td colspan="3">${ObservationName}</td>
			
		</tr>
		<tr align="center">
			<td colspan="10">
				<button type="button" class="btn btn-info active" onclick="viewTemplateFile()">View Template</button>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when test="${(tfdto.slotdata.id ne null and tfdto.slotdata.id ne '') and tfdto.fileStatus eq 'assigned' or tfdto.fileStatus eq 'reassigned'}">
						<a href='#' onclick="modifyTemplateStatus('${tfdto.slotdata.id}','reviewed')"><button type="button" class="btn btn-info active">Review Complete</button></a>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-info active" disabled="disabled">Review Complete</button>
					</c:otherwise>
				</c:choose>
				
				&nbsp;&nbsp;&nbsp;&nbsp;
				<c:choose>
					<c:when test="${tfdto.slotdata.id eq '' or tfdto.fileStatus eq 'assigned' or tfdto.fileStatus eq 'reassigned'}">
						<button type="button" class="btn btn-info active" disabled="disabled">Re-Assign</button>
					</c:when>
					<c:otherwise>
						<a href='#' onclick="modifyTemplateStatus('${tfdto.slotdata.id}','reassigned')"><button type="button" class="btn btn-info active">Re-Assign</button></a>
					</c:otherwise>
				</c:choose>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href='#' onclick="templateAuditTrail('${tfdto.slotdata.id}')"><button type="button" class="btn btn-info active">Field Level Audit</button></a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href='#' onclick="reviewComments('${tfdto.slotdata.id}', 'reviewComments')"><button type="button" class="btn btn-info active">Review Comments</button></a>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<a href='#' onclick="reAssignComments('${tfdto.slotdata.id}', 'reassignComments')"><button type="button" class="btn btn-info active">Re-Assign Comments</button></a>
				
			</td>
		</tr>
	</table>
	 	<c:url value="/obtemplate/templateView" var="templateUrl" />		 
 		<form:form action="${templateUrl}" method="POST" id="templateForm">
 			<input type="hidden" name="studyId" id="studyId" value="${tfdto.sgaia.subGroup.study.id}"/>
     		<input type="hidden" name="groupIdVal" id="groupIdVal" value="${tfdto.sgaia.subGroup.group.id}"/>
     		<input type="hidden" name="subGroupIdVal" id="subGroupIdVal" value="${tfdto.sgaia.subGroup.id}"/>
     		<input type="hidden" name="subGroupanimalId" id="subGroupanimalId" value="${tfdto.sgaia.id}"/>
     		<input type="hidden" name="templateId" id="templateId" value="${templateId}"/>
     		<input type="hidden" name="ObservationName" id="ObservationName" value="${ObservationName}"/>
     	</form:form> 
     <div id="templateFile"></div>
     <div id="auditInfo"></div>
</div>
<script type="text/javascript">
	function viewTemplateFile(){
		$('#auditInfo').html("");
		var url1 = mainUrl+"/obtemplate/templateView";
// 		var result= asynchronousAjaxCall(url);
		var studyId = $('#studyId').val();
		var groupIdVal = $('#groupIdVal').val();
		var subGroupIdVal = $('#subGroupIdVal').val();
		var subGroupanimalId = $('#subGroupanimalId').val();
		var templateId = $('#templateId').val();
		var ObservationName = $('#ObservationName').val();
// 		alert("url is : "+url1);
// 		alert("values are : "+studyId+"::"+groupIdVal+"::"+subGroupIdVal+"::"+subGroupanimalId+"::"+templateId+"::"+ObservationName);
		$.ajax({
            type: "get",
            url: url1,
            data:{ "studyId": studyId,"groupIdVal": groupIdVal, "subGroupIdVal": subGroupIdVal, 
            		"subGroupanimalId": subGroupanimalId, "templateId": templateId,"ObservationName": ObservationName},
            success: function (data) {
               if(data != ""){
            	   $('#templateFile').html(data);
               }
            }
	    });
		
// 		$('#templateForm').submit();
	}
	function modifyTemplateStatus(templateId, tempStatus){
		var labelStr = "";
		if(tempStatus == "reviewed")
			labelStr = "Comments For Review";
		else 
			labelStr = "Comments For Re-Assign";
		new swal({
	  		  html:"",
	  		  input: 'textarea',
	  		  customClass: 'swal-wide4',
	  		  inputLabel: labelStr,
	  		  inputPlaceholder: 'Type your Comments here...',
	  		  showCancelButton: true,
	  	 }).then(function (result) {
				if (result.value) {
					var url = mainUrl + "/obtemplate/modifyTemplateStatus/"+templateId+"/"+tempStatus+"/"+result.value;
					var result3 = asynchronousAjaxCall(url);
					if(tempStatus == "reviewed"){
						if(result3 == "Done"){
			         		  new swal(
			     				  	'Done',
			     				    'Template Review Process Done. Successfully...!',
			     				    'success'
			 	              );
			         		  $(".close").click();
			         		location.reload();
			         	    }else{
		            	  	  var erStr ="Template Review Failed. Please Try Again.";
		            	  	  new swal(
		                    	  'Oops...',
		                    	  erStr,
		                    	  'error'
			                  );
			            	}
					}else{
						if(result3 == "Done"){
			         		  new swal(
			     				  	'Done',
			     				    'Template Re-Assigning Process Done. Successfully...!',
			     				    'success'
			 	              );
			         		  $(".close").click();
			         		location.reload();
		         	    }else{
		            	  	  var erStr ="Template Re-Assigning Failed. Please Try Again.";
		            	  	  new swal(
		                    	  'Oops...',
		                    	  erStr,
		                    	  'error'
			                  );
		            	 }
					}
				}
	  	}); 
	}
	function templateAuditTrail(templateId){
		$('#templateFile').html("");
		var url = mainUrl+"/obtemplate/fieldLevelAudit/"+templateId;
		var result= asynchronousAjaxCall(url);
		$('#auditInfo').html(result);
	}
	function reviewComments(templateId, comStr){
		var url = mainUrl+"/obtemplate/getCommentsInformation/"+templateId+"/"+comStr;
		var result= asynchronousAjaxCall(url);
		$('#auditInfo').html(result);
	}
	function reAssignComments(templateId, comStr){
		var url = mainUrl+"/obtemplate/getCommentsInformation/"+templateId+"/"+comStr;
		var result= asynchronousAjaxCall(url);
		$('#auditInfo').html(result);
	}
</script>
</body>
</html>