<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
          <div class="card-header">
          <table  class="table table-bordered table-striped">
          	<tr><td>${sss.subjectId} Audit Logs </td></tr>
          </table>
          </div>
          <div class="card-body">
            <table class="table table-bordered table-striped">
              <thead>
              <tr>
              	<th>Study Subject ID</th>
              	<th>Date of Birth</th>
              	<th>Person ID</th>
              	<th>Created By</th>
              </tr>
              </thead>
              <tbody>
              <tr>
              	<td>${sss.subjectId}</td>
              	<td>${sss.dateOfBirth}</td>
              	<td>${sss.personId}</td>
              	<td>${sss.createdBy}</td>
              </tr>
              </tbody>
              <tfoot>
              </tfoot>
            </table>
            <br/>
            <table class="table table-bordered table-striped">
              <thead>
              <tr>
              	<th>Audit Event</th>
              	<th>Date/Time of Server</th>
              	<th>User</th>
              	<th>Value Type</th>
              	<th>Old</th>
              	<th>New</th>
              </tr>
              </thead>
              <tbody>
              <tr>
              	<td>Study Subject Created </td>
              	<td>${sss.enrollmentDate}</td>
              	<td>${sss.createdBy}</td>
              	<td>${sss.studyGroup.groupName }</td>
              	<td></td>
              	<td>${sss.groupClass.groupName }</td>
              </tr>
              </tbody>
              <tfoot>
              </tfoot>
            </table>
            <br/>
            <table class="table table-bordered table-striped">
              <thead>
              <tr>
              	<th>Phase</th>
<!--               	<th>Location</th> -->
              	<th>Date</th>
<!--               	<th>Occurrence Number</th> -->
              </tr>
              </thead>
              <tbody>
               	<c:forEach items="${vpcsList}" var="vp">
	              <tr>
	              	<td>${vp.period.name}</td>
<!-- 	              	<td></td> -->
	              	<td><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${vp.createdOn}"/></td>
<!-- 	              	<td>1</td> -->
	              </tr>	
              	</c:forEach>
              	
              </tbody>
              <tfoot>
              </tfoot>
            </table>
            <c:if test="${sslist.size() > 0  }">
            	<br/>
	            <table class="table table-bordered table-striped">
	              <thead>
	              <tr><th colspan="12">Withdraw Audit</th></tr>
	              <tr>
	              	<th>Site</th>
	              	<th>Phase</th>
	              	<th>Withdraw</th>
	              	<th>Phase Continue</th>
	              	<th>Done By</th>
	              	<th>Date</th>
	              </tr>
	              </thead>
	              <tbody>
	               	<c:forEach items="${sslist}" var="ss">
		              <tr>
		              	<td>${ss.site.siteName}</td>
		              	<td>${ss.period.name}</td>
		              	<td>${ss.withDraw}</td>
		              	<td>${ss.phaseContinue}</td>
		              	<td>${ss.createdBy}</td>
		              	<td><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${ss.createdOn}"/></td>
		              </tr>	
	              	</c:forEach>
	              	
	              </tbody>
	              <tfoot>
	              </tfoot>
	            </table>
            </c:if>
            
            
            <br/>
            <table class="table table-bordered table-striped">
              <thead>
              <tr><th colspan="12">Discrepancy Audit</th></tr>
              <tr>
              	<th>Phase Name</th>
              	<th>Crf name</th>
              	<th>Section Element</th>
              	<th>Group Element</th>
              	<th>Group Row</th>
              	<th>Current Value</th>
              	<th>Status</th>
              	<th>Assigned To</th>
              	<th>Raised By</th>
              	<th>Old Value</th>
              	<th>Old Status</th>
              	<th>Created On</th>
              </tr>
              </thead>
              <tbody>
               	<c:forEach items="${descrpencyList}" var="vp">
	              <tr>
	              	<td>${vp.volPeriodCrf.period.name}</td>
	              	<td>${vp.crf.name}</td>
	              	<td>${vp.secElement.name}</td>
	              	<td>${vp.groupElement.name}</td>
	              	<td>${vp.rowNo}</td>
	              	<td>
	              		<c:choose>
	              			<c:when test="${vp.secEleData != null}">${vp.secEleData.value}</c:when>
	              			<c:when test="${vp.groupEleData != null}">${vp.groupEleData.value}</c:when>
	              		</c:choose>
	              	</td>
	              	<td>${vp.status }</td>
	              	<td>${vp.assingnedTo }</td>
	              	<td>${vp.risedBy }</td>
	              	<td>${vp.oldValue }</td>
	              	<td>${vp.oldStatus }</td>
	              	<td><fmt:formatDate type="both" pattern="yyyy-MM-dd" value="${vp.createdOn}"/></td>
	              </tr>	
              	</c:forEach>
              	
              </tbody>
              <tfoot>
              </tfoot>
            </table>
          </div>
        </div>
        