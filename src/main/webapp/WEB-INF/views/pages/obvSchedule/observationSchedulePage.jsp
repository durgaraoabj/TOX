<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Observation Design Schedule</title>
</head>
<body>
<div class="card">
			<div class="card-header">
              <h3 class="card-title">Observation Design Schedule List</h3>
            </div>
    <div class="box-body">
		<table class="table table-bordered table-striped" id="obsvDesignTab" style="width: 100%;">
			<thead>
				<tr>
				   <th>Observation</th>
				   <th>Group</th>
				   <th>Sub Group</th>  
				   <th>Schedule Dates</th>
				   <th>
				   <table class="table table-bordered table-striped">
				   		<th>Animals</th>
				   		<th>Actual Dates</th>
				   </table>
				   </th>
	<!-- 			   <th></th> -->
				</tr>
			</thead>
			<c:set value="${0}" var="count"></c:set>
			<c:forEach items="${osdto.obsvMap}" var="obv">
				<tbody>
					<tr>
			   			<td>${obv.value.observationName}</td>
					    <td>${obv.value.groupName}</td>
						<td>${obv.value.subGroupInfo.name}</td>
						<td>
							<table class="table table-bordered table-striped">
								<c:forEach items="${obv.value.convDates}" var="days">
									<tr>
										<td>${days}</td>
									</tr>
								</c:forEach>
							</table>
						</td>
					<td>
						<table class="table table-bordered table-striped">
							<c:forEach items="${osdto.animalMap[obv.value.id]}" var="animal" varStatus="st">
								<tr>
									<td>${animal.animalNo}</td>
									<td>
										<table class="table table-bordered table-striped">
											<c:forEach items="${osdto.map2[animal.id]}" var="actual" varStatus="st">
												<c:choose>
													<c:when test="${obv.value.crf.id eq  actual.crf.crf.id}">
														<tr>
															<td>${actual.entryDate}</td>
														</tr>
													</c:when>
												</c:choose>
											</c:forEach>
										</table>
									</td>
								</tr>
							</c:forEach>
						</table> 
					</td>
	<!-- 				<td></td> -->
				 </tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</div>
	<script type="text/javascript">
		$(function(){
			$("#obsvDesignTab").DataTable();
		});
	</script>
</body>
</html>