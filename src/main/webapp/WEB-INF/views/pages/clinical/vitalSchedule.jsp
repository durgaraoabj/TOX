<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/study/clinical/vitalTimePontsPrivew" var="vitalTimePontsPrivew" />
<sf:form action="${vitalTimePontsPrivew}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">vital Schedule</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Dose Time : ${cli.doseTime}</td>
				<td>Dose Date : ${cli.doseDate}</td>
				<td>Name</td>
				<td colspan="6"><textarea name="name" class="form-control">MSR Sheet</textarea></td>
				<td>Time deviation</td>
				<td><input type="text" name="interval" id="interval" class="form-control" size="3" maxlength="3" placeholder="minutes"></input></td>
			</tr>
			<tr><td colspan="11">Pre Dose/ -ve Vital for Time Points</td></tr>
			<c:forEach begin="1" end="${cli.negativeVitals}" var="i" step="1">
			     <c:set var="j" value="${cli.negativeVitals-i+1}" scope="page"></c:set>
			     <tr><td>V-<c:out value="${j}"/></td>
				 	<td><input type="text" name="V-${j}" class="form-control" placeholder="HH.MM"></td>
				 	<td>Time deviation</td>
				 	<td><input type="text" name="V-${j}Time" class="form-control"></td>
				 	<td>Position</td>
				 	<td><input type="text" name="V-${j}position" class="form-control" placeholder="Minits"></td>
				 	<td><input type="checkbox" name="pulseRate-${j}" id="pulseRate-${j}" value="Pulse Rate">Pulse Rate</td>
				 	<td><input type="checkbox" name="oralTemp-${j}" id="oralTemp-${j}" value="Oral Temp">Oral Temp</td>
				 	<td><input type="checkbox" name="bp-${j}" id="bp-${j}" value="Blood Pressure">Blood Pressure</td>
				 	<td><input type="checkbox" name="respiratoryRate-${j}" id="respiratoryRate-${j}" value="Respiratory Rate">Respiratory Rate</td>
				 	<td><input type="checkbox" name="wellbeingAscertained-${j}" id="wellbeingAscertained-${j}" value="Wellbeing ascertained">Wellbeing ascertained</td>
				 </tr>
			</c:forEach>
			<tr><td colspan="11">Zero Hour</td></tr>
			<tr>
				<td>v-0</td>
				<td><input type="text" name="V-0" class="form-control" placeholder="HH.MM"></td>
				<td>Time deviation</td>
				 <td><input type="text" name="V-0Time" class="form-control" placeholder="Minits"></td>
				 <td>Position</td>
				 <td><input type="text" name="V-0position" class="form-control"></td>
				 	<td><input type="checkbox" name="pulseRate-0" id="pulseRate-0" value="Pulse Rate">Pulse Rate</td>
				 	<td><input type="checkbox" name="oralTemp-0" id="oralTemp-0" value="Oral Temp">Oral Temp</td>
				 	<td><input type="checkbox" name="bp-0" id="bp-0" value="Blood Pressure">Blood Pressure</td>
				 	<td><input type="checkbox" name="respiratoryRate-0" id="respiratoryRate-0" value="Respiratory Rate">Respiratory Rate</td>
				 	<td><input type="checkbox" name="wellbeingAscertained-0" id="wellbeingAscertained-0" value="Wellbeing ascertained">Wellbeing ascertained</td>
			</tr>
			
			<tr><td colspan="11">Post Dose /&nbsp;+ve Vital for Time Points</td></tr>
			<c:forEach var = "i" begin = "1" end = "${cli.noOfVitals-cli.negativeVitals-cli.ambulatorys-1}" step="1">
			     <tr><td>V ${i}</td>
				 	<td><input type="text" name="V${i}" class="form-control" placeholder="HH.MM"></td>
				 	<td>Time deviation</td>
				 	<td><input type="text" name="V-${i}Time" class="form-control" placeholder="Minits"></td>
				 	<td>Position</td>
				 	<td><input type="text" name="V-${i}position" class="form-control"></td>
				 	<td><input type="checkbox" name="pulseRate${i}" id="pulseRate${i}" value="Pulse Rate">Pulse Rate</td>
				 	<td><input type="checkbox" name="oralTemp${i}" id="oralTemp${i}" value="Oral Temp">Oral Temp</td>
				 	<td><input type="checkbox" name="bp${i}" id="bp${i}" value="Blood Pressure">Blood Pressure</td>
				 	<td><input type="checkbox" name="respiratoryRate${i}" id="respiratoryRate${i}" value="Respiratory Rate">Respiratory Rate</td>
				 	<td><input type="checkbox" name="wellbeingAscertained${i}" id="wellbeingAscertained${i}" value="Wellbeing ascertained">Wellbeing ascertained</td>
				 </tr>
			</c:forEach>
			<c:if test="${cli.ambulatorys >0}">
				<tr><td colspan="11">Vitals for Ambulatory</td></tr>
				<c:forEach var = "i" begin = "${cli.noOfVitals-cli.negativeVitals-cli.ambulatorys}" end = "${cli.noOfVitals-cli.negativeVitals-1}" step="1">
				    <tr><td>V ${ i }</td>
					<td><input type="text" name="V${ i }" class="form-control" placeholder="HH.MM"></td>
					<td>Time deviation</td>
				 	<td><input type="text" name="V-${i}Time" class="form-control" placeholder="Minits"></td>
				 	<td>Position</td>
				 	<td><input type="text" name="V-${i}position" class="form-control"></td>
					 	<td><input type="checkbox" name="pulseRate${ i }" id="pulseRate${ i }" value="Pulse Rate">Pulse Rate</td>
					 	<td><input type="checkbox" name="oralTemp${ i }" id="oralTemp${ i }" value="Oral Temp">Oral Temp</td>
					 	<td><input type="checkbox" name="bp${ i }" id="bp${ i }" value="Blood Pressure">Blood Pressure</td>
					 	<td><input type="checkbox" name="respiratoryRate${ i }" id="respiratoryRate${ i }" value="Respiratory Rate">Respiratory Rate</td>
					 	<td><input type="checkbox" name="wellbeingAscertained${ i }" id="wellbeingAscertained${ i }" value="Wellbeing ascertained">Wellbeing ascertained</td>
					 </tr>
				</c:forEach>
			</c:if>
			
			<tr><td colspan="11"><input type="button" value="View" class="btn btn-primary" onclick="submitForm()"/></td></tr>
			
		</table>
	</div>
</div>
</sf:form>
<script>
function submitForm(){
	$("#formsumit").submit();	
}
</script>