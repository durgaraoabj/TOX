<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sample Collection Form</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			
			<tr>
				<td>Scan Barcode</td>
				<td>
					<input type="text" name="barcode" id="barcode" onchange="barcodevalue(this.value)"/>
					<font style="color: red" id="barcodeMsg"></font>
				</td>
			</tr>
			<tr><td>Lab Technician</td><td id="labTechMsg"></td></tr>
			<tr><td>Vacutainer </td><td id="vacutainer1Msg"></td></tr>
			<tr><td>Vacutainer After</td><td id="vacutainer2Msg"></td></tr>
			<tr><td>Subjct</td><td id="subjectMsg"></td></tr>
			<tr><td>Message</td><td><input type="text" name="message0" id="message0"/></td></tr>
			<tr><td colspan="2"><input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
	</div>
</div>
<c:url value="/study/clinical/stdClinicalSampleCollectionSave" var="stdClinicalSampleCollectionSave" />
<sf:form action="${stdClinicalSampleCollectionSave}" method="POST"  id="formsumit" >
	<input type ="hidden" name="activeStudyNo" id="activeStudyNo" value="${activeStudyNo}"/><br/>
	<input type ="hidden" name="studybarcodeId" id="studybarcodeId" value="${studybarcodeId}"/><br/>
	<input type ="hidden" name="LabTechBacode" id="LabTechBacode" value="${LabTechBacode}"/><br/>
	labTech<input type ="text" name="labTech" id="labTech"/><br/>
	labTechTime<input type ="text" name="labTechTime" id="labTechTime"/><br/>
	labTechTime<input type ="text" name="vacutainer1" id="vacutainer1"/>
	vacutainer1Time<input type ="text" name="vacutainer1Time" id="vacutainer1Time"/>
	vacutainer2<input type ="text" name="vacutainer2" id="vacutainer2"/>
	vacutainer2Time<input type ="text" name="vacutainer2Time" id="vacutainer2Time"/>
	subject<input type ="text" name="subject" id="subject"/><br/>
	subjectTime<input type ="text" name="subjectTime" id="subjectTime"/><br/>
	message<input type ="text" name="message" id="message"/><br/>
</sf:form>

<script>
$("#barcode").on('input',function(e){
	var today = new Date();
	var h = today.getHours() + "";
	var m = today.getMinutes() + "";
	if(h.length == 1) h = "0" + h;
	if(m.length == 1) m = "0" + m;
	var time = h + ":" + m;
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
					$("#labTechTime").val(time);
				}
			}else{
				$("#labTechMsg").html("");
				$("#labTech").val("");
				$("#labTechTime").val("");
				$("#barcodeMsg").html("Lab Technician not belongs to Current Study : " + $("#activeStudyNo").val());
				
			}
			$("#vacutainer1Msg").html("");
			$("#vacutainer1").val("");	
			$("#vacutainer1Time").val("");	
			$("#vacutainer2Msg").html("");
			$("#vacutainer2").val("");	
			$("#vacutainer2Time").val("");	
			$("#subjectMsg").html("");
			$("#subject").val("");
			$("#subjectTime").val("");
		}else if(prefix == "04"){//vacutainer
			if($("#vacutainer1").val() == ""){
				if($("#labTech").val() != ''){
					var study = barcode.substring(2, 8);
					if(study == $("#studybarcodeId").val()){
						var labPeriod = $("#labTech").val().substring(8, 9);
						var period = barcode.substring(8, 9);
// 						if(labPeriod == period){
							
							var lbatch = $("#labTech").val().substring(9, 10);
							var vbatch = barcode.substring(13,14);
							var subject = $("#subject").val().substring(9, 12);
							var vacutainerSub = barcode.substring(9, 12);
							if(subject == vacutainerSub){
								var result = asynchronousAjaxCall(mainUrl+"/study/clinical/vacutainerBarcodeInfo/"+barcode);
								if(result != '' || result != 'undefined'){
									$("#vacutainer1Msg").html(result);
									$("#vacutainer1").val(barcode);
									$("#vacutainer1Time").val(time);
								}								
							}else{
								$("#vacutainer1Msg").html("");
								$("#vacutainer1").val("");
								$("#vacutainer1Time").val("");
								$("#barcodeMsg").html("Vacutainer And Subject Not matched");
							}
// 						}else{
// 							$("#vacutainer1Msg").html("");
// 							$("#vacutainer1").val("");
// 							$("#vacutainer1Time").val("");
// 							$("#barcodeMsg").html("Lab Technician period and Vacutainer Period Not Matched");
// 						}
					}else{
						$("#vacutainer1Msg").html("");
						$("#vacutainer1").val("");
						$("#vacutainer1Time").val("");	
						$("#vacutainer2Msg").html("");
						$("#vacutainer2").val("");	
						$("#vacutainer2Time").val("");	
						$("#subjectMsg").html("");
						$("#subject").val("");
						$("#subjectTime").val("");
						$("#barcodeMsg").html("Vacutainer not belongs to Current Study : " + $("#activeStudyNo").val());
					}
				}else{
					$("#vacutainer1Msg").html("");
					$("#vacutainer1").val("");
					$("#vacutainer1Time").val("");	
					$("#vacutainer2Msg").html("");
					$("#vacutainer2").val("");	
					$("#vacutainer2Time").val("");	
					$("#subjectMsg").html("");
					$("#subject").val("");
					$("#subjectTime").val("");
					$("#barcodeMsg").html("Lab Technician Required");
				}				
			}else{
				if($("#vacutainer1").val() == barcode){
					if($("#subject").val() != ""){
						$("#vacutainer2Msg").html($("#vacutainer1Msg").html());
						$("#vacutainer2").val($("#vacutainer1").val());			
						$("#vacutainer2Time").val(time);
					}else{
						$("#barcodeMsg").html("Subject Barcodes Required");
					}

				}else{
					$("#vacutainer2Msg").html("");
					$("#vacutainer2").val("");			
					$("#vacutainer2Time").val("");
					$("#barcodeMsg").html("Vacutainer After Barcodes Not Matched With current Vacutainer");
				}
			}

			
		}else if(prefix == "01"){//subject
			if($("#vacutainer1").val() != ''){
				var subject = barcode.substring(9, 12);
				var vacutainer1 = $("#vacutainer1").val().substring(9, 12);
				if(subject == vacutainer1){
					var result = asynchronousAjaxCall(mainUrl+"/study/clinical/subjectBarcodeInfo/"+barcode);
					if(result != '' || result != 'undefined'){
						$("#subjectMsg").html(result);
						$("#subject").val(barcode);
						$("#subjectTime").val(time);
					}
				}else{
					$("#subjectMsg").html("");
					$("#subject").val("");
					$("#subjectTime").val("");
					$("#barcodeMsg").html("Vacutainer And Subject Not matched");
				}
				$("#vacutainer2Msg").html("");
				$("#vacutainer2").val("");			
				$("#vacutainer2Time").val("");
			}else{
				$("#subjectMsg").html("");
				$("#subject").val("");
				$("#subjectTime").val("");
				$("#barcodeMsg").html("Vacutainer Barcodes Required");
			}
			
		}
		$("#barcode").val("");
	}
});
function submitForm(){
	if($("#labTech").val() != '' && $("#vacutainer1").val() != '' && $("#vacutainer2").val() != '' && $("#subject").val() != ''){
		$("#message").val($("#message0").val());
		$("#formsumit").submit();	
	}else{
		$("#barcodeMsg").html("Please scan all Barcodes");
	}
	
		
}
</script>