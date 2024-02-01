<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Assign Sites</h3>
            </div>
            <table class="table table-bordered table-striped">
				<tr>
					<th>Study No</th><td>${study.studyNo}</td>
					<th>Study Title</th><td>${study.studyDesc}</td>
					<th>Status</th><td>${study.globalStatus.statusDesc}</td>
				</tr>
			</table>
            <!-- /.card-header -->
            <div class="card-body">
            <c:url value="/administration/asignSiteSave" var="asignSiteSave" />
			<form:form action="${asignSiteSave}" method="post" id="submitform">
				<input type="hidden" name="cserIds" id="cserIds" />
				<input type="hidden" name="cserIdsRemove" id="cserIdsRemove" />
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>SlNo</th><th>EMP ID</th><th>Full Name</th><th>Role</th><th>Assign Sites</th><th>Comments</th>
                </tr>
                </thead>
                <tbody>
                 <c:forEach items="${list}" var="user" varStatus="st">
					<tr>
						<td>${st.count}</td><td>${user.username}</td><td>${user.fullName}</td><td>${user.role.roleDesc}</td>
						<th style="text-align: center;">
							<table>
								<tr><td>Assign</td><td>Remove</td></tr>
							<c:forEach items="${sites}" var="site">
								<tr>
									<c:choose>
										<c:when test="${site.usersIds.contains(user.id)}">
											<td><input type="checkbox" name="userInfo" value="${user.id}_${site.id}" checked="checked" />${site.siteName }</td>
										</c:when>
										<c:otherwise>
											<td><input type="checkbox" name="userInfo" value="${user.id}_${site.id}" />${site.siteName }</td>
										</c:otherwise>
									</c:choose>
									<td><input type="checkbox" name="userInfoRemove" value="${user.id}_${site.id}" />${site.siteName }</td>
								</tr>
							</c:forEach>
							</table>
						</th> 
						<th><input type="text" class="form-control input-sm" name="${user.id}_comment"></th>
					</tr>
				</c:forEach> 
                </tbody>
                <tfoot>
                <tr>
					<th>SlNo</th><th>EMP ID</th><th>Full Name</th><th>Role</th><th>Assign Sites</th><th>Comments</th>
                </tr>
                </tfoot>
              </table>
            </form:form>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          <c:if test="${sites.size() > 0}">
	          <c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'  and userRole ne 'USER'}">
	<%--           <c:if test="${userRole eq 'Data Manager' and userRole eq 'Data Specialist' and userRole eq 'USER' and userRole ne 'Monitor'}"> --%>
	          	<div style="text-align:center"><input type="button" value="Save" id="formSubmitBtn" onclick="formSubmit()" class="btn btn-primary"></div>
	          </c:if>
          </c:if>
<script>
	function formSubmit(){
		var favoritestudy = [];
		$.each($("input[name='userInfo']:checked"), function() {
			favoritestudy.push($(this).val());
		});
		$("#cserIds").val(favoritestudy);
		favoritestudy = []
		$.each($("input[name='userInfoRemove']:checked"), function() {
			favoritestudy.push($(this).val());
		});
		$("#cserIdsRemove").val(favoritestudy);
		$("#submitform").submit();
	}
</script>