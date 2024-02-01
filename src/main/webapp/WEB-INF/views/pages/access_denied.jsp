<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="error-page">
		<div class="row">
			<div class="col-sm-3" align="left">
				<img src='<c:url value="/static/images/access_denied.png"/>'>
			</div>
			<div class="col-sm-3"></div>
			<div class="error-content col-sm-6">
	        	<h3><i class="fa fa-warning text-red"></i> Oops! Something went wrong.</h3>
		        <p>
	            	We will work on fixing that right away.
	           		Meanwhile, you may <a href='<c:url value="/dashboard/"/>'><strong>return to dashboard</strong></a> or try using the search form.
	          </p>
	        </div>
	    </div>
	</div>
</body>
</html>