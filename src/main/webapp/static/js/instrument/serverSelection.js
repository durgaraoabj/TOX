$(document).ready(function() {
	if ('${ipAddressPk}' != 0) {
		$("#ipaddress").val('${ipAddressPk}');
	}
	// $("#SYSMEXTR").hide();
	$("#STAGOTR").hide();
	$("#VITROSTR").hide();

	if ($("#serviceType0").val() != null) {
		// debugger;
		displayServerSelectoins($("#serviceType0").val());
	}
});

function displayServerSelectoins(value) {
	debugger;
//	$("#studyIdMsg").html("");
//	if ($("#studyId").val() != -1) {
		$("#insturmetnData").html("");
		// debugger;
		// var value = $('input[name="instrument"]:checked').val();
		if (value == 'SYSMEX') {
			callSysmexService();
			// $("#SYSMEXTR").show();
			$("#STAGOTR").hide();
			// $("#VITROSTR").hide();
		} else if (value == 'STAGO') {
			// debugger;
//			studyStagoTestCodes();
			callStagoService();
			// $("#SYSMEXTR").hide();
			$("#STAGOTR").show();
			// $("#VITROSTR").hide();
		} else if (value == 'VITROS') {
			callVistrosService();
			// $("#SYSMEXTR").hide();
			$("#STAGOTR").hide();
			// $("#VITROSTR").show();

		}
//	} else {
//		$("#studyIdMsg").html("Required Field");
//		$("#SYSMEXTR").hide();
//		$("#STAGOTR").hide();
//		$("#VITROSTR").hide();
//		$("#insturmetnData").html("");
//	}

}
function sampleType() {
	$("#sampleTypeMsg").html("");
	$("#loatNo").val("");
	$("#loatNo").attr("disabled", true);
	if ($("#sampleType").val() != -1) {
		if ($("#sampleType").val() == 'QC') {
			$("#loatNo").removeAttr("disabled");
		} else {
			callStagoService();
		}
	} else
		$("#sampleTypeMsg").html("Required Field");

}

function studyStagoTestCodes() {
	$("#test").html("");
	var studyId = $("#studyId").val();
	var result = asynchronousAjaxCall(mainUrl + "/sysmex/studyStagoTestCodes/"
			+ studyId);
	if (result != '' || result != undefined) {
		var v = "<option value=\"-1\" selected=\"selected\">--Select--</option>";
		$.each(result, function(index, studyTestcode){
			v += "<option value=\""+studyTestcode.testCode.testCode+"\">"+studyTestcode.testCode.disPalyTestCode+"</option>";
		});
		debugger;
		$("#test").html(v);
	}

}

function callSysmexService() {
	var studyId = $("#studyId").val();
	$('#insturmetnData').html('');
	// if (value != -1) {
	// alert(mainUrl+"/sysmax/callSysmexService/"+value)
	var result = asynchronousAjaxCall(mainUrl + "/sysmex/callSysmexService/0");
	if (result != '' || result != undefined) {
		console.clear();
		console.log(result);
		var data = result;
		console.log(data);
		var header = "<input type=\"button\" value=\"Goto Report Page\" id=\"restId\" onclick=\"gotoReportPage('SYSMEX')\"> ";
		header += '<table id="instrumentDataTable" class="table table-bordered table-striped"><thead><tr>';
		// header += "<th id='studyNo'>Study No.</th>";
		// header += "<th id='animalNo'>Animal No.</th>";
		// header += "<th id='startTime'>Start Time</th>";
		// console.log(data.length);
		var sysMaxTestCodesMap = data.sysMaxTestCodesMap;
		$.each(sysMaxTestCodesMap, function(ind, value) {
			console.log(ind + "   " + value);
			header += "<th id=\"" + ind + "\">" + value + "</th>";
		});
		header += "</tr><tr>";
		var sysMaxTestCodesUnitsMap = data.sysMaxTestCodesUnitsMap;
		$.each(sysMaxTestCodesUnitsMap, function(ind, value) {
			console.log(ind + "   " + value);
			header += "<th id=\"" + ind + "\">" + value + "</th>";
		});
		header += "</tr></thead><tbody id='instrumentDataTableBody'></tbody></table>";

		// debugger;
		$("#insturmetnData").html(header);

	}
	// } else {

	// }
}
function gotoReportPage(service) {
	$("#serviceType").val(service);
	$("#callReportService").submit();
}
function callStagoService() {
	if (checkStagoValidation()) {
		// debugger;
//		var studyId = $("#studyId").val();
		var url = mainUrl + "/sysmex/callStagoService/"
				+ $("#sampleType").val() + "/NA/" + $("#test").val();
		if ($("#sampleType").val() == 'QC') {
			url = mainUrl + "/sysmex/callStagoService/"
					+ $("#sampleType").val() + "/" + $("#loatNo").val() + "/"
					+ $("#test").val();
		}
		var result = asynchronousAjaxCall(url);
		if (result != '' || result != undefined) {
//			alert(result)
			var databody = $("#insturmetnData").html();
			databody = databody.trim();
			// alert(databody);

			if (databody == '') {

				var header = '<table  class="table table-bordered table-striped"><thead><tr>';
				console.log(result.stagoTestCodesMap.length);
				// console.log(result.stagoTestCodesMap.length());
				console.log(result.stagoTestCodesMap.size);
				// console.log(result.stagoTestCodesMap.size());
				var map = result.stagoTestCodesMap;
				$.each(map, function(k, value) {
					header += '<th>' + value + '</td>';
				});
				header += "</tr><tr>";
//				map = result.stagoTestCodesUnitsMap;
//				$.each(map, function(k, value) {
//					header += '<th>' + value + '</td>';
//				});
				header += '</tr></thead><tbody id=\"instrumentDataTableBody\"></tbody><table>';
				$("#insturmetnData").html(header);
				$("#restId").removeAttr("disabled");
			}
		}
	}
}
function resetStago() {
	$("#sampleType").val(-1);
	$("#test").val(-1);
	// $("#study").removeAttr("disabled");
	// $("#test").removeAttr("disabled");
	// $("#comport").removeAttr("disabled");
	// $("#configre").removeAttr("disabled");
	$("#loatNo").val("");
	$("#loatNo").attr("disabled", true);
	$("#restId").attr("disabled", true);
	var result = asynchronousAjaxCall(mainUrl + "/sysmex/stopStagoService");
	if (result != '' || result != 'undefined') {
		// alert(result);
		$('#insturmetnData').html('');
	}
}
function checkStagoValidation() {
	$("#sampleTypeMsg").html("");
	$("#testMsg").html("");
	$("#loatNoMsg").html("");
	var flag = true;
	if ($("#sampleType").val() == -1) {
		$("#sampleTypeMsg").html("Required Field");
		flag = false;
	} else if ($("#sampleType").val() == 'QC'
			&& $("#loatNo").val().trim() == '') {
		$("#loatNoMsg").html("Required Field");
		flag = false;
	}
	if ($("#test").val() == -1) {
		$("#testMsg").html("Required Field");
		flag = false;
	}
	if ($("#study").val() == -1) {
		$("#studyMsg").html("Required Field");
		flag = false;
	}
	return flag;
}

