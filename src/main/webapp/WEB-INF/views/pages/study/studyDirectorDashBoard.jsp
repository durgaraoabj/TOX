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
						<th>Accept Comments</th>

						<th>Update</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${studys}" var="sm" varStatus="st">
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

							<td><input type="text" id="comment_${sm.id}"
								class="form-control" name="name${sm.id}"> <font
								color="red" id="commdataMsg_${sm.id}"></font></td>
							<td><input type="button" id="saveBtn"
								class="btn btn-primary" onclick="saveDataFunction('${sm.id}')"
								value="Accept" /></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<br />

			<table class="table table-bordered table-striped" id="stdListTab2">
				<tr>
					<th>Sno</th>
					<th>Study Number</th>
					<th>Title</th>
					<th>Created By</th>
					<th>Created On</th>
					<th>Test Item Code</th>
					<th>Sponsor Code</th>
					<th>GLP Or Non-GLP</th>
					<th>Accept Comments</th>
					<th>Study Status</th>
					<th>Action</th>
				</tr>
				<c:forEach items="${directorStudys}" var="sm" varStatus="st">
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
						<td>${sm.acceptComments}</td>
						<td>${sm.studyStatus}</td>
						<c:choose>
							<c:when test="${sm.status.statusCode eq 'IN'}">
								<td><a href='javascript: desingStudy("${sm.id}", "entry")'><i class="fa fa-link"
										aria-hidden="true" style="color: blue;"></i>Create</a></td>
							</c:when>
								
							<c:otherwise>
							<td><a href='javascript: desingStudy("${sm.id}", "update")'><i class="fa fa-link"
										aria-hidden="true" style="color: blue;"></i>View</a></td>
								<%-- 							<td><a href='<c:url value="/administration/metaDataEntry/${sm.id}/${sm.id}/update"/>'><i class="fas fa-edit"></i>Update</a></td> --%>
<!-- 								<td><a -->
<%-- 									href='<c:url value="/administration/metaDataEntry/${sm.id}/${sm.id}/view"/>'><i --%>
<!-- 										class="fas fa-edit"></i>View</a></td> -->
							</c:otherwise>
						</c:choose>


					</tr>
				</c:forEach>
			</table>
			<c:url value="/administration/aceeptMetdeta" var="aceeptMetdeta" />
			<form:form action="${aceeptMetdeta}" method="post" id="acceptmetasub">
				<input type="hidden" name="comments" id="commentsId" />
				<input type="hidden" name="studyid" id="studyIdva" />
			</form:form>
		</div>

		<c:url value="/administration/studyDesign" var="studyDesign" />
		<form:form action="${studyDesign}" method="post" id="studyDesignPage">
			<input type="hidden" name="studyId" id="studyDesignStudyId" />
			<input type="hidden" name="actionType" id="studyDesignType" value="entry" />
		</form:form>
	</div>
	
</body>
<script type="text/javascript">
		function desingStudy(studyId, type){
// 			alert(studyId)
			debugger;
			$("#studyDesignStudyId").val(studyId);
			$("#studyDesignType").val(type);
			$("#studyDesignPage").submit();
		}
		function saveDataFunction(id) {
			debugger;
			var comm = $('#comment_' + id).val();
			if (comm != "") {
				$('#commentsId').val(comm);
				$('#studyIdva').val(id);
				$('#acceptmetasub').submit();
			} else {
				$('#commdataMsg_' + id).html("Required Field");
			}

		}

		$(function() {
			$("#stdListTab").DataTable();
			$("#stdListTab2").DataTable();
		});
	</script>
</html>