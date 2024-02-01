<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
		<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
		<!-- general form elements -->
            <div class="card ">
              <div class="card-header">
                <h3 class="card-title">Calculation File Upload</h3>
              </div>
              <!-- /.card-header -->
              <!-- form start -->
              <c:url value="/admini/crf/crfuploadxml" var="crfuploadUrl" />
				<sf:form action="${crfuploadUrl}" method="POST"
					modelAttribute="crfpojo" id="crfupload" enctype="multipart/form-data">
                <div class="card-body">
                  <div class="form-group">
                    <label for="exampleInputFile">Upload XML File</label>
                    <div class="input-group">
                      <div class="custom-file">
                        <input type="file" name="file" class="custom-file-input" id="exampleInputFile" >
                        <label class="custom-file-label" for="exampleInputFile" style="width:240">Choose file</label>
                      </div>
                    </div>
                  </div>
<!--                   <div class="form-check"> -->
<!--                     <input type="checkbox" class="form-check-input" id="exampleCheck1"> -->
<!--                     <label class="form-check-label" for="exampleCheck1">Check me out</label> -->
<!--                   </div> -->
                </div>
                <!-- /.card-body -->
				<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
	                <div class="card-footer">
	                  <button type="submit" class="btn btn-primary" onmousemove="test()" id="formSubmitBtn">Submit</button>
	                </div>
	            </c:if>
              </sf:form>
            </div>
            <!-- /.card -->	
            </c:if>
            
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">All E-Forms Elements Calculation Info</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>CRF NAME</th>
					<th>Result Filed</th>
					<th>Calculation</th>
					<th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${list}" var="crf">
					<tr>
						<td>${crf.crf.name }</td>
						<td>${crf.resultField }</td>
						<td>${crf.caliculation }</td>
						<c:choose>
							<c:when test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
								<td><c:if test="${crf.status eq 'active'}">
										<a href='<c:url value="/admini/crf/crfChangeStatus/${crf.id}" />'
											class="btn btn-primary">ACTIVE</a>
									</c:if>
									<c:if test="${crf.status ne 'active'}">
										<a href='<c:url value="/admini/crf/crfChangeStatus/${crf.id}" />'
											class="btn btn-primary">NOT ACTIVE</a>
									</c:if>
								</td>
							</c:when>
							<c:otherwise>
								<td><c:if test="${crf.status eq 'active'}">ACTIVE</c:if>
									<c:if test="${crf.status ne 'active'}">NOT ACTIVE</c:if>
								</td>
							</c:otherwise>
						</c:choose>
						
					</tr>
				</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>CRF NAME</th>
					<th>Result Filed</th>
					<th>Calculation</th>
					<th>Status</th>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
</body>
</html>