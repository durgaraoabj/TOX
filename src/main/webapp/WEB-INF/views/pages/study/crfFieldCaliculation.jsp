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
            <div class="card card-primary">
              <div class="card-header">
                <h3 class="card-title">Study E-From Calculation file Upload</h3>
              </div>
              <!-- /.card-header -->
              <!-- form start -->
              <c:url value="/buildStdyCrf/crfuploadxml" var="crfuploadUrl" />
				<sf:form action="${crfuploadUrl}" method="POST"
					modelAttribute="crfpojo" id="crfupload" enctype="multipart/form-data">
                <div class="card-body">
                  <div class="form-group">
                    <div class="input-group">
                      <div class="custom-file">
                        <input type="file" name="file" class="custom-file-input" id="exampleInputFile">
                        <label class="custom-file-label" for="exampleInputFile">Choose XML file</label>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- /.card-body -->

                <div class="card-footer">
                  
                  <button type="submit" class="btn btn-primary" onmousemove="test()" id="formSubmitBtn">Submit</button>
                </div>
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
										<a href='<c:url value="/buildStdyCrf/crfChangeCaliculationStatus/${crf.id}" />'
											class="btn btn-primary">ACTIVE</a>
									</c:if>
									<c:if test="${crf.status ne 'active'}">
										<a href='<c:url value="/buildStdyCrf/crfChangeCaliculationStatus/${crf.id}" />'
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