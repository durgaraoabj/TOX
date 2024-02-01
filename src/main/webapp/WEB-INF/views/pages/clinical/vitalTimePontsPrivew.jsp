<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Period Wise info</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table class="table table-bordered table-striped">
					<c:forEach items="${periodWise}" var="sp">
						<tr><th>Period</th><th colspan="9">${sp.key.order }</th></tr>
							<c:forEach items="${sp.value}" var="sub">
								<tr><td>Subject</td><td colspan="9">${sub.key }</td></tr>
								<tr>
									<th>Type</th><th>Time Point</th>
									<th>Time Deviation</th><th>Position</th>
									<th>Time Point Date</th><th>Planned Time</th><th>Planned Date</th>
									<th>Pulse Rate</th><th>Oral Temp</th><th>Blood Pressure</th><th>Respiratory Rate</th><th>Wellbeing ascertained</th>
								</tr>
								<c:forEach items="${sub.value }" var="tp">
									<tr>
										<td>${tp.type}</td><td>${tp.timePoint}</td>
										<td>${tp.interval}</td><td>${tp.position}</td>
										<td>${tp.timePointDate}</td><td>${tp.planedTime}</td><td>${tp.planedDate}</td>
										<td>${tp.pulseRate}</td><td>${tp.oralTemp}</td><td>${tp.bp}</td><td>${tp.respiratoryRate}</td><td>${tp.wellbeingAscertained}</td>
									</tr>
								</c:forEach>
							</c:forEach>
					</c:forEach>	
					<tr>
						<td><input type="button" value="Save" class="btn btn-primary" onclick="submitForm()"/></td>
						<td colspan="4"><a href='<c:url value="/study/clinical/sampleSchedule"/>' class="btn btn-primary">Exit</a></td>
					</tr>
				</table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          
     
<c:url value="/study/clinical/vitalTimePontsSave" var="vitalTimePontsSave" />
	<sf:form action="${vitalTimePontsSave}" method="POST"  id="formsumit" ></sf:form>

	<script>
	function submitForm(){
		$("#formsumit").submit();	
	}
	</script>