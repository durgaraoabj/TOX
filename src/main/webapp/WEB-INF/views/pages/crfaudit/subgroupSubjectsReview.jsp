<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
	var subject = [];
</script>
<body>
	<input type="hidden" name="gid" id="gid" value="${subjects.group.id}"/>
	<input type="hidden" name="sgid" id="sgid" value="${subjects.id}"/>
	
	<div class="card">
		<div class="card-header">
			<h3 class="card-title"><b>Animal With Observation</b></h3><br/>
			<table>
				<tr>
					<th>Group Name :</th>
					<th>${subjects.group.groupName }</th>
					<th>Sub Group Name : </th>
					<th>${subjects.name }</th>
				</tr>
			</table>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped">
				<tr><th>Animal Number</th></tr>
				<c:forEach items="${subjects.animalInfoAll }" var="sub">
					<tr>
						<td>
							<div onclick="showSubGroup('${sub.id }')" style="color: blue">
								${sub.animalNo }&nbsp;
								<c:choose >
									<c:when test="${sub.noOfObservationsNeedToReview gt 0 }"><span class="right badge badge-danger">Available</span></c:when>
									<c:when test="${sub.noOfObservationsNeedToReview < 0 }"><span class="right badge badge-danger">Reviewed</span></c:when>
								</c:choose>
							</div>
							<div id="subject_${sub.id }">
								<c:if test="${sub.observation.size() gt 0}">
									<table class="table table-bordered table-striped">
										<tr><th colspan="3">Observation Name</th></tr>
										<c:forEach items="${sub.observation}" var="crf">
											<tr>
												<td>
													${crf.observationName }&nbsp;
													<c:choose >
														<c:when test="${crf.needToReview gt 0 }"><span class="right badge badge-danger">Available</span></c:when>
														<c:when test="${crf.needToReview < 0 }"><span class="right badge badge-danger">Reviewed</span></c:when>
													</c:choose>
												</td>
												<td>
													<c:choose >
														<c:when test="${crf.needToReview gt 0 }">
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
														</c:otherwise>
													</c:choose>
													
												</td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
							</div>
							
						</td>
					</tr>
					<script >
						subject.push('${sub.id }');
					</script>
				</c:forEach>
			</table>
		</div>
	</div>
<!-- 		<h6 class="box-title"> -->
<%-- 			Observation Name : ${crfviewTest.observationName }&nbsp;&nbsp;&nbsp;&nbsp; --%>
<%-- 	    	Observation Desc : ${crfviewTest.observationDesc }&nbsp;&nbsp;&nbsp;&nbsp; --%>
<!--     	</h6> -->
    	   
 		<c:url value="/studyReview/animalCrfDataReviewView" var="dataEntryUrl" />		 
 		<sf:form action="${dataEntryUrl}" method="POST" id="entryForm">
     		<input type="hidden" name="review" id="review" value="review"/>
     		<input type="hidden" name="groupId" id="groupId1"/>
     		<input type="hidden" name="subGroupId" id="subGroupId1"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1"/><!--subejct -->
     		<input type="hidden" name="stdSubGroupObservationCrfsId" id="stdSubGroupObservationCrfsId"/><!-- observationId -->
     	</sf:form> 
</body>
<script type="text/javascript">
	var justBefore = "";
	$.each(subject, function(k, v) {
		$("#subject_"+v).hide();
	});
	function showSubGroup(id){
		$.each(subject, function(k, v) {
			$("#subject_"+v).hide();
		});
		if(justBefore != id){
			$("#subject_"+id).show();
			justBefore = id;
		}else{
			justBefore = "";
		}
	}
	
	function crfDataReviewViewOnly(subId, stdSubGroupObservationCrfsId){
		$("#review").val("view");
		$("#groupId1").val($("#gid").val());
		$("#subGroupId1").val($("#sgid").val());
		$("#subGroupInfoId1").val(subId);
		$("#stdSubGroupObservationCrfsId").val(stdSubGroupObservationCrfsId);
		$("#entryForm").submit();
	}
	function crfDataReviewView(subId, stdSubGroupObservationCrfsId){
		$("#groupId1").val($("#gid").val());
		$("#subGroupId1").val($("#sgid").val());
		$("#subGroupInfoId1").val(subId);
		$("#stdSubGroupObservationCrfsId").val(stdSubGroupObservationCrfsId);
		$("#entryForm").submit();
	}
	
</script>
</html>