<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isErrorPage="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta charset="ISO-8859-1">
<title>Template Preview</title>

 <link href="<c:url value="/static/css/themes/infragistics/infragistics.theme.css"/>" rel="stylesheet" />
	<link href="<c:url value="/static/css/structure/infragistics.css"/>" rel="stylesheet" />
	<link rel="stylesheet" href="<c:url value="/static/css/dialog.css"/>">
	<link rel="stylesheet" href="<c:url value="/static/css/dialog-simple.css"/>">
	
	
	<script src="<c:url value="/static/modernizr/modernizr-2.8.3.js"/>"></script>
	<script src="<c:url value="/static/js/jquery-1.11.3.min.js"/>"></script>
	<script src="<c:url value="/static/js/jquery-ui.min.js"/>"></script>

	<!-- Ignite UI Required Combined JavaScript Files -->
<!-- 	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script> -->
	<script src="<c:url value="/static/js/infragistics.core.js"/>"></script>
	<script src="<c:url value="/static/js/infragistics.lob.js"/>"></script>
	<script src="<c:url value="/static/js/infragistics.excel-bundled.js"/>"></script>
	<script src="<c:url value="/static/js/infragistics.spreadsheet-bundled.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/dialog.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/static/js/dialog-polyfill.js"/>"></script>
	<link rel="stylesheet" href="<c:url value="/static/conformalert/jquery-confirm.min.css"/>">
<script src="<c:url value="/static/conformalert/jquery-confirm.min.js"/>"></script>

<head>
 <script type="text/javascript">
 $(document).ready(function(){
        	 $('#loading').hide();
        	 $('#col1').hide();
        	 $('#col2').show();
         });
 </script>
 </head>
<body>
 <c:if test="${tfDto.slotdata.status eq 'reviewed'}">
<div class="card" style="background-color: rgba(255, 0, 0, 0.9); border-radius: 20px; border: 3px solid #f3fff0; width: 80%; margin-left: 10%;">
		<h5 class="card-header info-color white-text text-center py-1">
			<strong style="color: white;">Document Status Was  ${tfDto.slotdata.status}  Modifications Are Not Possible.</strong>
		</h5>
</div>
</c:if>
<div class="card" style="height: 100%;">

<%-- 		<c:if test="${tfDto.slotdata.obstData.status eq 'assigned' or tfDto.slotdata.obstData.status eq 'reassigned' }"> --%>
		<c:url value="/obtemplate/saveOrupdateTemplateFile" var="updateDocUrl" />
		
        <form:form action="${updateDocUrl}" method="POST"  id="updateData">
        	<input type="hidden" name="dynamicFile" id="dynamicFile" value="${tfDto.file}">
			<input type="hidden" name = "studyId" id="studyId" value="${studyId}"/>
			<input type="hidden" name="groupId" id="groupId" value="${groupId}"/>
			<input type="hidden" name="subGroupId" id="subGroupId" value="${subGroupId}"/>
			<input type="hidden" name="subGroupanimalId" id="subGroupanimalId" value="${subGroupanimalId}">
			<input type="hidden" name="templateId" id="templateId" value="${templateId}" />
			<input type="hidden" name="ObservationName" id="ObservationName" value="${ObservationName}" />
			<c:if test="${tfDto.obvt != null}">
				<input type="hidden" name="fileName" id="fileName" value="${tfDto.obvt.obstd.fileName}" />
			</c:if>
			<c:if test="${tfDto.slotdata != null}">
				<input type="hidden" name="fileName" id="fileName" value="${tfDto.slotdata.obstData.fileName}" />
			</c:if>
			<input type="hidden" name="modifidDoc" id="modifidDoc" value="0">
			<input type="hidden" name="status" id="status">
			<div>
		</div>
				<!-- ============ -->
				
<%-- 			<c:if test="${fileDetails.status == 'Draft'}"> --%>
<%-- 			 <c:if test="${role == 'admin' || role == 'SuperAdmin' || role == 'Analyst'}"> --%>
				<span id="messageDiv" style="color: red; font-weight: bold; font-size: 14px;"></span>
				<div id="loading" align="center" style="display: none;">
  					<p><img src="<c:url value="/static/loadingImages/CircleThickBox.gif"/>"/></p>
				</div>
				<table class="table">
					<tr>
						<td id="col1">
							<input type="checkbox" name="condition" id="condition_id">&nbsp;<label for="condition_id">Do you want save modifications please select check box</label></td>
						<td id="col2">
							<input type="button" value="Save Document" onclick="myFunction()" class="btn btn-danger btn-sm" id="savedocument" />
						</td>
					</tr>
				</table>
