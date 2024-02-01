<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
<%@ page isELIgnored="false" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <!-- Content Header (Page header) -->


<div class="content-header ">
      <div class="container-fluid">
        <div class="row mb-30">
          <div class="" style="width:100%;">
          		<c:if test="${pageMessage != null && pageMessge != ''}">
          		<div   style=" margin-left: 15%" align="center">
        		<script type="text/javascript">
          			    $(function(){
//           			    	debugger;
          			    	var msg = bs4Toast.success('Success', '${pageMessage}');
          			    });
          			</script>
 		    </div>
          			
          		</c:if>
          		<c:if test="${pageError != null && pageError != ''}">
         		     <script type="text/javascript">
          			    $(function(){
          			    	bs4Toast.error('Error', '${pageError}');
          			    });
          			</script>
          		</c:if>
        		<c:if test="${pageWarning != null && pageWarning != '' }">
       		      	<script type="text/javascript">
        			    $(function(){
        			    	bs4Toast.warning('warning', '${pageWarning}');
        			    });
        			</script>
        		</c:if>
             </div>
        </div><!-- /.row -->
      </div><!-- /.container-fluid -->
    </div>


