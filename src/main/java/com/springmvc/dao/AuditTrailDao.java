package com.springmvc.dao;

import java.util.Map;

import com.springmvc.audittrail.Auditable;
import com.springmvc.model.AuditTrail;

public interface AuditTrailDao {

	void saveAuditTrailDetails(Map<AuditTrail, Auditable> sample, String username);

}
