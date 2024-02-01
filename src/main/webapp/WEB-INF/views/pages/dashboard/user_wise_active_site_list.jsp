<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Change Site</h3>&nbsp;
              <a href='<c:url value="/randomization/exitSite"/>' class="btn btn-primary">Exit Current Sites</a>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            <c:choose>
    		<c:when test="${sites.size() > 0 }">
		   		<table id="example1" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Site NO</th>
			   				<th>Site Name</th>
			   				<th>Protocol ID</th>
			   				<th>Start Date</th>
			   				<th>End Date</th>
			   				<th>Expected total enrollment</th>
			   				<th>Facility Name</th>
			   				<th>Status</th>
			   			</tr>
			   		</thead>
			   		<tbody>
 		   			<c:forEach items="${sites}" var="site">
			   			<tr>
			   				<td>${site.siteNo}</td>
			   				<td>${site.siteName}</td>
			   				<td>${site.protocalId}</td>
			   				<td>${site.startDate}</td>
			   				<td>${site.endDate}</td>
			   				<td>${site.subjects}</td>
			   				<td>${site.facilityName}</td>
			   				<td>
			   					<c:if test="${site.id == currentSite}">
				   						<a href="#" class="btn btn-primary">Current Site</a>
			   					</c:if>
			   					<c:if test="${site.id != currentSite}">
			   						<a href='<c:url value="/dashboard/changeSite/${site.id}" />' class="btn btn-primary">Change</a>
			   					</c:if>
			   				</td>
			   			</tr>
			   		</c:forEach> 
			   		<tbody>
			   		<tfoot>
	                <tr>
	                	<th>Site NO</th>
						<th>Site Name</th>
		   				<th>Protocol ID</th>
		   				<th>Start Date</th>
		   				<th>End Date</th>
		   				<th>Expected total enrollment</th>
		   				<th>Facility Name</th>
		   				<th>Status</th>
	                </tr>
	                </tfoot>
		   		</table>
		   	</c:when>
		   	
    	</c:choose>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->







