<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Template Comments</title>
</head>
<body>
<div class="card">
	<div class="card" style="background-color: rgba(51, 110, 123, 1); border-radius: 20px; border: 3px solid #f3fff0;">
		<h5 class="card-header info-color white-text text-center py-2">
			<c:choose>
				<c:when test="${type eq 'reviewComments'}">
					<strong style="color: white;">Template Reviewer Comments</strong>
				</c:when>
				<c:otherwise>
						<strong style="color: white;">Template Re-Assign Comments</strong>
				</c:otherwise>
			</c:choose>
		</h5>
	</div>
	<table class="table table-bordered table-striped" id="commentsTab">
		<thead>
			<tr>
				<th>Comments</th>
				<th>Created By</th>
				<th>Created On</th>
			</tr>
		</thead>
		<tbody>
		 <c:forEach items="${comList}" var="com">
		 	<c:if test="${com.comments ne null and com.comments ne ''}">
		 		<tr>
				<td>${com.comments}</td>
				<td>${com.createdBy}</td>
				<td>${com.createdOn}</td>
			</tr>
		 	</c:if>
		 </c:forEach>
			
		</tbody>
	</table>
</div>
</body>
</html>