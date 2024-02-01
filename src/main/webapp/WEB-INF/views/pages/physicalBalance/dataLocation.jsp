<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<input type="hidden" id="datavalue" value="${datavalue}"/>
	<input type="hidden" id="mainUrl" value="${mainUrl}"/>
	 <c:url value="/radwag/dataLocationSave" var="vollform"/>
	<form:form action="${vollform}" method="post" id="submitform" >
		<table>
			<TR><TD colspan="3"><b>From LAN (Ethernet)</b></TD></TR>
			<tr>
				<td>Select Ip Address:</td>
				<td>
					<select name="insturment" id="insturment">
						<option value="-1">--Select--</option>
						<option value="1">IP: 192.168.0.159   PORT : 4001</option>
						<option value="2">IP: 192.168.0.55   PORT : 4001</option>
					</select>
				</td>
				<td>
					<input type="submit" value="Submit" onclick="submitted()"/>
				</td>
			</tr>
			
			<TR><TD colspan="3"><b>RS232 to USB</b></TD></TR>
			<tr>
				<td>Select Ip Address:</td>
				<td>
					<select name="insturment2" id="insturment2">
						<option value="-1">--Select--</option>
						<option value="1">IP: 192.168.0.159   PORT : 4001</option>
					</select>
				</td>
				<td>USB PORT NAME ;</td>
				<td>
					<input type="text" name="portName" id="portName"/>
 				</td>
				
				<td>
					<input type="submit" value="Submit"/>
				</td>
			</tr>
		</table>
	</form:form>
	<div id="currentData">
	
	</div>
</body>
<script type="text/javascript">
		$(document).ready(function() {
			var url = mainUrl+"/testWebSocket/connect";
			var eventSource = new EventSource(url);
			eventSource.addEventListener("insData", function(event){
				var articalData = JSON.parse(event.data);
				console.log("Event : " + event.data);
// 				alert(articalData.id +" - "+ articalData.collected +" - "+ articalData.color)
				addBlock(articalData.id, articalData.dateAndTime, articalData.batchNo, articalData.nozzleNo,
						articalData.grossWt, articalData.tareWt, articalData.netWt,
						articalData.status, articalData.dataFrom, articalData.ipAddress)
			});
		});
		function addBlock(id, dateAndTime, batchNo, nozzleNo, grossWt, tareWt, netWt, status, dataFrom, ipAddress){
			if(id != '0'){
				var data = "<table><tr><th></th><th></th></tr>";
				data = data + "<tr><TD>Date And Time</TD><TD>"+dateAndTime+"</TD></TR>";
				data = data + "<tr><TD>Batch No</TD><TD>"+batchNo+"</TD></TR>";
				data = data + "<tr><TD>Nozzle No</TD><TD>"+nozzleNo+"</TD></TR>";
				data = data + "<tr><TD>Gross Wt</TD><TD>"+grossWt+"</TD></TR>";
				data = data + "<tr><TD>tareWt</TD><TD>"+tareWt+"</TD></TR>";
				data = data + "<tr><TD>netWt</TD><TD>"+netWt+"</TD></TR>";
				data = data + "<tr><TD>status</TD><TD>"+status+"</TD></TR>";
				data = data + "</table>";
				$("#currentData").html(data)
				
				
			}

		}
		
		
	</script>
<script type="text/javascript">
	var count=-1;
// 	$(document).ready(function(){
// 		var datavalue = $("#datavalue").val();
// 		if(datavalue == '1'){
// 			SDF
// 			while(true){
// //	 			alert(count)
// 				var mainUrl = $("#mainUrl").val();
// //	 			alert(mainUrl+"/radwag/currentData/"+count);
// 				var result = asynchronousAjaxCall(mainUrl+"/radwag/currentData/"+count);
				
// //	 			alert(result);
// 				if(result != '' || result != 'undefined'){
// 					if(result.trim() != count){
// 						$("#currentData").html(result);
// 						count++;
// 					}
// 				}
// 			}
// 		}
			
		
// 	});
	
	

</script>
</html>