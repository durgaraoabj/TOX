<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	var ids = [];							
</script>
</head>
<body>
<div class="box-body">
	<div align="center"><input type="button" id="uploadRandamization" onclick="uploadRandamization()"
		value="Upload Randamization" class="btn btn-primary" style="width:200px;"></div>
	<c:url value="/randomization/createRandomization" var="createRandomizationUrl"/>
	<form:form action="${createRandomizationUrl}" method="post" id="create_randomization" >
		<table class="table table-bordered table-striped">
			<tr>
				<th>Subject Id</th>
				<c:forEach items="${periods}" var="p" varStatus="c">
					<th>
						Visit&nbsp;${c.count}
					</th>
				</c:forEach>
			</tr>
			<c:forEach begin="1" end="${study.subjects }" step="1" var="sub">
				<tr>
					<td><input type="text" name="subject" value="${sub}" readonly="readonly" 
							size="4" maxlength="4"></td>
					<c:forEach items="${periods}" var="p">
						<td>
							<input type="text" name="${sub}_${p.id}" id="${sub}_${p.id}" size="1" maxlength="1"/>
							<font color="red" id="${sub}_${p.id}msg"></font>
							<script type="text/javascript">
								ids.push('${sub}_${p.id}');
							</script>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
		<table>
			<tr>
				<td><input type="button" onclick="save()"
					value="Save" class="btn btn-primary" style="width:200px;"></td>
				<td><input type="button" onclick="clear()"
					value="Clear" class="btn btn-primary" style="width:200px;"></td>	
			</tr>
		</table>
	</form:form>
</div>
</body>
<script type="text/javascript">
var letters = /^[A-Z]+$/;
	function save() {
		var flag = true;
		$.each(ids, function(k, v) {
			$("#"+v+"msg").html("");
			if($("#"+v).val().trim() == ''){
				$("#"+v+"msg").html("Requried Field");
				flag = false;
			}else if(!$("#"+v).val().trim().match(letters)){
				$("#"+v+"msg").html("In-valied");
				flag = false;
			}		
		});
		if(flag){
			$("#create_randomization").submit();
		}		
	}
	function clear() {
		$.each(ids, function(k, v) {
			$("#"+v).val("");
		});
	}
</script>
</html>