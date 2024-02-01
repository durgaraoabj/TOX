<%@ page isELIgnored="false"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
<link rel="stylesheet"
	href='<c:url value="/static/calenderEvent/lib/main.css"/>'>
<script src='<c:url value ="/static/calenderEvent/lib/main.js"/>'></script>

<style>
body {
	margin: 10px 5px;
	padding: 0;
	font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
	font-size: 14px;
}

#calendar {
	max-width: 100%;
	max-height: 450px;
	margin: 0 auto;
}
</style>
<style type="text/css">
.fadeInRight {
	-webkit-animation-name: fadeInRight;
	animation-name: fadeInRight;
}

@
keyframes fadeInRight {from { opacity:0;
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0);
}

to {
	opacity: 1;
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0);
}

}
@
-webkit-keyframes fadeInRight {from { opacity:0;
	-webkit-transform: translate3d(100%, 0, 0);
	transform: translate3d(100%, 0, 0);
}

to {
	opacity: 1;
	-webkit-transform: translate3d(0, 0, 0);
	transform: translate3d(0, 0, 0);
}

}
.modal-dialog {
	margin: 0;
}

.animated {
	-webkit-animation-duration: 0.5s;
	animation-duration: 0.5s;
	-webkit-animation-fill-mode: both;
	animation-fill-mode: both;
}

.modal-header {
	border-radius: 0;
	background-color: #f7f7f7;
	border-color: #d4d2d2;
	align-items: center;
	padding: 1.2rem;
}

.modal-content {
	border-radius: 0;
	border: 0;
	-webkit-box-shadow: -12px 0 38px -14px rgba(0, 0, 0, 0.25);
	-moz-box-shadow: -12px 0 38px -14px rgba(0, 0, 0, 0.25);
	box-shadow: -12px 0 38px -14px rgba(0, 0, 0, 0.25);
	background-clip: padding-box;
}

.modal-backdrop {
	background-color: transparent;
}

.modal-button-container {
	margin: 0 auto;
	width: 90%;
	max-width: 500px;
}

.btn {
	border-radius: 0;
}
</style>
<script type="text/javascript">
	var accessionjson = {};
	var treatmentjson = {};
