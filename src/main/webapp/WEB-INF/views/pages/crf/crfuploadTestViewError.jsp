<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
	<h5>Crf Excel Errors</h5>
	Observation Name : ${crfviewTest.observationName }&nbsp;&nbsp;&nbsp;&nbsp;
    Observation Desc : ${crfviewTest.observationDesc }&nbsp;&nbsp;&nbsp;&nbsp;
    <br/>
</body>
	<div class="box-body">
		<table class="table table-condensed" style="width: 100%">
			<tr><th>CRF Sheet</th>
			<c:forEach items="${exclDataErrors.crfSheet}" var="crf">
				<tr>
					<td>${crf.lineNo }</td>
					<td>${crf.name }</td>
					<td>${crf.desc }</td>
					<td>${crf.type }</td>
					<td>${crf.gender }</td>	
					<td>${crf.version }</td>	
				</tr>
			</c:forEach>
		</table>
		<br/>
		<table class="table table-condensed" style="width: 100%">
			<tr><th>CRF Section Sheet</th>
			<c:forEach items="${exclDataErrors.crfSectinSheet}" var="crf">
				<tr>
					<td>${crf.lineNo }</td>
					<td>${crf.name }</td>
					<td>${crf.desc }</td>
					<td>${crf.hedding }</td>
					<td>${crf.subHedding }</td>	
					<td>${crf.maxRows }</td>	
					<td>${crf.maxColumns }</td>	
					<td>${crf.order }</td>	
					<td>${crf.gender }</td>	
					<td>${crf.role }</td>	
					<td>${crf.containsGroup }</td>	
				</tr>
			</c:forEach>
		</table>
		<br/>
		<table class="table table-condensed" style="width: 100%">
			<tr><th>CRF Group Sheet</th>
			<c:forEach items="${exclDataErrors.crfGroupSheet}" var="crf">
				<tr>
					<td>${crf.lineNo }</td>
					<td>${crf.name }</td>
					<td>${crf.desc }</td>
					<td>${crf.hedding }</td>
					<td>${crf.subHedding }</td>	
					<td>${crf.minRows }</td>	
					<td>${crf.maxRows }</td>	
					<td>${crf.maxColumns }</td>	
					<td>${crf.sectionName }</td>	
				</tr>
			</c:forEach>
		</table>
		<br/>
		<table class="table table-condensed" style="width: 100%">
			<tr><th>CRF Section Element Sheet</th>
			<c:forEach items="${exclDataErrors.crfSectinElementSheet}" var="crf">
				<tr>
					<td>${crf.lineNo }</td>
					<td>${crf.name }</td>
					<td>${crf.leftDesc }</td>
					<td>${crf.rightDesc }</td>
					<td>${crf.middeDesc }</td>	
					<td>${crf.totalDesc }</td>	
					<td>${crf.columnNo }</td>	
					<td>${crf.rowNo }</td>	
					<td>${crf.bottomDesc }</td>	
					<td>${crf.type }</td>	
					<td>${crf.responseType }</td>	
					<td>${crf.display }</td>	
					<td>${crf.values }</td>	
					<td>${crf.pattren }</td>	
					<td>${crf.required }</td>	
					<td>${crf.sectionName }</td>	
					<td>${crf.dataType }</td>	
				</tr>
			</c:forEach>
		</table>
		<br/>
		<table class="table table-condensed" style="width: 100%">
			<tr><th>CRF Group Element Sheet</th>
			<c:forEach items="${exclDataErrors.crfgroupElementSheet}" var="crf">
				<tr>
					<td>${crf.lineNo }</td>
					<td>${crf.name }</td>
					<td>${crf.desc }</td>
					<td>${crf.columnNo }</td>	
					<td>${crf.rowNo }</td>	
					<td>${crf.type }</td>	
					<td>${crf.responseType }</td>	
					<td>${crf.display }</td>	
					<td>${crf.values }</td>	
					<td>${crf.pattren }</td>	
					<td>${crf.required }</td>	
					<td>${crf.sectionName }</td>	
					<td>${crf.groupName }</td>
					<td>${crf.dataType }</td>	
				</tr>
			</c:forEach>
		</table>
	</div>
</html>