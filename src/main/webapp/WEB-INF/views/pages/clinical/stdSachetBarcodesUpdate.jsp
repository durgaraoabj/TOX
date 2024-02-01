<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sachet Bar-codes/ Randamization</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<th>Period No</th>
				<th>Subject</th>
				<th>Treatment Sequence</th>
				<th>Treatment</th>
			</tr>
			<c:forEach items="${ssbList }" var="sub">
				<tr>
					<td>${sub.periodNo}</td>
					<td>${sub.subject}</td>
					<td>${sub.treatments}</td>
					<td>${sub.treatment}</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>

<c:url value="/study/clinical/stdSachetBarcodesPrint" var="stdSachetBarcodesPrint" />
<sf:form action="${stdSachetBarcodesPrint}" method="POST"  id="formsumit" >
<input type="button" value="Print Baro-Code" class="btn btn-primary" onclick="submitForm()"/>
</sf:form>
<script>
function submitForm(){
	$("#formsumit").submit();	
}
</script>