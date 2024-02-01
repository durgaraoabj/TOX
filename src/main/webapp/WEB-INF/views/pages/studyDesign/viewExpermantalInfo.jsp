<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Experimental Info</h3>
		</div>
		<!-- /.card-header -->
		<div class="card-body">
			<table id="example1" class="table table-bordered table-striped">
				 <thead>
					<tr>
						<th>Group</th><th>Sub Group</th><th>Observation Name</th><th>Gender</th><th>From</th><th>To</th><th>Day/Week</th>
						<c:forEach begin="1" end="${max}" step="1">
							<th>Date</th>
						</c:forEach>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${expData}" var="e">
					<tr>
						<td>${e.groupName}</td><td>${e.subGroupName}</td><td>${e.obserVationName}</td><td>${e.gender}</td><td>${e.from}</td><td>${e.to}</td><td>${e.dayOrWeek}</td>
						<c:forEach items="${e.days}" var="d">
							<td>${d}</td>
						</c:forEach>
					</tr>
				</c:forEach>
				</tbody>
				<tfoot>
				<tr>
					<th>Group</th><th>Sub Group</th><th>Observation Name</th><th>Gender</th><th>From</th><th>To</th><th>Day/Week</th>
					<c:forEach begin="1" end="${max}" step="1">
						<th>Date</th>
					</c:forEach>
				</tr>
				</tfoot>
			</table>
		</div>
	</div>