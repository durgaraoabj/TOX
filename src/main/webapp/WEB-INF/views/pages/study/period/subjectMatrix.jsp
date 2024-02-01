<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Phase Management for Study : ${study.studyNo } [Subject Matrix]</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Subject Id</th>
	   				<c:forEach items="${plist}" var="period">
	   					<th>${period.name }</th>
	   				</c:forEach>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${vlist}" var="v">
			   			<tr>
			   				<td>${v.bedNo }</td>
			   				<c:forEach items="${v.periods}" var="volperiod">
			   					<td>
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
			   					</td>
			   				</c:forEach>

			   			</tr>
			   		</c:forEach>
                </tbody>
                <tfoot>
                <tr>
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