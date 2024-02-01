<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Acclimatization Page</title>
<!-- <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css"> -->
<!--   <script src="//code.jquery.com/jquery-1.12.4.js"></script> -->
<!--   <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script> -->
<script type="text/javascript">
	var cofigIdsArr = [];
	var idDates = {};
</script>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Acclimatization Configuration</b>
			</h3>
		</div>
		<div class="card-body">
			<div id="warnMsg"
				style="color: red; text-align: center; font-weight: bold; font-size: medium;"></div>
			<c:url value="/acclimatization/saveAcclimatizationData"
				var="saveAcclimatization"></c:url>
			<form:form action="${saveAcclimatization}" method="POST"
				id="acclimatizationForm">
				<div id="crfIdsDiv"></div>


				<table id="example1" class="table table-bordered table-striped">
					<thead>
						<tr>
							<th>Select</th>
							<th>Type</th>
							<th>Sub Type</th>
							<th>Prefix</th>
							<th>Observation</th>
							<th>Day/Week</th>
							<th>Days/Weeks Pre Dose</th>
							<th colspan="2">Window Period</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${crfList}" var="crf" varStatus="st">
							<tr>

								<c:choose>
									<c:when test="${sadMap[crf.id] != null}">
										<c:set value="${sadMap[crf.id]}" var="sad"></c:set>
										<td><c:choose>
												<c:when test="${sm.studyStatus eq 'In-Review' or roleName ne 'SD'}">
													<input type="checkbox" checked="checked"
														disabled="disabled">
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="crfCheckBox"
														id="crfCheckBox_${crf.id}" value="${crf.id}"
														onclick="checkBoxValidation('${crf.id}', '', 'Acclimatization', '')"
														checked="checked">
												</c:otherwise>
											</c:choose></td>
										<td>${crf.type}</td>
										<td>${crf.subType}</td>
										<td>${crf.prefix}</td>
										<td>${crf.observationName}</td>
										<td><c:choose>
												<c:when test="${sm.studyStatus eq 'In-Review' or roleName ne 'SD'}">
													<c:choose>
														<c:when test="${sad.type eq 'day'}">
															<input type="radio" value="day" checked="checked"
																disabled="disabled"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" value="week" disabled="disabled"> Week
    					    			 			</c:when>
														<c:otherwise>
															<input type="radio" value="day" disabled="disabled"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" value="week" disabled="disabled"
																checked="checked"> Week
    					    			 			</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<c:choose>
														<c:when test="${sad.type eq 'day'}">
															<input type="radio" name="dayOrWeekRadio_${crf.id}"
																id="dayRadio_${crf.id}" value="day"
																onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')"
																checked="checked"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" name="dayOrWeekRadio_${crf.id}"
																id="weekRadio_${crf.id}" value="week"
																onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')"> Week
    					    			 			</c:when>
														<c:otherwise>
															<input type="radio" name="dayOrWeekRadio_${crf.id}"
																id="weekRadio_${crf.id}" value="day"
																onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" name="dayOrWeekRadio_${crf.id}"
																id="weekRadio_${crf.id}" value="week"
																onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')"
																checked="checked"> Week
    					    			 			</c:otherwise>
													</c:choose>
												</c:otherwise>
											</c:choose>

											<div id="dayOrWeekRadio_${crf.id}_Msg" style="color: red;"></div>
										</td>
										<td>
											<input type="button" id="button_${crf.id}"
											value="Configure" class="btn btn-primary btn-sm"
											onclick="selectDatesAndCount('${crf.id}', 'study', 'Acclimatization', '', '')" />
											<div id="dayOrWeek_${crf.id}_Msg" style="color: red;"></div>
											</td>
										<td><c:choose>
												<c:when test="${sm.studyStatus eq 'In-Review' or roleName ne 'SD'}">
													<select name="sign_${crf.id}" id="sign_${crf.id}"
														onchange="signValidation('${crf.id}',  '', 'sign_${crf.id}_Msg', 'Acclimatization', '')"
														disabled="disabled" class="form-control"><option
															value="+/-" selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
														<option value="+">+</option>
														<option value="-">-</option></select>
													<script type="text/javascript">
														$("#sign_${crf.id}")
																.val(
																		'${sad.deviationSign}')
													</script>
													<div id="sign_${crf.id}_Msg" style="color: red;"></div>
												</c:when>
												<c:otherwise>
													<select name="sign_${crf.id}" id="sign_${crf.id}"
														onchange="signValidation('${crf.id}',  '', 'sign_${crf.id}_Msg', 'Acclimatization', '')"
														class="form-control"><option value="+/-"
															selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
														<option value="+">+</option>
														<option value="-">-</option></select>
													<script type="text/javascript">
														$("#sign_${crf.id}")
																.val(
																		'${sad.deviationSign}')
													</script>
													<div id="sign_${crf.id}_Msg" style="color: red;"></div>
												</c:otherwise>
											</c:choose></td>
										<td><c:choose>
												<c:when test="${sm.studyStatus eq 'In-Review' or roleName ne 'SD'}">
													<input type="text" size="2" class="form-control"
														disabled="disabled" value="${sad.deviation}" />
												</c:when>
												<c:otherwise>
													<input type="text" size="2" name="window_${crf.id}"
														id="window_${crf.id}"
														onkeypress="return event.charCode >= 48 && event.charCode <= 57"
														onchange="windowValidation('${crf.id}', '', 'window_${crf.id}_Msg', 'Acclimatization', '')"
														id="window_${crf.id}" class="form-control"
														value="${sad.deviation}" />
													<div id="window_${crf.id}_Msg" style="color: red;"></div>
												</c:otherwise>
											</c:choose></td>
									</c:when>
									<c:otherwise>
										<td><c:choose>
												<c:when test="${sm.studyStatus eq 'In-Review' or roleName ne 'SD'}">
													<input type="checkbox" disabled="disabled">
												</c:when>
												<c:otherwise>
													<input type="checkbox" name="crfCheckBox"
														id="crfCheckBox_${crf.id}" value="${crf.id}"
														onclick="checkBoxValidation('${crf.id}', '', 'Acclimatization', '')">
												</c:otherwise>
											</c:choose></td>
										<td>${crf.type}</td>
										<td>${crf.subType}</td>
										<td>${crf.prefix}</td>
										<td>${crf.observationName}</td>
										<td><input type="radio" name="dayOrWeekRadio_${crf.id}"
											disabled="disabled" id="dayRadio_${crf.id}" value="day"
											checked="checked"
											onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')">
											Day &nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
											disabled="disabled" name="dayOrWeekRadio_${crf.id}"
											id="weekRadio_${crf.id}" value="week"
											onchange="dayOrWeekValidation('${crf.id}', '', 'dayOrWeekRadio_${crf.id}_Msg', 'Acclimatization', '')">
											Week
											<div id="dayOrWeekRadio_${crf.id}_Msg" style="color: red;"></div></td>
										<td><input type="button" id="button_${crf.id}"
											value="Configure" class="btn btn-primary btn-sm"
											onclick="selectDatesAndCount('${crf.id}', 'lib', 'Acclimatization', '', '' )"
											disabled="disabled" />
											<div id="dayOrWeek_${crf.id}_Msg" style="color: red;"></div>
										</td>
										<td><select name="sign_${crf.id}" id="sign_${crf.id}"
											onchange="signValidation('${crf.id}',  '', 'sign_${crf.id}_Msg', 'Acclimatization', '')"
											disabled="disabled" class="form-control"><option
													value="+/-" selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
												<option value="+">+</option>
												<option value="-">-</option></select>
											<div id="sign_${crf.id}_Msg" style="color: red;"></div></td>
										<td><input type="text" size="2" name="window_${crf.id}"
											id="window_${crf.id}"
											onchange="windowValidation('${crf.id}', '', 'window_${crf.id}_Msg', 'Acclimatization', '')"
											disabled="disabled"
											onkeypress="return event.charCode >= 48 && event.charCode <= 57"
											id="window_${crf.id}" class="form-control" />
											<div id="window_${crf.id}_Msg" style="color: red;"></div></td>
									</c:otherwise>
								</c:choose>

							</tr>
						</c:forEach>
					</tbody>
				</table>

			</form:form>
		</div>
	</div>
</body>
</html>