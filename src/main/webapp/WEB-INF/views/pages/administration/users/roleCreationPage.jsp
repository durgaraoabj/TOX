<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Role Creation Page</title>
</head>
<body>

	<c:if test="${messageOfPage != null}">
		<div class="card" id="success"
			style="background-color: rgba(0, 128, 0, 0.9); border-radius: 20px; border: 3px solid #f3fff0; width: 50%; margin-left: 25%;">
			<h5 class="card-header info-color white-text text-center py-2">
				<strong style="color: white;">${messageOfPage}</strong>
			</h5>
		</div>
	</c:if>
	<c:if test="${errorMsgOfPage != null}">
		<div class="card" id="failed"
			style="background-color: rgba(255, 0, 0, 0.9); border-radius: 20px; border: 3px solid #f3fff0; width: 50%; margin-left: 25%;">
			<h5 class="card-header info-color white-text text-center py-2">
				<strong style="color: white;">${errorMsgOfPage}</strong>
			</h5>
		</div>
	</c:if>
	<!-- rgba(137, 196, 244, 1) rgba(51, 110, 123, 1)-->
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Role Creation</h3>
		</div>
		<!--Card content-->
		<div class="card-body">
			<c:url value="/administration/saveNewRoleMaster" var="saveRole" />
			<form:form action="${saveRole}" method="post"
				modelAttribute="rolePojo" id="saveRoleMaster">
				<table class="table table-bordered table-striped"
					style="margin-left: 25%; width: 40%;">
					<tr>
						<td>Role Name :</td>
						<td>
							<div>
								<form:input path="role" id="role" cssClass="form-control"
									onblur="roleValidation('role', 'roleMsg')" />
							</div>
							<div id="roleMsg" style="color: red;"></div>
						</td>
					</tr>
					<tr>
						<td>Role Description :</td>
						<td>
							<div>
								<form:input path="roleDesc" id="roleDesc"
									cssClass="form-control"
									onblur="roleDescValidation('roleDesc', 'roleDescMsg')" />
							</div>
							<div id="roleDescMsg" style="color: red;"></div>
						</td>
					</tr>
					<tr>
						<td>Transaction Password :</td>
						<td><form:hidden path="tranPassword" id="tranPassword" /> <input
							type="radio" name="transPwd" id="transPwd1" value="true"
							checked="checked">Required &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="transPwd" id="transPwd2" value="false">
							Not Required
							<div id="transPwdMsg" style="color: red;"></div></td>
					</tr>
					<tr align="center">
						<td colspan="2"><input type="button" value="Submit"
							class="btn btn-primary btn-small" onclick="saveFunction()"></td>
					</tr>
				</table>
			</form:form>
			
			<table class="table table-bordered table-striped" style="width: 100%">
			<thead>
				<tr>
					<td>S.No.</td>
					<td>Role Name</td>
					<td>Role Description</td>
					<td>Transaction Password</td>
					<td>Created By</td>
					<td>Created On</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${roles}" var="r" varStatus="st">
					<tr>
						<td>${st.count}</td>
						<td>${r.role}</td>
						<td>${r.roleDesc}</td>
						<td><c:choose>
								<c:when test="${r.tranPassword}">
							Required       
						</c:when>
								<c:otherwise>
							Not Required
						</c:otherwise>
							</c:choose></td>
						<td>${r.createdBy}</td>
						<td><fmt:formatDate type="both" pattern="yyyy-MM-dd"
								value="${r.createdOn}" /></td>
					</tr>
				</c:forEach>
			</tbody>

		</table>
		</div>

		
	</div>
</body>
<script type="text/javascript">
	var roleFlag = false;
	var roleDesFlag = false;
	function roleValidation(id, messageId) {
		var value = $('#' + id).val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html("Required Field.");
			roleFlag = false;
		} else {
			$('#' + messageId).html("");
			roleFlag = true;
		}
		return roleFlag;
	}
	function roleDescValidation(id, messageId) {
		$('#tranPassword').val("");
		var value = $('#' + id).val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html("Required Field.");
			roleDesFlag = false;
		} else {
			$('#' + messageId).html("");
			roleDesFlag = true;
		}
		return roleDesFlag;
	}
	function transPwdValidation() {
		var flag = false;
		if ($("#transPwd1").prop("checked")) {
			$('#tranPassword').val(true);
			flag = true;
		} else {
			$('#tranPassword').val(false);
			flag = true;
		}
		return flag;
	}

	function saveFunction() {
		if (roleFlag == false)
			roleFlag = roleValidation('role', 'roleMsg')
		if (roleDesFlag == false)
			roleDesFlag = roleDescValidation('roleDesc', 'roleDescMsg');
		var trpwdFlag = transPwdValidation();
		//         		alert("Flags are : "+roleFlag +"::"+ roleDesFlag +"::"+ trpwdFlag);
		if (roleFlag && roleDesFlag && trpwdFlag)
			$('#saveRoleMaster').submit();

	}
</script>

</html>