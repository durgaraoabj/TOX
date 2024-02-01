<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Form Elements Page</title>
</head>
<body>
<table>
	<tr>
  		<td>Form Name : ${crf.name}</td>
  	</tr>
</table>
  <table class="table table-bordered"> 
  	<thead>
  		<tr>
  			<th>Section</th>
  			<th>Elements</th>
  		</tr>
  	</thead>
  	<tbody>
  	<c:forEach  items="${crf.sections}" var="sec">
  		<tr>
  			<td>${sec.name}</td>
  			<td>
  				<table class="table table-bordered">
  					<c:set value="${1}" var="count"></c:set>
			  		<c:forEach items="${sec.element}" var="sele">
			  			<c:if test="${count eq '1'}">
			  				<tr>
			  			</c:if>
			  		    <c:choose>
			  		    	<c:when test="${count lt '5'}">
			  		    		 <td>
									${sele.name}
								    &nbsp;&nbsp;&nbsp;&nbsp;
								    <input type="checkbox" name="sec_${sec.id}_${sele.id}" id="sec_${sec.id}_${sele.id}">
								    <c:set value="${count+1}" var="count"></c:set>
								</td>
							</c:when>
			  		    	<c:otherwise>
			  		    		 <td>
									${sele.name}
								    &nbsp;&nbsp;&nbsp;&nbsp;
								    <input type="checkbox" name="sec_${sec.id}_${sele.id}" id="sec_${sec.id}_${sele.id}">
								</td>
								<c:if test="${count eq '5'}">
			  						</tr>
			  						<c:set value="${1}" var="count"></c:set>
			  					</c:if>
							</c:otherwise>
			  		    </c:choose>
			  		</c:forEach>
  				</table>
  			</td>
  		</tr>
  	</c:forEach>
  	</tbody>
 </table>
</body>
</html>