</script>
</head>
<body>
	<div class="card">
		<!-- /.card-header -->
		<div class="card-body">
			<div id='calendar'></div>
		</div>
	</div>
	<c:forEach items="${expData}" var="mp">
		<c:forEach items="${mp.value}" var="obj">
			<div id="${obj.id}_${mp.key}" style="display: none">
				<table>
					<tr>
						<th>Animal</th>
						<th>Observation Status</th>
						<c:forEach begin="1" step="1"
							end="${reviewLevel.observationApprovelLevel}" var="v">
							<th>Review-${v}</th>
							<!-- 								<th>Date </th> -->
						</c:forEach>
					</tr>
					<c:forEach items="${obj.stdSubGroupObservationCrfs.animals}"
						var="animal">
						<tr>
							<td><a
								href='<c:url value="/studyExecution/animalCrfDataEntryGet/${animal.id}/${obj.stdSubGroupObservationCrfs.id}/${obj.stdSubGroupObservationCrfs.subGroupInfo.id}"/>'>${animal.animalNo}</a>
							</td>
							<td>&nbsp;&nbsp;&nbsp;${animal.reviewStatus}</td>
							<c:forEach items="${animal.livels}" var="level">
								<td>${level.user.username}<br />${level.date}&nbsp;&nbsp;&nbsp;</td>
							</c:forEach>
						</tr>
					</c:forEach>
				</table>

			</div>
		</c:forEach>

	</c:forEach>





	<!-- 	<!-- Modal -->

	<!-- 	<div class="modal" id="flyoutmodal" tabindex="-1" role="dialog" -->
	<!-- 		aria-hidden="true"> -->
	<!-- 		<div class="modal-dialog fadeInRight animated ml-auto" role="document"> -->
	<!-- 			<div class="modal-content"> -->
	<!-- 				<div class="modal-header"> -->
	<!-- 					<h5 class="modal-title" id="exampleModalLongTitle"> -->
	<!-- 						<font id="dateOfObj" style="font-style: b;"></font><br /> <font -->
	<!-- 							id="observation"></font> -->
	<!-- 					</h5> -->
	<!-- 					<button type="button" class="close" data-dismiss="modal" -->
	<!-- 						aria-label="Close"> -->
	<!-- 						<span aria-hidden="true">&times;</span> -->
	<!-- 					</button> -->
	<!-- 				</div> -->
	<!-- 				<div class="modal-body"> -->
	<!-- 					<div id="animalData"></div> -->
	<!-- 							        <p>Kid, I've flown from one side of this galaxy to the other. I've seen a lot of strange stuff, but I've never seen anything to make me believe there's one all-powerful Force controlling everything. There's no mystical energy field that controls my destiny. It's all a lot of simple tricks and nonsense. I want to come with you to Alderaan. There's nothing for me here now. I want to learn the ways of the Force and be a Jedi, like my father before me.</p> -->
	<!-- 							        <p>I have traced the Rebel spies to her. Now she is my only link to finding their secret base. The Force is strong with this one. I have you now. Still, she's got a lot of spirit. I don't know, what do you think?</p> -->
	<!-- 				</div> -->
	<!-- 				<div class="modal-footer"> -->
	<!-- 					<button type="button" class="btn btn-secondary" -->
	<!-- 						data-dismiss="modal">Close</button> -->
	<!-- 							        <button type="button" class="btn btn-primary">Submit</button> -->
	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->

	<c:forEach items="${acclamatizationMap}" var="accMap">
		<script>
			console.log()
			var map = {};
			map['primaryKey'] = '${accMap.value.primaryKey}';
			map['gender'] = '${accMap.value.gender}';
			map['navigation'] = '${accMap.value.navigation}';
			map['crfId'] = '${accMap.value.crfId}';
			map['studyId'] = '${accMap.value.studyId}';
			map['action'] = '${accMap.value.action}';
			map['divId'] = '${accMap.value.divId}';
			map['title'] = '${accMap.value.title}';
			map['start'] = '${accMap.value.start}';
			map['end'] = '${accMap.value.end}';
			map['windowPre'] = '${accMap.value.windowPre}';
			map['windowPost'] = '${accMap.value.windowPost}';
			map['studyAcclamatizationDatesId'] = '${accMap.value.studyAcclamatizationDatesId}';
			map['studyAcclamatizationDatesId'] = '${accMap.value.studyAcclamatizationDatesId}';
			if ('${accMap.value.clinPaht}' == 'true')
				map['clinPaht'] = true;
			else
				map['clinPaht'] = false;
			accessionjson['${accMap.key}'] = map;
		</script>
	</c:forEach>
	<c:forEach items="${treatmentMap}" var="accMap">
		<script>
			console.log()
			var map = {};
			map['primaryKey'] = '${accMap.value.primaryKey}';
			map['gender'] = '${accMap.value.gender}';
			map['navigation'] = '${accMap.value.navigation}';
			map['crfId'] = '${accMap.value.crfId}';
			map['studyId'] = '${accMap.value.studyId}';
			map['action'] = '${accMap.value.action}';
			map['divId'] = '${accMap.value.divId}';
			map['title'] = '${accMap.value.title}';
			map['start'] = '${accMap.value.start}';
			map['end'] = '${accMap.value.end}';
			map['subGroupId'] = '${accMap.value.subGroupId}';
			map['windowPre'] = '${accMap.value.windowPre}';
			map['windowPost'] = '${accMap.value.windowPost}';
			map['studyTreatmentDataDatesId'] = '${accMap.value.studyTreatmentDataDatesId}';
			if ('${accMap.value.clinPaht}' == 'true')
				map['clinPaht'] = true;
			else
				map['clinPaht'] = false;
			treatmentjson['${accMap.key}'] = map;
		</script>
	</c:forEach>

	<c:url value="/accession/viewEntryForm" var="saveValues" />
	<form:form action="${saveValues}" method="post" id="valuesForm">
		<input type="hidden" name="clinPaht" id="acclamatizationClinPaht">
		<input type="hidden" name="crfId" id="crfId">
		<input type="hidden" name="studyAcclamatizationDateId"
			id="studyAcclamatizationDateId">
		<input type="hidden" name="studyAcclamatizationDataId"
			id="studyAcclamatizationDataId">
		<input type="hidden" name="type" id="type" value="scheduled">
		<input type="hidden" name="seletedGender" id="seletedGender"
			value="${study.gender}">
		<input type="hidden" name="studyId" id="studyId" value="${study.id}">
		<input type="hidden" name="selecteDate" id="selecteDate">
	</form:form>
	<c:url value="/studyExecution/treatmentAnimalCrfDataEntry"
		var="dataEntryUrl" />
	<form:form action="${dataEntryUrl}" method="POST"
		id="treatmentEntryForm">
		<input type="hidden" name="clinPaht" id="treatmentClinPaht">
		<input type="hidden" name="studyTreatmentDataDatesId"
			id="studyTreatmentDataDatesId">
		<input type="hidden" name="seletedGender" id="treatmentSeletedGender"
			value="${study.gender}">
		<input type="hidden" name="crfId" id="treatmentCrfId">
		<input type="hidden" name="subGroupId" id="treatmentSubGroupId" />
		<input type="hidden" name="type" id="type" value="scheduled">
		<input type="hidden" name="stdSubGroupObservationCrfsId"
			id="stdSubGroupObservationCrfsId">
		<input type="hidden" name="selecteDate" id="treatmentSelecteDate">
	</form:form>

	<div class="modal fade" id="configDataModal" tabindex="-1"
		role="dialog" aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Select Gender</h4>
				</div>
				<div class="modal-body">
					<div id="configData">
						<table>
							<tr>
								<td>Gender</td>
								<td><input type="radio" name="dgender" value="Male"
									onchange="viewCrfPage()">Male <input type="radio"
									name="dgender" value="Female" onchange="viewCrfPage()">Female
									<font color="red" id="dgenderMsg"></font></td>
							</tr>
						</table>
					</div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox()" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#configDataModal').modal('hide');
		$('input[name="dgender"]:checked').prop('checked', false);
	});
	function acceptCheckBox() {
		$('#configDataModal').modal('hide');
	}
	function viewCrfPage() {
		acceptCheckBox();
		var value = $('input[name="dgender"]:checked').val();
		if (activityType == 'Treatment') {
			$("#treatmentSeletedGender").val(value)
			$("#treatmentEntryForm").submit();
		} else {
			$("#seletedGender").val(value)
			$("#valuesForm").submit();
		}
	}

	document.addEventListener('DOMContentLoaded', function() {
		var calendarEl = document.getElementById('calendar');
		var v = '${jsonvalues}';
		json = v;
		// 		    	alert(v);
		var calendar = new FullCalendar.Calendar(calendarEl, {
			headerToolbar : {
				left : 'prev,next today',
				center : 'title',
				right : 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
			},
			initialDate : '${currentDate}',
			editable : true,
			selectable : true,
			dayMaxEvents : true, // allow "more" link when too many events
			//      navLinks: true, // can click day/week names to navigate views
			businessHours : true, // display business hours
			selectable : true,
			eventClick : function(arg) {
				//           if (confirm('Are you sure you want Id?')) {
				// 			        	  alert(arg.event.start)

				//           }
				var date = arg.event.start;
				var dateString = moment(date).format('YYYY-MM-DD');
				gotoDataEntryFrom(arg.event.id, dateString, arg.event.groupId)
				// 						 alert(arg.event.id+"_"+dateString);
				// 						 alert($("#"+arg.event.id+"_"+dateString).html());
				// 						$("#animalData").html($("#"+arg.event.id+"_"+dateString).html());
				//    			          $('#flyoutmodal').modal('show');
			},
			events : eval(v)
		});
		calendar.render();
	});

	function geteventData() {
// 		alert("geteventData")
		var result = asynchronousAjaxCall(mainUrl
				+ "/studyExecution/viewExpermantalInCalenderview");
		if (result != '' || result != 'undefined') {
			alert(result)
			$("#cc").html(result)
			return result;
		}
	}
	function gotoObservationPage(animal, observation) {
		alert("gotoObservationPage")
		var result = asynchronousAjaxCall(mainUrl
				+ "/studyExecution/animalCrfDataEntryGet/" + animal + "/"
				+ observation);
		if (result != '' || result != 'undefined') {
			alert(result);
			$("#cc").html(result)
			return result;
		}
	}
	var activityType = "";
	function gotoDataEntryFrom(dataId, data, groupId) {

		$("#seletedGender").val('${study.gender}');
		var map;
		if (groupId == 'Acclamatization') {
			map = accessionjson[dataId];
			activityType = "Acclamatization";
		} else {
			var map = treatmentjson[dataId];
			activityType = "Treatment";
		}
		var navigation, crfId, studyId, action, divId, title, start, end, subGroupId, observationGener, primaryKey, studyAcclamatizationDatesId, studyTreatmentDataDatesId;
		var clinPaht = false;
		var preWindow, postWindow;
		$.each(map, function(key, value) {

			console.log(key + "\t" + value);

			if (key == 'primaryKey') {
				primaryKey = value;
			} else if (key == 'gender') {
				observationGener = value;
			} else if (key == 'navigation') {
				navigation = value;
			} else if (key == 'crfId') {
				crfId = value;
			} else if (key == 'studyId') {
				studyId = value;
			} else if (key == 'action') {
				action = value;
			} else if (key == 'divId') {
				divId = value;
			} else if (key == 'title') {
				title = value;
			} else if (key == 'start') {
				start = value;
			} else if (key == 'end') {
				end = value;
			} else if (key == 'subGroupId') {
				subGroupId = value;
			} else if (key == 'windowPre') {
				preWindow = parseInt(value);
			} else if (key == 'windowPost') {
				postWindow = parseInt(value);
			} else if (key == 'studyAcclamatizationDatesId') {
				studyAcclamatizationDatesId = parseInt(value);
			} else if (key == 'studyTreatmentDataDatesId') {
				studyTreatmentDataDatesId = parseInt(value);
			} else if (key == 'clinPaht') {
				debugger;
				if (value == 'true' || value == true)
					clinPaht = true;
			}

// 			map['clinPaht'] = '${accMap.value.clinPaht}';
		});
		debugger;
		var startDay = new Date(data);
		var startDate = new Date(startDay.setDate(startDay.getDate()
				- preWindow));
		startDate.setHours(0, 0, 0, 0);
		startDay = new Date(data);
		var endDate = new Date(startDay
				.setDate(startDay.getDate() + postWindow));
		endDate.setHours(0, 0, 0, 0);
		var currentDate = new Date();
		currentDate.setHours(0, 0, 0, 0);
		if(true){	
			console.log('The current date is between the start and end dates.');
// 			alert(activityType)
			if (activityType == 'Treatment') {
				$("#treatmentClinPaht").val(clinPaht);
				$("#treatmentCrfId").val(crfId);
				$("#studyTreatmentDataDatesId").val(studyTreatmentDataDatesId);
				$("#stdSubGroupObservationCrfsId").val(primaryKey);
				$("#treatmentSubGroupId").val(subGroupId);
				$("#treatmentCrfId").val(crfId);
				$("#treatmentSelecteDate").val(data);
				debugger;
				// 				if ($("#treatmentSeletedGender").val() != 'Both') {
				// 					$("#treatmentEntryForm").submit();
				// 				} else {
				// 					// 					displayGenderSelection();
				$("#studyAcclamatizationDatesId").val(
						studyAcclamatizationDatesId);
				$("#treatmentSeletedGender").val(observationGener);
				$("#treatmentEntryForm").submit();

				// 				}
			} else {
				$("#acclamatizationClinPaht").val(clinPaht);
				$("#crfId").val(crfId);
				$("#studyAcclamatizationDateId").val(
						studyAcclamatizationDatesId);
				$("#studyAcclamatizationDataId").val(primaryKey);
				$("#selecteDate").val(data);
				debugger;
				$("#seletedGender").val(observationGener)
				$("#valuesForm").submit();
				// 				debugger;
				// 				if ($("#seletedGender").val() != 'Both') {
				// 					$("#valuesForm").submit();
				// 				} else {
				// 					displayGenderSelection();
				// 				}

			}
		} else if (currentDate < startDate) {
			console.log('The current date is lessthan the start');
			alert("Date Entry Not Possible Before "
					+ moment(startDate).format('YYYY-MM-DD') + ".")
		} else if (currentDate > endDate) {
			console.log('The current date is lessthan the start');
			alert("Date Entry Not Possible After "
					+ moment(endDate).format('YYYY-MM-DD') + ".")
		} else {
			console
					.log('The current date is not between the start and end dates.');
			alert("Date Entry Not Possible. Observation has out of Start "
					+ moment(startDate).format('YYYY-MM-DD') + " and End "
					+ moment(endDate).format('YYYY-MM-DD') + " Date")
		}
	}

	function displayGenderSelection() {
		$('#configDataModal').modal('show');
		$('input[name="dgender"]:checked').prop('checked', false);
	}
</script>

</html>
