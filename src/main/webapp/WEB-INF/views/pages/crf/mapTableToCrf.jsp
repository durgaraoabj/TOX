<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	<div class="card">
	<c:url value="/admini/crf/mapTableToCrfSave" var="crfRuleSave" />
	<sf:form action="${crfRuleSave}" method="POST" id="crfSave"
		enctype="multipart/form-data">
		<div class="card-header">
			<h3 class="card-title">Map DB to Observation</h3>
		</div>
		
		<div class="card-body">
		<div style="width:60%;margin-left:25%">
			<div class="row">
				<div class="col-sm-4">
					<!-- text input -->
					<div class="form-group">
						<label>Select Observation</label> <select class="form-control"
							name="crfid" id="crfid" onchange="crfIdChange(this.value)">
							<option value="-1" selected="selected">--Select--</option>
							<c:forEach var="crf" items="${crfs}">
								<option value="${crf.id}">${crf.name}</option>
							</c:forEach>
						</select> <font color="red" id="crfidmsg"></font>
					</div>
				</div>
				<div class="col-sm-4">
					<!-- select -->
					<div class="form-group">
						<label>Select Section Element</label>
						<div id="secEleIdTd">
							<select name="secEleId" id="secEleId" class="form-control">
								<option value="-1" selected="selected">--Select--</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-4">
					<!-- text input -->
					<div class="form-group">
						<label>Select Table</label> 
						<select class="form-control"
							name="tableId" id="tableId" onchange="tableChange(this.value)">
							<option value="-1" selected="selected">--Select--</option>
							<c:forEach var="table" items="${tables}">
								<option value="${table.id}">${table.tableName}</option>
							</c:forEach>
						</select> <font color="red" id="tableIdmsg"></font>
					</div>
				</div>
				<div class="col-sm-4">
					<!-- select -->
					<div class="form-group">
						<label>Select Column</label>
						<div id="columnIdTd">
							<select name="columnId" id="columnId" class="form-control">
								<option value="-1" selected="selected">--Select--</option>
							</select>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-sm-4">
					<!-- text input -->
					<div class="form-group">
						<input type="button" value="Save" id="save" onclick="saveAction()"
	    						class="btn btn-primary" style="width:60px;margin-left:80%"/>
					</div>
				</div>
			</div>
			</div>
		</div>
	</sf:form>
</div>
<div class="card">
		<div class="card-header">
              <h3 class="card-title">All Mapped Elements</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>Observation NAME</th>
                	<th>Element</th>
					<th>Table</th>
					<th>Column</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${mppedElements}" var="map">
					<tr>
						<td>${map.crf.observationName}</td>
						<td>${map.element.name }</td>
						<td>${map.mappingTable.tableName }</td>
						<td>${map.mappingColumn.cloumnName }</td>
					</tr>
				</c:forEach>
                </tbody>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
</body>
<script type="text/javascript">
function crfIdChange(id){
	if(id == -1){
		$('#secEleIdTd').html('<select name="secEleId" id="secEleId"><option value="-1" selected="selected">--Select--</option></select>');
	}else{
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfSectionElementsSelectTable/"+id);
		if(result != '' || result != 'undefined'){
			$('#secEleIdTd').html(result);
		}
	}
	$("#crfidmsg").html();
}
function tableChange(id){
	if(id == -1){
		$('#secEleIdTd').html('<select name="columnId" id="columnId"><option value="-1" selected="selected">--Select--</option></select>');
	}else{
		var result = asynchronousAjaxCall(mainUrl+"/admini/crfRule/crfMappingTableColumns/"+id);
		if(result != '' || result != 'undefined'){
			$('#columnIdTd').html(result);
		}
	}
	$("#tableIdmsg").html();
}
function saveAction(){
	$("#crfSave").submit();
}
</script>
</html>