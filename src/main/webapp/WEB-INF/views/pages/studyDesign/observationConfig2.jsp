<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<script src='<c:url value="/static/admintltcss/plugins/jquery/jquery.min.js"/>'></script>
	<!-- jQuery UI 1.11.4 -->
	<script src='<c:url value="/static/admintltcss/plugins/jquery-ui/jquery-ui.min.js"/>'></script>
</head>
<body>
<input type="button" value="Obser Vation" onclick="showObservations()" />
	<div class="card">
		<c:url value="/expermentalDesign/observationConfigWithSelection" var="saveconfigureCrfs0" />
		<form:form action="${saveconfigureCrfs0}" method="post" id="submitform0">
			<input type="hidden" name="sibGroupId" value="${sibGroupId}" id="sibGroupId0"/>
			<input type="hidden" name="crfIds" value="" id="crfIds"/>
		</form:form>
		<c:forEach items="${stdcrfs}" var="crf"><input type="hidden" name="ids" value="${crf.id}"></c:forEach>
		<div class="card-header">
			<h3 class="card-title">Experimental Design</h3><br/>
			<h5 class="card-title">Study : ${study.studyNo }</h5>
		</div>
	    <div class="box-body">
	    	<c:url value="/expermentalDesign/saveconfigureCrfs"
				var="saveconfigureCrfs" />
			<form:form action="${saveconfigureCrfs}" method="post" id="submitform">
				<input type="hidden" name="sibGroupId" id="sibGroupId" value="${sibGroupId }"/>
		    		<table class="table table-bordered table-striped">
						<thead>
							<tr>
								<th>
									<%--                 	<c:if test="${configureStatus eq false}"> --%>
									<%--                 		<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}"> --%>
									<div align="center">
										<input type="checkbox" id="checkall" onclick="name()" />
										<!-- 							<input type="button" id="selectCrfscomplete" -->
										<!-- 								value="Mark As Complete" class="btn btn-primary" -->
										<!-- 								style="width: 200px;"> -->
									</div> <%-- 						</c:if> --%> <%-- 					</c:if> --%>
								</th>
								<th>Observation Name</th>
								<th>Observation Description</th>
								<!-- 					<th>Gender</th> -->
								<th>Day/Week</th>
								<th>Days/Weeks</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${stdcrfs}" var="crf">
								<tr>
									<td>
										<c:choose>
											<c:when test="${crf.active}">
												<input type="checkbox" name="selected" id="selected${crf.id}"
													value="${crf.id}" checked="checked"
													onclick="checkBoxChange(this.id, ${crf.id})" />
											</c:when>
											<c:otherwise>
												<input type="checkbox" name="selected" id="selected${crf.id}"
													value="${crf.id}" onclick="checkBoxChange(this.id, ${crf.id})" />
											</c:otherwise>
										</c:choose></td>
									<td>${crf.observationName}</td>
									<td>${crf.observationDesc}</td>
									<%-- 							<td>${crf.dayType}</td> --%>
									<td>
										<c:choose>
											<c:when
												test="${crf.dayType eq 'Day' }">
												<input type="radio" name="${crf.id}dayOrWeek"
													id="${crf.id}dayType" checked="checked" value="Day"
													onclick="checkRadioChange(${crf.id}, 1)" />Day
												<input type="radio" name="${crf.id}dayOrWeek"
													id="${crf.id}weekType" value="Week"
													onclick="checkRadioChange(${crf.id}, 0)" />Week
											</c:when>
											<c:otherwise>
												<input type="radio" name="${crf.id}dayOrWeek"
													id="${crf.id}dayType" value="Day"
													onclick="checkRadioChange(${crf.id}, 1)" />Day
												<input type="radio" name="${crf.id}dayOrWeek"
													id="${crf.id}weekType" checked="checked" value="Week"
													onclick="checkRadioChange(${crf.id}, 0)" />Week
											</c:otherwise>
										</c:choose></td>
									<td><input type="text" name="${crf.id}" id="${crf.id}"
										value="${crf.days}" onchange="valueChanges(${crf.id}, this.value)" /> 
										<font color="red" id="${crf.id}_Msg"></font>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div align="center"><input type="button" id="selectCrfs" value="Configure Crfs" class="btn btn-primary" style="width: 200px;"></div>
		    </form:form>
	    </div>
    </div>
<%--     <c:url value="/expermentalDesign/createExpermentalDesignEFrom" var="url"/> --%>
<%-- 	<form:form action="${url}" method="post"  id="form" > --%>
<!-- 	    <input type="hidden" id="sibGroupId" name="sibGroupId"/> -->
<%-- 	</form:form>. --%>

<!-- TestCodes Pop Up -->
<div class="modal fade" id="basicExampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
			  aria-hidden="true">
			  <div class="modal-dialog" role="document">
			    <div class="modal-content" style="width: 150%;">
			      <div class="modal-header">
<!-- 			        <h5 class="modal-title" id="exampleModalLabel">Modal title</h5> -->
			        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			          <span aria-hidden="true">&times;</span>
			        </button>
			      </div>
			      <div class="modal-body" id="observatonInfo" style="width: 100%">
			        ...
			      </div>
			      <div class="modal-footer" id="closebut">
			        <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closebutton">Close</button>
			      </div>
			    </div>
			  </div>
			</div>
