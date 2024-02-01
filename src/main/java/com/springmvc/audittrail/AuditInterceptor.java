package com.springmvc.audittrail;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.springmvc.model.AuditTrail;


public class AuditInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;
	public static Map<AuditTrail, Auditable> sample = null;

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
//		System.out.println("onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types)");
		return true;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) {
		System.out.println("onSave(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types)  Method Started");
		if (entity instanceof Auditable) {
			Auditable auditable = (Auditable) entity;
			String classn = entity.getClass().getName();
			Object obj;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String newDate = new String();
			java.sql.Timestamp timestamp = null;
			for (int i = 0; i < newValues.length; i++) {
				obj = newValues[i];
				if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Float
						|| obj instanceof Character || obj instanceof Date || obj instanceof java.sql.Date
						|| obj instanceof java.sql.Timestamp) {
					AuditTrail audit = new AuditTrail();
					audit.setAction("save");
					audit.setTableClass(classn);
					audit.setTableId("");
					audit.setFieldName(propertyNames[i]);
					audit.setOldValue("N/A");
					audit.setCreatedTime(new Date());
					audit.setReason("N/A");
					if (obj instanceof Date || obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
						if (obj instanceof Date) {
							date = (Date) obj;
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Date) {
							date = new Date(((java.sql.Date) obj).getTime());
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Timestamp) {
							timestamp = (java.sql.Timestamp) obj;
							long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
							date = new Date(milliseconds);
							newDate = sdf1.format(date);
						} else
							newDate = obj.toString();
						audit.setNewValue(newDate);
					} else {
						audit.setNewValue(obj.toString().trim());
					}
					if (audit.getNewValue() != null)
						if (!audit.getNewValue().equals(""))
							if (!audit.getOldValue().equals(audit.getNewValue())) {
								try {
								sample.put(audit, auditable);
								}catch (Exception e) {
									// TODO: handle exception
								}
							}
				}
			}
		}
		System.out.println("onSave(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types)  Method Ended");
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void preFlush(Iterator iterator) {
		System.out.println("Before commiting");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void postFlush(Iterator iterator) {
		System.out.println("After commiting");
	}
	
	@Override
	public void onDelete(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) {
		// TODO Auto-generated method stub
		System.out.println("onDelete(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) method started");
		if (entity instanceof Auditable) {
			Auditable auditable = (Auditable) entity;
			String classn = entity.getClass().getName();
			Object obj;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String newDate = new String();
			java.sql.Timestamp timestamp = null;
			for (int i = 0; i < newValues.length; i++) {
				obj = newValues[i];
				if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Float
						|| obj instanceof Character || obj instanceof Date || obj instanceof java.sql.Date
						|| obj instanceof java.sql.Timestamp) {
					AuditTrail audit = new AuditTrail();
					audit.setAction("DELETE");
					audit.setTableClass(classn);
					audit.setTableId("");
					audit.setFieldName(propertyNames[i]);
					audit.setNewValue("N/A");
					audit.setCreatedTime(new Date());
					audit.setReason("N/A");
					if (obj instanceof Date || obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
						if (obj instanceof Date) {
							date = (Date) obj;
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Date) {
							date = new Date(((java.sql.Date) obj).getTime());
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Timestamp) {
							timestamp = (java.sql.Timestamp) obj;
							long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
							date = new Date(milliseconds);
							newDate = sdf1.format(date);
						} else
							newDate = obj.toString();
						audit.setOldValue(newDate);
					} else {
						audit.setOldValue(obj.toString().trim());
					}
					if (audit.getNewValue() != null)
						if (!audit.getNewValue().equals(""))
							sample.put(audit, auditable);
				}
			}
		}
		System.out.println("onDelete(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) method ended");
		return;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] newValues, Object[] oldValues, String[] propertyNames, Type[] types) {
		// TODO Auto-generated method stub
		System.out.println("onFlushDirty(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) method started");
		if (entity instanceof Auditable) {
			Auditable auditable = (Auditable) entity;
			String classn = entity.getClass().getName();
			Object obj;
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String newDate = new String();
			java.sql.Timestamp timestamp = null;
			for (int i = 0; i < newValues.length; i++) {
				obj = newValues[i];
				if (obj instanceof String || obj instanceof Integer || obj instanceof Double || obj instanceof Float
						|| obj instanceof Character || obj instanceof Date || obj instanceof java.sql.Date
						|| obj instanceof java.sql.Timestamp) {
					AuditTrail audit = new AuditTrail();
					audit.setAction("UPDATE");
					audit.setTableClass(classn);
					audit.setTableId("");
					audit.setFieldName(propertyNames[i]);
					audit.setCreatedTime(new Date());
					audit.setReason("N/A");
					if (obj instanceof Date || obj instanceof java.sql.Date || obj instanceof java.sql.Timestamp) {
						if (obj instanceof Date) {
							date = (Date) obj;
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Date) {
							date = new Date(((java.sql.Date) obj).getTime());
							newDate = sdf1.format(date);
						} else if (obj instanceof java.sql.Timestamp) {
							timestamp = (java.sql.Timestamp) obj;
							long milliseconds = timestamp.getTime() + (timestamp.getNanos() / 1000000);
							date = new Date(milliseconds);
							newDate = sdf1.format(date);
						} else
							newDate = obj.toString();
						audit.setNewValue(newDate);
						try {
						audit.setOldValue(oldValues[i]!=null?oldValues[i].toString():"");
						}catch (Exception e) {
							// TODO: handle exception
							audit.setOldValue("");
						}
					} else {
						audit.setNewValue(obj.toString().trim());
						try {
						audit.setOldValue(oldValues[i]!=null?oldValues[i].toString().trim():"");
						}catch (Exception e) {
							// TODO: handle exception
							audit.setOldValue("");
						}
					}
					if (audit.getNewValue() != null)
						if (!audit.getNewValue().equals(""))
							if (!audit.getOldValue().equals(audit.getNewValue())) {
								try {
								sample.put(audit, auditable);
								}catch (Exception e) {
									// TODO: handle exception
								}
							}
				}
			}
		}
		System.out.println("onFlushDirty(Object entity, Serializable id, Object[] newValues, String[] propertyNames, Type[] types) method ended");
		return true;
	}
	

}
