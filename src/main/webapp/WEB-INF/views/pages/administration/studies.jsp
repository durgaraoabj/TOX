<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<div class="card">
	<div class="card-header">
		<h3 class="card-title" style="font-weight: bold; font-size: large;">Studies
			List</h3>
	</div>
	<!-- /.card-header -->
	<div class="card-body">
		<div style="padding-bottom: 20px">
<%-- 			<c:if test="${userRole eq 'TFM' or userRole eq 'SUPERADMIN'}"> --%>
<%-- 				<a href='<c:url value="/administration/createStudy"/>' --%>
<!-- 					class="btn btn btn-primary"><strong>Create Study</strong></a> -->
				
<!-- 				<a class="btn btn-gre" -->
<%-- 					href="<c:url value="/grouping/createStudyAndGrouping"/>"> <!-- class="btn btn btn-new"> --> --%>
<!-- 					<i class="fa fa-edit" aria-hidden="true" style="margin-right: 5px"></i>Create -->
<!-- 					Study<span class="float-right text-muted text-sm"></span> -->
<!-- 				</a> -->
<%-- 			</c:if> --%>
		</div>

		<table id="example1" class="table table-bordered table-striped"
			style="text-align: center">
			<thead>
				<tr>
					<th>Study No</th>
					<th>Title</th>
					<th>Study Director</th>
					<th>Test Item Code</th>
					<th>Sponsor Code</th>
					<th>GLP Or Non-GLP</th>
					<th>Study Date</th>
					<th>Status</th>
					<th>Change Status</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${allStudies}" var="study" varStatus="st">
					<tr>
						<td>${study.studyNo}</td>
						<td>${study.studyDesc}</td>
						<td>${sdMap[study.sdUser]}</td>
						<td>${study.testItemCode}</td>
						<td>${study.sponsorMasterId.code}</td>
						<td>${study.glpNonGlp}</td>
						
						
<%-- 						<td>${sdMap[study.asdUser]}</td> --%>
						<td><fmt:formatDate type="both" pattern="yyyy-MM-dd"
								value="${study.startDate}" /></td>
						<td>${study.status.statusDesc}</td>
<%-- 						<td><c:choose> --%>
<%-- 								<c:when test="${study.intrumentConfuguraiton}"> --%>
<%-- 									<a href="javascript:void(0)" onclick="viewStudy('${study.id}')"><strong>View</strong></a> --%>
<%-- 								</c:when> --%>
<%-- 								<c:otherwise> --%>
<!-- 									<a -->
<%-- 										href='<c:url value="/grouping/addIntrumentAndPerameters/${study.id}"/>' --%>
<!-- 										class="btn btn-gre"><strong>Configure</strong></a> -->
<%-- 								</c:otherwise> --%>
<%-- 							</c:choose></td> --%>
						<td><c:choose>
								<%-- 	   							<c:when test="${study.studyStatus eq 'Locked' }"> --%>
								<c:when test="${study.status.statusDesc eq 'Locked' }"> 
									Locked   							
	   							</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${userRole ne 'Monitor'}">
											<a
												href='<c:url value="/administration/changeStudyStatus/${study.id}"/>'
												class="btn btn-gre"><strong>Change</strong></a>
										</c:when>
										<c:otherwise>Change</c:otherwise>
									</c:choose>

								</c:otherwise>
							</c:choose></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<!-- /.card-body -->

	<div class="modal fade" id="studyModal" tabindex="-1" role="dialog"
		aria-labelledby="discDataModal" aria-hidden="true"
		data-backdrop="static" data-keyboard="false">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title" id="myModalLabel">Study View</h4>
				</div>
				<div class="modal-body">
					<div id="studyData"></div>
				</div>
				<div class="modal-footer">
					<div style="text-align: right;">
						<input type="button" class="btn btn-primary"
							onclick="acceptCheckBox('studyModal')" value="Close" />
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- /.card -->

<script src='/TOX/static/js/admin/createStudy.js'>
	
</script>

