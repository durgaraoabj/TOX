<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
   <div class="card">
   <div class="card-header">
              <h3 class="card-title">Study Search</h3>
   </div>
      <table id="data" class="table table-bordered table-striped">
         <tr>
         <td>Study No</td>
         <td><input type="text" name="studyNo" id="studyNoId" class="form-control" >
         <div id="studyNoIdMsg" style="color: red;"></div></td>
          <td>Study Desc</td>
         <td><input type="text" name="studyName" id="studyNameId" class="form-control" >
         <div id="studyNameIdMsg" style="color: red;"></div></td>
         
         </tr>
         <tr>
	         <td>Sponsor</td>
	         <td><input type="text" name="sponsor" id="sponsorid" class="form-control" >
	         <div id="sponceridMsg" style="color: red;"></div></td>
	         <td>Status</td>
	         <td><select  name="status" id="statusid" class="form-control" >
	                  <option value="-1" selected="selected">---Select---</option>
	                  <option value="Inprogress">In Progress</option>
	                  <option value="Initiate">Initiate</option>
	                  <option value="Design">Design</option>
	                  <option value="Cancel">Cancel</option>
	                  
	         </select>
	         <div id="statusidMsg" style="color: red;"></div></td>
         </tr>
         <tr>
	         <td>Role</td>
	         <td><select name="role" id="roleid" class="form-control" >
	                  <option value="-1" selected="selected">---Select---</option>
	                  <option value="SD">Study Director</option>
	                  <option value="TFM">Text Facility Manager</option>
	             </select>
	         <div id="roleidMsg" style="color: red;"></div></td>
	        
         </tr>
      <tr align="center">
		<td colspan="4" ><input type="button" value="Submit" class="btn btn-primary"
			 onclick="submitForm()"/>
		</td>
	  </tr>
      </table>
     <%--  <c:url value="/studyAudit/studySearchData" var="studySearch" />
      <form:form action="${studySearch}" method="POST" id="submival">
      <input type="hidden" name="studynoval" id="studynovalid">
      <input type="hidden" name="studyNameval" id="studyNamevalid">
      <input type="hidden" name="sponsorval" id="sponsorvalid">
      <input type="hidden" name="statusval" id="statusvalid">
      <input type="hidden" name="roleval" id="rolevalid">
      </form:form> --%>
      <div id="datalist"></div>
   </div>
   <script type="text/javascript">
     function submitForm(){
    	// alert("work");
    	var studynovalid="-1";
    	var studyNamevalid="-1";
    	var sponsorvalid="-1";
    	var statusvalid="-1";
    	var rolevalid="-1";
    	
    	var studyNoId=$('#studyNoId').val();
    	//alert(studyNoId);
    	if(studyNoId!=''){
    		studynovalid=studyNoId;
    	}
    	var studyNameId=$('#studyNameId').val();
    	//alert(studyNameId);
    	if( studyNameId!=''){
    		studyNamevalid=studyNameId;
    	}
    	var sponsorid=$('#sponsorid').val();
    	if(sponsorid!=''){
    		sponsorvalid=sponsorid;
    	}
    	statusvalid=$('#statusid').val();
    	rolevalid=$('#roleid').val();
    	
    	
    	//alert(studynovalid+"::"+studyNamevalid+"::"+sponsorvalid+"::"+statusvalid+"::"+rolevalid);
    	
    	var result = asynchronousAjaxCall(mainUrl+"/studyAudit/studySearchData/"+studynovalid+"/"+studyNamevalid+"/"+sponsorvalid+"/"+statusvalid+"/"+rolevalid);
		//alert(result);
    	if(result != '' || result != 'undefined'){
			$("#datalist").html(result);
		}else{
			$("#datalist").html("");
		}
     }
   
   </script>
   
</body>
</html>