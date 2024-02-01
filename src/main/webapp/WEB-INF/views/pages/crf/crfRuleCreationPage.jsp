<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<c:url value="/admini/crfRule/crfRuleSave" var="crfRuleSave" />
		<sf:form action="${crfRuleSave}" method="POST"  id="crfSave" enctype="multipart/form-data">
			<input type="hidden" name="newRows" id="newRows"/> 
			<div class="card">
              <div class="card-header">
                <h3 class="card-title">CRF Rule Creation</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body">
              <div style="width:40%;margin-left:25%">
                  <div class="row">
                    <div class="col-sm-4">
                      <!-- text input -->
                      <div class="form-group">
                        <label>Rule Name</label>
                        <input class="form-control" placeholder="Enter ..." type="text" name="name" id="name" />
						<font color="red" id="namemsg"></font>
                      </div>
                    </div>
                  
                    <div class="col-sm-4">
                      <!-- textarea -->
                      <div class="form-group">
                        <label>Description</label>
                         <input class="form-control" placeholder="Enter ..." type="text" name="desc" id="desc" />
                      </div>
                    </div>
                  
                    <div class="col-sm-4">
                      <!-- text input -->
                      <div class="form-group">
                        <label>Message </label>
                        <input class="form-control" placeholder="Enter ..." type="text" name="message" id="message" />
						<font color="red" id="messagemsg"></font>
                      </div>
                    </div>
                 </div>
				  <div class="row">
                    <div class="col-sm-4">
                      <!-- select -->
                      <div class="form-group">
                        <label>Select E-From</label>
                        <select class="form-control" name="crfid" id="crfid" onchange="crfIdChange(this.value)">
							<option value="-1" selected="selected">--Select--</option>
							<c:forEach var="crf" items="${crfs}">
								<option value="${crf.id}">${crf.name}</option>
							</c:forEach>
						</select>
						<font color="red" id="crfidmsg"></font>
                      </div>
                    </div>
                  
                    <div class="col-sm-4">
                      <!-- select -->
                      <div class="form-group">
                        <label>Select Section Element</label>
                        <div id="secEleIdTd">
                        <select name="secEleId" id="secEleId"  class="form-control">
                        	<option value="-1" selected="selected">--Select--</option></select></div>
                      </div>
                    </div>
                
                    <div class="col-sm-4">
                      <!-- select -->
                      <div class="form-group">
                        <label>Select Group Element</label>
                        <div id="groupEleIdTd">
                        <select name="groupEleId" id="groupEleId"  class="form-control">
                        	<option value="-1" selected="selected">--Select--</option></select></div>
					  </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-6">
                      <div class="form-group">
                        <font color="red" id="secEleIdmsg"></font>
					  </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-6">
                      <div class="form-group">
                      	<label>Compare With </label>
                      	<div class="form-check">
                          <input type="radio" name="compareWith" value="userInput" checked="checked" class="form-check-input">
                          <label class="form-check-label">Value</label>
                        </div>
                        <div class="form-check">
                          <input class="form-check-input" name="compareWith" value="Other CRF Field" type="radio">
                          <label class="form-check-label">Other E-From Field</label>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-6">
                      <!-- text input -->
                      <div class="form-group">
                        <label>Value </label>
                        <input class="form-control" placeholder="Enter ..." type="text" name="userInput" id="userInput" />
                        <label>Condition </label>
                        <select name="compareWithCondition" class="form-control">
							<option value="eq" selected="selected">=</option>
							<option value="ne">!=</option>
							<option value="le">Less</option>
							<option value="leq">Less and eq</option>
							<option value="gt">Grater</option>
							<option value="gte">grater and eq</option>
						</select>
                        <label>N Condition </label>
                        <select name="compareWithConditionN" class="form-control">
							<option value="and" selected="selected">And</option>
							<option value="or">Or</option>
						</select>
                      </div>
                    </div>
                  
                    <div class="col-sm-6">
                      <!-- text input -->
                      <div class="form-group">
                        <label>Value </label>
                        <input class="form-control" placeholder="Enter ..." type="text" name="userInput2" id="userInput2" />
                        <label>Condition </label>
                        <select name="compareWithCondition2" class="form-control">
							<option value="eq" selected="selected">=</option>
							<option value="ne">!=</option>
							<option value="le">Less</option>
							<option value="leq">Less and eq</option>
							<option value="gt">Grater</option>
							<option value="gte">grater and eq</option>
						</select>
                        <label>N Condition </label>
                        <select name="compareWithConditionN2" class="form-control">
							<option value="and" selected="selected">And</option>
							<option value="or">Or</option>
						</select>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-sm-6">
                      <div class="form-group">
                        <font color="red" id="userInputmsg"></font>
					  </div>
                    </div>
                  </div>
              </div>
              </div>
              </div>
              <!-- /.card-body --> 
              <div class="card"><div class="card-header"><h3 class="card-title">Other E-From Elements</h3></div><div class="card-body"> 
			<table id="example1" class="table table-bordered table-striped">
	    		<thead>
		    		<tr id="fixed">
		    			<td>E-From</td>
		    			<td>Section Element</td>
		    			<td>Group Element</td>
		    			<td>Row No</td>
		    			<td>Condition</td>
		    			<td>N Condition</td>
		    		</tr>
	    		</thead>
	    		<tbody>
		    		
	    		</tbody>
	    		<tfoot>
	    			<tr>
	    				<th colspan="6" style="text-align: left;">
	    					<input type="button" value="Add" id="addRow" onclick="addRroele()"
	    						class="btn btn-primary" >&nbsp;&nbsp;&nbsp;&nbsp;
	    					<input type="button" value="Remove" class="btn btn-primary" onclick="removeRowele()"
	    					id="removeRow" ></th>
	    			</tr>
	    			<tr>
	    				<th colspan="6" style="text-align: center;">
	    					<input type="button" value="Save" id="save" onclick="saveAction()"
	    						class="btn btn-primary"/>
	    					<a href='<c:url value="/admini/crfRule/" />' class="btn btn-primary">Exit</a>
	    				</th>
	    			</tr>
	    		</tfoot>
	    	</table>
	    	</div></div>
		</sf:form>
		

