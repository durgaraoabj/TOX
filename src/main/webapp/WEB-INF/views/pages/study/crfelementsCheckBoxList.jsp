<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<table class="table table-bordered table-striped">
		<tr><th>Crf Name</th><td>${crf.name }</td></tr>
		<tr><th>Description</th><td>${crf.title }</td></tr>
		<tr><th>version</th><td>${crf.version }</td></tr>
	</table>
	<table class="table table-bordered table-striped">
		<tr>
			<td></td>
			<th>Name</th>
			<th>Label</th>
			<th>Section</th>
			<th>Group</th>
			<th>Max Repeats</th>
			<th>Data Type</th>
			<th>Required?</th>
			<th>Default Value</th>
			<th>Pattren</th>
		</tr>
		
		<c:forEach items="${secEleList}" var="sec">
			<tr>
				<td>
					<label class="checklabel">
					  <c:if test="${sec.seclectionStatus eq true}"><input type="checkbox" name="secEleName" value="${sec.id}" checked="checked"></c:if>
					  <c:if test="${sec.seclectionStatus eq false}"><input type="checkbox" name="secEleName" value="${sec.id}"></c:if>
					  <span class="checkmark"></span>
					</label>
				</td>
				<td>${sec.name }</td>
				<td>${sec.leftDesc }</td>
				<td>${sec.section.name }</td>
				<td>Ungrouped</td>
				<td>1</td>
				<td>${sec.type}</td>
				<td>${sec.required }</td>
				<td></td>
				<td>${sec.pattren }</td>
			</tr>
		</c:forEach>
		<c:forEach items="${groupEleList}" var="grp">
			<tr>
				<td>
					<label class="checklabel">
					  <c:if test="${grp.seclectionStatus eq true}"><input type="checkbox" name="groupEleName" value="${grp.id}" checked="checked"></c:if>
					  <c:if test="${grp.seclectionStatus eq false}"><input type="checkbox" name="groupEleName" value="${grp.id}"></c:if>					  
					  <span class="checkmark"></span>
					</label>
				</td>
				<td>${grp.name }</td>
				<td>${grp.title }</td>
				<td>${grp.section.name }</td>
				<td>${grp.group.name }</td>
				<td>${grp.group.maxRows }</td>
				<td>${grp.type}</td>
				<td>${grp.required }</td>
				<td></td>
				<td>${grp.pattren }</td>
			</tr>
		</c:forEach>
	</table>