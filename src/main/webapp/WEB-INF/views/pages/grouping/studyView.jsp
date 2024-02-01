<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<body>
<c:url value="/administration/studyDesign" var="gotostudyDesign" />
		<form:form action="${gotostudyDesign}" method="post" id="gotostudyDesignPage">
			<input type="hidden" name="studyId" id="studyDesignStudyId"  value="${studyId}"/>
			<input type="hidden" name="actionType" id="studyDesignType" value="update" />
		</form:form>
	<table class="table table-bordered table-striped">
		<thead>
			<tr><th colspan="4">Study Information</th></tr>
		</thead>
		<tbody>
			<tr>
				<td colspan="4"><b>Study Number :</b>${studyDto.studyNo}</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<th>Group Name</th> -->
<!-- 				<th>Gender</th> -->
<!-- 				<th>Animal From</th> -->
<!-- 				<th>Animal To</th> -->
<!-- 			</tr> -->
<%-- 			<c:forEach items="${studyDto.groups}" var="group"> --%>
<!-- 				<tr> -->
<%-- 					<td>${group.group.groupName}</td> --%>
<%-- 					<td>${group.gender}</td> --%>
<%-- 					<td>${group.from}</td> --%>
<%-- 					<td>${group.to}</td> --%>
<!-- 				</tr>			 -->
<%-- 			</c:forEach> --%>
		</table>
		<table class="table table-bordered table-striped">
<!-- 		<thead> -->
<!-- 			<tr><th colspan="4">Study Animals</th></tr> -->
<!-- 		</thead> -->
<!-- 		<tbody> -->
<!-- 			<tr> -->
<!-- 				<th>Group Name</th> -->
<!-- 				<th>Animal No</th> -->
<!-- 				<th>Gender</th> -->
<!-- 			</tr> -->
<%-- 			<c:forEach items="${studyDto.animals}" var="animals"> --%>
<!-- 				<tr> -->
<%-- 					<td>${animals.groupInfo.groupName}</td> --%>
<%-- 					<td>${animals.permanentNo}</td> --%>
<%-- 					<td>${animals.gender}</td> --%>
<!-- 				</tr>			 -->
<%-- 			</c:forEach> --%>
<!-- 		</table> -->
		<table class="table table-bordered table-striped">
			<c:forEach items="${studyDto.insturmentViewTestCodes}" var="insturmentTc">
				<tr><th colspan="2">${insturmentTc.key}</th></tr>
				<tr><th>Parameter</th><th>Order</th>
				<c:forEach items="${insturmentTc.value}" var="tc">
					<tr>
						<td>${tc.testCode.disPalyTestCode}</td>
						<td>${tc.orderNo}</td>
					</tr>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>
<div align="center"><br/>
				<input type="button" id="Config" onclick="gotoStudyDesignPage()" value="Previous Page" class="btn btn-primary">
				</div>
</body>
<script type="text/javascript">
function gotoStudyDesignPage(){
	$("#gotostudyDesignPage").submit();	
}
</script>
</html>