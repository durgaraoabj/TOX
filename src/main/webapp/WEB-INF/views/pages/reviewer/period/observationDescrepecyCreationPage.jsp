<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<body>
	<br />
	<table class="table table-bordered table-striped">
		<tr>
			<th>Study No</th>
			<td>${data.subjectDataEntryDetails.study.studyNo }</td>
			<tH>Animal No</th>
			<td>${data.subjectDataEntryDetails.animal.animalNo  }</td>

		</tr>
		<tr>
			<th>Type</th>
			<td>${data.crf.type }</td>
			<th>Sub Type</th>
			<td>${data.crf.subType}</td>
		</tr>
		<tr>
			<th>Prefix</th>
			<td>${data.crf.prefix }</td>
			<th>Observation</th>
			<td>${data.crf.observationName}</td>
		</tr>
		<tr>
			<th>Field</th>
			<td>${data.element.name }</td>
			<th>Current Value</th>
			<th>${data.value }</th>
		</tr>
	</table>
	<table class="table table-bordered table-striped">
		<tr>
			<td colspan="5">Created Discrepancy's</td>
		</tr>
		<tr>
			<th>Date</th>
			<th>Created By</th>
			<th>Raised By</th>
			<th>Assigned To</th>
			<th>Old Value</th>
			<th>Old Status</th>
			<th>Value</th>
			<th>Comment</th>
			<th>Update Comment</th>
			<th>Status</th>
		</tr>
		<c:forEach items="${list}" var="v">
			<tr>
				<td>${v.createdOn}</td>
				<td>${v.createdBy}</td>
				<td>${v.risedBy}</td>
				<td>${v.assingnedTo}</td>
				<th>${v.oldValue}</th>
				<th>${v.oldStatus}</th>
				<th>${v.value}</th>
				<th>${v.comment}</th>
				<th>${v.updateReason}</th>
				<th>${v.status}</th>
			</tr>
		</c:forEach>
	</table>
	<br />
	<c:if test="${createDiscrepency}">
		<input type="hidden" name="dataId" id="dataId" value="${data.id }">
		<table class="table table-bordered table-striped">
			<tr>
				<th>User name</th>
				<td>${username}</td>
				<th>Assign To</th>
				<td><select id="userId" name="userId">
						<option value="-1" selected="selected">--select--</option>
						<c:forEach items="${allLoginUsers}" var="user">
							<option value="${user.id}">${user.username}</option>
						</c:forEach>
				</select> <font color="red" id="userIdmsg"></font></td>
			</tr>
			<tr>
				<th>Commentss</th>
				<td><input type="text" name="commentdesc" id="commentdesc" /><font
					color="red" id="commentdescmsg"></font></td>
				<td colspan="2"><c:if test="${status}">
						<input type="button" class="btn btn-warning btn-sm"
							onclick="saveDisc()" value="Save" />
					</c:if></td>
			</tr>
		</table>
	</c:if>

</body>
</html>