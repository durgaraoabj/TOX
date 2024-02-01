<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script type="text/javascript">
	var rows = {};
	var count = 0;
</script>
</head>
<body>
	<div class="card">
		<div class="card-header"></div>
		<div class="card-body">
			<c:url value="/administration/clinicalCodesSave" var="saveunits"></c:url>
			<form:form action="${saveunits}" method="POST" id="saveunitsform">
				<input type="hidden" name="newunits" id="newunits">
				<!-- style="text-align:center;width:100%;" -->
				<table id="clincialCodeTable" class="table table-bordered table-striped">
					<thead>
						<tr style="height: 45px;">
							<th colspan="7"
								style="padding-bottom: 12px; background-color: #3C8DBC; color: #fff;">Clinical
								Codes</th>
						</tr>
						<tr>
							<td/>
							<th>Heading</th>
							<th>Code</th>
							<th>Sign</th>
							<th>Description</th>
							<th>Rank</th>
							<th><input type="button" onclick="addRow()"
								class="btn btn-gre" style="width: 90px;" value="Add" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${clinicalCodes}" var="code">
							<tr>
								<th style="width: 100px"><c:choose>
										<c:when test="${code.activeStatus}">
											<input type="checkbox" name="unitId" value="${code.id}"
												checked="checked">
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="unitId" value="${code.id}">
										</c:otherwise>
									</c:choose><font color="red" id="${unit.id}_Msg"></font></th>
								<td style="width: 170px"><input type="text"
									name="${code.id}_headding" id="${code.id}_headding"
									class="form-control" readonly="readonly"
									onchange="checkClinicalCode(this.value, '${code.id}_headding', '${code.id}_headdingMsg')"
									value="${code.headding}" /><font color="red"
									id="${unit.id}_headdingMsg"></font></td>
								<td style="width: 170px"><input type="text"
									name="${code.id}_clinicalCode" id="${code.id}_clinicalCode"
									class="form-control" readonly="readonly"
									onchange="checkClinicalCode(this.value, '${code.id}_clinicalCode', '${code.id}_clinicalCodeMsg')"
									value="${code.clinicalCode}" /><font color="red"
									id="${unit.id}_clinicalCodeMsg"></font></td>
								<td style="width: 170px"><input type="text"
									name="${code.id}_clinicalSign" id="${code.id}_clinicalSign"
									class="form-control" readonly="readonly"
									onchange="clinicalSign(this.value, '${code.id}_clinicalSign', '${code.id}_clinicalSignMsg')"
									value="${code.clinicalSign}" /><font color="red"
									id="${unit.id}_clinicalSignMsg"></font></td>
								<td style="width: 170px"><input type="text" id="${code.id}_description"
									name="${code.id}_description" value="${code.description}"
									class="form-control" /><font color="red"
									id="${code.id}_descriptionMsg"></font></td>
								<td style="width: 170px"><input type="text"
									name="${code.id}_rank" id="${code.id}_rank"
									class="form-control" readonly="readonly"
									onchange="rank(this.value, '${code.id}_rank', '${code.id}_rankMsg')"
									value="${code.rank}" /><font color="red"
									id="${unit.id}_rankMsg"></font></td>
								<td style="width: 170px"></td>
								<!-- 							<td /> -->
							</tr>
						</c:forEach>
						<tr></tr>
					</tbody>
				</table>.
			<table style="text-align: center; width: 70%; margin-left: 13%">
					<tr>
						<td></td>
						<td></td>
						<td colspan="5" style="text-align: center;"><input
							type="button" class="btn btn-gre" style="width: 90px;"
							onclick="saveData()" value="Save" /></td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</form:form>
		</div>
	</div>
</body>
<script type="text/javascript">
	function addRow() {
		var flag = 0;
		$.each(rows, function(k, v) {
			if (v == 0) {
				flag = k;
				return false;
			}
		})
		if (flag == 0) {
			count++;
			flag = count;
		}
		rows[flag] = flag;
		var row = "<tr id=\"" + flag + "Tr\"><td/><td><input type=\"text\" name=\""
				+ flag
				+ "_newheadding\" class='form-control' id=\""
				+ flag
				+ "_newheadding\" onchange=\"checkClinicalCode(this.value, '"
				+ flag
				+ "_newheadding', '"
				+ flag
				+ "_newheaddingMsg')\" /><font color='red' id=\"" + flag
			+ "_newheaddingMsg\"></font></td>";
		row += "<td><input type=\"text\" name=\""
				+ flag
				+ "_newclinicalCode\" class='form-control' id=\""
				+ flag
				+ "_newclinicalCode\" onchange=\"checkClinicalCode(this.value, '"
				+ flag
				+ "_newclinicalCode', '"
				+ flag
				+ "_newclinicalCodeMsg')\" /><font color='red' id=\"" + flag
				+ "_newclinicalCodeMsg\"></font></td>";
		row += "<td><input type=\"text\" name=\""
				+ flag
				+ "_newclinicalSign\" class='form-control' id=\""
				+ flag
				+ "_newclinicalSign\" onchange=\"checkClinicalCode(this.value, '"
				+ flag
				+ "_newclinicalSign', '"
				+ flag
				+ "_newclinicalSignMsg')\" /><font color='red' id=\"" + flag
					+ "_newclinicalSignMsg\"></font></td>";
		row += "<td><input type=\"text\" name=\""
				+ flag
				+ "_newdescription\" class='form-control' id=\""
				+ flag
				+ "_newdescription\" onchange=\"checkClinicalCode(this.value, '"
				+ flag
				+ "_newdescription', '"
				+ flag
				+ "_newdescriptionMsg')\" /><font color='red' id=\"" + flag
						+ "_newdescriptionMsg\"></font></td>";
		row += "<td><input type=\"text\" name=\""
				+ flag
				+ "_newrank\" class='form-control' id=\""
				+ flag
				+ "_newrank\" onchange=\"checkClinicalCode(this.value, '"
				+ flag
				+ "_newrank', '"
				+ flag
				+ "_newrankMsg')\" /><font color='red' id=\"" + flag
									+ "_newrankMsg\"></font></td>";
		row += "<td><input type=\"button\" class='btn btn-gre' value=\"Remove\" onclick=\"removeRow('"
				+ flag + "')\"/></td>";
		// debugger;
		$("#clincialCodeTable").append(row);
	}
	function removeRow(trId) {
		rows[trId] = 0;
		$("#" + trId + "Tr").remove();
	}
	
	
	function checkClinicalCode(value, id, messagId) {
		
	}
	
	function checkMandatory(value, id, messageId) {
		$("#" + messageId).html("");
		if (value != '') {
			return true;
		} else {
			$("#" + messageId).html("Required Field");
			return false;
		}
	}

	function saveData() {
		var flag = true;
		var count = 0;
		$("input:checkbox[name='unitId']:checked").each(
				function() {
					if (!checkMandatory($("#" + $(this).val()).val(),
							$(this).val(), $(this).val() + "_Msg")) {
						flag = false;
					}
					count++;
				});
		var newRows = [];
		$.each(rows, function(k, v) {
			if (v != 0) {
// 				if (!checkMandatory($("#" + k + "_newunit").val(), k + "_newunit",
// 						k + "_newunitMsg")) {
// 					flag = false;
// 				}
				count++;
				newRows.push(k);
			}
		});
		if (flag) {
			$("#newunits").val(newRows);
			$("#saveunitsform").submit();
		}
	}
</script>


</html>