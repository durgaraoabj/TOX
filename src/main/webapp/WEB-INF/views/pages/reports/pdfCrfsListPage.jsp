<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Pdf Crfs List</title>
<script type="text/javascript">
var crfIds = [];
</script>
<script>
$(function () {
    $("#example1").DataTable({
      "paging": true,
      "lengthChange": true,
      "searching": true,
      "ordering": true,
      "info": true,
      "autoWidth": false,
    });
  });
</script>
</head>
<body>
    <!-- /.card-header -->
            <div class="card-body">
            <div style="text-align: center; font-size: large; color: red; font-weight: bold;" id="checkboxMsg"></div>
            <c:choose>
    		<c:when test="${crfList.size() > 0 }">
		   		<table id="example1" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Study No</th>
			   				<th>Volunteer ID</th>
			   				<th>Phase</th>
			   				<th>Crf Name</th>
			   				<th>Select</th>
			   				<th>Annoted Crf</th>
			   			</tr>
			   		</thead>
			   		<tbody>
		   			<c:forEach items="${crfList}" var="crf">
			   			<tr>
			   				<td>${crf.period.studyMaster.studyNo}</td>
			   				<td>${crf.vol.volId}</td>
			   				<td>${crf.period.name}</td>
			   				<td>${crf.stdCrf.crfName}</td>
			   				<td><input type="checkbox" name="crfId" id="crfId" value="${crf.id}">
			   				<td><a href='<c:url value="/reports/annotedCrfPdfReport/${crf.id}"/>' target="_blank"><img alt="" src="<c:url value="/static/images/pdfIcon.png"/>"/></a></td>
			   			</tr>
			   			 
			   		</c:forEach>
			   		<tbody>
			   		<tfoot>
	                  <tr>
						<th>Study No</th>
			   			<th>Volunteer ID</th>
			   			<th>Phase</th>
			   			<th>Crf Name</th>
			   			<th>Select</th>
			   			<th>Annoted Crf</th>
	                  </tr>
	                </tfoot>
		   		</table>
		   	</c:when>
		   	<c:otherwise>
		   	     <tr><td align="center">No Records Avaliable</td></tr>
		   	</c:otherwise>
    	</c:choose>
            </div>
            <!-- /.card-body -->                                                                                 
    <div align="center"><input type="button" value="Submit" class="btn btn-danger" onclick="pdfListSubmition()"></div>
 <c:url value ="/reports/generateSinglePdf" var="singlePdf"></c:url>
<form action="${singlePdf}" method="get" id="submtsinglePdfForm" target="_blank">
	<input type="hidden" name="crfsIds" id="crfsIds">
</form>
</body>

<script type="text/javascript">
      function  pdfListSubmition(){
    	  var flag = false;
    	  $('#crfsIds').val("");
    	  var numberOfChecked = $('input:checkbox:checked').length;
    	//  alert("no of Checkboxces checked :"+numberOfChecked);
    	  if(numberOfChecked != 0){
    		//  alert("if condition");
    		  $('#checkboxMsg').html("");
    		  flag=true;
    	  }else{
    		//  alert("else condition");
    		  $('#checkboxMsg').html("Please Select at least One CheckBox.")
    		  flag = false;
    	  }
    	  if(flag){
    		//  alert("Every thing ok");
    		  var ids = [];
              $.each($("input[name='crfId']:checked"), function(){
                  ids.push($(this).val());
              });
              for(var j=0; j<ids.length; j++){
            	  var idNo = $('#crfsIds').val();
            	 // alert("idNo val is :"+idNo);
            	  if(idNo != ""){
            		  $('#crfsIds').val(idNo+"##"+ids[j]);
            	  }else{
            		  $('#crfsIds').val(ids[j]); 
            	  }
              }
    		  $('#submtsinglePdfForm').submit();
    	  }
      }
</script>
</html>