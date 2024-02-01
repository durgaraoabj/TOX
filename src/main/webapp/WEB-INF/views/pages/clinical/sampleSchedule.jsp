<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/study/clinical/sampleTimePontsPrivew" var="sampleTimePontsPrivew" />
<sf:form action="${sampleTimePontsPrivew}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sample Schedule</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Dose Time</td><td>${cli.doseTime}</td>
				<td>Dose Date</td><td colspan="2">${cli.doseDate}</td>
			</tr>
			<tr><td>Name of Sample Collection</td><td colspan="4"><textarea name="name" class="form-control">Blood Sample Collection</textarea></td></tr>
			<c:if test="${cli.negativeSamples >0}">
				<tr><td colspan="6">Pre Dose/ -ve Time Points</td></tr>
				<c:forEach begin="1" end="${cli.negativeSamples}" var="i" step="1">
				     <c:set var="j" value="${cli.negativeSamples-i+1}" scope="page"></c:set>
				     <tr><td>P-<c:out value="${j}"/></td>
					 <td colspan="4"><input type="text" name="P-${j}" class="form-control" placeholder="HH.MM"></td></tr>
				</c:forEach>
			</c:if>
			
			<tr><td colspan="6">Zero Hour</td></tr>
			<tr>
				<td>P-0</td>
				<td>Start</td>
				<td><input type="text" name="P-0Start" class="form-control" placeholder="HH.MM"></td>
				<td>End</td>
				<td><input type="text" name="P-0End" class="form-control" placeholder="HH.MM"></td>
			</tr>
			
			<tr><td colspan="6">Post Time Points/&nbsp;+ve</td></tr>
			<c:forEach var = "i" begin = "1" end = "${cli.positiveSamples}" step="1">
			     <tr><td>P ${i}</td>
				 <td colspan="4"><input type="text" name="P${i}" class="form-control" placeholder="HH.MM"></td></tr>
			</c:forEach>
			<c:if test="${cli.ambulatorys >0}">
			<tr><td colspan="6">ambulatory samples</td></tr>
			<c:forEach var = "i" begin = "1" end = "${cli.ambulatorys}" step="1">
			     <tr><td>P ${cli.positiveSamples + i }</td>
				 <td colspan="4"><input type="text" name="P${cli.positiveSamples + i }" class="form-control" placeholder="HH.MM"></td></tr>
			</c:forEach>
			</c:if>
			<tr><td colspan="6"><input type="button" value="View" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
	</div>
</div>
</sf:form>
<script>
function submitForm(){
	$("#formsumit").submit();	
}
</script>