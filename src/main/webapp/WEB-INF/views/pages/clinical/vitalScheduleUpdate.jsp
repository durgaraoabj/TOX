<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<div class="card">
	<div class="card-header">
		<h3 class="card-title"><B>Vital Schedule</B></h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Dose Time : ${cli.doseTime}</td>
				<td>Dose Date : ${cli.doseDate}</td>
				<td>Name</td>
				<td colspan="2">${cli.vitalShetName}</td>
				<td>Interval  between Subject</td>
				<td>${cli.intervalBetweenSubjectVital}</td>
			</tr>
		</table>	
		<table id="example1" class="table table-bordered table-striped">
			<tr><th>Type</th><th>Time Point</th><th>Time Deviation</th><th>Position</th><th>Time Point Date</th><th>Planed Time</th><th>Pulse Rate</th><th>Oral Temp</th><th>BP</th><th>Respiratory Rate</th><th>Wellbeing Ascertained</th></tr>
			<c:forEach items="${timePoints}" var="tp">
				<tr>
					<td>${tp.type }</td>
					<td>${tp.timePoint }</td>
					<td>${tp.interval }</td>
					<td>${tp.position }</td>
					<td>${tp.timePointDate }</td>
					<td>${tp.planedTime }</td>
					
					<c:choose>
						<c:when test="${tp.pulseRate == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
						<c:when test="${tp.pulseRate == 'Yes'}"><td></td></c:when>
					</c:choose>
   					<c:choose>
						<c:when test="${tp.oralTemp == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
						<c:when test="${tp.oralTemp == 'Yes'}"><td></td></c:when>
					</c:choose>
					<c:choose>
						<c:when test="${tp.bp == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
						<c:when test="${tp.bp == 'Yes'}"><td></td></c:when>
					</c:choose>
					<c:choose>
						<c:when test="${tp.respiratoryRate == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
						<c:when test="${tp.respiratoryRate == 'Yes'}"><td></td></c:when>
					</c:choose>
					<c:choose>
						<c:when test="${tp.wellbeingAscertained == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
						<c:when test="${tp.wellbeingAscertained == 'Yes'}"><td></td></c:when>
					</c:choose>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>
