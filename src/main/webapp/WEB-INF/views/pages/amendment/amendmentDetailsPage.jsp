<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>AmandmentDetails</title>
<script type="text/javascript">
   var amdIdsArr = [];
   
   $(document).ready(function() {
	   $('#amdTable').DataTable( {
		   "bPaginate": false,
	      // "scrollY": 400,
	       "scrollX": true
	   } );
	} );
   
</script>

</head>
<body>
<div class="card">
    <div class="card-header">
      <h3 class="card-title" style="font-weight: bold; font-size: large;">Amendment Details</h3>
    </div>
    <div class="card-body">
       
	    	<table class="table table-bordered" id="amdTable" style="width:100%">
	    		<thead>
		    		<tr>
		    			<th>Action</th>
		    			<th>Observation Name</th>
		    			<th>StudyId</th>
		    			<th>Crested By</th>
		    			<th>Created On</th>
		    			<th>Status</th>
		    		</tr>
	    		</thead>
	    		<tbody>
	    		<c:forEach items="${amdList}" var="amd">
	    			<tr>
	    				<td>
	    					${amd.action}
	    					<script type="text/javascript">
	    						amdIdsArr.push('${amd.id}');
	    					</script>
	    				</td>
	    				<td>${amd.obvId.observationName}</td>
	    				<td>${amd.studyName}</td>
	    				<td>${amd.createdBy}</td>
	    				<td><fmt:formatDate value="${amd.createdOn}" pattern="yyyy-MM-dd"/> </td>
	    				<td>
	    					<select name="amdStatus_${amd.id}" id="amdStatus_${amd.id}" class="form-control">
	    						<option value="Open">Open</option>
	    						<option value="Close">Close</option>
	    						<option value="Approved">Approved</option>
	    						<option value="Not Approved">Not Approved</option>
	    						<option value="ON Hold">ON Hold</option>
	    					</select>
	    					<script type="text/javascript">
	    					  	$("#amdStatus_${amd.id}").val('${amd.status}');
	    					</script>
	    				</td>
	    			</tr>
	    		</c:forEach>
	    		</tbody>
	    		<c:if test="${not empty amdList}">
	    			<tr align="center">
	    				<td colspan="6"><input type="button" value="Submit" class="btn btn-primary btn-sm" onclick="submitFunction()"></td>
	    			</tr>
	    		</c:if>
	    	</table>
    </div>
    <c:url value="/amandment/saveAmandmentDetails" var="saveamnd"></c:url>
    <form:form action="${saveamnd}" method="POST" id="saveAmdForm">
    	<input type="hidden" name="amdDetails" id="amdDetails">
    </form:form>
</div>
<script type="text/javascript">
    	function submitFunction(){
    		$('#amdDetails').val("");
    		if(amdIdsArr.length > 0){
    			for(var i=0; i<amdIdsArr.length; i++){
    				var value = $('#amdStatus_'+amdIdsArr[i]).val();
    				if(value != null && value != "undefinded" && value != "" && value !="Open"){
    					var amdVal = $('#amdDetails').val();
    					if(amdVal == "")
    						$('#amdDetails').val(amdIdsArr[i]+"##"+value);
    					else $('#amdDetails').val(amdVal+","+amdIdsArr[i]+"##"+value);
    				}
    			}
    			var amdDetails = $('#amdDetails').val();
    			if(amdDetails != ""){
    				$('#saveAmdForm').submit();
    			}
    		}
    	}
    </script>
</body>
</html>