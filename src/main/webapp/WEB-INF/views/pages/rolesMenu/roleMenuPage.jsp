<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Roles Wise Menu</title>
</head>
<body>
<div class="card">
<c:if test="${messageOfPage != null}" >
	<div class="card" id="success" style="background-color: rgba(0, 128, 0, 0.9); border-radius: 20px; border: 3px solid #f3fff0; width: 50%; margin-left: 25%;">
		<h5 class="card-header info-color white-text text-center py-2">
	       <strong style="color: white;">${messageOfPage}</strong>
	     </h5>
	 </div>
</c:if>
<c:if test="${errorMsgOfPage != null}">
 <div class="card" id="failed" style="background-color: rgba(255, 0, 0, 0.9); border-radius: 20px; border: 3px solid #f3fff0; width: 50%; margin-left: 25%;">
		<h5 class="card-header info-color white-text text-center py-2">
	       <strong style="color: white;" >${errorMsgOfPage}</strong>
	     </h5>
</div>
</c:if>
	<%-- <div class="container">  
    <ol class="breadcrumb arr-bread">  
        <li> <a href = "<c:url value="/dashboard/"/>" > DashBoard </a> </li>  
        <li> <a href = "<c:url value="/administration/"/>"> Administration </a> </li>  
        <li class = "active"><span>Module Access Roles</span></li>  
    </ol>  
</div>   --%>
	<!--Card content-->
	<div class="card-header"><h3 class="card-title">Module Access</h3></div><div class="card-body">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-10">
			<div class="card" style="margin-left: 23%;">
              <div class="card-header" style="background: #123347;">
              	<div>
              		<h3 class="card-title" style="color:white; text-align: center;">Roles Wise Modules </h3>
              		<div align="right">
              			 	<a href='<c:url value="/roleMenu/printRolesinPdf"/>' target="_blank"><input type="button" value="Print" class="btn btn-warning btn-sm"></a>
              		</div>
              	</div>
                
              </div>
              <div class="card-body p-0">
                <table  border="2" style="width: 100%;" style="background: #CDD59C;">
                	<c:set value="${0}" var="count" scope="request"></c:set>
                   	 <c:forEach items="${rolesList}" var="rlist">
                   	 <c:choose>
                   	 		<c:when test="${count eq 0}">
                   	 			<tr></tr>
                  	 				<td><a href="#" style="color: #105978; font-size: medium; font-weight: bold;" onclick="getModules('${rlist.id}')">${rlist.role}</a></td>
              						<c:set value="${count+1}" var="count"></c:set>
                   	 		</c:when>
                   	 		<c:otherwise>
                   	 			<td><a href="#" style="color: #105978; font-size: medium; font-weight: bold;" onclick="getModules('${rlist.id}')">${rlist.role}</a></td>
              					<c:set value="${count+1}" var="count"></c:set>
               	 				<c:if test="${count eq 5}">
               	 					<c:set var="count" value="${0}"></c:set>
               	 				</c:if>		
               	 			</c:otherwise>
                   	 </c:choose>
                   	</c:forEach>
               </table>
              </div>
            </div>
            <!-- /.card -->
          </div>
          
        </div>
        <!-- /.row -->
       </div><!-- /.container-fluid -->
       </div>
      
       <div id="rolewiseResult"></div>
  </div>             
     <script type="text/javascript">
     	function getModules(rolId){
     		debugger;
     		var url = mainUrl+'/roleMenu/viewRolewiseModules/'+rolId;
     		var result = asynchronousAjaxCall(url);
			$('#rolewiseResult').html(result);
     	}
     	
     	function submitInactiveLink(linkId){
     		debugger;
			if(linkId != "" && linkId != "undefined"){
				var roleId = $('#roleId').val();
				var url = mainUrl+'/roleMenu/inactiveSideMenu/'+roleId+'/'+linkId;
				var result = asynchronousAjaxCall(url);
				$('#rolewiseResult').html(result);
				
	     		
			}
			
			
		}
     	function submitSelectedLinks(){
     		debugger;
     		if(idsArr.length == 0){
				$('#linksMsg').html("Select Atleast One Link");
			}else{
				var roleId = $('#roleId').val();
				var url = mainUrl+'/roleMenu/AddSubLinksToRole/'+roleId+'/'+idsArr;
				var result = asynchronousAjaxCall(url);
				$("#modal .close").click();
				$("#links").modal("hide");
				$('#rolewiseResult').html(result);
				
				
			}
		}
     </script>  
    
</body>
</html>