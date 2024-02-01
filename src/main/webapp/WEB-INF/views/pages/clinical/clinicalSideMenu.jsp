<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<body>
	<!-- Main Sidebar Container -->
	<aside class="main-sidebar sidebar-dark-primary elevation-4">
		<!-- Brand Logo -->
		<a href="#" class="brand-link topSideMenu"> <img
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
					
<!-- 					<li class="nav-item"> -->
<%-- 						<a href='<c:url value="/extractData/"/>' --%>
<!-- 						class="nav-link"> <i class="nav-icon fas fa-th"></i> -->
<!-- 							<p> -->
<!-- 								Extract Data<span class="right badge badge-danger">New</span> -->
<!-- 							</p> -->
<!-- 					</a></li> -->
					<li class="nav-item has-treeview">
					    <a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
								CPU Execution <i class="fas fa-angle-left right"></i> <span
									class="badge badge-info right">4</span>
					    </a>
						<ul class="nav nav-treeview">
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicaldosing"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Dosing</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalSampleCollection"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Sample Collection</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalSampleSaperation"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Sample Separation</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalVitalCollection"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Vital Collection</a></li>
					
						</ul>
					</li>
					<c:if test="${userName ne 'CPU Activity' }">
					<li class="nav-item has-treeview">
					    <a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							Clinical Report<i class="fas fa-angle-left right"></i> 
							<span class="badge badge-info right">4</span>
					    </a>
						<ul class="nav nav-treeview">
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicaldosingReport"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Doseing</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalSampleCollectionReport"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Sample Schedule</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalSampleSaperationReport"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Sample Saperation</a></li>
							<li class="nav-item"><a href='<c:url value="/study/clinical/stdClinicalVitalCollectionReport"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i>Vital Schedule</a></li>
						</ul>
					</li>
					</c:if>
				</ul>
			</nav>
			<!-- /.sidebar-menu -->
		</div>
		<!-- /.sidebar -->
	</aside>

</body>
</html>