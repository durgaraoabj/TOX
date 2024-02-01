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
			alt="Covide" class="brand-image img-circle elevation-3"
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
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Study Design <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">4</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL18}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studies"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL19}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studyUpdate"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study Update</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL20}">
									<li class="nav-item"><a
										href='<c:url value="/administration/studyMetaData"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study MetaData</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL21}">
									<li class="nav-item"><a
										href='<c:url value="/administration/asignStudy"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Assign Study</p>
									</a></li>
								</c:if>
							</ul></li>
							
					<c:if test="${SMenu1}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Build Study<i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">9</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<li class="nav-item"><a
									href='<c:url value="/grouping/addIntrumentAndPerameters"/>'
									class="nav-link"> <i class="nav-icon fas fa-th"></i>
										<p>Clinpath Parameter</p>
								</a></li>
								<c:if test="${SMenuL38}">
									<li class="nav-item"><a
										href='<c:url value="/acclimatization/"/>' class="nav-link">
											<i class="nav-icon fas fa-th"></i>
											<p>Acclimatization Configuration</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL1}">
									<li class="nav-item"><a
										href='<c:url value="/expermentalDesign/"/>' class="nav-link">
											<i class="nav-icon fas fa-th"></i>
											<p>Experimental Design</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL2}">
									<li class="nav-item"><a
										href='<c:url value="/expermentalDesign/observationConfig"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Treatment Configuration</p>
									</a></li>
								</c:if>
								

								<c:if test="${SMenuL2}">
									<li class="nav-item"><a
										href='<c:url value="/stuydPlan/studyPlanUpload"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study Plan Upload</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL37}">
									<li class="nav-item"><a
										href='<c:url value="/stuydPlan/viewStudyPlan"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>View Study Plan</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu17}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Accession<i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">1</span>
								</p>
						</a>
							<ul class="nav nav-treeview">

								<c:if test="${SMenuL39}">
									<li class="nav-item"><a
										href='<c:url value="/accession/animalAccession"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Animal Accession</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL40}">
									<li class="nav-item"><a
										href='<c:url value="/accession/viewDataEntryForms"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Animal Accession Data Entry</p>
									</a></li>
								</c:if>
								<li class="nav-item"><a
									href='<c:url value="/cageDesign/"/>' class="nav-link"> <i
										class="nav-icon fas fa-th"></i>
										<p>Animal Cage</p>
								</a></li>
							</ul></li>
					</c:if>
					<c:if test="${SMenu18}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Randomization <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">4</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL42}">
									<li class="nav-item"><a
										href='<c:url value="/animalRadomization/"/>' class="nav-link">
											<i class="nav-icon fas fa-th"></i>
											<p>Generate Randomization</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>
					<c:if test="${SMenu2}">
						<li class="nav-item has-treeview"><a href="#"
							class="nav-link"> <i class="nav-icon fas fa-copy"></i>
								<p>
									Data Capture <i class="fas fa-angle-left right"></i> <span
										class="badge badge-info right">4</span>
								</p>
						</a>
							<ul class="nav nav-treeview">
								<c:if test="${SMenuL3}">
									<li class="nav-item"><a
										href='<c:url value="/studyExecution/"/>' class="nav-link">
											<i class="nav-icon fas fa-th"></i>
											<p>Study Execution</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL46}">
									<li class="nav-item"><a
										href='<c:url value="/sysmex/readInsturmentDataCapture/NA"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>
												Instrument Data<span class="right badge badge-danger">New</span>
											</p>
									</a></li>
								</c:if>
								
<%-- 								<c:if test="${SMenuL47}"> --%>
									<li class="nav-item"><a
										href='<c:url value="/sysmex/insturmentDataExport"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>
												Instrument Data Export <span
													class="right badge badge-danger">New</span>
											</p>
									</a></li>
<%-- 								</c:if> --%>
								<c:if test="${SMenuL9}">
									<li class="nav-item"><a
										href='<c:url value="/studyAudit/studyViewData/"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study Audit</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL10}">
									<li class="nav-item"><a
										href='<c:url value="/studyAudit/crfAuditData/"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>CRF Audit</p>
									</a></li>
								</c:if>
								<c:if test="${SMenuL11}">
									<li class="nav-item"><a
										href='<c:url value="/crfAudit/crfauditObjservations"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>CRF Deviations</p>
									</a></li>
								</c:if>
							</ul></li>
					</c:if>

					<li class="nav-item has-treeview"><a href="#" class="nav-link">
							<i class="nav-icon fas fa-copy"></i>
							<p>
								Operation Log<i class="fas fa-angle-left right"></i> <span
									class="badge badge-info right">4</span>
							</p>
					</a>
						<ul class="nav nav-treeview">
							<c:if test="${SMenu3}">
								<c:if test="${SMenuL8}">
									<li class="nav-item"><a
										href='<c:url value="/studyAudit/serachData/"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study Search</p>
									</a></li>
								</c:if>
							</c:if>
							<c:if test="${SMenu4}">
								<c:if test="${SMenuL4}">
									<li class="nav-item"><a
										href='<c:url value="/discrepancy/"/>' class="nav-link"> <i
											class="nav-icon fas fa-th"></i>
											<p>Discrepancy</p>
									</a></li>
								</c:if>
							</c:if>
							<c:if test="${SMenu5}">
								<c:if test="${SMenuL5}">
									<li class="nav-item"><a
										href='<c:url value="/satroius/readData"/>' class="nav-link">
											<i class="nav-icon fas fa-th"></i>
											<p>
												Sartorius Data<span class="right badge badge-danger">New</span>
											</p>
									</a></li>
								</c:if>
							</c:if>
							<c:if test="${SMenu6}">
								<c:if test="${SMenuL6}">
									<li class="nav-item"><a
										href='<c:url value="/studyExecution/viewExpermantalInfo"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study View</p>
									</a></li>
								</c:if>
							</c:if>

							<%-- <li class="nav-item"><a href='<c:url value="/studyExecution/exportExpermantalInfo"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Export data : 
							</p>
					</a></li> --%>
							<c:if test="${SMenu7}">
								<c:if test="${SMenuL7}">
									<li class="nav-item"><a
										href='<c:url value="/studyExecution/viewExpermantalInCalender"/>'
										class="nav-link"> <i class="nav-icon fas fa-th"></i>
											<p>Study Calender</p>
									</a></li>
								</c:if>
							</c:if>
						</ul></li>

					<%-- <li class="nav-item"><a href='<c:url value="/studyExecution/reviewObjservations"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								Review 
							</p>
					</a></li> --%>

					<%-- <li class="nav-item"><a href='<c:url value="/radwag/dataLocation"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								RADWAG CONFIG<span class="right badge badge-danger">New</span>
							</p>
					</a></li>
					<li class="nav-item"><a href='<c:url value="/radwag/viewInstrumentData"/>'
						class="nav-link"> <i class="nav-icon fas fa-th"></i>
							<p>
								RADWAG DATA<span class="right badge badge-danger">New</span>
							</p>
					</a></li> --%>
				</ul>
			</nav>
			<!-- /.sidebar-menu -->
		</div>
		<!-- /.sidebar -->
	</aside>

</body>
</html>