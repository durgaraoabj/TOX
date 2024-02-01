<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<script type="text/javascript">
	var group = [];
</script>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">
				<b>Study Execution</b>
			</h3>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped">
				<c:choose>
					<c:when test="${study.status.statusCode eq 'SD' }">
						<font color="red">Study in "Design" State. </font>
					</c:when>
					<c:otherwise>
						<tr>
							<th>Group Name</th>
						</tr>
						<c:forEach items="${group }" var="g">
							<tr>
								<td>
								<c:if test="${g.noOfGroupsNeedToReview gt 0 }">
								<div onclick="showSubGroup('${g.id }')" style="color: blue">${g.groupName }&nbsp;<c:if test="${g.noOfGroupsNeedToReview gt 0 }"><span class="right badge badge-danger">Available</span></c:if></div>
								</c:if>
									
									
									<div id="group_${g.id }">
										<c:if test="${g.subGroupInfo.size() gt 0}">
											<table class="table table-bordered table-striped">
												<tr>
													<th>SubGroup</th>
												</tr>
												<c:forEach items="${g.subGroupInfo}" var="sg">
													<tr>
														<td>
														<c:if test="${sg.noOfSubGroupsNeedToReview gt 0 }">
													     	<!-- Swami Code -->
														    <%--  <div onclick="gotoSubjectSelection('${sg.id }')"
																style="color: blue">${sg.name }&nbsp;<c:if test="${sg.noOfSubGroupsNeedToReview gt 0 }"><span class="right badge badge-danger">Available</span></c:if>
															</div>  --%>
														   <div onclick="gotoObservations('${sg.id }')"
																style="color: blue">${sg.name }&nbsp;<c:if test="${sg.noOfSubGroupsNeedToReview gt 0 }"><span class="right badge badge-danger">Available</span></c:if>
															</div> 
														</c:if>
														</td>
													</tr>
												</c:forEach>
											</table>
										</c:if>
									</div>

								</td>
							</tr>
							<script>
								group.push('${g.id }');
							</script>
						</c:forEach>
					</c:otherwise>
				</c:choose>

			</table>
		</div>
	</div>
	<!-- 		<h6 class="box-title"> -->
	<%-- 			Observation Name : ${crfviewTest.observationName }&nbsp;&nbsp;&nbsp;&nbsp; --%>
	<%-- 	    	Observation Desc : ${crfviewTest.observationDesc }&nbsp;&nbsp;&nbsp;&nbsp; --%>
	<!--     	</h6> -->

	<c:url value="/studyExecution/subgroupSubjectsReview" var="url" />
	<sf:form action="${url}" method="POST" id="form">
		<input type="hidden" name="subGroupId" id="subGroupId" />
		<!--              <div style="text-align:center">  -->
		<%--             	<a href='<c:url value="/admini/crf/" />' class="btn btn-primary">EXIT</a>  --%>
		<!--              </div>  -->
	</sf:form>
	<c:url value="/crfAudit/subgroupSubjectsAllReviewsAudit" var="url" />
	<sf:form action="${url}" method="POST" id="formall">
		<input type="hidden" name="subGroupId1" id="subGroupId1" />
		<!--              <div style="text-align:center">  -->
		<%--             	<a href='<c:url value="/admini/crf/" />' class="btn btn-primary">EXIT</a>  --%>
		<!--              </div>  -->
	</sf:form>
</body>
<script type="text/javascript">
	var justBefore = "";
	$.each(group, function(k, v) {
		$("#group_" + v).hide();
	});
	// 	$(document).ready(function(){
	// 		$.each(group, function(k, v) {
	// 			$("#group_"+v).hide();
	// 		});
	// 	});
	function showSubGroup(id) {
		$.each(group, function(k, v) {
			$("#group_" + v).hide();
		});
		if (justBefore != id) {
			$("#group_" + id).show();
			justBefore = id;
		} else {
			justBefore = "";
		}

	}
	function gotoSubjectSelection(id) {
		$("#subGroupId").val(id);
		$("#form").submit();
	}
	function gotoObservations(id) {
		//alert("id"+id);
		$("#subGroupId1").val(id);
		$("#formall").submit();
	}
	
</script>
</html>