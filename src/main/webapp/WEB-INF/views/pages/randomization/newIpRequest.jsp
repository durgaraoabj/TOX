<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>

</head>
<body>
	<div class="card">
		<div class="card-header">
	       <h3 class="card-title">
	       		New IP Request
	       </h3>
	     </div>
	     <div class="card-body">
	     	<c:url value="/randomization/createIpRequest" var="create_ipRequesturl"/>
			<form:form action="${create_ipRequesturl}" method="post" id="create_ipRequest" >
				<table class="table table-bordered table-striped">
					<tr>
						<th>Subject Id</th>
						<td>
							<select id="subject" name="subject">
								<option value="-1">--Select--</option>
								<c:forEach items="${siteSubs}" var="s">
									<option value="${s.id}">${s.subjectno}</option>
								</c:forEach>
							</select>
							<font color="red" id="subjectmsg"></font>
						</td>
						<th>Visit Name</th>
						<td>
							<select id="period" name="period">
								<option value="-1">--Select--</option>
								<c:forEach items="${periods}" var="p">
									<option value="${p.id}">${p.name}</option>
								</c:forEach>
							</select>
							<font color="red" id="periodmsg"></font>
						</td>
					</tr>
				</table>
				<table>
					<tr>
						<td><input type="button" onclick="save()"
							value="Save" class="btn btn-primary" style="width:200px;"></td>
						<td><input type="button" onclick="clear()"
							value="Clear" class="btn btn-primary" style="width:200px;"></td>	
					</tr>
				</table>
			</form:form>
	     </div>
	</div>

	<div class="modal fade" id="signacherModal" tabindex="-1" role="dialog" aria-labelledby="discDataModal" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
	    	<div class="modal-content">
	      		<div class="modal-header">
	        		<h4 class="modal-title" id="myModalLabel">Electronic Signature</h4>
	        		<input type="hidden" id="signcheck"/>
	      		</div>
	      		<div class="modal-body">
	      			<table>
	      				<tr>
	      					<th>User Name : </th>
	      					<td>${userName} </td>
	      				</tr>
	      				<tr>
	      					<th>Transaction Password</th>
	      					<td><input type="password" id="tpw"/><font color="red" id="tpwmsg"></font></td>
	      				</tr>
	      				<tr>
	      				<tr><td/><td><input type="button" class="btn btn-primary" onclick="subjectPeriodCrfsDataReview()" value="Submit"/></td></tr>
	      			</table>
	        	</div>
	      		<div class="modal-footer">
	      			<div style="text-align: left;">
	      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptsignCheckId" />&nbsp;&nbsp;&nbsp;Do You want to close page.</label>
	      			</div>
	      			<div style="text-align: right;">
	        		<input type="button" class="btn btn-primary" onclick="acceptsignCheckBox()" value="Close"/>
	        		</div>
	        	</div>
	    	</div>
	  	</div>
	</div>
	
			
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$('#signacherModal').modal('hide');
	});
	function signacherPopup(){
		$('#signacherModal').modal('show');
	}
	function acceptsignCheckBox() {
		if ($('#acceptsignCheckId').is(':checked')) {
			$('#signacherModal').modal('hide');
	  	}else{
			aler("Please check checkbox...");
	  	}
	}
	function save() {
		$("#subjectmsg").html("");
		$("#periodmsg").html("");
		var flag = true;
		if($("#subject").val().trim() == -1){
			$("#subjectmsg").html("Required Filed");
			flag = false;
		}
		if($("#period").val().trim() == -1){
			$("#periodmsg").html("Required Filed");
			flag = false;
		}
		if(flag){
			$("#create_ipRequest").submit();
// 			$('#signacherModal').modal('show');
		}		
	}
	
	function subjectPeriodCrfsDataReview(){
		$("#tpwmsg").html("");
		var signcheck = asynchronousAjaxCall(mainUrl+"/reviewer/signcheck/"+$("#tpw").val());
		$("#signcheck").val(signcheck.trim());
		if($("#signcheck").val() == "yes"){
			$('#signacherModal').modal('hide');
			$("#create_ipRequest").submit();
		}else{
			$("#tpw").val("");
			$("#tpwmsg").html("In-valied password");
		}
	}
	function clear() {
		$("#subject").val("-1");
		$("#period").val("-1");
	}
</script>
</html>