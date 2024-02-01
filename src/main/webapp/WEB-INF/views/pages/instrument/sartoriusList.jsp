<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Sartorius</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
		   		<table id="example2" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<!-- <th>Animal Id</th> -->
			   				<th>CRF Name</th>
			   				<th>Item Name</th>
			   				<th>Weight</th>
			   				<th>User</th>
			   				<th>Created On</th>
			   			</tr>
			   		</thead>
			   		<tbody>
 		   			<c:forEach items="${cseivList}" var="cil">
			   			<tr>
			   				<%-- <td>${cil.animalSerialId}</td> --%>
			   				<td>${cil.crf.observationName}</td>
			   				<td>${cil.element.name}</td>
			   				<td>${cil.weight}</td>
			   				
			   				<td>${cil.userName}</td>
			   				<td>${cil.createdOn}</td>
			   				
			   			</tr>
			   		</c:forEach> 
			   		<tbody>
			   		<tfoot>
	                <tr>
	                	<!-- <th>Animal Id</th> -->
	                	<th>CRF Name</th>
			   			<th>Item Name</th>
			   			<th>Weight</th>
			   			<th>User</th>
			   			<th>Created On</th>
	                </tr>
	                </tfoot>
		   		</table>
            </div>
         </div>
         <script>
  $(function () {
    $('#example2').DataTable({
      "paging": true,
      "lengthChange": false,
      "searching": true,
      "ordering": true,
      "info": true,
      "autoWidth": false,
    });
  });;
</script>
</html>