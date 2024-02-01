<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
      <div class="container-fluid">
        <div class="row">
          <!-- /.col-md-6 -->
          <div class="col-lg-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h5 class="m-0">Time Points & Planned Times</h5>
              </div>
              <div class="card-body">
                <table class="table table-bordered table-striped">
					<tr><th>Type</th><th>Time Point</th><th>Planned Time</th><th>Planned Date</th></tr>
					<c:forEach items="${timePoints}" var="t">
						<tr>
							<td>${t.type}</td>
							<td>${t.timePoint}</td>
							<td>${t.planedTime}</td>
							<td>${t.timePointDate}</td>
						</tr>
					</c:forEach>
				</table>
              </div>
            </div>
          </div>
          <div class="col-lg-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h5 class="m-0">Info</h5>
              </div>
              <div class="card-body">
              	<table class="table table-bordered table-striped">
					<tr><td>Study</td><td>${study.studyNo}</td></tr>
					<tr><td>Subjects</td><td>${study.subjects}</td></tr>
					<tr><td>Dose Time</td><td>${clinical.doseTime }</td>
					<tr><td>Interval Between Subject</td><td>${clinical.intervalBetweenSubject }</td>
				</table>	
				<table class="table table-bordered table-striped">
					<tr><th>BATCH</th><th>Subjects</th></tr>
					<c:forEach items="${batchInfo}" var="b">
						<tr><td>${b.key}</td><td>${b.value}</td></tr>
					</c:forEach>
				</table>	
              </div>
            </div>
            

          </div>
          <!-- /.col-md-6 -->
        </div>
        <!-- /.row -->
        
		          <div class="card-header">
	                <h5 class="m-0">Period Wise info</h5>
	              </div>
	              <div class="card-body">
					<table class="table table-bordered table-striped">
						<c:forEach items="${periodWise}" var="sp">
							<tr><th>Period</th><th colspan="4">${sp.key.order }</th></tr>
							<c:forEach items="${sp.value}" var="batch">
								<tr><th>Batch</th><th colspan="4">${batch.key }</th></tr>
								<c:forEach items="${batch.value}" var="sub">
									<tr><td>Subject</td><td colspan="4">${sub.key }</td></tr>
									<tr><th>Type</th><th>Time Point</th><th>Time Point Date</th><th>Planned Time</th><th>Planned Date</th></tr>
									<c:forEach items="${sub.value }" var="tp">
										<tr><td>${tp.type}</td><td>${tp.timePoint}</td><td>${tp.timePointDate}</td><td>${tp.planedTime}</td><td>${tp.planedDate}</td></tr>
									</c:forEach>
								</c:forEach>
							</c:forEach>
						</c:forEach>	
						<tr>
							<td><input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/></td>
							<td colspan="4"><a href='<c:url value="/study/clinical/sampleSchedule"/>' class="btn btn-primary">Exit</a></td>
						</tr>
					</table>	
	              </div>
      </div><!-- /.container-fluid -->
	<c:url value="/study/clinical/sampleTimePontsSave" var="sampleTimePontsSave" />
	<sf:form action="${sampleTimePontsSave}" method="POST"  id="formsumit" ></sf:form>

	<script>
	function submitForm(){
		$("#formsumit").submit();	
	}
	</script>