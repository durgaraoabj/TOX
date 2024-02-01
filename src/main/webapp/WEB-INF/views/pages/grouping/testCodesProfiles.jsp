<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Profile</title>
<script type="text/javascript">
	var insturmentcs = {};
	var tempTcs = [];
	var instrumentNames = {};
</script>
</head>
<body>
<div class="card">
<div class="card-header">
<h3 class="card-title">Standard Parameters List</h3><input
							type="button" value="Change Status" class="btn btn-clBtn"
							onclick="changeProfileStatus()" style="float:right">
</div>
	<div class="card-body">
		<c:url value="/grouping/changeProfileStatus" var="profileStatus"></c:url>
		<form:form action="${profileStatus}" method="POST" id="profileForm">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<!-- <tr>
						<th colspan="5">Standard Parameter List's 
						</th>
					</tr> -->
					<tr>
						<th>Active</th>
						<th>Standard Parameter List Name</th>
						<th>Instrument</th>
						<th>Created By</th>
						<th>Created On</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${testCodesProfiles}" var="profileVar">
						<tr>
							<td><c:choose>
									<c:when test="${profileVar.activeStatus}">
										<input type="checkbox" name="profileId" onchange="changeProfileStatus()" 
											checked="checked" value="${profileVar.profileId}">
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="profileId"
											value="${profileVar.profileId}">
									</c:otherwise>
								</c:choose></td>
							<td><a href="javascript:void(0);"
								onclick="testCodesProfileView('${profileVar.profileId}')">${profileVar.profileName}</a>
							</td>
							<td>${profileVar.insturment.instrumentName}</td>
							<td>${profileVar.createdBy}</td>
							<td>${profileVar.createdOn}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form:form>

		<br />
		<div id="profileDiv">
			<c:url value="/grouping/testCodesProfilesSave" var="saveprofil"></c:url>
			<form:form action="${saveprofil}" method="POST" id="saveform">
				<input type="hidden" name="addedTcIds" id="addedTcIds">
				<table id="example2" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th colspan="2">Standard Parameter List Creation</th>
						</tr>
					</thead>
					<tbody>
					<tr>
						<td>Instrument</td>
						<td><select id="instrument" name="instrument" class="form-control"
							onchange="instrumentChange(this.value)">
								<option value="-1" selected="selected" >--Select--</option>
								<c:forEach items="${intrumentIpAddress}" var="inst">
									<option value="${inst.id}">${inst.instrumentName}</option>
								</c:forEach>
						</select> <font color="red" id="instrumentMsg"></font></td>
					</tr>
					<tr>
						<td>Standard Parameter List Name</td>
						<td><input type="text" name="profileName" id="profileName" class="form-control"
							onchange="checkaProfileName(this.value)" /> <font color="red"
							id="profileNameMsg"></font></td>
					</tr>
					<tr>
						<td id="testCodesTd" colspan="2"></td>
					</tr>
					<tr>
						<td></td>
						<td><input type="button" value="Save" onclick="saveProfile()" class="btn btn-new" />
						</tbody>
				</table>
			</form:form>
		</div>
	

	<div class="modal fade" id="profiledataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Standard Parameter
						List View</h4>
				</div>
				<div class="modal-body">
					<div id="profileData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('profiledataModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
</body>
<script src='/TOX/static/js/grouping/testCodesProfile.js'></script>
</html>