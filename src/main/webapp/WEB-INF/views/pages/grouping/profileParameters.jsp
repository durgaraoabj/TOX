<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
	<table class="table table-bordered table-striped">
		<tr>
			<th>Standard Parameter List Name</th>
			<td>${profile.profileName}</td>
		</tr>
		<tr>
			<th>Instrument</th>
			<td>${profile.insturment.instrumentName}</td>
		</tr>
		<tr>
			<th>Parameter</th>
			<th>Order</th>
		</tr>
		<c:forEach items="${testcodes}" var="tc">
			<tr>
				<td>${tc.testCode.disPalyTestCode}</td>
				<td>${tc.orderNo}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>