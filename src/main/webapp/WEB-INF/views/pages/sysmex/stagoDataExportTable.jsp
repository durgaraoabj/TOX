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
<input type="hidden" value="${sampleType}" id="sampleType" name="sampleType">
	<table id="example1" class="table table-bordered table-striped">
		<thead>
			<tr>
				<th>All<input type="checkbox" name="allStagoAnimals"
					id="allStagoAnimals" onclick="selectAllStagoAnimals()" /> <input
					type="button" value="PDF View" onclick="stagoAnimalPdf()" /> <font
					color='red' id="stagopdfMsg"></font>
				</th>
				<th>Study</th>
				<c:if test="${sampleType ne 'Animal'}">
					<th>Lot No.</th>
				</c:if>
				<th>Group</th>
				<th>Animal No.</th>
				<c:forEach items="${testCodes}" var="tc">
					<th>${tc.testCode.testCode}(Sec)</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:set var="resultCount" value="1" scope="page" />
			<c:forEach items="${sysmexDataList}" var="row" varStatus="st">
				<tr>
					<th><input type="checkbox" name="stagoAnimals"
						id="stagoAnimals_${row.animal.id}" value="${row.animal.id}"
						onclick="selectStagoAnimals('stagoAnimals_${row.animal.id}')" /></th>
					<th>${row.study.studyNo}</th>

					<c:if test="${sampleType ne 'Animal'}">
						<th><c:if test="${row.lotNo ne 'null'}">${row.lotNo}</c:if></th>
					</c:if>
					<th>${row.groupName}</th>
					<th><a href="javascript:void(0);"
						onclick="diaplayStagoReport('${row.study.id}', '${row.animal.id}', 'PT')">
							${row.animalNo}</a></th>
					<c:forEach items="${testCodes}" var="tc">
						<c:choose>
							<c:when test="${tc.testCode.testCode eq 'PT'}">
								<c:choose>
									<c:when test="${row.multipleResult}">
										<th id="${resultCount+1}td"
											onclick="selectStagoResultPage('${row.id}','${row.animalNo}', '${row.study.id}', '${row.study.studyNo}', 'PT', '${resultCount+1}td', '${row.animalId}')">${row.ptResult}</th>
									</c:when>
									<c:otherwise>
										<th>${row.ptResult}</th>
									</c:otherwise>

								</c:choose>
							</c:when>
							<c:when test="${tc.testCode.testCode eq 'APTT'}">
								<c:choose>
									<c:when test="${row.multipleResult}">
										<th id="${resultCount+2}td"
											onclick="selectStagoResultPage('${row.id}','${row.animalNo}', '${row.study.id}', '${row.study.studyNo}', 'APTT', '${resultCount+2}td', '${row.animalId}')">${row.apttResult}</th>
									</c:when>
									<c:otherwise>
										<th>${row.apttResult}</th>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:when test="${tc.testCode.testCode eq 'Fibrinogen'}">
								<c:choose>
									<c:when test="${row.multipleResult}">
										<th id="${resultCount+3}td"
											onclick="selectStagoResultPage('${row.id}','${row.animalNo}', '${row.study.id}', '${row.study.studyNo}', 'Fibrinogen', '${resultCount+3}td', '${row.animalId}')">${row.fibrinogenResult}</th>
									</c:when>
									<c:otherwise>
										<th>${row.fibrinogenResult}</th>
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>
					</c:forEach>

					<%-- 					<c:if test="${sampleType ne 'Animal'}"><td>${row.lotNo}</td></c:if> --%>



					<c:set var="resultCount" value="${resultCount+3}" scope="page" />
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>



<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1" --%>
<%-- 	pageEncoding="ISO-8859-1"%> --%>
<%-- <%@ page isELIgnored="false"%> --%>
<%-- <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> --%>
<%-- <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> --%>
<%-- <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> --%>

<!-- <html> -->
<!-- <head> -->

<!-- </head> -->
<!-- <body> -->

<!-- 	<table id="example1" class="table table-bordered table-striped"> -->
<!-- 		<thead> -->
<!-- 			<tr> -->
<%-- 				<c:forEach items="${stagoTestCodesMap}" var="hedding1"> --%>
<%-- 					<th>${hedding1.value}</th> --%>
<%-- 				</c:forEach> --%>
<!-- 			</tr> -->
<!-- 			<tr> -->
<%-- 				<c:forEach items="${stagoTestCodesUnitsMap}" var="hedding1"> --%>
<%-- 					<th>${hedding1.value}</th> --%>
<%-- 				</c:forEach> --%>
<!-- 			</tr> -->
<!-- 		</thead> -->
<!-- 		<tbody> -->
<%-- 			<c:set var="resultCount" value="1" scope="page" /> --%>
<%-- 			<c:forEach items="${collectedData}" var="each"> --%>
<!-- 				<Tr> -->
<%-- 					<c:forEach items="${each.tcdataDispay}" var="tc"> --%>
<%-- 						<td id="${resultCount}td" --%>
<%-- 							onclick="selectStagoResultPage('${row.animalNo}', '${row.study.id}', '${row.study.studyNo}',  --%>
<%-- 							'PT', '${resultCount}td', '${row.animalId}')">${tc.value}</td> --%>
<%-- 					</c:forEach> --%>
<!-- 				</Tr> -->
<%-- 			</c:forEach> --%>
<!-- 		</tbody> -->
<!-- 	</table> -->
<!-- </body> -->
<!-- </html> -->
