<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Sponsor Creation Page</title>
</head>
<body>
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
<!-- rgba(137, 196, 244, 1) rgba(51, 110, 123, 1)-->
 <div class="card"  >
	<div class="card-header">
              <h3 class="card-title">Sponsor Creation</h3>
            </div>
	<!--Card content-->
  <div class="card-body">
   		<c:url value="/administration/saveSponsorMaster" var="saveSponsor" />
        <form:form action="${saveSponsor}" method="post" modelAttribute="sponsor" id="saveSponsorMastar">
           <table class="table table-bordered table-striped" style="margin-left: 30%; width: 30%;">
           		<tr>
           			<td>Code </td>
           			<td>
           				<div >
           					<form:input path="code" id="code" cssClass="form-control" onchange="codeValidation('code', 'codeMsg')"/>
           				</div>
           				<div id="codeMsg" style="color: red;"></div>
           			</td>
           		</tr>
           		<tr>
           			<td>Name </td>
           			<td>
           				<div >
           					<form:input path="name" id="code" cssClass="form-control" onchange="nameValidation('name', 'nameMsg')"/>
           				</div>
           				<div id="nameMsg" style="color: red;"></div>
           			</td>
           		</tr>
           		<tr>
           			<td>Description </td>
           			<td>
           				<div >
           					<form:input path="descData" id="descData" cssClass="form-control" onchange="descDataValidation('descData', 'descDataMsg')"/>
           				</div>
           				<div id="descDataMsg" style="color: red;"></div>
           			</td>
           		</tr>
           		<tr>
           			<td>Address </td>
           			<td>
           				<div >
           					<form:input path="address" id="address" cssClass="form-control" onchange="addressValidation('address', 'addressMsg')"/>
           				</div>
           				<div id="addressMsg" style="color: red;"></div>
           			</td>
           		</tr>
           		
           		<tr align="center">
           			<td colspan="2"><input  type="button" value="Submit" class="btn btn-primary btn-small" onclick="saveFunction()"></td>
           		</tr>
           </table>
        </form:form>
        </div>
        </div>
        <div class="card">
		<div class="card-header">
			<h3 class="card-title">Sponsor List</h3>
		</div>
		<div class="card-body">
			<table id="example1" class="table table-bordered table-striped">
    	   <thead>
	    		<tr>
	    			<th>S. NO.</th>
	    			<th>Code</th>
	    			<th>Name</th>
	    			<th>Description</th>
	    			<th>Created By</th>
	    			<th>Created On</th>
	    		</tr>
    		</thead>
    		<tbody>
    			<c:forEach items="${sponsorList}" var="sap" varStatus="st">
    			  <tr>
    			    <td>${st.count}</td>
    				<td>${sap.code}</td>
    				<td>${sap.name}</td>
    				<td>${sap.descData}</td>
    				<td>${sap.createdBy}</td>
    				<td>${sap.createdOn}</td>
    						
    				</tr>
    			</c:forEach>
    		</tbody>
    		
    	</table>
        </div>
        
       
   </div>
   <script type="text/javascript">
   
   function codeValidation(id,messagId) {
	   //debugger;
	   var flag=false;
	   $('#'+messagId).html("");
	   var data=$('#'+id).val();
	   var result="Yes";
	   if(data!=""){
		   result = asynchronousAjaxCall(mainUrl
					+ "/administration/getSponsorMasterUniqueCheck/"+data);
	   }
	   
	   if(result=="Yes"){
		   flag=false;
		   $('#'+messagId).html("Code Alrady Existing");
	   }else{
		   flag=true;
	   }
	   return flag;
   }
   function nameValidation(id,messagId) {
	   //debugger;
	   var flag=false;
	   var data=$('#'+id).val();
	   if(data!="" && data!="undefined"){
		   flag=true;
		   $('#'+messagId).html("");
	   }else{
		   flag=false;
		   $('#'+messagId).html("Required Field");
	   }
	   return flag;
   }
   function descDataValidation(id,messagId) {
	   var flag=false;
	   var data=$('#'+id).val();
	   if(data!="" ){
		   flag=true;
		   $('#'+messagId).html("");
	   }else{
		   flag=false;
		   $('#'+messagId).html("Required Field");
	   }
	   return flag;
   }
   function addressValidation(id,messagId) {
	   var flag=false;
	   var data=$('#'+id).val();
	   if(data!=""){
		   flag=true;
		   $('#'+messagId).html("");
	   }else{
		   flag=false;
		   $('#'+messagId).html("Required Field");
	   }
	   return flag;
   }
   
   function saveFunction() {
	  // debugger;
	  var codev =codeValidation('code','codeMsg');
	  var name =nameValidation('name','nameMsg');
	  var descv =descDataValidation('descData','descDataMsg');
	  var addressv =addressValidation('address','addressMsg');
	  if(codev && name && descv&&addressv){
		  $('#saveSponsorMastar').submit();
	  }
       
   }
   
   </script>
</body>
</html>