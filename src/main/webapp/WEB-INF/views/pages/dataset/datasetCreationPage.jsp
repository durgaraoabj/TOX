<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
	<h3 class="box-title">Dataset Creation</h3>

	<div >
			<input type="hidden" id="name2" value="${name}"/>
			<input type="hidden" id="desc2" value="${desc}"/>
			<input type="hidden" id="itemStatus2" value="${itemStatus}"/>
			<input type="hidden" id="metaDataVersion2" value="${metaDataVersion}"/>
			<input type="hidden" id="metaDataVersionName2" value="${metaDataVersionName}"/>
			<input type="hidden" id="studyODMIdName2" value="${studyODMIdName}"/>
			<input type="hidden" id="previousMetaDataVersionName2" value="${previousMetaDataVersionName}"/>
			<input type="hidden" id="phases2" value="${phases}"/>
			<input type="hidden" id="crf2" value="${crf}"/>
		<c:url value="/admini/crfRule/crfRuleSave" var="crfRuleSave" />
		<sf:form action="${crfRuleSave}" method="POST"  id="crfSave" enctype="multipart/form-data">
			<table class="table table-bordered table-striped">
				<tr>
					<td>Dataset Name</td>
					<td>
						<input type="text" class="form-control" name="name" id="name" />
						<font color="red" id="namemsg"></font>
					</td>
				</tr>
				<tr>
					<td>Desc</td>
					<td><textarea class="form-control" rows="3" name="desc" id="desc" onchange=""></textarea></td>
				</tr>
				<tr>
					<td>Item Status: : </td>
					<td>
					<input type="radio" name="itemStatus" value="all" checked="checked">all Available CRFs
					<input type="radio" name="itemStatus" value="marked">CRFs not Marked Complete
					<input type="radio" name="itemStatus" value="completed">CRFs Marked Complete
					</td>
				</tr>
				<tr>
					<td>Meta Data Version ODM ID : </td>
					<td>
						<input class="form-control" type="text" name="metaDataVersion" id="metaDataVersion" value="v1.0.0"/>
						<font color="red" id="metaDataVersionmsg"></font>
					</td>
				</tr>
				<tr>
					<td>Meta Data Version Name: </td>
					<td>
						<input class="form-control" type="text" name="metaDataVersionName" id="metaDataVersionName" value="MetaDataVersion_v1.0.0"/>
						<font color="red" id="metaDataVersionNamemsg"></font>
					</td>
				</tr>
				
				<tr>
				<td>Previous Study ODM ID:</td>
					<td>
						<input class="form-control" type="text" name="studyODMIdName" id="studyODMIdName" value=""/>
						<font color="red" id="studyODMIdNamemsg"></font>
					</td>
				</tr>
				<tr>
					<td>Previous MetaDataVersion ODM ID:</td>
					<td>
						<input class="form-control" type="text" name="previousMetaDataVersionName" 
						id="previousMetaDataVersionName" value=""/>
						<font color="red" id="previousMetaDataVersionNamemsg"></font>
					</td>
				</tr>
				<tr>
					<td>Select Phase : </td>
					<td id="secEleIdTd">
						<select class="form-control" name="phases" id="phases" onchange="displayCrfs()">
							<option value="-1" selected="selected">--Select--</option>
						<c:forEach items="${phases}" var="ph">
							<option value="${ph.id }">${ph.name}</option>
						</c:forEach>
					</select></td>
				</tr>

				<tr>
					<td>Select E-Form</td>
					<td id="crfTd"><select class="form-control" name="crf" id="crf"><option
								value="-1" selected="selected">--Select--</option></select></td>
				</tr>
				
			</table>
			
			<div id="discDataModal"  >
			<table id="tableId" class="table table-bordered table-striped">
	    		<tfoot>
	    			<tr>
	    				<td>
	    					<input type="checkbox" name="allItems" id="allItems" onclick="selectAllItems()">Select All Items	    				
	    				</td>
	    				<th colspan="6" style="text-align: center;">
	    					<input type="button" value="Save Add More Elements" onclick="addMoreElemets()"
	    						class="btn btn-primary" style="width:180px;"/>
	    					<input type="button" value="Save" id="saveDataSet" onclick="saveDataSetbut()" 
	    						class="btn btn-primary" style="width:60px;"/>
	    					<a href='<c:url value="/extractData/" />' class="btn btn-primary">Exit</a>
	    				</th>
	    			</tr>
	    		</tfoot>
	    	</table>
	    	<div id="crfelements"></div>
		</sf:form>
		<c:url value="/extractData/addMoreElemets" var="addMoreElemets" />
		<sf:form action="${addMoreElemets}" method="POST"  id="addMoreElemetsform" enctype="multipart/form-data">
			<input type="hidden" name="saveOrAdd" id="saveOrAdd" value="add"/>
			<input type="hidden" name="name1" id="name1"/>
			<input type="hidden" name="desc1" id="desc1"/>
			<input type="hidden" name="itemStatus1" id="itemStatus1"/>
			<input type="hidden" name="metaDataVersion1" id="metaDataVersion1"/>
			<input type="hidden" name="metaDataVersionName1" id="metaDataVersionName1"/>
			<input type="hidden" name="studyODMIdName1" id="studyODMIdName1"/>
			<input type="hidden" name="previousMetaDataVersionName1" id="previousMetaDataVersionName1"/>
			<input type="hidden" name="phases1" id="phases1"/>
			<input type="hidden" name="crf1" id="crf1"/>
			<input type="hidden" name="secchecklist" id="secchecklist"/>
			<input type="hidden" name="gropchecklist" id="gropchecklist"/>
			<input type="hidden" name="crfIds" value="2"/>
		</sf:form>
	</div>
