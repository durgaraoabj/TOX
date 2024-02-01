<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
	<body>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Observation Approval Level:</h3>
              <input type="hidden" id="oldVal" value="${rl.observationApprovelLevel}"/>
			</div>
			
			 <div class="card-body">
            	<div style="width: 100%; overflow: scroll">
            		<c:url value="/administration/crf/observationApprovelLevel" var="observationApprovelLevelId" />
			    	<form action="${observationApprovelLevelId}" method="POST"  id="observationApprovelLevelSubmit">
			    		<table class="table table-bordered table-striped">
						<tr>
							<th>Approval Level : </th>
							<td>
								<input type="text" value="${rl.observationApprovelLevel}" name="observationApprovelLevel" 
									id="observationApprovelLevel" onchange="chekcValue(this.value)"> 
								<font color="red" id="observationApprovelLevelMsg"></font>
							</td>
							<td><input type="button" class="btn btn-primary" onclick="submit()" value="Save"/></td>
						</tr>
						</table>
			    	</form>
			    </div>
			 </div>
			
		</div>
	</body>
	<script type="text/javascript">
		function submit() {
			alert("asdf")
			var value = $("#observationApprovelLevel").val().trim();
			if(chekcValue(value)){
				alert("sdf")
// 				if($("#observationApprovelLevel").val() == $("#oldVal").val()){
// 					$("#observationApprovelLevelSubmit").submit();
// 				}else {
// 					$("#observationApprovelLevelMsg").html("In-Valied data");
// 				}
			}			
		}
		
		function chekcValue(value){
// 			alert(value.trim());
			value = value.trim();
			if(value != ''){
				value = parseInt(value);
				if(value< 1){
					$("#observationApprovelLevelMsg").html("In-Valied data");
				}else{
					return true;
				}
			}else
				$("#observationApprovelLevelMsg").html("Required Field");
			return false;
		}
	</script>
</html>