<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">E-Form Date Comparison</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            	<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
              		<a href='<c:url value="/admini/crfRule/crfDateComparisonPage"/>' class="btn btn-primary"><strong>Create E-Form Date Comparison</strong></a>
              	</c:if>
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
					<th>NAME</th>
					<th>DESC</th>
					<th>E-Form</th>
					<th>Section Element</th>
					<th>Group Element</th>
					<th>Message</th>
					<th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="rule">
					<tr>
						<td>${rule.name }</td>
						<td>${rule.descreption }</td>
						<td>${rule.sourcrf.name }</td>
						<td>${rule.secEle.name }</td>
						<td>${rule.groupEle.name }</td>
						<td>${rule.message }</td>
						<c:choose>
							<c:when test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
								<td>
									<c:if test="${rule.status eq 'active'}">
										<a href='<c:url value="/admini/crfRule/crfDateComparisonStatus/${rule.id}" />' class="btn btn-primary">ACTIVE</a>
									</c:if> 
									<c:if test="${rule.status eq 'In-active'}">
										<a href='<c:url value="/admini/crfRule/crfDateComparisonStatus/${rule.id}" />' class="btn btn-primary">NOT ACTIVE</a>
									</c:if>
								</td>
							</c:when>
							<c:otherwise>
								<td>
									<c:if test="${rule.status eq 'active'}">ACTIVE</c:if> 
									<c:if test="${rule.status eq 'In-active'}">NOT ACTIVE</c:if>
								</td>
							</c:otherwise>
						</c:choose>
						
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>NAME</th>
					<th>DESC</th>
					<th>E-Form</th>
					<th>Section Element</th>
					<th>Group Element</th>
					<th>Message</th>
					<th>Status</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          
