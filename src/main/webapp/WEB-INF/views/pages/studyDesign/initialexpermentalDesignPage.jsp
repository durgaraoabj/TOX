<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Experimental Design</h3>
			<br />
			<h5 class="card-title">Study : ${study.studyNo }</h5>
		</div>
		<div class="box-body">
			<c:url value="/expermentalDesign/createNewExperimentalDesign"
				var="url" />
			<form:form action="${url}" method="post" id="form">
				<table class="table table-bordered table-striped">
					<tr>
						<th>Group</th>
<!-- 						<th>Dose(mg/kg b.w)</th> -->
<!-- 						<th>Conc. (mg/ml)</th>	 -->
						<th>No.Of Animals</th>
						<th>Sex</th>
					</tr>
					<c:forEach items="${group}" var="g">
						<c:choose>
							<c:when test="${study.requiredSubGroup}">
								<tr>
									<th>${g.groupName}</th>
								</tr>
								<c:forEach begin="1" step="1" end="${g.groupTest}" var="s">
									<tr>
										<td><textarea rows="2" cols="5" class="form-control"
												name="subGroupName${g.id}_${s}"
												id="subGroupName${g.id}_${s}"></textarea> <font color="red"
											id="subGroupName${g.id}_${s}msg"></font></td>
<!-- 										<td style="width: 10"><input type="text" -->
<%-- 											class="form-control" name="subGroupDose${g.id}_${s}" --%>
<%-- 											id="subGroupDose${g.id}_${s}" size="5" /> <font color="red" --%>
<%-- 											id="subGroupDose${g.id}_${s}msg"></font></td> --%>
<!-- 										<td style="width: 10"><input type="text" -->
<%-- 											name="subGroupConc${g.id}_${s}" id="subGroupConc${g.id}_${s}" --%>
<!-- 											size="5" class="form-control" /> <font color="red" -->
<%-- 											id="subGroupConc${g.id}_${s}msg"></font></td> --%>
										<c:choose>
											<c:when test="${g.gender == 'Male'}">

												<td><input type="text" name="subGroupCount${g.id}_${s}"
													id="subGroupCount${g.id}_${s}" size="3"  onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
													class="form-control" /> <font color="red"
													id="subGroupCount${g.id}_${s}msg"></font></td>
												<td><input type="radio"
													name="subGroupCount${g.id}_${s}" value="Male" checked />Male
													<font color="red" id="subGroupGender${g.id}_${s}msg"></font>
												</td>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${g.gender == 'Female'}">
														<td><input type="text"
															name="subGroupCount${g.id}_${s}" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
															id="subGroupCount${g.id}_${s}" size="3"
															class="form-control" /> <font color="red"
															id="subGroupCount${g.id}_${s}msg"></font></td>
														<td><input type="radio"
															name="subGroupGender${g.id}_${s}" value="Female" checked />Female
															<font color="red" id="subGroupGender${g.id}_${s}msg"></font>
														</td>
													</c:when>
													<c:otherwise>
														<td colspan="4">
															<table class="table table-bordered table-striped">
																<tr>
																	<td><input type="text"
																		name="subGroupCount${g.id}_${s}_Male" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
																		id="subGroupCount${g.id}_${s}_Male" size="3"
																		class="form-control" /> <font color="red"
																		id="subGroupCount${g.id}_${s}_Malemsg"></font></td>
																	<td><input type="radio"
																		name="subGroupGender${g.id}_${s}_Male" value="Male"
																		checked />Male<br /> <%-- 		    										<input type="radio" name="subGroupConc${g.id}_${s}" value="Female"/>Female --%>
																		<font color="red"
																		id="subGroupGender${g.id}_${s}_Malemsg"></font></td>
																</tr>
																<tr>
																	<td><input type="text"
																		name="subGroupCount${g.id}_${s}_Female" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
																		id="subGroupCount${g.id}_${s}_Female" size="3"
																		class="form-control" /> <font color="red"
																		id="subGroupCount${g.id}_${s}_Femalemsg"></font></td>
																	<td><input type="radio"
																		name="subGroupGender${g.id}_${s}_Female"
																		value="Female" checked />Female<br /> <%-- 		    										<input type="radio" name="subGroupConc${g.id}_${s}" value="Female"/>Female --%>
																		<font color="red"
																		id="subGroupGender${g.id}_${s}_Femalemsg"></font></td>
																</tr>
															</table>
														</td>
													</c:otherwise>
												</c:choose>
											</c:otherwise>


										</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<tr>
									<th>${g.groupName}</th>

