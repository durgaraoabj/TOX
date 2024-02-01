package com.covide.crfpdf.dao.Impl;


import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.covide.crf.dto.Crf;
import com.covide.crf.dto.CrfGroup;
import com.covide.crf.dto.CrfGroupDataRows;
import com.covide.crf.dto.CrfGroupElementData;
import com.covide.crfpdf.dao.CrfPdfDao;
import com.springmvc.abstractdao.AbstractDao;

@Repository("crfPdfDao")
public class CrfPdfDaoImpl extends AbstractDao<Long, Crf>implements CrfPdfDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<CrfGroupElementData> getStudyCrfGroupElementDataList(CrfGroupDataRows rowsPojo,
			CrfGroup group)  {
		return getSession().createCriteria(CrfGroupElementData.class)
				.add(Restrictions.eq("group", group))
				.add(Restrictions.eq("volPeriodCrf", rowsPojo.getVolPeriodCrf())).list();
		
	}

	
}
