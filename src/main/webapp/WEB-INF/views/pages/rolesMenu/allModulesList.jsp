<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isErrorPage="false"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>All Links List</title>
<script type="text/javascript">
	var menuNames = [];
	var msLinks = [];
	var idsArr = [];
	
</script>
</head>
<body>
	<div class="card">
	<div id="linksMsg" style="font-weight: bold; font-size: large; color: red; text-align: center;"></div>
	  <input type="hidden" name="roleId" id="roleId" value="${roleId}">
	   <table>
	   <c:set value="${0}" var="count"></c:set>
	   		<c:forEach items="${map}" var="entry">
			 	<c:choose>
					<c:when test="${count eq 0}">
						<tr></tr>
						<td>
							 <div class="container-fluid">
        						<div class="row">
          							<div class="col-md-12">
			 							<div class="card">
			 								 <div class="card-header" style="background: #139BF3; border: 1%; border-color: #C70039;">
			                					<h3 class="card-title" style="color:white;">${entry.key}</h3>
			                					<c:set var="string1" value="${entry.key}"/>
												<c:set var="string2" value="${fn:replace(string1,' ', '')}" />
			                					<div align="right"><input type="checkbox" name="menuCheckbox" id="menuCheckbox_${string2}" onclick="mainMenuSublinksValidation('${entry.key}')"></div>
			         						 </div>
			           						<div class="card-body p-0">
			           						<c:set var="lcount" value="${0}"></c:set>
			           						<table border="1" bordercolor="#985D41" style="width: 100%; background: #DEB887;">
				           						<c:forEach items="${entry.value}" var="sm">
		           									<c:choose>
			           									<c:when test="${lcount eq 0}">
			           										<tr></tr>
			           										<td>
			           											<input type="checkbox" name="smlink" id="smLink_${sm.id}"  value="${sm.id}" onclick="addSublinks('smLink_${sm.id}')">
			           											&nbsp;&nbsp;${sm.sideLink}
			           											<c:set var="lcount" value="${lcount+1}"></c:set>
			           											<script type="text/javascript">
			           											   var menuName = '${entry.key}';
			           											   var finalName = menuName.replace(/\s/g, '');
			           											   if(menuNames.indexOf(finalName) != -1){
			           												   var index = menuNames.indexOf(finalName);
			           												   var linksStr = msLinks[index];
			           												   linksStr = linksStr+"##"+'${sm.id}';
			           												   msLinks[index] = linksStr;
			           											   }else{
			           													menuNames.push(finalName);
			           													msLinks.push('${sm.id}');
			           											   }
			           											</script>
			           										</td>
			           										
			           								</c:when>
			           								<c:otherwise>
			           									<td>
		           											<input type="checkbox" name="smlink" id="smLink_${sm.id}" value="${sm.id}" onclick="addSublinks('smLink_${sm.id}')">
		           											&nbsp;&nbsp;${sm.sideLink}
		           											<c:set var="lcount" value="${lcount+1}"></c:set>
				           									<c:if test="${lcount eq 2}">
				           										<c:set var="lcount" value="${0}"></c:set>
				           									</c:if>
				           									<script type="text/javascript">
			           											   var menuName = '${entry.key}';
			           											   var finalName = menuName.replace(/\s/g, '');
			           											   if(menuNames.indexOf(finalName) != -1){
			           												   var index = menuNames.indexOf(finalName);
			           												   var linksStr = msLinks[index];
			           												   linksStr = linksStr+"##"+'${sm.id}';
			           												   msLinks[index] = linksStr;
			           											   }else{
			           													menuNames.push(finalName);
			           													msLinks.push('${sm.id}');
			           											   }
			           											</script>
			           									</td>
			           								</c:otherwise>
			           								</c:choose>
				           						</c:forEach>
				           						</table>
			           						</div>
			           					</div>
			           				</div>
			           			</div>
			           		</div>
			           		<c:set value="${count+1}" var="count"></c:set>
						</td>
					</c:when>
					<c:otherwise>
						<td>
							 <div class="container-fluid">
        						<div class="row">
          							<div class="col-md-12">
			 							<div class="card">
			 								 <div class="card-header" style="background: #139BF3; border: 1%; border-color: #C70039;">
			                					<h3 class="card-title" style="color:white;">${entry.key}</h3>
			                					<c:set var="string1" value="${entry.key}"/>
												<c:set var="string2" value="${fn:replace(string1,' ', '')}" />
			                					<div align="right"><input type="checkbox" name="menuCheckbox" id="menuCheckbox_${string2}" onclick="mainMenuSublinksValidation('${entry.key}')"></div>
			         						 </div>
			           						<div class="card-body p-0">
			           							<c:set var="licount1" value="${0}"></c:set>
			           							<table border="1" bordercolor="#985D41" style="width: 100%; background: #DEB887;">
				           							<c:forEach items="${entry.value}" var="sm">
			           									<c:choose>
				           									<c:when test="${licount1 eq 0}">
				           										<tr></tr>
				           										<td>
				           											<input type="checkbox" name="smlink" id="smLink_${sm.id}" value="${sm.id}" onclick="addSublinks('smLink_${sm.id}')">
				           											&nbsp;&nbsp;${sm.sideLink}
				           											<c:set var="licount1" value="${licount1+1}"></c:set>
				           											<script type="text/javascript">
				           											   var menuName = '${entry.key}';
				           											   var finalName = menuName.replace(/\s/g, '');
				           											   if(menuNames.indexOf(finalName) != -1){
				           												   var index = menuNames.indexOf(finalName);
				           												   var linksStr = msLinks[index];
				           												   linksStr = linksStr+"##"+'${sm.id}';
				           												   msLinks[index] = linksStr;
				           											   }else{
				           													menuNames.push(finalName);
				           													msLinks.push('${sm.id}');
				           											   }
				           											</script>
				           										</td>
				           									</c:when>
				           									<c:otherwise>
					           									<td>
				           											<input type="checkbox" name="smlink" id="smLink_${sm.id}" value="${sm.id}" onclick="addSublinks('smLink_${sm.id}')">
				           											&nbsp;&nbsp;${sm.sideLink}
				           											<c:set var="licount1" value="${licount1+1}"></c:set>
						           									<c:if test="${licount1 eq 2}">
						           										<c:set var="licount1" value="${0}"></c:set>
						           									</c:if>
						           									<script type="text/javascript">
				           											   var menuName = '${entry.key}';
				           											   var finalName = menuName.replace(/\s/g, '');
				           											   if(menuNames.indexOf(finalName) != -1){
				           												   var index = menuNames.indexOf(finalName);
				           												   var linksStr = msLinks[index];
				           												   linksStr = linksStr+"##"+'${sm.id}';
				           												   msLinks[index] = linksStr;
				           											   }else{
				           													menuNames.push(finalName);
				           													msLinks.push('${sm.id}');
				           											   }
				           											</script>
					           									</td>
				           									</c:otherwise>
				           								</c:choose>
				           							</c:forEach>
				           							</table>
			           							</div>
			           						<c:set value="${count+1}" var="count"></c:set>
			           					</div>
			           				</div>
			           			</div>
			           		</div>
						</td>
						<c:if test="${count eq 3}">
							<c:set value="${0}" var="count"></c:set>
						</c:if>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<tr align="center">
				<td colspan="3"><input type="button" value="Submit" class="btn btn-danger btn-md" onclick="submitSelectedLinks()"></td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
	    function addSublinks(id){
			if($('#'+id).prop("checked") == true){
				var valIndex = idsArr.indexOf($('#'+id).val());
		    	if(valIndex == -1)
		    		idsArr.push($('#'+id).val());
		    }else{
		    	var index = idsArr.indexOf($('#'+id).val());
		        if(index != -1){
		        	var removed = idsArr.splice(index,1);
		        }
		    }
		}
	</script>
	<script type="text/javascript">
		function mainMenuSublinksValidation(name){
			var mName = name.replace(/\s/g, '');
			if($('#menuCheckbox_'+mName).prop("checked") == true){
				var index = menuNames.indexOf(mName);
				var linkStr = msLinks[index];
				if(linkStr != null && linkStr !="" && linkStr !="undefined"){
					var temp = linkStr.split("##");
					if(temp.length > 0){
						for(var j=0; j<temp.length; j++){
							$('#smLink_'+temp[j]).prop('checked', true);
							idsArr.push($('#smLink_'+temp[j]).val());
						}
					}
				}
			}else{
				var index = menuNames.indexOf(mName);
				var linkStr = msLinks[index];
				if(linkStr != null && linkStr !="" && linkStr !="undefined"){
					var temp = linkStr.split("##");
					if(temp.length > 0){
						for(var j=0; j<temp.length; j++){
							$('#smLink_'+temp[j]).prop('checked', false);
							var index = idsArr.indexOf($('#smLink_'+temp[j]).val());
					        if(index != -1){
					        	var removed = idsArr.splice(index,1);
					        }
						}
					}
				}
			}
			
			
		}
	</script>
</body>
</html>