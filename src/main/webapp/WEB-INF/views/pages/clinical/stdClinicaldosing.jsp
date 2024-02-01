<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="card">
	<div class="card-header">
		<h3 class="card-title">Dosing Form</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			
			<tr>
				<td>Scan Barcode</td>
				<td>
<!-- 					<input type="text" name="barcode" id="barcode" onchange="barcodevalue(this.value)" /> -->
						<input type="text" name="barcode" id="barcode" />
					<font style="color: red" id="barcodeMsg"></font>
				</td>
			</tr>
			<tr><td>Lab Technician</td><td id="labTechMsg"></td></tr>
			<tr><td>Sachet </td><td id="sachetMsg"></td></tr>
			<tr><td>Subject</td><td id="subjectMsg"></td></tr>
			<tr><td>Message</td><td><input type="text" name="message0" id="message0"/></td></tr>
			<tr><td colspan="2"><input type="button" value="Dose" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
	</div>
</div>
<c:url value="/study/clinical/stdClinicaldosingSave" var="stdClinicaldosingSave" />
<sf:form action="${stdClinicaldosingSave}" method="POST"  id="formsumit" >
	<input type ="hidden" name="activeStudyNo" id="activeStudyNo" value="${activeStudyNo}"/><br/>
	<input type ="hidden" name="studybarcodeId" id="studybarcodeId" value="${studybarcodeId}"/><br/>
	<input type ="hidden" name="LabTechBacode" id="LabTechBacode" value="${LabTechBacode}"/><br/>
	<input type ="hidden" name="labTech" id="labTech"/><br/>
	<input type ="hidden" name="sachet" id="sachet"/><br/>
	<input type ="hidden" name="subject" id="subject"/><br/>
	<input type ="hidden" name="message" id="message"/><br/>
</sf:form>

<script>
  
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
		}else if(prefix == "03"){//sachet
			if($("#labTech").val() != ''){
				var study = barcode.substring(2, 8);
				if(study == $("#studybarcodeId").val()){
					var labPeriod = $("#labTech").val().substring(8, 9)
					var period = barcode.substring(8, 9);
					if(labPeriod == period){
						var result = asynchronousAjaxCall(mainUrl+"/study/clinical/sachetBarcodeInfo/"+barcode);
						if(result != '' || result != 'undefined'){
							$("#sachetMsg").html(result);
							$("#sachet").val(barcode);
						}										
					}else{
						$("#sachetMsg").html("");
						$("#sachet").val("");
						$("#barcodeMsg").html("Lab Technician period and Sachet Period Not Matched");
					}
				}else{
					$("#sachetMsg").html("");
					$("#sachet").val("");
					$("#barcodeMsg").html("Sachet not belongs to Current Study : " + $("#activeStudyNo").val());
				}				
			}else{
				$("#sachetMsg").html("");
				$("#sachet").val("");
				$("#barcodeMsg").html("Lab Technician Required");
			}
		}else if(prefix == "01"){//subject
			if($("#sachet").val() != ''){
				var subject = barcode.substring(9, 12);
				var sachet = $("#sachet").val().substring(9, 12);
				if(subject == sachet){
					var result = asynchronousAjaxCall(mainUrl+"/study/clinical/subjectBarcodeInfo/"+barcode);
					if(result != '' || result != 'undefined'){
						$("#subjectMsg").html(result);
						$("#subject").val(barcode);
					}					
				}else{
					$("#subjectMsg").html("");
					$("#subject").val("");
					$("#barcodeMsg").html("Sachet And Subject Not matched");
				}

			}else{
				$("#subjectMsg").html("");
				$("#subject").val("");
				$("#barcodeMsg").html("Sachet Barcodes Required");
			}
			
		}
		$("#barcode").val("");
	}
});
function barcodevalue(barcode){
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
		}else if(prefix == "03"){//sachet
			if($("#labTech").val() != ''){
				var study = barcode.substring(2, 8);
				if(study == $("#studybarcodeId").val()){
					var labPeriod = $("#labTech").val().substring(8, 9)
					var period = barcode.substring(8, 9);
					if(labPeriod == period){
						var result = asynchronousAjaxCall(mainUrl+"/study/clinical/sachetBarcodeInfo/"+barcode);
						if(result != '' || result != 'undefined'){
							$("#sachetMsg").html(result);
							$("#sachet").val(barcode);
						}										
					}else{
						$("#sachetMsg").html("");
						$("#sachet").val("");
						$("#barcodeMsg").html("Lab Technician period and Sachet Period Not Matched");
					}
				}else{
					$("#sachetMsg").html("");
					$("#sachet").val("");
					$("#barcodeMsg").html("Sachet not belongs to Current Study : " + $("#activeStudyNo").val());
				}				
			}else{
				$("#sachetMsg").html("");
				$("#sachet").val("");
				$("#barcodeMsg").html("Lab Technician Required");
			}
		}else if(prefix == "01"){//subject
			if($("#sachet").val() != ''){
				var subject = barcode.substring(9, 12);
				var sachet = $("#sachet").val().substring(9, 12);
				if(subject == sachet){
					var result = asynchronousAjaxCall(mainUrl+"/study/clinical/subjectBarcodeInfo/"+barcode);
					if(result != '' || result != 'undefined'){
						$("#subjectMsg").html(result);
						$("#subject").val(barcode);
					}					
				}else{
					$("#subjectMsg").html("");
					$("#subject").val("");
					$("#barcodeMsg").html("Sachet And Subject Not matched");
				}

			}else{
				$("#subjectMsg").html("");
				$("#subject").val("");
				$("#barcodeMsg").html("Sachet Barcodes Required");
			}
			
		}
		$("#barcode").val("");
	}
}

function submitForm(){	
	if($("#labTech").val() != '' && $("#sachet").val() != '' && $("#subject").val() != ''){
		$("#message").val($("#message0").val());
		$("#formsumit").submit();	
	}else{
		$("#barcodeMsg").html("Please scan all Barcodes");
	}
		
}
</script>