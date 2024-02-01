<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
    	<h3 class="box-title">Phase Management for Study : ${study.studyNo }</h3>
        <div class="box-tools pull-right">
   	    	<button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
            	<i class="fa fa-minus"></i>
            </button>
        </div>
    </div>
    <div class="box-body">
		   		<table id="userStudies" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Phase Name</th>
			   				<th>Start Date</th>
			   				<th>End Date</th>
			   				<th>Phase Exe</th>
			   			</tr>
			   		</thead>
			   		<c:forEach items="${plist}" var="period">
			   			<tr>
			   				<td>${period.name }</td>
			   				<td>${period.startDate }</td>
			   				<td>${period.endDate }</td>
			   				<td>
			   					<c:choose>
			   						<c:when test="${period.crfStatus eq ''}">
			   							<a href='<c:url value="/period/viewSubjectInfo/${period.id}" />' class="btn btn-warning btn-sm">NOT STARTED</a>
			   						</c:when>
			   						<c:when test="${period.crfStatus eq 'In Progress'}">
			   							<a href='<c:url value="/dashboard/changeStudy/${period.id}" />' class="btn btn-warning btn-sm">In PROGESS</a>
			   						</c:when>
			   						<c:when test="${period.crfStatus eq 'In Review'}">
			   							<a href='<c:url value="/dashboard/changeStudy/${period.id}" />' class="btn btn-warning btn-sm">IN REVIEW</a>
			   						</c:when>
			   						<c:when test="${period.crfStatus eq 'On Hold'}">
			   							<a href='<c:url value="/dashboard/changeStudy/${period.id}" />' class="btn btn-warning btn-sm">ON HOLD</a>
			   						</c:when>
			   						<c:when test="${period.crfStatus eq 'Discrepancy Raised'}">
			   							<a href='<c:url value="/dashboard/changeStudy/${period.id}" />' class="btn btn-warning btn-sm">DISREPANCY RAISED</a>
			   						</c:when>
			   						<c:when test="${period.crfStatus eq 'Completed'}">
			   							<a href='<c:url value="/dashboard/changeStudy/${period.id}" />' class="btn btn-warning btn-sm">COMPLETED</a>
			   						</c:when>
			   						<c:otherwise>
			   							<a href='#" />' class="btn btn-warning btn-sm">NO STATE </a>
			   						</c:otherwise>
			   					</c:choose>
			   				</td>
			   			</tr>
			   		</c:forEach>
		   		</table>
    </div>
</body>
<script type="text/javascript">
  
</script>
</html>