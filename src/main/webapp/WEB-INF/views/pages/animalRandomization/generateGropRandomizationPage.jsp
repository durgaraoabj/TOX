<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Randomization</title>
</head>
<body>
<div class="card">
   <div class="card-header">
     <h3 class="card-title">Final Randomization</h3>
    </div>
   <!-- /.card-header -->
   <div class="card-body">
   		<div style="font-weight: bold; font-size: medium; text-align: center;">Study No : ${frmd.sm.studyNo}<br/> Randomization Sheet</div>
		        <div style="height:300px; overflow: auto">
		        	<table class="table table-bordered table-striped">
			   			<tr>
			   				<th>Animal Accession No</th>
			   				<th>Body Weight(g)</th>
			   				<th>G No</th>
			   				<th>Permanent No.</th>
			   				<th>Mean</th>
			   				<th>SD</th>
			   			 </tr>
			   			 <c:set value="${0}" var="idVal"></c:set>
			   			 <c:set value="${1}" var="count"></c:set>
			   			 <c:forEach items="${frmd.sgsgdMap}" var="sgMap">
   			    			<c:forEach items="${sgMap.value}" var="sgs">
   			    			   <tr>
				   			 	  <td>${sgs.tempAnimalId}</td>
				   			 	  <td>${sgs.weight}</td>
				   			 	  <td>${sgs.groupId.groupName}</td>
				   			 	  <td>${sgs.perminentNo}</td>
				   			 	  	<c:if test="${idVal ne sgs.groupId.id}">
				   			 	  		 <td rowspan="${sgMap.value.size()}">${frmd.meanMap[sgMap.key]}</td>
					 					 <td rowspan="${sgMap.value.size()}">${frmd.sdMap[sgMap.key]}</td>
					 					 <c:set value="${sgs.groupId.id}" var="idVal"></c:set>
				   			 	  	</c:if>
				   			 	</tr>
					    	</c:forEach>
		   	  		</c:forEach>
		   	  </table>
   		</div>
	<c:if test="${frmd.submitFlag eq true}">
		<div align="center"><input type="button" value="Submit" class="btn btn-primary btn-md" onclick="submitForm()"></div>
	</c:if>
   </div>
   <c:url value="/animalRadomization/savegenerateGropRandomizationData" var="saveGropRandomization"></c:url>
   <form:form action="${saveGropRandomization}" method="POST" id="gropForm">
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