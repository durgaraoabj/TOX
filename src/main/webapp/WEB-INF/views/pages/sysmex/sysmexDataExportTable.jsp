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
				<th>Study Number</th>
				<!-- 				<th>Group Name</th> -->
				<th>Animal Id</th>
				<!-- 				<th>Test Run Time</th> -->
				<c:forEach items="${sysmexDataDto.heading}" var="head">
					<th>${head.value.testCode.disPalyTestCode}</th>
				</c:forEach>
			</tr>
			<tr>
<!-- 				<th /> -->
				<!-- 				<th /> -->
				<th />
				<th />
				<c:forEach items="${sysmexDataDto.heading}" var="head">
					<th>${head.value.testCode.testCodeUints.displayUnit}</th>
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<c:set var="resultCount" value="1" scope="page" />
			<!-- 		SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataList -->
			<c:forEach items="${sysmexDataDto.sysmexDataListMap}" var="map"
				varStatus="st">
				<tr>
					<td>${sysmexDataDto.study.studyNo}</td>
					<%-- 					<td>${row.group.groupName}</td> --%>
					<td><a href="javascript:void(0);"
						onclick="diaplaySysmexReport('${sysmexDataDto.study.id}','${map.key}')">${map.key}</a>
					</td>
					<%-- 					<td>${row.runTime}</td> --%>

					<c:forEach items="${map.value}" var="data">

						<c:choose>

							<c:when test="${data.value.size() > 0}">
								<th
									onclick="viewSysmexRepetedValuesUpdtePage('${map.key}', '${data.key}', '${sysmexDataDto.study.id}', '${resultCount}_${data.key}td')"
									id="${resultCount}_${data.key}td"><c:forEach
										items="${data.value}" var="eachTest">
										<c:choose>
											<c:when test="${eachTest.finalValue}">
												<font color="green">${eachTest.value},</font>
											</c:when>
											<c:otherwise>
													${eachTest.value},	
												</c:otherwise>
										</c:choose>
									</c:forEach> <c:set var="resultCount" value="${resultCount+1}" scope="page" />
								</th>
							</c:when>
							<c:otherwise>
								<th><c:forEach items="${data.value}" var="eachTest">
										${eachTest.value}
									</c:forEach></th>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- 	<table id="example1" class="table table-bordered table-striped"> -->
	<!-- 		<thead> -->
	<!-- 			<tr> -->
	<!-- 				<th>Study Number</th> -->
	<!-- 				<th>Group Name</th> -->
	<!-- 				<th>Animal Id</th> -->
	<!-- 				<th>Test Run Time</th> -->
	<%-- 				<c:forEach items="${sysmexDataDto.heading}" var="head"> --%>
	<%-- 					<th>${head.value.testCode.testCode}</th> --%>
	<%-- 				</c:forEach> --%>
	<!-- 			</tr> -->
	<!-- 			<tr> -->
	<!-- 				<th /> -->
	<!-- 				<th /> -->
	<!-- 				<th /> -->
	<!-- 				<th /> -->
	<%-- 				<c:forEach items="${sysmexDataDto.heading}" var="head"> --%>
	<%-- 					<th>${head.value.testCode.testCodeUints.displayUnit}</th> --%>
	<%-- 				</c:forEach> --%>
	<!-- 			</tr> -->
	<!-- 		</thead> -->
	<!-- 		<tbody> -->
	<%-- 			<c:set var="resultCount" value="1" scope="page" /> --%>
	<!-- 			<!-- 		SortedMap<String, SortedMap<Integer, List<SysmexTestCodeData>>> sysmexDataList -->
	-->
	<%-- 			<c:forEach items="${sysmexDataDto.sysmexDataList}" var="row" --%>
	<%-- 				varStatus="st"> --%>
	<!-- 				<tr> -->
	<%-- 					<td>${sysmexDataDto.study.studyNo}</td> --%>
	<%-- 					<td>${sysmexDataDto.studyAnimalsGroup[row.key]}</td> --%>
	<!-- 					<td><a href="javascript:void(0);" -->
	<%-- 						onclick="diaplaySysmexReport('${sysmexDataDto.study.id}','${row.key}')">${row.key}</a> --%>
	<!-- 					</td> -->
	<%-- 					<td>${sysmexDataDto.animalTestDoneDate[row.key]}</td> --%>
	<!-- 										SortedMap<Integer, List<SysmexTestCodeData>> -->
	<%-- 					<c:forEach items="${row.value}" var="data"> --%>
	<!-- 												List<SysmexTestCodeData> -->

	<%-- 						<c:choose> --%>
	<%-- 							<c:when test="${data.value.size() > 1}"> --%>
	<!-- 								<th -->
	<%-- 									onclick="viewSysmexRepetedValuesUpdtePage('${row.key}', '${data.key}', '${sysmexDataDto.study.id}', '${resultCount}_${data.key}td')" --%>
	<%-- 									id="${resultCount}_${data.key}td"> --%>
	<%-- 							</c:when> --%>
	<%-- 							<c:otherwise> --%>
	<!-- 								<th> -->
	<%-- 							</c:otherwise> --%>
	<%-- 						</c:choose> --%>
	<!-- 												List<SysmexTestCodeData> -->
	<%-- 						<c:forEach items="${data.value}" var="testResult" --%>
	<%-- 							varStatus="resultCount"> --%>
	<%-- 							<c:choose> --%>
	<%-- 								<c:when test="${testResult.finalValue eq true}"> --%>
	<%-- 									<font color="green">${testResult.value}</font> --%>
	<%-- 								</c:when> --%>
	<%-- 								<c:otherwise>${testResult.value}</c:otherwise> --%>
	<%-- 							</c:choose> --%>
	<%-- 						</c:forEach> --%>
	<!-- 						</th> -->
	<%-- 						<c:set var="resultCount" value="${resultCount+1}" scope="page" /> --%>
	<%-- 					</c:forEach> --%>
	<!-- 											public SortedMap<String, SortedMap<Integer, SysmexTestCodeData>> getSysmexDataList() { -->
	<!-- 				</tr> -->
	<%-- 			</c:forEach> --%>
	<!-- 		</tbody> -->
	<!-- 	</table> -->
</body>
</html>