<script type="text/javascript">
function crfIdChange(id){
	
	if(id == -1){
		$('#secEleIdTd').html('<select name="secEleId" id="secEleId"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd').html('<select name="groupEleId" id="groupEleId"><option value="-1" selected="selected">--Select--</option></select>');
	}else{
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElements/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd').html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfGroupElements/"+id);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd').html(result);
			groupEleId = result;
		}
	}
	$("#tableId tbody").html("");
	$("#crfidmsg").html();
}
function secEleSelection(id, value){
	if(value != -1){
		$("#groupEleId").val(-1);
	}
}


	var rowId = 0;
	function addRroele(){
		var crf = $("#crfid").val();
// 		alert(crf)
		if(crf != '-1'){
			var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfRuleElements/"+rowId+"/"+$("#crfid").val());
			if(result != '' || result != 'undefined'){
				rowId ++;
				$("#tableId tbody").append(result);
			}			
		}else{
			$("#crfidmsg").html("Required Field");
		}

	}
	function removeRowele(){
		if(rowId != 0){
			$('#AddRow'+rowId).remove();
			rowId --;
		}else{
			alert('No more Other Crf Elements to remove');	
		}
	}

</script>

<script type="text/javascript">

	function othersecEleSelection(id, value, count){
		if(value != -1){
			$("#otherCrfGroupEle"+count).val(-1);
		}
	}
	function groupEleSelection(id, value){
		if(value != -1){
			$("#secEleId").val(-1);
			var v = $("#groupEleId").val();
			var nameArr = value.split(',');
			var rows = nameArr[1];
			var rowsele = "Row No : <select name='groupEleId' id='groupEleId'><option value='1' selected='selected'>1</option> ";
			for(var i=2; i<= rows; i++){
				rowsele += "<option value='"+i+"'>"+i+"</option>";
			}
			rowsele += "</select>";
			$("#groupEleIdTd").html(groupEleId + rowsele);
			$("#groupEleId").val(v);
		}
	}
	function othergroupEleSelection(id, value, count){
		if(value != -1){
			$("#otherCrfSecEle"+count).val(-1);
			var nameArr = value.split(',');
			var rows = nameArr[1];
			var rowsele = "<select name='otherCrfGroupEleRowNo"+count+"' id='otherCrfGroupEleRowNo1"+count+"'><option value='1' selected='selected'>1</option> ";
			for(var i=2; i<= rows; i++){
				rowsele += "<option value='"+i+"'>"+i+"</option>";
			}
			rowsele += "</select>";
			$("#groupEleIdTdRow"+count).html(rowsele);		}
	}
