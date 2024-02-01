<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Satorius Data</title>
</head>
<body>
	<div style="text-align: center; font-size: large; color: blue; font-weight: bold;">Satorius Data</div>
<div class="card">
   <div class="card-header">
   <div style="width: 70%; padding-left: 30%; ">
		 <table class="table table-bordered table-striped">
			<tr>
				<td style="text-align: center">Select Study :</td>
				<td align="center"><select name="studyid" id="studyid" class="form-control" onchange="studyidSelectBoxFunction('studyid')">
					    <option value="-1">--Select--</option>
						<c:forEach items="${studyList}" var="study">
						   <option value="${study.id}">${study.studyNo}</option>
						</c:forEach>
					</select>
					<font id="studyMsg" color="red"></font>
				</td>
				
			</tr>
		</table>
		<div align="center"><input type="button" value="Submit" class="btn btn-danger" onclick="submitForm()"></div>
    </div>
    <div id="satList"></div>
</div>
</div>
<script type="text/javascript">
    function studyidSelectBoxFunction(id){
    	var flag = false;
    	var value = $('#'+id).val();
    	if(value =="-1" || value =="undefined"){
    		$('#studyMsg').html("Please Select Study");
    		flag = false;
    	}else{
    		$('#studyMsg').html("");
    		flag =true;
    	}
    	return flag;
    }
    
	function submitForm(){
		var studyidFlag = studyidSelectBoxFunction('studyid');
		if(studyidFlag){
			//alert("Every thing Ok");
			var studyId = $('#studyid').val();
			//alert("studyId : "+studyId+"::"+mainUrl+"/instrument/coagulometerStudyData/"+studyId);
			
			var result = asynchronousAjaxCall(mainUrl+"/instrument/sartoriusFullData/"+studyId);
			//alert("result is :"+result);
			if(result != '' || result != 'undefined'){
				$('#satList').html(result);
			}else{
				$('#satList').html("");
			}
		}
	}
</script>
</body>
</html>