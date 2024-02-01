<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h3>Create User</h3>
	<div>
<div class="card"><div class="card-header"><h3 class="card-title">Create User</h3></div><div class="card-body">
		<c:url value="/administration/employee/createEmployee"
			var="createEmployeeUrl" />
		<form:form action="${createEmployeeUrl}" method="post"
			modelAttribute="employeeMaster" id="employeeFormId">

			<input type="hidden" name="roleVal" id="roleVal">
			<table class="table table-bordered table-striped" style="width:30%">
				<tr>
					<th colspan="2"
						style="color: blue; font-size: 19px; font-weight: bold; text-align: center;">Job
						Information</th>
				</tr>
				<tr>
					<td>User ID</td>
					<td><form:input path="empId" type="text"
							class="form-control input-sm" id="empId"
							onblur="checkempid(this.value)" /> <span id="empIdMsg"
						style="color: red"></span></td>
						</tr><tr>
					<td>Role</td>
					
					<td>
						<select id="roleId" name="roleId" class="form-control input-sm" onchange="roleIdValidation('roleId', 'roleIdMsg')">
							<option value="">----Select----</option>
							<c:forEach items="${roles}" var="ro">
								<option value="${ro.id}">${ro.roleDesc}</option>
							</c:forEach>
							
						</select> 
						<span id="roleIdMsg" style="color: red"></span></td>
				</tr>
				<tr>
					<td colspan="2"
						style="color: blue; font-size: 19px; font-weight: bold; text-align: center;">Personal Information</td>
				</tr>
				<tr>
					<td>First Name</td>
					<td><form:input path="firstName" type="text"
							class="form-control input-sm" id="firstNameid"
							onblur="funtionFirstName('firstNameid','firstNameidMsg')" /> <span
						id="firstNameidMsg" style="color: red"></span></td>
						</tr><tr>
					<td>Middle Name</td>
					<td><form:input path="middleName" type="text" id="middleNameid"
							class="form-control input-sm"  onblur="funtionMiddleName('middleNameid','middleNameidMsg')" />
							<span
						id="middleNameidMsg" style="color: red"></span></td>
						</tr><tr>
					<td>Last Name</td>
					<td><form:input path="lastName" type="text"
							class="form-control input-sm" id="lastNameid"
							onblur="funtionLastName('lastNameid','lastNameidMsg')" /> <span
						id="lastNameidMsg" style="color: red"></span></td>
				</tr>
				<tr>
					<td>Birth Date</td>
					<td><form:input path="birthDate" type="text"
							class="form-control input-sm" id="dateOfBirth" autocomplete="off" />
						<script>
						$(function(){
							$("#dateOfBirth").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								minDate : -21915,
								 maxDate: -6574,
								changeYear:true
							});
						});
					</script></td>
					</tr><tr>
					<td>Gender</td>
					<td colspan="3"><label class="radio-inline"><form:radiobutton
								path="gender" id="genderm" value="Male" />&nbsp;&nbsp;&nbsp;Male</label> <label
						class="radio-inline"><form:radiobutton path="gender"
								value="Female" id="genderf" />&nbsp;&nbsp;&nbsp;Female</label>
								<br><span id="genderMsg" style="color: red"></span></td>
					<%-- <td>Marital Status</td>
					<td>
						<form:select path="maritalStatus" class="form-control input-sm">
							<form:option value="" label="---Select---"/>
							<form:option value="Married" label="Married"/>
							<form:option value="Un-Married" label="Un-Married"/>
						</form:select>
					</td> --%>
				</tr>
				<%-- <tr>
					<td colspan="6">Present Address</td>
				</tr>
				<tr>
					<td>Postal/Zip Code</td>
					<td><form:input path="addressMaster.presentZipCode" type="text" class="form-control input-sm" id="presentZipCode" onblur="getAddress(this.id, 'presentCityId', 'presentStateId', 'presentCountryId')" maxlength="6" onkeypress="return numberValidator(event)" /></td>
					<td>Street Line 1</td>
					<td><form:input path="addressMaster.presentStreet1" type="text" class="form-control input-sm" /></td>
					<td>Street Line 2</td>
					<td><form:input path="addressMaster.presentStreet2" class="form-control input-sm"/></td>
				</tr>
				<tr>
					<td>City</td>
					<td>
						<form:input path="addressMaster.presentCity" class="form-control input-sm" id="presentCityId" />
					</td>
					<td>State/Province</td>
					<td>
						<form:input path="addressMaster.presentState" class="form-control input-sm" id="presentStateId" />
					</td>
					<td>Country</td>
					<td>
						<form:input path="addressMaster.presentCountry" class="form-control input-sm" id="presentCountryId" />
					</td>
				</tr>
				<tr>
					<td colspan="6">Permanent Address</td>
				</tr>
				<tr>
					<td>Postal/Zip Code</td>
					<td><form:input path="addressMaster.permanentZipCode" type="text" class="form-control input-sm" id="perZipCodeId" onblur="getAddress(this.id, 'perCityId', 'perStateId', 'perCountryId')" maxlength="6" onkeypress="return numberValidator(event)" /></td>
					<td>Street Line 1</td>
					<td><form:input path="addressMaster.permanentStreet1" type="text" class="form-control input-sm" /></td>
					<td>Street Line 2</td>
					<td><form:input path="addressMaster.permanentStreet2" type="text" class="form-control input-sm" /></td>
				</tr>
				<tr>
					<td>City</td>
					<td>
						<form:input path="addressMaster.permanentCity" class="form-control input-sm" id="perCityId" />
					</td>
					<td>State/Province</td>
					<td>
						<form:input path="addressMaster.permanentState" class="form-control input-sm" id="perStateId" />
					</td>
					<td>Country</td>
					<td>
						<form:input path="addressMaster.permanentCountry" class="form-control input-sm" id="perCountryId" />
					</td>
				</tr>
				<tr>
					<td>Mobile No</td>
					<td><form:input path="mobileNo" type="text" class="form-control input-sm" /></td>
					<td>Alt. Mobile No</td>
					<td><form:input path="altMobileNo" type="text" class="form-control input-sm" /></td>
					<td>Phone No</td>
					<td><form:input path="phoneNo" type="text" class="form-control input-sm" /></td>
				</tr>
				<tr>
					<td>Fax No</td>
					<td><form:input path="faxNo" type="text" class="form-control input-sm" /></td>
					<td>Email ID </td>
					<td><form:input path="emailId" type="text" class="form-control input-sm" /></td>
				</tr>
			</table>
               <table class="table table-bordered table-striped">
				<tr>
					<td colspan="6" style="color:blue; font-size: 19px; font-weight: bold; text-align: center;">Emergency Contact Information</td>
				</tr>
				<tr>
					<td>First Name</td>
					<td><form:input path="emerContMaster.firstName" type="text" class="form-control input-sm" /></td>
					<td>Last Name</td>
					<td><form:input path="emerContMaster.lastName" type="text" class="form-control input-sm" /></td>
					<td>Relationship </td>
					<td>
						<form:input path="emerContMaster.relationShip" class="form-control input-sm" />
					</td>
				</tr>
				<tr>
					<td>Postal/Zip Code</td>
					<td><form:input path="emerContMaster.zipCode" type="text" class="form-control input-sm" id="zipCodeId" onblur="getAddress(this.id, 'cityId', 'stateId', 'countryId')" maxlength="6" onkeypress="return numberValidator(event)" /></td>
					<td>Street Line 1</td>
					<td><form:input path="emerContMaster.street1" type="text" class="form-control input-sm" /></td>
					<td>Street Line 2</td>
					<td><form:input path="emerContMaster.street2" type="text" class="form-control input-sm" /></td>
				</tr>
				<tr>
					<td>City</td>
					<td>
						<form:input path="emerContMaster.city" class="form-control input-sm" id="cityId" />
					</td>
					<td>State/Province</td>
					<td>
						<form:input path="emerContMaster.state" class="form-control input-sm" id="stateId" />
					</td>
					<td>Country</td>
					<td>
						<form:input path="emerContMaster.country" class="form-control input-sm" id="countryId" />
					</td>
				</tr>
				<tr>
					<td>Mobile No</td>
					<td><form:input path="emerContMaster.mobileNo" type="text" class="form-control input-sm" /></td>
					<td>Alt. Mobile No</td>
					<td><form:input path="emerContMaster.altMobileNo" type="text" class="form-control input-sm" /></td>
					<td>Phone No</td>
					<td><form:input path="emerContMaster.phoneNo" type="text" class="form-control input-sm" /></td>
				</tr> --%>
			</table>
			<c:if
				test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
				<div style="text-align: center">
					<input type="button" value="Create Employee" id="formSubmitBtn"
						class="btn btn-warning btn-sm" onclick="submitForm()">
				</div>
			</c:if>
		</form:form>
		</div></div>
	</div>
	<div id="jsonData"></div>
