<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Study Creation</title>
<script type="text/javascript">
	var insturmentcs = {};
	var instrumentNames = {};
	var instrumentProfile = {};
	var tcs = {}; //left side selection status
	var tcsRight = {}; // rigth side selectio status
	var tempTcs = []; // test code ids in temp array

	var tcName = {}; //test code key = id, value = names  
	var tcOrder = {}; //test code key = id, value = order
	var rigthTestCodesOrder = {}
	var onlyRigthElements = {};

	var rigthSideElements = {}
</script>
</head>
<body>
	<c:forEach items="${instrumentNames}" var="ins">
		<script type="text/javascript">
			instrumentNames['${ins.key}'] = '${ins.value}'
		</script>
	</c:forEach>
	<c:url value="/grouping/createStudy" var="savestudy"></c:url>
	<form:form action="${savestudy}" method="POST" id="savestudyform">
	<input type="hidden" name="groupRows" id="groupRows" value="0"/>
		<div class="box-body">
				<div id="profileIdsDiv"></div>
				<div id="addedTcIdsDiv"></div>
<!-- 				<input -->
<!-- 				type="text" name="addedTcIds" id="addedTcIds"> -->
			<table class="table table-bordered table-striped">
				<tr>
					<th>Study No :</th>
					<td><input type="text" name="studyNo" id="studyNo" value=""
						onblur="checkStudyNumber('studyNo', 'studyNoMsg', true)" />
						<div id="studyNoMsg" style="color: red;"></div></td>
					<th>Start Date :</th>
					<td><input type="text" class="form-control" name="startDate" id="startDate" autocomplete="off" readonly="readonly" onchange="checkStudyDate()"/>
					<script>
						$(function(){
							$("#startDate").datepicker({
								dateFormat:$("#dateFormatJsp").val(),
								changeMonth:true,
								 minDate: 0,
								changeYear:true
							});
						});
					</script>
					<font color="red" id="startDateMsg"></font>
				</td>
				</tr>
			</table>
			<table id="groupTable" class="table table-bordered table-striped">
				<thead>
					<tr>
						<th>Group</th>
						<th>Sex</th>
						<th>Animal No</th>
						<th>From</th>
						<th>To</th>
						<td><input type="button" value="Add" onclick="addRow()"></td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><input type="text" name="0_groupName" id="0_groupName" /><font
							color="red" id="0_groupNameMsg"></font></td>
						<td><select id="0_gender" name="0_gender">
								<option value="M" selected="selected">M</option>
								<option value="F">F</option>
						</select></td>
						<td id="0_prefixtd"></td>
						<td><input type="text" name="0_from" id="0_from" value="1"
							size="3" maxlength="3"
							onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44" /><font
							color="red" id="0_fromMsg"></font></td>
						<td><input type="text" name="0_to" id="0_to" size="3"
							maxlength="3"
							onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44" /><font
							color="red" id="0_toMsg"></font></td>
					</tr>
				</tbody>
			</table>


		</div>
		<c:forEach items="${instumentTestCodes}" var="instument">
			<script type="text/javascript">
				tempTcs = [];
			</script>
			<table class="table table-bordered table-striped">

				<tr>
					<th colspan="3">${instrumentNames[instument.key]}</th>
				</tr>
				<tr>
					<td align=left class="ui-widget-content"><script
							type="text/javascript">
						tempTcs = [];
					</script>
						<table id="${instrumentNames[instument.key]}_table">
							<thead>
								<tr>
									<th>Parameter</th>
									<th>Order</th>
								</tr>
							</thead>
							<tbody id="${instrumentNames[instument.key]}_leftTableBody">

								<c:forEach items="${instument.value}" var="tcv">
									<tr id="${tcv.id}DivLeft" onclick="testCodeSelect('${tcv.id}')">
										<td>${tcv.disPalyTestCode}</td>
										<td>${tcv.orderNo}</td>
									</tr>
									<script type="text/javascript">
										tcs['${tcv.id}'] = 0; //left side selection status
										tcsRight['${tcv.id}'] = 0; // rigth side selectio status
										tempTcs.push('${tcv.id}'); // test code ids in temp array

										tcName['${tcv.id}'] = '${tcv.disPalyTestCode}'; //test code key = id, value = names 
										tcOrder['${tcv.id}'] = '${tcv.orderNo}'; //test code key = id, value = order
										rigthTestCodesOrder['${tcv.id}'] = ""; //default riggth test code key = id, value = 0
									</script>
								</c:forEach>
							</tbody>
						</table> <script type="text/javascript">
							insturmentcs['${instrumentNames[instument.key]}'] = tempTcs;
							instrumentProfile['${instrumentNames[instument.key]}'] = -1;
							rigthSideElements['{instument.key}'] = {};
						</script></td>
					<td align="center">
						<table>
							<tr>
								<td><input type="button" id="btnAdd" value=">"
									onclick="moveTcsToRight('${instrumentNames[instument.key]}')"
									style="width: 50px;" /></td>
							</tr>
							<tr>
								<td><input type="button" id="btnAddAll"
									onclick="moveAllTcsToRight('${instrumentNames[instument.key]}')"
									value=">>" style="width: 50px;" /></td>
							</tr>
							<tr>
								<td><input type="button"
									onclick="moveTcsToLeft('${instrumentNames[instument.key]}')"
									id="btnRemove" value="<" style=" width: 50px;" /></td>
							</tr>
							<tr>
								<td><input type="button" id="btnRemoveAll"
									value="<<" style="
									width: 50px;" onclick="moveAllTcsToLeft('${instrumentNames[instument.key]}')" /></td>
							</tr>

						</table>
						<table>
							<tr>
								<td><c:if
										test="${profileMap[instrumentNames[instument.key]].size() gt 0}">
										<br />
										<b>Standard Parameter List</b>
										<br />
										<select
											onchange="profileSelection('${instrumentNames[instument.key]}', this.value)">
											<option value="-1">--Select--</option>
											<c:forEach
												items="${profileMap[instrumentNames[instument.key]]}"
												var="profile">
												<option value="${profile.id}">${profile.profileName}</option>
											</c:forEach>
										</select>
									</c:if></td>
							</tr>
						</table>

					</td>
					<td align="left" class="ui-widget-content">
						<div style="overflow-x: auto"
							id="${instrumentNames[instument.key]}_RightTable">
							<table id="${instrumentNames[instument.key]}_DivRightTable">
								<thead>
									<tr>
										<th>Parameter</th>
										<th>Old Order</th>
										<th>Order&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
									</tr>
								</thead>
								<tbody id="${instrumentNames[instument.key]}_RightTableBody">
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</table>
		</c:forEach>
		<table class="table table-bordered table-striped">
			<tr>
				<th colspan="5"><input type="button" value="Create Study"
					onclick="saveData()" /></th>
			</tr>
		</table>

	</form:form>

	<div class="modal fade" id="profiledataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Profile View</h4>
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
</body>
<script src='/TOX/static/js/grouping/createStudyAndGrouping.js'></script>
</html>