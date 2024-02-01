<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
</head>
<body>
<div class="card">
<div class="card-header">
<h3 class="card-title">Units</h3>
</div>
	<div class="card-body">
		<c:url value="/grouping/mergeTestCodeUnits" var="saveunits"></c:url>
		<form:form action="${saveunits}" method="POST" id="saveunitsform">
			<input type="hidden" name="newunits" id="newunits"><!-- style="text-align:center;width:100%;" -->
			<table id="unitsTable" class="table table-bordered table-striped" >
				<thead >
					<tr style="height:45px;"><th colspan="4" style="padding-bottom: 12px;background-color: #3C8DBC;
	                 color: #fff;">Parameter Units</th></tr>
					<tr>
						<th>Select</th>
						<th>Unit</th>
						<th>Display Unit</th>
						<th><input type="button" onclick="addRow()" class="btn btn-gre" style="width:90px;" value="Add" /></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${testCodeUnits}" var="unit">
						<tr>
							<th style="width:100px"><c:choose>
									<c:when test="${unit.activeStatus}">
										<input type="checkbox" name="unitId" value="${unit.id}"
											checked="checked">
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="unitId" value="${unit.id}">
									</c:otherwise>
								</c:choose><font color="red" id="${unit.id}_Msg"></font></th>
							<td style="width:170px"><input type="text" name="${unit.id}_tcUnit" id="${unit.id}_tcUnit" class="form-control" readonly="readonly"
								onchange="testCodeUnits(this.value, '${unit.id}_tcUnit', '${unit.id}_tcUnitMsg')"
								value="${unit.instumentUnit}" /><font color="red"
								id="${unit.id}_tcUnitMsg"></font></td>
							<td style="width:170px"><input type="text" name="${unit.id}_displayUnit"
								value="${unit.displayUnit}" class="form-control" /><font color="red"
								id="${unit.id}_dispalyUnitMsg"></font></td>
							<td style="width:170px"></td>
<!-- 							<td /> -->
						</tr>
					</c:forEach>
					<tr></tr>
				</tbody>
			</table>.
			<table style="text-align:center;width:70%;margin-left:13%">
				<tr>
					<td ></td>
					<td ></td>
					<td colspan="5" style="text-align:center;"><input type="button" class="btn btn-gre" style="width:90px;" onclick="saveData()" value="Save" /></td>
					<td ></td>
					<td ></td>
				</tr>
			</table>
		</form:form>
	</div>
	</div>
</body>
<script src='/TOX/static/js/grouping/testCodeUnits.js'></script>
</html>