<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/site/saveSubject" var="saveSubject" />
<sf:form action="${saveSubject}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Add New Subject</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Study Subject ID:</td>
				<td>
					<input type="text" class="form-control" name="subjectId" id="subjectId" disabled="disabled" placeholder="Auto Generated"/>
				</td>
				<td>Person ID:*</td>
				<td><input type="text" class="form-control" name="personId" id="personId"/><font color="red" id="personIdmsg"></font></td>
			</tr>
			<tr>
				<td>Enrollment Date:*</td>
				<td><input type="text" class="form-control" name="enrollmentDate" id="enrollmentDate" autocomplete="off"/><font color="red" id="enrollmentDatemsg"></font>
					<script>
						$(function(){
							$("#enrollmentDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								 minDate: 0,
								changeYear:true
							});
						});
					</script>
				</td>
				<td>Date of Birth:*</td>
				<td><input type="text" class="form-control" name="dateOfBirth" id="dateOfBirth" autocomplete="off"/><font color="red" id="dateOfBirthmsg"></font>
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
			</tr>
			<tr>
<!-- 				<td>Name:</td> -->
<!-- 				<td><input type="text" class="form-control" name="name" id="name"/></td> -->
				<td>Gender:</td>
				<td>
					<select  class="form-control" name="gender" id="gender">
						<option value="Male">Male</option>
						<option value="Female">Female</option>
					</select>
				</td>
				<td/><td/>
			</tr>
			<tr>
				<td colspan="4">
					<table>
						<tr>
							<th>Subject Group Class</th>
							<th>Subject Group Name</th>
						</tr>
						<c:forEach items="${groupMap}" var="mp">
							<tr>
								<td>${mp.key }</td>
								<td>
									<select name="group" id="group"  class="form-control" >
										<option value="-1">--Select Group--</option>
									<c:forEach items="${mp.value}" var="group">
										<option value="${group.id }">${group.groupName}</option>
									</c:forEach>
									</select>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td colspan="2"><font color="red" id="groupmsg"></font></td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				
				<td>Start Date:*</td>
				<td><input type="text" class="form-control" name="startDate" id="startDate" autocomplete="off"/><font color="red" id="startDatemsg"></font>
					<script>
						$(function(){
							$("#startDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
// 								 minDate: 0,
								changeYear:true
							});
						});
					</script>
				</td>
				<td>
					<input type="button" value="Add" class="btn btn-primary"
					 onclick="submitForm()"/>
				</td>
				<td><a href='<c:url value="/site/subjectMatrix"/>' class="btn btn-primary">Exit</a></td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function submitForm(){
	$("#personIdmsg").html("");
	$("#enrollmentDatemsg").html("");
	$("#dateOfBirthmsg").html("");
	$("#groupmsg").html("");
	$("#startDatemsg").html("");
	var flag = true;
	if($("#personId").val().trim() == ''){
		flag = false;
		$("#personIdmsg").html("Required Field");
	}
	if($("#enrollmentDate").val().trim() == ''){
		flag = false;
		$("#enrollmentDatemsg").html("Required Field");
	}
	if($("#dateOfBirth").val().trim() == ''){
		flag = false;
		$("#dateOfBirthmsg").html("Required Field");
	}
	if($("#startDate").val().trim() == ''){
		flag = false;
		$("#startDatemsg").html("Required Field");
	}
	
	var selectflag = 0;
	var articlesSelected = $.map($('select[name="group"]'), function (val, _) {
	    var newObj = {};
	    if(val.value > 0){
	    	selectflag++;
	    }
	    newObj.article = val.value;
	    return newObj;
	});
	if(selectflag == 0){
		flag = false;
		$("#groupmsg").html("Required Field");
	}if(selectflag > 1){
		flag = false;
		$("#groupmsg").html("Please, Select Only One Group");
	}
	
	if(flag)
	$("#formsumit").submit();	
}
</script>




</body>
</html>