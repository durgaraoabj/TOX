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
	<aside class="main-sidebar sideMenuBac elevation-4">
		<!-- Brand Logo -->
		<a href="#" class="brand-link topSideMenu"> <img
			src='<c:url value="/static/admintltcss/dist/img/Covide.png"/>'
			alt="AdminLTE Logo" class="brand-image img-circle elevation-3"
			style="opacity: .8"> <span class="brand-text font-weight-light">Covide</span>
		</a>

		<!-- Sidebar -->
		<div class="sidebar">
			<!-- Sidebar user panel (optional) -->
			<div class="user-panel mt-5 pb-4 mb-4 d-flex">
				<div class="image">
					<img
						src='<c:url value="/static/admintltcss/dist/img/person-male.png"/>'
						class="img-circle elevation-2" alt="User Image">
				</div>
				<div class="info">
					<a href="#" class="d-block aCol disabled">${userName}</a>
				</div>
			</div>

			<!-- Sidebar Menu -->
			<nav class="mt-2">
				<ul class="nav nav-pills nav-sidebar flex-column"
					data-widget="treeview" role="menu" data-accordion="false">
						<c:if test="${SMenu15}">
							<li class="nav-item has-treeview">
							    <a href="#" class="nav-link">
									<i class="nav-icon fas fa-copy"></i>
									<p>
										Review <i class="fas fa-angle-left right"></i> <span
											class="badge badge-info right">2</span>
									</p>
							    </a>
								<ul class="nav nav-treeview">
									<c:if test="${SMenuL29}">
										<li class="nav-item">
											<a href='<c:url value="/studyReview/designReview/"/>'  class="nav-link">
												<i class="nav-icon far fa-dot-circle"></i></i><p>Study Design Review</p>
											</a>
										</li>
									</c:if>
									<c:if test="${SMenuL30}">
										<li class="nav-item">
											<a href='<c:url value="/studyExecution/reviewObjservations"/>'  class="nav-link">
												<i class="nav-icon far fa-dot-circle"></i><p>Data Entry Review</p>
											</a>
										</li>
									</c:if>
									<c:if test="${SMenuL41}">
									   <li class="nav-item">
									   		<a href='<c:url value="/studyReview/reviewAccessionAnimalsData"/>' class="nav-link"><i class="nav-icon far fa-dot-circle"></i>
												<p>Accession Animals Data Review</p>
											</a>
										</li>
										<li class="nav-item">
									   		<a href='<c:url value="/studyReview/reviewAccessionAnimalsDataCalender"/>' class="nav-link"><i class="nav-icon far fa-dot-circle"></i>
												<p>Accession Animals Data Review Calender</p>
											</a>
										</li>
										
									</c:if>
									<li class="nav-item">
									   		<a href='<c:url value="/studyReview/randamizationData"/>' class="nav-link"><i class="nav-icon far fa-dot-circle"></i>
												<p>Randamization Review</p>
											</a>
										</li>
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