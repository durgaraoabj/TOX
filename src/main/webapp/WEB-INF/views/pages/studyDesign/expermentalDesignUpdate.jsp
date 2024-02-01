<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
 var groupIds = [];
 var groupGenArr =[];
 var subgroupIds = [];
 var dataIds =[];
 var count =[];
 var newGid = [];
 var newgtest = [];
 var genderArr = [];
 var animalNoId = [];
 var animalNoArr = [];
</script>
</head>
<body>
	<div class="card">
		<div class="card-header">
			<h3 class="card-title">Update Experimental Design View </h3><br/>
			<h5 class="card-title">Study : ${study.studyNo }</h5>
		</div>
	    <div class="box-body">
	    <c:url value="/expermentalDesign/updateExpermentalDesign" var="url"/>
	    <form:form action="${url}" method="post"  id="form" >
	    	<table class="table table-bordered table-striped">
	    		<tr>
	    			<th>Group</th>
	    			<th style="width: 15">Dose(mg/kg b.w)</th>
	    			<th style="width: 15">Conc. (mg/ml)</th>
	    			<th style="width: 10%">No.Of Animals</th>
	    			<th style="width: 10%">Sex</th>
	    			<td colspan="2">
	    				<table class="table table-bordered table-striped">
	    					<tr><th colspan="2" style="text-align: center"> <font style="text-align: center" >Animal Number</font></th></tr>
							<tr>	    				
								<th style="width: 10%">From</th>
								<th style="width: 10%">To</th>
							</tr>
						</table>
					</td>
				</tr>
				<c:forEach items="${group}" var="g">
					<tr>
		    			<th colspan="7">
		    				${g.groupName}
		    				<script type="text/javascript">
		    					groupIds.push('${g.id}');
		    					groupGenArr.push('${g.gender}');
		    				 </script>
		    			</th>
		    		</tr>
		    		<c:choose>
		    			<c:when test="${not empty g.subGroupInfo}">
		    			    <c:forEach begin="1" step="1" end="${g.groupTest}" var="c">
				    		   <c:forEach items="${g.subGroupInfo}" var="s" varStatus="std">
				    		       <c:choose>
				    		       	<c:when test="${c le std.count}">
				    		       		<c:if test="${c eq std.count}">
					    		   			<tr>	
							    				<td>
							    					<textarea rows="2" cols="10" class="form-control" name="subGroupName${g.id}_${s.id}" id="subGroupName${g.id}_${s.id}" >${s.name}</textarea>
							    					<font color="red" id="subGroupName${g.id}_${s.id}msg"></font>
							    					<script type="text/javascript">
								    					$(function (){
								    						subgroupIds.push('${s.id}');
								    						dataIds.push('${g.id}_${s.id}');
								    						count.push('${s.animalInfo.size()}');
								    					});
							    					</script>
							    				</td>
							    				<td>
							    					<input type="text" class="form-control" name="subGroupDose${g.id}_${s.id}" id="subGroupDose${g.id}_${s.id}" size="5" value="${s.dose}"/>
							    					<font color="red" id="subGroupDose${g.id}_${s.id}msg"></font>
							    				</td>
							    				<td>
							    					<input type="text" name="subGroupConc${g.id}_${s.id}" id="subGroupConc${g.id}_${s.id}" size="5" class="form-control" value="${s.conc}"/>
							    					<font color="red" id="subGroupConc${g.id}_${s.id}msg"></font>
							    				</td>
							    				
							    				<c:choose>
							    					<c:when test="${s.animalInfo.size() eq 1}">
							    						<c:forEach items="${s.animalInfo }" var="animals" varStatus="st">
							    								<c:choose>
							    									<c:when test="${animals.gender eq 'Male'}">
							    										<td>
							    											<input type="text" name="subGroupCount1${g.id}_${s.id}" id="subGroupCount1${g.id}_${s.id}" onblur="animalConuntCheck('subGroupCount1${g.id}_${s.id}', '${g.id}_${s.id}', 'subGroupCount1${g.id}_${s.id}msg')" size="3" class="form-control" value="${animals.count}"/>
							    											<font color="red" id="subGroupCount1${g.id}_${s.id}msg"></font>
							    											<script type="text/javascript">
							    												animalNoId.push('${g.id}_${s.id}');
							    											 	animalNoArr.push('${animals.count}');
							    											</script>
							    										</td>
							    										<td>${animals.gender}</td>
							    										<td>
							    											<input type="text" name="subGroupFrom1${g.id}_${s.id}" id="subGroupFrom1${g.id}_${s.id}" size="15" class="form-control" value="${animals.formId}"/>
							    											<font color="red" id="subGroupFrom1${g.id}_${s.id}msg"></font>
							    										</td>
							    										<td>
							    											<input type="text" name="subGroupTo${g.id}_${s.id}}" id="subGroupTo1${g.id}_${s.id}" size="15" class="form-control" value="${animals.toId}"/>
							    											<font color="red" id="subGroupTo${g.id}_${s.id}msg"></font>
							    										</td>
							    									</c:when>
							    									<c:otherwise>
							    										<td>
							    											<input type="text" name="subGroupCount1${g.id}_${s.id}_Female" id="subGroupCount1${g.id}_${s.id}_Female" onblur="animalConuntCheck('subGroupCount1${g.id}_${s.id}_Female', '${g.id}_${s.id}_Female', 'subGroupCount1${g.id}_${s.id}_Femalemsg')" size="3" class="form-control" value="${animals.count}"/>
							    											<font color="red" id="subGroupCount1${g.id}_${s.id}_Femalemsg"></font>
							    											<script type="text/javascript">
							    												animalNoId.push('${g.id}_${s.id}_Female');
							    											 	animalNoArr.push('${animals.count}');
							    											</script>
							    										</td>
							    										<td>${animals.gender}</td>
							    										<td>
							    											<input type="text" name="subGroupFrom1${g.id}_${s.id}_Female" id="subGroupFrom1${g.id}_${s.id}_Femlae" size="15" class="form-control" value="${animals.formId}"/>
							    											<font color="red" id="subGroupFrom1${g.id}_${s.id}_Femalemsg"></font>
							    										</td>
							    										<td>
							    											<input type="text" name="subGroupTo${g.id}_${s.id}}" id="subGroupTo1${g.id}_${s.id}_Female" size="15" class="form-control" value="${animals.toId}"/>
							    											<font color="red" id="subGroupTo${g.id}_${s.id}_Femalemsg"></font>
							    										</td>
							    									</c:otherwise>
							    								</c:choose>
					    								</c:forEach>
							    					</c:when>
							    					<c:otherwise>
							    						<td colspan="4">
							    							<table class="table table-bordered table-striped">
							    								<c:forEach items="${s.animalInfo }" var="animals" varStatus="st">
							    									<tr>
							    										<td>
							    											<input type="text" name="subGroupCount2${g.id}_${s.id}_${st.count}" id="subGroupCount2${g.id}_${s.id}_${st.count}" onblur="animalConuntCheck('subGroupCount2${g.id}_${s.id}_${st.count}', '${g.id}_${s.id}_${st.count}', 'subGroupCount2${g.id}_${s.id}_${st.count}msg')" size="3" class="form-control" value="${animals.count}"/>
					    													<font color="red" id="subGroupCount2${g.id}_${s.id}_${st.count}msg"></font>
					    													<script type="text/javascript">
							    												animalNoId.push('${g.id}_${s.id}_${st.count}');
							    											 	animalNoArr.push('${animals.count}');
							    											</script>
					    												</td>
							    										<td>${animals.gender}</td>
							    										<td>
							    											<input type="text" name="subGroupFrom2${g.id}_${s.id}_${st.count}" id="subGroupFrom2${g.id}_${s.id}_${st.count}" size="15" class="form-control" value="${animals.formId}"/>
					    													<font color="red" id="subGroupFrom2${g.id}_${s.id}_${st.count}msg"></font>	
							    										</td>
							    										<td>
							    											<input type="text" name="subGroupTo2${g.id}_${s.id}_${st.count}" id="subGroupTo2${g.id}_${s.id}_${st.count}" size="15" class="form-control" value="${animals.toId}"/>
					    													<font color="red" id="subGroupTo2${g.id}_${s.id}_${st.count}msg"></font>
							    										</td>
							    									</tr>
							    								</c:forEach>
							    							</table>
							    						</td>
							    					</c:otherwise>
							    				</c:choose>
							    			</tr>
				    		   			</c:if>
				    		       	</c:when>
				    		       </c:choose>
				    		   </c:forEach>
				    		   <c:if test="${c ge g.groupTest and c gt g.subGroupInfo.size()}">
		    		       			<tr>	
					    				<td>
					    					<textarea rows="2" cols="10" class="form-control" name="subGroupName3${g.id}_${c}" id="subGroupName3${g.id}_${c}"></textarea>
					    					<font color="red" id="subGroupName3${g.id}_${c}msg"></font>
					    				</td>
					    				<td style="width: 10">
					    					<input type="text" class="form-control" name="subGroupDose3${g.id}_${c}" id="subGroupDose3${g.id}_${c}" size="5"/>
					    					<font color="red" id="subGroupDose3${g.id}_${c}msg"></font>
					    				</td>
					    				<td style="width: 10">
					    					<input type="text" name="subGroupConc3${g.id}_${c}" id="subGroupConc3${g.id}_${c}" size="5" class="form-control"/>
					    					<font color="red" id="subGroupConc3${g.id}_${c}msg"></font>
					    				</td>
					    				<c:choose>
					    					<c:when test="${g.gender == 'Male'}">
					    						
			    									<td>
			    										<input type="text" name="subGroupCount3${g.id}_${c}" id="subGroupCount3${g.id}_${c}" size="3" class="form-control"/>
			    										<font color="red" id="subGroupCount3${g.id}_${c}msg"></font>
			    									</td>
			    									<td>
			    										<input type="radio" name="subGroupGender3${g.id}_${c}" id="subGroupGender3${g.id}_${c}" value="Male" checked/>Male
			    										<font color="red" id="subGroupGender3${g.id}_${s}msg"></font>
			    									</td>
			    									<td>
			    										<input type="text" name="subGroupFrom3${g.id}_${c}" id="subGroupFrom3${g.id}_${c}" size="15" class="form-control"/>
			    										<font color="red" id="subGroupFrom3${g.id}_${c}msg"></font>
			    									</td>
			    									<td>
			    										<input type="text" name="subGroupTo3${g.id}_${c}" id="subGroupTo3${g.id}_${c}" size="15" class="form-control"/>
			    										<font color="red" id="subGroupTo3${g.id}_${c}msg"></font>
			    									</td>
			    								
					    					</c:when>
					    					<c:otherwise>
					    						<c:choose>
					    							<c:when test="${g.gender == 'Female'}">
						    							<td>
				    										<input type="text" name="subGroupCount3${g.id}_${c}" id="subGroupCount3${g.id}_${c}" size="3" class="form-control" />
				    										<font color="red" id="subGroupCount3${g.id}_${c}msg"></font>
				    									</td>
				    									<td>
				    										<input type="radio" name="subGroupGender3${g.id}_${c}"  id="subGroupGender3${g.id}_${c}" value="Female" checked/>Female
				    										<font color="red" id="subGroupGender3${g.id}_${c}msg"></font>
				    									</td>
				    									<td>
				    										<input type="text" name="subGroupFrom3${g.id}_${c}" id="subGroupFrom3${g.id}_${c}" size="15" class="form-control"/>
				    										<font color="red" id="subGroupFrom3${g.id}_${c}msg"></font>
				    									</td>
				    									<td>
				    										<input type="text" name="subGroupTo3${g.id}_${c}" id="subGroupTo3${g.id}_${c}" size="15" class="form-control"/>
				    										<font color="red" id="subGroupTo3${g.id}_${c}msg"></font>
				    									</td>
					    							</c:when>
					    							<c:otherwise>
						    						  <td colspan="4">
						    							<table class="table table-bordered table-striped">
						    								<tr>
						    									<td>
						    										<input type="text" name="subGroupCount3${g.id}_${c}_Male" id="subGroupCount3${g.id}_${c}_Male" size="3" class="form-control" />
						    										<font color="red" id="subGroupCount${g.id}_${c}_Malemsg"></font>
						    									</td>
						    									<td>
						    										<input type="radio" name="subGroupGender3${g.id}_${c}_Male" id="subGroupGender3${g.id}_${c}_Male" value="Male" checked/>Male<br/>
						    										<font color="red" id="subGroupGender3${g.id}_${c}_Malemsg"></font>
						    									</td>
						    									<td>
						    										<input type="text" name="subGroupFrom3${g.id}_${c}_Male" id="subGroupFrom3${g.id}_${c}_Male" size="15" class="form-control"/>
						    										<font color="red" id="subGroupFrom3${g.id}_${s}_Malemsg"></font>
						    									</td>
						    									<td>
						    										<input type="text" name="subGroupTo3${g.id}_${c}_Male" id="subGroupTo3${g.id}_${c}_Male" size="15"  class="form-control"/>
						    										<font color="red" id="subGroupTo${g.id}_${c}_Malemsg"></font>
						    									</td>
						    								</tr>
						    								<tr>
						    									<td>
						    										<input type="text" name="subGroupCount3${g.id}_${c}_Female" id="subGroupCount3${g.id}_${c}_Female" size="3"  class="form-control"/>
						    										<font color="red" id="subGroupCount3${g.id}_${c}_Femalemsg"></font>
						    									</td>
						    									<td>
						    										<input type="radio" name="subGroupGender3${g.id}_${c}_Femlae" id="subGroupGender3${g.id}_${c}_Femlae" value="Female" checked/>Female<br/>
						    										<font color="red" id="subGroupGender3${g.id}_${c}_Femalemsg"></font>
						    									</td>
						    									<td>
						    										<input type="text" name="subGroupFrom3${g.id}${c}_Female" id="subGroupFrom3${g.id}_${c}_Female" size="15" class="form-control"/>
						    										<font color="red" id="subGroupFrom3${g.id}_${c}_Femalemsg"></font>
						    									</td>
						    									<td>
						    										<input type="text" name="subGroupTo3${g.id}_${c}_Female" id="subGroupTo3${g.id}_${c}_Female" size="15" class="form-control"/>
						    										<font color="red" id="subGroupTo3${g.id}_${c}_Femalemsg"></font>
						    									</td>
						    								</tr>
						    							</table>
						    						</td>
					    							</c:otherwise>
					    						</c:choose>
					    					</c:otherwise>
					    				</c:choose>
					    			</tr>
				    		   </c:if>
				    		</c:forEach>
		    			</c:when>
		    			<c:otherwise>
		    				<c:forEach begin="1" step="1" end="${g.groupTest}" var="s" varStatus="stc">
				    			<tr>	
				    				<td>
				    					<textarea rows="2" cols="10" class="form-control" name="subGroupName3${g.id}_${s}" id="subGroupName3${g.id}_${s}"></textarea>
				    					<font color="red" id="subGroupName3${g.id}_${s}msg"></font>
				    				</td>
				    				<td style="width: 10">
				    					<input type="text" class="form-control" name="subGroupDose3${g.id}_${s}" id="subGroupDose3${g.id}_${s}" size="5"/>
				    					<font color="red" id="subGroupDose3${g.id}_${s}msg"></font>
				    				</td>
				    				<td style="width: 10">
				    					<input type="text" name="subGroupConc3${g.id}_${s}" id="subGroupConc3${g.id}_${s}" size="5" class="form-control"/>
				    					<font color="red" id="subGroupConc3${g.id}_${s}msg"></font>
				    				</td>
				    				<c:choose>
				    					<c:when test="${g.gender == 'Male'}">
				    						
		    									<td>
		    										<input type="text" name="subGroupCount3${g.id}_${s}" id="subGroupCount3${g.id}_${s}" size="3" class="form-control"/>
		    										<font color="red" id="subGroupCount3${g.id}_${s}msg"></font>
		    									</td>
		    									<td>
		    										<input type="radio" name="subGroupGender3${g.id}_${s}" id="subGroupGender3${g.id}_${s}" value="Male" checked/>Male
		    										<font color="red" id="subGroupGender3${g.id}_${s}msg"></font>
		    									</td>
		    									<td>
		    										<input type="text" name="subGroupFrom3${g.id}_${s}" id="subGroupFrom3${g.id}_${s}" size="15" class="form-control"/>
		    										<font color="red" id="subGroupFrom3${g.id}_${s}msg"></font>
		    									</td>
		    									<td>
		    										<input type="text" name="subGroupTo3${g.id}_${s}" id="subGroupTo3${g.id}_${s}" size="15" class="form-control"/>
		    										<font color="red" id="subGroupTo3${g.id}_${s}msg"></font>
		    									</td>
		    								
				    					</c:when>
				    					<c:otherwise>
				    						<c:choose>
				    							<c:when test="${g.gender == 'Female'}">
					    							<td>
			    										<input type="text" name="subGroupCount3${g.id}_${s}" id="subGroupCount3${g.id}_${s}" size="3" class="form-control" />
			    										<font color="red" id="subGroupCount3${g.id}_${s}msg"></font>
			    									</td>
			    									<td>
			    										<input type="radio" name="subGroupGender3${g.id}_${s}"  id="subGroupGender3${g.id}_${s}" value="Female" checked/>Female
			    										<font color="red" id="subGroupGender3${g.id}_${s}msg"></font>
			    									</td>
			    									<td>
			    										<input type="text" name="subGroupFrom3${g.id}_${s}" id="subGroupFrom3${g.id}_${s}" size="15" class="form-control"/>
			    										<font color="red" id="subGroupFrom3${g.id}_${s}msg"></font>
			    									</td>
			    									<td>
			    										<input type="text" name="subGroupTo3${g.id}_${s}" id="subGroupTo3${g.id}_${s}" size="15" class="form-control"/>
			    										<font color="red" id="subGroupTo3${g.id}_${s}msg"></font>
			    									</td>
				    							</c:when>
				    							<c:otherwise>
					    						  <td colspan="4">
					    							<table class="table table-bordered table-striped">
					    								<tr>
					    									<td>
					    										<input type="text" name="subGroupCount3${g.id}_${s}_Male" id="subGroupCount3${g.id}_${s}_Male" size="3" class="form-control" />
					    										<font color="red" id="subGroupCount${g.id}_${s}_Malemsg"></font>
					    									</td>
					    									<td>
					    										<input type="radio" name="subGroupGender3${g.id}_${s}_Male" id="subGroupGender3${g.id}_${s}_Male" value="Male" checked/>Male<br/>
					    										<font color="red" id="subGroupGender3${g.id}_${s}_Malemsg"></font>
					    									</td>
					    									<td>
					    										<input type="text" name="subGroupFrom3${g.id}_${s}_Male" id="subGroupFrom3${g.id}_${s}_Male" size="15" class="form-control"/>
					    										<font color="red" id="subGroupFrom3${g.id}_${s}_Malemsg"></font>
					    									</td>
					    									<td>
					    										<input type="text" name="subGroupTo3${g.id}_${s}_Male" id="subGroupTo3${g.id}_${s}_Male" size="15"  class="form-control"/>
					    										<font color="red" id="subGroupTo${g.id}_${s}_Malemsg"></font>
					    									</td>
					    								</tr>
					    								<tr>
					    									<td>
					    										<input type="text" name="subGroupCount3${g.id}_${s}_Female" id="subGroupCount3${g.id}_${s}_Female" size="3"  class="form-control"/>
					    										<font color="red" id="subGroupCount3${g.id}_${s}_Femalemsg"></font>
					    									</td>
					    									<td>
					    										<input type="radio" name="subGroupGender3${g.id}_${s}_Femlae" id="subGroupGender3${g.id}_${s}_Femlae" value="Female" checked/>Female<br/>
					    										<font color="red" id="subGroupGender3${g.id}_${s}_Femalemsg"></font>
					    									</td>
					    									<td>
					    										<input type="text" name="subGroupFrom3${g.id}${s}_Female" id="subGroupFrom3${g.id}_${s}_Female" size="15" class="form-control"/>
					    										<font color="red" id="subGroupFrom3${g.id}_${s}_Femalemsg"></font>
					    									</td>
					    									<td>
					    										<input type="text" name="subGroupTo3${g.id}_${s}_Female" id="subGroupTo3${g.id}_${s}_Female" size="15" class="form-control"/>
					    										<font color="red" id="subGroupTo3${g.id}_${s}_Femalemsg"></font>
					    									</td>
					    								</tr>
					    							</table>
					    						</td>
				    							</c:otherwise>
				    						</c:choose>
				    					</c:otherwise>
				    					
				    					
				    				</c:choose>
				    			</tr>
				    			</c:forEach>
		    			</c:otherwise>
		    		</c:choose>
		    	</c:forEach>
				<tr align="center">
					<td colspan="7"><input type="button" id="submitBtn" onclick="submitBtnbut()" value="Update" class="btn btn-primary" style="width:200px;"></td>
				</tr>
			</table>
			<input type="hidden" name="subGroupName" id="subGroupName">
			<input type="hidden" name="subGroupDose" id="subGroupDose">
			<input type="hidden" name="subGroupConc" id="subGroupConc">
			<input type="hidden" name="subGroupCount" id="subGroupCount">
			<input type="hidden" name="subGroupFrom" id="subGroupFrom">
			<input type="hidden" name="subGroupTo" id="subGroupTo">
			
			<input type="hidden" name="newsubGroupName" id="newsubGroupName">
			<input type="hidden" name="newsubGroupDose" id="newsubGroupDose">
			<input type="hidden" name="newsubGroupConc" id="newsubGroupConc">
			<input type="hidden" name="newsubGroupCount" id="newsubGroupCount">
			<input type="hidden" name="newsubGroupFrom" id="newsubGroupFrom">
			<input type="hidden" name="newsubGroupTo" id="newsubGroupTo">
			<input type="hidden" name="newsubGroupGender" id="newsubGroupGender">
			
			
		</form:form>
	    </div>
	    <c:forEach items="${newGroupMap}" var="gmap">
	    	<script type="text/javascript">
	    	    var key = '${gmap.key}';
	    	    var value = '${gmap.value}';
	    	    var arr = value.split("##");
	    	    newGid.push(key);
	    		newgtest.push(arr[0]);
	    		genderArr.push(arr[1]);
	    	</script>
	    </c:forEach>
    </div>
