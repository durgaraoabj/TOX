<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src='/TOX/static/js/validaiton.js'></script>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Group Observation Design</h3>
		</div>

		<div class="card-body">
			<c:url value="/admini/crf/crfupload" var="crfuploadUrl" />
			<sf:form action="${crfuploadUrl}" method="POST"
				modelAttribute="crfpojo" id="crfupload"
				enctype="multipart/form-data" role="form">
				<table id="example" class="table table-bordered table-striped"
					style="width: 80%">
					<tr>
						<td>Type</td>
						<td><select id="type" name="type"
							onchange="checkObservationType('type', 'typeMsg', '-1')"
							class="form-control">
								<option value="-1">--Select--</option>
								<option value="IN-LIFE OBSERVATIONS">IN-LIFE
									OBSERVATIONS</option>
								<option value="TERMINAL OBSERVATIONS">TERMINAL
									OBSERVATIONS</option>
						</select> <font id="typeMsg"></font></td>

						<td>Sub Type</td>
						<td><select id="subType" name="subType"
							onchange="checkRequired('subType', 'subTypeMsg', '-1')"
							class="form-control">
								<option value="-1">--Select--</option>
						</select> <font id="subTypeMsg"></font></td>
					</tr>
					<tr>
						<td>Prefix</td>
						<td><input type="text" name="prefix" id="prefix"
							onblur="observationNameValidation('prefix', 'prefixMsg', 'prefix')"
							class="form-control" /> <font color="red" id="prefixMsg"></font>
						</td>

						<td>Observation Name</td>
						<td><input type="text" name="observationName"
							id="observationName"
							onblur="observationNameValidation('observationName', 'observationNameMsg', 'observation')"
							class="form-control" /> <font color="red"
							id="observationNameMsg"></font></td>
					</tr>
					<tr>
						<td>Observation Desc</td>
						<td><textarea rows="3" cols="20" name="observationDesc"
								id="observationDesc" class="form-control"></textarea></td>

						<td>From</td>
						<td><select id="configurationFor" name="configurationFor"
							onchange="checkType('configurationFor', 'configurationForMsg')"
							class="form-control">
								<option value="-1">--Select--</option>
								<option value="Acclimatization">Acclimatization</option>
								<option value="Treatment">Treatment</option>
								<option value="Both">Both</option>
						</select> <font id="configurationForMsg"></font></td>
					</tr>
					<tr>
						<td>Select Roles <font color="red">*</font></td>
						<td>
							<table class="table table-bordered table-striped">
								<tr>
									<td><input type="checkbox" id="allRoles" name="allRoles"
										onclick="checkOrUnchekAll('allRoles', 'roles')" />All</td>
									<td>Role</td>
									<td>Role Desc</td>
								</tr>
								<c:forEach items="${roles}" var="role">
									<tr>
										<td><input type="checkbox" name="roles"
											value="${role.id}"></td>
										<td>${role.role}</td>
										<td>${role.roleDesc }</td>
									</tr>
								</c:forEach>
								<tr>
									<td colspan="2"><font id="rolesMsg"></font></td>
								</tr>
							</table>
						</td>
						<td colspan="2">
							<table>
								<TR>
									<Td>Upload ECRF</Td>
									<TD><input type="file" name="file" id="exampleInputFile" /><font
										color="red" id="exampleInputFileMsg"></font></TD>
								</TR>
								<tr>
									<td>Upload Template File :</td>
									<td><input type="file" name="tempfile" id="tempfile" /><font
										color="red" id="tempfileMsg"></font></td>
								</tr>
								<tr align="center">
									<td colspan="2"><c:if
											test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
											<button type="button" class="btn btn-primary"
												id="formSubmitBtn">Submit</button>
										</c:if></td>
								</tr>
							</table>
						</td>
					</tr>


				</table>

			</sf:form>
			<!-- /.card -->
		</div>
	</div>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">All E-Forms</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<td>Type</td>
						<td>Sub Type</td>
						<th>Prefix</th>
						<th>Observation Name</th>
						<th>From</th>
						<th>Roles</th>
						<th>View</th>

						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${crfList}" var="crf">
						<tr>
							<td>${crf.type}</td>
							<td>${crf.subType}</td>
							<td>${crf.prefix}</td>
							<td>${crf.observationName}</td>
							<td>${crf.configurationFor }</td>
							<td><c:forEach items="${crf.observationRoles}" var="role"
									varStatus="st">
									<c:choose>
										<c:when test="${st.count == 1}">
										${role.roleMaster.role}
									</c:when>
										<c:otherwise>
										,${role.roleMaster.role}
									</c:otherwise>
									</c:choose>
								</c:forEach></td>
							<td><a
								href='<c:url value="/admini/crf/viewCrf/${crf.id}" />'
								class="btn btn-primary">Click</a></td>
							<c:choose>
								<c:when
									test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
									<td><c:if test="${crf.active}">
											<a
												href='<c:url value="/admini/crf/crfChangeStatus/${crf.id}" />'
												class="btn btn-primary">ACTIVE</a>
										</c:if> <c:if test="${crf.active eq false}">
											<a
												href='<c:url value="/admini/crf/crfChangeStatus/${crf.id}" />'
												class="btn btn-primary">NOT ACTIVE</a>
										</c:if></td>
								</c:when>
								<c:otherwise>
									<td><c:if test="${crf.active}">ACTIVE</c:if> <c:if
											test="${crf.active eq false}">NOT ACTIVE</c:if></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<!-- /.card-body -->
	</div>
	<!-- /.card -->

