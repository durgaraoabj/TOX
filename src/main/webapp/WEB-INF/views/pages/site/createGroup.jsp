<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
</head>
<body>

<c:url value="/site/createGroupSave" var="createGroupSave" />
<sf:form action="${createGroupSave}" method="POST"  id="formsumit" modelAttribute="group">
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Group Creation Page</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Name:</td>
				<td><input type="text" class="form-control" name="groupName" id="groupName" 
				 onblur="checkGroupName(this.value)"/><font color="red" id="groupNamemsg"></font></td>
			</tr>
			<tr>
				<td>Description:</td>
				<td><textarea class="form-control" name="description" id="description"></textarea><font color="red" id="descriptionmsg"></font></td>
			</tr>
			<tr>
				<td>Group Class:</td>
				<td>
					<select name="gc" id="gc" class="form-control" onchange="checkGroupName2()">
						<c:forEach items="${groupClass }" var="gcl">
							<option value="${gcl.id }">${gcl.groupName }</option>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
			<tr>
				<td colspan="2">
					<input type="button" value="Save" class="btn btn-primary"
					 onclick="submitForm()"/>
				</td>
			</tr>
		</table>
	</div>
</div>
</sf:form>
<script>
var flag2 = true;
function checkGroupName(groupName){
	flag2 = true;
	var groupclass = $("#gc").val();
	var result = asynchronousAjaxCall(mainUrl+"/site/checkGroupName/"+groupclass+"/"+groupName);
	if(result != '' && result != 'undefined'){
		$('#groupNamemsg').html(result);
		$('#groupName').val("");
		flag2 = false;
	}else{
		$('#groupNamemsg').html('');
	}
}
function checkGroupName2(){
	flag2 = true;
	var groupName = $("#groupName").val();
	var groupclass = $("#gc").val();
	var result = asynchronousAjaxCall(mainUrl+"/site/checkGroupName/"+groupclass+"/"+groupName);
	if(result != '' && result != 'undefined'){
		$('#groupNamemsg').html(result);
		flag2 = false;
	}else{
		$('#groupNamemsg').html('');
	}
}

function submitForm(){	
	if(flag2)
		$("#groupNamemsg").html("");
	$("#descriptionmsg").html("");
	var flag = true;
	if($("#groupName").val().trim() == ""){
		$("#groupNamemsg").html("Required Field");  flag = false;
	}else{
		if($("#groupName").val().trim() == 'UTI' || $("#groupName").val().trim() == 'IAI' || $("#groupName").val().trim() == 'LRTI'){
			
		}else{
			$("#groupNamemsg").html("Invalid Group Name");  flag = false;
		}
	}
	
	if($("#description").val().trim() == ""){
		$("#descriptionmsg").html("Required Field");  flag = false;
	}
	if(flag && flag2) $("#formsumit").submit();
}
</script>




</body>
</html>