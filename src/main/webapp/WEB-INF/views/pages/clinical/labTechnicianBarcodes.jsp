<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/study/clinical/labTechnicianBarcodesPrint" var="labTechnicianBarcodesPrint" />
<sf:form action="${labTechnicianBarcodesPrint}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Lab Technician Bar-codes</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Select Period</td>
				<td>
					<select name="period" class="form-control" id="period">
						<option value="-1">--Select--</option>
						<c:forEach items="${periodList2}" var="p">
							<option value="${p.id}">P-${p.order}</option>	
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Lab Technician Name</td>
				<td><input type="text" name="name" id="name"/></td>
			</tr>
			<tr>
				<td>Type</td>
				<td>
					<select name="type" id="type">
						<option value="PHLIBOTOMIST">PHLIBOTOMIST</option>
						<option value="CLINICAL STAFF">CLINICAL STAFF</option>
						<option value="PARAMEDICAL">PARAMEDICAL</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>Batch NO</td>
				<td><input type="text" name="batchNo" id="batchNo"/></td>
			</tr>
			<tr>
				<td><input type="button" value="Print Baro-Code" class="btn btn-primary" onclick="submitForm()"/></td>
			</tr>
			
		</table>
	</div>
</div>
</sf:form>

<script>
function submitForm(){
	$("#formsumit").submit();	
}
</script>