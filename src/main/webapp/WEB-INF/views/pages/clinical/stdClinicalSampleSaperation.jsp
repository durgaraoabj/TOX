<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sample Separation Form</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			
			<tr>
				<td>Scan Barcode</td>
				<td>
				<input type="text" name="barcode" id="barcode"/>
				<font style="color: red" id="barcodeMsg"></font>
				 </td>
			</tr>
			<tr><td>Lab Technician</td><td id="labTechMsg"></td></tr>
			<tr><td>Vacutainer </td><td id="vacutainer1Msg"></td></tr>
			<c:forEach begin="1" end="${clinical.noOfVials}" step="1" var="count">
				<tr><td>Vial-${count}</td><td id="vial${count}Msg"></td></tr>
			</c:forEach>
			<tr><td colspan="2"><input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
	</div>
</div>
<c:url value="/study/clinical/stdClinicalSampleSaperationSave" var="stdClinicalSampleSaperationSave" />
<sf:form action="${stdClinicalSampleSaperationSave}" method="POST"  id="formsumit" >
	<input type ="hidden" name="activeStudyNo" id="activeStudyNo" value="${activeStudyNo}"/><br/>
	<input type ="hidden" name="studybarcodeId" id="studybarcodeId" value="${studybarcodeId}"/><br/>
	<input type ="hidden" name="LabTechBacode" id="LabTechBacode" value="${LabTechBacode}"/><br/>
	<input type ="hidden" name="labTech" id="labTech"/><br/>
	<input type ="hidden" name="vacutainer1" id="vacutainer1"/><br/>
	<input type ="hidden" name="vacutainertemp" id="vacutainertemp"/><br/>
	<input type="hidden" name = "noOfVials" id="noOfVials" value="${clinical.noOfVials}"/>
	<c:forEach begin="1" end="${clinical.noOfVials}" step="1" var="count">
		<input type ="hidden" name="vial${count}" id="vial${count}"/><br/>
	</c:forEach>
	
</sf:form>

<script>
var vacutainer = "";
var vacutainermsg = ""; 
$("#barcode").on('input',function(e){
	var barcode = $("#barcode").val();
	$("#barcodeMsg").html("");
	var length = barcode.length;
	if(length == 21){
		var prefix = barcode.substr(0,2);
		if(prefix == "02"){//lab tech
			var study = barcode.substring(2, 8);
			if(study == $("#studybarcodeId").val()){
				var result = asynchronousAjaxCall(mainUrl+"/study/clinical/labTechBarcodeInfo/"+barcode);
				if(result != '' || result != 'undefined'){
					$("#labTechMsg").html(result);
					$("#labTech").val(barcode);
				}
			}else{
				$("#labTechMsg").html("");
				$("#labTech").val("");
				$("#barcodeMsg").html("Lab Technician not belongs to Current Study : " + $("#activeStudyNo").val());
			}
			$("#barcode").val("");
		}else if(prefix == "04"){//vacutainer
			if($("#labTech").val() != ''){
				var labPeriod = $("#labTech").val().substring(8, 9)
				var period = barcode.substring(8, 9);
// 				if(labPeriod == period){
					vacutainer = "";
					vacutainermsg = "";
					var noOfVials = $("#noOfVials").val();
					for(var i = 1; i<=noOfVials; i++){
						$("#vial"+i+"Msg").html("");
						$("#vial"+i).val("");
					}
					var result = asynchronousAjaxCall(mainUrl+"/study/clinical/vacutainerBarcodeInfo/"+barcode);
					if(result != '' || result != 'undefined'){
						$("#vacutainer1Msg").html(result);
						vacutainermsg = result;
						$("#vacutainer1").val(barcode);
						vacutainer = barcode.substr(02,18);
						$("#vacutainertemp").val(vacutainer);
					}
// 				}else{
// 					$("#vacutainer1Msg").html("");
// 					$("#vacutainer1").val("");
// 					$("#vacutainertemp").val("");
// 					vacutainer = "";
// 					$("#barcodeMsg").html("Lab Technician period and Vacutainer Period Not Matched");
// 				}
			}else{
				$("#vacutainer1Msg").html("");
				$("#vacutainer1").val("");
				$("#vacutainertemp").val("");
				vacutainer = "";
				$("#barcodeMsg").html("Lab Technician Required");
			}
		
			$("#barcode").val("");
		}else{
			$("#barcodeMsg").html("In-Valied Barcode");
		}
// 		else if(prefix == "05"){//Vial
// 			var vial = barcode.substr(02,18);
// 			var endChar = barcode.substr(20,21);
			
// 			if($("#vacutainer1").val() != ''){
// 				if(vacutainer == ""){
// 					$("#barcodeMsg").html("Please, scan vacutainer First");
// 				}else{
// 					if(vacutainer ==  vial){
// 						$("#vial"+endChar+"Msg").html(vacutainermsg + ", Vial-"+endChar);
// 						$("#vial"+endChar).val(vial);
// 					}else{
// 						$("#vial"+endChar+"Msg").html("");
// 						$("#vial"+endChar).val("");
// 						$("#barcodeMsg").html("Vial-"+endChar + " is not matched With Vacutainer");
// 					}
					
// 				}
// 			}else{
// 				$("#vial"+endChar+"Msg").html("");
// 				$("#vial"+endChar).val("");
// 				$("#barcodeMsg").html("Vacutainer Barcodes Required");
// 			}
			
// 		}
		
	}else if(length == 22){
		var prefix = barcode.substr(0,2);
		if(prefix == "05"){//Vial
			var vial = barcode.substr(02,18);
			var endChar = barcode.substr(20,1);
			if(vacutainer == ""){
				$("#barcodeMsg").html("Please, scan vacutainer First");
			}else{
				if(vacutainer ==  vial){
					$("#vial"+endChar+"Msg").html(vacutainermsg + ", Vial-"+endChar);
					$("#vial"+endChar).val(vial);
				}else{	
					$("#vial"+endChar+"Msg").html("");
					$("#vial"+endChar).val("");
					$("#barcodeMsg").html("Vial-"+endChar + " is not matched With Vacutainer");
				}
			}
		}
		$("#barcode").val("");
	}
	if(length > 22)	$("#barcode").val("");
});
function submitForm(){
	if($("#labTech").val() != '' && $("#vacutainer1").val()){
		var flag = true;
		for(var i=1; i<=$("#noOfVials").val(); i++){
			if($("#vial"+i).val() == ''){
				flag = false;
			}
		}
		if(flag){
			$("#formsumit").submit();				
		}else
			$("#barcodeMsg").html("Please scan all Barcodes");

	}else{
		$("#barcodeMsg").html("Please scan all Barcodes");
	}
}
</script>