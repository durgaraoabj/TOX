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
	<h3 class="box-title">Login Credentials</h3>
    <div class="box-body" align="center">
		<table class="table table-bordered table-striped" style="width:65%;">
			<tr>
				<td>Enter User ID </td>
				<td><input type="text" name="empId" id="empId" class="form-control input-sm"></td>
				<td style="width:140px;"><input type="button" value="Search" class="btn btn-primary" 
				id="empSearchBtnId" style="width:140px;" onclick="empSearch()"></td>
			</tr>
		</table>
		<br/>
		<div id="empDetailsDivId"></div>
	</div>	
</body>
<script type="text/javascript">
	function empSearch(){
		var empId = $('#empId').val();
		var result = asynchronousAjaxCall(mainUrl+"/administration/employee/loginCredentials/"+empId);
		$('#empDetailsDivId').html(result);
	} 
// 	$('#empSearchBtnId').click(function(){
// 		var empId = $('#empId').val();
// 		var result = asynchronousAjaxCall(mainUrl+"/administration/employee/loginCredentials/"+empId);
// 		$('#empDetailsDivId').html(result);
// 	});
</script>
</html>