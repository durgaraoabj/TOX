<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<div class="box-header with-border">
		<h3 class="box-title">Crf List</h3>
		<div class="box-tools pull-right">
			<button type="button" class="btn btn-box-tool" data-widget="collapse"
				data-toggle="tooltip" title="Collapse">
				<i class="fa fa-minus"></i>
			</button>
		</div>
	</div>
	<div class="box-body">
		<table id="crftable" class="table table-bordered table-striped">
			<tr>
				<th><c:if test="${configureStatus eq false}">
						<div align="center">
							<input type="button" id="selectCrfscomplete"
								value="Mark As Complete" class="btn btn-warning btn-sm"
								style="width: 200px;">
						</div>
					</c:if></th>
				<th>Crf Name</th>
				<th>Crf Description</th>
				<th>type</th>
			</tr>
			<c:forEach items="${crfs}" var="crf">
				<tr>

					<td><c:choose>
							<c:when test="${crf.configure eq  true}">
								<input type="checkbox" name="selected" id="selected"
									value="${crf.id}" checked="checked" />
							</c:when>
							<c:otherwise>
								<input type="checkbox" name="selected" id="selected"
									value="${crf.id}" />
							</c:otherwise>
						</c:choose></td>
					<td>${crf.crfName}</td>
					<td>${crf.crfDesc}</td>
					<td>${crf.type}</td>
				</tr>
			</c:forEach>
		</table>


		<c:url value="/study/saveconfigureCrfs" var="saveconfigureCrfs" />
		<form:form action="${saveconfigureCrfs}" method="post" id="submitform">
			<input type="hidden" name="crfIds" id="crfIds" />

			<c:if test="${configureStatus eq false}">
				<div align="center">
					<input type="button" id="selectCrfs" value="Select Crfs"
						class="btn btn-warning btn-sm" style="width: 200px;">
				</div>
			</c:if>

		</form:form>

		<c:url value="/study/configureCrfsMarkComplete"
			var="configureCrfsMarkComplet" />
		<form:form action="${configureCrfsMarkComplet}" method="post"
			id="configureCrfsMarkComplet">

		</form:form>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function() {
		$('#crftable').DataTable();
		/* {
		'paging'      : true,
		'lengthChange': false,
		'searching'   : false,
		'ordering'    : true,
		'info'        : true,
		'autoWidth'   : false
		} */
	});
	$('#selectCrfs').click(function() {
		var favorite = [];
		$.each($("input[name='selected']:checked"), function() {
			favorite.push($(this).val());
		});
		alertify.confirm("Are you sure to Configure Crf?", function(e) {
			if (e) {
				alertify.success('you are click ok.');
				$('#crfIds').val(favorite.join(", "));
				$('#submitform').submit();
			} else {
				alertify.error('you are clicked cancel.');
			}
		});
	});

	$('#selectCrfscomplete').click(function() {
		var favorite = [];
		$.each($("input[name='selected']:checked"), function() {
			favorite.push($(this).val());
		});
		alertify.confirm("Are you sure to Mark as Complete?", function(e) {
			if (e) {
				alertify.success('you are click ok.');
				$('#configureCrfsMarkComplet').submit();
			} else {
				alertify.error('you are clicked cancel.');
			}
		});
	});
</script>
</html>