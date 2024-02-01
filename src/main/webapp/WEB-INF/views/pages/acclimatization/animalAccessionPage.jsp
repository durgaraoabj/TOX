<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	
</script>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title"><b>Animal Accession</b></h3>
		</div>
		<div class="card-body">
		<c:url value="/accession/saveAccessData" var="saveData"></c:url>
		<form:form action="${saveData}" method="POST" id="accessionDataForm">
			<input type="hidden" name="rows" id="rows" />
			<table id="table1" class="table table-bordered table-striped" style="width:60%">
				<tr>
					<td>Perfix :</td>
					<td><input type="text" name="prefix" id="prefix"
						onblur="prefixValidation()" class="form-control">
						<div id="prefixMsg" style="color: red;"></div></td>
					<td colspan="2"><input type="button" value="Generate"
						onclick="generateAnimals()" class='btn btn-primary' />&nbsp;&nbsp;&nbsp;
						<input type="button" value="Edit" id="editButton"
						onclick="editFrom()" class='btn btn-primary' /></td>
					<th><font color="red" id="formMessage"></font></th>
				</tr>
				<tr>
					<th>Gender</th>
					<th>From</th>
					<th>To</th>
					<th><input type="button" value="Add" id="addButton" class='btn btn-primary'
						onclick="addAnnimalInfo()" /></th>
					<th />
				</tr>
				<tr id="row1">
					<th><select name="gender1" id="gender1" class="form-control"><option
								value="Male" selected="selected">Male</option>
							<option value="Female">Female</option></select></th>
					<td><input type="text" name="from1" id="from1"
						onkeypress="return event.charCode >= 48 && event.charCode <= 57"
						class="form-control" /><font color="red" id="from1msg"></font></td>
					<td><input type="text" name="to1" id="to1"
						onkeypress="return event.charCode >= 48 && event.charCode <= 57"
						class="form-control" /><font color="red" id="to1msg"></font>
					<td><input type="button" value="Remove" id="remove1"
						onclick="removeAnnimalInfo('1')" class="btn btn-primary" /></td>
				</tr>
			</table>
			<div id="animalslist"></div>
			<div id="submitButton" style="width:100%;margin-top:20px">
				<input type='button' value='Save' onclick='submitFrom()' style="margin-left:45%"
					id="savebutton" disabled="disabled" class="btn btn-primary" />
			</div>
		</form:form>
		</div>
	</div>
	<c:if test="${viewAnimals}">
		<div class="card">
			<div class="card-header">
				<h3 class="card-title"><b>Animal Accession View</b></h3>
			</div>
			<div class="card-body">
			<table class="table table-bordered table-striped" style="width:60%">
				
				
<%-- 				<c:forEach items="${saaList}" var="saa" varStatus="st"> --%>
<%-- 					<c:if test="${st.count eq 1}"> --%>
<!-- 						<tr> -->
<!-- 							<td>Prefix :</td> -->
<%-- 							<td colspan="3">${saa.prefix}</td> --%>
<!-- 						</tr> -->
<%-- 					</c:if> --%>
<%-- 				</c:forEach> --%>
				<c:forEach items="${total}" var="saa">
					<tr>
						<td>Gender :</td>
						<td>${saa.key}</td>
						<td>No of Animals :</td>
						<td>${saa.value}</td>
					</tr>
				</c:forEach>
			</table>
			<table class="table table-bordered table-striped" style="width:60%">
				<c:forEach items="${animals}" var="sa" varStatus="st">
					<tr>
						<th colspan="6">${sa.key}</th>
					</tr>
					<c:forEach items="${sa.value}" var="anims">
						<tr>
							<c:forEach items="${anims}" var="a">
								<td>${a.animalNo}${a.genderCode} </td>
							</c:forEach>
						</tr>
					</c:forEach>
					
				</c:forEach>
			</table>
			</div>
		</div>
	</c:if>
	
	
