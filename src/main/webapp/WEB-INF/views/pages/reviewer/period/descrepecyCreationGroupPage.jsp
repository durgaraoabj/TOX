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
			<th>RisedBy By</th>
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
			<th>${v.secEleData.value}</th>
			<th>${v.status}</th>	
		</tr>
	</c:forEach>
	</table>
	<br/>
	
	<c:url value="/studyexe/studyCrfSave" var="studyCrfSave" />		
	<sf:form action="${studyCrfSave}" method="POST" modelAttribute="crfpojo"
   		id="studyCrfSave" enctype="multipart/form-data">
   		<input type="hidden" name="vpcId" id="vpcId2" value="${vpc.id }">
   		<input type="hidden" name="crfId" id="crfId2" value="${crf.id }">
   		<input type="hidden" name="crfId" id="eleId" value="${ele.id }">
   		<input type="hidden" name="dataId" id="dataId" value="${data.id }">
   		<input type="hidden" name="kayName" id="kayName" value="${kayName}">
   		<input type="hidden" name="username" id="username" value="${username }">
   		<table class="table table-bordered table-striped">
   			<tr>
   				<th>User name</th>
   				<td>${username}</td>
   				<th>Assign To</th>
   				<td>
	   				<select id="userId" name="userId">
	   					<option value="-1" selected="selected">--select--</option>
	   					<c:forEach items="${allLoginUsers}" var="user">
	   						<option value="${user.id}">${user.username}</option>
	   					</c:forEach>
	   				</select>	
	   				<font color="red" id="userIdmsg"></font>
   				</td>
   			</tr>
   			<tr>
   				<th>Comment</th>
   				<td>
   					<input type="text" name="commentdesc" id="commentdesc"/><font color="red" id="commentdescmsg"></font>
   				</td>
   				<td colspan="2">
   				<input type="button" class="btn btn-warning btn-sm" onclick="saveDiscGroup()" value="Save"/>
   				</td>
   			</tr>
   		</table>
   	</sf:form>
</body>
</html>