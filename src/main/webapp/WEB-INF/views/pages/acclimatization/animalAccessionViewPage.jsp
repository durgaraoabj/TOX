<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Animal Accession View</title>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Animal Accession View</h3>
		</div>
		<div class="card-body">
		<table class="table table-bordered table-striped" style="width:60%">
			<c:forEach items="${saaList}" var="saa" varStatus="st">
				<c:if test="${st.count eq 1}">
					<tr>
						<td>Prefix :</td>
						<td colspan="3">${saa.prefix}</td>
					</tr>
				</c:if>
				<tr>
					<td>Gender :</td>
					<td>${saa.gender}</td>
					<td>No of Animals :</td>
					<td>${saa.noOfAnimals}</td>
				</tr>
			</c:forEach>
		</table>
		<table class="table table-bordered table-striped" style="width:60%">
			<c:forEach items="${animals}" var="sa" varStatus="st">
				<tr>
					<th colspan="6">${sa.key}</th>
				</tr>
				<c:forEach items="${sa.value}" var="anims">
					<tr>
						<c:forEach items="${anims}" var="a">
							<td>${a.animalNo}${a.genderCode} </td>
						</c:forEach>
					</tr>
				</c:forEach>
				
			</c:forEach>
		</table>
		</div>
	</div>
</body>
</html>