<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Accession Data Entry Forms</title>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Animal Accession Data Entry Forms</h3>
		</div>
		<div class="card-body">
			<table id="example2" class="table table-bordered table-striped" style="width:60%">
				<thead>
					<tr>
						<th>Type</th>
							<th>Sub Type</th>
							<th>Prefix</th>
							<th>Observation</th>
						<th colspan="3">Operations</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${sadList}" var="sad" varStatus="st">
						<tr>
							<td>${sad.crf.type}</td>
							<td>${sad.crf.subType}</td>
							<td>${sad.crf.prefix}</td>
							<td>${sad.crf.observationName}</td>
<%-- 							<td><c:choose> --%>
<%-- 									<c:when test="${sad.allowDataEntry and hideElements}"> --%>
<%-- 										<c:choose> --%>
<%-- 											<c:when test="${hideElements}"> --%>
<!-- 												<input type="button" value="Scheduled Data Entry" -->
<%-- 													onclick="viewForm('${sad.crf.id}', 'scheduled')" --%>
<%-- 													id="button_${sad.id}" class="btn btn-primary btn-sm"> --%>
<%-- 											</c:when> --%>
<%-- 											<c:otherwise> --%>
<!-- 												<input type="button" value="Scheduled Data Entry" -->
<%-- 													onclick="viewForm('${sad.crf.id}', 'scheduled')" --%>
<%-- 													disabled="disabled" id="button_${sad.id}" --%>
<!-- 													class="btn btn-primary btn-sm"> -->
<%-- 											</c:otherwise> --%>
<%-- 										</c:choose> --%>
<%-- 									</c:when> --%>
<%-- 									<c:otherwise> --%>
<!-- 										<input type="button" value="Scheduled Data Entry" -->
<%-- 											onclick="viewForm('${sad.crf.id}', 'scheduled')" --%>
<%-- 											id="button_${sad.id}" class="btn btn-primary btn-sm" --%>
<!-- 											disabled="disabled"> -->
<%-- 									</c:otherwise> --%>
<%-- 								</c:choose></td> --%>
							<td><c:choose>
									<c:when test="${hideElements}">
										<input type="button" value="Un-Scheduled Data Entry"
											class="btn btn-primary btn-sm"
											onclick="viewForm('${sad.id}','${sad.crf.id}', 'unscheduled')">
									</c:when>
									<c:otherwise>
										<input type="button" value="Un-Scheduled Data Entry"
											class="btn btn-primary btn-sm" disabled="disabled"
											onclick="viewForm('${sad.id}','${sad.crf.id}', 'unscheduled')">
									</c:otherwise>
								</c:choose>
							<td><a
								href='<c:url value="/accession/viewFormData/${sad.id}"/>'><input
									type="button" value="View" class="btn btn-primary btn-sm"></a>
								<%-- 							<c:choose> --%> <%-- 									<c:when test="hideElements"> --%>
								<!-- 										<a --> <%-- 											href="<c:url value="/accession/viewFormData/${sad.crf.id}/${studyId}"/>"><input --%>
								<!-- 											type="button" value="View" class="btn btn-primary btn-sm"></a> -->
								<%-- 									</c:when> --%> <%-- 									<c:otherwise> --%> <!-- 										<a href="#"><input type="button" value="View" -->
								<!-- 											class="btn btn-primary btn-sm" disabled="disabled"></a> -->
								<%-- 									</c:otherwise> --%> <%-- 								</c:choose> --%>
							</td>
							<td>
								<a
								href='<c:url value="/accession/accessionDataExport/${sad.id}"/>'><input
									type="button" value="Export" class="btn btn-primary btn-sm"></a>
							</td>
						</tr>
					</c:forEach>
				</tbody>

			</table>
			</div>
		</div>
	</div>
	<c:url value="/accession/viewEntryForm" var="saveValues" />
	<form:form action="${saveValues}" method="post" id="valuesForm">
		<input type="hidden" name="studyAcclamatizationDataId" id="studyAcclamatizationDataId">
		<input type="hidden" name="crfId" id="crfId">
		<input type="hidden" name="type" id="type" value="unscheduled">
		<input type="hidden" name="seletedGender" id="seletedGender" value="${study.gender}">
		<input type="hidden" name="studyId" id="studyId" value="${studyId}">
		<input type="hidden" name="selecteDate" id="selecteDate" value="no">
		
	</form:form>
</body>
<script type="text/javascript">
	function viewForm(studyAcclamatizationDataId,crfId, type) {
		$("#studyAcclamatizationDataId").val(studyAcclamatizationDataId);
		$("#crfId").val(crfId);
		$("#type").val(type);
		$("#valuesForm").submit();
	}
</script>
</html>