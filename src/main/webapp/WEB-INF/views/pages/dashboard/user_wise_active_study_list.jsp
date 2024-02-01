<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- 		<div class="card"> -->
            <div class="card-header">
              <h3 class="card-title">Change Study</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            <c:choose>
    		<c:when test="${userWiseStudies.size() > 0 }">
		   		<table id="example1" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
				   			<th>ID</th>
			   				<th>Study No</th>
			   				<th>Title</th>
			   				<th>Primary User</th>
			   				<th>Secondary User</th>
			   				<th>Study Status</th>
<!-- 			   				<th>Study Date</th> -->
			   				<th>Status</th>
			   			</tr>
			   		</thead>
			   		<tbody>
		   			<c:forEach items="${userWiseStudies}" var="study" varStatus="st">
			   			<tr>
			   				<td>${study.id}</td>
			   				<td>${study.studyNo}</td>
			   				<td>${study.studyDesc}</td>
			   				<td>${sdMap[study.sdUser]}</td>
	   						<td>${sdMap[study.asdUser]}</td>
			   				<td>${study.status.statusDesc}</td>
<%-- 			   				<td><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${study.startDate}" /></td> --%>
			   				<td>
			   					<c:choose>
			   						<c:when test="${study.id eq acstdId}">
			   							<a href="#" class="btn btn-primary btn-sm">Current Study</a>
			   						</c:when>
			   						<c:otherwise>
			   							<a href='<c:url value="/dashboard/changeStudy/${study.id}" />' class="btn btn-primary">Change</a>
			   						</c:otherwise>
			   					</c:choose>
			   				</td>
			   			</tr>
			   		</c:forEach>
			   		<tbody>
			   	</table>
		   	</c:when>
		   	<c:otherwise>
		   	<script type="text/javascript">
				$(document).ready(function(){
					$('#memberModal').modal('show');
				});
			</script>
		   		<!-- Modal -->
				<div class="modal fade" id="memberModal" tabindex="-1" role="dialog" aria-labelledby="memberModalLabel" aria-hidden="false" data-backdrop="static" data-keyboard="false">
				  <div class="modal-dialog">
				    <div class="modal-content">
				      <div class="modal-header">
				        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				        <h4 class="modal-title" id="memberModalLabel">No studies assigned...!</h4>
				      </div>
				      <div class="modal-body">
				        <p>Please contact your admin...<BR>
				        etc.....</p>
				
				        <p>Thanks for support...</p>
				      </div>
				      <div class="modal-footer" style="text-align: center;">
<%-- 				        <a href='<c:url value="/administration/createStudy"/>' class="btn btn-primary">Create Study</a> or --%>
				        <a href='<c:url value="/logout"/>' class="btn btn-primary">Sign Out</a>
				      </div>
				    </div>
				  </div>
				</div>
		   	</c:otherwise>
    	</c:choose>
            </div>
            <!-- /.card-body -->
<!--           </div> -->
          <!-- /.card -->