<%-- 				</c:if> --%>
<%-- 			</c:if> --%>
		</form:form>
		
			<div class="card" style="width: 9%;">
			    <c:if test="${tfDto.slotdata.status ne 'reviewed'}">
						<input id="saveWorkbookBtn" type="button" class="btn btn-danger btn-sm"  value="Save Workbook" onclick="saveWorkbook()">
				</c:if>
			</div>
			<div class="card">
				<div id="result"></div>
				<div id="spreadsheet1" onkeyup="getAllCellValues()" onmouseout="mouseupFucntion()"></div>
			</div>
<%-- 		</c:if>  --%>
	</div>
</body>
<script type="text/javascript">

$(document).ready(function(){
$('#savedocument').fadeOut('slow');
$('#condition_id').change(function(){
if(this.checked)
$('#savedocument').fadeIn('slow');
else
$('#savedocument').fadeOut('slow');
});

});
</script>
<script type="text/javascript">

function myFunction() {
var txt;
var filename = document.getElementById("fileName").value;
// alert(filename);
var fname = filename.split(".");
// var count = document.getElementById("count").value;
// var count1= Number(count)+1;
//alert("Count val is : "+count1);
// $('#countVal').val(count1);
var modifiedoc = $('#modifidDoc').val();
// var statusVal = $('#status_id').val();
// $('#status').val(statusVal);
// alert(modifiedoc+"::"+fname[0]);
if(modifiedoc != ""){
	$("#messageDiv").html("");
// 	$('#updateData').submit();
   $.confirm({
	    title: 'Confirmation',
	    content: 'Do you want to save  this '+fname[0],
	    type: 'orange',
	    typeAnimated: true,
	    buttons: {
	        tryAgain: {
	            text: 'OK',
	            btnClass: 'btn-red',
	            action: function(){
	            	$('#updateData').submit();
	            }
	        },
	        close: function () {
	        }
	    }
	});

}else{
	$("#messageDiv").html("Write Document then Save");
	return false;
}
}
</script>
<script type="text/javascript">
$(document).ready(function(){
$('#hidebtn').click(function(){
$('#tablediv').slideToggle('slow');
});
});
</script>

<script type="text/javascript">
		$( function () {
			if ($("#spreadsheet1").length !== 0) {
				$("#spreadsheet1").igSpreadsheet({
					height: "600",
					width: "100%",
					activeWorksheetChanged: function (evt, ui) {
						if (ui.newActiveWorksheetName == "Statistics") {
							$("#spreadsheet1").igSpreadsheet("option", "activeCell", "E7");
						} else {
							$("#spreadsheet1").igSpreadsheet("option", "activeCell", "J15");
						}
					}
				});
				var workbook = null;
				var str = $("#dynamicFile").val();
// 				alert("str is : "+str);
				var url = "data:text/plain;base64,"+str;
// 				alert("url is : "+url);
				fetch(url)
				  	.then(res => res.arrayBuffer())
    				.then(buffer => {
      				console.log("base64 to buffer: " + new Uint8Array(buffer));
      				var responseArray = new Uint8Array(buffer);
				    $.ig.excel.Workbook.load(responseArray, function () {
						workbook = arguments[0];
						setInitialSettings();
					}, function () {
						console.log("fail");
					})
      				
    			})
				function setInitialSettings() {
					$("#spreadsheet1").igSpreadsheet("option", "workbook", workbook);
					var ws = workbook.worksheets("Table data");
					$("#spreadsheet1").igSpreadsheet("option", "activeWorksheet", ws);
					$("#spreadsheet1").igSpreadsheet("option", "activeCell", "J15");
					$("#spreadsheet1").igSpreadsheet("option", "zoomLevel", "95");
				} 
			}
		});
	</script>

   <script type="text/javascript">
   var finalResult ="";
	function saveWorkbook() {
	$('#modifidDoc').val("");
// 	var extension = $('#extention').val();
	var filename = $('#fileName').val();
	    Dialog.confirm('Do you want to save', 'Confomation', (dlg)=>{
	    	var finalStr ="";
		 	$("#spreadsheet1").igSpreadsheet("option", "workbook")
				.save({ type: 'blob' }, function (data) {
					    var base64data ="";
					    var reader = new window.FileReader();
					    reader.readAsDataURL(data); 
					    reader.onloadend = function() {
		                   base64data = reader.result;                
		                   console.log(base64data );
		                 //  alert("last data is : "+base64data)
						    var url1 = mainUrl+"/obtemplate/saveExcelBolob";
							$.ajax({
					            type: "get",
					            url: url1,
					            data:{ "fileName": filename,"dataStr": base64data},
					            success: function (data) {
					               if(data != ""){
					            	   $('#modifidDoc').val(data);
					            	   $('#loading').hide();
					                   $('#col1').show();
					               }
					            }
					        }); 
		               }
					   
				
				}, function (error) {
					//alert('Error exporting: : ' + error);
					 console.log(error );
				});
	        dlg.close();
	        $('#loading').show();
	   }, (dlg)=>{
	       // alert('Thank you!');
	        dlg.close();
	        $('#loading').hide();
	        
	    });
	} 
	</script>
</html>