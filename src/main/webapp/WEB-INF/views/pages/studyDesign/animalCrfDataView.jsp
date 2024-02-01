<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<body>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Observation :</h3>
              <table class="table table-bordered table-striped">
				<tr><td>Study : ${data.study.studyNo }</td><td colspan="4">Observation Data Entry For : ${data.crfOrObservation.observationName}</td></tr>
<!-- 				<tr> -->
<%-- 					<td>Animal No : ${data.animalAll.animalNo}</td> --%>
<%-- 					<td>Group : ${data.group.groupName }</td> --%>
<%-- 					<td>Sub Group : ${data.subGroup.name}</td> --%>
<!-- 				</tr> -->
<!-- 				<tr> -->
<%-- 					<td>Dose : ${data.subGroup.dose }</td> --%>
<%-- 					<td>Gender : ${data.group.gender}</td> --%>
<%-- 					<td>Day/Week : ${data.crfOrObservation.dayType }</td> --%>
<!-- 				</tr> -->
			</table>
			
			
			
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            	<div style="width: 100%; overflow: scroll">
		        	<table class="table table-bordered table-striped">
						<tr>
							<th>Count</th>
							<th>Date Deviation</th>
							<th>Frequently Deviation</th>
							<th>Entered By</th>
							<th>Entered On</th>
							<th>Entry NO</th>
							
							<th>Animal No</th>
							<th>Group</th>
							<th>Sub Group</th>
							<th>Dose</th>
							<th>Gender</th>
							<th>${data.crfOrObservation.dayType }</th>
							<c:forEach items="${data.elements }" var="ele">
								<th>
									<c:choose>
										<c:when test="${ele.value.leftDesc ne null and ele.value.leftDesc ne ''}">
											${ele.value.leftDesc}
										</c:when>
										<c:when test="${ele.value.rigtDesc ne null and ele.value.rigtDesc ne ''}">
											${ele.value.rigtDesc}
										</c:when>
										<c:when test="${ele.value.topDesc ne null and ele.value.topDesc ne ''}">
											${ele.value.topDesc}
										</c:when>
										<c:when test="${ele.value.bottemDesc ne null and ele.value.bottemDesc ne ''}">
											${ele.value.bottemDesc}
										</c:when>
										<c:when test="${ele.value.middeDesc ne null and ele.value.middeDesc ne ''}">
											${ele.value.middeDesc}
										</c:when>
										<c:otherwise>
											${ele.value.totalDesc}
										</c:otherwise>
									</c:choose>
<%-- 								${ele.value.leftDesc } --%>
								</th>
							</c:forEach>
						</tr>
						<c:forEach items="${data.animalData }" var="ele" varStatus="st">
							<tr>
								<td>${st.count }</td>
								<td>${ele.deviationMessage}</td>
								<td>${ele.frequntlyMessage}</td>
								<td>${ele.userName }</td>
								<td>${ele.date }</td>
								<td>${ele.count }</td>
								<td>${data.animalAll.animalNo}</td>
								<td>${data.group.groupName }</td>
								<td>${data.subGroup.name}</td>
								<td>${data.subGroup.dose }</td>
								<td>${data.animal.gender}</td>
								<td>${ele.dayOrWeek }</td>			
								<c:forEach items="${ele.elementData }" var="eleData">
									<td>${eleData.value.value }</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
            	</div>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
	</body>
</html>

		
          
