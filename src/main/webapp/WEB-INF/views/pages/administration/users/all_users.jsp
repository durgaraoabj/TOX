<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<div class="card">
            <div class="card-header">
              <h3 class="card-title">ALL Users</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>User Name</th><th>FULL NAME</th><th>Role</th><th>Role</th><th>Current Study</th><th>Current Site</th>
                	<th>Status</th><th>Change Status</th><th>Update</th>
                </tr>
                </thead>
                <tbody>
                 <c:forEach items="${allUsers}" var="user" varStatus="st">
					<tr>
						<td>${user.username}</td><td>${user.fullName}</td><td>${user.role.roleDesc}</td><td>${user.activeStudy.studyNo}</td>
						<td>${user.activeStudy.studyNo}</td>
						<td>${user.activeSite.siteName}</td>
						<td>${user.status}</td>
						<td>
							<c:choose>
								<c:when test="${user.status eq 'Active'}">
						   			<a href='<c:url value="/administration/employee/changeStatus/${user.id}"/>' class="btn btn btn-primary"><strong>In-Active</strong></a>	
						   		</c:when>
						   		<c:otherwise>
						   			<a href='<c:url value="/administration/employee/changeStatus/${user.id}"/>' class="btn btn btn-primary"><strong>Active</strong></a>
						   		</c:otherwise>
						   </c:choose>
		   				</td>
		   				<td>
		   					<a href='<c:url value="/administration/employee/updateEmployee/${user.username}"/>' class="btn btn btn-primary"><strong>Change</strong></a>
		   				</td>
					</tr>
					
				</c:forEach> 
                </tbody>
                <tfoot>
                <tr>
					<th>User Name</th><th>FULL NAME</th><th>Role</th><th>Role</th><th>Current Study</th><th>Current Site</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->