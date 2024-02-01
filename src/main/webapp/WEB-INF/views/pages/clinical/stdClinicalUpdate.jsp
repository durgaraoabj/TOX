<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
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
					<td>${cli.doseTime }</td>
					<td>Dose Date</td>
					<td>${cli.doseDate }</td>
					<td>Interval between Subjects</td>
					<td>${cli.intervalBetweenSubject }</td>
			</tr>
			<tr><td colspan="6">Sample Time Points Information</td></tr>
			<tr>
					<td>Total Time Points</td>
					<td>${cli.noOfSamples }</td>
					<td>Pre Dose/ -ve</td>
					<td>${cli.negativeSamples }</td>
					<td>Zero Hour No of Vacutainer tubes</td>
					<td>${cli.zeroHourVacutaines }</td>
			</tr>
			<tr>
					<td>Post Time Points/ +ve</td>
					<td>${cli.positiveSamples }</td>
					<td>Ambulatory</td>
					<td>${cli.ambulatorys }</td>
					<td>No.of Batch size</td>
					<td>${cli.batches }</td>
			</tr>
			<tr>
				<td colspan="2">No of Vials for Sample Separation</td>
				<td>${cli.noOfVials }</td>
				<td/><td/><td/>
			</tr>
			
			<tr><td colspan="6" id="batchWiseSub0"></td></tr>
			<tr><td colspan="6" id="batchWiseSub"></td></tr>
			<tr><td colspan="6">Vital Information</td></tr>
			<tr>
					<td>Total Vitals</td>
					<td>${cli.noOfVitals }</td>
					<td>Pre Dose/ -ve Vita</td>
					<td>${cli.negativeVitals }</td>
					<td>No of Vials</td>
					<td>${cli.noOfVials }</td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function batchWiseSub(batches){
	var batch = batches.trim();
	if(batch != '' && batch > 1){
		var ele = "<table class='table table-bordered table-striped'><tr><th>Bath No</th><th>Sujects</th></tr>";
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