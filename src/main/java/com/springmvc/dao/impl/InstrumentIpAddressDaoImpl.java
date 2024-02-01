package com.springmvc.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.model.InstrumentIpAddress;

@SuppressWarnings("unchecked")
@Repository
public class InstrumentIpAddressDaoImpl extends AbstractDao<Long, InstrumentIpAddress>{
	
	public List<InstrumentIpAddress> instrumentIpAddressesOfListOfIds(List<Long> arrayList) {
		return createEntityCriteria().add(Restrictions.in("id", arrayList)).list();
	}

	public InstrumentIpAddress instrumentIpAddressByName(String instrumentName) {
		// TODO Auto-generated method stub
		return (InstrumentIpAddress) createEntityCriteria().add(Restrictions.eq("instrumentName", instrumentName)).uniqueResult();
	}

}