</body>
<script type="text/javascript">
	function prefixValidation() {
		$("#prefixMsg").html("");
		var prefix = $("#prefix").val();
		if (prefix == '') {
			$("#prefixMsg").html("Required Field");
			return false;
		} else
			return true;
	}
</script>
<script type="text/javascript">
	function gneetateValidation() {
		$("#formMessage").html("");
		if (rowCount < 1) {
			$("#formMessage").html(
					"At leat one gender and From and To animial required.");
			return false;
		} else {
			var generateflag = true;
			for (var row = 1; row <= rowCount; row++) {
				var rowFlag = true;
				$.each(removedRows, function(k, v) {
					if (row == v) {
						rowFlag = false;
					}
				});
				if (rowFlag) {
					
					$("#from" + row + "msg").html("")
					$("#to" + row + "msg").html("")
					var from = $("#from" + row).val();
					var to = $("#to" + row).val();
					var animalnoflag = true;
					if (from === undefined || from == '') {
						$("#from" + row + "msg").html("Required Field");
						animalnoflag = false;
					}
					if (to === undefined || to == '') {
						$("#to" + row + "msg").html("Required Field");
						animalnoflag = false;
					}
					if (animalnoflag) {
						from = Number(from); to = Number(to);
						if (from > to) {
							$("#to" + row + "msg")
									.html(
											"Animal From should be graterthan to Animal");
							generateflag = false;
						}
					} else {
						generateflag = false;
					}

				} else {
					generateflag = false;
				}
			}
			var ranges = [];
			console.log(generateflag);

			if (generateflag) {
				for (var row = 1; row <= rowCount; row++) {
					var rowFlag = true;
					$.each(removedRows, function(k, v) {
						if (row == v) {
							rowFlag = false;
						}
					});
					if (rowFlag) {
						var from = Number($("#from" + row).val());
						var to = Number($("#to" + row).val());
						$.each(ranges, function(k, v) {
							console.log(v);
						});
						$.each(ranges, function(k, v) {
							var range = v.split(",");
							var fromold = Number(range[0]);
							var toold = Number(range[1]);
							if (from >= fromold && from <= toold) {
								$("#from" + row + "msg").html(
										fromold + " should no between " + from
												+ " and " + to);
								generateflag = false;
							}

							if (to >= fromold && to <= toold) {
								$("#to" + row + "msg").html(
										toold + " should no between " + from
												+ " and " + to);
								generateflag = false;
							}
						});
						ranges.push(from + "," + to);
					}
				}
			}
			return generateflag;
		}
	}
</script>
<script type="text/javascript">
	var rowCount = 1;
	var removedRows = [];
	function addAnnimalInfo() {
		rowCount++;
		var row = "<tr id='row"+rowCount+"'>";
		row += "<td><select name='gender"+rowCount+"' id='gender"+rowCount+"' class='form-control'><option value='Male' selected='selected' class='form-control'>Male</option>";
		row += "<option value='Female'>Female</option></select></td>";
		row += "<td><input type='text' name='from"+rowCount+"' id='from"+rowCount+"' onkeypress='return event.charCode >= 48 && event.charCode <= 57' class='form-control'/>"
				+ "<font color='red' id='from"+rowCount+"msg'></font></td>";
		row += "<td><input type='text' name='to"+rowCount+"' id='to"+rowCount+"' onkeypress='return event.charCode >= 48 && event.charCode <= 57' class='form-control'/>"
				+ "<font color='red' id='to"+rowCount+"msg'></font></td>"
				+ "<td><input type='button' value='Remove' id='remove"+rowCount+"' onclick='removeAnnimalInfo("
				+ rowCount + ")' class='btn btn-primary'/>";
		row += "</td></tr>";
		console.log(row);
		$("#table1 tbody").append(row);

		// 		$("#submitButton").html(
		// 				"<input type='button' value='Save' onclick='submitFrom()'/>")
	}
	function removeAnnimalInfo(row) {
		removedRows.push(row);
		$('#row' + row).remove();
		rowCount--;
	}
