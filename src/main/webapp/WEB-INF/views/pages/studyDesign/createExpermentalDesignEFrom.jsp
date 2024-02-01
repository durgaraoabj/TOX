<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Treatment Page</title>
<script type="text/javascript">
	// 	var cofigIdsArr = [];
	// 	var idDates = {};
</script>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Treatment Observation Forms</b>
			</h3>

		</div>
		<div style="overflow: scroll;">
			<table class="table table-bordered table-striped" style="width: 100%"
				id="${sibGroupId}_table">
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
					<c:forEach items="${stdcrfs}" var="sad" varStatus="st">
						<tr>

							<c:choose>
								<c:when test="${sad.active}">
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<input type="checkbox" name="crfCheckBox"
													disabled="disabled"
													id="crfCheckBox_${sad.id}_${sibGroupId}" checked="checked">
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="crfCheckBox"
													id="crfCheckBox_${sad.id}_${sibGroupId}" value="${sad.id}"
													onclick="checkBoxValidation('${sad.crf.id}', '_${sibGroupId}', 'Treatment', '${sibGroupId}')"
													checked="checked">
											</c:otherwise>
										</c:choose></td>
									<td>${sad.crf.type}</td>
									<td>${sad.crf.subType}</td>
									<td>${sad.crf.prefix}</td>
									<td>${sad.crf.observationName}</td>
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<c:choose>
													<c:when test="${sad.dayType eq 'day'}">
														<input type="radio" disabled="disabled"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="dayRadio_${sad.crf.id}_${sibGroupId}" value="day"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
															checked="checked"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" disabled="disabled"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="week"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"> Week
    					    			 			</c:when>
													<c:otherwise>
														<input type="radio" disabled="disabled"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="day"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio" disabled="disabled"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="week"
															onchange="dayOrWeekValidation('${sad.crf.id}','_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
															checked="checked"> Week
    					    			 			</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${sad.dayType eq 'day'}">
														<input type="radio"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="dayRadio_${sad.crf.id}_${sibGroupId}" value="day"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
															checked="checked"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="week"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"> Week
    					    			 			</c:when>
													<c:otherwise>
														<input type="radio"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="day"
															onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"> Day
				    									&nbsp;&nbsp;&nbsp;&nbsp;
				    									<input type="radio"
															name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
															id="weekRadio_${sad.crf.id}_${sibGroupId}" value="week"
															onchange="dayOrWeekValidation('${sad.crf.id}','_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
															checked="checked"> Week
    					    			 			</c:otherwise>
												</c:choose>
												<div id="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg"
													style="color: red;"></div>
											</c:otherwise>
										</c:choose></td>
									<td><input type="button"
										id="button_${sad.crf.id}_${sibGroupId}" value="Configure"
										class="btn btn-primary btn-sm"
										onclick="selectDatesAndCount('${sad.crf.id}', 'study', 'Treatment', '_${sibGroupId}', '${sibGroupId}')" />
										<div id="dayOrWeek_${sad.crf.id}_${sibGroupId}_Msg"
											style="color: red;"></div></td>
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<select name="sign_${sad.crf.id}" disabled="disabled"
													id="sign_${sad.crf.id}_${sibGroupId}" class="form-control"><option
														value="+/-" selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
													<option value="+">+</option>
													<option value="-">-</option></select>
												<script type="text/javascript">
													$(
															"#sign_${sad.crf.id}_${sibGroupId}")
															.val(
																	'${sad.windowSign}')
												</script>
											</c:when>
											<c:otherwise>
												<select name="sign_${sad.crf.id}"
													id="sign_${sad.crf.id}_${sibGroupId}"
													onchange="signValidation('${sad.crf.id}', '_${sibGroupId}', 'sign_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
													class="form-control"><option value="+/-"
														selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
													<option value="+">+</option>
													<option value="-">-</option></select>
												<script type="text/javascript">
													$(
															"#sign_${sad.crf.id}_${sibGroupId}")
															.val(
																	'${sad.windowSign}')
												</script>
												<div id="sign_${sad.crf.id}_${sibGroupId}_Msg"
													style="color: red;"></div>
											</c:otherwise>
										</c:choose></td>
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<input type="text" size="2" disabled="disabled"
													name="window_${sad.crf.id}"
													id="window_${sad.crf.id}_${sibGroupId}"
													onkeypress="return event.charCode >= 48 && event.charCode <= 57"
													id="window_${sad.crf.id}_${sibGroupId}"
													class="form-control" value="${sad.window}" />
												<div id="window_${sad.crf.id}_${sibGroupId}_Msg"
													style="color: red;"></div>
											</c:when>
											<c:otherwise>
												<input type="text" size="2" name="window_${sad.crf.id}"
													id="window_${sad.crf.id}_${sibGroupId}"
													onkeypress="return event.charCode >= 48 && event.charCode <= 57"
													onchange="windowValidation('${sad.crf.id}', '_${sibGroupId}', 'window_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
													id="window_${sad.crf.id}_${sibGroupId}"
													class="form-control" value="${sad.window}" />
												<div id="window_${sad.crf.id}_${sibGroupId}_Msg"
													style="color: red;"></div>
											</c:otherwise>
										</c:choose></td>
								</c:when>
								<c:otherwise>
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<input type="checkbox" name="crfCheckBox"
													id="crfCheckBox_${sad.crf.id}_${sibGroupId}"
													disabled="disabled" value="${sad.crf.id}">
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="crfCheckBox"
													id="crfCheckBox_${sad.crf.id}_${sibGroupId}"
													value="${sad.crf.id}"
													onclick="checkBoxValidation('${sad.crf.id}', '_${sibGroupId}', 'Treatment', '${sibGroupId}')">
											</c:otherwise>
										</c:choose></td>
									<td>${sad.crf.type}</td>
									<td>${sad.crf.subType}</td>
									<td>${sad.crf.prefix}</td>
									<td>${sad.crf.observationName}</td>
									<td><c:choose>
											<c:when test="${sm.studyStatus eq 'In-Review'}">
												<input type="radio" disabled="disabled" checked="checked">
										Day &nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
													disabled="disabled" value="week">
										Week
											</c:when>
											<c:otherwise>
												<input type="radio"
													name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
													disabled="disabled"
													id="dayRadio_${sad.crf.id}_${sibGroupId}" value="day"
													checked="checked"
													onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')">
										Day &nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
													disabled="disabled"
													name="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}"
													id="weekRadio_${sad.crf.id}_${sibGroupId}" value="week"
													onchange="dayOrWeekValidation('${sad.crf.id}', '_${sibGroupId}', 'dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')">
										Week
										<div id="dayOrWeekRadio_${sad.crf.id}_${sibGroupId}_Msg"
													style="color: red;"></div>
											</c:otherwise>
										</c:choose></td>
									<td><input type="button"
										id="button_${sad.crf.id}_${sibGroupId}" value="Configure"
										class="btn btn-primary btn-sm"
										onclick="selectDatesAndCount('${sad.crf.id}', 'lob', 'Treatment', '_${sibGroupId}', '${sibGroupId}')"
										disabled="disabled" />
										<div id="dayOrWeek_${sad.crf.id}_${sibGroupId}_Msg"
											style="color: red;"></div></td>
									<td><select name="sign_${sad.crf.id}"
										id="sign_${sad.crf.id}_${sibGroupId}"
										onchange="signValidation('${sad.crf.id}', '_${sibGroupId}','sign_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
										disabled="disabled" class="form-control"><option
												value="+/-" selected="selected">+/-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>
											<option value="+">+</option>
											<option value="-">-</option></select>
										<div id="sign_${sad.crf.id}_${sibGroupId}_Msg"
											style="color: red;"></div></td>
									<td><input type="text" size="2"
										name="window_${sad.crf.id}"
										id="window_${sad.crf.id}_${sibGroupId}"
										onchange="windowValidation('${sad.crf.id}', '_${sibGroupId}', 'window_${sad.crf.id}_${sibGroupId}_Msg', 'Treatment', '${sibGroupId}')"
										disabled="disabled"
										onkeypress="return event.charCode >= 48 && event.charCode <= 57"
										id="window_${sad.crf.id}_${sibGroupId}" class="form-control" />
										<div id="window_${sad.crf.id}_${sibGroupId}_Msg"
											style="color: red;"></div></td>
								</c:otherwise>
							</c:choose>

						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>

	<!-- 	<script src='/TOX/static/js/study/studyDesign.js'></script> -->

</body>
</html>