<!-- 										<td style="width: 10"><input type="text" -->
<%-- 											class="form-control" name="subGroupDose${g.id}" --%>
<%-- 											id="subGroupDose${g.id}" size="5" /> <font color="red" --%>
<%-- 											id="subGroupDose${g.id}msg"></font></td> --%>
<!-- 										<td style="width: 10"><input type="text" -->
<%-- 											name="subGroupConc${g.id}" id="subGroupConc${g.id}" --%>
<!-- 											size="5" class="form-control" /> <font color="red" -->
<%-- 											id="subGroupConc${g.id}msg"></font></td> --%>
										<c:choose>
											<c:when test="${g.gender == 'Male'}">

												<td><input type="text" name="subGroupCount${g.id}"
													id="subGroupCount${g.id}" size="3" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
													class="form-control" /> <font color="red"
													id="subGroupCount${g.id}msg"></font></td>
												<td><input type="radio"
													name="subGroupCount${g.id}" value="Male" checked />Male
													<font color="red" id="subGroupGender${g.id}msg"></font>
												</td>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${g.gender == 'Female'}">
														<td><input type="text"
															name="subGroupCount${g.id}" onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
															id="subGroupCount${g.id}" size="3"
															class="form-control" /> <font color="red"
															id="subGroupCount${g.id}msg"></font></td>
														<td><input type="radio"
															name="subGroupGender${g.id}" value="Female" checked />Female
															<font color="red" id="subGroupGender${g.id}msg"></font>
														</td>
													</c:when>
													<c:otherwise>
														<td colspan="4">
															<table class="table table-bordered table-striped">
																<tr>
																	<td><input type="text"
																		name="subGroupCount${g.id}_Male"
																		id="subGroupCount${g.id}_Male" size="3"
																		class="form-control" /> <font color="red"
																		id="subGroupCount${g.id}_Malemsg"></font></td>
																	<td><input type="radio"
																		name="subGroupGender${g.id}_Male" value="Male"
																		checked />Male<br /> <%-- 		    										<input type="radio" name="subGroupConc${g.id}" value="Female"/>Female --%>
																		<font color="red"
																		id="subGroupGender${g.id}_Malemsg"></font></td>
																</tr>
																<tr>
																	<td><input type="text"
																		name="subGroupCount${g.id}_Female"
																		id="subGroupCount${g.id}_Female" size="3"
																		class="form-control" /> <font color="red"
																		id="subGroupCount${g.id}_Femalemsg"></font></td>
																	<td><input type="radio"
																		name="subGroupGender${g.id}_Female"
																		value="Female" checked />Female<br /> <%-- 		    										<input type="radio" name="subGroupConc${g.id}" value="Female"/>Female --%>
																		<font color="red"
																		id="subGroupGender${g.id}_Femalemsg"></font></td>
																</tr>
															</table>
														</td>
													</c:otherwise>
												</c:choose>
											</c:otherwise>


										</c:choose>
							</c:otherwise>
						</c:choose>

					</c:forEach>
					<tr align="center">
						<td colspan="7"><input type="button" id="submitBtn"
							onclick="submitBtnbut()" value="Save" class="btn btn-primary"
							style="width: 200px;"></td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
</body>
<script type="text/javascript">
	function submitBtnbut() {
		var flag = true;

		if (flag)
			$("#form").submit();
	}
</script>
</html>