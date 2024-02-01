<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Period</th>
                	<th>Batch NO</th>
                	<th>Subject</th>
                	<th>Type</th>
	   				<th>Time Point</th>
	   				<th>Planned Time</th>
	   				<th>Schedule Time</th>
	   				<th>Schedule Date</th>
	   				<th>Actual Time</th>
	   				<th>Actual Done</th>
	   				<th>Done By</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sampleSeparationData}" var="d">
	   				<tr>
	   					<td>${d.periodNo}</td>
	   					<td>${d.batchNo}</td>
	   					<td>${d.subjectNo}</td>
	   					<td>${d.type}</td>
	   					<td>${d.timePoint}</td>
	   					<td>${d.planedTime}</td>
	   					<td>${d.scheduleTime}</td>
	   					<td>${d.scheduleDate}</td>
	   					<td>${d.vialactualTime}</td>
	   					<td>${d.vialactualDate}</td>
	   					<td>${d.vialDoneBy}</td>
	   				</tr>
 	   			</c:forEach>  
                </tbody>
                <tfoot>
                <tr>
					<th>Period</th>
                	<th>Batch NO</th>
                	<th>Subject</th>
                	<th>Type</th>
	   				<th>Time Point</th>
	   				<th>Planned Time</th>
	   				<th>Schedule Time</th>
	   				<th>Schedule Date</th>
	   				<th>Actual Time</th>
	   				<th>Actual Done</th>
	   				<th>Done By</th>
                </tr>
                </tfoot>
              </table>
              