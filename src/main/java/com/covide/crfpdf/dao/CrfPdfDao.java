package com.covide.crfpdf.dao;

import java.util.List;

import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElementData;

public interface CrfPdfDao {
	
	List<CrfGroupElementData> getStudyCrfGroupElementDataList(CrfGroupDataRows rowsPojo, CrfGroup group);
}
