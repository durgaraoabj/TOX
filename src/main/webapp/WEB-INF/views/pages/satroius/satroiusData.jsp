<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<body>
<c:url value="/satroius/saveInstrumentWeight" var="saveInstrumentWeightSubmit" />
<sf:form action="${saveInstrumentWeightSubmit}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Weight Reading From</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Animal No :</td>
				<td>
					<select name="animal" class="form-control" id="animals" onchange="getcrfs(this.value)">
						<option value="-1">--Select--</option>
						<c:forEach items="${animals}" var="a">
							<option value="${a.id}">${a.animalNo}</option>	
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Observation :</td>
				<td id="crfDiv">
					<select name="crf" class="form-control" id="crf" onchange="getcrfsElements(this.value)">
						<option value="-1">--Select--</option>
					</select>
					<font color="red" id="crfmsg"></font>
				</td>
			</tr>
			<tr>
				<td>Observation Crf Element :</td>
				<td id="crfEleDiv">
					<select name="crfSecEle" class="form-control" id="crfSecEle" onchange="getcrfsElements(this.value)">
						<option value="-1">--Select--</option>
					</select>
					<font color="red" id="crfmsg"></font>
				</td>
			</tr>
			<tr>
				<td>Weight (gm) </td>
				<td><input type="text" name="weight" id="weight"  class="form-control"/>
				
				<input type="button" value="Clear"  class="btn btn-primary" onclick="clearData1()"/></td>
			</tr>
			<tr>
				<td><input type="button" value="Submit" class="btn btn-primary" onclick="submitForm()"/></td>
				<td><input type="button" value="Reset" class="btn btn-primary" onclick="reset()"/></td>
			</tr>
			
		</table>
	</div>
</div>
</sf:form>
</body>
<script type="text/javascript">
function submitForm(){
	$("#formsumit").submit();
}
</script>
<script type="text/javascript">	
function clearData1(){
	$("#weight").val("");
}
</script>
<script type="text/javascript">	

function getcrfs(value){
	if(value != -1){
		var result = asynchronousAjaxCall(mainUrl+"/satroius/getConfgiuredCrfs/"+value);
		if(result != '' || result != 'undefined'){
			$('#crfDiv').html(result);
		}else
			$('#crfDiv').html('<select name="crf" class="form-control" id="crf" onchange="getcrfsElements(this.value)"><option value="-1">--Select--</option></select><font color="red" id="crfmsg"></font>');
	}else{
		$('#crfDiv').html('<select name="crf" class="form-control" id="crf" onchange="getcrfsElements(this.value)"><option value="-1">--Select--</option></select><font color="red" id="crfmsg"></font>');
	}	
}
</script>
<script type="text/javascript">	
function getcrfsElements(value){
	if(value != -1){
		var result = asynchronousAjaxCall(mainUrl+"/satroius/getCrfElements/"+value);
		if(result != '' || result != 'undefined'){
			$('#crfEleDiv').html(result);
		}else
			$('#crfEleDiv').html('');
	}else{
		$('#crfEleDiv').html('');
	}	
}
</script>

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
		function addBlock(id, dateAndTime, batchNo, nozzleNo,
				grossWt, tareWt, netWt,
				status, dataFrom, ipAddress){
			if(id != '0'){
// 				alert(netWt)
				var data = netWt;
				$("#weight").val(data)
				
				
			}

		}
		
		
	</script>
</html>
