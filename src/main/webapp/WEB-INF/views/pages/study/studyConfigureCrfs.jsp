<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Crf List</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th><c:if test="${configureStatus eq false}">
                		<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
						<div align="center">
							<input type="checkbox" id="checkall" onclick="checkall()"/> 
							<input type="button" id="selectCrfscomplete"
								value="Mark As Complete" class="btn btn-primary"
								style="width: 200px;">
						</div>
						</c:if>
					</c:if></th>
					<th>Crf Name</th>
					<th>Crf Description</th>
					<th>type</th>
					<th>Gender</th>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${crfs}" var="crf">
						<tr>
		
							<td><c:choose>
									<c:when test="${crf.configure eq  true}">
										<input type="checkbox" name="selected" id="selected"
											value="${crf.id}" checked="checked" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="selected" id="selected"
											value="${crf.id}" />
									</c:otherwise>
								</c:choose></td>
							<td>${crf.name}</td>
							<td>${crf.title}</td>
							<td>Library</td>
							<td>${crf.type}</td>
						</tr>
					</c:forEach>
					<c:forEach items="${stdLevelCrfs}" var="crf">
						<tr>
		
							<td><c:choose>
									<c:when test="${crf.configure eq  true}">
										<input type="checkbox" name="selectedstudy" id="selectedstudy"
											value="${crf.id}" checked="checked" />
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="selectedstudy" id="selectedstudy"
											value="${crf.id}" />
									</c:otherwise>
								</c:choose></td>
							<td>${crf.name}</td>
							<td>${crf.title}</td>
							<td>Study</td>
							<td>${crf.type}</td>
						</tr>
					</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th><c:if test="${configureStatus eq false}">
						<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
						<div align="center">
							<input type="button" id="selectCrfscomplete2"
								value="Mark As Complete" class="btn btn-primary"
								style="width: 200px;">
						</div>
						</c:if>
					</c:if></th>
					<th>Crf Name</th>
					<th>Crf Description</th>
					<th>type</th>
					<th>Gender</th>
					
                </tr>
                </tfoot>
              </table>     
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->

		<c:url value="/buildStdyCrf/saveconfigureCrfs" var="saveconfigureCrfs" />
		<form:form action="${saveconfigureCrfs}" method="post" id="submitform">
			<input type="hidden" name="crfIds" id="crfIds" />
			<input type="hidden" name="crfIdsStudy" id="crfIdsStudy" />
			<input type="hidden" name="markComplete" id="markComplete" value="No"/>
			<c:if test="${configureStatus eq false}">
				<div align="center">
					<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
						<input type="button" id="selectCrfs" value="Configure Crfs" 
							class="btn btn-primary" style="width: 200px;">
					</c:if>
				</div>
			</c:if>

		</form:form>

		<c:url value="/buildStdyCrf/configureCrfsMarkComplete"
			var="configureCrfsMarkComplet" />
		<form:form action="${configureCrfsMarkComplet}" method="post"
			id="configureCrfsMarkComplet">

		</form:form>
<script type="text/javascript">
function checkall(){
	if($('#checkall').is(":checked")){
		$.each($("input[name='selected']"), function(){
	        $(this).prop("checked", true);
	    });
		$.each($("input[name='selectedstudy']"), function(){
	        $(this).prop("checked", true);
	    });
	}else{
		$.each($("input[name='selected']"), function(){
	        $(this).prop("checked", false);
	    });
		$.each($("input[name='selectedstudy']"), function(){
	        $(this).prop("checked", false);
	    });
	}
}
	$('#selectCrfs').click(function() {
		var favorite = [];
		$.each($("input[name='selected']:checked"), function() {
			favorite.push($(this).val());
		});
		$('#crfIds').val(favorite.join(", "));
		var favoritestudy = [];
		$.each($("input[name='selectedstudy']:checked"), function() {
			favoritestudy.push($(this).val());
		});
		$('#crfIdsStudy').val(favoritestudy.join(", "));
		$('#submitform').submit();
	});

	$('#selectCrfscomplete').click(function() {
		var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
		if(flag){
			var favorite = [];
			$.each($("input[name='selected']:checked"), function() {
				favorite.push($(this).val());
			});
			$('#crfIds').val(favorite.join(", "));
			var favoritestudy = [];
			$.each($("input[name='selectedstudy']:checked"), function() {
				favoritestudy.push($(this).val());
			});
			$('#crfIdsStudy').val(favoritestudy.join(", "));
			$("#markComplete").val("Yes");
// 			$('#configureCrfsMarkComplet').submit();		
			$('#submitform').submit();
		}
	});
	$('#selectCrfscomplete2').click(function() {
		var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
		if(flag){
			var favorite = [];
			$.each($("input[name='selected']:checked"), function() {
				favorite.push($(this).val());
			});
			$('#crfIds').val(favorite.join(", "));
			var favoritestudy = [];
			$.each($("input[name='selectedstudy']:checked"), function() {
				favoritestudy.push($(this).val());
			});
			$('#crfIdsStudy').val(favoritestudy.join(", "));
			$("#markComplete").val("Yes");
// 			$('#configureCrfsMarkComplet').submit();
			$('#submitform').submit();
		}

	});
</script>
</html>