<!-- TestCodes Pop up -->

</body>
<script type="text/javascript">
	function showObservations(){
		var sibGroupId0 = $("#sibGroupId0").val();
		var url = mainUrl+'/expermentalDesign/showObservations/'+sibGroupId0;
		var result = asynchronousAjaxCall(url);
		$('#observatonInfo').html(result);
		 $('#basicExampleModal').modal('show');
	}
	function submitBtnbut(sibGroupId){
		$("#sibGroupId").val(sibGroupId);
		$("#form").submit();
	}
	
	var checkBoxStatus = {};
	var radioStatus = {};
	var valueChange = {};
		function checkBoxChange(eleId, crfId){
//	 		alert(crfId);
//	 		alert($('#'+eleId).is(":checked"))
			if($('#'+eleId).is(":checked")){
				checkBoxStatus[crfId] = 1;
			}else{
				checkBoxStatus[crfId] = 0;
			}
		}
		function checkRadioChange(id, value){
			radioStatus[id] = value;
		}
		function valueChanges(id, value){
			valueChange[id] = value;
		}
	$('#selectCrfs').click(
			function() {
				var errors = false;

				$.each($("input[name='ids']"), function() {
					$("#" + $(this).val() + "_Msg").html("");
				});
				var favorite = [];
				var days = "";
				var count = true;
				$.each($("input[name='selected']:checked"), function() {
					var messageId = $(this).val() + "_Msg";
					var error = false;
					favorite.push($(this).val());
					var v = $("#" + $(this).val()).val();
					if (v.trim() == '') {
						$("#" + $(this).val() + "_Msg").html("Required field");
						error = true;
					} else {
						// 				var idv = $("[name='"+$(this).val()+"dayOrWeek']:checked").val();
						// 				alert(idv);
						for (var i = 0; i < v.length; i++) {
// 							alert(v.charAt(i)+"  =  " + error);
							switch (v.charAt(i)) {
								case '0':
									break;
								case '1':
									break;
								case '2':
									break;
								case '3':
									break;
								case '4':
									break;
								case '5':
									break;
								case '6':
									break;
								case '7':
									break;
								case '8':
									break;
								case '9':
									break;
								case ',':
									break;
								case '-':
									break;
								default:
									error = true;
							}
// 							alert(v.charAt(i)+"  -  " + error);
							if (error) {
								$("#" + $(this).val() + "_Msg").html(
										"In-Valied Data");
								break;
							}
						}
						if (!error) {
							var eachv = v.split(",");
							$.each(eachv,
									function(k, v) {
										if (v.trim() == "") {
											$("#" + messageId).html(
													"In-Valied Data");
											error = true;
										} else if (v.toLocaleLowerCase()
												.indexOf("-") != -1) {
											var eachvv = v.split("-");
											var f = parseInt(eachvv[0]);
											var l = parseInt(eachvv[1]);
											if (f >= l) {
												$("#" + messageId).html(
														"In-Valied Data");
												error = true;
											}
										}
									});
						}
					}
					if (!error) {
						if (count) {
							days = $(this).val() + "*" + v;
							count = false;
						} else
							days = days + "**" + $(this).val() + "*" + v;
					}

					if (error) {
						errors = true;
					}
				});
				if (errors) {
					alert("From Cotains In-Valied Data")
				} else {
// 					alert("valied data")
					var dayType = [];
					$.each($("input[name='ids']"), function() {
						var v = $(
								"[name='" + $(this).val()
										+ "dayOrWeek']:checked").val();
						//	 			alert($("[name='"+$(this).val()+"dayOrWeek']:checked").val());
						//	 			alert(v);
						//	 			alert(v == undefined);
						//	 			alert(v == "undefined");
						if (v !== undefined) {
							var idv = $(this).val()
									+ "-"
									+ $(
											"[name='" + $(this).val()
													+ "dayOrWeek']:checked")
											.val();
							dayType.push(idv);
						}

					});
					//	 		alert(dayType.join(", "));
					$("#dayType").val(dayType.join(", "));

					$('#days').val(days);
					$('#crfIds').val(favorite.join(", "));

						var modifiedCheckBox = "";
						var flag = false;
						$.each(checkBoxStatus, function(k, v) {
							if(flag){
								modifiedCheckBox = modifiedCheckBox + "," + k + " - " +v;
							}else{
								modifiedCheckBox = k + " - " +v;
								flag = true;
							}		
						});
						$("#modifiedCheckBox").val(modifiedCheckBox);
						var modifiedRadio = "";
						flag = false;
						$.each(radioStatus, function(k, v) {
							if(flag){
								modifiedRadio = modifiedRadio + "," + k + " - " +v;
							}else{
								modifiedRadio = k + " - " +v;
								flag = true;
							}		
						});
						$("#modifiedRadio").val(modifiedRadio);
						var modifiedTextField = "";
						flag = false;
						$.each(valueChange, function(k, v) {
							if(flag){
								modifiedTextField = modifiedTextField + "**" + k + "*" +v;
							}else{
								modifiedTextField = k + "*" +v;
								flag = true;
							}		
						});
						$("#modifiedTextField").val(modifiedTextField);
					$('#submitform').submit();
				}
			});
</script>
</html>