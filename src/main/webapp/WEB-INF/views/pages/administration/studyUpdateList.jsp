<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Study Update List</title>
</head>
<body>
	<div style="text-align: center;font-size: large; font-weight: bold;">Study Update List </div>
	<table class="table table-bordered table-striped" id="stdupdateTab">
		<thead>
			<tr align="center">
				<th>Study Number</th>
				<th>Title</th>
				<th>Start Date</th>
				<th>Update</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${smList}" var="sm">
				<c:if test="${sm.id ne 1 }">
					<tr>
						<td>${sm.studyNo}</td>
						<td>${sm.studyDesc}</td>
						<td><fmt:formatDate value="${sm.startDate}" pattern="yyyy-MM-dd"/></td>
						<td align="center"><a href='<c:url value="/administration/updateStudy/${sm.id}"/>'><i class="fa fa-edit" style="font-size:24px"></i></a></td>
					</tr>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
<script type="text/javascript">
$(function(){
	 $("#stdupdateTab").DataTable();
});
</script>
</body>
</html>