</body>
<script type="text/javascript">
	function checkObservationType(id, msgId, compareValue) {
		$("#subType").empty();
		$('#subType').append(
				$('<option></option>').attr('value', '-1').text('--Select--'));
		if (!checkRequired(id, msgId, compareValue)) {
			if ($("#" + id).val() == 'IN-LIFE OBSERVATIONS') {
				$('#subType').append(
						$('<option></option>').attr('value',
								'Clinical Observations').text(
								'Clinical Observations'));
				$('#subType').append(
						$('<option></option>').attr('value', 'CNS/PNS').text(
								'CNS/PNS'));
				$('#subType').append(
						$('<option></option>').attr('value', 'Observations')
								.text('Observations'));
				$('#subType').append(
						$('<option></option>').attr('value', 'Body Weight')
								.text('Body Weight'));
				$('#subType').append(
						$('<option></option>').attr('value', 'Feeding Details')
								.text('Feeding Details'));
			} else if ($("#" + id).val() == 'TERMINAL OBSERVATIONS') {
				$('#subType').append(
						$('<option></option>').attr('value',
								'Clinical Pathology')
								.text('Clinical Pathology'));
				$('#subType').append(
						$('<option></option>').attr('value',
								'Terminal Body Weight').text(
								'Terminal Body Weight'));
				$('#subType').append(
						$('<option></option>').attr('value',
								'Absolute Organ Weight').text(
								'Absolute Organ Weight'));
				$('#subType').append(
						$('<option></option>').attr('value',
								'Relative Organ Weight').text(
								'Relative Organ Weight'));
				$('#subType').append(
						$('<option></option>').attr('value',
								'Anatomic Pathology')
								.text('Anatomic Pathology'));
			}
		}
	}

	var oberFlag = false;
	function observationNameValidation(id, messageId, type) {
		$('#' + messageId).html("");
		var value = $('#' + id).val();
		if (value != null && value != "" && value != "undefinde") {
			var url = mainUrl + "/admini/crf/checkObserVationNameStatus/"
					+ value + "/" + type;
			var result = asynchronousAjaxCall(url);
			if (result == "") {
				oberFlag = true;
			} else
				$('#' + messageId).html(result);
		}
	}

	function checkType(id, messageId) {
		$('#' + messageId).html("");
		if ($("#" + id).val() == -1) {
			$('#' + messageId).html("Required Field");
			return false;
		} else
			return true;
	}
</script>

<script type="text/javascript">
	$('#formSubmitBtn')
			.click(
					function() {

						var flag = true;
						if (checkRequired('type', 'typeMsg', '-1')) {
							flag = false;
						}
						if (checkRequired('subType', 'subTypeMsg', '-1')) {
							flag = false;
						}
						if (checkRequired('prefix', 'prefixMsg', '')) {
							flag = false;
						}
						if (checkRequired('observationName',
								'observationNameMsg', '')) {
							flag = false;
						}
						if (checkRequired('configurationFor',
								'configurationForMsg', '-1')) {
							flag = false;
						}
						if (checkCheckboxRequired('roles', 'rolesMsg')) {
							flag = false;
						}
						debugger;
						if (checkRequired('exampleInputFile',
								'exampleInputFileMsg', '')
								&& checkRequired('tempfile', 'tempfileMsg', '')) {
							flag = true;
						} else {
							if (fileValidatation('exampleInputFile',
									'exampleInputFileMsg', 'xlsx')) {
								flag = false;
							}
							if (fileValidatation('tempfile', 'tempfileMsg', 'pdf')) {
								flag = false;
							}
							if (flag) {
								$('#crfupload').submit();
							}

						}

					});

	function requiredValidation(id, msgId, compareValue) {
		$("#" + msgId).html("");
		var value = $("#" + id).val();
		// 		value = value.trim();
		if (value == compareValue) {
			$("#" + msgId).html("Required Field");
			return true;
		} else
			return false;
	}
</script>
</html>