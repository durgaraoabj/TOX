<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
   <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
   <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"  %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Accession Data Entry Forms</title>
</head>
<body>
<div class="card">
	<div class="card-header">
         <h3 class="card-title">Accession Animal Data Entry Review Forms</h3>
    </div>
    <div>
    <div class="card-body">
    	<table id="example1" class="table table-bordered table-striped" style="width:100%">
    	   <thead>
	    		<tr>
	    		<th>Type</th>
	    		<th>Sub Type</th>
	    		<th>Prefix</th>
	    		<th>Observation</th>
	    		<th colspan="3">Operations</th>
	    		</tr>
    		</thead>
    		<tbody>
    			<c:forEach items="${sadList}" var="sad" varStatus="st">
    			<c:if test="${sad.crf.type ne 'CP'}">
    				  <tr>
    				<td>${sad.crf.type}</td>
    				<td>${sad.crf.subType}</td>
    				<td>${sad.crf.prefix}</td>
    				<td>${sad.crf.name}</td>
    				<td><a href="<c:url value="/studyReview/reviewAccessionForm/${sad.crf.id}/${studyId}"/>"><input type="button" value="Review" class="btn btn-primary btn-sm"></a></td>
    			 </tr>
    			</c:if>
    			
    			</c:forEach>
    		</tbody>
    		
    	</table>
    	</div>
    </div>
</div>
</body>
</html>