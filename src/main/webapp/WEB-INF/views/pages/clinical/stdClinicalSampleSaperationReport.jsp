<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sample Separation Report</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table class="table table-bordered table-striped">
			<tr>
				<td>Select Period</td>
				<td>
					<select name="period" class="form-control" id="period" >
						<option value="-1">--Select--</option>
						<c:forEach items="${periodList2}" var="p">
							<option value="${p.id}">P-${p.order}</option>	
						</c:forEach>
					</select>
				</td>
			</tr>		
			<tr>
				<td>Select Subject</td>
				<td>
					<select name="subject" class="form-control" id="subject" onchange="dosesingReport(this.value)">
						<c:forEach var="sub" begin="1" end="${study.subjects }">
						    <option value="${sub}">${sub}</option>
						</c:forEach>
					</select>
				</td>
			</tr>			
			<tr><td colspan="2"><input type="button" value="View" class="btn btn-primary" onclick="submitForm()"/></td></tr>
		</table>
		
		<div id="report"></div>
		
	</div>
</div>


<script>

function submitForm(){
	var periodId = $("#period").val();
	var subject = $("#subject").val();
	if(periodId != -1){
		
		var result = asynchronousAjaxCall(mainUrl+"/study/clinical/stdClinicalSampleSeparationReportTable/"+periodId+"/"+subject);
    	if(result != '' || result != 'undefined'){
			$("#report").html(result);
		}
	}else{
		$("#report").html("");
	}
}
</script>
