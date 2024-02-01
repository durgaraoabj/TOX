<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<body>
	<c:forEach items="${instumentTestCodes}" var="instument">
		<script type="text/javascript">
			var tcs = {};	//left side selection status
			var tcsRight = {};	// rigth side selectio status
			var tempTcs = []	// test code ids in temp array
			
			var tcName  = {};	//test code key = id, value = names  
			var tcOrder = {};	//test code key = id, value = order
			var rigthTestCodesOrder = {}
			var onlyRigthElements = {};
		</script>
		<table>
			<tr>
				<th colspan="3">${instument.key}</th>
			</tr>
			<tr>
				<td align=left class="ui-widget-content">
					<div style="overflow-x: auto"
						id="${instrumentNames[instument.key]}DivLeft">
						<table>
							<thead>
								<tr>
									<th>Parameter</th>
									<th>Order</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${instument.value}" var="tcv">
									<tr id="${tcv.id}Div" onclick="testCodeSelect('${tcv.id}')">

										<td>${tcv.disPalyTestCode}</td>
										<td>${tcv.orderNo}</td>
									</tr>
									<script type="text/javascript">
										tcs['${tcv.id}'] = 0; //left side selection status
										tcsRight['${tcv.id}'] = 0; // rigth side selectio status
										tempTcs.push('${tcv.id}'); // test code ids in temp array
										
										tcName['${tcv.id}'] = '${tcv.disPalyTestCode}'; //test code key = id, value = names 
										tcOrder['${tcv.id}'] = '${tcv.orderNo}';	//test code key = id, value = order
										rigthTestCodesOrder['${tcv.id}'] = "";		//default riggth test code key = id, value = 0
									</script>
								</c:forEach>
							</tbody>
						</table>
						<script type="text/javascript">
							insturmentcs['${instrumentNames[instument.key]}'] = tempTcs;
						</script>
					</div>
				</td>
				<td align="center"><input type="button" id="btnAdd" value=">"
					onclick="moveTcsToRight('${instrumentNames[instument.key]}')"
					style="width: 50px;" /><br /> <input type="button" id="btnAddAll"
					onclick="moveAllTcsToRight('${instrumentNames[instument.key]}')"
					value=">>" style="width: 50px;" /><br /> <input type="button"
					onclick="moveTcsToLeft('${instrumentNames[instument.key]}')"
					id="btnRemove" value="<" style=" width: 50px;" /> <br /> <input
					type="button" id="btnRemoveAll" value="<<" style="
					width: 50px;" onclick="moveAllTcsToLeft('${instrumentNames[instument.key]}')" />
				<td align="left" class="ui-widget-content">
					<div style="overflow-x: auto"
						id="${instrumentNames[instument.key]}_DivRight">
						<table id="${instrumentNames[instument.key]}_DivRightTable">
							<thead>
							<tr>
								<th>Parameter</th>
								<th>Old Order</th>
								<th>Order&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;</th>
							</tr>
							</thead>
							<tbody id="rigthtbody">
							</tbody>
						</table>
					</div>
				</td>

			</tr>
		</table>
	</c:forEach>
</body>
</html>