<script type="text/javascript">

function selectAllItems(){
	if($('#allItems').is(":checked")){
		$.each($("input[name='secEleName']"), function(){
	        $(this).prop("checked", true);
	    });
		$.each($("input[name='groupEleName']"), function(){
	        $(this).prop("checked", true);
	    });
	}else{
		$.each($("input[name='secEleName']"), function(){
	        $(this).prop("checked", false);
	    });	
		$.each($("input[name='groupEleName']"), function(){
	        $(this).prop("checked", false);
	    });
	}
}

$(document).ready(function(){
	$('#discDataModal').hide();
	if($("#name2").val() != '') $("#name").val($("#name2").val());
	if($("#desc2").val() != '') $("#desc").val($("#desc2").val());
	if($("#itemStatus2").val() != '') $("#itemStatus").val($("#itemStatus2").val());
	if($("#metaDataVersion2").val() != '') $("#metaDataVersion").val($("#metaDataVersion2").val());
	if($("#metaDataVersionName2").val() != '') $("#metaDataVersionName").val($("#metaDataVersionName2").val());
	if($("#studyODMIdName2").val() != '') $("#studyODMIdName").val($("#studyODMIdName2").val());
	if($("#previousMetaDataVersionName2").val() != '') $("#previousMetaDataVersionName").val($("#previousMetaDataVersionName2").val());
// 		crf
});
function displayCrfs(){
	var periodId = $("#phases").val(); 
	if(periodId == -1){
		$("#crfTd").html('<select class="form-control" name="secEleId" id="secEleId"><option value="-1" selected="selected">--Select--</option></select>');
	}else{
		var result = asynchronousAjaxCall(mainUrl+"/extractData/crflist/"+periodId);
		if(result != '' || result != 'undefined')
			$("#crfTd").html(result);		
	}
}
function displayCrfsElemenst(){
	var crf = $("#crf").val(); 
	if(crf == -1){
		$("#crfelements").html('');
	}else{
		var phaseid = $("#phases").val();
		var result = asynchronousAjaxCall(mainUrl+"/extractData/crfelementsCheckBoxList/"+phaseid+"/"+crf);
		if(result != '' || result != 'undefined'){
			$("#crfelements").html(result);
			$('#discDataModal').show();
		}
					
	}
}

function addMoreElemets(){
	$("#name1").val($("#name").val());
	$("#desc1").val($("#desc").val());
	$("#itemStatus1").val($("#itemStatus").val());
	$("#metaDataVersion1").val($("#metaDataVersion").val());
	$("#metaDataVersionName1").val($("#metaDataVersionName").val());
	$("#studyODMIdName1").val($("#studyODMIdName").val());
	$("#previousMetaDataVersionName1").val($("#previousMetaDataVersionName").val());
	$("#phases1").val($("#phases").val());
	$("#crf1").val($("#crf").val());
	var favorite = [];
    $.each($("input[name='secEleName']:checked"), function(){
        favorite.push($(this).val());
    });
    $("#secchecklist").val(favorite.join(", "));
    favorite = [];
    $.each($("input[name='groupEleName']:checked"), function(){
        favorite.push($(this).val());
    });
    $("#gropchecklist").val(favorite.join(", "));
    $('#addMoreElemetsform').submit();
}

function saveDataSetbut(){
	var flag = 0;
	if(flag == 0){
		$("#saveOrAdd").val("saveDataSet");
		$("#name1").val($("#name").val());
		$("#desc1").val($("#desc").val());
		$("#itemStatus1").val($("#itemStatus").val());
		$("#metaDataVersion1").val($("#metaDataVersion").val());
		$("#metaDataVersionName1").val($("#metaDataVersionName").val());
		$("#studyODMIdName1").val($("#studyODMIdName").val());
		$("#previousMetaDataVersionName1").val($("#previousMetaDataVersionName").val());
		$("#phases1").val($("#phases").val());
		$("#crf1").val($("#crf").val());
		var favorite = [];
	    $.each($("input[name='secEleName']:checked"), function(){
	        favorite.push($(this).val());
	    });
	    $("#secchecklist").val(favorite.join(", "));
	    favorite = [];
	    $.each($("input[name='groupEleName']:checked"), function(){
	        favorite.push($(this).val());
	    });
	    $("#gropchecklist").val(favorite.join(", "));
		$('#addMoreElemetsform').submit();
	}else
		alert('From contains ' + flag + ' Requied Fields');
}

</script>

<script type="text/javascript">
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
			$("#otherCrf"+i+"msg").html("");
			if($("#otherCrf"+i).val().trim() == -1){
				$("#otherCrf"+i+"msg").html("Required Field"); flag ++;
			}
			$("#otherCrfSecEle"+i+"msg").html("");
			if($("#otherCrfSecEle"+i).val().trim() == -1 && $("#otherCrfGroupEle"+i).val().trim() == -1){
				$("#otherCrfSecEle"+i+"msg").html("Section Or Group element Required"); flag ++;	
			}
		}
	}
	return flag;
}
</script>