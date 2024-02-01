<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<body>
<br/>
	<table class="table table-bordered table-striped">
		<tr>
			<th>Study No</th>
			<td>${crf.std.studyNo }</td>
			<th>Subject id</th>
			<td>${vpc.vol.volId }</td>
			<th>Phase</th>
			<td>${vpc.period.name }</td>
		</tr>
		<tr>
			<th>Crf Name</th>
			<td>${crf.libCrf.name }</td>
			<th>Element</th>
			<td>${ele.name}</td>
			<th>Current Value</th>
			<th>${data.value }</th>
		</tr>
	</table>
	<table class="table table-bordered table-striped">
		<tr><td colspan="5">Available Desc</td></tr>
		<tr>
			<th>Date</th>
			<th>Created By</th>
			<th>Raised By</th>
			<th>Assigned To</th>
			<th>Value</th>
			<th>Status</th>
		</tr>
	<c:forEach items="${list}" var="v">
		<tr>
			<td>${v.createdOn}</td>
			<td>${v.createdBy}</td>
			<td>${v.risedBy}</td>
			<td>${v.assingnedTo}</td>
			<th>${v.oldValue}</th>
			<th>${v.status}</th>	
		</tr>
	</c:forEach>
	</table>
	<br/>
</body>
</html>