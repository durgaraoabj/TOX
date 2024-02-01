<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
     <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Multi Form Calculation</title>
</head>
<body>
 <div class="card">
	<div class="card-header">
		<h3 class="card-title">
			<b>Multi Form Calculation</b>
		</h3>
	</div>
	<div class="box-body">
		<table class="table table-bordered">
			<tr>
				<td>Source Form :</td>
				<td>
					<select name="sourceForm" id="sourceForm" class="form-control" onchange="sourceFormValidation('sourceForm', 'sourceFormMsg')">
						<option value="">-----Select-----</option>
						<c:forEach items="${crfList}" var="crf">
							<option value="${crf.id}">${crf.name}</option>
						</c:forEach>
					</select>
					<div id="sourceFormMsg" style="color: red;"></div>
			    </td>
			</tr>
		</table>
		<div id="sectionsDiv"></div>
	</div>
</div>
<script type="text/javascript">
    var sourceFlag = false;
	function sourceFormValidation(id, messageId){
		var value = $('#'+id).val();
		if(value != null && value != "" && value != "undefined"){
			$('#'+messageId).html("");
			sourceFlag = true;
			var result = asynchronousAjaxCall(mainUrl+"/formCalculation/getCrfSectionAndGroupElements/"+value);
			if(result != null && result != "" && result != "undefined"){
				$('#sectionsDiv').html(result);
			}
		}else{
			$('#'+messageId).html("Select Source Form.");
			sourceFlag = false;
		}
		return sourceFlag;
		
	}
</script>
</body>
</html>