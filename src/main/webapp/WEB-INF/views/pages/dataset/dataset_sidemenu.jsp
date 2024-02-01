<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<body>
	<!-- Main Sidebar Container -->
	<aside class="main-sidebar sidebar-dark-primary elevation-4">
		<!-- Brand Logo -->
		<a href="#" class="brand-link"> <img
			src='<c:url value="/static/admintltcss/dist/img/Covide.png"/>'
			alt="AdminLTE Logo" class="brand-image img-circle elevation-3"
			style="opacity: .8"> <span class="brand-text font-weight-light">Covide</span>
		</a>

		<!-- Sidebar -->
		<div class="sidebar">
			<!-- Sidebar user panel (optional) -->
			<div class="user-panel mt-3 pb-3 mb-3 d-flex">
				<div class="image">
					<img
						src='<c:url value="/static/admintltcss/dist/img/person-male.png"/>'
						class="img-circle elevation-2" alt="User Image">
				</div>
				<div class="info">
					<a href="#" class="d-block">${userName}</a>
				</div>
			</div>

			<!-- Sidebar Menu -->
			<nav class="mt-2">
				<ul class="nav nav-pills nav-sidebar flex-column"
					data-widget="treeview" role="menu" data-accordion="false">
					<!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
					
					<%-- <li class="nav-item">
						<a href='<c:url value="/extractData/"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Extract Data<span class="right badge badge-danger">New</span>
							</p>
					</a></li>
					<li class="nav-item has-treeview">
					    <a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							<p>
								Study Configuration <i class="fas fa-angle-left right"></i> <span
									class="badge badge-info right">2</span>
							</p>
					    </a>
						<ul class="nav nav-treeview">
							<li class="nav-item">
							<a href='<c:url value="/administration/studies"/>' class="nav-link"> 
									<i class="far fa-circle nav-icon"></i>
									<p>Study</p>
							</a></li>
							<li class="nav-item">
							<a href='<c:url value="/administration/asignStudy"/>'
								class="nav-link"> <i class="far fa-circle nav-icon"></i>
									<p>Asign Study</p>
							</a></li>
						</ul>
					</li> --%>
				</ul>
			</nav>
			<!-- /.sidebar-menu -->
		</div>
		<!-- /.sidebar -->
	</aside>

</body>
</html>