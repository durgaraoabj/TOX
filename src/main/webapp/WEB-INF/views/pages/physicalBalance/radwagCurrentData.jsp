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
			<th>Date And Time</th>
			<td>${curentData.dateAndTime}</td>
		</tr>
		<tr>
			<th>Batch No</th>
			<td>${curentData.batchNo }</td>
		</tr>
		<tr>
			<th>Nozzle No</th>
			<td>${curentData.nozzleNo }</td>
		</tr>
		<tr>
			<th>Gross Wt</th>
			<td>${curentData.grossWt }</td>
		</tr>
		<tr>
			<th>tareWt</th>
			<td>${curentData.tareWt}</td>
		</tr>
		<tr>
			<th>netWt</th>
			<th>${curentData.netWt}</th>
		</tr>
		<tr>
			<th>status</th>
			<td>${curentData.status }</td>
		</tr>
	</table>
	<br/>
</body>
</html>