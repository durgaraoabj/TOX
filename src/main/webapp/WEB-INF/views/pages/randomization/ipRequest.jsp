<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="card">
	<div class="card-header">
       <h3 class="card-title">
       		<a href='<c:url value="/randomization/newIpRequest"/>' class="btn btn-primary">New IP Request</a>
       </h3>
     </div>
     <div class="card-body">
     	<table id="example1" class="table table-bordered table-striped">
   			<thead>
	   			<tr>
	   				<th>Subject ID</th>
	   				<th>Visit Name</th>
	   				<th>Status</th>
	   			</tr>
	   		</thead>
	   		<tbody>
	   			<c:forEach items="${ipRequests}" var="ip">
	   				<tr>
		   				<td>${ip.subject}</td>
		   				<td>${ip.period.name}</td>
		   				<td>${ip.status}</td>
	   				</tr>
	   			</c:forEach>
	   		</tbody>
	   		<tfoot>
                <tr>
					<th>Subject ID</th>
	   				<th>Visit Name</th>
	   				<th>Status</th>
                </tr>
            </tfoot>
	   	</table> 
     </div>
</div>