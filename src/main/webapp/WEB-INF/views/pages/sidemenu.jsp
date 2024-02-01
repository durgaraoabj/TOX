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
		<div class="sidebar sidebarback">
			<!-- Sidebar user panel (optional) -->
			<div class="user-panel mt-3 pb-3 mb-3 d-flex">
				<div class="image">
					<img
						src='<c:url value="/static/admintltcss/dist/img/person-male.png"/>'>
				</div>
				<div class="info">
					<a href="#" class="d-block aCol disabled"><B>${userName}</B></a>
				</div>
			</div>

			<!-- Sidebar Menu -->
			<nav class="mt-2">
				<ul class="nav nav-pills nav-sidebar flex-column"
					data-widget="treeview" role="menu" data-accordion="false">
					<c:if test="${SMenu_Masters or userName eq 'superadmin'}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Masters<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<li class="nav-item"><a
									href='<c:url value="/administration/clinicalCodes"/>'
									class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
										<p>Clinical Codes</p>
								</a></li>
								<c:if test="${SMenuL_Units}">
									<li class="nav-item"><a
										href='<c:url value="/grouping/testCodeUnits"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Units</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_ClinpathParameters}">
									<li class="nav-item"><a
										href='<c:url value="/grouping/testCodes"/>' class="nav-link">
											<i class="nav-icon far fa-dot-circle"></i>
											<p>Clinpath Parameters</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_StandardParametersList}">
									<li class="nav-item"><a
										href='<c:url value="/grouping/testCodesProfiles"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Standard Parameters List</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Sponsors}">
									<li class="nav-item"><a
										href='<c:url value="/administration/createSponsorMaster"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Sponsors</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Observations}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/"/>' class="nav-link"> <i
											class="nav-icon far fa-dot-circle"></i>
											<p>Observations</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_MapDBToObservationField}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/mapTableToCrf"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Map DB to Observation Field</p>
									</a></li>
								</c:if>
								<li class="nav-item"><a
									href='<c:url value="/admini/crf/mapTableToCrf"/>'
									class="nav-link"> <i class="far fa-circle nav-icon"></i>
										<p>Map DB to Observation</p>
								</a></li>
								<c:if test="${SMenuL_XMLFormula}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crf/crfFieldCaliculation"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>XML Formula</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Rules}">
									<li class="nav-item"><a
										href='<c:url value="/admini/crfRule/"/>' class="nav-link">
											<i class="nav-icon far fa-dot-circle"></i>
											<p>Rules</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_UserRole}">
									<li class="nav-item"><a
										href='<c:url value="/administration/rolesCreationPage"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>User Role</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_UserCreation}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/createEmployee"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>User Creation</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_UsersList}">
									<li class="nav-item"><a
										href='<c:url value="/administration/employee/"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>User's List</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_ModuleAccess or userName eq 'superadmin'}">
									<li class="nav-item"><a
										href='<c:url value="/roleMenu/showRolesWiseMenu"/>'
										class="nav-link"><i class="nav-icon far fa-dot-circle"></i>
											<p>Module Access</p> </a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_Study}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Study<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL_CreateStudy}">
									<li class="nav-item"><a
										href='<c:url value="/administration/createStudy"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Create Study</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_StudysList}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studies"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Study's List</p>
									</a></li>
								</c:if>
								<li class="nav-item"><a
									href='<c:url value="/administration/asignStudy"/>'
									class="nav-link"> <i class="nav-icon fas fa-th"></i>
										<p>Assign Study</p>
								</a></li>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_StudyDesign}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Study Design<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<!-- 							<li class="nav-item"><a -->
								<%-- 										href='<c:url value="/administration/studyMetaData"/>' --%>
								<!-- 										class="nav-link"> <i class="nav-icon fas fa-th"></i> -->
								<!-- 											<p>Study MetaData</p> -->
								<!-- 									</a></li>	 -->
								<c:if test="${SMenuL_ExperimentalDesign}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studyDesign"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Experimental Design</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_ClinicalPathParameters}">
									<li class="nav-item"><a
										href='<c:url value="/grouping/addIntrumentAndPerameters"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Clinical Path Parameters</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_AssignStudy}">
									<li class="nav-item"><a
										href='<c:url value="/administration/asignStudy"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Assign Study</p>
									</a></li>
								</c:if>

								<c:if test="${SMenuL_StudyPlan}">
									<li class="nav-item"><a
										href='<c:url value="/stuydPlan/studyPlanUpload"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Study Plan</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Amendment}">
									<li class="nav-item"><a
										href='<c:url value="/amandment/"/>' class="nav-link"> <i
											class="nav-icon far fa-dot-circle"></i>
											<p>Amendment</p></a></li>
								</c:if>
								<c:if test="${SMenuL_StudyView}">
									<li class="nav-item"><a
										href='<c:url value="/amandment/"/>' class="nav-link"> <i
											class="nav-icon far fa-dot-circle"></i>
											<p>Study View</p></a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_Accession}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Accession<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL_AccessionID}">
									<li class="nav-item"><a
										href='<c:url value="/accession/animalAccession"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Accession ID</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_UnscheduledObservations}">
									<li class="nav-item"><a
										href='<c:url value="/accession/viewDataEntryForms"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Unscheduled Observations</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Randomization}">
									<li class="nav-item"><a
										href='<c:url value="/animalRadomization/"/>' class="nav-link">
											<i class="nav-icon far fa-dot-circle"></i>
											<p>Randomization</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_Observations}">
