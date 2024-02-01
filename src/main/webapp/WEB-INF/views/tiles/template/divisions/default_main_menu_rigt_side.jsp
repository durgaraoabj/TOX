<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
	    <!-- Right navbar links -->
    <ul class="navbar-nav ml-auto">
    <li class="nav-item">
        <%-- <a class="nav-link"  href='#'>
          Current Study&nbsp;:&nbsp;<B>${activeStudyNo}</B>
        </a> --%>
      </li> 
   <li><div style="padding-right:40px">
	<table >
	<tr>
	<Td class="nav-item">Current Study&nbsp;:&nbsp;<B>${activeStudyNo}</B></Td>
	</tr>
		<%-- <tr>
			<Td class="nav-item">User&nbsp;:&nbsp;<B>${userName}</B></Td>
		</tr> --%>
		<tr>
			<Td class="nav-item">Role&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;<B >${userRole}</B></Td>
		</tr>
	</table>
	</div>
	
	</li> 
	
      <!-- Notifications Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-bell" style="margin-top:5px"></i>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <div class="dropdown-divider"></div>	
        <%--   &nbsp;User : ${userName}
          <a href="#" class="dropdown-item">
            <span class="float-right text-muted text-sm"></span>
          </a>
          <div class="dropdown-divider"></div>
          &nbsp;Role : ${userRole}
          <a href="#" class="dropdown-item">
            <span class="float-right text-muted text-sm"></span>
          </a> --%>
          <c:if test="${userRole ne 'ADMIN'}">
	          <div class="dropdown-divider"></div>
	          <%-- &nbsp;Current Study : ${activeStudyNo}--%>
	          <a class="nav-link" href="<c:url value="/dashboard/userWiseActiveStudies"/>"> <!-- class="btn btn btn-new"> --> 
	             <i class="fa fa-edit" aria-hidden="true" style="margin-right:5px"></i>Change Study<span class="float-right text-muted text-sm"></span>
	          </a>
<!-- 	          <div class="dropdown-divider"></div> -->
<%-- 	          &nbsp;Current Site : ${siteName} --%>
<%-- 	          <a href="<c:url value="/dashboard/userWiseActiveStudySites"/>" class="btn btn btn-primary"> --%>
<!-- 	             Change<span class="float-right text-muted text-sm"></span> -->
<!-- 	          </a> -->
          </c:if>
          <!-- <div class="dropdown-divider"></div> -->
	          <a class="nav-link"  href='<c:url value="/administration/employee/loginCredentials/changePassword"/>'> <!-- class="btn btn-new" --><i class="fa fa-edit" aria-hidden="true" style="margin-right:5px"></i> Change Password </a>&nbsp;&nbsp;&nbsp;
	          <%-- <a href='<c:url value="/logout"/>' class="btn btn-default"> Sign out </a> --%>
        </div>
      </li>
      
      <li>
      <a  class="nav-link" href='<c:url value="/logout"/>'><i class="fa fa-sign-out" aria-hidden="true" style="margin-top:5px"></i></a></li>
     <%--  <li class="breadcrumb-item"><a href='<c:url value="/dashboard/"/>'>Home</a></li> --%>
    </ul>
