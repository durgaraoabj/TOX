<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Period List</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>
					<c:if test="${study.crfPeriodConfiguation eq null || study.crfPeriodConfiguation eq false }">
						<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
							<div align="center"><input type="button" id="selectCrfscomplete" 
							value="Mark As Complete" class="btn btn-primary" style="width:100px;"></div>
						</c:if>
					</c:if>
					<c:if test="${study.crfPeriodConfiguation eq true }">
						Configuration Completed
					</c:if>
					</th>
					<th>Period Name</th>
					<th>Start Date</th>
					<th>End Date</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${periods}" var="period">
						<tr>
							
							<td>
								<c:if test="${study.crfPeriodConfiguation eq null || study.crfPeriodConfiguation eq false }">
									<c:choose>
										<c:when test="${period.crfConfiguation eq null ||  period.crfConfiguation eq ''}">
											<div align="center"><input type="button" onclick="selectCrf('${period.id}')"
											value="Configure" class="btn btn-primary" style="width:100px;"></div>
										</c:when>
										<c:otherwise>
											<div align="center">Completed</div>
										</c:otherwise>
									</c:choose>
								</c:if>
								
								<c:if test="${study.crfPeriodConfiguation eq true }">
									Completed
								</c:if>
								
							</td>
							<td>${period.name}</td>
							<td>${period.startDate}</td>
							<td>${period.endDate}</td>
						</tr>
					</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>
					<c:if test="${study.crfPeriodConfiguation eq null || study.crfPeriodConfiguation eq false }">
						<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
							<div align="center"><input type="button" id="selectCrfscomplete2" 
							value="Mark As Complete" class="btn btn-primary" style="width:100px;"></div>
						</c:if>
					</c:if>
					<c:if test="${study.crfPeriodConfiguation eq true }">
						Configuration Completed
					</c:if>
					</th>
					<th>Period Name</th>
					<th>Start Date</th>
					<th>End Date</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          


    	
    <c:url value="/buildStdyCrf/configureCrfsPeriod" var="configureCrfsPeriod"/>
    <form:form action="${configureCrfsPeriod}" method="post" id="configureCrfsPer" >
			<input type="hidden" name="periodId" id="periodId"/>
    </form:form>
    
    <c:url value="/buildStdyCrf/configureAllPeriodsCrfsMarkComplete" var="configureCrfsMarkComplet"/>
    <form:form action="${configureCrfsMarkComplet}" method="post" id="configureCrfsMarkComplet" >
			
    </form:form>
<script type="text/javascript">
	function selectCrf(pid){
		$('#periodId').val(pid);
		$('#configureCrfsPer').submit();
	}
	
	$('#selectCrfscomplete').click(function(){
//         var favorite = [];
//         $.each($("input[name='selected']:checked"), function(){            
//             favorite.push($(this).val());
//         });
		var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
		if(flag){
        	$('#configureCrfsMarkComplet').submit();
		}
	});
	$('#selectCrfscomplete2').click(function(){
//         var favorite = [];
//         $.each($("input[name='selected']:checked"), function(){            
//             favorite.push($(this).val());
//         });
		var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
		if(flag){
        	$('#configureCrfsMarkComplet').submit();
		}
	});
</script>