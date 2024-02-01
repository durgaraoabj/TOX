<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
            <table  class="table table-bordered table-striped">
            	<tr><td>View Study Log for  : ${study.studyNo } </td></tr>
            </table>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Study Subject ID</th>
                	<th>Date of Birth</th>
                	<th>Person ID</th>
                	<th>Created By</th>
                	<th>Actions</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${list}" var="s">
			   			<tr>
			   				<td>${s.subjectId }</td>
			   				<td>${s.dateOfBirth }</td>
			   				<td>${s.personId }</td>
			   				<td>${s.createdBy }</td>
			   				<td>
			   					<a href='<c:url value="/globalStudyView/subAuditLogs/${s.id}" />' class="btn btn-primary">View</a>
			   				</td>
			   			</tr>
			   		</c:forEach>
                </tbody>
                <tfoot>
                <tr>
                	<th>Study Subject ID</th>
                	<th>Date of Birth</th>
                	<th>Person ID</th>
                	<th>Created By</th>
                	<th>Actions</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->