function callVistrosService() {
	debugger;
	var result = asynchronousAjaxCall(mainUrl + "/sysmex/callVistrosService");
	if (result != '' || result != undefined) {
		console.clear();
		console.log(result);
		var data = result;
		console.log(data);
		var header = "<input type=\"button\" value=\"Goto Report Page\" id=\"restId\" onclick=\"gotoReportPage('SYSMEX')\"> ";
		header += '<table id="instrumentDataTable" class="table table-bordered table-striped"><thead><tr>';
		// header += "<th id='studyNo'>Study No.</th>";
		// header += "<th id='animalNo'>Animal No.</th>";
		// header += "<th id='startTime'>Start Time</th>";
		// console.log(data.length);
		var sysMaxTestCodesMap = data.sysMaxTestCodesMap;
		$.each(sysMaxTestCodesMap, function(ind, value) {
			console.log(ind + "   " + value);
			header += "<th id=\"" + ind + "\">" + value + "</th>";
		});
		header += "</tr><tr>";
		var sysMaxTestCodesUnitsMap = data.sysMaxTestCodesUnitsMap;
		$.each(sysMaxTestCodesUnitsMap, function(ind, value) {
			console.log(ind + "   " + value);
			header += "<th id=\"" + ind + "\">" + value + "</th>";
		});
		header += "</tr></thead><tbody id='instrumentDataTableBody'></tbody></table>";

		debugger;
		$("#insturmetnData").html(header);

	}

}
$(document).ready(
		function() {
			var url = mainUrl + "/sysmexWebSocket/fatchData";
			var eventSource = new EventSource(url);
			eventSource.addEventListener("sysmesinsData", function(event) {
				var articalData = JSON.parse(event.data);
//				alert("Event : " + event.data);
//				 alert(articalData.id +" - "+ articalData.collected +" - "+
//				 articalData.color)
				addBlock(articalData.id, articalData.status,
						articalData.dataFrom, articalData.ipAddress);
			});
		});
function addBlock(id, status, dataFrom, ipAddress) {
//	alert(id)
	if (id != '0' && status == '200') {
		console.clear();
//		alert(dataFrom);
		// var old = $("#insturmetnData").html();
		debugger;
		var v = $("#instrumentDataTableBody").html();
		$("#instrumentDataTableBody").empty("");
		$("#instrumentDataTableBody").append(dataFrom);
		$("#instrumentDataTableBody").append(v);
		// $('#instrumentDataTable tr:last').after(dataFrom);
	}

}

$(document).ready(
		function() {
			var url = mainUrl + "/stagoWebSocket/fatchStagoData";
			var eventSource = new EventSource(url);
			eventSource.addEventListener("stagoinsData", function(event) {
				var articalData = JSON.parse(event.data);
				console.log("Event : " + event.data);
				// alert(articalData.id +" - "+ articalData.collected +" - "+
				// articalData.color)
				addBlocksStago(articalData.id, articalData.status,
						articalData.dataFrom, articalData.portNo)
			});
		});
function addBlocksStago(id, status, dataFrom, portNo) {

	if (id != '0' && status == '200') {
		$("#instrumentDataTableBody").append(dataFrom);
		// var old = $("#insturmetnData").html();
		// $("#stagoData").append("");
		// $("#insturmetnData").html(old + "<br/>" + dataFrom);
	}

}

$(document).ready(
		function() {
			var url = mainUrl + "/vistrosWebSocket/fatchVistorsData";
			var eventSource = new EventSource(url);
			eventSource.addEventListener("vistrosData", function(event) {
				var articalData = JSON.parse(event.data);
				console.log("Event : " + event.data);
				// alert(articalData.id +" - "+ articalData.collected +" - "+
				// articalData.color)
				addBlockVistros(articalData.id, articalData.status,
						articalData.dataFrom, articalData.portNo)
			});
		});
function addBlockVistros(id, status, dataFrom, portNo) {
	if (id != '0' && status == '200') {
		$("#instrumentDataTableBody").append(dataFrom);
	}

}
