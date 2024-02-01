<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Study Plan Upload Page</title>
</head>
<body>
<div class="card">
			<div class="card-header" style="text-align: center;">
              <h3 class="card-title">Upload Study Plan</h3>
            </div>
    <div class="card-body">
	    <c:url value="/stuydPlan/saveStudyPlan" var="saveStudyPlan"></c:url>
	    <form:form action="${saveStudyPlan}" method="POST" enctype="multipart/form-data" id="studyPlanForm">
	    	<input type="hidden" name="studyId" id="studyId" value="${studyId}">
	    	<table class="table table-bordered table-striped" style="width:60%">
	    		<tr>
	    		    <td>Upload Study Plan : </td>
	    		    <td>
	    		    	<input type="file" name="file" id="file">
	    		    	<div id="uploadFileMsg" style="color: red;"></div>
	    		    </td>
	    		    
	    		</tr>
	    		<tr align="center">
	    			<td colspan="2">
	    				<input type="button" value="Submit" class="btn btn-primary" onclick="submtForm()">
	    				
	    			</td>
	    		</tr>
	    	</table>
	    </form:form>
    </div>
</div>
<script type="text/javascript">
function submtForm(){
	var fileVal = $('#file').val();
	if(fileVal != null && fileVal != "" && fileVal != "undefined"){
		regex = new RegExp("(.*?)\.(pdf)$");
	       if ((regex.test(fileVal))) {
			   $('#uploadFileMsg').html("");
			   $('#studyPlanForm').submit();
	       }else{
	    	   $('#uploadFileMsg').html("Please Upload PDF File.");
	       }
	}
}
</script>
</body>
</html>