<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<body>
	<c:set var="instruments"
		value="${studyDto.insturmentViewTestCodes.size()}"></c:set>
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th colspan="${instruments}">Study Information</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<c:forEach items="${studyDto.insturmentViewTestCodes}"
					var="insturmentTc">
					<td>
						<table>
							<tr>
								<th colspan="2">${insturmentTc.key}</th>
							</tr>
							<tr>
								<th>Parameter</th>
								<th>Order</th>
								<c:forEach items="${insturmentTc.value}" var="tc">
									<tr>
										<td>${tc.testCode.disPalyTestCode}</td>
										<td>${tc.orderNo}</td>
									</tr>
								</c:forEach>
						</table>
					</td>
				</c:forEach>
			</tr>
		</tbody>
	</table>
</body>
</html>