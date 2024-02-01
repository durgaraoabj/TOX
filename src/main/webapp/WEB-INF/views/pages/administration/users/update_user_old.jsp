<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h3>Update User</h3>
    <div>
    
    	<c:url value="/administration/employee/updateEmployee" var="createEmployeeUrl" />
        <form:form action="${createEmployeeUrl}" method="post" modelAttribute="employeeMaster" id="employeeFormId">
            
            
            <table class="table table-bordered table-striped">
				<tr>
					<th colspan="4" style="color:blue; font-size: 19px; font-weight: bold; text-align: center;">Job Information</th>
				</tr>
				<tr>
					<td>User ID</td>
					<td>
						<form:input path="empId" type="text" class="form-control input-sm" id="empId" readonly="true"/>
						<span id="empIdMsg" style="color: red"></span>
					</td>
					<td>Department</td>
					<td>
						<form:select path="jobMaster.dept.id" class="form-control input-sm">
							<form:option value="0" disabled="true" label="---Select---"/>
							<form:options items="${deptList}"  itemLabel="deptDecription" itemValue="id"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td>Job Title</td>
					<td>
						<form:select path="jobMaster.jobTitle" class="form-control input-sm">
							<form:option value="0" disabled="true" label="---Select---"/>
							<form:option value="Administration" label="Administration"/>
							<form:option value="Director" label="Director"/>
							<form:option value="Manager" label="Manager"/>
							<form:option value="Assistant Manager" label="Assistant Manager"/>
							<form:option value="Consultant" label="Consultant"/>
							<form:option value="Training" label="Training"/>
						</form:select>
					</td>
					<td>Supervisor</td>
					<td><form:input path="jobMaster.supervisor" type="text" class="form-control input-sm" /></td>
				</tr>
				<tr>
					<td>Work Location</td>
					<td><form:input path="jobMaster.workLocation" type="text" class="form-control input-sm" /></td>
					<td>Salary</td>
					<td><form:input path="jobMaster.salary" type="text" class="form-control input-sm" /></td>
				</tr>
				<tr>
					<td>Work Contact No</td>
					<td><form:input path="jobMaster.contactNo" type="text" class="form-control input-sm" /></td>
					<td>Email ID </td>
					<td><form:input path="jobMaster.emailId" type="text" class="form-control input-sm" /></td>
				</tr>
			</table>
           	<table class="table table-bordered table-striped">
				<tr>
					<td colspan="6" style="color:blue; font-size: 19px; font-weight: bold; text-align: center;">Personal Information</td>
				</tr>
				<tr>
					<td>First Name</td>
					<td><form:input path="firstName" type="text" class="form-control input-sm"  readonly="true"/></td>
					<td>Middle Name</td>
					<td><form:input path="middleName" type="text" class="form-control input-sm"  readonly="true"/></td>
					<td>Last Name</td>
					<td><form:input path="lastName" type="text" class="form-control input-sm"  readonly="true"/></td>
				</tr>
				<tr>
					<td>Birth Date</td>
					<td><form:input path="birthDate" type="text" class="form-control input-sm" id="dateOfBirth" autocomplete="off"/>
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
					</script>
					</td>
					<td>Gender</td>
					<td>
						<label class = "radio-inline"><form:radiobutton path="gender" value = "Male" />&nbsp;&nbsp;&nbsp;Male</label>
						<label class = "radio-inline"><form:radiobutton path="gender" value="Female" />&nbsp;&nbsp;&nbsp;Female</label>
					</td>
					<td>Marital Status</td>
					<td>
						<form:select path="maritalStatus" class="form-control input-sm">
							<form:option value="" label="---Select---"/>
							<form:option value="Married" label="Married"/>
							<form:option value="Un-Married" label="Un-Married"/>
						</form:select>
					</td>
				</tr>
				<tr>
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
				</tr>
			</table>
			<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
				<div style="text-align:center"><input type="button" value="Update Employee" id="formSubmitBtn" 
				class="btn btn-warning btn-sm" onclick="submitForm()"></div>
			</c:if>
    	</form:form>
	</div>
	<div id="jsonData"></div>
</body>
<script type="text/javascript">
	function submitForm(){
// 		$("#empIdMsg").html("");
// 		var flag = true;
// 		if($("#empId").val() == ''){
// 			flag = false;
// 			$("#empIdMsg").html("Required Field");
// 		}
// 		if(flag)
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
	
</script>

</html>