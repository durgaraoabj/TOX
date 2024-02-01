<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
    <div class="box-body">
    <c:url value="/administration/createStudy" var="studyCreateUrl"/>
    <form:form action="${studyCreateUrl}" method="post" modelAttribute="studyMaster" id="studyCreateFormId" >
    	<table class="table table-bordered table-striped">
    		<tr>
    			<th colspan="4">New Study</th>
    		</tr>
    		<tr>
    			<td>Study Number</td>
    			<td>
    				<form:input path="studyNo" type="text" class="form-control input-sm" onblur="checkStudyNumber(this.value, this.id)" id="studyNo"/>
    				<font color="red" id="studyNomsg"></font>
    			</td>
    			<td>Brief Title</td>
    			<td><form:textarea path="studyDesc" rows="2" class="form-control input-sm" id="studyDesc"/><font color="red" id="studyDescmsg"></font></td>
    		</tr>
    		<tr>
    			<td>No. Of Groups</td>
    			<td>
    				<form:input path="noOfGroups" type="text" class="form-control input-sm" id="noOfGroups" placeholder="count" 
					onkeypress="return event.charCode >= 48 && event.charCode <= 57" onchange="groupInfo(this.value)"/>
    				<font color="red" id="noOfGroupsmsg"></font>
    			</td>
    			<td>Subjects</td>
    			<td>
    				<form:input path="subjects" type="text" class="form-control input-sm" id="subjects"
		    			oncopy="return false" onpaste="return false"
		    			onkeypress="return event.charCode >= 48 && event.charCode <= 57"/><font color="red" id="subjectsmsg"></font>
		    	</td>
    		</tr>
    		<tr><td colspan="4" id="groupInfo0"></td></tr>
			<tr><td colspan="4" id="groupInfo"></td></tr>
		
    		<tr>
    			<td>Principal Investigator</td>
    			<td>
    				<form:input path="principalInvestigator" type="text" class="form-control input-sm" id="principalInvestigator"/>
    				<font color="red" id="principalInvestigatormsg"></font>
    			</td>
    			
    			
    			<td>Start Date</td>
    			<td><input name="startDate" id="startDate" type="text" class="form-control input-sm" autocomplete="off"/>
	    				<script type="text/javascript">
						$(function(){
								$("#startDate").datepicker({
									dateFormat: $("#dateFormatJsp").val(),
									changeMonth:true,
									changeYear:true
								});
							});
						</script>
						<font color="red" id="startDatemsg"></font></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td></td>
    		</tr>
    		
    	</table>
    	
    	<div align="center"><input type="button" id="studyCreateFormSubmitBtn" onclick="studyCreateFormSubmitBtnbut()" value="Create Study" class="btn btn-primary" style="width:200px;"></div>
    </form:form>
    </div>
    <c:if test="${infoMessage == true }">
    	<div class="modal fade" id="studyInformationModal" tabindex="-1" role="dialog" aria-labelledby="studyInformationModal" aria-hidden="true" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog modal-lg">
		    	<div class="modal-content">
		      		<div class="modal-header">
		        		<h4 class="modal-title" id="myModalLabel">Study Creation Information</h4>
		      		</div>
		      		<div class="modal-body">
		        		<h3>Modal Body</h3>
		      		</div>
		      		<div class="modal-footer">
		      			<div style="text-align: left;">
		      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptCheckId" />&nbsp;&nbsp;&nbsp;Please read and confirm study creation information.</label>
		      			</div>
		      			<div style="text-align: right;">
		        		<input type="button" class="btn btn-primary" onclick="acceptCheckBox()" value="Accepted"/>
		        		</div>
		        	</div>
		    	</div>
		  	</div>
		</div>
    	
    </c:if>
</body>
<script type="text/javascript">
$('#studyInformationModal').modal('hide');
	function acceptCheckBox() {
		if ($('#acceptCheckId').is(':checked')) {
			$('#studyInformationModal').modal('hide');
	  	}else{
			alertify.alert("Please check checkbox...");
	  	}
	}
</script>

