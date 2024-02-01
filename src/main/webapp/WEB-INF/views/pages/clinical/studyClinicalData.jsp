<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/study/clinical/clinicalInfoSave" var="clinicalInfoSave" />
<sf:form action="${clinicalInfoSave}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Clinical Information</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
					<td>Dose Time</td>
					<td>
						<div class="input-group clockpicker-with-callbacks">
							<input type="text" class="form-control" placeholder="time" readonly="readonly" name="doseTime" autocomplete="off">
						</div>
					</td>
					<td>Dose Date</td>
					<td>
						<input type="text" name="doseDate" id="doseDate" class="form-control" autocomplete="off">
						<script>
							$(function(){
								$("#doseDate").datepicker({
									dateFormat:$("#dateFormatJsp").val(),
									changeMonth:true,
									 minDate: 0,
									changeYear:true
								});
							});
						</script>
					</td>
					<td>Interval between Subjects</td>
					<td><input type="text" name="intervalBetweenSubject" class="form-control" placeholder="no of minutes"></td>
			</tr>
			<tr><td colspan="6">Sample Time Points Information</td></tr>
			<tr>
					<td>Total Time Points</td>
					<td><input type="text" name="noOfSamples" class="form-control" placeholder="count"></td>
					<td>Pre Dose/ -ve</td>
					<td><input type="text" name="negativeSamples" class="form-control" placeholder="count"></td>
					<td>Zero Hour No of Vacutainer tubes</td>
					<td><input type="text" name="zeroHourVacutaines" class="form-control" placeholder="count" value="1"></td>
			</tr>
			<tr>
					<td>Post Time Points/ +ve</td>
					<td><input type="text" name="positiveSamples" class="form-control" placeholder="count"></td>
					<td>Ambulatory</td>
					<td><input type="text" name="ambulatorys" class="form-control" placeholder="count"></td>
					<td>No.of Batch size</td>
					<td><input type="text" name="batches" class="form-control" placeholder="count" onchange="batchWiseSub(this.value)"></td>
			</tr>
			<tr><td colspan="6" id="batchWiseSub0"></td></tr>
			<tr><td colspan="6" id="batchWiseSub"></td></tr>
			<tr>
				<td colspan="2">No of Vials for Sample Separation</td>
				<td><input type="text" name="noOfVials" class="form-control" placeholder="count" value="1"></td>
				<td/><td/><td/>
			</tr>
			<tr><td colspan="6">Vital Information</td></tr>
			
			<tr>
					<td>Total Vitals</td>
					<td><input type="text" name="noOfVitals" class="form-control" placeholder="count"></td>
					<td>Pre Dose/ -ve Vital</td>
					<td><input type="text" name="negativeVitals" class="form-control" placeholder="count"></td>
					<td/><td/>
			</tr>
			<tr>
					<td colspan="6">
						<input type="button" value="Save" class="btn btn-primary"
						 onclick="submitForm()"/>
					</td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function batchWiseSub(batches){
	var batch = batches.trim();
	if(batch != '' && batch > 1){
		var ele = "<table class='table table-bordered table-striped'><tr><th>Batch No</th><th>Subjects</th></tr>";
		for(var i = 1; i<= batch; i++){
			ele += "<tr><td>Batch&nbsp;"+i+"</td><td colspan='5'><input type='text' name='batch"+i+"' class='form-control' placeholder='Subjects with , Separator'/></td></tr>";
		}		
		ele += "<table>";
		$("#batchWiseSub0").html("Batch wise subjects");
		$("#batchWiseSub").html(ele);
	}else{
		$("#batchWiseSub0").html("");
		$("#batchWiseSub").html("");
	}
}
function submitForm(){
	$("#formsumit").submit();	
}
</script>




</body>
</html>