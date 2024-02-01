<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Randomization</title>
<style type="text/css">
.column {
  float: left;
  width: 50%;
  padding: 10px;
/*   height: auto; /* Should be removed. Only for demonstration */ */
}
th {
  padding: 3px;
  position: sticky;
  top: -10;
  z-index: 1;
  width: 25vw;
  background: white;
  height: 30px;
}
</style>
</head>
<body>
<div class="card">
   <div class="card-header">
     <h3 class="card-title">Animal Randomization Details</h3>
    </div>
   <!-- /.card-header -->
   <div class="card-body">
   		<div class="row">
		  <div class="column">
		  		<div style="font-weight: bold; font-size: medium; text-align: center;">Study No : ${rdmDto.sm.studyNo}<br/> Randomization Sheet</div>
		        <div style="height:450px; overflow: auto">
		        	<table class="table table-bordered table-striped">
			   			<tr>
			   				<th>Animal Accession No</th>
			   				<th>Body Weight(g)</th>
			   			 </tr>
			   			 <c:forEach items="${rdmDto.actMap}" var="map">
			   			 	<tr>
			   			 	  <td>${map.key}</td>
			   			 	  <td>${map.value}</td>
			   			 	</tr>
			   			 </c:forEach>
			   		</table>
			   		<div style="width: 60%; margin-left: 20%;">
			   			<table class="table table-bordered table-striped">
			   				<tr>
			   					<td>Mean :</td><td>${rdmDto.mean}</td>
			   				</tr>
			   				<tr>
			   					<td>Mean + 20%</td><td>${rdmDto.plustwentyVal}</td>
			   				</tr>
			   				<tr>
			   					<td>Mean - 20%</td><td>${rdmDto.minusTewentyVal}</td>
			   				</tr>
			   				<tr>
			   					<td>Minimum</td><td>${rdmDto.minVal}</td>
			   				</tr>
			   				<tr>
			   					<td>Maximum</td><td>${rdmDto.maxVal}</td>
			   				</tr>
			   			</table>
		   			</div>
		        </div>
		  		
		  </div>
		  <div class="column">
		  		<div style="font-weight: bold; font-size: medium; text-align: center;">Study No : ${rdmDto.sm.studyNo}<br/> Randomization Sheet</div>
		        <div style="height:450px; overflow: auto">
		        	<table class="table table-bordered table-striped">
			   			<tr>
			   				<th>Animal Accession No</th>
			   				<th>Body Weight(g)</th>
			   				<th>G No</th>
			   			 </tr>
			   			 <c:choose>
			   			 	<c:when test="${rdmDto.result eq 'Done' }">
			   			 		<c:forEach items="${rdmDto.sgsgList}" var="sgs">
					   			 	<tr>
					   			 	  <td>${sgs.tempAnimalId}</td>
					   			 	  <td>${sgs.weight}</td>
					   			 	  <td>${sgs.groupId.groupName}</td>
					   			 	</tr>
					   			 </c:forEach>
			   			 	</c:when>
			   			 	<c:otherwise>
			   			 		<td colspan="3" style="color: red; font-size: medium; font-weight: bold;">${rdmDto.groupingMsg}</td>
			   			 	</c:otherwise>
			   			 </c:choose>
			   		</table>
		        </div>
		  </div>
		</div>
		<c:if test="${rdmDto.sgsgList.size() gt 0}">
			<div align="center"><input type="button" value="Submit" class="btn btn-primary btn-md" onclick="submitForm()"></div>
		</c:if>
   </div>
   <c:url value="/animalRadomization/generateGropRandomization" var="gropRandomization"></c:url>
   <form:form action="${gropRandomization}" method="GET" id="gropForm">
   	  <input type="hidden" name="studyId" id="studyId" value="${stuydId}">
   </form:form>
</div>
<script type="text/javascript">
function submitForm(){
	$('#gropForm').submit();
}
</script>
</body>
</html>