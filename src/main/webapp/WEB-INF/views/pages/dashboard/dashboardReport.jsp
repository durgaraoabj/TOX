<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-6">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Subject Enrollment By Site</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body">
                <table class="table table-bordered">
                  <thead>                  
                    <tr>
                      <th style="width: 10px">Site</th>
                      <th>Enrolled</th>
                      <th>Exp Enrollment</th>
                      <th>Withdrawn</th>
                      <th>Progress</th>
                      <th style="width: 40px">%</th>
                    </tr>
                  </thead>
                  <tbody>
                  	<c:forEach items="${siteList}" var="site">
                  		<tr>
	                      <td>${site.siteName }</td>
	                      <td>${site.enrolled }</td>
	                      <td>${site.totalSubjects }</td>
	                      <td>${site.withDron }</td>
	                      <td>
	                        <div class="progress progress-xs">
	                          <div class="progress-bar progress-bar-danger" style="width: ${site.persentage}%"></div>
	                        </div>
	                      </td>
	                      <td><span class="badge bg-danger">${site.persentage}%</span></td>
	                    </tr>
                  	</c:forEach>
                    
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->

            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Study Progress</h3>
              </div>
              <div class="card-body p-0">
                <table class="table table-condensed">
                  <thead>
                    <tr>
                      <th>Event Status</th>
                      <th>Of Events</th>
                      <th>Progress</th>
                      <th style="width: 40px">%</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${spList}" var="study">
                  		<tr>
	                      <td>${study.name }</td>
	                      <td>${study.enrolled }</td>
	                      <td>
	                        <div class="progress progress-xs">
	                          <div class="progress-bar progress-bar-danger" style="width: ${study.persentage}%"></div>
	                        </div>
	                      </td>
	                      <td><span class="badge bg-danger">${study.persentage}%</span></td>
	                    </tr>
                  	</c:forEach>
                  </tbody>
                </table>
              </div>
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
          <div class="col-md-6">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">Subject Enrollment For Study</h3>
              </div>
              <!-- /.card-header -->
              <div class="card-body p-0">
                <table class="table">
                  <thead>
                    <tr>
                      <th>Study</th>
                      <th>Enrolled</th>
                      <th>Expected Enrollment</th>
                      <th>Progress</th>
                      <th style="width: 40px">%</th>
                    </tr>
                  </thead>
                  <tbody>
                    <c:forEach items="${studyList}" var="study">
                  		<tr>
	                      <td>${study.studyNo }</td>
	                      <td>${study.enrolled }</td>
	                      <td>${study.totalSubjects }</td>
	                      <td>
	                        <div class="progress progress-xs">
	                          <div class="progress-bar progress-bar-danger" style="width: ${study.persentage}%"></div>
	                        </div>
	                      </td>
	                      <td><span class="badge bg-danger">${study.persentage}%</span></td>
	                    </tr>
                  	</c:forEach>
                  </tbody>
                </table>
              </div>
              <!-- /.card-body -->
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
        </div>
        <!-- /.row -->
        
      </div><!-- /.container-fluid -->