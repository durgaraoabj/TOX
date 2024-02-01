<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

	<table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Batch NO</th>
                	<th>Subject</th>
	   				<th>Proposed Dose Time</th>
	   				<th>Actual Dose Time</th>
	   				<th>Deviation</th>
	   				<th>Message</th>
	   				<th>Date</th>
	   				<th>Done By</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${doseDate}" var="d">
	   				<tr>
	   					<td>${d.batchNo}</td>
	   					<td>${d.subjectNo}</td>
	   					<td>${d.planedTime}</td>
	   					
	   					<td>${d.actualTime}</td>
	   					<td>${d.deviationOfCollection }</td>
	   					<td>${d.message }</td>
	   					<td>${d.timePointDate}</td>
	   					<td>${d.doneBy}</td>
	   				</tr>
 	   			</c:forEach>  
                </tbody>
                <tfoot>
                <tr>
					<th>Batch NO</th>
                	<th>Subject</th>
	   				<th>Proposed Dose Time</th>
	   				<th>Actual Dose Time</th>
	   				<th>Deviation</th>
	   				<th>Message</th>
	   				<th>Date</th>
	   				<th>Done By</th>
                </tr>
                </tfoot>
              </table>
              