</body>
<script type="text/javascript">
var roleFlag = false;
function roleIdValidation(id, messageId){
	$('#roleVal').val("");
	var value = $('#'+id).val();
	if(value == null || value == "" || value == "undefined"){
		$('#'+messageId).html("Required Field.");
		roleFlag = false;
	}else{
		$('#'+messageId).html("");
		$('#roleVal').val(value);
		roleFlag = true;
	}
	return roleFlag;
}
	function submitForm(){
		$("#empIdMsg").html("");
		if(roleFlag == false)
			roleFlag = roleIdValidation('roleId', 'roleIdMsg')
		var flag = true;
		if($("#empId").val() == ''){
			flag = false;
			$("#empIdMsg").html("Required Field");
		}
		if($("#roleId").val() == '0'){
			flag = false;
			$("#roleIdMsg").html("Required Field");
		}
		var firstn=funtionFirstName("firstNameid",'firstNameidMsg');
		var lastn=funtionLastName("lastNameid","lastNameidMsg");
		var gender=funtionGender();
		if(flag && firstn &&lastn && gender && roleFlag)
			$('#employeeFormId').submit();
	}

</script>
<script type="text/javascript">
$(function(){
	$('a[title]').tooltip();
});
</script>
<script type="text/javascript">
	function checkempid(empId){
		var result = asynchronousAjaxCall(mainUrl+"/administration/employee/empId/"+empId);
		if(result != '' && result != 'undefined'){
			$('#empIdMsg').html(result);
		}else{
			$('#empIdMsg').html('');
		}
	}
