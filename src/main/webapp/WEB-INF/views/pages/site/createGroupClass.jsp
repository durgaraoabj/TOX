<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/site/createGroupClassSave" var="createGroupClassSave" />
<sf:form action="${createGroupClassSave}" method="POST"  id="formsumit" modelAttribute="group">
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Group Class Creation Page</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Name:</td>
				<td><input type="text" class="form-control" name="groupName" id="groupName" onblur="checkGroupName(this.value)"/><font color="red" id="groupNamemsg"></font> </td>
			</tr>
			<tr>
				<td>Type:</td>
				<td>
					<select name="type" id="type" class="form-control">
						<option value="Arm">Arm</option>
						<option value="Family/Pedigree">Family/Pedigree</option>
						<option value="Demografic">Demografic</option>
						<option value="Other">Other</option>
					</select>
				</td>
			</tr>
			<tr>				
				<td>Subject Assignment:</td>
				<td>
					<input type="radio" name="subjectAssignment" id="subjectAssignment" value="Required" checked="checked">Required
					<input type="radio" name="subjectAssignment" id="subjectAssignment" value="Optional">Optional
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/>
				</td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function checkGroupName(groupclass){
	var result = asynchronousAjaxCall(mainUrl+"/site/checkGroupClassName/"+groupclass);
	if(result != '' && result != 'undefined'){
		$('#groupNamemsg').html(result);
		$('#groupName').val("");
	}else{
		$('#groupNamemsg').html('');
	}
}
function submitForm(){
	$("#groupNamemsg").html("");
	if($("#groupName").val().trim() != ""){
		$("#formsumit").submit();
	}else{
		$("#groupNamemsg").html("Required Field");	
	}
}
</script>




</body>
</html>