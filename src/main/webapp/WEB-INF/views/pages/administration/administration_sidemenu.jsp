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
					<c:if test="${SMenu16}">
						<c:if test="${SMenuL34}">
							<li class="nav-item"><a
								href='<c:url value="/roleMenu/showRolesWiseMenu"/>'
								class="nav-link"> <i class="far fa-circle nav-icon"></i>
									<p>Module Access</p>
							</a></li>
						</c:if>
					</c:if>
					<li class="nav-item has-treeview"><a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							<p>
								Instrument Units<i class="fas fa-angle-left right"></i> <span
									class="badge badge-info right">3</span>
							</p>
					</a>
						<ul class="nav nav-treeview">
							<c:if test="${SMenuL43}">
								<li class="nav-item"><a
									href='<c:url value="/grouping/testCodeUnits"/>'
									class="nav-link"> <i class="fa-regular fa-circle-dot"></i>
										<p>Units Creation</p>
								</a></li>
							</c:if>
							<c:if test="${SMenuL44}">
								<li class="nav-item"><a
									href='<c:url value="/grouping/testCodes"/>' class="nav-link">
										<i class="fa-regular fa-circle-dot"></i>
										<p>Instrument Parameters</p>
								</a></li>
							</c:if>
							<c:if test="${SMenuL45}">
								<li class="nav-item"><a
									href='<c:url value="/grouping/testCodesProfiles"/>'
									class="nav-link"> <i class="fa-regular fa-circle-dot"></i>
										<p>Standard Parameter List</p>
								</a></li>
							</c:if>
							<li class="nav-item"><a
								href='<c:url value="/sysmex/selectInstrument"/>'
								class="nav-link"> <i class="fa-regular fa-circle-dot"></i>
									<p>
										Instruments<span class="right badge badge-danger">New</span>
									</p>
							</a></li>
						</ul></li>


					<c:if test="${SMenu8}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Group Observation <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">4</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL12}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/"/>' class="nav-link"> <i
											class="far fa-circle nav-icon"></i>
											<p>Observation Design</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL13}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/mapTableToCrf"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Map DB to Observation</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL14}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/crfFieldCaliculation"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Observation E-Form calculation</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL15}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crfRule/"/>' class="nav-link">
											<i class="far fa-circle nav-icon"></i>
											<p>Observation Rule</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL16}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crfRule/crfDateComparison"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>E-Form Date Comparison</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL17}">
									<li class="nav-item"><a
										href='<c:url value="/administration/crf/observationApprovelLevel"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Observation approval</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>

					<c:if test="${SMenu9}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Study Configuration <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">2</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL18}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studies"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Study</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL19}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studyUpdate"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Study Update</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL20}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studyMetaData"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Study Design</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL21}">
									<li class="nav-item"><a
										href='<c:url value="/administration/asignStudy"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Assign Study</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<li class="nav-item has-treeview"><a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							<p>
								Observation Design<i class="fas fa-angle-left right"></i> <span
									class="badge badge-info right">3</span>
							</p>
					</a>
						<ul class="nav nav-treeview">
							<c:if test="${SMenu10}">
								<c:if test="${SMenuL22}">
									<li class="nav-item"><a
										href='<c:url value="/obvd/obserVasionDesignSchedule"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Observation Design Schedule</p></a></li>
								</c:if>
							</c:if>
							<c:if test="${SMenu11}">
								<c:if test="${SMenuL23}">
									<li class="nav-item"><a
										href='<c:url value="/obvd/datwiseobserVasionDesignSchedule"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Today Observation Design Schedule</p></a></li>
								</c:if>
							</c:if>
							<c:if test="${SMenu12}">
								<c:if test="${SMenuL24}">
									<li class="nav-item"><a
										href='<c:url value="/amandment/"/>' class="nav-link"> <i
											class="far fa-circle nav-icon"></i>
											<p>Amendment</p></a></li>
								</c:if>
							</c:if>
						</ul></li>

					<c:if test="${SMenu13}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									User Configuration <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">3</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL35}">
									<li class="nav-item"><a
										href='<c:url value="/administration/rolesCreationPage"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Create Role</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL25}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>All Users</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL26}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/createEmployee"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Create Login User</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL27}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/updateEmployee"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Update Login User</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL28}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/loginCredentials"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Login Credentials</p>
									</a></li>
								</c:if>
								<%-- 								 <c:if test="${userRole eq 'ADMIN'}"> --%>
								<!-- 									<li class="nav-item"> -->
								<%-- 										<a href='<c:url value="/administration/asignStudy"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i> --%>
								<!-- 											Assign Study</a></li>	 -->
								<!-- 									<li class="nav-item"> -->
								<%-- 										<a href='<c:url value="/administration/asignSite"/>' class="nav-link"> <i class="far fa-circle nav-icon"></i> --%>
								<!-- 											Assign Site</a></li>	 -->
								<%-- 								</c:if> --%>
							</ul></li>
					</c:if>
					<!-- Add Link In DB After Task -->
					<%-- <c:if test="${SMenu13}"> --%>
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Sponsor <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">3</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<%-- <c:if test="${SMenuL35}"> --%>
									<li class="nav-item"><a
										href='<c:url value="/administration/createSponsorMaster"/>'
										class="nav-link"> <i class="far fa-circle nav-icon"></i>
											<p>Sponsor Master</p>
									</a></li>
								<%-- </c:if> --%>
								</ul>
								</li>
					<%-- </c:if> --%>
					
					<%-- 					<c:if test="${SMenu14}"> --%>
					<!-- 						<li class="nav-item has-treeview"><a href="#" -->
					<!-- 							class="nav-link"> <i class="nav-icon fas fa-copy"></i> -->
					<!-- 								<p> -->
					<!-- 									Instrument Data<i class="fas fa-angle-left right"></i> <span -->
					<!-- 										class="badge badge-info right">3</span> -->
					<!-- 								</p> -->
					<!-- 						</a> -->
					<!-- 							<ul class="nav nav-treeview"> -->
					<%-- 								<c:if test="${SMenuL31}"> --%>
					<!-- 									<li class="nav-item"><a -->
					<%-- 										href='<c:url value="/instrument/coagulometerData"/>' --%>
					<!-- 										class="nav-link"> <i class="far fa-circle nav-icon"></i> -->
					<!-- 											<p>Coagulometer</p> -->
					<!-- 									</a></li> -->
					<%-- 								</c:if> --%>
					<%-- 								<c:if test="${SMenuL32}"> --%>
					<!-- 									<li class="nav-item"><a -->
					<%-- 										href='<c:url value="/instrument/sartoriusData"/>' --%>
					<!-- 										class="nav-link"> <i class="far fa-circle nav-icon"></i> -->
					<!-- 											<p>Sartorius</p> -->
					<!-- 									</a></li> -->
					<%-- 								</c:if> --%>
					<%-- 								<c:if test="${SMenuL33}"> --%>
					<!-- 									<li class="nav-item"><a -->
					<%-- 										href='<c:url value="/instrument/sysmaxData"/>' --%>
					<!-- 										class="nav-link"> <i class="far fa-circle nav-icon"></i> -->
					<!-- 											<p>SysMax</p> -->
					<!-- 									</a></li> -->
					<%-- 								</c:if> --%>
					<!-- 							</ul></li> -->
					<%-- 					</c:if> --%>


<%-- 					<c:if test="${userRole ne 'Data Specialist' }"> --%>
<%-- 						<li class="nav-item"><a href='<c:url value="/extractData/"/>' --%>
<!-- 							class="nav-link"> <i class="nav-icon fas fa-th"></i> -->
<!-- 								<p> -->
<!-- 									Extract Data<span class="right badge badge-danger">New</span> -->
<!-- 								</p> -->
<!-- 						</a></li> -->
<%-- 					</c:if> --%>
				</ul>
			</nav>
			<!-- /.sidebar-menu -->
		</div>
		<!-- /.sidebar -->
	</aside>

</body>
</html>