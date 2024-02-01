<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
     <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Roles wise Menu Details</title>
<style type="text/css">
.insmod {
	display: none; 
	position: fixed;
	z-index: 1;
	padding-top: 100px; 
	left: 0;
	top: 0;
	width: 100%; 
	height: 100%;
	overflow: auto; 
	background-color: rgb(0, 0, 0); 
	background-color: rgba(0, 0, 0, 0.4); 
}
.my-custom-scrollbar {
position: relative;
height: 350px;
overflow: auto;
}
.table-wrapper-scroll-y {
display: block;
}
</style>
</head>
<body>
	<div class="card">
	 <input type="hidden" name="roleId" id="roleId" value="${roleId}">
		<c:url value="/roleMenu/saveSideMenuLinks" var="saveData"></c:url>
      <form:form action="${saveData}" method="POST">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-8">
			 <div class="card" style="margin-left: 50%;"> 
			 	<div class="table-wrapper-scroll-y my-custom-scrollbar">
					    <table border="2" style="width: 100%;" bordercolor="#EBBC9D" class="table table-borderd table-striped" id="linksList">
					    <tbody>
		             	<c:forEach items="${menusMap}" var="entry">
		      			     <div class="card-header" style="background: #EB9A2F;">
					                <h3 class="card-title" style="color:white;">${entry.key} 
					                 &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp; 
					                 &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;
					                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					                <a href='<c:url value="/roleMenu/printRolewiseConfiguredLinks/${roleId}/${entry.key}"/>' target="_blank"><input type="button" value="Print" class="btn btn-success btn-sm"></a>
					                </h3>
					          </div>
					           <div class="card-body p-0">
					              	 	<c:forEach items="${entry.value}" var="map">
					                		<tr style="background: #0C8179;">
					                			<td colspan="3" style="color: white; font-size: medium; font-weight: bold;" >${map.key}</td>
					                		</tr>
					                		<c:forEach items="${map.value}" var="list">
					                			<tr style="background: #FDEBD0;">
					                				<td></td>
					                				<td style="color: #494541; font-size: medium; font-weight: bold;">${list.appsideMenu.sideLink}</td>
			                					    <td>
			               	 					    	<input type="button" value="Remove" class="btn btn-danger btn-sm" onclick="submitInactiveLink('${list.appsideMenu.id}')">
			               	 					    </td>
					              	 			</tr>
					                		</c:forEach>
					                	</c:forEach>
					             </div>
		
				      		</c:forEach>
				      			<tr align="center"><td align="center" colspan="3"><input type="button" value="Add" onclick="showAllLinks('${roleId}')" class="btn btn-warning btn-sm"></td></tr>
				          
				      		</tbody>
				      	</table>  
				      	</div>   	 	
          			  </div>
			            <!-- /.card -->
			          </div>
			          
			        </div>
			        <!-- /.row -->
			      </div><!-- /.container-fluid -->
			     							
      </form:form>
      <!-- Links Pop Up -->
<!-- 	      		<div id="links" class="modal fade" tabindex="-3"  role="document"> -->
 					<div id="links" class="modal modal-fullscreen" id="modal-fullscreen-xs" tabindex="-1" role="dialog" aria-hidden="true">
			      
			            <div class="modal-content">
<!-- 						<div class="insmod"> -->
			                <div class="modal-header" style="background: #244e54; text-align: center;">
			                    <div align="center" style="color: white;font-weight: bold;">All Links</div>
			                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			                </div>
			                <div class="modal-body">
			               </div>
			                <div class="modal-footer">
			                    <button type="button" class="btn btn-danger" data-dismiss="modal" id="coloseBtn">Cancel</button>
			                </div>
			            </div>
			        </div>
    			</div>
<!-- Links Pop up -->
</div>
	<script type="text/javascript">
		function showAllLinks(roleId){
			var url = mainUrl+'/roleMenu/getAllModuleLinksRole/'+roleId;
			var result = asynchronousAjaxCall(url);
     		var body = result;
		    $("#links .modal-body").html(body);
	   		$("#links").modal("show");
		}
	</script>
	<script type="text/javascript">
$(document).ready(function() {
    $('#linksList').DataTable({
    	"scrollX": true
    });
} );
</script>
<script type="text/javascript">
	function printRoleWiseConfigurationList(roleName){
		alert("Role Name is : "+roleName);
	}
</script>
</body>
</html>