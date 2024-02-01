<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<table class="table table-bordered table-striped">
		<tr>
			<td>STUDY NO</td><td>${std.studyNo }</td>
			<td>PERIOD NO</td><td>${period.name }</td>
		</tr>
		<tr>
			<td>Protocal</td><td>${std.protocalNo }</td>
			<td>subeject</td><td>${subeject }</td>
		</tr>
	</table>
	<table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th colspan="11">${clinical.vitalShetName }</th>
                </tr>
                <tr>
                	<th>Type</th>
	   				<th>Time Point</th>
	   				<th>Planned Time</th>
	   				<th>Schedule Time</th>
	   				<th>Actual Time</th>
	   				<th>Deviation</th>
	   				<th>OralTemp (°F)</th>
	   				<th>Blood Pressure(mm Hg)</th>
	   				<th>Pulse Rate(/min)</th>
	   				<th>Respiratory rate(/min)</th>
	   				<th>Participant's Wellbeing ascertained</th>
	   				<th>Done By</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${vitalData}" var="d">
	   				<tr>
	   					<td>${d.type}</td>
	   					<td>${d.timePoint}</td>
	   					<td>${d.planedTime}</td>
	   					<td>${d.scheduleTime}</td>
	   					<td>${d.actualTime}</td>
	   					<td>${d.deviationOfCollection }</td>
	   					<c:choose>
   							<c:when test="${d.pulseRate == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
   							<c:when test="${d.pulseRate == 'Yes'}"><td></td></c:when>
   							<c:otherwise><td>${d.pulseRate}</td></c:otherwise>
   						</c:choose>
   						<c:choose>
   							<c:when test="${d.oralTemp == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
   							<c:when test="${d.oralTemp == 'Yes'}"><td></td></c:when>
   							<c:otherwise><td>${d.oralTemp}</td></c:otherwise>
   						</c:choose>
   						<c:choose>
   							<c:when test="${d.bp == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
   							<c:when test="${d.bp == 'Yes'}"><td></td></c:when>
   							<c:otherwise><td>${d.bp}</td></c:otherwise>
   						</c:choose>
   						<c:choose>
   							<c:when test="${d.respiratoryRate == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
   							<c:when test="${d.respiratoryRate == 'Yes'}"><td></td></c:when>
   							<c:otherwise><td>${d.respiratoryRate}</td></c:otherwise>
   						</c:choose>
   						<c:choose>
   							<c:when test="${d.wellbeingAscertained == 'No'}"><td background="<c:url value="/static/lines.png"/>"></td></c:when>
   							<c:when test="${d.wellbeingAscertained == 'Yes'}"><td></td></c:when>
   							<c:otherwise><td>${d.wellbeingAscertained}</td></c:otherwise>
   						</c:choose>
   						<td>${d.doneBy}</td>
	   				</tr>
 	   			</c:forEach>  
                </tbody>
              </table>