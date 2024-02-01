<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Study Creation</h3>
		</div>
		<div class="card-body">
			<c:url value="/administration/createStudy" var="studyCreateUrl" />
			<form:form action="${studyCreateUrl}" method="post"
				modelAttribute="studyMaster" id="studyCreateFormId">
				<input type="hidden" name="userVal" id="userVal">
				<table id="example" class="table table-bordered table-striped" style="width:80%">
					<tr>
						<td style="width:30%">Study Number </td>
						<td>
							<div >
								<form:hidden path="studyNo" id="studyNo" />
								<table>
									<tr>
										<td><input type="text" name="studyNo1" id="studyNo1" onblur="checkStudyNumberSplit()" value="VLL"
											class="form-control" /></td>
										<td>/</td>
										<td colspan="2"><input type="text" name="studyNo2" id="studyNo2"
											  readonly="readonly" class="form-control" /></td>
										<td>/</td>
										<td>
											<select name="studyNo3" id="studyNo3" onblur="checkStudyNumberSplit()" class="form-control" style="width: 95px">
												<option value="-1">Select</option>
												<option value="G">&nbsp;&nbsp;&nbsp;G</option>
												<option value="NG">&nbsp;&nbsp;NG</option>
											</select>
										</td>
										<td>/</td>
										<td colspan="2"><input type="text" name="studyNo4" id="studyNo4" onblur="checkStudyNumberSplit()" 
											class="form-control" /></td>
									</tr>
								</table>

								<%--     					<form:input path="studyNo" type="text"  class="form-control input-sm" onblur="checkStudyNumber('studyNo', 'studyNoMsg', true)" id="studyNo"/> --%>
							</div>
							<div id="studyNoMsg" style="color: red;"></div>
						</td>
					</tr>
					<tr>
						<td style="width:30%">Title </td>
						<td>
							<div>
								<form:textarea path="studyDesc" rows="2"
									class="form-control input-sm" id="studyDesc"
									onblur="studyDescValidation('studyDesc', 'studyDescMsg')" />
							</div>
							<div id="studyDescMsg" style="color: red;"></div>
						</td>
					<tr>
					<tr>

						<td>Test Item Code</td>
						<td>
							<div >
								<form:input path="testItemCode" type="text"
									class="form-control input-sm"
									onblur="testItemCodeValidation('testItemCodeId', 'testItemCodeIdMsg')"
									id="testItemCodeId" />
							</div>
							<div id="testItemCodeIdMsg" style="color: red;"></div>
						</td>
					</tr>
					 <tr>

						<td style="width:30%">Sponsor Master </td>
						<td>
							<div >
								<form:select path="sponsorMasterId.id" id="sponsorMasterId"
									onchange="sponsorMasterIdValidation('sponsorMasterId', 'sponsorMasterIdMsg')"
									class="form-control">
									<form:option value="-1">----Select----</form:option>
									<c:forEach items="${sponsorList}" var="sponsor">
										<form:option value="${sponsor.id}">${sponsor.code}</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div id="sponsorMasterIdMsg" style="color: red;"></div>
						</td>
					</tr> 
					<tr>

						<td style="width:30%">GLP Or Non-GLP </td>
						<td>
							<div>
							
						<label class = "radio-inline"><form:radiobutton path="glpNonGlp" id="glpNonGlpId1" onblur="glpNonGlpValidation()" value = "GLP" />&nbsp;&nbsp;&nbsp;GLP</label>
						<label class = "radio-inline"><form:radiobutton path="glpNonGlp" id="glpNonGlpId2" onblur="glpNonGlpValidation()" value="Non-GLP" />&nbsp;&nbsp;&nbsp;Non-GLP</label>
							<div id="glpNonGlpIdMsg" style="color: red;"></div>
							</div>
						</td>
					</tr> 
					<tr>
					<td style="width:30%">Director User </td>
					<td>
						<div >
							<form:select path="sdUser" id="userId"
								onchange="userIdValidation('userId', 'userIdMsg')"
								class="form-control">
								<form:option value="">----Select----</form:option>
								<c:forEach items="${usersList}" var="sdu">
									<form:option value="${sdu.id}">${sdu.username}</form:option>
								</c:forEach>
							</form:select>
						</div>
						<div id="userIdMsg" style="color: red;"></div>
					</td>
					</tr>
					<%-- <tr>
						<td>Secondary User :</td>
						<td>
							<div style="width: 40%;">
								<form:select path="asdUser" id="asduserId"
									onchange="asduserIdValidation('asduserId', 'asduserIdMsg')"
									class="form-control">
									<form:option value="">----Select----</form:option>
									<c:forEach items="${usersList}" var="sdu">
										<form:option value="${sdu.id}">${sdu.username}</form:option>
									</c:forEach>
								</form:select>
							</div>
							<div id="asduserIdMsg" style="color: red;"></div>
						</td>
					</tr> --%>
				</table>

				<div align="center">
					<input type="button" id="studyCreateFormSubmitBtn"
						onclick="studyCreateFormSubmitBtnbut()" value="Create Study"
						class="btn btn-primary" style="width: 200px;">
				</div>
			</form:form>
			</div></div>
			<c:if test="${smMap.size() > 0 }">
		<div class="card"><div class="card-header"><h3 class="card-title">UserWise Assigned Studies List</h3></div><div class="card-body">
			
				<!-- <div
					style="color: blue; font-size: medium; text-align: center; font-weight: bold;"></div> -->
				<div style="height: 550px; overflow: auto">
					<table id="example1" class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>User Name</th>
								<th colspan="4" style="text-align: center;">Studies</th>
							</tr>
						</thead>
						<c:forEach items="${smMap}" var="sm" varStatus="st">
						<tbody>
							<tr>
								<td>${sm.key}</td>
								<td>
									<div style="height: 150px; overflow: auto">
										<table  class="table table-bordered table-striped" style="width:100%">
											<thead style="background-color: #3C8DBC;">
												<tr>
													<th>Study Number</th>
													<th>Title</th>
													<th>User Type</th>
													<th>Start Date</th>
												</tr>
											</thead>
											<c:forEach items="${sm.value}" var="std">
												<tr>
													<td>${std.studyNo}</td>
													<td>${std.studyDesc}</td>
													<c:choose>
														<c:when test="${std.userId eq std.sdUser}">
															<td>Primary</td>
														</c:when>
														<c:otherwise>
															<td>Secondary</td>
														</c:otherwise>
													</c:choose>
													<td><fmt:formatDate value="${std.startDate}"
															pattern="yyyy-MM-dd" /></td>
												</tr>
											</c:forEach>
										</table>
									
									</div>
								</td>
							</tr>
							</tbody>
						</c:forEach>
					</table>
				</div>
				</div></div>
			</c:if>
		
	
