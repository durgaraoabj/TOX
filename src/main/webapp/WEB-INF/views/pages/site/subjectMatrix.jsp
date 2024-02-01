<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <table  class="table table-bordered table-striped">
	            	<tr><td>Phase Management for Study : ${study.studyNo } </td><td>Study Status : ${study.studyStatus }</td><td>Site : ${siteName }</td></tr>
	            </table>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
            <c:if test="${study.studyStatus eq 'Available'}">
              <c:if test="${createSubject == 'Yes' }">
              		<c:choose>
	              	<c:when test="${study.crfPeriodConfiguation eq true}">
	              		 <a href='<c:url value="/site/addSubject"/>' class="btn btn btn-primary"><strong>Add New Subject</strong></a>
	              	</c:when>
	              	<c:otherwise>
	              		<font color="red">Crf Configuration not Completed for Period</font>
	              	</c:otherwise>
	              </c:choose>
	             
              </c:if>
            </c:if>
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
<!--                 	<th>Subject</th> -->
                	<th>Subject Id</th>
	   				<c:forEach items="${plist}" var="period">
	   					<th>${period.name }</th>
	   				</c:forEach>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${vlist}" var="v">
			   			<tr>
<%-- 			   				<td>${v.bedNo }</td> --%>
			   				<td>${v.volId }</td>
			   				<c:forEach items="${v.periods}" var="volperiod">
			   					<td>
			   					<c:choose>
			   						<c:when test="${study.studyStatus eq 'Available'}">
			   							<c:choose>
				   							<c:when test="${volperiod.vpcs != null }">
				   								<c:if test="${volperiod.vpcs.status eq 'NOT STARTED'  }">
							   						<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${v.id}/${volperiod.id}" />' 
							   						class="btn btn-primary">NOT STARTED</a>
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'IN PROGRESS'  }">
							   						<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${v.id}/${volperiod.id}" />'
							   						class="btn btn-primary">IN PROGRESS</a>
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'IN REVIEW'  }">
							   						<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${v.id}/${volperiod.id}" />' 
							   						class="btn btn-primary">IN REVIEW</a>
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'COMPLETED'  }">
							   						<a href='<c:url value="/studyexe/viewSubjectPeriodCrfs/${v.id}/${volperiod.id}" />' 
							   						class="btn btn-primary">COMPLETED</a>
							   					</c:if>
				   							</c:when>
				   							<c:when test="${volperiod.phaseContinue eq 'Yes' }">
				   								<c:choose>
				   									<c:when test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
				   										<a href='<c:url value="/site/scheduleSubject/${v.id}/${volperiod.id}" />' 
							   							class="btn btn-primary">Schedule</a>
				   									</c:when>
				   									<c:otherwise>
				   										Schedule
				   									</c:otherwise>
				   								</c:choose>
				   								
				   							</c:when>
				   							<c:otherwise>
				   								Withdrawn In Previous Phase
				   							</c:otherwise>
				   						</c:choose>
			   						</c:when>
			   						<c:otherwise>
			   							<c:choose>
				   							<c:when test="${volperiod.vpcs != null }">
				   								<c:if test="${volperiod.vpcs.status eq 'NOT STARTED'  }">
							   						NOT STARTED
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'IN PROGRESS'  }">
							   						IN PROGRESS
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'IN REVIEW'  }">
							   						IN REVIEW
							   					</c:if>
							   					<c:if test="${volperiod.vpcs.status eq 'COMPLETED'  }">
							   						COMPLETED
							   					</c:if>
				   							</c:when>
				   							<c:when test="${volperiod.phaseContinue eq 'Yes' }">
				   								Schedule
				   							</c:when>
				   							<c:otherwise>
				   								Withdrawn In Previous Phase
				   							</c:otherwise>
				   						</c:choose>
			   						</c:otherwise>
			   					</c:choose>
			   						
			   						
			   						
			   					</td>
			   				</c:forEach>

			   			</tr>
			   		</c:forEach>
                </tbody>
                <tfoot>
                <tr>
<!--                 	<th>Subject</th> -->
					<th>Subject Id</th>
	   				<c:forEach items="${plist}" var="period">
	   					<th>${period.name }</th>
	   				</c:forEach>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->