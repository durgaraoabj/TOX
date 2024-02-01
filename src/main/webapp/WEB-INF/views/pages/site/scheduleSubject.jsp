<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/site/saveScheduleSubject" var="saveScheduleSubject" />
<sf:form action="${saveScheduleSubject}" method="POST"  id="formsumit" >
<input type="hidden" name="volid" value="${volid}">
<input type="hidden" name="periodId" value="${periodId}">
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Schedule Study Phase for Subject : <B>${sub}</B></h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Study Subject ID:</td>
				<td>${sub}</td>
			</tr>
			<tr>
				<td>Phase:</td>
				<td>${phaseName}</td>
			</tr>
			<tr>
				<td>Start Date:</td>
				<td><input type="text" class="form-control" name="startDate" id="startDate" autocomplete="off"/>
					<script>
						$(function(){
							$("#startDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
// 								 minDate: 0,
								minDate: new Date(2020, 1 - 1, 1),
								changeYear:true
							});
						});
					</script>
				</td>
			</tr>
			<tr>
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
	$("#formsumit").submit();	
}
</script>




</body>
</html>