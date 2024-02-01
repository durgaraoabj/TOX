<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<c:url value="/study/clinical/stdSubjectBarcodePrint" var="stdSubjectBarcodePrint" />
<sf:form action="${stdSubjectBarcodePrint}" method="POST"  id="formsumit" >
<input type="hidden" name="type" id="type"/>
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Subject Bar-codes</h3>
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
							<option value="${p.order}">P-${p.order}</option>	
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td>Subject No : </td>
				<td><input type="text" lang="3" size="3" id="subject" name="subject" value="0"/></td>
			</tr>
			<tr>
				<td><input type="button" value="Print Baro-Code" class="btn btn-primary" onclick="submitForm()"/></td>
				<td><input type="button" value="Print all Subjects Baro-Codes" class="btn btn-primary" onclick="submitFormAll()"/></td>
			</tr>
			
		</table>
	</div>
</div>
</sf:form>

<script>
function submitForm(){
	$("#type").val("0");
	$("#formsumit").submit();	
}
function submitFormAll(){
	$("#type").val("1");
	$("#formsumit").submit();		
}
</script>