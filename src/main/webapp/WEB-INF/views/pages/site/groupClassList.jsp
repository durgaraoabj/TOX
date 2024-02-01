<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" name="stdcrfConfiguation" id="stdcrfConfiguation" value="${configureStatus}"/>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Group Creation for the study : ${study.studyNo}</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            	<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">	
              		<a href='<c:url value="/site/createGroupClass"/>' class="btn btn btn-primary"><strong>Create Group Class</strong></a>
				</c:if>
		   		<table id="example1" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Group NO</th>
			   				<th>Group Name</th>
			   				<th>type</th>
			   				<th>Subject Assignment</th>
			   			</tr>
			   		</thead>
			   		<tbody>
 		   			<c:forEach items="${groups}" var="group">
			   			<tr>
			   				<td>${group.groupNo}</td>
			   				<td>${group.groupName}</td>
			   				<td>${group.type}</td>
			   				<td>${group.subjectAssignment}</td>
			   			</tr>
			   		</c:forEach> 
			   		<tbody>
			   		<tfoot>
	                <tr>
	                	<th>Group NO</th>
		   				<th>Group Name</th>
		   				<th>type</th>
		   				<th>Subject Assignment</th>
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