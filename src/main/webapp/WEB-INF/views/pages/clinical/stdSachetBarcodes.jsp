<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/study/clinical/stdSachetBarcodesPrint" var="stdSachetBarcodesPrint" />
<sf:form action="${stdSachetBarcodesPrint}" method="POST"  id="formsumit" >
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sachet Bar-codes/ Randamization</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<th>Subject</th>
				<th>Treatment Sequence</th>
				<c:forEach items="${periods }" var="p">
					<th>P-${p.order }</th>
				</c:forEach>
			</tr>
			<c:forEach begin="1" step="1" end="${subjects}" var="sub">
				<tr>
					<td>${sub}</td>
					<td><input type="text" name="sub${sub}_treatement" id="sub${sub}"></td>
					<c:forEach items="${periods }" var="p">
						<td><input type="text" name="sub${sub}_${p.id}" id="sub${sub}_${p.id}"></td>
					</c:forEach>
				</tr>
			</c:forEach>
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