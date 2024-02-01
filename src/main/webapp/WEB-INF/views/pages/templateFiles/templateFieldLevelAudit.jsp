<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Template Field Level Audit</title>
<script src='<c:url value="/static/admintltcss/plugins/jquery/jquery.min.js"/>'></script>
<!-- DataTables -->
<script src='<c:url value="/static/admintltcss/plugins/datatables/jquery.dataTables.js"/>'></script>
<script src='<c:url value="/static/admintltcss/plugins/datatables-bs4/js/dataTables.bootstrap4.js"/>'></script>
<script type="text/javascript">
$(document).ready(function () {
	 $('#example5').DataTable();
	 $('.dataTables_length').addClass('bs-select');
	 });
$(document).ready(function () {
	 $('#example6').DataTable();
	 $('.dataTables_length').addClass('bs-select');
	 });
</script>
<style>
/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 160px; /* Location of the box */
    padding-left: 210px;
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
    position: relative;
    background-color: #fefefe;
    margin: auto;
    padding: 0;
    border: 1px solid #888;
    width: 94%;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
    -webkit-animation-name: animatetop;
    -webkit-animation-duration: 0.4s;
    animation-name: animatetop;
    animation-duration: 0.4s
}

/* Add Animation */
@-webkit-keyframes animatetop {
    from {top:-300px; opacity:0} 
    to {top:0; opacity:1}
}

@keyframes animatetop {
    from {top:-300px; opacity:0}
    to {top:0; opacity:1}
}

/* The Close Button */
.close {
    color: white;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

.modal-header {
    padding: 2px 16px;
    background-color: #5cb85c;
    color: white;
    text-align:left;
}

.modal-body {padding: 2px 16px;}

.modal-footer {
    padding: 2px 16px;
    background-color: #5cb85c;
    color: white;
}

</style>

</head>
<body>
	<!-- The Modal -->
<div id="myModal" class="modal">

  <!-- Modal content -->
  <div class="modal-content">
  <div class="modal-header">
     <h3>${tatDto.fileDetails.obstData.fileName}&nbsp;&nbsp;Audit Details</h3>
       <span class="close">&times;</span>
    </div>
   <%--  <span class="close">&times;</span> --%>
   <div class="modal-body">
   <br>
	<div class="card"
		style="background-color: rgba(51, 110, 123, 1); border-radius: 20px; border: 3px solid #f3fff0;">
		<h5 class="card-header info-color white-text text-center py-2">
			<strong style="color: white;">Audit Details</strong>
		</h5>
	</div>
	<div class="card">
    	<table class="table table-bordered table-striped" id="example5">
			<thead style="background-color: RGBA(128, 0, 0, 0.3);">
	    		<tr>
	    			<th>Sno</th>
					<th>Sheet Name</th>
					<th>Row</th>
					<th>Column</th>
	    			<th>Old Value</th>
	    			<th>New Value</th>
	    			<th>Modified Date</th>
	    			<th>Modified By</th>
	    		</tr>
    		</thead>
    		<tbody>
    		<c:forEach items="${tatDto.tempAuditList}" varStatus="st" var="dal">
    			<tr align="center">
    				<td>${st.count}</td>
					<td>${dal.sheetName}</td>
					<td>${dal.row}</td>
					<td>${dal.col}</td>
    				<td>${dal.old_value}</td>
    				<td>${dal.new_value}</td>
    				<td>${dal.modified_date}</td>
    				<td>${dal.modifiedBy}</td>
    			</tr>
	    	</c:forEach>
	    	</tbody>
	 	</table>
	</div> 	
<div class="card"
		style="background-color: rgba(51, 110, 123, 1); border-radius: 20px; border: 3px solid #f3fff0;">
		<h5 class="card-header info-color white-text text-center py-2">
			<strong style="color: white;">Audit Log Details</strong>
		</h5>
	</div>
<div class="card">
    	<table class="table table-bordered table-striped" id="example6">
   			<thead style="background-color: RGBA(128, 0, 0, 0.3);">
	    		<tr>
	    			<th>Sno</th>
					<th>Sheet Name</th>
					<th>Row</th>
					<th>Column</th>
	    			<th>Old Value</th>
	    			<th>New Value</th>
	    			<th>Modified Date</th>
	    			<th>Modified By</th>
	    		</tr>
    		</thead>	
    	    <tbody>
	    	<c:forEach items="${tatDto.tempAuditListLog}" var="dall" varStatus="st">
    			<tr align="center">
    				<td>${st.count}</td>
					<td>${dall.sheetName}</td>
					<td>${dall.row}</td>
					<td>${dall.col}</td>
    				<td>${dall.old_value}</td>
    				<td>${dall.new_value}</td>
    				<td>${dall.modified_date}</td>
    				<td>${dall.modifiedBy}</td>
    			</tr>
	    	</c:forEach>
	    	</tbody>
    	</table>
    	</div>
  	</div>
  </div>

</div>

<script>
// Get the modal
var modal = document.getElementById('myModal');

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
/* window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
} */

// open the modal 
modal.style.display = "block";
</script>
		
</body>
</html>