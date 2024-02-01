<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/admini/crfRule/crfDateComparisonSave" var="crfDateComparisonSave" />
<sf:form action="${crfDateComparisonSave}" method="POST"  id="crfComparisonSave" enctype="multipart/form-data">
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">E-Form Date Comparison</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<table id="example1" class="table table-bordered table-striped">
				<tr>
					<td>Name</td>
					<td><input type="text" name="name" class="form-control"> </td>
					<td>Desc</td>
					<td colspan="3"><input type="text" name="descreption" class="form-control"> </td>
				</tr>
				<tr>
					<td>Select E-Form </td>
					<td>
						<select name="crf" class="form-control" onchange="getCrfElemntsAsSelect(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${crfList }" var="eform"> 
								<option value="${eform.id}">${eform.name}</option>
							</c:forEach>
						</select></td>
					<td>Message Section Element</td>
					<td id="secEleIdTd">
						<select name="secEleId" class="form-control">
							<option value="-1">--Select--</option>
						</select>
					</td>
					<td>Message Group Element</td>
					<td id="groupEleIdTd">
						<select name="groupEleId" id="groupEleId"><option value="-1" selected="selected">--Select--</option></select>
					</td>
				</tr>
				<tr>
					<td>Message</td>
					<td colspan="3"><input type="text" name="message" class="form-control"> </td>
					<td>Condition</td>
					<td>
						<select name="condition" id = "condition" class="form-control">
							<option value="eq" selected="selected">=</option>
							<option value="ne">!=</option>
							<option value="le">Less</option>
							<option value="leq">Less and eq</option>
							<option value="gt">Grater</option>
							<option value="gte">grater and eq</option>
						</select>
					</td>
				</tr>
				
				<tr><td colspan="6">Difference b/w two dates</td></tr>
				<tr>
					<td>E-Form </td>
					<td>
						<select name="crf1" class="form-control" onchange="getCrfElemntsAsSelect1(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${crfList }" var="eform"> 
								<option value="${eform.id}">${eform.name}</option>
							</c:forEach>
						</select></td>
					<td>Section Element</td>
					<td id="secEleIdTd1">
						<select name="crf1" class="form-control">
							<option value="-1">--Select--</option>
						</select>
					</td>
					<td>Group Element</td>
					<td id="groupEleIdTd1">
						<select name="groupEleId1" id="groupEleId1"><option value="-1" selected="selected">--Select--</option></select>
					</td>
				</tr>
				<tr>
					<td>E-Form </td>
					<td>
						<select name="crf2" class="form-control" onchange="getCrfElemntsAsSelect2(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${crfList }" var="eform"> 
								<option value="${eform.id}">${eform.name}</option>
							</c:forEach>
						</select></td>
					<td>Section Element</td>
					<td id="secEleIdTd2">
						<select name="crf2" class="form-control">
							<option value="-1">--Select--</option>
						</select>
					</td>
					<td>Group Element</td>
					<td id="groupEleIdTd2">
						<select name="groupEleId2" id="groupEleId2"><option value="-1" selected="selected">--Select--</option></select>
					</td>
				</tr>
				
				
				<tr><td colspan="6">Difference b/w two dates</td></tr>
				<tr>
					<td>E-Form </td>
					<td>
						<select name="crf3" class="form-control" onchange="getCrfElemntsAsSelect3(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${crfList }" var="eform"> 
								<option value="${eform.id}">${eform.name}</option>
							</c:forEach>
						</select></td>
					<td>Section Element</td>
					<td id="secEleIdTd3">
						<select name="crf3" class="form-control">
							<option value="-1">--Select--</option>
						</select>
					</td>
					<td>Group Element</td>
					<td id="groupEleIdTd3">
						<select name="groupEleId3" id="groupEleId3"><option value="-1" selected="selected">--Select--</option></select>
					</td>
				</tr>
				<tr>
					<td>E-Form </td>
					<td>
						<select name="crf4" class="form-control" onchange="getCrfElemntsAsSelect4(this.value)">
							<option value="-1">--Select--</option>
							<c:forEach items="${crfList }" var="eform"> 
								<option value="${eform.id}">${eform.name}</option>
							</c:forEach>
						</select></td>
					<td>Section Element</td>
					<td id="secEleIdTd4">
						<select name="crf4" class="form-control">
							<option value="-1">--Select--</option>
						</select>
					</td>
					<td>Group Element</td>
					<td id="groupEleIdTd4">
						<select name="groupEleId4" id="groupEleId4"><option value="-1" selected="selected">--Select--</option></select>
					</td>
				</tr>
				
				<tr>
					<td colspan="6">
						<input type="button" value="Save" class="btn btn-primary" 
						 onclick="submitForm()"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
</sf:form>

<script>
var groupEleId = "";
var groupEleId1 = "";
var groupEleId2 = "";
var groupEleId3 = "";
var groupEleId4 = "";
function submitForm(){
	$("#crfComparisonSave").submit();	
}
function getCrfElemntsAsSelect(id){
	if(id != -1){
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd').html(result);
			groupEleId = result;
		}
	}else{
		$('#secEleIdTd').html('<select name="secEleId" id="secEleId"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd').html('<select name="groupEleId" id="groupEleId"><option value="-1" selected="selected">--Select--</option></select>');
	}
}
function groupEleSelection(id, value){
	if(value != -1){
// 		groupEleId = $("#groupEleIdTd").html();
		$("#secEleId").val(-1);
		var v = $("#groupEleId").val();
		var nameArr = value.split(',');
		var rows = nameArr[1];
		var rowsele = "Row No : <select name='rowgroupEleId' id='rowgroupEleId'><option value='1' selected='selected'>1</option> ";
		for(var i=2; i<= rows; i++){
			rowsele += "<option value='"+i+"'>"+i+"</option>";
		}
		rowsele += "</select>";
		$("#groupEleIdTd").html(groupEleId + rowsele);
		$("#groupEleId").val(v);
	}
}
function secEleSelection(id, value){
	if(value != -1){
		$("#groupEleIdTd").html(groupEleId);
		$("#groupEleId").val(-1);
	}
}



