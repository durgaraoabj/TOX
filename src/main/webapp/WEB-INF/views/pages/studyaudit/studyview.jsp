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
					<th>Primary User :</th><td>${study.sdUserLable}</td>
					
				</tr>
				<tr>
				    <th>Secondary User :</th><td>${study.asdUserLable}</td>
					<th>Create By :</th><td>${study.createdBy}</td>
					<th>Create On :</th><td>${study.createdOn}</td>
				</tr>
			</table>
			<div class="card-header">
              <h3 class="card-title" >Study Meta Data</h3>
            </div>
            <table class="table">
				<tr>
					<th>No. Of Groups :</th><td>${study.noOfGroups}</td>
					<th>Subjects :</th><td>${study.subjects}</td>
					<th>Principal Investigator :</th><td>${study.principalInvestigator}</td>
					
					 
				</tr>
				<tr>
				    <th>Start Date :</th><td> <fmt:formatDate pattern = "yyyy-MM-dd" value = "${study.startDate}" /></td>
					<th>Create By :</th><td>${study.metaDataBy}</td>
					<th>Create On :</th><td> <fmt:formatDate pattern = "yyyy-MM-dd" value = "${study.metaDataOn}" /></td>
				</tr>
			</table>
		     <table class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Group Name</th>
	   				<th>No. Of Sub Group's</th>
	   				<th>Gender</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${group}" var="group">
	   				<tr>
	   					<td>${group.groupName}</td>
	   					<td>${group.groupTest}</td>
	   					<td>${group.gender}</td>
	   				</tr>
	   			</c:forEach> 
                </tbody>
               
              </table>
           <div class="card-header">
              <h3 class="card-title">Experimental Design</h3>
            </div>
            <table  class="table">
				<tr>
					<th>Created By :</th><td>${study.edDoneBy}</td>
					<th>Created On:</th><td><fmt:formatDate pattern = "yyyy-MM-dd hh:mm:ss" value = "${study.edDoneDate}"/></td>
			    </tr>
				
			</table>
			
             <div class="card-header">
              <h3 class="card-title">Observation Configuration</h3>
            </div>
              <table class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Group Name</th>
	   				<th>Sub Group</th>
	   				<th>Observation Name</th>
	   				<th>Created By </th>
	   				<th>Created On</th>
	   				
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${ssgocList}" var="ssgocList">
	   				<tr>
	   					<td>${ssgocList.subGroupInfo.group.groupName}</td>
	   					<td>${ssgocList.subGroupInfo.name}</td>
	   					<td>${ssgocList.observationName}</td>
	   					
	   					<c:choose>
	   					<c:when test = "${not empty ssgocList.updatedBy}">
	   					<td>${ssgocList.updatedBy}</td>
	   					<td><fmt:formatDate pattern = "yyyy-MM-dd hh:mm:ss" value = "${ssgocList.updatedOn}"/></td>
	   					</c:when>
	   					<c:otherwise>
	   					<td>${ssgocList.createdBy}</td>
	   					<td><fmt:formatDate pattern = "yyyy-MM-dd hh:mm:ss" value = "${ssgocList.createdOn}"/></td>
	   					</c:otherwise>
	   					</c:choose>
					    
	   				</tr>
	   			</c:forEach> 
                </tbody>
               
              </table>
            </div>
			