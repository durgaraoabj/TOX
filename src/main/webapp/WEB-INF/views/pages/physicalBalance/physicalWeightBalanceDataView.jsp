<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

		<div class="card">
            <div class="card-header"><h3 class="card-title">Physical Weight Balance Data</h3></div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Date & Time</th>
					<th>Batch No</th>
					<th>Nozzle No</th>
					<th>Gross Wt</th>
					<th>Tare Wt</th>
					<th>Net Wt</th>
					<th>Status</th>
					<th>Data Throw</th>
					<th>IP</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="l">
					<tr>
						<td>${l.dateAndTime }</td>
						<td>${l.batchNo }</td>
						<td>${l.nozzleNo }</td>
						<td>${l.grossWt }</td>
						<td>${l.tareWt }</td>
						<td>${l.netWt }</td>
						<td>${l.status }</td>
						<td>${l.dataFrom }</td>
						<td>${l.ipAddress }</td>
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
                <tr>
                	<th>Date & Time</th>
					<th>Batch No</th>
					<th>Nozzle No</th>
					<th>Gross Wt</th>
					<th>Tare Wt</th>
					<th>Net Wt</th>
					<th>Status</th>
					<th>Data Throw</th>
					<th>IP</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          