function getCrfElemntsAsSelect1(id){
	if(id != -1){
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements1/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd1').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements1/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd1').html(result);
			groupEleId1 = result;
		}
	}else{
		$('#secEleIdTd1').html('<select name="secEleId1" id="secEleId1"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd1').html('<select name="groupEleId1" id="groupEleId1"><option value="-1" selected="selected">--Select--</option></select>');
	}
}
function groupEleSelection1(id, value){
	if(value != -1){
// 		groupEleId = $("#groupEleIdTd1").html();
		$("#secEleId1").val(-1);
		var v = $("#groupEleId1").val();
		var nameArr = value.split(',');
		var rows = nameArr[1];
		var rowsele = "Row No : <select name='rowgroupEleId1' id='rowgroupEleId1'><option value='1' selected='selected'>1</option> ";
		for(var i=2; i<= rows; i++){
			rowsele += "<option value='"+i+"'>"+i+"</option>";
		}
		rowsele += "</select>";
		$("#groupEleIdTd1").html(groupEleId1 + rowsele);
		$("#groupEleId1").val(v);
	}
}
function secEleSelection1(id, value){
	if(value != -1){
		$("#groupEleIdTd1").html(groupEleId1);
		$("#groupEleId1").val(-1);
	}
}




function getCrfElemntsAsSelect2(id){
	if(id != -1){
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements2/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd2').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements2/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd2').html(result);
			groupEleId2 = result;
		}
	}else{
		$('#secEleIdTd2').html('<select name="secEleId2" id="secEleId2"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd2').html('<select name="groupEleId2" id="groupEleId2"><option value="-1" selected="selected">--Select--</option></select>');
	}
}
function groupEleSelection2(id, value){
	if(value != -1){
// 		groupEleId = $("#groupEleIdTd2").html();
		$("#secEleId2").val(-1);
		var v = $("#groupEleId2").val();
		var nameArr = value.split(',');
		var rows = nameArr[1];
		var rowsele = "Row No : <select name='rowgroupEleId2' id='rowgroupEleId2'><option value='1' selected='selected'>1</option> ";
		for(var i=2; i<= rows; i++){
			rowsele += "<option value='"+i+"'>"+i+"</option>";
		}
		rowsele += "</select>";
		$("#groupEleIdTd2").html(groupEleId2 + rowsele);
		$("#groupEleId2").val(v);
	}
}
function secEleSelection2(id, value){
	if(value != -1){
		$("#groupEleIdTd2").html(groupEleId2);
		$("#groupEleId2").val(-1);
	}
}

//-------------------------------------------
function getCrfElemntsAsSelect3(id){
	if(id != -1){
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements3/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd3').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements3/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd3').html(result);
			groupEleId = result;
		}
	}else{
		$('#secEleIdTd3').html('<select name="secEleId3" id="secEleId3"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd3').html('<select name="groupEleId3" id="groupEleId3"><option value="-1" selected="selected">--Select--</option></select>');
	}
}
function groupEleSelection3(id, value){
	if(value != -1){
// 		groupEleId = $("#groupEleIdTd3").html();
		$("#secEleId3").val(-1);
		var v = $("#groupEleId3").val();
		var nameArr = value.split(',');
		var rows = nameArr[1];
		var rowsele = "Row No : <select name='rowgroupEleId3' id='rowgroupEleId3'><option value='1' selected='selected'>1</option> ";
		for(var i=2; i<= rows; i++){
			rowsele += "<option value='"+i+"'>"+i+"</option>";
		}
		rowsele += "</select>";
		$("#groupEleIdTd3").html(groupEleId3 + rowsele);
		$("#groupEleId3").val(v);
	}
}
function secEleSelection1(id, value){
	if(value != -1){
		$("#groupEleIdTd3").html(groupEleId3);
		$("#groupEleId3").val(-1);
	}
}




function getCrfElemntsAsSelect4(id){
	if(id != -1){
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements4/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd4').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements4/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd4').html(result);
			groupEleId4 = result;
		}
	}else{
		$('#secEleIdTd4').html('<select name="secEleId4" id="secEleId4"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd4').html('<select name="groupEleId4" id="groupEleId4"><option value="-1" selected="selected">--Select--</option></select>');
	}
}
function groupEleSelection4(id, value){
	if(value != -1){
// 		groupEleId = $("#groupEleIdTd4").html();
		$("#secEleId4").val(-1);
		var v = $("#groupEleId4").val();
		var nameArr = value.split(',');
		var rows = nameArr[1];
		var rowsele = "Row No : <select name='rowgroupEleId4' id='rowgroupEleId4'><option value='1' selected='selected'>1</option> ";
		for(var i=2; i<= rows; i++){
			rowsele += "<option value='"+i+"'>"+i+"</option>";
		}
		rowsele += "</select>";
		$("#groupEleIdTd4").html(groupEleId4 + rowsele);
		$("#groupEleId4").val(v);
	}
}
function secEleSelection4(id, value){
	if(value != -1){
		$("#groupEleIdTd4").html(groupEleId4);
		$("#groupEleId4").val(-1);
	}
}


</script>