</body>
<script type="text/javascript">
function animalConuntCheck(id, arrId, messageId){
	var value = $('#'+id).val();
	var index = animalNoId.indexOf(arrId);
	if(index != -1){
		var existsVal = animalNoArr[index];
		if(parseInt(value) >= parseInt(existsVal)){
			$('#'+messageId).html("");
		}else{
			$('#'+messageId).html("Animal No Should be Grater than (OR) equal to existing Animal No.");
			$('#'+id).val(existsVal);
		}
		
	}
}
</script>
<script type="text/javascript">
    var grNameFlag = false;
    var doseFlag = false;
    var countFlag = false;
    var concFlag = false;
    var fromFlag = false;
    var toFlag = false;
    var count2Flag = true;
    var from2Flag = true;
    var to2Flag = true;
    var  genderFlag = true;
    function submitBtnbut(){
		
		$('#subGroupName').val("");
		$('#subGroupDose').val("");
		$('#subGroupConc').val("");
		$('#subGroupCount').val("");
		$('#subGroupFrom').val("");
		$('#subGroupTo').val("");
		
		$('#newsubGroupName').val("");
		$('#newsubGroupDose').val("");
		$('#newsubGroupConc').val("");
		$('#newsubGroupCount').val("");
		$('#newsubGroupFrom').val("");
		$('#newsubGroupTo').val("");
		$('#newsubGroupGender').val("");
		
		var flag = true;
		if(dataIds.length > 0){
			for(var i=0; i<dataIds.length; i++){
				
				var tempArr = dataIds[i].split("_");
				var index = groupIds.indexOf(tempArr[0]);
				var ggender = groupGenArr[index];
// 				alert("gender is :"+ggender);
				if(ggender != "undefined"){
					//subgroup
					var subgnameVal = $('#subGroupName'+dataIds[i]).val();
					if(subgnameVal != ""){
						var value = $('#subGroupName').val();
						if(value == "")
							$('#subGroupName').val(dataIds[i]+"###"+subgnameVal);
						else $('#subGroupName').val(value +","+dataIds[i]+"###"+subgnameVal);
						$('#subGroupName'+dataIds[i]+"msg").html("");
						grNameFlag = true;
					}else{
						$('#subGroupName'+dataIds[i]+"msg").html("Requeired Field.");
						grNameFlag = false;
					}
					
					//Dose
					var doseVal = $('#subGroupDose'+dataIds[i]).val();
					if(doseVal != ""){
						var value = $('#subGroupDose').val();
						if(value == "")
							$('#subGroupDose').val(dataIds[i]+"###"+doseVal);
						else $('#subGroupDose').val(value +","+dataIds[i]+"###"+doseVal);
						$('#subGroupDose'+dataIds[i]+"msg").html("");
						doseFlag = true;
					}else{
						$('#subGroupDose'+dataIds[i]+"msg").html("Requeired Field.");
						doseFlag = false;
						return doseFlag;
					}
					//conc
					var concVal = $('#subGroupConc'+dataIds[i]).val();
					if(concVal != "" && concVal != "undefined"){
						var value = $('#subGroupConc').val();
						if(value == "")
							$('#subGroupConc').val(dataIds[i]+"###"+concVal);
						else $('#subGroupConc').val(value +","+dataIds[i]+"###"+concVal);
						$('#subGroupConc'+dataIds[i]+"msg").html("");
						concFlag = true;
					}else{
						if(concVal != ""){
							$('#subGroupConc'+dataIds[i]+"msg").html("Requeired Field.");
							concFlag = false;
							return concFlag;
						}
					}
// 					alert("id is : "+dataIds[i]);
					//count
					if(ggender == "Male"){
// 						alert("male condition is Started : subGroupCount1"+dataIds[i]);
						var countVal2 = $('#subGroupCount1'+dataIds[i]).val();
// 						alert("Male condition countVal2 :"+countVal2);
						if(countVal2 != "" && countVal2 != "undefined"){
							var value = $('#subGroupCount').val();
							if(value == "")
								$('#subGroupCount').val(dataIds[i]+"###"+countVal2);
							else $('#subGroupCount').val(value +","+dataIds[i]+"###"+countVal2);
							$('#subGroupCount1'+dataIds[i]+"msg").html("");
							countFlag = true;
						}else{
							if(countVal2 != ""){
								$('#subGroupCount1'+dataIds[i]+"msg").html("Requeired Field.");
								countFlag = false;
								return countFlag;
							}
							
						}
						//from
						var fromVal = $('#subGroupFrom1'+dataIds[i]).val();
// 						alert("Male condition fromVal :"+fromVal);
						if(fromVal != ""){
							var value = $('#subGroupFrom').val();
							if(value == "")
								$('#subGroupFrom').val(dataIds[i]+"###"+fromVal);
							else $('#subGroupFrom').val(value +","+dataIds[i]+"###"+fromVal);
							$('#subGroupFrom1'+dataIds[i]+"msg").html("");
							fromFlag = true;
						}else{
							$('#subGroupFrom1'+dataIds[i]+"msg").html("Requeired Field.");
							fromFlag = false;
							return fromFlag;
						}
						//to
						var toVal = $('#subGroupTo1'+dataIds[i]).val();
// 						alert("Male condition toVal :"+toVal);
						
						if(toVal != ""){
							var value = $('#subGroupTo').val();
							if(value == "")
								$('#subGroupTo').val(dataIds[i]+"###"+toVal);
							else $('#subGroupTo').val(value +","+dataIds[i]+"###"+toVal);
							$('#subGroupTo1'+dataIds[i]+"msg").html("");
							toFlag = true;
						}else{
							$('#subGroupTo1'+dataIds[i]+"msg").html("Requeired Field.");
							toFlag = false;
							return toFlag;
						}
					}
					if(ggender == "Female"){
						var countVal2 = $('#subGroupCount1'+dataIds[i]+"_Female").val();
// 						alert("FeMale condition countVal2 :"+countVal2);
						if(countVal2 != "" && countVal2 != "undefined"){
							var value = $('#subGroupCount').val();
							if(value == "")
								$('#subGroupCount').val(dataIds[i]+"###"+countVal2);
							else $('#subGroupCount').val(value +","+dataIds[i]+"###"+countVal2);
							$('#subGroupCount1'+dataIds[i]+"msg").html("");
							countFlag = true;
						}else{
							if(countVal2 != ""){
								$('#subGroupCount1'+dataIds[i]+"_Femalemsg").html("Requeired Field.");
								countFlag = false;
								return countFlag;
							}
							
						}
						//from
// 						alert("from dataid is :"+dataIds[i]);
						var fromVal2 = $('#subGroupFrom1'+dataIds[i]+"_Femlae").val();
// 						alert("FeMale condition fromVal :"+fromVal2);
						if(fromVal2 != ""){
							var value = $('#subGroupFrom').val();
							if(value == "")
								$('#subGroupFrom').val(dataIds[i]+"###"+fromVal2);
							else $('#subGroupFrom').val(value +","+dataIds[i]+"###"+fromVal2);
							$('#subGroupFrom1'+dataIds[i]+"_Femalemsg").html("");
							fromFlag = true;
						}else{
							$('#subGroupFrom1'+dataIds[i]+"_Femlaemsg").html("Requeired Field.");
							fromFlag = false;
							return fromFlag;
						}
						//to
						var toVal = $('#subGroupTo1'+dataIds[i]+"_Female").val();
// 						alert("FeMale condition toVal :"+toVal);
						if(toVal != ""){
							var value = $('#subGroupTo').val();
							if(value == "")
								$('#subGroupTo').val(dataIds[i]+"###"+toVal);
							else $('#subGroupTo').val(value +","+dataIds[i]+"###"+toVal);
							$('#subGroupTo1'+dataIds[i]+"_Femalemsg").html("");
							toFlag = true;
						}else{
							$('#subGroupTo1'+dataIds[i]+"_Femalemsg").html("Requeired Field.");
							toFlag = false;
							return toFlag;
						}
					}
					if(ggender == "Both"){
						var subId = dataIds[i].split("_");
		                var index = subgroupIds.indexOf(subId[1]);
		                var countVal = count[index];
						if(countVal != ""){
							for(var j=1; j<=parseInt(countVal); j++){
								//count2
								var count2Val = $('#subGroupCount2'+dataIds[i]+"_"+j).val();
								if(count2Val != ""){
									var value = $('#subGroupCount').val();
									if(value == "")
										$('#subGroupCount').val(dataIds[i]+"###"+count2Val);
									else $('#subGroupCount').val(value +","+dataIds[i]+"###"+count2Val);
									$('#subGroupCount2'+dataIds[i]+"_"+j+"msg").html("");
									count2Flag = true;
								}else{
									if(count2Val == ""){
										$('#subGroupCount2'+dataIds[i]+"_"+j+"msg").html("Requeired Field.");
										count2Flag = false;
										return count2Flag;
									}
								}
								//from
								var from2Val = $('#subGroupFrom2'+dataIds[i]+"_"+j).val();
								if(from2Val != ""){
									var value = $('#subGroupFrom').val();
									if(value == "")
										$('#subGroupFrom').val(dataIds[i]+"###"+from2Val);
									else $('#subGroupFrom').val(value +","+dataIds[i]+"###"+from2Val);
									$('#subGroupFrom2'+dataIds[i]+"_"+j+"msg").html("");
									from2Flag = true;
								}else{
									if(from2Val == ""){
										$('#subGroupFrom2'+dataIds[i]+"_"+j+"msg").html("Requeired Field.");
										from2Flag = false;
										return from2Flag;
									}
								}
								
								//to
								var to2Val = $('#subGroupTo2'+dataIds[i]+"_"+j).val();
								if(to2Val != ""){
									var value = $('#subGroupTo').val();
									if(value == "")
										$('#subGroupTo').val(dataIds[i]+"###"+to2Val);
									else $('#subGroupTo').val(value +","+dataIds[i]+"###"+to2Val);
									$('#subGroupTo2'+dataIds[i]+"_"+j+"msg").html("");
									to2Flag = true;
								}else{
									if(to2Val == ""){
										$('#subGroupTo2'+dataIds[i]+"_"+j+"msg").html("Requeired Field.");
										to2Flag = false;
										return to2Flag;
									}
								}
							}
						}
					}
				}
			}
		}
		if(newgtest.length > 0){
// 			alert("newgtest is : "+newgtest);
				for(var j=0; j<newgtest.length; j++){
					for(var k=1; k<=parseInt(newgtest[j]); k++){
						var gid = newGid[j];
						var sgnname = $('#subGroupName3'+gid+"_"+k).val();
						if(sgnname != "" && typeof(sgnname) != "undefined"){
							var value = $('#newsubGroupName').val();
							if(value == "")
								$('#newsubGroupName').val(gid+"###"+sgnname);
							else $('#newsubGroupName').val(value +","+gid+"###"+sgnname);
							$('#subGroupName3'+gid+"_"+k+"msg").html("");
							grNameFlag = true;
						}else{
							if(sgnname == ""){
								$('#subGroupName3'+gid+"_"+k+"msg").html("Requeired Field.");
								grNameFlag = false;
//	 							return grNameFlag;
							}
						}
						//Dose
						var doseVal = $('#subGroupDose3'+gid+"_"+k).val();
						if(doseVal != "" && typeof(doseVal) != "undefined"){
							var value = $('#newsubGroupDose').val();
							if(value == "")
								$('#newsubGroupDose').val(gid+"###"+doseVal);
							else $('#newsubGroupDose').val(value +","+gid+"###"+doseVal);
							$('#subGroupDose3'+gid+"_"+k+"msg").html("");
							doseFlag = true;
						}else{
							if(doseVal == ""){
								$('#subGroupDose3'+gid+"_"+k+"msg").html("Requeired Field.");
								doseFlag = false;
//	 							return doseFlag;	
							}
						}
						//conc
						var concVal = $('#subGroupConc3'+gid+"_"+k).val();
						if(concVal != "" && typeof(concVal) != "undefined"){
							var value = $('#newsubGroupConc').val();
							if(value == "")
								$('#newsubGroupConc').val(gid+"###"+concVal);
							else $('#newsubGroupConc').val(value +","+gid+"###"+concVal);
							$('#subGroupConc3'+gid+"_"+k+"msg").html("");
							concFlag = true;
						}else{
							if(concVal == ""){
								$('#subGroupConc3'+gid+"_"+k+"msg").html("Requeired Field.");
								concFlag = false;
//	 							return concFlag;	
							}
						}
						//count
						if(genderArr[j] == "Male" || genderArr[j] == "Female"){
							 var countVal1 = $('#subGroupCount3'+gid+"_"+k).val();
								if(countVal1 != "" && typeof(countVal1) != "undefined"){
									var value = $('#newsubGroupCount').val();
									if(value == "")
										$('#newsubGroupCount').val(gid+"_"+k+"###"+countVal1);
									else $('#newsubGroupCount').val(value +","+gid+"_"+k+"###"+countVal1);
									$('#subGroupCount3'+gid+"_"+k+"msg").html("");
									countFlag = true;
								}else{
									if(countVal1 == ""){  
										$('#subGroupCount3'+gid+"_"+k+"msg").html("Requeired Field.");
										countFlag = false;
//			 							return countFlag;
									}
								} 
								
								//gender
								var genderVal = $('#subGroupGender3'+gid+"_"+k).val();
								if(genderVal != "" && typeof(genderVal) != "undefined"){
									var value = $('#newsubGroupGender').val();
									if(value == "")
										$('#newsubGroupGender').val(gid+"_"+k+"###"+genderVal);
									else $('#newsubGroupGender').val(value +","+gid+"_"+k+"###"+genderVal);
									$('subGroupGender3'+gid+"_"+k+"_Malemsg").html("");
									genderFlag = true;
								}else{
									if(genderVal == ""){
										$('#subGroupGender3'+gid+"_"+k+"_Malemsg").html("Requeired Field.");
										genderFlag = false;
										return genderFlag;
									}
								}
								//from
								var fromVal = $('#subGroupFrom3'+gid+"_"+k).val();
								if(fromVal != "" && typeof(fromVal) != "undefined"){
									var value = $('#newsubGroupFrom').val();
									if(value == "")
										$('#newsubGroupFrom').val(gid+"_"+k+"###"+fromVal);
									else $('#newsubGroupFrom').val(value +","+gid+"_"+k+"###"+fromVal);
									$('#subGroupFrom3'+gid+"_"+k+"msg").html("");
									fromFlag = true;
								}else{
									if(fromVal == ""){
										$('#subGroupFrom3'+gid+"_"+k+"msg").html("Requeired Field.");
										fromFlag = false;
// 										return fromFlag;	
									}
								}
								
								//to
								var toVal = $('#subGroupTo3'+gid+"_"+k).val();
								if(toVal != "" && typeof(toVal) != "undefined"){
									var value = $('#newsubGroupTo').val();
									if(value == "")
										$('#newsubGroupTo').val(gid+"_"+k+"###"+toVal);
									else $('#newsubGroupTo').val(value +","+gid+"_"+k+"###"+toVal);
									$('#subGroupTo3'+gid+"_"+k+"msg").html("");
									toFlag = true;
								}else{
									if(toVal == ""){
										$('#subGroupTo3'+gid+"_"+k+"msg").html("Requeired Field.");
										toFlag = false;
// 										return toFlag;
									}
								}
						}
					   //count2
						if(genderArr[j] == "Both"){
							var countVal2 = $('#subGroupCount3'+gid+"_"+k+"_Male").val();
							if(countVal2 != "" && typeof(countVal2) !="undefined"){
								var value = $('#newsubGroupCount').val();
								if(value == "")
									$('#newsubGroupCount').val(gid+"_"+k+"###"+countVal2);
								else $('#newsubGroupCount').val(value +","+gid+"_"+k+"###"+countVal2);
								$('#subGroupCount3'+gid+"_"+k+"_Malemsg").html("");
								countFlag = true;
							}else{
								if(countVal2 == ""){
									$('#subGroupCount3'+gid+"_"+k+"_Malemsg").html("Requeired Field.");
									countFlag = false;
//		 							return countFlag;
								}
							} 
							var countVal33 = $('#subGroupCount3'+gid+"_"+k+"_Female").val();
							if(countVal33 != "" && typeof(countVal33) !="undefined"){
								var value = $('#newsubGroupCount').val();
								if(value == "")
									$('#newsubGroupCount').val(gid+"_"+k+"###"+countVal33);
								else $('#newsubGroupCount').val(value +","+gid+"_"+k+"###"+countVal33);
								$('#subGroupCount3'+gid+"_"+k+"_Femalemsg").html("");
								countFlag = true;
							}else{
								if(countVal33 == ""){
									$('#subGroupCount3'+gid+"_"+k+"_Femalemsg").html("Requeired Field.");
									countFlag = false;
//		 							return countFlag;
								}
							} 
								//gender						
								var genderVal2 = $('#subGroupGender3'+gid+"_"+k+"_Male").val();
								if(genderVal2 != "" && typeof(genderVal2) != "undefined"){
									var value = $('#newsubGroupGender').val();
									if(value == "")
										$('#newsubGroupGender').val(gid+"_"+k+"###"+genderVal2);
									else $('#newsubGroupGender').val(value+","+gid+"_"+k+"###"+genderVal2);
									$('subGroupGender3'+gid+"_"+k+"_Malemsg").html("");
									genderFlag = true;
								}else{
									if(genderVal2 == ""){
										$('#subGroupGender3'+gid+"_"+k+"_Malemsg").html("Requeired Field.");
										genderFlag = false;
// 										return genderFlag;
									}
									
								}
								var genderVal3 = $('#subGroupGender3'+gid+"_"+k+"_Femlae").val();
								if(genderVal3 != "" && typeof(genderVal3) != "undefined"){
									var value = $('#newsubGroupGender').val();
									if(value == "")
										$('#newsubGroupGender').val(gid+"_"+k+"###"+genderVal3);
									else $('#newsubGroupGender').val(value +","+gid+"_"+k+"###"+genderVal3);
									$('subGroupGender3'+gid+"_"+k+"_Femalemsg").html("");
									genderFlag = true;
								}else{
									if(genderVal3 == ""){
										$('#subGroupGender3'+gid+"_"+k+"_Femalemsg").html("Requeired Field.");
										genderFlag = false;
// 										return genderFlag;
									}
								}
								//from
								var fromVal = $('#subGroupFrom3'+gid+"_"+k+"_Male").val();
								if(fromVal != "" && typeof(fromVal) != "undefined"){
									var value = $('#newsubGroupFrom').val();
									if(value == "")
										$('#newsubGroupFrom').val(gid+"_"+k+"###"+fromVal);
									else $('#newsubGroupFrom').val(value +","+gid+"_"+k+"###"+fromVal);
									$('#subGroupFrom3'+gid+"_"+k+"_Malemsg").html("");
									fromFlag = true;
								}else{
									if(fromVal == ""){
										$('#subGroupFrom3'+gid+"_"+k+"_Malemsg").html("Requeired Field.");
										fromFlag = false;
//	 									return fromFlag;
									}
								}
								
								//from
								var fromVal2 = $('#subGroupFrom3'+gid+"_"+k+"_Female").val();
								if(fromVal2 != "" && typeof(fromVal2) != "undefined"){
									var value = $('#newsubGroupFrom').val();
									if(value == "")
										$('#newsubGroupFrom').val(gid+"_"+k+"###"+fromVal2);
									else $('#newsubGroupFrom').val(value +","+gid+"_"+k+"###"+fromVal2);
									$('#subGroupFrom3'+gid+"_"+k+"_Femalemsg").html("");
									fromFlag = true;
								}else{
									if(fromVal2 == ""){
										$('#subGroupFrom3'+gid+"_"+k+"_Femalemsg").html("Requeired Field.");
										fromFlag = false;
//	 									return fromFlag;
									}
								}
								
								//to
								var toVal = $('#subGroupTo3'+gid+"_"+k+"_Male").val();
								if(toVal != "" && typeof(toVal) != "undefined"){
									var value = $('#newsubGroupTo').val();
									if(value == "")
										$('#newsubGroupTo').val(gid+"_"+k+"###"+toVal);
									else $('#newsubGroupTo').val(value +","+gid+"_"+k+"###"+toVal);
									$('#subGroupTo3'+gid+"_"+k+"_Malemsg").html("");
									toFlag = true;
								}else{
									if(toVal == ""){
										$('#subGroupTo3'+gid+"_"+k+"_Malemsg").html("Requeired Field.");
										toFlag = false;
//	 									return toFlag;
									}
								}
								
								var toVal2 = $('#subGroupTo3'+gid+"_"+k+"_Female").val();
								if(toVal2 != "" && typeof(toVal2) != "undefined"){
									var value = $('#newsubGroupTo').val();
									if(value == "")
										$('#newsubGroupTo').val(gid+"_"+k+"###"+toVal2);
									else $('#newsubGroupTo').val(value +","+gid+"_"+k+"###"+toVal2);
									$('#subGroupTo3'+gid+"_"+k+"_Femalemsg").html("");
									toFlag = true;
								}else{
									if(toVal2 == ""){
										$('#subGroupTo3'+gid+"_"+k+"_Femalemsg").html("Requeired Field.");
										toFlag = false;
//	 									return toFlag;
									}
								}
						}
					}
				}
			
		}else{
			$('#newsubGroupName').val("0");
			$('#newsubGroupDose').val("0");
			$('#newsubGroupConc').val("0");
			$('#newsubGroupCount').val("0");
			$('#newsubGroupFrom').val("0");
			$('#newsubGroupTo').val("0");
			$('#newsubGroupGender').val("0");
		}
// 		alert("final Flag : "+grNameFlag);
// 		alert("final Flag is : "+grNameFlag +"::"+ doseFlag +"::"+ concFlag+"::"+countFlag+"::"+fromFlag+"::"+toFlag+"::"+count2Flag+"::"+from2Flag+"::"+to2Flag);
		  if(grNameFlag && doseFlag && concFlag && countFlag && from2Flag && toFlag && count2Flag && from2Flag && to2Flag)
		    $("#form").submit();  
	}
</script>
</html>