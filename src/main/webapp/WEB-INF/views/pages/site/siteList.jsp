<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" name="stdcrfConfiguation" id="stdcrfConfiguation" value="${configureStatus}"/>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Site Creation for the study : ${study.studyNo}</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            <c:choose>
            	<c:when test="${study.studyStatus eq 'Available'}">
            		<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'  and userRole ne 'USER'}">
	            		<a href='<c:url value="/site/createSite"/>' class="btn btn btn-primary"><strong>Create Site</strong></a>
	            	</c:if>
            	</c:when>
            	<c:otherwise>
            		<b><font color="blue">Study State : ${study.studyStatus}</font></b>
            	</c:otherwise>
            </c:choose>
           
		   		<table id="example1" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Site NO</th>
			   				<th>Site Name</th>
			   				<th>Protocol ID</th>
			   				<th>Start Date</th>
			   				<th>End Date</th>
			   				<th>Expected total enrollment</th>
			   				<th>Facility Name</th>
			   			</tr>
			   		</thead>
			   		<tbody>
 		   			<c:forEach items="${sites}" var="site">
			   			<tr>
			   				<td>${site.siteNo}</td>
			   				<td>${site.siteName}</td>
			   				<td>${site.protocalId}</td>
			   				<td>${site.startDate}</td>
			   				<td>${site.endDate}</td>
			   				<td>${site.subjects}</td>
			   				<td>${site.facilityName}</td>
			   			</tr>
			   		</c:forEach> 
			   		<tbody>
			   		<tfoot>
	                <tr>
	                	<th>Site NO</th>
						<th>Site Name</th>
		   				<th>Protocol ID</th>
		   				<th>Start Date</th>
		   				<th>End Date</th>
		   				<th>Expected total enrollment</th>
		   				<th>Facility Name</th>
	                </tr>
	                </tfoot>
		   		</table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->



<%-- 			<input type="hidden" name="crfIds" id="crfIds"/>
			<c:if test="${study.crfPeriodConfiguation eq true}">
				<div align="center"><input type="button" id="createVoluntees" value="Create Subjects" class="btn btn-warning btn-sm" style="width:200px;"></div>
			</c:if> --%>
    
    <c:url value="/study/configureCrfsMarkCompletePeriod" var="configureCrfsMarkComplet"/>
    <form:form action="${configureCrfsMarkComplet}" method="post" id="configureCrfsMarkComplet" >
<%-- 			<input type="hidden" name="periodId" id="periodId" value="${periodId}"/> --%>
    </form:form>
<script type="text/javascript">
$('#createVoluntees').click(function(){
	$('#submitform').submit();	
});

</script>
</html>