package com.springmvc.dao;

import com.springmvc.model.PinCodeMaster;

public interface PincodeDao {

	PinCodeMaster findByPinCodeId(Long pincode);

}
