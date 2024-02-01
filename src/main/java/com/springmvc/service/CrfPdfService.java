package com.springmvc.service;

import java.util.List;

import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElementData;

public interface CrfPdfService {
	
	List<CrfGroupElementData> studyCrfGroupElementDataList(CrfGroupDataRows rowsPojo, CrfGroup group);

}
