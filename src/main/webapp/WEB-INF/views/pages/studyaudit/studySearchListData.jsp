<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<body>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Study Search List</h3>
            </div>
            
		      <table id="example2" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Study No</th>
	   				<th>Study Name</th>
	   			    <th>Study Status</th>
	   				<th>Sponsor</th> 
	   				<th>Created By</th>
	   				<th>Created No</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${sml}" var="sml">
	   				<tr>
	   					<td>${sml.studyNo}</td>
	   					<td>${sml.studyDesc}</td>
	   					 <td>${sml.status.statusDesc}</td>
	   					<td>${sml.sponsorname}</td>
	   					<td>${sml.createdBy}</td>
	   					<td>${sml.createdOn}</td>
	   				</tr>
	   			</c:forEach> 
                </tbody>
               
              </table> 
         
            </div>
            <script>
            $(function () {
                $('#example2').DataTable({
                  "paging": true,
                  "lengthChange": false,
                  "searching": true,
                  "ordering": true,
                  "info": true,
                  "autoWidth": true,
                });
              });
            </script>
	</body>