<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title" style="font-weight: bold; font-size: large;">Vistros
				Data Export</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body"></div>
	</div>
	<c:url value="/sysmex/saveInstrument"
		var="saveInstrumentWeightSubmit" />
	<sf:form action="${saveInstrumentWeightSubmit}" method="POST"
		id="formsumit">
		<div id="dataDiv">
			<table id="example1" class="table table-bordered table-striped">
				<thead>
					<tr>
						<td />
						<th>Instrument Name</th>
						<th>IP Address</th>
						<th>Port Not</th>
						<th>COM Port Not</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${insturments}" var="row">
						<tr>
							<td><c:choose>
									<c:when test="${row.activeStatus.statusCode eq 'ACTIVE'}">
										<input type="checkbox" name="insturmentId" checked="checked" value="${row.id}"/>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="insturmentId" value="${row.id}"/>
									</c:otherwise>
								</c:choose></td>
							<td>${row.instrumentName}</td>
							<td>${row.ipAddress}</td>
							<td>${row.portNo}</td>
							<td>${row.comPortNo}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<input type="submit" value="Save" />
		</div>
	</sf:form>
</body>
</html>