</script>
<script>
	function generateAnimals() {
		var prefix = prefixValidation();
		$("#animalslist").html("");
		var generate = gneetateValidation();
		if (prefix && generate) {
			prefix = $("#prefix").val();
			var tdcount = 1;
			var tableData = "<table><tr>";
			for (var row = 1; row <= rowCount; row++) {

				var rowFlag = true;
				$.each(removedRows, function(k, v) {
					if (row == v) {
						rowFlag = false;
					}
				});
				if (rowFlag) {
					var gender = $("#gender" + row).val();
					// 				console.log(gender)
					var from = $("#from" + row).val();
					var to = $("#to" + row).val();
					if (from === undefined)
						from = 0;
					if (to === undefined)
						to = 0;
					var flag = true;
					from = parseInt(from);
					to = parseInt(to);
					// 	 			debugger;
					// 				alert(row + " " + from + " " + to)
					// form validaiton need
					for (var count = from; count <= to; count++, tdcount++) {
						// 					alert(count);
						var animalId = '';
						if ((count + "").length == 1) {
							if (gender == 'Male')
								animalId = prefix + "000" + count + "M";
							else
								animalId = prefix + "000" + count + "F";
						} else if ((count + "").length == 2) {
							if (gender == 'Male')
								animalId = prefix + "00" + count + "M";
							else
								animalId = prefix + "00" + count + "F";
						} else if ((count + "").length == 3) {
							if (gender == 'Male')
								animalId = prefix + "0" + count + "M";
							else
								animalId = prefix + "0" + count + "F";
						} else if (gender == 'Male')
							animalId = prefix + count + "M";
						else
							animalId = prefix + count + "F";
						tableData += "<TD><input type='checkbox' name='animalId' value=\""+animalId+"\" checked='checked'>&nbsp;"
								+ animalId + "</TD>";
						if (tdcount % 5 == 0) {
							tableData += "</tr><tr>";
						}
						// 					alert(tableData);
					}
					console.log(tableData);
				}
			}
			tableData += "</tr></table>";
			// 		console.log(tableData);
			$("#animalslist").html(tableData);
			$("#savebutton").removeAttr("disabled");
			$("#addButton").attr("disabled", true);
			for (var row = 1; row <= rowCount; row++) {
				var rowFlag = true;
				$.each(removedRows, function(k, v) {
					if (row == v) {
						rowFlag = false;
					}
				});
				if (rowFlag) {
					$("#from"+row).attr("readonly", true);
					$("#to"+row).attr("readonly", true);
					$("#remove"+row).attr("disabled", true);
				}
			}
		}

	}
	function editFrom() {
		$("#animalslist").html("");
		$("#savebutton").attr("disabled", true);
		
		for (var row = 1; row <= rowCount; row++) {
			var rowFlag = true;
			$.each(removedRows, function(k, v) {
				if (row == v) {
					rowFlag = false;
				}
			});
			if (rowFlag) {
				$("#from"+row).removeAttr("readonly");
				$("#to"+row).removeAttr("readonly");
				$("#remove"+row).removeAttr("disabled");
			}
		}
		
		$("#addButton").removeAttr("disabled");

	}
</script>
<script>
	function submitFrom() {$("#formMessage").html("");
		if (prefixValidation()) {
			var rows = [];
			for (var row = 1; row <= rowCount; row++) {
				var flag = true;
				$.each(removedRows, function(k, v) {
					console.log(row + "-" + v + "   " + (row != v));
					if (row == v) {
						flag = false;
					}
				});
				if (flag)
					rows.push(row);
			}
			$("#rows").val(rows.toString());
		
			var animal = 0;
			$.each($("input[name='animalId']:checked"), function() {
				animal ++;
			});
			if(animal > 0){
				$("#accessionDataForm").submit();
			}else{
				$("#formMessage").html(
				"Please, Select atleat one animal.");
			} 			
			
		}

	}
</script>
</html>