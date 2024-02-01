<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
</script>
<body>
	<input type="hidden" name="gid" id="gid" value="${groupid}"/>
	<input type="hidden" name="sgid" id="sgid" value="${subgroupnid}"/>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">CFR Observation List</h3>
		</div>
		 <div class="box-body">
			<table class="table table-bordered table-striped">
				<tr>
				    <th>Study Name :</th>
					<th>${study.studyNo}</th>
					<th>Group Name :</th>
					<th>${groupname}</th>
					<th>Sub Group Name :</th>
					<th>${subgroup }</th>
					
				</tr>
				</table>
			<table class="table table-bordered table-striped">
			<tr><th colspan="3">Observation Name</th></tr>
					<c:forEach items="${sgaindcListData}" var="obs">
						<tr>
							<td>
								${obs.crf.observationName }
								
							</td>
							<td>
							<input type="button" id="crfDataEntry" value="VIEW" onclick="crfDataReviewView('${subjectgroupnid}', '${obs.crf.id}')"
										class="btn btn-primary" style="width: 150px;"/>
							</td>
						</tr>
					</c:forEach>
				</table>
		</div> 
	 </div>
	   
 		<c:url value="/crfAudit/allAnimalCrfDataReviewViewAudit" var="dataEntryUrl" />		 
 		<sf:form action="${dataEntryUrl}" method="POST" id="entryForm">
     		<input type="hidden" name="review" id="review" value="review"/>
     		<input type="hidden" name="groupId" id="groupId1"/>
     		<input type="hidden" name="subGroupId" id="subGroupId1"/>
     		<input type="hidden" name="subGroupInfoId" id="subGroupInfoId1"/><!--subejct -->
     		<input type="hidden" name="stdSubGroupObservationCrfsId" id="stdSubGroupObservationCrfsId"/><!-- observationId -->
     	</sf:form> 
</body>
<script type="text/javascript">
	
	
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