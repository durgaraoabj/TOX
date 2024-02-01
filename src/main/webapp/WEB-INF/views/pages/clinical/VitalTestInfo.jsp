<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<table class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Test Name</th>
                	<th>Value</th>
                </tr>
                </thead>
                <tbody>
                	<c:if test="${testinfo.pulseRate == 'Yes' }">
                		<c:if test="${testinfo.pulseRatePattren != '' }">
                			<tr><td>Pulse Rate</td>
                			<td><input type="text" id="pulseRate0" placeholder="${testinfo.pulseRatePattren}" 
                				onchange="pattrenValidation('${testinfo.pulseRatePattren}', 'pulseRate0Msg', this.value, 'text' , 'pulseRate')">
	                			<font color="red" id="pulseRate0Msg"> </font></td></tr>
                		</c:if>
                		<c:if test="${testinfo.pulseRatePattren == '' }"><tr><td>Pulse Rate</td><td><input type="text" id="pulseRate0"></td></tr></c:if>
                	</c:if>
                	<c:if test="${testinfo.pulseRate == 'No' }"><input type="hidden" id="pulseRate0" value="-"></c:if>
                	
                	<c:if test="${testinfo.oralTemp == 'Yes' }">
                		<c:if test="${testinfo.oralTempPattren != '' }">
                			<tr><td>Oral Temperature</td>
                			<td><input type="text" id="oralTemp0" placeholder="${testinfo.oralTempPattren}" 
                				onchange="pattrenValidation('${testinfo.oralTempPattren}', 'oralTemp0Msg', this.value, 'text' , 'oralTemp' )">
	                			<font color="red" id="oralTemp0Msg"> </font></td></tr>
                		</c:if>
                		<c:if test="${testinfo.oralTempPattren == '' }"><tr><td>Oral Temperature</td><td><input type="text" id="oralTemp0"></td></tr></c:if>
                	</c:if>
                	<c:if test="${testinfo.oralTemp == 'No' }"><input type="hidden" id="oralTemp0" value="-"></c:if>
                	
                	<c:if test="${testinfo.bp == 'Yes' }">
                		<c:if test="${testinfo.bpPattren != '' }">
                			<tr><td>Blood Pressure</td>
                			<td><input type="text" id="oralTemp0" placeholder="${testinfo.bpPattren}" 
                				onchange="pattrenValidation('${testinfo.bpPattren}', 'bp0Msg', this.value, 'text' , 'bp' )">
	                			<font color="red" id="bp0Msg"> </font></td></tr>
                		</c:if>
                		<c:if test="${testinfo.bpPattren == '' }"><tr><td>Blood Pressure</td><td><input type="text" id="bp0"></td></tr></c:if>
                	</c:if>
                	<c:if test="${testinfo.bp == 'No' }"><input type="hidden" id="bp0" value="-"></c:if>
                	
                	<c:if test="${testinfo.respiratoryRate == 'Yes' }">
                		<c:if test="${testinfo.respiratoryRatePattren != '' }">
                			<tr><td>Respiratory Rate</td>
                			<td><input type="text" id="respiratoryRate0" placeholder="${testinfo.respiratoryRatePattren}" 
                				onchange="pattrenValidation('${testinfo.respiratoryRatePattren}', 'respiratoryRate0Msg', this.value, 'text' , 'respiratoryRate' )">
	                			<font color="red" id="respiratoryRate0Msg"> </font></td></tr>
                		</c:if>
                		<c:if test="${testinfo.respiratoryRatePattren == '' }"><tr><td>Respiratory Rate</td><td><input type="text" id="respiratoryRate0"></td></tr></c:if>
                	</c:if>
                	<c:if test="${testinfo.respiratoryRate == 'No' }"><input type="hidden" id="respiratoryRate0" value="-"></c:if>
                	
					<c:if test="${testinfo.wellbeingAscertained == 'Yes' }">
						<c:if test="${testinfo.wellbeingAscertainedPattren != '' }">
                			<tr><td>Wellbeing Ascertained</td>
                			<td><input type="text" id="respiratoryRate0" placeholder="${testinfo.wellbeingAscertainedPattren}" 
                				onchange="pattrenValidation('${testinfo.wellbeingAscertainedPattren}', 'wellbeingAscertained0Msg', this.value, 'text' , 'wellbeingAscertained' )">
	                			<font color="red" id="wellbeingAscertained0Msg"> </font></td></tr>
                		</c:if>
                		<c:if test="${testinfo.wellbeingAscertainedPattren == '' }"><tr><td>Wellbeing Ascertained</td><td><input type="text" id="wellbeingAscertained0"></td></tr></c:if>
                	</c:if>
                	<c:if test="${testinfo.wellbeingAscertained == 'No' }"><input type="hidden" id="wellbeingAscertained0" value="-"></c:if>
                </tbody>
              </table>
              
<script>


function pattrenValidation(pattren, id, val, ele, pattrenfalg0){
	$("#"+id).html("");
	var pattrenfalg = true;
		if(val != ''){
			if(ele == 'text' || ele == 'textArea' || ele == 'date'|| ele == 'datetime'){
				var flag = 0;
				if(val.length == pattren.length){
					for(var i = 0; i< pattren.length; i++){
						if(pattren[i] == 'A'){
							if (val[i].match(/^[a-zA-Z]+$/) == null){
								flag = 1;
							}
						}else if(pattren[i] == 'N'){
							if (val[i].match(/^[0-9]+$/) == null){
								flag = 1;
							}
						}else{
							if(val[i] != pattren[i]){
								flag = 1;
							}
						}	
					}
					
					if(flag === 1){
						$("#"+id).html("Pattren Field. Required : " + pattren);
						pattrenfalg = false;
					}else{
						$("#"+id).html("");
						pattrenfalg = true;
					}
				}else{
					pattrenfalg = false;
					$("#"+id).html("Pattren Field. Required : " + pattren);
				}
			}
		}else{
			pattrenfalg = false;
			$("#"+id).html("Pattren Field. Required : " + pattren);
		}
		
		alert(pulseRatefalg +" : "+ oralTempfalg+" : "+ bpfalg+" : "+ respiratoryRatefalg+" : "+ wellbeingAscertainedfalg);
		alert(pattrenfalg0 + " : " + pattrenfalg)
		if(pattrenfalg0 === 'pulseRate'){
			pulseRatefalg = pattrenfalg;
		}else if(pattrenfalg0 === 'oralTemp'){
			oralTempfalg = pattrenfalg;
		}else if(pattrenfalg0 === 'bp'){
			bpfalg = pattrenfalg;
		}else if(pattrenfalg0 === 'respiratoryRate'){
			respiratoryRatefalg = pattrenfalg;
		}else if(pattrenfalg0 === 'wellbeingAscertained'){
			wellbeingAscertainedfalg = pattrenfalg;
		}
		alert(pulseRatefalg +" : "+ oralTempfalg+" : "+ bpfalg+" : "+ respiratoryRatefalg+" : "+ wellbeingAscertainedfalg);
}

</script>