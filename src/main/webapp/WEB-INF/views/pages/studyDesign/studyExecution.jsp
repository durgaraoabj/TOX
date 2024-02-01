<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
	var group = [];
</script>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Study Execution</b>
			</h3>
		</div>
		<div class="card-body">
			<font color="red">${pageMsg}</font>
			<table class="table table-bordered table-striped" style="width: 90%">
				<tr>
					<th>Select Group</th>
					<th><select name="groupId" id="groupId"
						onchange="groupSelection(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${group}" var="g">
								<c:if test="${g.dataEntry eq true}">
									<option value="${g.id}">${g.groupName}</option>
								</c:if>
							</c:forEach>
					</select><font color='red' id="groupIdMsg"></font></th>
					<c:if test="${study.requiredSubGroup}">
						<th>Select Sub Group</th>
						<th><select id="subGroupId" name="subGroupId"
							onchange="subGroupObservations(this.value)">
								<option value="-1">--Select--</option>
						</select><font color='red' id="subGroupIdMsg"></font></th>
					</c:if>
				</tr>
				<tr>
					<th>Observation</th>
					<th><select id="observationId" name="observationId"
						onchange="displayAnimals()">
							<option value="-1">--Select--</option>
					</select><font color='red' id="observationIdMsg"></font></th>
				</tr>
				<tr>
					<c:if test="${study.gender == 'Both' }">
						<th>Select Gender</th>
						<th><select id="gender" name="gender"
							onchange="displayAnimals()">
								<option value="-1">--Select--</option>
								<option value="Male">Male</option>
								<option value="Female">Female</option>
						</select><font color='red' id="genderMsg"></font></th>
					</c:if>
				</tr>
				<tr>
					<th>Animal No</th>
					<th><select id="animalIdPk" name="animalId"
						onchange="viewTreatmentObservation()">
							<option value="-1">--Select--</option>
					</select><font color='red' id="animalIdMsg"></font></th>
				</tr>
			</table>

		</div>
		<div id="treatmentFrom"></div>
	</div>
	<c:url value="/studyExecution/subgroupSubjects" var="url" />
	<sf:form action="${url}" method="POST" id="form">
		<input type="hidden" name="subGroupId" id="subGroupId" />
		<input type="hidden" name="groupId" id="groupId" />
		<!--              <div style="text-align:center">  -->
		<%--             	<a href='<c:url value="/admini/crf/" />' class="btn btn-primary">EXIT</a>  --%>
		<!--              </div>  -->
	</sf:form>
</body>
<script type="text/javascript">
	function groupSelection(groupId) {
		$("#groupIdMsg").html("");
		if (groupId != -1) {
			var subGroups = '${study.requiredSubGroup}';
			if (subGroups == 'true') {
				$("#subGroupId").empty()
				var result = asynchronousAjaxCall(mainUrl
						+ "/studyExecution/subGroups/" + groupId);
				if (result != '' || result != "undefined") {
					$("#subGroupId").append(
							$('<option></option>').attr('value', '-1').text(
									'--Select--'));
					if (result.length > 0) {
						for (var i = 0; i < result.length; i++) {
							$("#subGroupId").append(
									$('<option></option>').attr('value',
											result[i].id).text(result[i].name));
						}
					} else {
						alert("Server Error");
					}
				} else {
					$("#subGroupId").append(
							$('<option></option>').attr('value', '-1').text(
									'--Select--'));
				}
			} else {
				groupObservations(groupId)
			}
		} else {
			$("#groupIdMsg").html("Required Field");
		}

	}
	function groupObservations(groupId) {
		$("#observationId").empty();
		var result = asynchronousAjaxCall(mainUrl
				+ "/studyExecution/groupObservations/" + groupId);
		observations(result);
	}
	function subGroupObservations(subGroupId) {
		$("#observationId").empty()
		observations(asynchronousAjaxCall(mainUrl
				+ "/studyExecution/subGroupObservations/" + subGroupId));
	}

	function observations(result) {
		if (result != '' || result != "undefined") {
			$("#observationId").append(
					$('<option></option>').attr('value', '-1').text(
							'--Select--'));
			if (result.length > 0) {
				for (var i = 0; i < result.length; i++) {
					$("#observationId")
							.append(
									$('<option></option>')
											.attr('value', result[i].id)
											.text(
													"Type :"
															+ result[i].type
															+ ", Sub Type:"
															+ result[i].subType
															+ ",Prefix:"
															+ result[i].prefix
															+ ",Observation:"
															+ result[i].observationName));
				}
			} else {
				alert("Server Error");
			}
		} else {
			$("#observationId").append(
					$('<option></option>').attr('value', '-1').text(
							'--Select--'));
		}
	}
	function displayAnimals() {
		
		if (checkRequiredField('observationId', 'observationIdMsg', -1)) {
			var fg = false;
			var url = mainUrl + "/studyExecution/animals" + "/"
					+ $("#observationId").val();
			if ('${study.gender}' == 'Both') {
				if (checkRequiredField('gender', 'genderMsg', -1)) {
					url = url + "/" + $("#gender").val();
					fg = true;
				}
			} else {
				fg = true;
				url = url + "/gender";
			}
			debugger;
			if (fg) {
				var result = asynchronousAjaxCall(url);
				if (result != '' || result != "undefined") {
					$("#animalIdPk").append(
							$('<option></option>').attr('value', '-1').text(
									'--Select--'));
					if (result.length > 0) {
						for (var i = 0; i < result.length; i++) {
							$("#animalIdPk").append(
									$('<option></option>').attr('value',
											result[i].id).text(
											result[i].animalNo));
						}
					} else {
						alert("Server Error");
					}
				} else {
					$("#animalIdPk").append(
							$('<option></option>').attr('value', '-1').text(
									'--Select--'));
				}
			}
		}
	}

	function checkRequiredField(id, msg, defaultVal) {
		$("#" + msg).html("");
		if ($("#"+id).val() == -1) {
			$("#" + msg).html("Required Field");
			return false;
		} else
			return true;
	}

	function viewTreatmentObservation() {
		if (checkRequiredField('groupId', 'groupIdMsg', -1)
				&& checkRequiredField('observationId', 'observationIdMsg', -1)) {
			var fg = false;
			var gender = "gender";
			if ('${study.gender}' == 'Both') {
				if (checkRequiredField('gender', 'genderMsg', -1)) {
					fg = true;
					gender = $("#gender").val();
				}
			} else {
				fg = true;
			}
			if (fg) {
				if (checkRequiredField('animalIdPk', 'animalIdMsg', -1)) {
					var result = asynchronousAjaxCall(mainUrl
							+ "/studyExecution/unschudelTreatmentAnimalCrfDataEntry/"
							+ $("#observationId").val() + "/"+gender);
					$("#treatmentFrom").html(result);
				}
			}
		}
	}
</script>
</html>