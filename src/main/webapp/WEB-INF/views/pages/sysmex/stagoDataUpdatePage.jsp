<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
</head>
<body>
	<table class="table table-bordered table-striped">
		<tr>
			<th>Study :</th>
			<th>${study.studyNo}</th>
		</tr>
		<tr>
			<th>Animal No :</th>
			<th>${animal.permanentNo}</th>
		</tr>
		<tr>
			<th>Select Result :</th>
			<td>
				<table>
					<tr><th>Value</th><td>Date</td></tr>

				<c:forEach items="${eachStagoData}" var="ele">
					<tr>
					<td>
					<c:choose>
						<c:when test="${ele.selectedResult}">
							<input type="radio" name="finalResult" value="${ele.id}"
								checked="checked"> ${ele.testResult}<br />
						</c:when>
						<c:otherwise>
							<input type="radio" name="finalResult" value="${ele.id}"> ${ele.testResult}<br />
						</c:otherwise>
					</c:choose>
					</td>
					<td><fmt:formatDate type="both" pattern="yyyy-MM-dd"
								value="${ele.receivedTime}" /> </td>
					</tr>
				</c:forEach>				</table></td>
		</tr>
		<tr>
			<td>Comment:</td>
			<td><input id="rerunCommnet" name="rerunCommnet" /><font
				color="red" id="rerunCommnetMsg"></font></td>
		</Tr>
		<tr>
			<td colspan="2"><input type="button" value="Save"
				onclick="stagoResultSelectionSave('${study.id}', '${animal.id}', '${testName}', '${rowId}', '${animalId}', '${tdId}', '${sampleType}')" /></td>
		<tr>
	</table>
</body>
</html>