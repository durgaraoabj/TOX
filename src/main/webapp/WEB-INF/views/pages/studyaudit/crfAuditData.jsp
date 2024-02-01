<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Study View</h3>
            </div>
            <table  class="table">
				<tr>
					<th >Study No :</th><td>${study.studyNo}</td>
					<th>Study Title :</th><td>${study.studyDesc}</td>
					
					<th>Create By :</th><td>${study.createdBy}</td>
					<th>Create On :</th><td>${study.createdOn}</td>
				</tr>
			</table> 
			  <div class="card-header">
              <h3 class="card-title">CRF Audit Data</h3>
            </div>
		     <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Group Name</th>
	   				<th>Sub Group</th>
	   				<th>Animal No</th>
	   				<th>Observation Name</th>
	   				<th>Created By</th>
	   				<th>Created On</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sgaindcListData}" var="salist">
	   				<tr>
	   				    <td>${salist.subGroup.group.groupName}</td>
	   				    <td>${salist.subGroup.name}</td>
	   				    <td>${salist.subGroupAnimalsInfoAll.animalNo}</td>
	   					<td>${salist.crf.observationName}</td>
	   					<td>${salist.createdBy}</td>
	   					<td><fmt:formatDate pattern = "yyyy-MM-dd hh:mm:ss" value = "${salist.createdOn}" /></td>
	   				</tr>
	   			</c:forEach> 
                </tbody>
               
              </table>
          
            </div>
			