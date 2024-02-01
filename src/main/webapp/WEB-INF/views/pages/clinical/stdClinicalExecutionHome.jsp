<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
      <div class="container-fluid">
        <div class="row">
          <!-- /.col-md-6 -->
          <div class="col-lg-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h5 class="m-0">CPU Execution</h5>
              </div>
              <div class="card-body">
<!--                 <h6 class="card-title">Before going to execute study we have to build study.</h6> -->
				<a href='<c:url value="/study/clinical/stdClinicaldosing"/>' class="btn btn-primary">Dosing</a>&nbsp;&nbsp;
				<a href='<c:url value="/study/clinical/stdClinicalSampleCollection"/>' class="btn btn-primary">Sample Collection</a><br/><br/>
				<a href='<c:url value="/study/clinical/stdClinicalSampleSaperation"/>' class="btn btn-primary">Sample Separation</a>&nbsp;&nbsp;
				<a href='<c:url value="/study/clinical/stdClinicalVitalCollection"/>' class="btn btn-primary">Vital Collection</a><br/><br/>
              </div>
            </div>
          </div>
          <!-- /.col-md-6 -->
          <c:if test="${userName ne 'CPU Activity' }">
          <div class="col-lg-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h5 class="m-0">Clinical Report</h5>
              </div>
              <div class="card-body">
                <a href='<c:url value="/study/clinical/stdClinicaldosingReport"/>' class="btn btn-primary">Doseing</a>&nbsp;&nbsp;
                <a href='<c:url value="/study/clinical/stdClinicalSampleCollectionReport"/>' class="btn btn-primary">Sample Schedule</a><br/><br/>
                <a href='<c:url value="/study/clinical/stdClinicalSampleSaperationReport"/>' class="btn btn-primary">Sample Separation</a>&nbsp;&nbsp;
                <a href='<c:url value="/study/clinical/stdClinicalVitalCollectionReport"/>' class="btn btn-primary">Vital Schedule</a><br/><br/>
              </div>
            </div>
          </div>
          </c:if>
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->