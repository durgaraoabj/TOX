<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
	<select class="form-control" name="crf" id="crf" onchange="displayCrfsElemenst()">
		<option value="-1" selected="selected">--Select--</option>
	<c:forEach items="${periodCrf}" var="pc">
		<option value="${pc.crfId}">${pc.crfName}</option>
	</c:forEach>
	</select>
</body>
</html>