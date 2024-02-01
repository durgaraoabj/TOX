<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
	var subject = [];
</script>
<body>
	<input type="hidden" name="gid" id="gid" value="${subjects.group.id}" />
	<input type="hidden" name="sgid" id="sgid" value="${subjects.id}" />

	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Animal With Observation</b>
			</h3>
			<br />
			<table>
				<tr>
					<th>Group Name :</th>
					<th>${subjects.group.groupName }</th>
					<c:if test="${study.requiredSubGroup}">
						<th>Sub Group Name :</th>
						<th>${subjects.name }</th>
					</c:if>
				</tr>
			</table>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped">
				<tr>
					<th colspan="6">Observation Name</th>
				</tr>
				<!-- 			 </table> -->
				<c:forEach items="${obvList}" var="obv">
					<tr>

						<td>${obv.crf.observationName }</td>
						<td><a
							href="<c:url value="/studyReview/reviewObservationForm/${obv.id}/review"/>"><input
								type="button" value="Review" class="btn btn-primary btn-sm"></a></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<%-- <c:when test="${crf.needToReview gt 0 }">
					<input type="button" id="crfDataEntry" value="IN REVIEW" onclick="crfDataReviewView('${sub.id}', '${crf.id}')"
								class="btn btn-primary" style="width: 150px;"/>
				</c:when>
				<c:when test="${crf.needToReview < 0 }">
					<input type="button" id="crfDataEntry" value="REVIEWED" onclick="crfDataReviewViewOnly('${sub.id}', '${crf.id}')"
								class="btn btn-primary" style="width: 150px;"/>
				</c:when>
				<c:otherwise>
					<input type="button" id="crfDataEntry" value="Veiw" onclick="crfDataEntry('${sub.id}', '${crf.id}')"
					class="btn btn-primary" style="width: 150px;" disabled="disabled"/>	
				</c:otherwise> --%>
	<c:url value="/studyReview/animalCrfDataReviewView" var="dataEntryUrl" />
	<sf:form action="${dataEntryUrl}" method="POST" id="entryForm">
		<input type="hidden" name="review" id="review" value="review" />
		<input type="hidden" name="groupId" id="groupId1" />
		<input type="hidden" name="subGroupId" id="subGroupId1" />
		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1" />
		<!--subejct -->
		<input type="hidden" name="stdSubGroupObservationCrfsId"
			id="stdSubGroupObservationCrfsId" />
		<!-- observationId -->
	</sf:form>
</body>
<script type="text/javascript">
	var justBefore = "";
	$.each(subject, function(k, v) {
		$("#subject_" + v).hide();
	});
	function showSubGroup(id) {
		$.each(subject, function(k, v) {
			$("#subject_" + v).hide();
		});
		if (justBefore != id) {
			$("#subject_" + id).show();
			justBefore = id;
		} else {
			justBefore = "";
		}
	}

	function crfDataReviewViewOnly(subId, stdSubGroupObservationCrfsId) {
		$("#review").val("view");
		$("#groupId1").val($("#gid").val());
		$("#subGroupId1").val($("#sgid").val());
		$("#subGroupInfoId1").val(subId);
		$("#stdSubGroupObservationCrfsId").val(stdSubGroupObservationCrfsId);
		$("#entryForm").submit();
	}
	function crfDataReviewView(subId, stdSubGroupObservationCrfsId) {
		$("#groupId1").val($("#gid").val());
		$("#subGroupId1").val($("#sgid").val());
		$("#subGroupInfoId1").val(subId);
		$("#stdSubGroupObservationCrfsId").val(stdSubGroupObservationCrfsId);
		$("#entryForm").submit();
	}
</script>
</html>