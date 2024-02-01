package com.springmvc.audittrail;

import java.util.List;

public interface Auditable {

	public Long getId();

	public String getLoginUser();

	public List<?> getAuditableFields();

	public String getPrimaryField();

	public String getAuditMessage();

}
