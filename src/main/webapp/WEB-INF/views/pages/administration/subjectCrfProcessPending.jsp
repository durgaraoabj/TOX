<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Pending Crf List</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
<%--               <a href='<c:url value="/administration/createStudy"/>' class="btn btn btn-primary"><strong>Create Study</strong></a> --%>
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Site Name</th>
	   				<th>protocalId</th>
	   				<th>Subject Id</th>
	   				<th>Phase Name</th>
	   				<th>Crf</th>
	   				<th>Crf Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${crfInfo}" var="vpc">
	   				<tr>
	   					<td>${vpc.vol.site.siteName}</td>
	   					<td>${vpc.vol.site.protocalId}</td>
	   					<td>${vpc.vol.volId}</td>
	   					<td>${vpc.period.name}</td>
	   					<td>${vpc.stdCrf.crfName}</td>
	   					<td>${vpc.crfStatus }</td>
	   				</tr>
	   			</c:forEach> 
                </tbody>
                <tfoot>
                <tr>
					<th>Site Name</th>
	   				<th>protocalId</th>
	   				<th>Subject Id</th>
	   				<th>Phase Name</th>
	   				<th>Crf</th>
	   				<th>Crf Status</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          