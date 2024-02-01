<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Parameters</title>
<script type="text/javascript">
	var unitdata = {};
	var tecodeOrders = {};
</script>
</head>
<body>
	<c:forEach items="${testCodesUnits}" var="unit">
		<script type="text/javascript">
			unitdata['${unit.id}'] = '${unit.displayUnit}';
		</script>
	</c:forEach>
	<div class="card">
	<div class="card-header">
	<h3 class="card-title">Clinpath Parameters</h3>
	</div>
	<div class="card-body">
		<c:url value="/grouping/mergeTestCodes" var="savetcs"></c:url>
		<form:form action="${savetcs}" method="POST" id="savetcform">
			<input type="hidden" name="newtcrows" id="newtcrows">
			<table id="tcTable" class="table table-bordered table-striped" style="width:90%;margin-left:55px">
				<thead>
					<tr>
						<th colspan="6" style="text-align:center;height:45px;padding-bottom: 12px;background-color: #3C8DBC;
	                     color: #fff;">Clinpath Parameters</th>
					</tr>
					<tr>
						<th  style="padding-bottom: 12px;">Select Instrument :</th>
						
						<th colspan="6"> <select id="instument" style="width:190px"
							name="instument" class="form-control" onchange="displayTestCodes(this.value)">
								<option value="-1">--Select--</option>
								<option value="SYSMEX">SYSMEX</option>
								<option value="STAGO">STAGO</option>
								<option value="VITROS">VITROS</option>
						</select> <script type="text/javascript">
							$("#instument").val('${instument}')
						</script>
						</th>
					</tr>

					<tr style="text-align:center;">
						<th style="width:190px;padding-bottom: 12px;">Select</th>
						<th style="width:190px;padding-bottom: 12px;">Clinpath Parameter</th>
						<th style="width:190px;padding-bottom: 12px;">Display Parameter</th>
						<th style="width:190px;padding-bottom: 12px;">Order</th>
						<th style="width:190px;padding-bottom: 12px;">Units</th>
						<th style="width:190px;"><input type="button" onclick="addRow()" class="btn btn-gre" style="width:90px;" value="Add" /></th>
					</tr>
				</thead>
				<tbody id="tbodyid" >
					<c:forEach items="${testCodes}" var="tc">
						<tr style="text-align:center">
							<th><c:choose>
									<c:when test="${tc.activeStatus}">
										<input type="checkbox" name="testCodeIds"  value="${tc.id}"
											checked="checked">
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="testCodeIds" value="${tc.id}">
									</c:otherwise>
								</c:choose><font color="red" id="${tc.id}_Msg"></font></th>
							<td><input type="text"  name="${tc.id}_testCode"
								id="${tc.id}_testCode" readonly="readonly" class="form-control"
								onchange="testCodeUnits(this.value, '${tc.id}_testCode', '${tc.id}_testCodeMsg')"
								value="${tc.testCode}" /><font color="red"
								id="${tc.id}_testCodeMsg"></font></td>
							<td><input type="text" class="form-control" name="${tc.id}_dispalyTestCode"
								value="${tc.disPalyTestCode}" /><font color="red"
								id="${tc.id}_dispalyTestCodeMsg"></font></td>
							<td><input type="text" class="form-control" name="${tc.id}_order"
								id="${tc.id}_order"
								onchange="checkDuplicate('${tc.id}_order', this.value)"
								onkeypress="return (event.charCode >= 48 && event.charCode <= 57) || event.charCode == 44"
								value="${tc.orderNo}" /><font color="red"
								id="${tc.id}_orderMsg"></font> <script type="text/javascript">
									tecodeOrders['${tc.id}_order'] = '${tc.orderNo}';
								</script></td>
							<td><select id="${tc.id}_type" class="form-control" name="${tc.id}_type">
									<c:forEach items="${testCodesUnits}" var="unit">
										<option value="${unit.id}">${unit.displayUnit}</option>
									</c:forEach>
							</select> <script type="text/javascript">
								$("#${tc.id}_type").val(
										'${tc.testCodeUints.id}');
							</script> <font color="red" id="${tc.id}_typeMsg"></font></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<table style="width:90%;margin-left:55px">
				<tr>
					<td />
					<td />
					<td style="text-align:center"><input type="button" onclick="saveData()" class="btn btn-gre" style="width:90px;" value="Save" /></td>
					<td />
				</tr>
			</table>
		</form:form>
	</div>
	</div>
</body>
<c:url value="/grouping/testCodes" var="gettcs"></c:url>
<form:form action="${gettcs}" method="GET" id="gettcsform">
	<input type="hidden" name="instument" id="instumentid" />
</form:form>

<script src='/TOX/static/js/grouping/testCodes.js'></script>
</html>