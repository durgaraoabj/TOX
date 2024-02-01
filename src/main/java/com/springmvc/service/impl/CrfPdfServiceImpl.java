package com.springmvc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crfpdf.dao.CrfPdfDao;
import com.springmvc.service.CrfPdfService;

@Service("crfPdfService")
public class CrfPdfServiceImpl implements CrfPdfService{
	
	@Autowired
	CrfPdfDao crfPdfDao;

	
	@Override
	public List<CrfGroupElementData> studyCrfGroupElementDataList(CrfGroupDataRows rowsPojo,
			CrfGroup group) {
		return crfPdfDao.getStudyCrfGroupElementDataList(rowsPojo, group);
	}


	
	

	

}
