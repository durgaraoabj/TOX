<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<c:if test="${not empty message}">
	<div class="row">
		<div class="col-xs-12 col-md-offset-2 col-md-8">
			<div class="alert alert-info fade in">${message}</div>
		</div>
	</div>
</c:if>
<body>
	<div class="box-header with-border">
    	<h3 class="box-title">CRF Upload</h3>
        <div class="box-tools pull-right">
   	    	<button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
            	<i class="fa fa-minus"></i>
            </button>
        </div>
    </div>
    <div class="box-body">
    	<c:url value="/administration/crf/crfupload" var="crfuploadUrl" />
    	<!--     	createEmployee -->
    	<sf:form action="${crfuploadUrl}" method="POST" modelAttribute="crfpojo"
    		id="crfupload" enctype="multipart/form-data">
            <table class="table table-bordered table-striped">
				<tr>
					<th colspan="4" style="color:blue; font-size: 19px; font-weight: bold; text-align: center;">Upload CRF</th>
				</tr>
				<tr>
					<td>Select CRF</td>
					<td>
						<input type="file" name="file"/>  
						<span id="empIdMsg" style="color: red"></span>
					</td>
				</tr>
			</table>
           	<div style="text-align:center"><input type="button" value="Upload" id="formSubmitBtn" class="btn btn-warning btn-sm"></div>
    	</sf:form>
	</div>
	<div id="jsonData"></div>
</body>
<script type="text/javascript">
	$('#formSubmitBtn').click(function(){
		alertify.confirm("Are you want to View CRF Preview?",
				function(e){
            if(e){
                alertify.success('you are click ok.');
        		$('#crfupload').submit();
            } else {
                alertify.error('you are clicked cancel.');
            }
		});
	});
</script>
<script type="text/javascript">
$(function(){
	$('a[title]').tooltip();
});
</script>
</html>