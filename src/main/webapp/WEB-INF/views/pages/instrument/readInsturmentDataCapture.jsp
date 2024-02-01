<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<!-- Font Awesome -->
<link rel="stylesheet" href='<c:url value="/static/css/tab.css"/>'>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Data Collection From</h3>
			<input type="hidden" value="${serviceType}" id="serviceType0" />
		</div>


		<!-- /.card-header -->
		<div class="card-body">
<!-- 			Select Study : <select name="studyId" id="studyId"> -->
<!-- 				<option value="-1">--Select--</option> -->
<%-- 				<c:forEach items="${studyList}" var="std"> --%>
<%-- 					<option value="${std.id}">${std.studyNo }</option> --%>
<%-- 				</c:forEach> --%>
<!-- 			</select><font color="red" id="studyIdMsg"></font> -->
			<div class="w3-bar w3-black">
				<c:forEach items="${ipAddress}" var="a">
					<button class="w3-bar-item w3-button"
						onclick="displayServerSelectoins('${a.instrumentName}')">${a.instrumentName}</button>
				</c:forEach>
			</div>
			<div id="STAGOTR">
				<table>
					<tr>
					<td>Sample Type.</td>
											<td><select name="sampleType" id="sampleType"
							class="form-control" onchange="sampleType()">
								<option value="-1" selected="selected">--Select--</option>
								<option value="QC">QC</option>
								<option value="Animal">Animal</option>
						</select> <font color='red' id="sampleTypeMsg"></font></td>
						<td>Lot No.</td>
						<td><input type="text" name="loatNo" id="loatNo"
							class="form-control" disabled="disabled"
							onchange="callStagoService()" /><font color='red' id="loatNoMsg"></font></td>
						<td>Test :</td>
						<td>
						<select name="test" id="test" class="form-control"
							onchange="callStagoService()">
								<option value="-1" selected="selected">--Select--</option>
								<c:forEach items="${stagoTestCodes}" var="tc">
									<option value="${tc.testCode.testCode}">${tc.testCode.disPalyTestCode}</option>
								</c:forEach>
						</select> <font color='red' id="testMsg"></font></td>

						<td><input type='button' value="Reset" id="restId"
							onclick="resetStago()" disabled="disabled"></td>
						<td><input type='button' value="Goto Report Page" id="restId"
							onclick="gotoReportPage('STAGO')"></td>
					</tr>
				</table>
			</div>
<!-- 			<div id="VITROSTR"> -->
<!-- 				<table> -->
<!-- 					<tr> -->
<!-- 						<td>VITROS COM PORT :</td> -->
<!-- 						<td><select name="cistroscomport" class="form-control" -->
<!-- 							id="cistroscomport"> -->
<!-- 								<option value="-1">--Select--</option> -->
<!-- 								<option value="COM1">COM1</option> -->
<!-- 								<option value="COM2">COM2</option> -->
<!-- 								<option value="COM3">COM3</option> -->
<!-- 								<option value="COM4">COM4</option> -->
<!-- 								<option value="COM5">COM5</option> -->
<!-- 						</select><font color='red' id="comportMsg"></font></td> -->
<!-- 						<td><input type='button' value="Configure" -->
<!-- 							id="vistrosConfigre" onclick="callVistrosService()"></td> -->
<!-- 												<td><input type='button' value="Reset" id="restId" onclick="resetStago()" disabled="disabled"></td> -->
<!-- 						<td><input type='button' value="Goto Report Page" id="restId" -->
<!-- 							onclick="gotoReportPage('VITROSTR')"></td> -->
<!-- 					</tr> -->
<!-- 				</table> -->
<!-- 			</div> -->
			<div id="insturmetnData" style="width: 100%; overflow: scroll"></div>
		</div>

		<c:url value="/sysmex/callInstrumentReport" var="var1" />
		<sf:form action="${var1}" method="POST" id="callReportService">
			<input type="hidden" name="serviceType" id="serviceType" value="" />
		</sf:form>

	</div>
</body>
<script src='/TOX/static/js/instrument/serverSelection.js'></script>
<script src='/TOX/static/js/instrument/sysmexData.js'></script>

</html>
