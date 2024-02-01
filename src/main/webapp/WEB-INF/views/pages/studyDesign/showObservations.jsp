<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isErrorPage="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <script src='<c:url value="/static/css/popper.min.js"/>'></script> 
  <script type="text/javascript">
  $(document).ready(function(){
	  $("#tcDiv").hide();
  });
  </script>
 </head>

<body>
<%-- <input type="hidden" value="${gender}" id="gender"/> --%>
<%-- <input type="hidden" value="${ipaddr}" id="ipaddr"/> --%>
<%-- <input type="hidden" value="${labName}" id="labName"/> --%>
<%-- <input type="hidden" value="${regType}" id="regType"/> --%>
<%-- <input type="hidden" value="${docId}" id="docId"/> --%>
<%-- <input type="hidden" value="${cusId}" id="cusId"/> --%>
<table class="table table-bordered table-striped">
	<tr><th>Selected Observations :</th></tr>
	<tr><td>
		<input type="hidden" name="selectedtcs" id="selectedtcs" value="0">
		<input type ="hidden" name="tcPopupFlag" id="tcPopupFlag" value="true"/>
		<textarea rows="4" cols="90" name="selectedtcsNames" id ="selectedtcsNames" readonly="readonly"></textarea>
		<font id="selectedtcsmsg" color="red"></font>
	</td></tr>
</table>
<div id="tcDupicateMsg" style="color: red;"></div>
<table class="table table-bordered table-striped">
	<tr>
		<td>
			<input type="radio" name="name" id="typetoshow002" value="tcDiv" onchange="showhide(this.value)">Observation
						
						
								
		</td>
	</tr>
</table>
<div align="center"  id="button0">
	<input type="button" value="SELECT" name="selectId0" id="selectId0" class="btn btn-warning btn-sm" onclick="selectedTestCodes()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="CANCEL" name="cancelId" onclick="closebox()" id="cancelId" class="btn btn-warning btn-sm"  data-dismiss="modal"/>
</div>
<div id="testCodeSearch">
     <input class="form-control" id="myInput22" type="text" placeholder="Search Observation">
</div>
<div style="position: inherit; overflow-Y : scroll; height: 800px;">
<div id="tcDiv">
<!-- 	<h3>Test Codes</h3> -->
	<table  class="table table-bordered table-striped">
	<thead>
	    <tr class="header" style="background-color: #07B4AB">
	    	<th></th>
		    <th>Observation</th>
<!-- 		    <th>Group </th> -->
<!-- 	        <th>Sub Group</th> -->
	  	</tr>
	 </thead>
	 <tbody id="myTable2">
	 	  <c:forEach items="${crfIds}" var="crf">
	 	  		<tr>
					<th><input type="checkbox" value="${crf.value.id}##${crf.value.observationName}" id="tcChechBox" class="testcheckbox" onchange="selectedProfiles()"/></th>
					<td>${crf.value.observationName}</td>
<%-- 					<td>${crf.subGroupInfo.name}</td> --%>
				</tr>
		  </c:forEach>
	 </tbody>
	</table>
	
	
</div>
<script>
$(document).ready(function(){
  $("#myInput22").on("keyup", function() {
    var value = $(this).val().toLowerCase();
    $("#myTable2 tr").filter(function() {
        $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
      });
    });
});
</script>

<div align="center" id="button1">
	<input type="button" value="SELECT" name="selectId" id="selectId" class="btn btn-warning btn-sm" onclick="selectedTestCodes()" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="CANCEL" name="cancelId" onclick="closebox()" id="cancelId" class="btn btn-warning btn-sm"  data-dismiss="modal"/>
</div>

</div>
<script>
$("#tcDiv").hide();
$("#button0").hide();
$("#button1").hide();
$("#testCodeSearch").hide();
function showhide(value){
	if(value == "tcDiv"){
		$("#tcDiv").show();
		$("#testCodeSearch").show();
		$("#button0").show();
	}
	$("#button1").show();
}

function selectedProfiles(){
	$('#tcPopupFlag').val("true");
	$('#selectedtcs').val("0");
	$("#selectedtcsNames").val("");
	var ipAddr = $('#ipaddr').val();
	var regType = $('#regType').val();
	var type ="";
	
	var tcIds = [];
	var tcNames =[];
	var finalNames =[];
	
 	//TestCodes
 	$("input:checkbox[class=testcheckbox]:checked").each(function () { 
			var value = $(this).val();
			var temp = value.split("##");
			tcIds.push(temp[0]);
			tcNames.push(temp[1]);
 	});
 	if(tcIds.length > 0 && tcIds !="0"){
 		 $('#tcDupicateMsg').html("");
		 $('#tcPopupFlag').val("true");
		 $('#selectedtcs').val(tcIds);
//			 finalNames = tcNames.slice();
		$.each(tcNames, function( index, value ) {
			finalNames.push(value);
		});
		 $("#selectedtcsNames").val(finalNames);
	}

}


function selectedTestCodes(){
	$("#selectedtcsmsg").html("");
	var obsercationIds = $("#selectedtcs").val().trim();
// 	alert(obsercationIds == '0')
	if(obsercationIds == null || obsercationIds == '0' || obsercationIds =="undefined"){
		$("#selectedtcsmsg").html("Select Atleast on Observation.");	
		$('#tcPopupFlag').val("false");
	}else{
		var tcFlag = $("#tcPopupFlag").val();
		if(tcFlag == "true"){
			$("#selectedtcsmsg").html("");
			if(obsercationIds == "")	obsercationIds = "0";
			
			$("#crfIds").val(obsercationIds);
			$("#submitform0").submit();
			
			$("#closebutton").click();
			$("#closebutton").mousedown();
			$("#closebutton").mouseup();
		}
	}
}
</script>

</body>
</html>
