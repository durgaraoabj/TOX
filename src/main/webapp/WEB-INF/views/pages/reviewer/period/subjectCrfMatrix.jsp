<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
            	<table class="table table-bordered table-striped">
            		<tr><td>CRF INFORMATIN</td><td>Study : ${sm.studyNo }</td><td>Phase : ${period.name }</td><td>Subject : ${vol.volId }</td><td>Bed No : ${vol.bedNo }</td></tr>
            	</table>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>CRF NAME</th>
			   		<th>CRF Status</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${vpcl}" var="vc">
			   			<tr>
			   				<td>${vc.stdCrf.crfName }</td>
			   					<td>
<!-- 			   					//'' or NOT STARTED, IN PROGRESS, IN REVIEW , COMPLETED -->
			   						<c:if test="${vc.crfStatus eq '' or vc.crfStatus eq 'NOT STARTED'  }">
			   							NOT STARTED
<%-- 				   						<a href='<c:url value="/reviewer/subjectPeriodCrfsDataEntryView/${vc.id}" />'  --%>
<!-- 				   						class="btn btn-warning btn-sm">NOT STARTED</a> -->
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'IN PROGRESS'  }">
				   						IN PROGRESS
<%-- 				   						<a href='<c:url value="/reviewer/subjectPeriodCrfsDataEntryView/${vc.id}" />' --%>
<!-- 				   						class="btn btn-warning btn-sm">IN PROGRESS</a> -->
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'IN REVIEW'  }">
				   						<a href='<c:url value="/reviewer/subjectPeriodCrfsDataEntryView/${vc.id}" />' 
				   						class="btn btn-primary">IN REVIEW</a>
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'COMPLETED'  }">
				   						<a href='<c:url value="/reviewer/subjectPeriodCrfsDataEntryView/${vc.id}" />' 
				   						class="btn btn-primary">COMPLETED</a>
				   					</c:if>
			   					</td>
			   			</tr>
			   		</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>CRF NAME</th>
			   		<th>CRF Status</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->