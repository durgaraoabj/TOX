<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<div class="card">
	<div class="card-header">
		<h3 class="card-title">Sample Schedule</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<table id="example1" class="table table-bordered table-striped">
			<tr>
				<td>Dose Time</td><td>${cli.doseTime}</td>
				<td>Dose Date</td><td>${cli.doseDate}</td>
			</tr>
			<tr><td>Name of Sample Collection</td><td colspan="3">${cli.sampleSheetName }</td></tr>
		</table>
		<table id="example1" class="table table-bordered table-striped">
			<tr><th>Type</th><th>Time Point</th><th>Time Point Date</th><th>Planed Time</th></tr>
			<c:forEach items="${timePoints}" var="tp">
				<tr>
					<td>${tp.type }</td>
					<td>${tp.timePoint }</td>
					<td>${tp.timePointDate }</td>
					<td>${tp.planedTime }</td>
<%-- 					<td>${tp.planedDate }</td> --%>
				</tr>
			</c:forEach>
		</table>
	</div>
</div>