</script>
<script type="text/javascript">

var groupEleId= "";
$(document).ready(function() {
	$("#crfid").change(function() {
		var id = $( this ).val();
		
	});
});
</script>

<script type="text/javascript">
// var othergroupEleId= {};
function otherCrf(id, value, count){
	if(id == -1){
		$('#secEleIdTd'+count).html('<select name="otherCrfSecEle'+count+'" name="otherCrfSecEle'+count+'"><option value="-1" selected="selected">--Select--</option></select>');
		$('#groupEleIdTd'+count).html('<select name="otherCrfGroupEle'+count+'" id="otherCrfGroupEle'+count+'"><option value="-1" selected="selected">--Select--</option></select>');
	}else{
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/otherCrfSectionElements/"+value+"/"+count);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd'+count).html(result);
		}
		result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/otherCrfGroupElements/"+value+"/"+count);
		if(result != '' || result != 'undefined'){
			$('#groupEleIdTd'+count).html(result);
// 			othergroupEleId[count] = result;
		}
	}
	
}
</script>

<script type="text/javascript">
function saveAction(){
	var flag = validation();
	if(flag == 0){
		$("#newRows").val(rowId);
		$('#crfSave').submit();
	}
}
function validation(){
	var flag = 0;
	$("#namemsg").html("");
	if($("#name").val().trim() == ''){
		$("#namemsg").html("Required Field"); flag ++;
	}
	$("#messagemsg").html("");
	if($("#message").val().trim() == ''){
		$("#messagemsg").html("Required Field"); flag ++;
	}
	$("#crfidmsg").html("");
	if($("#crfid").val().trim() == -1){
		$("#crfidmsg").html("Required Field"); flag ++;
	}
	$("#secEleIdmsg").html("");
	if($("#secEleId").val().trim() == -1 && $("#groupEleId").val().trim() == -1){
		$("#secEleIdmsg").html("Section Or Group element Required"); flag ++;	
	}
	$("#userInputmsg").html("");
	if($("input[name='compareWith']:checked").val() == 'userInput'){
		if($("#userInput").val().trim() == ''){
			$("#userInputmsg").html("Required Field"); flag ++;
		}
	}else{
		for(var i=1; i<=rowId; i++){
// 			alert("#otherCrf"+i)
			$("#otherCrf"+i+"msg").html("");
			if($("#otherCrf"+i).val().trim() == -1){
				$("#otherCrf"+i+"msg").html("Required Field"); flag ++;
			}
			$("#otherCrfSecEle"+i+"msg").html("");
// 			alert("#otherCrfSecEle"+i)
			if($("#otherCrfSecEle"+i).val().trim() == -1 && $("#otherCrfGroupEle"+i).val().trim() == -1){
				$("#otherCrfSecEle"+i+"msg").html("Section Or Group element Required"); flag ++;	
			}
		}
	}
	return flag;
}
</script>