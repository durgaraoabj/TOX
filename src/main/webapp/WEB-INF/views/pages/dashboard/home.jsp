<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
      <div class="container-fluid">
        <div class="row">
          <!-- /.col-md-6 -->
          <div class="col-lg-6">
            <div class="card card-outlineCl">
              <div class="card-header">
<!--                 <h5 class="m-0">CRF Configuration</h5> -->
              </div>
              <div class="card-body">
<%--                <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}"> --%>
<%-- 			      	<a href='<c:url value="/dashboardReport/"/>' class="btn btn-primary">Dash Board</a>&nbsp;&nbsp; --%>
<%-- 			   </c:if> --%>
<%--                <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}"> --%>
<%-- 		      	  <a href='<c:url value="/globalStudyView/"/>' class="btn btn-primary">Global View</a>&nbsp;&nbsp; --%>
<%-- 		      	</c:if> --%>
<%--                <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}"> --%>
<%-- 			      	<a href='<c:url value="/reviewer/"/>' class="btn btn-primary">Review</a>&nbsp;&nbsp; --%>
<%-- 			   </c:if> --%>
<%-- 			   <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'USER' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}"> --%>
<%-- 					<a href='<c:url value="/discrepancy/"/>' class="btn btn-primary">Discrepancy</a><br/><br/> --%>
<%-- 				</c:if> --%>
			   <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'QA' || userRole eq 'QC' || userRole eq 'USER' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}">
					<a href='<c:url value="/study/"/>' class="btn btn-new">Study</a>&nbsp;&nbsp;
				</c:if>
<%-- 				 <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'USER' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}"> --%>
<%-- 					<a href='<c:url value="/globalStudyView/studyAuditLog"/>' class="btn btn-primary">Audit Log</a>&nbsp;&nbsp; --%>
<%-- 				</c:if> --%>
			   <c:if test="${userRole eq 'SUPERADMIN' || userRole eq 'QA' || userRole eq 'ADMIN' || userRole eq 'Study Director' || userRole eq 'Data Manager' || userRole eq  'Data Specialist' || userRole eq  'Monitor'}">
					<a href='<c:url value="/administration/"/>' class="btn btn-new">Administration</a><br/>
		      </c:if>
<!--                 <h6 class="card-title">Before going to execute study we have to build study.</h6> -->
	
<!--                 <p class="card-text">With supporting text below as a natural lead-in to additional content.</p> -->

              </div>
            </div>
          </div>
          <!-- /.col-md-6 -->
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->
      
      