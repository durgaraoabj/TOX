<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Experimental Design View</h3>
			<br />
			<h5 class="card-title">Study : ${study.studyNo }</h5>
		</div>
		<div class="box-body">
			<c:url value="/expermentalDesign/createExpermentalDesign" var="url" />
			<form:form action="${url}" method="post" id="form">
				<table class="table table-bordered table-striped">
					<tr>
						<th>Group</th>
<!-- 						<th style="width: 15">Dose(mg/kg b.w)</th> -->
<!-- 						<th style="width: 15">Conc. (mg/ml)</th> -->
						<th style="width: 10%">No.Of Animals</th>
						<th style="width: 10%">Sex</th>
						<td colspan="2">
							<table class="table table-bordered table-striped">
								<tr>
									<th colspan="2" style="text-align: center"><font
										style="text-align: center">Animal Number</font></th>
								</tr>
								<tr>
									<th style="width: 10%">From</th>
									<th style="width: 10%">To</th>
								</tr>
							</table>
						</td>
					</tr>
					<c:forEach items="${group}" var="g">
						<c:if test="${study.requiredSubGroup}">
							<tr>
								<th colspan="7">${g.groupName}</th>
							</tr>
						</c:if>
						<c:forEach items="${g.subGroupInfo }" var="s">
							<tr>
								<td>${s.name}</td>
<%-- 								<td>${s.dose}</td> --%>
<%-- 								<td>${s.conc}</td> --%>

								<c:choose>
									<c:when test="${s.animalInfo.size() eq 1}">
										<c:forEach items="${s.animalInfo }" var="animals">
											<td>${animals.count}</td>
											<td>${animals.gender}</td>
											<td>${animals.formId}</td>
											<td>${animals.toId}</td>
										</c:forEach>
									</c:when>
									<c:otherwise>
										<td colspan="4">
											<table class="table table-bordered table-striped">
												<c:forEach items="${s.animalInfo }" var="animals">
													<tr>
														<td>${animals.id} - ${animals.count}</td>
														<td>${animals.gender}</td>
														<td>${animals.formId}</td>
														<td>${animals.toId}</td>
													</tr>
												</c:forEach>
											</table>
										</td>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
					</c:forEach>
					<!-- 				<tr> -->
					<!-- 					<td><input type="button" id="submitBtn" onclick="submitBtnbut()" value="Save" class="btn btn-primary" style="width:200px;"></td> -->
					<!-- 				</tr> -->
				</table>
			</form:form>
		</div>
	</div>
</body>
<script type="text/javascript">
	function submitBtnbut() {
		var flag = true;

		if (flag)
			$("#form").submit();
	}
</script>
</html>