</script>
<script type="text/javascript">
	function getAddress(id, city, state, country){
		var zip = $('#'+id).val();
//		alert(zip);
		var result = asynchronousAjaxCall(mainUrl+'/pincode/'+zip);
//		alert(result);
		if(result != '' && result != 'undefined'){
	 		$('#'+city).val(result.city);
	 		$('#'+state).val(result.state);
			$('#'+country).val(result.country);
		}else{
			$('#'+city).val('');
	 		$('#'+state).val('');
			$('#'+country).val('');
		}
	}
	
	function numberValidator(evt){
		evt = (evt) ? evt : window.event;
	    var charCode = (evt.which) ? evt.which : evt.keyCode;
	    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
	        return false;
	    }
	    return true;	
	}
	
	function funtionFirstName(id,messageid){
		var flag=false;
		var firstName = $('#'+id).val();
		
		var letters = /^[A-Za-z]+$/;
		if(firstName.match(letters)) {
		//alert("firstName:"+firstName);
		if(firstName!=null && firstName!=""){
			flag=true;
			$('#'+messageid).html("");
		}else{
			$('#'+messageid).html("Required Field");
			flag=false;
		}
		//alert("flag1:"+flag);
	  }else{
		  $('#'+messageid).html("Please input alphabet characters only");
		  flag=false;
	  }
		return flag;
	}
	function funtionLastName(id,messageid){
		var flag=false;
		var lastName = $('#'+id).val();
		var letters = /^[A-Za-z]+$/;
		if(lastName.match(letters)) {
		if(lastName!=null && lastName!=""){
			flag=true;
			$('#'+messageid).html("");
		}else{
			$('#'+messageid).html("Required Field");
			flag=false;
		}
		}else{
			  $('#'+messageid).html("Please input alphabet characters only");
			  flag=false;
		}
		return flag;
	}
	function funtionMiddleName(id,messageid){
		var flag=false;
		var middlename = $('#'+id).val();
		if(middlename!="" && middlename!=null){
			var letters = /^[A-Za-z]+$/;
			if(middlename.match(letters)) {
				$('#'+messageid).html("");
				 flag=true;
			}else{
				  $('#'+messageid).html("Please input alphabet characters only");
				  flag=false;
			}
		}else{
			$('#'+messageid).html("");
			flag=true;
		}
		return flag;
	}
	function funtionGender(){
		var flag=false;
		if($('#genderm').is(':checked') || $('#genderf').is(':checked') ) {
			$('#genderMsg').html("");
			flag=true;
		}else{
			$('#genderMsg').html("Required Field");
			flag=false;
		}
		return flag;
	}
	
</script>

</html>