</body>
<script type="text/javascript">


	$(document).ready(function() {
// 		debugger;
		const today = new Date();
		const month = ('0' + (today.getMonth() + 1)).slice(-2); // pad with leading zero if necessary
		const year = today.getFullYear().toString().slice(-2); // get last 2 digits
		$("#studyNo2").val(month + "" + year);
	});
	debugger;
	console.log( $("#mainUrl").val());
	var stdNoFlag = false;
	var stdDescFlag = false;
	var userFlag = false;
	var asdUserFlag = false;
	var sponsor = false;
	var speciesFlag = false;
	function checkStudyNumber(id, messageId, backed) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
// 		debugger;
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			stdNoFlag = false;
		} else {

			if (backed) {
				var studyCreationDto = [];
				$
						.ajax({
							url : $("#mainUrl").val()
									+ '/administration/getCsrfToken',
							type : 'GET',
							success : function(data) {
								studyCreationDto.push({
									name : data.parameterName,
									value : data.token
								});
								studyCreationDto.push({
									name : "studyId",
									value : value
								});
								studyCreationDto.push({
									name : "id",
									value : 0
								});
								$
										.ajax({
											url : $("#mainUrl").val()
													+ '/administration/checkStudyNumber',
											type : 'POST',
											data : studyCreationDto,
											success : function(e) {
												console.log(e.success)
												if (e.Success === 'true'
														|| e.Success === true) {
													$('#' + messageId)
															.html(
																	value
																			+ " aleredy avilable.");
													stdNoFlag = false;
													$('#' + id).val("");
												} else {
													$('#' + messageId).html('');
													stdNoFlag = true;
												}
											},
											error : function(er) {
// 												debugger;
											}
										});
							},
							error : function(er) {
// 								debugger;
							}
						});

			}

		}
		return stdNoFlag;
	}
	function studyDescValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			stdDescFlag = false;
		} else {
			$('#' + messageId).html('');
			stdDescFlag = true;
		}
		return stdDescFlag;
	}
	function speciesValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		if (value == null || value == "-1" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			speciesFlag = false;
		} else {
			$('#' + messageId).html('');
			speciesFlag = true;
		}
		return stdDescFlag;
	}
	function userIdValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		var value2 = $('#asduserId').val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			userFlag = false;
		} else {
			if (value2 == null || value2 == "" || value2 == "undefined") {
				$('#' + messageId).html('');
				$('#asduserIdMsg').html('');
				userFlag = true;
			} else {
				if (value != value2) {
					$('#' + messageId).html('');
					$('#asduserIdMsg').html('');
					userFlag = true;
				} else {
					$('#' + messageId)
							.html(
									'Secondary User And Alternate User Both Are Not Same.');
					userFlag = false;
				}
			}

		}
		return userFlag;
	}
	function asduserIdValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		var value2 = $('#userId').val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			asdUserFlag = false;
		} else {
			if (value2 == null || value2 == "" || value2 == "undefined") {
				$('#' + messageId).html('');
				$('#userIdMsg').html('');
				asdUserFlag = true;
			} else {
				if (value != value2) {
					$('#' + messageId).html('');
					$('#userIdMsg').html('');
					asdUserFlag = true;
				} else {
					$('#' + messageId)
							.html(
									'Primary User And Secondary User Both Are Not Same.');
					asdUserFlag = false;
				}
			}
		}
		return asdUserFlag;
	}
	function checksponsorname(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		if (value == null || value == "-1" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			stdDescFlag = false;
		} else {
			$('#' + messageId).html('');
			stdDescFlag = true;
		}
		return stdDescFlag;
	}
	function testItemCodeValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		if (value == null || value == "" || value == "undefined") {
			$('#' + messageId).html('Required Field.');
			stdDescFlag = false;
		} else {
			$('#' + messageId).html('');
			stdDescFlag = true;
		}
		return stdDescFlag;
	}
	function sponsorMasterIdValidation(id, messageId) {
		$('#' + messageId).html('');
		var value = $('#' + id).val();
		if (value == null || value == "" || value == "undefined" || value == "-1") {
			$('#' + messageId).html('Required Field.');
			stdDescFlag = false;
		} else {
			$('#' + messageId).html('');
			stdDescFlag = true;
		}
		return stdDescFlag;
	}
	function glpNonGlpValidation() {
		var flag=false
		$('#glpNonGlpIdMsg').html('');
		if($('#glpNonGlpId1').is(':checked') || $('#glpNonGlpId2').is(':checked') ) {
			$('#glpNonGlpIdMsg').html('');
			flag = true;
		} else {
			
			$('#glpNonGlpIdMsg').html('Required Field.');
			flag = false;
		}
		return flag;
	}
	
	
	
	function studyCreateFormSubmitBtnbut() {
// 		debugger;
		if (stdNoFlag == false)
			stdNoFlag = checkStudyNumber('studyNo', 'studyNoMsg', false);
		if (stdDescFlag == false)
			stdDescFlag = studyDescValidation('studyDesc', 'studyDescMsg');
		if (asdUserFlag == false)
			//asdUserFlag = asduserIdValidation('asduserId', 'asduserIdMsg'); Option This Method
			asdUserFlag=true;
		if (speciesFlag == false)
			speciesFlag = speciesValidation('asduserId', 'asduserIdMsg');
		
		
		if (userFlag == false)
			userFlag = userIdValidation('userId', 'userIdMsg');
		if (sponsor == false)
			sponsor = checksponsorname('sponsornameId', 'sponsornameIdMsg');
		// 		alert(stdNoFlag && stdDescFlag && userFlag && asdUserFlag)
		var testcodev=testItemCodeValidation('testItemCodeId','testItemCodeIdMsg');
		var spons=sponsorMasterIdValidation('sponsorMasterId','sponsorMasterIdMsg');
		var glpval=glpNonGlpValidation();
// 		debugger;
		if (stdNoFlag && stdDescFlag && userFlag && asdUserFlag && speciesFlag && testcodev && spons && glpval)
			$('#studyCreateFormId').submit();
	}
	function checkStudyNumberSplit(){
		$("#studyNo").val($("#studyNo1").val()+"/"+$("#studyNo2").val()+"/"+$("#studyNo3").val()+"/"+$("#studyNo4").val());
		checkStudyNumber('studyNo', 'studyNoMsg', true)
	}
</script>

</html>