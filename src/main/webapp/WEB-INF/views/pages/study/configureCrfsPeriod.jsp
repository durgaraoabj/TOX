<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<input type="hidden" name="stdcrfConfiguation" id="stdcrfConfiguation" value="${stdcrfConfiguation}"/>
		<div class="card">
            <div class="card-header">
              <h3 class="card-title">Crf configuration to Period  =  ${periodName}</h3>
            </div>
            <!-- /.card-header -->
            <div class="card-body">
              <table id="example1" class="table table-bordered table-striped">
                <thead>
                <tr>
                	<th>
						<c:if test="${crfConfiguation eq false}">
							<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
								<input type="checkbox" id="checkall" onclick="checkall()"/>
								<div align="center"><input type="button" id="selectCrfscomplete" 
								value="Mark As Complete" class="btn btn-primary" style="width:100px;"></div>
							</c:if>
						</c:if>
					</th>
					<th>Crf Name</th>
					<th>Crf Description</th>
					<c:if test="${sp.status eq 'Yes' }">
					<th>
					<input type="checkbox" id="checkall2" onclick="checkall2()"/>Exit Crf</th>
					</c:if>
                </tr>
                </thead>
                <tbody>
                	<c:forEach items="${stdcrfsp}" var="crf">
						<tr>
							<td>
								<c:choose>
									<c:when test="${crf.periodCrfstatus eq  true}">
										<input type="checkbox" name="selected" id="selected" value="${crf.id}" checked="checked"/>
									</c:when>
									<c:otherwise>
										<input type="checkbox" name="selected" id="selected" value="${crf.id}"/>	
									</c:otherwise>
								</c:choose>
							</td>
							<td>${crf.name}</td>
							<td>${crf.title}</td>
							<c:if test="${sp.status eq 'Yes' }">
								<td>
									<c:choose>
										<c:when test="${crf.exitCrf eq  true}">
											<input type="checkbox" name="exitCrf" id="exitCrf" value="${crf.id}" checked="checked"/>
										</c:when>
										<c:otherwise>
											<input type="checkbox" name="exitCrf" id="exitCrf" value="${crf.id}"/>	
										</c:otherwise>
									</c:choose>
								</td>
							</c:if>
						</tr>
					</c:forEach>
                </tbody>
                <tfoot>
                <tr>
					<th>
						<c:if test="${crfConfiguation eq false}">
							<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
								<div align="center"><input type="button" id="selectCrfscomplete2" 
								value="Mark As Complete" class="btn btn-primary" style="width:100px;"></div>
							</c:if>
						</c:if>
					</th>
					<th>Crf Name</th>
					<th>Crf Description</th>
					<c:if test="${sp.status eq 'Yes' }">
						<th>Exit Crf</th>
					</c:if>
                </tr>
                </tfoot>
              </table>
            </div>
            <!-- /.card-body -->
          </div>
          <!-- /.card -->
          



    <c:url value="/buildStdyCrf/configureCrfsPeriodSave" var="configureCrfsPeriodSave"/>
    <form:form action="${configureCrfsPeriodSave}" method="post" id="submitform" >
    		<input type="hidden" name="periodId" id="periodId" value="${periodId}"/>
			<input type="hidden" name="crfIds" id="crfIds"/>
			<input type="hidden" name="exitcrfIds" id="exitcrfIds"/>
			<input type="hidden" name="markComplete" id="markComplete" value="No"/>
			<c:if test="${crfConfiguation eq false}">
				<c:if test="${userRole ne 'Study Director' and userRole ne 'Monitor'}">
					<div align="center"><input type="button" id="selectCrfs" value="Configure" class="btn btn-primary" style="width:200px;"></div>
				</c:if>
			</c:if>
    </form:form>
    
    <c:url value="/buildStdyCrf/configureCrfsMarkCompletePeriod" var="configureCrfsMarkComplet"/>
    <form:form action="${configureCrfsMarkComplet}" method="post" id="configureCrfsMarkComplet" >
			<input type="hidden" name="periodId" id="periodId" value="${periodId}"/>
    </form:form>
<script type="text/javascript">
function checkall(){
	if($('#checkall').is(":checked")){
		$.each($("input[name='selected']"), function(){
        $(this).prop("checked", true);
    	});
	}else{
		$.each($("input[name='selected']"), function(){
        	$(this).prop("checked", false);
   		});
	}
}
function checkall2(){
	if($('#checkall2').is(":checked")){
		$.each($("input[name='exitCrf']"), function(){
        $(this).prop("checked", true);
    });
	}else{
		$.each($("input[name='exitCrf']"), function(){
        $(this).prop("checked", false);
    });
	}
}
$('#selectCrfs').click(function(){
	if($("#stdcrfConfiguation").val() == 'true'){
	    var favorite = [];
	    $.each($("input[name='selected']:checked"), function(){            
	        favorite.push($(this).val());
	    });
	    $('#crfIds').val(favorite.join(", "));
	    favorite = [];
	    $.each($("input[name='exitCrf']:checked"), function(){            
	        favorite.push($(this).val());
	    });
	    $('#exitcrfIds').val(favorite.join(", "));
		$('#submitform').submit();		
	}else
		alert('Plase, complete study level crf configuration');

});
$('#selectCrfscomplete').click(function(){
	var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
	if(flag){
		if($("#stdcrfConfiguation").val() == 'true'){
			
			$("#markComplete").val("Yes");
			 var favorite = [];
		    $.each($("input[name='selected']:checked"), function(){            
		        favorite.push($(this).val());
		    });
		    $('#crfIds').val(favorite.join(", "));
		    favorite = [];
		    $.each($("input[name='exitCrf']:checked"), function(){            
		        favorite.push($(this).val());
		    });
		    $('#exitcrfIds').val(favorite.join(", "));
			$('#submitform').submit();		
// 			$('#configureCrfsMarkComplet').submit();
		}else
			alert('Plase, complete study level crf configuration');		
	}

});
$('#selectCrfscomplete2').click(function(){
	var flag = confirm("If Mark As Complete, Further We are Unable to Comfigure crfs.\nDo You Want to Continue?");
	if(flag){
		if($("#stdcrfConfiguation").val() == 'true'){
			
			$("#markComplete").val("Yes");
			 var favorite = [];
		    $.each($("input[name='selected']:checked"), function(){            
		        favorite.push($(this).val());
		    });
		    $('#crfIds').val(favorite.join(", "));
		    favorite = [];
		    $.each($("input[name='exitCrf']:checked"), function(){            
		        favorite.push($(this).val());
		    });
		    $('#exitcrfIds').val(favorite.join(", "));
			$('#submitform').submit();		
// 			$('#configureCrfsMarkComplet').submit();
		}else
			alert('Plase, complete study level crf configuration');
	}
});
</script>
</html>