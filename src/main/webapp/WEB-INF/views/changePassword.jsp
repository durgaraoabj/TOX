<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/administration/employee/loginCredentials/updatePassword" var="formsumit1" />
<sf:form action="${formsumit1}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Change Password</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>New Password:</td>
				<td>
					<input type="password" class="form-control" name="pws1" id="pws1" /><font color="red" id="pws1msg"></font>
				</td>
			</tr>
			<tr>
				<td>Repeat Password:</td>
				<td><input type="password" class="form-control" name="pws2" id="pws2"/><font color="red" id="pws2msg"></font></td>
			</tr>
			<tr>
				<td/>
				<td>
					<input type="button" value="Update" class="btn btn-primary" onclick="submitForm()"/>
				</td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function submitForm(){
	$("#pws1msg").html("");
	$("#pws2msg").html("");
	var flag = true;
	if($("#pws1").val().trim() == ''){
		flag = false;
		$("#pws1msg").html("Required Field");
	}
	if($("#pws2").val().trim() == ''){
		flag = false;
		$("#pws2msg").html("Required Field");
	}
	if(flag){
		if($("#pws1").val().trim() != $("#pws2").val().trim()){
			$("#pws2msg").html("Password Not Matched");
			$("#pws1").val("");
			$("#pws2").val("");
			flag = false;
		}
		if(flag)
			$("#formsumit").submit();	
	}
	
}
</script>




</body>
</html>