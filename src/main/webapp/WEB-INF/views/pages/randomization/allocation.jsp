<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<body>
<div class="card">
	<div class="card-header">
       <h3 class="card-title">
       		Allocation
       </h3>
     </div>
     <div class="card-body">
     	<table id="example1" class="table table-bordered table-striped">
   			<thead>
	   			<tr>
	   				<th>Site ID</th>
	   				<th>Subject ID</th>
	   				<th>Visit Name</th>
<!-- 	   				<th>IP</th> -->
	   				<th>Status</th>
	   			</tr>
	   		</thead>
	   		<tbody>
	   			<c:forEach items="${ipRequests}" var="ip">
	   				<tr>
		   				<td>${ip.site.siteName}</td>
		   				<td>${ip.subject}</td>
		   				<td>${ip.period.name}</td>
	<%-- 	   				<td>${ip.testSeq}</td> --%>
		   				<td>${ip.status}</td>
		   				<td>
		   					<c:choose>
		   						<c:when test="${ip.status == 'REQUESTED'}">
		   							<input type="button" value="Allocate" onclick="save('${ip.id}', 
		   								'${ip.site.siteName}','${ip.subject}','${ip.period.name}','${ip.testSeq}')"
		   								class="btn btn-primary"/>
		   						</c:when>
		   						<c:otherwise>Change Status</c:otherwise>
		   					</c:choose>
		 				</td>
		 			</tr>
	   			</c:forEach>
	   		</tbody>
	   		<tfoot>
                <tr>
                	<th>Site ID</th>
					<th>Subject ID</th>
	   				<th>Visit Name</th>
<!-- 	   				<th>IP</th> -->
	   				<th>Status</th>
                </tr>
            </tfoot>
	   	</table> 
     </div>
</div>
	<div class="modal fade" id="signacherModal" tabindex="-1" role="dialog" aria-labelledby="discDataModal" aria-hidden="true" data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
	    	<div class="modal-content">
	      		<div class="modal-header">
	        		<h4 class="modal-title" id="myModalLabel">Electronic Signature</h4>
	        		<input type="hidden" id="signcheck"/>
	      		</div>
	      		<div class="modal-body">
	      			<table>
	      				<tr>
	      					<th>Site Name<br/></th>
	      					<td id="siteName"></td>
	      				</tr>
	      				<tr>
	      					<th>Subject<br/></th><td id="subject"></td>
	      				</tr>
	      				<tr>
	      					<th>Visit<br/></th><td id="visitName"></td>
	      				</tr>
	      				<tr>
	      					<th>IP<br/></th><td id="ip"></td>
	      				</tr>
	      				<tr>
	      					<th>User Name : <br/></th>
	      					<td>${userName} </td>
	      				</tr>
	      				<tr>
	      					<th>Transaction Password</th>
	      					<td><input type="password" id="tpw"/><font color="red" id="tpwmsg"></font></td>
	      				</tr>
	      				<tr>
	      				<tr><td/><td><input type="button" class="btn btn-primary" onclick="subjectPeriodCrfsDataReview()" value="Submit"/></td></tr>
	      			</table>
	        	</div>
	      		<div class="modal-footer">
	      			<div style="text-align: left;">
	      			<label class = "checkbox-inline"><input type="checkbox" value="true" id="acceptsignCheckId" />&nbsp;&nbsp;&nbsp;Do You want to close page.</label>
	      			</div>
	      			<div style="text-align: right;">
	        		<input type="button" class="btn btn-primary" onclick="acceptsignCheckBox()" value="Close"/>
	        		</div>
	        	</div>
	    	</div>
	  	</div>
	</div>
	<c:url value="/randomization/createIpAllocation" var="create_ipAllocationurl"/>
	<sf:form action="${create_ipAllocationurl}" method="post" id="create_ipAllocation" >
		<input type="hidden" name="ipId" id="ipId"/>
	</sf:form>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$('#signacherModal').modal('hide');
});
function signacherPopup(){
	$('#signacherModal').modal('show');
}
function acceptsignCheckBox() {
	if ($('#acceptsignCheckId').is(':checked')) {
		$('#signacherModal').modal('hide');
  	}else{
		aler("Please check checkbox...");
  	}
}
function save(id, siteName, subject, visitName,ip ) {
	$("#siteName").html(siteName);
	$("#subject").html(subject);
	$("#visitName").html(visitName);
	$("#ip").html(ip);
// 	$("#info").html("<th>Site Name</th><td>"+siteName+"</td>"+"<th>Subject</th><td>"+subject+"</td>"+
// 			"<th>Visit</th><td>"+visitName+"</td>"+"<th>IP</th><td>"+ip+"</td>");
	$("#ipId").val(id);
	
	$('#signacherModal').modal('show');
}

function subjectPeriodCrfsDataReview(){
	$("#tpwmsg").html("");
	var signcheck = asynchronousAjaxCall(mainUrl+"/reviewer/signcheck/"+$("#tpw").val());
	$("#signcheck").val(signcheck.trim());
	if($("#signcheck").val() == "yes"){
		$('#signacherModal').modal('hide');
		$("#create_ipAllocation").submit();
	}else{
		$("#tpw").val("");
		$("#tpwmsg").html("In-valid password");
	}
}

</script>
</html>