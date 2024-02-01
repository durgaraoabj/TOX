package com.springmvc.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.covide.crf.dao.CrfDAO;
import com.covide.crf.dto.Crf;
import com.covide.enums.DepartmentMasterCodes;
import com.covide.enums.RoleMasterRoles;
import com.covide.enums.SideMenuCodes;
import com.covide.enums.SideMenuLinksCodes;
import com.covide.enums.StatusMasterCodes;
import com.springmvc.audittrail.AuditToken;
import com.springmvc.dao.DepartmentDao;
import com.springmvc.dao.EmployeeDao;
import com.springmvc.dao.InstrumentDao;
import com.springmvc.dao.RoleMasterDao;
import com.springmvc.dao.RolesWiseMenusDao;
import com.springmvc.dao.StatusDao;
import com.springmvc.dao.UserDao;
import com.springmvc.model.ApplicationSideMenuLinks;
import com.springmvc.model.ApplictionSideMenus;
import com.springmvc.model.DepartmentMaster;
import com.springmvc.model.EmpJobMaster;
import com.springmvc.model.EmployeeMaster;
import com.springmvc.model.InstrumentIpAddress;
import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationRole;
import com.springmvc.model.RoleMaster;
import com.springmvc.model.RolesWiseModules;
import com.springmvc.model.Species;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudySite;
import com.springmvc.model.UserWiseStudiesAsignMaster;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewStages;
import com.springmvc.model.dummy.LoginFieldDummyForm;
import com.springmvc.service.StudyService;
import com.springmvc.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	DepartmentDao departmentDao;
	@Autowired
	private UserDao userdao;

	@Autowired
	private StudyService studyService;

	@Autowired
	RoleMasterDao roleMasterDao;
	@Autowired
	StatusDao statusDao;
	@Autowired
	EmployeeDao employeeDao;
	@Autowired
	RolesWiseMenusDao rolesWiseMenusDao;
	@Autowired
	InstrumentDao instrumentDao;
	static boolean freshDBConfigure = true;
	@Autowired
	CrfDAO crfDao;

	@Override
	public LoginUsers findByActiveUsername(String username) {
		// TODO Auto-generated method stub
		checkAndImportFreshDB();
		LoginUsers user = userdao.findByActiveusername(username);
		return user;
	}

	/*
	 * Check mandatory data avilable or not. Create default data in required tables
	 */
	private void checkAndImportFreshDB() {
		// TODO Auto-generated method stub
		if (freshDBConfigure) {
			staticDataImport();
			speciesDataImport();
			applictionSideMenusImport();
			StatusMaster sd = statusMasterImport();
			List<RoleMaster> roles = rolemasterImport();
			departmentMasterImport();
			createSuperadmin();
			defaultStudy(sd);
			loadDefaultEformForInsturment(roles);
			instrumentIpAddressDataImport();
			workFlowDataTable();

			freshDBConfigure = false;
		}

	}

	private void workFlowDataTable() {

		StatusMaster activeStatus = statusDao.statusMaster("ACTIVE");
//			instrumentDao.saveInstrumentIpAddressData(new InstrumentIpAddress("SYSMEX", "Hematology", "",
//					"10.1.23.85", 8002, st, true, 1));

		List<WorkFlow> wb = new ArrayList<>();
		wb.add(statusDao.saveWorkFlow(new WorkFlow("ACCESSION", "accession", activeStatus)));
		wb.add(statusDao.saveWorkFlow(new WorkFlow("OBSERCVATION", "obsercvation", activeStatus)));
		wb.add(statusDao.saveWorkFlow(new WorkFlow("RANDAMIZATION", "randamization", activeStatus)));

		RoleMaster qa = userdao.roleMaster("QA"), superadmin = userdao.roleMaster("SUPERADMIN"),
				sd = userdao.roleMaster("SD"), lt = userdao.roleMaster("LT"), manager = userdao.roleMaster("MANAGER"),
				tfm = userdao.roleMaster("TFM"), admin = userdao.roleMaster("ADMIN"), sp = userdao.roleMaster("SP");
		for (WorkFlow w : wb) {
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(superadmin, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(sd, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(superadmin, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(lt, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(manager, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(tfm, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(admin, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(sp, qa, w, "Sent To Review", true));
			statusDao.saveworkFlowReviewStages(new WorkFlowReviewStages(qa, null, w, "Review", true));
		}
	}

	private void instrumentIpAddressDataImport() {
		// TODO Auto-generated method stub
		StatusMaster st = statusDao.statusMaster("ACTIVE");
		instrumentDao.saveInstrumentIpAddressData(
				new InstrumentIpAddress("SYSMEX", "Hematology", "", "10.1.23.85", 8002, st, true, 1));
		instrumentDao.saveInstrumentIpAddressData(
				new InstrumentIpAddress("VITROS", "Clin.Chem", "COM3", "", 8080, st, true, 2));
		instrumentDao.saveInstrumentIpAddressData(
				new InstrumentIpAddress("STAGO", "Coagulation", "COM3", "", 8080, st, true, 3));
	}

	/*
	 * 
	 */
	private void loadDefaultEformForInsturment(List<RoleMaster> roles) {
		// TODO Auto-generated method stub
		Crf crf = crfDao.saveIsntrumebtCrf(new Crf("Clin Path", "Clinpath parameters", "CP", "Clinpath parameters", "Both"));
		List<ObservationRole> obRoles = new ArrayList<>(); 
		for(RoleMaster role : roles) {
			obRoles.add(new ObservationRole(crf, role, "System")); 
		}
		crfDao.saveObservationRoles(obRoles);
	}

	/*
	 * Create species with code by Static witch are included in pathalozy insturment
	 */
	private void speciesDataImport() {
		// TODO Auto-generated method stub
		statusDao.saveSpecies(new Species("001", "Rat"));
		statusDao.saveSpecies(new Species("002", "Dog"));
	}

	/*
	 * Static data import It is the details of: Weight capture form in Accession and
	 * weight field units of measure
	 * 
	 * 
	 */
	private void staticDataImport() {
		statusDao.saveStaticData(new StaticData("ACTITYORCRF",
				"Appendix 2: Motor Activity Data of Individual Animal  ANNEXURE 4", "ACTIVITYWEIGHTFROM"));
		statusDao.saveStaticData(new StaticData("ACTITYORCRF", "total", "TOTAL"));
		statusDao.saveStaticData(new StaticData("ACTITYORCRF", "ambulatory", "AMBULATORY"));
		statusDao.saveStaticData(new StaticData("WEIGHTUNITS", "Kilogram", "KG"));
		statusDao.saveStaticData(new StaticData("WEIGHTUNITS", "Gram", "GR"));

	}

	/*
	 * Import Application Side Menus
	 */
	private void applictionSideMenusImport() {
		// TODO Auto-generated method stub
		Map<String, String> allStatusMasters = allSideMenuLinks();
		List<ApplictionSideMenus> sideMenus = rolesWiseMenusDao.allApplicationSideMenus();
		Set<String> codes = new HashSet<>();
		sideMenus.forEach((s) -> {
			codes.add(s.getCode());
		});
		for (Map.Entry<String, String> mp : allStatusMasters.entrySet()) {
			if (!codes.contains(mp.getKey())) {
				rolesWiseMenusDao.saveApplicationSideMenu(new ApplictionSideMenus(mp.getValue(), mp.getKey()));
			}
		}
		applictionSideMenusLinksImport();
	}

	/*
	 * Import Application Side Menus Links
	 */
	private void applictionSideMenusLinksImport() {
		// Master
		importSideMenuLinksImport(SideMenuCodes.Masters.toString());
		importSideMenuLinksImport(SideMenuCodes.Study.toString());
		importSideMenuLinksImport(SideMenuCodes.StudyDesign.toString());
		importSideMenuLinksImport(SideMenuCodes.Accession.toString());
		importSideMenuLinksImport(SideMenuCodes.Observations.toString());
		importSideMenuLinksImport(SideMenuCodes.Treatment.toString());
		importSideMenuLinksImport(SideMenuCodes.Review.toString());
		importSideMenuLinksImport(SideMenuCodes.Discrepancy.toString());
	}

	/*
	 * Import Application Side Menus Links
	 */
	private void importSideMenuLinksImport(String sideMenuCode) {
		// TODO Auto-generated method stub
		Map<String, String> sideMenuCodes = sideMenuCodes(sideMenuCode);
		ApplictionSideMenus sideMenu = rolesWiseMenusDao.applictionSideMenusByCode(sideMenuCode);
		List<ApplicationSideMenuLinks> sideMenuLinks = rolesWiseMenusDao
				.allApplicationSideMenuLinksOfSideMenu(sideMenu.getId());
		Set<String> codes = new HashSet<>();
		for (ApplicationSideMenuLinks sl : sideMenuLinks) {
			codes.add(sl.getCode());
		}
		for (Map.Entry<String, String> m : sideMenuCodes.entrySet()) {
			if (!codes.contains(m.getKey())) {
				if (m.getKey().equals(SideMenuCodes.Observations.toString())
						|| m.getKey().equals(SideMenuCodes.Discrepancy.toString()))
					rolesWiseMenusDao.saveApplicationSideMenuLink(
							new ApplicationSideMenuLinks(m.getValue(), sideMenu, true, 'T', m.getKey()));
				else
					rolesWiseMenusDao.saveApplicationSideMenuLink(
							new ApplicationSideMenuLinks(m.getValue(), sideMenu, false, 'T', m.getKey()));
			}
		}
	}

	/*
	 * Get Application Side Menu Links
	 */
	private Map<String, String> sideMenuCodes(String key) {
		// TODO Auto-generated method stub
		Map<String, String> sideMenuCodes = new HashMap<>();

		switch (key) {
		case "Masters":
			sideMenuCodes.put(SideMenuLinksCodes.Units.toString(), "Units");
			sideMenuCodes.put(SideMenuLinksCodes.ClinpathParameters.toString(), "Clinpath Parameters");
			sideMenuCodes.put(SideMenuLinksCodes.StandardParametersList.toString(), "Standard Parameters List");
			sideMenuCodes.put(SideMenuLinksCodes.Sponsors.toString(), "Sponsors");
			sideMenuCodes.put(SideMenuLinksCodes.Observations.toString(), "Observations");
			sideMenuCodes.put(SideMenuLinksCodes.MapDBToObservationField.toString(), "Map DB To Observation Field");
			sideMenuCodes.put(SideMenuLinksCodes.XMLFormula.toString(), "XML Formula");
			sideMenuCodes.put(SideMenuLinksCodes.Rules.toString(), "Rules");
			sideMenuCodes.put(SideMenuLinksCodes.UserRole.toString(), "User Role");
			sideMenuCodes.put(SideMenuLinksCodes.UserCreation.toString(), "User Creation");
			sideMenuCodes.put(SideMenuLinksCodes.UsersList.toString(), "Users List");
			sideMenuCodes.put(SideMenuLinksCodes.ModuleAccess.toString(), "Module Access");
			break;
		case "Study":
			sideMenuCodes.put(SideMenuLinksCodes.CreateStudy.toString(), "Create Study");
			sideMenuCodes.put(SideMenuLinksCodes.StudysList.toString(), "Study's List");
			break;
		case "StudyDesign":
			sideMenuCodes.put(SideMenuLinksCodes.ExperimentalDesign.toString(), "Experimental Design");
			sideMenuCodes.put(SideMenuLinksCodes.ClinicalPathParameters.toString(), "Clinical Path Parameters");
			sideMenuCodes.put(SideMenuLinksCodes.StudyPlan.toString(), "Study Plan");
			sideMenuCodes.put(SideMenuLinksCodes.Amendment.toString(), "Amendment");
			sideMenuCodes.put(SideMenuLinksCodes.StudyView.toString(), "Study View");
			break;
		case "Accession":
			sideMenuCodes.put(SideMenuLinksCodes.AccessionID.toString(), "Accession ID");
			sideMenuCodes.put(SideMenuLinksCodes.UnscheduledObservations.toString(), "Unscheduled Observations");
			sideMenuCodes.put(SideMenuLinksCodes.Randomization.toString(), "Randomization");
			break;
		case "Observations":
			sideMenuCodes.put(SideMenuLinksCodes.Observations.toString(), "Observations");
			break;
		case "Treatment":
			sideMenuCodes.put(SideMenuLinksCodes.UnscheduledObservations.toString(), "Unscheduled Observations");
			break;
		case "Review":
			sideMenuCodes.put(SideMenuLinksCodes.StudyDesign.toString(), "Study Design");
			sideMenuCodes.put(SideMenuLinksCodes.AccessionData.toString(), "Accession Data");
			sideMenuCodes.put(SideMenuLinksCodes.Randomization.toString(), "Randomization");
			sideMenuCodes.put(SideMenuLinksCodes.TreatmentData.toString(), "Treatment Data");
			sideMenuCodes.put(SideMenuLinksCodes.Observations.toString(), "Observations");
			break;
		case "Discrepancy":
			sideMenuCodes.put(SideMenuLinksCodes.Discrepancy.toString(), "Discrepancy");
			break;
		default:
			break;
		}
		return sideMenuCodes;
	}

	/*
	 * get Application Side Menu
	 */
	private Map<String, String> allSideMenuLinks() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(SideMenuCodes.Masters.toString(), "Masters");
		map.put(SideMenuCodes.Study.toString(), "Study");
		map.put(SideMenuCodes.StudyDesign.toString(), "Study Design");
		map.put(SideMenuCodes.Accession.toString(), "Accession");
		map.put(SideMenuCodes.Observations.toString(), "Observations");
		map.put(SideMenuCodes.Treatment.toString(), "Treatment");
		map.put(SideMenuCodes.Review.toString(), "Review");
		map.put(SideMenuCodes.Discrepancy.toString(), "Discrepancy");
		return map;
	}

	/*
	 * StatusMaster data import
	 */
	private StatusMaster statusMasterImport() {
		// TODO Auto-generated method stub
		Map<String, String> allStatusMasters = allStatusMasters();
		List<StatusMaster> statusMasters = statusDao.fiendAll();
		Set<String> stIds = new HashSet<>();
		StatusMaster sd = null;
		for (StatusMaster st : statusMasters) {
			stIds.add(st.getStatusCode());
			if (st.getStatusCode().equals(StatusMasterCodes.SD.toString())) {
				sd = st;
			}
		}

		for (Map.Entry<String, String> m : allStatusMasters.entrySet()) {
			if (!stIds.contains(m.getKey())) {
				StatusMaster st = statusDao.saveStatusMaster(new StatusMaster(m.getKey(), m.getValue()));
				if (m.getKey().equals(StatusMasterCodes.SD.toString())) {
					sd = st;
				}
			}
		}
		return sd;
	}

	/*
	 * get all StatusMasters
	 */
	private Map<String, String> allStatusMasters() {
		Map<String, String> status = new HashMap<String, String>();
		status.put(StatusMasterCodes.IN.toString(), "Initiate");
		status.put(StatusMasterCodes.SD.toString(), "Design");
		status.put(StatusMasterCodes.SI.toString(), "Inprogress");
		status.put(StatusMasterCodes.SF.toString(), "Frozen");
		status.put(StatusMasterCodes.SL.toString(), "Locked");
		status.put(StatusMasterCodes.SC.toString(), "Cancel");
		status.put(StatusMasterCodes.EDOROD.toString(), "Expermental Design (or) Obeservation Design");
		status.put(StatusMasterCodes.DINR.toString(), "Design In Review");
		status.put(StatusMasterCodes.RASD.toString(), "Reviewer Accepted Study Design ");
		status.put(StatusMasterCodes.RRSD.toString(), "Reviewer Rjected Study Design");
		status.put(StatusMasterCodes.DIN.toString(), "Design InProgress");
		status.put(StatusMasterCodes.DU.toString(), "Design Update");
		status.put(StatusMasterCodes.SU.toString(), "Study Update");
		status.put(StatusMasterCodes.ACTIVE.toString(), "Active");
		status.put(StatusMasterCodes.INACTIVE.toString(), "In-Active");
		status.put(StatusMasterCodes.APPROVED.toString(), "APPROVED");
		status.put(StatusMasterCodes.STUDINFORMATION.toString(), "Study Information Saved Successfully");

		status.put(StatusMasterCodes.INREVIEW.toString(), "In-Review");
		status.put(StatusMasterCodes.REVIEWED.toString(), "REVIEWED");
		status.put(StatusMasterCodes.REJECTED.toString(), "REJECTED");
		status.put(StatusMasterCodes.DATAENTRY.toString(), "Data Entry");
		status.put(StatusMasterCodes.SENDTORIVEW.toString(), "Send To Rivew");
		status.put(StatusMasterCodes.REJCTED.toString(), "Rejcted");
		return status;
	}

	/*
	 * To create default study
	 */
	private void defaultStudy(StatusMaster sd) {
		StudyMaster study = studyService.findByStudyNo("defaultStudy");
		if (study == null) {
			study = new StudyMaster();
			study.setStudyDesc("DefaultStudy");
			study.setStudyNo("defaultStudy");
			study.setStudyType("Single");
			study.setSubjects(10);
			study.setGlobalStatus(sd);
			study.setStatus(sd);
			studyService.saveStudyMaster(study, "superadmin");
		}
	}

	/*
	 * Check and Save Super admin user with employee details
	 */
	private void createSuperadmin() {
		List<LoginUsers> users = findAllUsers();
		boolean fg = true;
		for (LoginUsers u : users) {
			if (u.getUsername().equals("superadmin")) {
				fg = false;
			}
		}
		if (fg) {
			RoleMaster role = roleMasterDao.roleIdByRoleName(RoleMasterRoles.SUPERADMIN.toString());
			DepartmentMaster dept = departmentDao.findById(1l);
			EmpJobMaster jobMaster = new EmpJobMaster();
			jobMaster.setDept(dept);
			EmployeeMaster employeeMaster = new EmployeeMaster();
			employeeMaster.setEmpId("superadmin");
//			employeeMaster.setAddressMaster(new AddressMaster());
			employeeMaster.setRole(role);
//			employeeMaster.setJobMaster(jobMaster);
//			employeeMaster.setEmerContMaster(new EmergencyContMaster());

			employeeDao.saveEmpDetails(employeeMaster, role.getId());
		}
	}

	/*
	 * Check and Save required Departments
	 */
	private void departmentMasterImport() {
		Map<String, String> roles = alltheDepartmentMaster();
		List<DepartmentMaster> currentDepts = departmentDao.findActiveAllDepts();
		List<String> roleCodes = new ArrayList<>();
		currentDepts.stream().forEach((currentDept) -> {
			roleCodes.add(currentDept.getDeptCode());
		});
		roles.forEach((key, value) -> {
			if (!roleCodes.contains(key)) {
				departmentDao.saveDepartmentMaster(new DepartmentMaster(key, value, "HYD"));
			}
		});
	}

	/*
	 * get all Department codes
	 */
	private Map<String, String> alltheDepartmentMaster() {
		Map<String, String> depts = new HashMap<String, String>();
		depts.put(DepartmentMasterCodes.hmt.toString(), "hmt");
		return depts;
	}

	/*
	 * Check and Save required roles
	 */
	private List<RoleMaster> rolemasterImport() {
		List<RoleMaster> all = new ArrayList<>();
		// TODO Auto-generated method stub
		Map<String, String> roles = alltheRoles();
		List<RoleMaster> currentRoles = roleMasterDao.findAll();
		all.addAll(currentRoles);
		List<String> roleCodes = new ArrayList<>();
		currentRoles.stream().forEach((currentRole) -> {
			roleCodes.add(currentRole.getRole());
		});
		roles.forEach((key, value) -> {
			if (!roleCodes.contains(key)) {
				all.add(roleMasterDao.saveRoleMaster(new RoleMaster(key, value, 'T', true)));
			}
		});
		return all;
	}

	/*
	 * get all Role codes
	 */
	private Map<String, String> alltheRoles() {
		Map<String, String> roles = new HashMap<String, String>();
		roles.put(RoleMasterRoles.SUPERADMIN.toString(), "SuperAdmin");
		roles.put(RoleMasterRoles.ADMIN.toString(), "Administration");
		roles.put(RoleMasterRoles.TFM.toString(), "Test Facility Manager");
		roles.put(RoleMasterRoles.SD.toString(), "Study Director");
		roles.put(RoleMasterRoles.LT.toString(), "Lab Tech");
		roles.put(RoleMasterRoles.SP.toString(), "Study Personnel");
		roles.put(RoleMasterRoles.QA.toString(), "Quality Assurance");
		roles.put(RoleMasterRoles.MANAGER.toString(), "Manager/Group Leader");
		return roles;
	}

	@Override
	public LoginUsers findByUsername(String username) {
		// TODO Auto-generated method stub
		LoginUsers user = userdao.findByusername(username);
		return user;
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public LoginUsers findById(Long id) {
		return userdao.findById(id);
	}

	@Override
	public List<LoginUsers> findAllUsers() {
		return userdao.findAllUsers();
	}

	@Override
	public boolean isUserSSOUnique(Long id, String username) {
		LoginUsers user = findByUsername(username);
		return (user == null || ((id != null) && (user.getId() == id)));
	}

	@Override
	@AuditToken
	public StudyMaster changeStudy(Long studyId, String username) {
		// TODO Auto-generated method stub
		try {
			LoginUsers user = findByUsername(username);
			StudyMaster study = studyService.findByStudyId(studyId);
			user.setActiveStudy(study);
			user.setActiveSite(null);
			userdao.updateStudy(user);
			return study;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@AuditToken
	public StudySite changeSite(Long siteId, String username) {
		// TODO Auto-generated method stub
		try {
			LoginUsers user = findByUsername(username);
			StudySite site = studyService.studySiteId(siteId);
			user.setActiveSite(site);
			userdao.updateStudy(user);
			return site;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@AuditToken
	public boolean generateLoginInfo(LoginFieldDummyForm loginFieldDummyForm, String username) {
		// TODO Auto-generated method stub
		try {
			StudyMaster sm = studyService.findByStudyNo("default_study");
			LoginUsers loginUsers = new LoginUsers();
			loginUsers.setActiveStudy(sm);
			loginUsers.setCreatedBy(username);
			loginUsers.setCreatedOn(new Date());
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, 1); // to get previous year add -1
			Date nextYear = cal.getTime();
			loginUsers.setAccountexprie(nextYear);
			loginUsers.setAccountNotDisable(true);
			loginUsers.setAccountNotLock(true);
			String fullName = "";
			fullName = loginFieldDummyForm.getFirstName() != null ? loginFieldDummyForm.getFirstName() : fullName;
			fullName = loginFieldDummyForm.getMiddleName() != null
					? fullName + " " + loginFieldDummyForm.getMiddleName()
					: fullName;
			fullName = loginFieldDummyForm.getLastName() != null ? fullName + " " + loginFieldDummyForm.getLastName()
					: fullName;
			loginUsers.setFullName(fullName);
			loginUsers.setUsername(loginFieldDummyForm.getEmpId());
			RoleMaster role = roleMasterDao.findById(loginFieldDummyForm.getRole().getId());
			loginUsers.setRole(role);
//			String password = new SimpleDateFormat("yyyyMMdd").format(loginFieldDummyForm.getBirthDate());
//			System.out.println("Password : "+password);
			loginUsers.setPassword(passwordEncoder.encode(loginUsers.getUsername() + "@123"));
			if (role.isTranPassword())
				loginUsers.setTranPassword(passwordEncoder.encode(loginUsers.getUsername() + "@123456"));
			System.out.println(loginUsers.toString());
			userdao.generateLoginDetails(loginUsers, loginFieldDummyForm.getEmpMasterId());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public LoginFieldDummyForm checkDuplicate(String empId) {
		// TODO Auto-generated method stub
		LoginUsers loginUsers = userdao.findByusername(empId);
		if (loginUsers != null) {
			LoginFieldDummyForm loginFieldDummyForm = new LoginFieldDummyForm();
			loginFieldDummyForm.setId(loginUsers.getId());
			loginFieldDummyForm.setAccountNotDisable(loginUsers.isAccountNotDisable());
			loginFieldDummyForm.setAccountNotLock(loginUsers.isAccountNotLock());
			loginFieldDummyForm.setEmpId(empId);
			loginFieldDummyForm.setFullName(loginUsers.getFullName());
			loginFieldDummyForm.setLoginCredentials(true);
			loginFieldDummyForm.setRole(loginUsers.getRole());
			if (loginUsers.getTranPassword() != null && !loginUsers.getTranPassword().equals(""))
				loginFieldDummyForm.setTranPasswordflag(true);
			else
				loginFieldDummyForm.setTranPasswordflag(false);
			return loginFieldDummyForm;
		}
		return null;
	}

	@Override
	@AuditToken
	public boolean updateLoginInfo(LoginUsers checkLoginUser, LoginFieldDummyForm loginFieldDummyForm,
			String username) {
		// TODO Auto-generated method stub
		boolean flag = false;
		RoleMaster role = null;
		if (checkLoginUser.getRole() != null) {
			if (checkLoginUser.getRole().getId() != loginFieldDummyForm.getRole().getId()) {
				role = roleMasterDao.findById(loginFieldDummyForm.getRole().getId());
				checkLoginUser.setRole(role);
				flag = true;
			} else {
				role = checkLoginUser.getRole();
			}
		} else {
			role = roleMasterDao.findById(loginFieldDummyForm.getRole().getId());
			checkLoginUser.setRole(role);
			flag = true;
		}
		String password = new SimpleDateFormat("yyyyMMdd").format(new Date());
		password = "reset" + password;
		if (role != null && flag) {
			if (loginFieldDummyForm.isResetPassword()) {
				checkLoginUser.setPassword(passwordEncoder.encode(checkLoginUser.getUsername() + "@123"));
			}
			if (loginFieldDummyForm.isResetTranPassword() || loginFieldDummyForm.isTranPasswordflag()) {
				if (role.isTranPassword()) {
					checkLoginUser.setTranPassword(passwordEncoder.encode(checkLoginUser.getUsername() + "@123456"));
				} else {
					checkLoginUser.setTranPassword("");
				}
			}
		} else {
			if (loginFieldDummyForm.isResetPassword()) {
				checkLoginUser.setPassword(passwordEncoder.encode(checkLoginUser.getUsername() + "@123"));
				flag = true;
			}
			if (loginFieldDummyForm.isResetTranPassword()) {
				checkLoginUser.setTranPassword(passwordEncoder.encode(checkLoginUser.getUsername() + "@123456"));
				flag = true;
			}
		}
		if (flag) {
			checkLoginUser.setUpdatedBy(username);
			checkLoginUser.setUpdatedOn(new Date());
			userdao.updateLoginCredentials(checkLoginUser);
			return true;
		}
		return false;
	}

	@Override
	@AuditToken
	public boolean updateLoginPassword(LoginUsers checkLoginUser, String password) {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			checkLoginUser.setPassword(passwordEncoder.encode(password));
			checkLoginUser.setUpdatedBy(checkLoginUser.getUsername());
			checkLoginUser.setUpdatedOn(new Date());
			userdao.updateLoginCredentials(checkLoginUser);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public List<LoginUsers> findAllActiveUsers() {
		// TODO Auto-generated method stub
		return userdao.findAllActiveUsers();
	}

	@Override
	public void updateLoginInfo(LoginUsers user) {
		userdao.updateLoginCredentials(user);

	}

	@Override
	public UserWiseStudiesAsignMaster getUserWiseSitesAsignMasterRecord(Long studyId, Long userId) {
		return userdao.getUserWiseSitesAsignMasterRecord(studyId, userId);
	}

	@Override
	public List<RolesWiseModules> getApplicationMenus(LoginUsers users) {
		return userdao.getApplicationMenus(users);
	}

	@Override
	public List<RolesWiseModules> getApplicationMenusBasedOnStudy(RoleMaster role) {
		return userdao.getApplicationMenusBasedOnStudy(role);
	}

	@Override
	public List<RolesWiseModules> getRolesWiseModulesList() {
		return userdao.getRolesWiseModulesList();
	}
}
