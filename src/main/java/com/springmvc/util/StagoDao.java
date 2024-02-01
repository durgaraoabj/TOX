package com.springmvc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.springmvc.model.LoginUsers;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.StagoData;
import com.springmvc.model.StudyAnimal;
import com.springmvc.model.StudyMaster;
import com.springmvc.model.StudyTestCodes;

public class StagoDao {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;

	public String saveStagoData(List<StagoData> sysmexTestCodeDatas, Long studyId) {
		String studyNo = "";
		try {
			if (con == null) {
				con = DBConnection.dbConnection();
			}
			studyNo = sutydIdNumberById(studyId, con);
			Long id2 = stagoDataMaxId(con);
			String sql = "insert into tox.STAGO_DATA (" + "studyId, testName, animalNo, data, testResult,"
					+ "instrument, tar, afterDuriation,noOfAnimals, receivedTime,"
					+ "studyNumber,studyAnimalId,selectedResult, testCodeId, activityDoneBy, "
					+ "sampleType, lotNo, id,observationInturmentConfigurationId) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?)";

			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
			for (StagoData stcd : sysmexTestCodeDatas) {
				id2++;
				stmt.setLong(1, Long.parseLong(stcd.getStdId()));
				stmt.setString(2, stcd.getTestCode().getTestCode().getTestCode());
				stmt.setString(3, stcd.getAnimalNo());

				String d = stcd.getData();
				d.replaceAll("\u0000", "");
				System.out.println(d);
//				stmt.setString(4, stcd.getData());
				stmt.setString(4, "asdfasdfasfasdf");
				stmt.setString(5, stcd.getTestResult());

				stmt.setString(6, stcd.getInstrument());
				stmt.setString(7, stcd.getTar());
				stmt.setString(8, stcd.getAfterDuriation());
				stmt.setString(9, stcd.getNoOfAnimals());
				System.out.println(new java.sql.Timestamp(stcd.getReceivedTime().getTime()));
				stmt.setTimestamp(10, new java.sql.Timestamp(stcd.getReceivedTime().getTime()));

				stmt.setString(11, studyNo);
				if(stcd.getSampleType().equals("Animal")) {
					Long animalId = studyAninalId(con, studyId, stcd.getAnimalNo(), stcd.getObservationInturmentConfiguration());
					if (animalId != 0l)
						stmt.setLong(12, animalId);
					else
						stmt.setNull(12, Types.NULL);
				}else
					stmt.setNull(12, Types.NULL);

				stmt.setBoolean(13, true);
				stmt.setLong(14, stcd.getTestCode().getId());
				stmt.setLong(15, StagoThread.userId);
				stmt.setString(16, stcd.getSampleType());
				stmt.setString(17, stcd.getLotNo());
				stmt.setLong(18, id2 + 1);
				stmt.setLong(19, stcd.getObservationInturmentConfiguration().getId());
//				stmt.setBoolean(14, true);
				stmt.addBatch();
			}
			System.out.println(stmt.toString());
			int[] ids = stmt.executeBatch();
			stmt.close();
			stmt.close();
			con.close();
			con = null;
		} catch (Exception e) {
			e.printStackTrace();
			studyNo = "";
		}
		return studyNo;
	}

	private Long studyAninalId(Connection con, Long studyId, String animalNo, ObservationInturmentConfiguration observationInturmentConfiguration) {
		// TODO Auto-generated method stub
		Long id = 0l;
		try {
			st = con.createStatement();
			if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null)
				rs = st.executeQuery("select id from tox.STUDY_animal where studyId = '" + studyId + "' and sequnceNo = '"
						+ animalNo + "'");
			else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null) {
				rs = st.executeQuery("select id from tox.STUDY_animal where studyId = '" + studyId + "' and animalId = '"
						+ animalNo + "'");
			}
				
			
			while (rs.next()) {
				id = rs.getLong(1);
			}
			st.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return id;
	}

	public String sutydIdNumberById(Long studyId, Connection con) {
		String number = "";
		try {
			st = con.createStatement();
			rs = st.executeQuery("select studyNo from tox.study_master where id = '" + studyId + "';");
			while (rs.next()) {
				number = rs.getString(1);
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			number = studyId + "";
		}
		return number;
	}

	public Long stagoDataMaxId(Connection con) {
		Long id = 0l;
		try {
			String sql = "select max(id) from tox.STAGO_DATA";
			st = con.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong(1);
			}
//			id++;
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 1l;
		}
		return id;
	}
	
	public void getconnection() {
		// TODO Auto-generated method stub
		con = DBConnection.dbConnection();
	}

}
