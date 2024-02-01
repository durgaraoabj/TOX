<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>PDF Selection</title>
</head>
<body>
	<div style="text-align: center; font-size: large; color: blue; font-weight: bold;">PDF Reports Selection</div>
<div class="card">
   <div class="card-header">
   <div style="width: 70%; padding-left: 30%; ">
		 <table class="table table-bordered table-striped">
			<tr>
				<td style="text-align: center">Select Volunteer :</td>
				<td align="center"><select name="volId" id="volId" onchange="volunteerSelectBoxFunction('volId')">
					    <option value="-1">--Select--</option>
					    <option value="All">All</option>
						<c:forEach items="${volList}" var="vol">
						   <option value="${vol.id}">${vol.volId}</option>
						</c:forEach>
					</select>
					<font id="volMsg" color="red"></font>
				</td>
				<td style="text-align: center">Select Phase :</td>
				<td align="center"><select name="period" id="period" onchange="phraseSelectBoxFunction('period')">
					    <option value="-1">--Select--</option>
					    <option value="All">All</option>
						<c:forEach items="${periodList}" var="pd">
						   <option value="${pd.id}">${pd.name}</option>
						</c:forEach>
					</select>
					<font id="phaseMsg" color="red"></font>
				</td>
			</tr>
		</table>
		<div align="center"><input type="button" value="Submit" class="btn btn-danger" onclick="submitForm()"></div>
    </div>
    <div id="pdfresultList"></div>
</div>
</div>
<script type="text/javascript">
    function volunteerSelectBoxFunction(id){
    	var flag = false;
    	var value = $('#'+id).val();
    	if(value =="-1" || value =="undefined"){
    		$('#volMsg').html("Please Select Volunteer");
    		flag = false;
    	}else{
    		$('#volMsg').html("");
    		flag =true;
    	}
    	return flag;
    }
    function phraseSelectBoxFunction(id){
    	var flag = false;
    	var value = $('#'+id).val();
    	if(value =="-1" || value =="undefined"){
    		$('#phaseMsg').html("Please Select Phase");
    		flag = false;
    	}else{
    		$('#phaseMsg').html("");
    		flag =true;
    	}
    	return flag;
    }
	function submitForm(){
		var volFlag = volunteerSelectBoxFunction('volId');
		var phFlag = phraseSelectBoxFunction('period');
		if(volFlag && phFlag){
		//	alert("Every thing Ok");
			var volId = $('#volId').val();
			var phid = $('#period').val();
			//alert("Parameters : "+volId+":: "+phid);
			var result = asynchronousAjaxCall(mainUrl+"/reports/getPdfList/"+volId+"/"+phid);
			//alert("result is :"+result);
			if(result != '' || result != 'undefined'){
				$('#pdfresultList').html(result);
			}
		}
	}
</script>
</body>
</html>