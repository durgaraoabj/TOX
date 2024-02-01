<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/site/createSiteSave" var="createSiteSave" />
<sf:form action="${createSiteSave}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Site Creation Page</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Site Name:*</td>
				<td><input type="text" class="form-control" name="siteName" id="siteName" onblur="checkSiteName(this.value)"/><font color="red" id="siteNamemsg"></font></td>
				<td>Unique Protocol ID:*</td>
				<td><input type="text" class="form-control" name="protocalId" id="protocalId"/><font color="red" id="protocalIdmsg"></font></td>
			</tr>
			<tr>
				<td>Secondary IDs (separate by commas):</td>
				<td><textarea class="form-control" name="secondaryIDs" id="secondaryIDs"></textarea></td>
				<td>Principal Investigator:*</td>
				<td><input type="text" class="form-control" name="principalInvestigator" id="principalInvestigator"/><font color="red" id="principalInvestigatormsg"></font></td>
			</tr>
			<tr>
				<td>Brief Summary:</td>
				<td><textarea class="form-control" name="briefSummary" id="briefSummary"></textarea></td>
				<td>Protocol Verification/IRB Approval Date:</td> 
				<td><input type="text" class="form-control" name="protocolVerificationIRBApprovalDate" id="protocolVerificationIRBApprovalDate" autocomplete="off" readonly="readonly"/>
					<script>
						$(function(){
							$("#protocolVerificationIRBApprovalDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								 minDate: 0,
								changeYear:true
							});
						});
					</script>
				</td>
			</tr>
			<tr>
				<td>Start Date:*</td>
				<td><input type="text" class="form-control" name="startDate" id="startDate" autocomplete="off"  readonly="readonly"/>
					<script>
						$(function(){
							$("#startDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								 minDate: 0,
								changeYear:true
							});
						});
					</script><font color="red" id="startDatemsg"></font>
				</td>
				<td>Estimated Completion Date:</td>
				<td><input type="text" class="form-control" name="endDate" id="endDate" autocomplete="off"  readonly="readonly"/>
					<script>
						$(function(){
							$("#endDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								 minDate: 0,
								changeYear:true
							});
						});
					</script>
				</td>
			</tr>
			<tr>
				<td>Expected total enrollment:*</td>
				<td><input type="number" class="form-control"  name="subjects" id="subjects" onkeyup="this.value=this.value.replace(/[^\d]/,'')" /><font color="red" id="subjectsmsg"></font></td>
				<td>Facility Name:</td>
				<td><input type="text" class="form-control" name="facilityName" id="facilityName"/></td>
			</tr>
			<tr>
				<td>Facility City:</td>
				<td><input type="text" class="form-control" name="facilityCity" id="facilityCity"/></td>
				<td>Facility State/Province:</td>
				<td><input type="text" class="form-control" name="facilityState" id="facilityState"/></td>
			</tr>
			<tr>
				<td>Facility ZIP: no</td>
				<td><input type="number" class="form-control" name="facilityZip" id="facilityZip" onkeyup="this.value=this.value.replace(/[^\d]/,'')" /><font color="red" id="facilityZipmsg"></font></td>
				<td>Facility Country:</td>
				<td><input type="text" class="form-control" name="facilityCountry" id="facilityCountry"/></td>
			</tr>
			<tr>
				<td>Facility Contact Name:</td>
				<td><input type="text" class="form-control" name="facilityContactName" id="facilityContactName"/></td>
				<td>Facility Contact Degree:</td>
				<td><input type="text" class="form-control" name="facilityContactDegree" id="facilityContactDegree"/></td>
			</tr>
			<tr>
				<td>Facility Contact Phone:</td>
				<td><input type="number" class="form-control" name="facilityContactPhone" id="facilityContactPhone"
				    onkeyup="this.value=this.value.replace(/[^\d]/,'')"
					/><font color="red" id="facilityContactPhonemsg"></font></td>
				<td>Facility Contact Email:</td>
				<td><input type="text" class="form-control" name="facilityContactEmail" id="facilityContactEmail"/></td>
			</tr>
			<tr>
				
				<td colspan="2"><input type="button" value="Save" class="btn btn-primary"
					 onclick="submitForm()"/>
				</td>
				<td colspan="2">
					<a href='<c:url value="/site/siteList"/>' class="btn btn-primary">Exit</a>&nbsp;&nbsp;
				</td>
			</tr>
			<tr>
				
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function checkSiteName(siteName){
	var result = asynchronousAjaxCall(mainUrl+"/site/checkSiteName/"+siteName);
	if(result != '' && result != 'undefined'){
		$('#siteNamemsg').html(result);
		$('#siteName').val("");
	}else{
		$('#siteNamemsg').html('');
	}
}
function submitForm(){
	$("#siteNamemsg").html("");
	$("#protocalIdmsg").html("");
	$("#principalInvestigatormsg").html("");
	$("#subjectsmsg").html("");
	$("#startDatemsg").html("");
	var flag = true;
	
	
	if($("#siteName").val().trim() == ''){
		flag = false;
		$("#siteNamemsg").html("Required Field");	
	}
	if($("#protocalId").val().trim() == ''){
		flag = false;
		$("#protocalIdmsg").html("Required Field");	
	}
	if($("#principalInvestigator").val().trim() == ''){
		flag = false;
		$("#principalInvestigatormsg").html("Required Field");	
	}
	if($("#startDate").val().trim() == ''){
		flag = false;
		$("#startDatemsg").html("Required Field");	
	}
	if($("#subjects").val().trim() == ''){
		flag = false;
		$("#subjectsmsg").html("Required Field");	
	}else if($("#subjects").val() < 1){
		flag = false;
		$("#subjectsmsg").html("Invalied");
	}
	if(flag)
		$("#formsumit").submit();	
}
</script>

</body>
</html>