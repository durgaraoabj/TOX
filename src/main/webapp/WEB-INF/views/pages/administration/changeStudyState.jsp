<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
<div class="card">
	<div class="card-header">
              <h3 class="card-title">Change Study State</h3>
    </div>
    <div class="box-body">
    <c:url value="/administration/changeStudyStatus" var="changeStudyState"/>
    <form:form action="${changeStudyState}" method="post"  id="changeStudyState" >
    	<input type="hidden" name="studyId" value="${sm.id }"> 
    	<table class="table table-bordered table-striped">
    		<tr>
    			<td>Study Number</td>
    			<td>${sm.studyNo}</td>
    		</tr>
    		<tr>
    			<td>Current Status</td>
    			<td>${sm.status.statusDesc }</td>
    		</tr>
    		<tr>
    			<td>Select Status</td>
    			<c:choose>
    				<c:when test="${sm.status.statusDesc != null and sm.status.statusDesc ne 'Initiate' and sm.status.statusDesc ne ''}">
    					<td>
		    				<select id="newStatus" name="newStatus">
								<option value="Inprogress">In-Progress</option>
								<option value="Frozen">Frozen</option>
								<option value="Locked">Locked</option>
								<option value="Cancel">Cancel</option>
							</select>
		    			</td>
    				</c:when>
    				<c:otherwise>
    					<td>Study In Design Stage</td>
    				</c:otherwise>
    			</c:choose>
    			
    		</tr>
    		
    	</table>
    	<c:if test="${sm.status.statusDesc != null and sm.status.statusDesc ne 'Initiate' and sm.status.statusDesc ne ''}">
    		<div align="center"><input type="button" onclick="changeStudyStatus()" value="Change" class="btn btn btn-primary" style="width:200px;"></div>
    	</c:if>
    </form:form>
    </div>
   </div>
</body>
<script type="text/javascript">
   function changeStudyStatus(){
	   var con = confirm("Do You want to change Change Study Status");
	 	if(con){
			$("#changeStudyState").submit();
		}
   }
	

</script>
	

</html>