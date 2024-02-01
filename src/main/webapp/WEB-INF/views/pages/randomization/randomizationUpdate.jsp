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
	var kv = {};
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
							<input type="text" name="${sub}_${p.id}" id="${sub}_${p.id}" 
								size="1" maxlength="1" />
							<font color="red" id="${sub}_${p.id}msg"></font>
							<script type="text/javascript">
								ids.push('${sub}_${p.id}');
							</script>
						</td>
					</c:forEach>
				</tr>
			</c:forEach>
		</table>
<!-- 		<table> -->
<!-- 			<tr> -->
<!-- 				<td><input type="button" onclick="save()" -->
<!-- 					value="Save" class="btn btn-primary" style="width:200px;"></td> -->
<!-- 			</tr> -->
<!-- 		</table> -->
	</form:form>
<c:forEach var="tmp" items="${mp}">
	<script type="text/javascript">
		kv['${tmp.key}'] ='${tmp.value}';
	</script>
</c:forEach>
<div id="tt"></div>
</div>
</body>
<script type="text/javascript">
	$.each(kv, function(k, v){
		$("#"+k).val(v);
	});
</script>
</html>