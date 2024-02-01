<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<c:if test="${createType == true}">
		<c:url value="/administration/employee/loginCredentials" var="loginCredentialsUrl" />
		<form:form action="${loginCredentialsUrl}" method="post" modelAttribute="loginFieldDummyForm" id="loginCredentialsFormId">
			<table class="table table-bordered table-striped" style="width:65%;">
				<tr>
					<th colspan="2" style="text-align: center;">
						User Details
						<form:input path="empId" value="${employeeMaster.empId}"/>
						<form:input path="empMasterId" value="${employeeMaster.id}"/>
						<form:input path="firstName" value="${employeeMaster.firstName}" />
						<form:input path="middleName" value="${employeeMaster.middleName}" />
						<form:input path="lastName" value="${employeeMaster.lastName}" />
						<form:input path="birthDate" value="${employeeMaster.birthDate}" />
					</th>
				</tr>
				<tr>
					<td>User Name</td>
					<td><c:out value="${employeeMaster.firstName}"/>&nbsp;&nbsp;<c:out value="${employeeMaster.lastName}"/></td>
				</tr>
				<tr>
					<td>Department </td>
					<td><c:out value="${employeeMaster.jobMaster.dept.deptDecription}"/></td>
				</tr>
				<tr>
					<td>Job Title</td>
					<td><c:out value="${employeeMaster.jobMaster.jobTitle}"/></td>
				</tr>
				<tr>
					<td>Mobile No</td>
					<td><c:out value="${employeeMaster.mobileNo}"/></td>
				</tr>
				<tr>
					<td>Email ID</td>
					<td><c:out value="${employeeMaster.emailId}"/></td>
				</tr>
				<tr>
					<td>Role </td>
					<td><form:select path="role.id" cssClass="form-control input-sm" id="roleId">
						<form:option disabled="true" value="0" label="---Select---"/>
						<c:forEach items="${allRoles}" var="role">
							<form:option value="${role.id}" label="${role.roleDesc}" lang="${role.tranPassword}" />
						</c:forEach>
					</form:select></td>
				</tr>
				<tr>
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="loginCredentials" value="true" id="loginCredentialsId" />&nbsp;&nbsp;&nbsp;Please confirm login credentials</label>
				</tr>
				<tr id="tranPassTrId">
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="tranPasswordflag" value="true" id="tranPasswordflagId" />&nbsp;&nbsp;&nbsp;Please confirm transaction password credentials</label>
				</tr>
				<c:if test="${employeeMaster.loginCredantials == false}">
					<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
						<tr>
							<td colspan="2" style="text-align: center;">
								<input type="button" class="btn btn-primary" value="Generate Login Credentials" 
								id="generateLoginCredentialsBtn" onclick="generateLoginCredentialsBtn()"></td>
						</tr>
					</c:if>
				</c:if>
			</table>
		</form:form>
	</c:if>
	<c:if test="${createType== false}">
		<c:url value="/administration/employee/updateLoginCredentials" var="updateLoginCredentialsUrl" />
		<form:form action="${updateLoginCredentialsUrl}" method="POST" modelAttribute="loginFieldDummyForm" id="updateLoginCredentialsFormId">
			<table class="table table-bordered table-striped" style="width:65%;">
				<tr>
					<th colspan="2" style="text-align: center;">
						Login Credential Details
						<form:hidden path="id"/>
						<form:hidden path="empId"/>
					</th>
				</tr>
				<tr>
					<td>User Id</td>
					<td><c:out value="${loginFieldDummyForm.empId}"/></td>
				</tr>
				<tr>
					<td>User Name</td>
					<td><c:out value="${loginFieldDummyForm.fullName}"/></td>
				</tr>
				<tr>
					<td>Role </td>
					<td><form:select path="role.id" cssClass="form-control input-sm" id="updateRoleId">
						<form:option disabled="true" value="0" label="---Select---"/>
						<c:forEach items="${allRoles}" var="role">
							<form:option value="${role.id}" label="${role.roleDesc}" lang="${role.tranPassword}" />
						</c:forEach>
					</form:select></td>
				</tr>
				<tr>
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="loginCredentials" value="true" id="loginCredentialsId" />&nbsp;&nbsp;&nbsp;Please confirm login credentials</label>
				</tr>
				<tr id="updateTranPassTrId">
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="tranPasswordflag" value="true" id="tranPasswordflagId" />&nbsp;&nbsp;&nbsp;Please confirm transaction password credentials</label>
				</tr>
				<tr>
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="resetPassword" value="true" id="resetPasswordId" />&nbsp;&nbsp;&nbsp;Reset Password credentials</label>
				</tr>
				<tr id="updateResetTranPassTrId">
					<td>Please Confirm</td>
					<td><label class = "checkbox-inline"><form:checkbox path="resetTranPassword" value="true" id="resetTranPasswordId" />&nbsp;&nbsp;&nbsp;Reset Transaction password credentials</label>
				</tr>
				<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
					<tr>
						<td colspan="2" style="text-align: center;">
						<input type="button" class="btn btn-primary" value="Update Login Credentials" 
						id="updateLoginCredentialsBtn"></td>
					</tr>
				</c:if>
			</table>
		</form:form>
	</c:if>
</body>
<script type="text/javascript">

	$('#updateLoginCredentialsBtn').click(function(){
		$('#updateLoginCredentialsFormId').submit();
	});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		var roleType = $(updateRoleId).children("option:selected").attr('lang');
		if(roleType == 'true'){
			$('#updateTranPassTrId').show();
			$('#updateResetTranPassTrId').show();
		}else{
			$('#updateTranPassTrId').hide();
			$('#updateResetTranPassTrId').hide();
		}
	});

	$('#updateRoleId').change(function(){
		var roleType = $(this).children("option:selected").attr('lang');
		if(roleType == 'true'){
			$('#updateTranPassTrId').show();
			$('#updateResetTranPassTrId').show();
		}else{
			$('#updateTranPassTrId').hide();
			$('#updateResetTranPassTrId').hide();
		}
	});
</script>

<script type="text/javascript">
$('input').iCheck({
    checkboxClass: 'icheckbox_square-blue',
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' /* optional */
  });
</script>
<script type="text/javascript">
	function generateLoginCredentialsBtn(){
		$('#loginCredentialsFormId').submit();
	}
	$('#generateLoginCredentialsBtn').click(function(){
		if($('#loginCredentialsId').is(":checked")){
			$('#loginCredentialsFormId').submit();
		}else{
			alert('Please select login credentials checkbox');
		}
	});
</script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#tranPassTrId').hide();
	});

	$('#roleId').change(function(){
		var roleType = $(this).children("option:selected").attr('lang');
		if(roleType == 'true'){
			$('#tranPassTrId').show();
		}else{
			$('#tranPassTrId').hide();
		}		
	});
</script>
</html>