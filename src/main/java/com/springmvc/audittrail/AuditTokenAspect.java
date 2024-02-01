package com.springmvc.audittrail;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springmvc.dao.AuditTrailDao;
import com.springmvc.model.AuditTrail;

@Aspect
@Component
public class AuditTokenAspect {

	@Autowired
	AuditTrailDao auditTrailDao;

	@Autowired
	private HttpSession session;

	@Before("@annotation(auditToken)")
	public void beforeTokenRequiredWithAnnoation(AuditToken auditToken) throws Throwable {
		System.out.println("=====================================>Before AuditTrail start<====================================");
		AuditInterceptor.sample = new HashMap<AuditTrail, Auditable>();
		System.out.println("=====================================>Before AuditTrail Ended<====================================");
	}

	@After("@annotation(auditToken)")
	public void afterTokenRequiredWithAnnoation(AuditToken auditToken) throws Throwable {
		try {
			System.out.println("=====================================>After AuditTrail start<====================================");
			Map<AuditTrail, Auditable> sample = AuditInterceptor.sample;
			String username = session.getAttribute("userName").toString();
			auditTrailDao.saveAuditTrailDetails(sample, username);
//			AuditInterceptor.sample = null;
			System.out.println("=====================================>After AuditTrail Ended<====================================");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		
	}

}
