package com.springmvc.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springmvc.dao.impl.ClinicalCodesDaoImple;
import com.springmvc.model.ClinicalCodes;

@Component
public class OneTimeInitializer{
	@Autowired
	ClinicalCodesDaoImple clinicalCodesDao;
	public static Map<String, List<ClinicalCodes>> clinicalCodes = new HashMap<String, List<ClinicalCodes>>();
	@PostConstruct
    public void init() {
		System.out.println(clinicalCodesDao);		
		List<ClinicalCodes> list = clinicalCodesDao.allActiveClinicalCodes();
		list.forEach((cc) -> {
			List<ClinicalCodes> ccs = clinicalCodes.get(cc.getHeadding());
			if(ccs == null) ccs = new ArrayList<>();
			ccs.add(cc);
			clinicalCodes.put(cc.getHeadding(), ccs);
		});
    }


}
