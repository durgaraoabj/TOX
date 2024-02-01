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

	<table id="example1" class="table table-bordered table-striped">
		<thead>
			<tr>
				<th>Study No</th>
				<c:if test="${vistrosDataDto.sampleType eq 'Animal' }">
					<th>Group Name</th>
				</c:if>
				<th>Animal No</th>
				<c:forEach items="${vistrosDataDto.heading}" var="row"
					varStatus="st">
					<th>${row.value.testCode.disPalyTestCode}</th>
				</c:forEach>
			</tr>
			<tr>
				<th></th>
				<c:if test="${vistrosDataDto.sampleType eq 'Animal' }">
					<th></th>
				</c:if>
				<th></th>
				<c:forEach items="${vistrosDataDto.heading}" var="row"
					varStatus="st">
					<th>${row.value.testCode.testCodeUints.displayUnit}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>

			<c:set var="resultCount" value="1" scope="page" />
<!-- 			private Map<String, SortedMap<Integer, List<VitrosTestCodeDataDto>>> animalTcDataListMap = new HashMap<>(); -->
			<c:forEach items="${vistrosDataDto.animalTcDataListMap}" var="row">
				<tr>
					<th>${vistrosDataDto.study.studyNo}</th>
					<c:if test="${vistrosDataDto.sampleType eq 'Animal' }">
						<th>${vistrosDataDto.studyAnimalsGroup[row.key]}</th>
					</c:if>
					<th>${row.key} <!-- 					<a href="javascript:void(0);" --> <%-- 						onclick="vistroDataExportPdfFunction('${vistrosDataDto.study.id}','${row.key}')">${row.key}</a> --%>
<%-- 					currentrow.value.size() : ${currentrow.value.size()} --%>
					</th>
					<%-- 					<th>${vistrosDataDto.animalTestDoneDate[row.key] }</th> --%>
					<!-- 					SortedMap<Integer, List<VitrosTestCodeDataDto>> -->
					<c:forEach items="${row.value}" var="currentrow" varStatus="st">
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${currentrow.value.size() > 1}"> --%>
								<th
									onclick="viewRepetedValuesUpdtePage('${row.key}', '${currentrow.key}', '${vistrosDataDto.study.id}', '${resultCount}_${currentrow.key}td')"
									id="${resultCount}_${currentrow.key}td">
<!-- 									List<VitrosTestCodeDataDto> -->
									<c:forEach
										items="${currentrow.value}" var="tc" >
										<c:forEach items="${tc.vitroData}" var="eachValue" varStatus="resultCount">
											<c:choose>
											<c:when test="${resultCount.index gt 0}">,
												<c:choose>
													<c:when test="${eachValue.finalize}">
														<font color="green">${eachValue.result}</font>
													</c:when>
													<c:otherwise>${eachValue.result}</c:otherwise>
												</c:choose>
											</c:when>
											<c:otherwise>
												<c:choose>
													<c:when test="${eachValue.finalize}">
														<font color="green">${eachValue.result}</font>
													</c:when>
													<c:otherwise>${eachValue.result}</c:otherwise>
												</c:choose>
											</c:otherwise>
											</c:choose>		
										</c:forEach>
										

									</c:forEach></th>
									<c:set var="resultCount" value="${resultCount+1}" scope="page" />
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<%-- 								<th><c:forEach --%>
<%-- 										items="${currentrow.value}" var="tc" varStatus="resultCount"> --%>
<%-- 										<font color="green">${tc.vitroData.result}</font> --%>
<%-- 									</c:forEach></th> --%>
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>


						
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
