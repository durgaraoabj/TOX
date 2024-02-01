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
<body>
<c:if test="${not empty message}">
	<div class="row">
		<div class="col-xs-12 col-md-offset-2 col-md-8">
			<div class="alert alert-info fade in">${message}</div>
		</div>
	</div>
</c:if>
	<div class="box-header with-border">
    	<h3 class="box-title">CRF Name : ${crf.crfDesc}</h3>
        <div class="box-tools pull-right">
   	    	<button type="button" class="btn btn-box-tool" data-widget="collapse" data-toggle="tooltip" title="Collapse">
            	<i class="fa fa-minus"></i>
            </button>
        </div>
    </div>
    <div class="box-body">
    <c:forEach begin="1" step="1" end="${maxSections }" var="key">
		<table class="table table-bordered table-striped">
		<tr>
			<td>
			<c:set var="sec" value="${sections[key]}"></c:set>
			<c:if test="${sec.active}">
				<h5><c:out value="${sec.sectionDesc}"/></h5>
				<table border="1">
					<c:choose>
					<c:when test="${sec.containsGroup eq 'no'}">
						<c:forEach begin="1" step="1" end="${sec.maxRows}" var="row">
						<tr>
							<c:forEach begin="1" step="1" end="${sec.maxColumns}" var="col">
								<c:set var="itemkey" >${sec.sectionName},${row},${col}</c:set>
								<c:set var="item" value="${items[itemkey]}"></c:set>
								<c:choose>
									<c:when test="${item ne null}">
										<td>
											<table>
												<tr>
													<td>${item.itemDesc}</td>
													<td>
														<c:choose>
															<c:when test="${item.itemType eq 'text'}">
																<input type="text" value="" name="${item.itemName}"/>
															</c:when>
															<c:when test="${item.itemType eq 'textarea'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:when test="${item.itemType eq 'radio'}">
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<input type="radio" name="${item.itemName}" value="${itemval.elemenstValue}"> ${itemval.elemenstValue}<br>
																</c:forEach>
															</c:when>
															<c:when test="${item.itemType eq 'checkbox'}">
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<input type="checkbox" name="${item.itemName}" value="${itemval.elemenstValue}"> ${itemval.elemenstValue}<br>
																</c:forEach>
															</c:when>
															<c:when test="${item.itemType eq 'select'}">
																<select name="${item.itemName}">
																	<option value="-1">--Select--</option>
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<option value="${itemval.elemenstValue}">${itemval.elemenstValue}</option>
																</c:forEach>
																</select>
															</c:when>
															<c:when test="${item.itemType eq 'date'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:when test="${item.itemType eq 'datetime'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:otherwise>${item.responseValue}</c:otherwise>
														</c:choose>
													</td>
													<td>${item.itemDescRight}</td>
												</tr>
												<tr>
													<td colspan="3"></td>
												</tr>
											</table>
										</td>
									</c:when>
									<c:otherwise><td> </td></c:otherwise>
								</c:choose>
							</c:forEach>
						</tr>
						</c:forEach>	
						</c:when>
						<c:otherwise>
							
							<c:forEach begin="1" step="1" end="5" var="row">
							<tr>
							<c:forEach begin="1" step="1" end="${sec.maxColumns}" var="col">
								<c:set var="itemkey" >${sec.sectionName},1,${col}</c:set>
								<c:set var="item" value="${items[itemkey]}"></c:set>
								<c:choose>
									<c:when test="${item ne null}">
										<td>
											<table>
												<tr>
													<td>${item.itemDesc}</td>
													<td>
														<c:choose>
															<c:when test="${item.itemType eq 'text'}">
																<input type="text" value="" name="${item.itemName}"/>
															</c:when>
															<c:when test="${item.itemType eq 'textarea'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:when test="${item.itemType eq 'radio'}">
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<input type="radio" name="${item.itemName}" value="${itemval.elemenstValue}"> ${itemval.elemenstValue}<br>
																</c:forEach>
															</c:when>
															<c:when test="${item.itemType eq 'checkbox'}">
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<input type="checkbox" name="${item.itemName}" value="${itemval.elemenstValue}"> ${itemval.elemenstValue}<br>
																</c:forEach>
															</c:when>
															<c:when test="${item.itemType eq 'select'}">
																<select name="${item.itemName}">
																	<option value="-1">--Select--</option>
																<c:forEach items="${item.itemResponceValues}" var="itemval">
																	<option value="${itemval.elemenstValue}">${itemval.elemenstValue}</option>
																</c:forEach>
																</select>
															</c:when>
															<c:when test="${item.itemType eq 'date'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:when test="${item.itemType eq 'datetime'}">
																<textarea rows="3" cols="10" name="${item.itemName}"></textarea>
															</c:when>
															<c:otherwise>${item.responseValue}</c:otherwise>
														</c:choose>
													</td>
													<td>${item.itemDescRight}</td>
												</tr>
												<tr>
													<td colspan="3"></td>
												</tr>
											</table>
										</td>
									</c:when>
									<c:otherwise><td> </td></c:otherwise>
								</c:choose>
							</c:forEach>
							</tr>
							</c:forEach>
							
							
						</c:otherwise>
					</c:choose>
									
				</table>
			</c:if>
			</td>
		</tr>
		</table>
	</c:forEach>	
		<br/>
		<c:url value="/administration/crf/crfuploadsave" var="crfuploadsaveUrl" />		
		<sf:form action="${crfuploadsaveUrl}" method="GET" modelAttribute="crfpojo"
    		id="crfuploadsave" enctype="multipart/form-data">
            <div style="text-align:center">
            	<input type="button" value="SAVE" id="formSubmitBtn" class="btn btn-warning btn-sm">
            </div>
    	</sf:form>
	</div>
</body>
<script type="text/javascript">
	$('#formSubmitBtn').click(function(){
		alertify.confirm("Are you want to SAVE CRF",
		function(e){
            if(e){
                alertify.success('you are click ok.');
        		$('#crfuploadsave').submit();
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