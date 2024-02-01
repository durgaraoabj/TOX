<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" name="stdcrfConfiguation" id="stdcrfConfiguation" value="${configureStatus}"/>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Subject Creation for the study : ${study.studyNo}</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <c:url value="/study/volunteerCreationSave" var="vollform"/>
		    <form:form action="${vollform}" method="post" id="submitform" >
		    	<table id="example1" class="table table-bordered table-striped">
		    		<tr>
						<th>
		<%-- 					<c:if test="${crfConfiguation eq false}"> --%>
		<!-- 						<div align="center"><input type="button" id="selectCrfscomplete"  -->
		<!-- 						value="Mark As Complete" class="btn btn-warning btn-sm" style="width:100px;"></div> -->
		<%-- 					</c:if> --%>
							Bed No:
						</th>
						<th>Subject Id</th>
						<th>Subject Name</th>
					</tr>
					 <c:forEach begin="1" step="1" end="${study.subjects }" var="key">
					 	<tr>
					 		<td><input type="text" size="3" name="bedNo" id="bedNo_${key}" value="${key}" readonly="readonly"/></td>
					 		<td><input type="text" size="6" name="volId" id="volId_${key}" value="V-${key}"/></td>
					 		<td><input type="text" size="20" name="volName" id="volName_${key}" value="V-${key}"/></td>
					 	</tr>
					 </c:forEach>
				</table>
		
					<input type="hidden" name="crfIds" id="crfIds"/>
					<c:if test="${study.crfPeriodConfiguation eq true}">
						<div align="center"><input type="button" id="createVoluntees" value="Create Subjects" class="btn btn-primary" style="width:200px;"></div>
					</c:if>
		    </form:form>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
	<c:url value="/study/configureCrfsMarkCompletePeriod" var="configureCrfsMarkComplet"/>
    <form:form action="${configureCrfsMarkComplet}" method="post" id="configureCrfsMarkComplet" >
<%-- 			<input type="hidden" name="periodId" id="periodId" value="${periodId}"/> --%>
    </form:form>

<script type="text/javascript">
$('#createVoluntees').click(function(){
	$('#submitform').submit();
});

</script>
