<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Design Review</title>
<script src='<c:url value ="/static/sweetalert/sweetalert2.js"/>'></script>
  <link rel="stylesheet" href='<c:url value="/static/sweetalert/sweetalert2.css"/>'>
</head>
<body>
<div class="card">
		<div class="card-header">
			<h3 class="card-title">Observation Design Review</h3><br/>
			<h5 class="card-title">Study : ${study.studyNo }</h5>
		</div>
	    <div class="card-body">
	    	<input type="hidden" name="studyId" id="studyId" value="${study.id}">
	    	<table  class="table table-bordered table-striped" style="width:100%">
	    	<thead style="background-color: #3C8DBC;">
	    		<tr>
	    			<th>Group</th>
	    			<th>Dose(mg/kg b.w)</th>
	    			<th>Conc. (mg/ml)</th>
	    			<th>No.Of Animals</th>
	    			<th>Sex</th>
	    			<td colspan="2">
	    				<table class="table table-bordered table-striped" style="width:100%">
	    					<tr><th colspan="2" style="text-align: center;background-color:#248E8F"> <font style="text-align: center" >Animal Number</font></th></tr>
							<tr>	    				
								<th style="width: 10%">From</th>
								<th style="width: 10%">To</th>
							</tr>
						</table>
					</td>
					<td colspan="3">
	    				<table class="table table-bordered table-striped" style="width:100%">
	    					<tr style="background-color:#248E8F"><th colspan="3" style="text-align: center"> <font style="text-align: center" >Observation Details</font></th></tr>
							<tr >	    				
								<th>Name</th>
								<th>Type</th>
								<th>Days/Week</th>
							</tr>
						</table>
					</td>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${group}" var="g">
					<tr>
		    			<th colspan="10">${g.groupName}</th>
		    		</tr>
		    		<c:forEach items="${g.subGroupInfo }" var="s">
		    			<tr>	
		    				<td>${s.name}</td>
		    				<td style="width: 10%;">${s.dose}</td>
		    				<td style="width: 10%;">${s.conc}</td>
		    				
		    				<c:choose>
		    					<c:when test="${s.animalInfo.size() eq 1}">
		    						<c:forEach items="${s.animalInfo }" var="animals">
    										<td style="width: 10%;">${animals.count}</td>
    										<td>${animals.gender}</td>
    										<td>${animals.formId}</td>
    										<td>${animals.toId}</td>
    								</c:forEach>
		    					</c:when>
		    					<c:otherwise>
		    						<td colspan="4">
		    							<table class="table table-bordered table-striped">
		    								<c:forEach items="${s.animalInfo }" var="animals">
		    									<tr>
		    										<td style="width: 24%;">${animals.count}</td>
		    										<td style="width: 17%;">${animals.gender}</td>
		    										<td>${animals.formId}</td>
		    										<td>${animals.toId}</td>
		    									</tr>
		    								</c:forEach>
		    							</table>
		    						</td>
		    					</c:otherwise>
		    				</c:choose>
		    				<td>
		    					<table class="table table-bordered table-striped">
		    						<c:forEach items="${s.ssgocList}" var="observ">
		    							<tr>	    				
											<td>${observ.observationName}</td>
											<td>${observ.dayType}</td>
											<td>${observ.days}</td>
										</tr>
		    						</c:forEach>
			    				</table>
		    				</td>
		    			</tr>
		    		</c:forEach>
				</c:forEach>
				</tbody>
			</table>
			<c:if test="${not empty group}">
				<div style="margin-left: 45%;">
					<input type="button" value="Accept" onclick="reviewFunction('Accept')" class="btn btn-primary btn-md"> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
					<input type="button" value="Reject" onclick="reviewFunction('Reject')" class="btn btn-primary btn-md">
				</div>
			</c:if>
		</div>
		<c:url value="/studyReview/obserVationReviewProcess" var="processObservation"></c:url>
		<form:form action="${processObservation}" method="POST" id="observForm">
			<input type="hidden" name="studyId" id="studyId" value="${sm.id}">
			<input type="hidden" name="type" id="type">
			<input type="hidden" name="comments" id="comments">
		</form:form>
    </div>
    <script type="text/javascript">
    	function reviewFunction(value){
    		if(value == "Accept"){
    			$('#type').val(value);
    			$('#comments').val("0");
    			$('#observForm').submit();
    		}else{
    			new swal({
		      		  html:'',
		      		  input: 'textarea',
//		       		  customClass: 'swal-wide2',
		      		  inputLabel: 'Comments',
		      		  inputPlaceholder: 'Enter Commenets...',
		      		  showCancelButton: true
		      	 }).then(function (result) {
		      		if(result.value != null && result.value != "" && result.value !="undefined"){
		      			$('#comments').val(result.value);
		      			$('#type').val(value);
		    			$('#comments').val();
		    			$('#observForm').submit();
		      		}
		      	});
    		}
    	}
    </script>
</body>
</html>