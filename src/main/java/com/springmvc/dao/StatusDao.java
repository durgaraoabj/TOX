package com.springmvc.dao;

import java.util.List;

import com.springmvc.model.Species;
import com.springmvc.model.StagoData;
import com.springmvc.model.StaticData;
import com.springmvc.model.StatusMaster;
import com.springmvc.model.WorkFlow;
import com.springmvc.model.WorkFlowReviewStages;

public interface StatusDao {

	StatusMaster findById(long statusId);

	StatusMaster statusMaster(String string);

	StaticData staticData(String string, String string2);

	/*
	 * To get all Status Masters
	 */
	List<StatusMaster> fiendAll();

	/*
	 * To save Status Masters
	 */
	StatusMaster saveStatusMaster(StatusMaster statusMaster);
	/*
	 * To save StaticData
	 */
	void saveStaticData(StaticData staticData);

	/*
	 * To save Species Masters
	 */
	void saveSpecies(Species species);

	WorkFlow saveWorkFlow(WorkFlow workFlow);

	void saveworkFlowReviewStages(WorkFlowReviewStages workFlowReviewStages);

	List<StaticData> staticDataList();


}
