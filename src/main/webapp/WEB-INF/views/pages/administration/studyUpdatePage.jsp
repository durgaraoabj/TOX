<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Study Update</title>
</head>
<body>
  <c:url value="/administration/saveUpdateStudy" var="saveUpdate"></c:url>
  <form:form action="${saveUpdate}" method="POST" id="updateForm" modelAttribute="sm">
  	<input type='hidden' name="studyId" id="studyId" value="${sm.id}"/> 
  	 <div style="text-align: center;font-size: large; font-weight: bold;">Study Update</div>
  	 <table class="table table-bordered table-striped">
  	 	<form:hidden path="id"/>
  	 	<tr>
    			<td>Study Number :</td>
    			<td>
    				<div style="width: 40%;">
    					<form:input path="studyNo" type="text"  class="form-control input-sm" onblur="checkStudyNumber('studyNo', 'studyNoMsg', true)" id="studyNo"/>
    				</div>
    				<div id="studyNoMsg" style="color: red;"></div>
    			</td>
    		</tr>
    		<tr>
    			<td>Title : </td>
    			<td>
    				<div style="width: 40%;">
    					<form:textarea path="studyDesc" rows="2" class="form-control input-sm" id="studyDesc" onblur="studyDescValidation('studyDesc', 'studyDescMsg')"/>
    				</div>
    				<div id="studyDescMsg" style="color: red;"></div>
    			</td>
    		<tr>
    			<td>Primary User : </td>
    			<td>
    				<div style="width: 40%;">
	    				<form:select path="sdUser" id="userId" onchange="userIdValidation('userId', 'userIdMsg')" class="form-control">
	    					<form:option value="">----Select----</form:option>
	    					<c:forEach items="${usersList}" var="sdu">
	    							<form:option value="${sdu.id}">${sdu.username}</form:option>
	    					</c:forEach>
	    				</form:select>
    				</div>
    				<div id="userIdMsg" style="color: red;"></div>
    			</td>
    		</tr>
    		<tr>
    			<td>Secondary User : </td>
    			<td>
    				<div style="width: 40%;">
	    				<form:select path="asdUser" id="asduserId" onchange="asduserIdValidation('asduserId', 'asduserIdMsg')" class="form-control">
	    					<form:option value="">----Select----</form:option>
	    					<c:forEach items="${usersList}" var="sdu">
	    							<form:option value="${sdu.id}">${sdu.username}</form:option>
	    					</c:forEach>
	    				</form:select>
    				</div>
    				<div id="asduserIdMsg" style="color: red;"></div>
    			</td>
    		</tr>
    </table>
    		<div align="center"><input type="button" id="studyCreateFormSubmitBtn" onclick="submitUpdateForm()" value="Submit" class="btn btn-primary" style="width:200px;"></div>
  	 
  </form:form>
   <c:if test="${smMap.size() > 0 }">
    	<div style="color: blue; font-size: medium; text-align: center; font-weight: bold;">UserWise Assigned Studies List</div>
    	<div style="height: 550px; overflow: auto">
	    	<table class="table table-bordered table-striped" >
	    		<thead>
	    		   <tr>
		    			<th>User Name</th>
		    			<th colspan="4" style="text-align: center;">Studies</th>
	    			</tr>
	    		</thead>
	    		<c:forEach items="${smMap}" var="sm" varStatus="st">
					<tr>
						<td>${sm.key}</td>
						<td>
							<div style="height: 150px; overflow: auto">
								<table class="table table-bordered table-striped">
									<thead>
										<tr>
											<th>Study Number</th>
											<th>Title</th>
											<th>User Type</th>
											<th>Start Date</th>
										</tr>
									</thead>
									<c:forEach items="${sm.value}" var="std">
										<tr>
											<td>${std.studyNo}</td>
											<td>${std.studyDesc}</td>
											<c:choose>
												<c:when test="${std.userId eq std.sdUser}">
													<td>Primary</td>
												</c:when>
												<c:otherwise>
													<td>Secondary</td>
												</c:otherwise>
											</c:choose>
											<td><fmt:formatDate value="${std.startDate}" pattern="yyyy-MM-dd"/></td>
										</tr>
									</c:forEach>
								</table>
							</div>
						</td>
					</tr>
				</c:forEach>
			</table>
		</div>
    </c:if>
  <script type="text/javascript">
    var stdNoFlag = true;
    var stdDescFlag = false;
    var userFlag = false;
    var asdUserFlag = false;
    var chagneFlag = false;
	function checkStudyNumber(id, messageId, backed){
		chagneFlag = true;
		$('#'+messageId).html('');
		var value = $('#'+id).val();
        if(value == null || value == "" || value == "undefined"){
        	$('#'+messageId).html('Required Field.');
        	stdNoFlag = false;
        }else{
        	if(backed){
		       	var result = asynchronousAjaxCall(mainUrl+"/administration/checkStudyNumber/"+value+"/"+$("#studyId").val());
		   		result = result.trim();
		   		if(result != '' || result != 'undefined'){
		   			if(result == 'yes'){
		   				$('#'+messageId).html(value + " aleredy avilable.");
		   				stdNoFlag = false;
		   				$('#'+id).val("");			
		   			}else{
		   				$('#'+messageId).html('');
		   			}
		   		}else{
		   			$('#'+messageId).html('');
		   		}	
        	}
        }
		return stdNoFlag;
	}
	function studyDescValidation(id, messageId){
		chagneFlag = true;
		$('#'+messageId).html('');
		var value = $('#'+id).val();
        if(value == null || value == "" || value == "undefined"){
        	$('#'+messageId).html('Required Field.');
        	stdDescFlag = false;
        }else{
        	$('#'+messageId).html('');
        	stdDescFlag = true;
    	}
		return stdDescFlag;
	}
	function userIdValidation(id, messageId){
		chagneFlag = true;
		$('#'+messageId).html('');
		var value = $('#'+id).val();
		var value2 = $('#asduserId').val();
		if(value == null || value == "" || value == "undefined"){
        	$('#'+messageId).html('Required Field.');
        	userFlag = false;
        }else{
        	if(value2 == null || value2 == "" || value2 == "undefined"){
        		$('#'+messageId).html('');
        		$('#asduserIdMsg').html('');
            	userFlag = true;
        	}else{
        		if(value != value2){
        			$('#'+messageId).html('');
        			$('#asduserIdMsg').html('');
                	userFlag = true;
        		}else{
        			$('#'+messageId).html('Secondary User And Alternate User Both Are Not Same.');
                	userFlag = false;
        		}
        	}
        	
    	}
		return userFlag;
	}
	function asduserIdValidation(id, messageId){
		chagneFlag = true;
		userchagneFlag = true;
		$('#'+messageId).html('');
		var value = $('#'+id).val();
		var value2 = $('#userId').val();
		if(value == null || value == "" || value == "undefined"){
        	$('#'+messageId).html('Required Field.');
        	asdUserFlag = false;
        }else{
        	if(value2 == null || value2 == "" || value2 == "undefined"){
        		$('#'+messageId).html('');
        		$('#userIdMsg').html('');
        		asdUserFlag = true;
        	}else{
        		if(value != value2){
        			$('#'+messageId).html('');
        			$('#userIdMsg').html('');
        			asdUserFlag = true;
        		}else{
        			$('#'+messageId).html('Primary User And Secondary User Both Are Not Same.');
        			asdUserFlag = false;
        		}
        	}
    	}
		return asdUserFlag;
	}
	function submitUpdateForm(){
// 		alert(chagneFlag)
		if(chagneFlag ){
			stdNoFlag = checkStudyNumber('studyNo', 'studyNoMsg', false);
			stdDescFlag = studyDescValidation('studyDesc', 'studyDescMsg');
			asdUserFlag = asduserIdValidation('asduserId', 'asduserIdMsg');
			userFlag = userIdValidation('userId', 'userIdMsg');
// 			alert((stdNoFlag +"   "+ stdDescFlag +"   "+ userFlag +"   "+ asdUserFlag))
			if(stdNoFlag && stdDescFlag && userFlag && asdUserFlag)
				$('#updateForm').submit();			
		}else {
			alert("From dos n't have any changes");
		}

	}
</script>
</body>
</html>