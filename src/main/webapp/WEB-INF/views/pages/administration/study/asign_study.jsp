<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Assign Study</h3>
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
            <c:url value="/administration/asignStudySave" var="asignStudySave" />
			<form:form action="${asignStudySave}" method="post" id="submitform">
			  <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>SNo</th><th>EMP ID</th><th>Full Name</th><th>Role</th><th> Study</th><th>Comments</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${allLoginUsers}" var="user" varStatus="st">
					<tr>
						<td>${st.count}</td>
						<td>${user.username}</td>
						<td>${user.fullName}</td>
						<%-- <td>${user.role.roleDesc}</td> --%>
						<c:choose>
							<c:when test="${user.username eq 'superadmin'}">
								<td>${user.role.role}</td>
							</c:when>
							<c:otherwise>
								<td>
									<select name="roleName_${user.id}" id="roleName_${user.id}" class="form-control">
										<c:forEach items="${rolesList}" var="role">
											<option value="${role.id}">${role.role}</option>
										</c:forEach>
									</select>
									<script type="text/javascript">
										if('${slrMap.get(user.id)}' != '0'  && '${slrMap.get(user.id)}' != ''){
											$("#roleName_${user.id}").val('${slrMap.get(user.id)}');	
										}
										
									</script>
								</td>
							</c:otherwise>
						</c:choose>
						<th style="text-align: center;">
							<c:choose>
								<c:when test="${activedUsers.contains(user.username)}">
									<label class = "checkbox-inline"><input type="checkbox" name="userInfo" value="${user.id}" checked="checked" />Assign</label>
								</c:when>
								<c:otherwise>
									<label class = "checkbox-inline"><input type="checkbox" name="userInfo" value="${user.id}" />Assign</label>
								</c:otherwise>
							</c:choose>
						</th>
						<th><input type="text" class="form-control input-sm" name="${user.id}_comment"></th>
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
                </tfoot>
              </table>
            </form:form>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          <c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
          	<div style="text-align:center"><input type="button" value="Save" id="formSubmitBtn" onclick="formSubmit()" class="btn btn-primary"></div>
          </c:if>
<script>
	function formSubmit(){
// 		var favoritestudy = [];
// 		var checkedRole = [];
// 		$.each($("input[name='userInfo']:checked"), function() {
// 			var userId = $(this).val();
// 			if(userId != 1){
// 				var roleVal = $('#roleName_'+userId).val();
// 				checkedRole.push(roleVal);
// 			}else{
// 				checkedRole.push(0);
// 			}
// 			favoritestudy.push($(this).val());
// 		});
// 		$('#assignRole').val(checkedRole);
// 		$("#cserIds").val(favoritestudy);
// 		favoritestudy = [];
// 		checkedRole = [];
// 		$.each($("input[name='userInfoRemove']:checked"), function() {
// 			favoritestudy.push($(this).val());
// 			var userId = $(this).val();
// 			if(userId != 1){
// 				var roleVal = $('#roleName_'+userId).val();
// 				checkedRole.push(roleVal);
// 			}else checkedRole.push(0);
// 		});
// 		$('#removeRole').val(checkedRole);
// 		$("#cserIdsRemove").val(favoritestudy);
		$("#submitform").submit();
	}
</script>