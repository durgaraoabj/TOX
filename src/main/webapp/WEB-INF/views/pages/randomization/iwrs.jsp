<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
      <div class="container-fluid">
        <div class="row">
          <!-- /.col-md-6 -->
         
          <div class="col-lg-6">
            <div class="card card-primary card-outline">
              <div class="card-header">
                <h5 class="m-0">IWRS</h5>
              </div>
              <div class="card-body">
                <a href='<c:url value="/randomization/ipRequest"/>' style="width: 130px" class="btn btn-primary" title="IP REQUEST">IP Request</a>
                <a href='<c:url value="/randomization/ipResponse"/>' style="width: 130px" class="btn btn-primary" title="IP RESPONSE">IP Response</a>
                <c:if test="${siteName eq null or siteName eq ''}">
                	<a href='<c:url value="/randomization/allocation"/>' style="width: 130px" class="btn btn-primary" title="Allocation">Allocation</a>
                </c:if>
                
              </div>
            </div>
          </div>
          <!-- /.col-md-6 -->
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->