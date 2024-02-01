<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Sysmax</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
		   		<table id="example2" class="table table-bordered table-striped">
		   			<thead>
			   			<tr>
			   				<th>Animal Id</th>
			   				<th>WBC</th>
			   				<th>RCB</th>
			   				<th>HGB</th>
			   				<th>HCT</th>
			   			</tr>
			   		</thead>
			   		<tbody>
 		   			<c:forEach items="${sdlist}" var="dd">
			   			<tr>
			   				<td>${dd.animalSerialId}</td>
			   				<td>${dd.wbc}</td>
			   				<td>${dd.rcb}</td>
			   				<td>${dd.hgb}</td>
			   				<td>${dd.hct}</td>
			   				
			   			</tr>
			   		</c:forEach> 
			   		<tbody>
			   		<tfoot>
	                <tr>
	                	    <th>Animal Id</th>
			   				<th>WBC</th>
			   				<th>RCB</th>
			   				<th>HGB</th>
			   				<th>HCT</th>
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