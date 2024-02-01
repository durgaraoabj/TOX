<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="card">
	<div class="card-header">
		<h3 class="card-title">Vital Collection Form</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			
			<tr>
				<td>Scan Barcode</td>
				<td><input type="text" name="barcode" id="barcode" onchange="barcodevalue(this.value)"/><font color="red" id="barcodeMsg"></font> </td>
			</tr>
			<tr><td>Lab Technician</td><td id="labTechMsg"></td></tr>
			<tr><td>Subject </td><td id="subjectMsg"></td></tr>
			<tr>
				<td>Select Time Point</td>
				<td id="vitalInfo"></td>
			</tr>
			<tr>
				<td id="vitalTestInfo" colspan="2"></td>
			</tr>
			<tr><td colspan="2"><input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
	</div>
</div>
<c:url value="/study/clinical/stdClinicalVitalCollectionSave" var="stdClinicalVitalCollectionSave" />
<sf:form action="${stdClinicalVitalCollectionSave}" method="POST"  id="formsumit" >
	<input type ="hidden" name="activeStudyNo" id="activeStudyNo" value="${activeStudyNo}"/><br/>
	<input type ="hidden" name="studybarcodeId" id="studybarcodeId" value="${studybarcodeId}"/><br/>
	<input type ="hidden" name="LabTechBacode" id="LabTechBacode" value="${LabTechBacode}"/><br/>
	<input type ="hidden" name="labTech" id="labTech"/><br/>
	<input type ="hidden" name="subject" id="subject"/><br/>
	<input type ="hidden" name="timePoint" id="timePoint"/><br/>
	<input type ="hidden" name="pulseRate" id="pulseRate"/><br/>
	<input type ="hidden" name="oralTemp" id="oralTemp"/><br/>
	<input type ="hidden" name="bp" id="bp"/><br/>
	<input type ="hidden" name="respiratoryRate" id="respiratoryRate"/><br/>
	<input type ="hidden" name="wellbeingAscertained" id="wellbeingAscertained"/><br/>
</sf:form>

<script>
var pulseRatefalg = true;
var oralTempfalg = true;
var bpfalg = true;
var respiratoryRatefalg = true;
var wellbeingAscertainedfalg = true;
var vacutainer = "";
var vacutainermsg = ""; 
$("#barcode").on('input',function(e){
	var barcode = $("#barcode").val();
	$("#barcodeMsg").html("");
	var length = barcode.length;
	if(length == 21){
		var prefix = barcode.substr(0,2);
		if(prefix == "02"){//lab tech
			$("#labTechMsg").html("");
			$("#labTech").val("");
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
		}else if(prefix == "01"){//subject
			$("#subjectMsg").html("");
			$("#subject").val("");
			if($("#labTech").val() != ''){
				var subject = barcode.substring(2, 9);
				var labTech = $("#labTech").val().substring(2, 9);
				if(labTech == subject){
					var result = asynchronousAjaxCall(mainUrl+"/study/clinical/subjectBarcodeInfo/"+barcode);
					if(result != '' || result != 'undefined'){
						$("#subjectMsg").html(result);
						$("#subject").val(barcode);
						result = asynchronousAjaxCall(mainUrl+"/study/clinical/vitalTimePointsWithSuejctBarcode/"+barcode)
						if(result != '' || result != 'undefined'){
							$("#vitalInfo").html(result);
						}
					}
				}else{
					$("#subjectMsg").html("");
					$("#subject").val("");
					$("#barcodeMsg").html("Lab Technician  And Subject Not matched");
				}
				
			}else{
				$("#subjectMsg").html("");
				$("#subject").val("");
				$("#barcodeMsg").html("Lab Technician Barcodes Required");
			}
			
		}
		$("#barcode").val("");
	}
});
function vitalTestInfo(cvtppId){
	$("#vitalTestInfo").html("");
	$("#timePoint").val("");
	if(cvtppId != -1){
		var result = asynchronousAjaxCall(mainUrl+"/study/clinical/vitalTestInfo/"+cvtppId);
		if(result != '' || result != 'undefined'){
			$("#vitalTestInfo").html(result);
			$("#timePoint").val(cvtppId);
		}
	}
}
function submitForm(){
	if($("#labTech").val() != '' && $("#subject").val() != ''){
		if($("#timePoint").val() != ''){
			
			alert(pulseRatefalg +" : "+ oralTempfalg+" : "+ bpfalg+" : "+ respiratoryRatefalg+" : "+ wellbeingAscertainedfalg);
			if(pulseRatefalg && oralTempfalg && bpfalg && respiratoryRatefalg && wellbeingAscertainedfalg){
				$("#pulseRate").val($("#pulseRate0").val());
				$("#oralTemp").val($("#oralTemp0").val());
				$("#bp").val($("#bp0").val());
				$("#respiratoryRate").val($("#respiratoryRate0").val());
				$("#wellbeingAscertained").val($("#wellbeingAscertained0").val());
				$("#formsumit").submit();									
			}else{
				if(!pulseRatefalg) $("#pulseRate0Msg").html("Pattren Faild.");
				if(!oralTempfalg) $("#oralTemp0Msg").html("Pattren Faild.");
				if(!bpfalg) $("#bp0Msg").html("Pattren Faild.");
				if(!respiratoryRatefalg) $("#respiratoryRate0Msg").html("Pattren Faild.");
				if(!wellbeingAscertainedfalg) $("#wellbeingAscertained0Msg").html("Pattren Faild.");
			}

		}else{
			$("#barcodeMsg").html("Please, Select Time Point");
		}

	}else{
		$("#barcodeMsg").html("Please scan all Barcodes");
	}
	
	
}

</script>