<script type="text/javascript">
$("#noOfGroups").val("");
$("#subjects").val("");


	function groupInfo(value){
		$("#groupInfo0").html("");
		$("#groupInfo").html("");
		$("#noOfGroupsmsg").html("");
		var batch = value.trim();
		if(batch != '' && batch > 0){
			var ele = "<table class='table table-bordered table-striped'><tr><th>Group Name</th><th>No. Of Sub Group's</th></tr>";
			for(var i = 1; i<= batch; i++){
				ele += "<tr><td><input type='text' name='groupName"+i+"'  id='groupName"+i+"' class='form-control'/><font color='red' id='groupName"+i+"msg'></font></td>"
				+"<td><input type='number' name='groupTest"+i+"' id='groupTest"+i+"' class='form-control' placeholder='Number' onkeypress='return event.charCode >= 48 && event.charCode <= 57'/><font color='red' id='groupTest"+i+"msg'></font></td>"
				+"<td>"
				+"<input type='radio' name='groupGender"+i+"' value='Male'/>Male<br/>"
				+"<input type='radio' name='groupGender"+i+"' value='Female'/>Female<br/>"
				+"<input type='radio' name='groupGender"+i+"' value='Both' checked/>Both<br/>"
				+"<font color='red' id='groupGender"+i+"msg'></font></td></tr>";
			}		
			ele += "<table>";
			$("#groupInfo0").html("Group wise Test's");
			$("#groupInfo").html(ele);	
		}else{
			if(batch <= 0)	$("#noOfGroupsmsg").html("Value must more than 0");
			else $("#noOfGroupsmsg").html("Required Field.");
		}
	}
	function checkStudyNumber(value, id){
		$('#'+id+'msg').html('');
// 		alert(mainUrl)
		var result = asynchronousAjaxCall(mainUrl+"/administration/checkStudyNumber/"+value);
		result = result.trim();
		if(result != '' || result != 'undefined'){
			if(result == 'yes'){
				$('#'+id+'msg').html(value + " aleredy avilable.");
				$('#'+id).val("");			
			}
		}
	}
	function studyCreateFormSubmitBtnbut(){
		$("#studyNomsg").html("");
		$("#studyDescmsg").html("");
		$("#principalInvestigatormsg").html("");
		$("#subjectsmsg").html("");
		$("#noOfGroupsmsg").html("");
		var flag = true;
		if($("#studyNo").val().trim() == ''){
			$("#studyNomsg").html("Required Field");flag = false;
		}
		if($("#studyDesc").val().trim() == ''){
			$("#studyDescmsg").html("Required Field");flag = false;
		}
		if($("#principalInvestigator").val().trim() == ''){
			$("#principalInvestigatormsg").html("Required Field");flag = false;
		}
		if($("#noOfGroups").val().trim() == ''){
			$("#noOfGroupsmsg").html("Required Field");flag = false;
		}else{
			
			var value = Number($("#noOfGroups").val().trim());
			if(value > 0){
				var groupNames = [];
				for(var v =1; v<=value; v++){
// 					alert(v)
					$("#groupName"+v+"msg").html("");
					$("#groupTest"+v+"msg").html("");
// 					alert($("#groupName"+v).val());
					var groupName = $("#groupName"+v).val().trim();
					if(groupName == ''){
						$("#groupName"+v+"msg").html("Required Field");flag = false;
					}else{
						var fg = true;
						$.each(groupNames, function(k, v) {
							if(groupName == v){
								fg = false;
							}				
						});
						if(fg)
							groupNames.push(groupName);
					}
					var groupTest = $("#groupTest"+v).val().trim();
// 					alert($("#groupName"+v).val());
					if(groupTest == ''){
						$("#groupTest"+v+"msg").html("Required Field");flag = false;
					}else{
						var fg = true;
						if(Number(groupTest) <0){
							$("#groupTest"+v+"msg").html("Value must more than 0");flag = false;
						}
					}
				}
			}else{
				$("#noOfGroupsmsg").html("Value must more than 0");flag = false;
			}
		}
		if($("#subjects").val().trim() == ''){
			$("#subjectsmsg").html("Required Field");flag = false;
		}
		if(flag){
// 			alert("asdf")
			$('#studyCreateFormId').submit();			
		}
	}
</script>
<script type="text/javascript">
function sample(){
$('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' /* optional */
  });
}
</script>


</html>