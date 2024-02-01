<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
            	<table class="table table-bordered table-striped">
            		<tr><td>CRF INFORMATIN</td><td>Study : ${sm.studyNo }</td><td>Phase : ${period.name }</td><td>Subject : ${vol.volId }</td><td>Bed No : ${vol.bedNo }</td></tr>
            	</table>
            	<c:if test="${ss ne null}">
            		<font color="blue">
            			<table class="table table-bordered table-striped">
            			<tr><td>Subject Status : Withdrawn</td> 
            			<td>Continue To Next Phase : ${ss.phaseContinue}</td>
            			</tr>
            			</table>
            		</font>
            	</c:if>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            <c:if test="${withDraw}">
           		<c:url value="/studyexe/subjectStatus" var="subjectStatus" />		
				<sf:form action="${subjectStatus}" method="POST" modelAttribute="crfpojo"
			   		id="subjectStatus" enctype="multipart/form-data">
	            	<input type="hidden" name="volId" value="${vol.id }"/>
	            	<input type="hidden" name="periodId" value="${period.id }"/>
	            	<table class="table table-bordered table-striped">
		                <tr><th>Subject Status</th><td><input type="checkbox" value="withDraw" name="subjectStatus">withdraw</td></tr>
						<tr><td/><td><input type="checkbox" value="Continue" name="phaseSatus">Continue To Next Phase</td></tr>
						<tr><td/><td><input type="button" value="Save" class="btn btn-primary" onclick="subStatus()"></td></tr>
	            	</table>
            	</sf:form>
           	</c:if>
            <c:if test="${withDraw}">
            	
            </c:if>
            
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>CRF NAME</th>
			   		<th>CRF Status</th>
			   		<th>Pdf</th>
                </tr>
                </thead> 
                <tbody>
                	<c:forEach items="${vpcl}" var="vc">
			   			<tr>
			   				<td>${vc.stdCrf.crfName }</td>
			   					<td>
<!-- 			   					//'' or NOT STARTED, IN PROGRESS, IN REVIEW , COMPLETED -->
			   						<c:if test="${vc.crfStatus eq '' or vc.crfStatus eq 'NOT STARTED'  }">
				   						<a href='<c:url value="/studyexe/subjectPeriodCrfsDataEntryView/${vc.id}" />' 
				   						class="btn btn-primary">NOT STARTED</a>
				   						<%-- <a href='<c:url value="/pdfview/crfPdfView/${vc.id}"/>' target="_blank">
				   							<img alt="img" src='<c:url value="/static/images/pdfIcon.png" />'>
				   						</a> --%>
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'IN PROGRESS'  }">
				   						<a href='<c:url value="/studyexe/subjectPeriodCrfsDataEntryView/${vc.id}" />'
				   						class="btn btn-primary">IN PROGRESS</a>
				   						<%-- <a href='<c:url value="/pdfview/crfPdfView/${vc.id}"/>' target="_blank">
				   							<img alt="img" src='<c:url value="/static/images/pdfIcon.png" />'>
				   						</a> --%>
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'IN REVIEW'  }">
				   						<a href='<c:url value="/studyexe/subjectPeriodCrfsDataEntryView/${vc.id}" />' 
				   						class="btn btn-primary">IN REVIEW</a>
				   						<%-- <a href='<c:url value="/pdfview/crfPdfView/${vc.id}"/>' target="_blank">
				   							<img alt="img" src='<c:url value="/static/images/pdfIcon.png" />'>
				   						</a> --%>
				   					</c:if>
				   					<c:if test="${vc.crfStatus eq 'COMPLETED'  }">
				   						<a href='<c:url value="/studyexe/subjectPeriodCrfsDataEntryView/${vc.id}" />' 
				   						class="btn btn-primary">COMPLETED</a>
				   						<%-- <a href='<c:url value="/pdfview/crfPdfView/${vc.id}"/>' target="_blank">
				   							<img alt="img" src='<c:url value="/static/images/pdfIcon.png" />'>
				   						</a> --%>
				   					</c:if>
			   					</td>
			   					<td> <a href='<c:url value="/pdfview/crfPdfView/${vc.id}"/>' target="_blank">
				   							<img alt="img" src='<c:url value="/static/images/pdfIcon.png" />'>
				   						</a>
				   				</td>
			   			</tr>
			   		</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>CRF NAME</th>
			   		<th>CRF Status</th>
			   		<th>Pdf</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
<script>
	function subStatus(){
		var retVal = confirm	("Do You Want to Change Subject Status?");
		if(retVal) $("#subjectStatus").submit();
	}
</script>
