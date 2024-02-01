<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>User Studies List</title>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Studies List</h3>
		</div>
		<div class="box-body">
			<table class="table table-bordered table-striped" id="stdListTab">
				<thead>
					<tr>
						<th>Sno</th>
						<th>Study Number</th>
						<th>Title</th>
						<th>Created By</th>
						<th>Created On</th>
						<th>Test Item Code</th>
						<th>Sponsor Code</th>
						<th>GLP Or Non-GLP</th>
						<th>Review Requested on</th>
						<th>Reveiw</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${reviewStudys}" var="sm" varStatus="st">
						<tr>
							<td>${st.count}</td>
							<td>${sm.studyNo}</td>
							<td>${sm.studyDesc}</td>
							<td>${sm.createdBy}</td>
							<td><fmt:formatDate value="${sm.createdOn}"
									pattern="yyyy-MM-dd" /></td>
							<td>${sm.testItemCode}</td>
							<td>${sm.sponsorMasterId.code}</td>
							<td>${sm.glpNonGlp}</td>
							<td><fmt:formatDate value="${sm.sentToReviewOn}"
									pattern="yyyy-MM-dd" /></td>

							<td>
<!-- 							<input type="button" id="saveBtn" -->
<%-- 								class="btn btn-primary" onclick="viewStudy('${sm.id}')" --%>
<!-- 								value="View" /> -->
							<a href='javascript: desingStudy("${sm.id}", "update")'><i class="fa fa-link"
										aria-hidden="true" style="color: blue;"></i>View</a></td>	
							
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
	<div id="studyView"></div>
	<c:url value="/administration/studyDesign" var="studyDesign" />
		<form:form action="${studyDesign}" method="post" id="studyDesignPage">
			<input type="hidden" name="studyId" id="studyDesignStudyId" />
			<input type="hidden" name="actionType" id="studyDesignType" value="entry" />
		</form:form>
</body>
<script type="text/javascript">
function desingStudy(studyId, type){
//		alert(studyId)
	debugger;
	$("#studyDesignStudyId").val(studyId);
	$("#studyDesignType").val(type);
	$("#studyDesignPage").submit();
}

// 	function viewStudy(studyId) {
// 		$("#studyView").html("");
// 		var result = asynchronousAjaxCall(mainUrl
// 				+ "/studyReview/studyReviewView/" + studyId);
// 		if (result != 'undefined' && result != '') {
// 			$("#studyView").html(result);
// 		}
// 	}

	$(function() {
		$("#stdListTab").DataTable();
		$("#stdListTab2").DataTable();
	});
</script>
</html>