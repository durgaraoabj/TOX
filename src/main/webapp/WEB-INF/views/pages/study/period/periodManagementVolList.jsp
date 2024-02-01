<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="box-header with-border">
		<h3 class="box-title">Phase Management Subject List</h3>
		<br />
		<table class="table table-bordered table-striped">
			<tr>
				<td>Study : ${study.studyNo }</td>
				<td>Phase : ${period.name }</td>
			</tr>
		</table>
		<div class="box-tools pull-right">
			<button type="button" class="btn btn-box-tool" data-widget="collapse"
				data-toggle="tooltip" title="Collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
	</div>
	<div class="box-body">
		<table id="userStudies" class="table table-bordered table-striped">
			<thead>
				<tr>
					<th>Bed No</th>
					<th>Subject Id</th>
					<th>Subject Name</th>
					<th>Crf Exe</th>
				</tr>
			</thead>
			<c:forEach items="${vpcsl}" var="vpc">
				<tr>
					<td>${vpc.vol.bedNo }</td>
					<td>${vpc.vol.volId }</td>
					<td>${vpc.vol.volName }</td>
					<td>
						<c:if test="${vpc.status eq 'NOT STARTED'}">
							<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${vpc.vol.id}/${period.id}/" />' 
 									class="btn btn-warning btn-sm">NOT STARTED</a>	
						</c:if>
						<c:if test="${vpc.status eq 'Data Entry'}">
							<a href='<c:url value="/period/viewSubjectInfo/${period.id}" />' 
 									class="btn btn-warning btn-sm">Data Entry</a>	
						</c:if>
						<c:if test="${vpc.status eq 'Mark As Complete'}">
							<a href='<c:url value="/period/viewSubjectInfo/${period.id}" />' 
 									class="btn btn-warning btn-sm">Mark As Complete</a>	
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
<script type="text/javascript">
	
</script>
</html>