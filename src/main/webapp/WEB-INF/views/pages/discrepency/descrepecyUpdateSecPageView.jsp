<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<body>
	<br />
	<c:url value="/discrepancy/discrepancyCloseOrUpdate" var="urlval" />
	<sf:form action="${urlval}" method="POST" modelAttribute="crfpojo"
		id="updatedesc" enctype="multipart/form-data">
		<input type="hidden" name="id" id="id" value="${scd.id}" />
		<input type="hidden" name="eleType" id="eleType" value="${eleType}" />
		<input type="hidden" name="newValue2" id="newValue2" value="" />
		<input type="hidden" name="type" id="type" value="${type}" />
		<table class="table table-bordered table-striped">
			<tr>
				<th>Study No</th>
				<td>${scd.study.studyNo }</td>
				<tH>Animal No</th>
				<td>${scd.accAnimalDataEntryDetails.animal.animalNo  }</td>

			</tr>
			<tr>
				<th>Observation</th>
				<td>${scd.crf.observationName }</td>
				<th>Field</th>
				<td>${scd.secElement.name }</td>
			<tr>
				<th>Status</th>
				<td>${scd.status }</td>
				<th>Old Status</th>
				<td>${scd.oldStatus }</td>
			</tr>
			<tr>
				<th>Value</th>
				<td>${scd.stydyAccCrfSecEleData.value  }</td>
				<th>Old Value</th>
				<td>${scd.oldValue }</td>
			</tr>
			<tr>
				<th>Raised By</th>
				<td>${scd.risedBy }</td>
				<th>Raised On</th>
				<td>${scd.createdOn }</td>
			</tr>
			<tr>
				<th>New Value</th>
				<td><c:choose>
						<c:when test="${eleType eq 'staticData'}">
							<textarea rows="3" cols="10" name="newValue" id="newValue"></textarea>
						</c:when>
						<c:when test="${eleType eq 'text'}">
							<input type="text" name="newValue" id="newValue">
						</c:when>
						<c:when test="${eleType eq 'textArea'}">
							<textarea rows="3" cols="10" name="newValue" id="newValue"></textarea>
						</c:when>
						<c:when test="${eleType eq 'date'}">
							<input type="text" name="newValue" id="newValue" 
								autocomplete="off">
							<script type="text/javascript">
								$(function() {
									$("#newValue").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
						</c:when>
						<c:when test="${eleType eq 'dateAndTime'}">
							<input type="text" name="newValue" id="newValue">
							<input type="text" name="newValue" id="newValue"
								autocomplete="off">
							<script type="text/javascript">
								$(function() {
									$("#newValue").datepicker({
										dateFormat : $("#dateFormatJsp").val(),
										changeMonth : true,
										changeYear : true
									});
								});
							</script>
						</c:when>
						<c:when test="${eleType eq 'time'}">
							<input class="form-control" type="text" name="newValue"
								id="newValue" placeholder="Now" autocomplete="off">
							<script type="text/javascript">
								var input = $('#newValue').clockpicker({
									placement : 'bottom',
									align : 'left',
									autoclose : true,
									'default' : 'now'
								});
							</script>
						</c:when>
						<c:when test="${eleType eq 'non'}">
							<input type="text" name="newValue" id="newValue">
						</c:when>
					</c:choose> <c:choose>
						<c:when test="${eleType eq 'radioLabel'}">
						${htmlField}
					</c:when>
						<c:when test="${eleType eq 'radio'}">
						${htmlField}
					</c:when>
					</c:choose> <c:if test="${eleType eq 'checkBox'}">${htmlField}</c:if> <c:if
						test="${eleType eq 'select'}">${htmlField}</c:if> <c:if
						test="${eleType eq 'selectTable'}">${htmlField}</c:if></td>
				<th>Comment</th>
				<td><input type="text" name="comment" id="comment"></td>
			</tr>
			<tr>
				<th>Assign To</th>
				<td><select id="userId" name="userId">
						<option value="-1" selected="selected">--select--</option>
						<c:forEach items="${allLoginUsers}" var="user">
							<option value="${user.id}">${user.username}</option>
						</c:forEach>
				</select> <font color="red" id="userIdmsg"></font></td>
				<th>Change Status</th>
				<td><select id="status" name="status">
						<option value="closed" selected="selected">Close</option>
						<option value="onHold">onHold</option>
				</select> <font color="red" id="statusmsg"></font></td>
			</tr>
			<c:if test="${userRole ne 'Study Director'}">
				<tr>
					<td colspan="4"><input type="button" class="btn btn-primary"
						onclick="saveDisc()" value="Update" /></td>
				</tr>
			</c:if>

		</table>
	</sf:form>
</body>
<script type="text/javascript">
	function saveDisc() {
		var v = $("#eleType").val();
		if (v == 'radio' || v == 'checkBox') {
			var flag = false;
			var values = "";
			$.each($("input[name='newValue']:checked"), function() {
				if (flag) {
					values = values + "#####" + $(this).val();
				} else {
					flag = true;
					values = $(this).val();
				}
			});
			$("#newValue2").val(values);
		} else {
			$("#newValue2").val($("#newValue").val());
		}
		$("#updatedesc").submit();
	}
</script>
</html>