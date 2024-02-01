package com.springmvc.dao.impl;

import org.springframework.stereotype.Repository;

import com.springmvc.abstractdao.AbstractDao;
import com.springmvc.dao.PincodeDao;
import com.springmvc.model.PinCodeMaster;

@Repository
public class PincodeDaoImpl extends AbstractDao<Long, PinCodeMaster> implements PincodeDao {

	@Override
	public PinCodeMaster findByPinCodeId(Long pincode) {
		// TODO Auto-generated method stub
		return getByKey(pincode);
	}

}
