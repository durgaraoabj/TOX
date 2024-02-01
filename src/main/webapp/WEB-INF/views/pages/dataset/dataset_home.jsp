<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="box-title">${sm.studyNo } : View Dataset</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            	<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
	              <a href='<c:url value="/extractData/datasetCreationPage2"/>' class="btn btn-primary"><strong>Create Dataset</strong></a>
	            </c:if>
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>NAME</th>
					<th>Description</th>
					<th>Created By</th>
					<th>Created On</th>
					<th>Excel</th>
					<th>Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${datasets}" var="dataset">
					<tr>
					<td>${dataset.name }</td>
					<td>${dataset.description }</td>
					<td>${dataset.createdBy }</td>
					<td>${dataset.createdOn }</td>
					<td>
						<c:if test="${dataset.status eq 'active'}">
							<a href='<c:url value="/extractData/excelExport/${dataset.id}" />' class="btn btn-primary">Export</a>
						</c:if> 
						<c:if test="${dataset.status eq 'In-Active'}">
							Export
						</c:if>
					</td> 
					<td>
						<c:if test="${dataset.status eq 'active'}">
							<a href='<c:url value="/extractData/changeStatus/${dataset.id}" />' class="btn btn-primary">ACTIVE</a>
						</c:if> 
						<c:if test="${dataset.status eq 'In-Active'}">
							<a href='<c:url value="/extractData/changeStatus/${dataset.id}" />' class="btn btn-primary">NOT ACTIVE</a>
						</c:if>
					</td>
					
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>NAME</th>
					<th>Description</th>
					<th>Created By</th>
					<th>Created On</th>
					<th>Excel</th>
					<th>Action</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->