<%-- 						<c:if --%>
<%-- 							test="${activeStudy.studyStatus eq 'Reviewed' activeStudy.studyStatus eq 'In-Progress' }"> --%>
							<li class="nav-item"><a
								href='<c:url value="/studyExecution/viewExpermantalInCalender"/>'
								class="nav-link"> <i class="nav-icon fas fa-edit"></i>
									<p>Observations</p>
							</a></li>
<%-- 						</c:if> --%>
					</c:if>
					<li class="nav-item has-treeview"><a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							<p>
								Acclimatization Data<i class="fas fa-angle-left right"></i>
							</p>
					</a>
					<li class="nav-item"><a
						href='<c:url value="/sysmex/insturmentDataExport"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Instrument Data Export <span class="right badge badge-danger">New</span>
							</p>
					</a></li>
					<li class="nav-item"><a
						href='<c:url value="/dataExport/"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Data Export <span class="right badge badge-danger">New</span>
							</p>
					</a></li>
					<ul class="nav nav-treeview">
						<li class="nav-item"><a
							href='<c:url value="/instrumentReport/Stago"/>' class="nav-link">
								<i class="nav-icon far fa-dot-circle"></i>
								<p>Stago</p>
						</a></li>
						<li class="nav-item"><a
							href='<c:url value="/instrumentReport/Sysmex"/>' class="nav-link">
								<i class="nav-icon far fa-dot-circle"></i>
								<p>Sysmex</p>
						</a></li>
						<li class="nav-item"><a
							href='<c:url value="/instrumentReport/Vitros"/>' class="nav-link">
								<i class="nav-icon far fa-dot-circle"></i>
								<p>Vitros</p>
						</a></li>
					</ul>
					</li>

					<c:if test="${SMenu_Treatment}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Treatment<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL_UnscheduledObservations}">
									<li class="nav-item"><a
										href='<c:url value="/studyExecution/"/>' class="nav-link">
											<i class="nav-icon far fa-dot-circle"></i>
											<p>Unscheduled Observations</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_Review}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Review<i class="fas fa-angle-left right"></i>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL_StudyDesign}">
									<li class="nav-item"><a
										href='<c:url value="/studyReview/designReview/"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Study Design</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_AccessionData}">
									<li class="nav-item"><a
										href='<c:url value="/studyReview/reviewAccessionAnimalsData"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Accession Data</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Randomization}">
									<li class="nav-item"><a
										href='<c:url value="/studyReview/randamizationData"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Randomization</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_TreatmentData}">
									<li class="nav-item"><a
										href='<c:url value="/studyExecution/reviewObjservations"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Treatment Data</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL_Observations}">
									<li class="nav-item"><a
										href='<c:url value="/studyReview/reviewAccessionAnimalsDataCalender"/>'
										class="nav-link"> <i class="nav-icon far fa-dot-circle"></i>
											<p>Observations</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu_Discrepancy}">
						<li class="nav-item"><a href='<c:url value="/discrepancy/"/>'
							class="nav-link"> <i class="nav-icon fas fa-edit"></i>
								<p>Discrepancy</p>
						</a></li>
					</c:if>
					<li class="nav-item"><a
						href='<c:url value="/sysmex/readInsturmentDataCapture/NA"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Instrument Data<span class="right badge badge-danger">New</span>
							</p>
					</a></li>






				</ul>
			</nav>
			<!-- /.sidebar-menu -->
		</div>
		<!-- /.sidebar -->
	</aside>

</body>
</html>