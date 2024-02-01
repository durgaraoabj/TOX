package com.springmvc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.covide.template.dto.SysmexDto;
import com.springmvc.model.ObservationInturmentConfiguration;
import com.springmvc.model.SysmexData;
import com.springmvc.model.SysmexRawData;
import com.springmvc.model.SysmexTestCodeData;

public class SysmexDao {
	Connection con = null;
	Statement st = null;
	ResultSet rs = null;

	public Long sutydIdByNumber(String studyNumber) {
		Long id = 0l;
		try {
			st = con.createStatement();
			rs = st.executeQuery("select id from tox.study_master where studyNo = '" + studyNumber + "';");
			while (rs.next()) {
				id = rs.getLong(1);
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 0l;
		}
		return id;
	}

	public Long animalIdByByanimalPerminentId(String animalId, Long studyId, ObservationInturmentConfiguration observationInturmentConfiguration) {
		Long id = 0l;
		try {
//			 ;
			st = con.createStatement();
			if(observationInturmentConfiguration.getStudyAcclamatizationDates() != null)
			rs = st.executeQuery("select id from tox.STUDY_animal where animalNo = '" + animalId
					+ "' and studyId = '" + studyId + "';");
			else if(observationInturmentConfiguration.getStudyTreatmentDataDates() != null)
				rs = st.executeQuery("select id from tox.STUDY_animal where permanentNo = '" + animalId
						+ "' and studyId = '" + studyId + "';");
			else
				return id;
			while (rs.next()) {
				id = rs.getLong(1);
			}
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 0l;
		}
		return id;
	}

	public Long saveSysmexData(SysmexData sysData, Long studyId, Long animalId) {
		SimpleDateFormat dateTimeSecondsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long id = 0l;
		try {
			String testRuntype = "Animal";
			
			if (sysData.getAnimalNumber().contains("QC")) {
				testRuntype = "QC";
			}
			String sql = "select id from tox.SYSMEX_DATA where studyId=? and animalNumber= ? and startTime= ? and typeOfActivity = ?";
			
			
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
			stmt.setLong(1, studyId);
			stmt.setString(2, sysData.getAnimalNumber());
			stmt.setTimestamp(3, new java.sql.Timestamp(sysData.getStartTime().getTime()));
			if(sysData.getObservationInturmentConfiguration().getStudyAcclamatizationDates() != null) {
				stmt.setString(4, "Acclamatization");
			}else if(sysData.getObservationInturmentConfiguration().getStudyTreatmentDataDates() != null) {
				stmt.setString(4, "Treatment");
			}
//			stmt.setDate(3, dateTimeSecondsFormat.format(sysData.getStartTime()));
			System.out.println(stmt.toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				id = rs.getLong(1);
			}
			if (id < 1) {
				sql = "insert into tox.SYSMEX_DATA (instumentModelNo, instumentYear, instrumentSno, instrumentReferenceNo, instrumentRemainNo,"
						+ "sampleNo, gender, animalNumber, animalCode, studyNumber,"
						+ " timePoint, startTime, endTime, animalId, studyId, "
						+ "sysmexAnimalCodeId, finalValue, testRuntype, id, observationInturmentConfigurationId, " 
						+ " typeOfActivity) " 
						+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?)";
				System.out.println(sysData.getInstumentModelNo());
				System.out.println(sysData.getInstumentYear());
				System.out.println(sysData.getInstrumentSno());
				System.out.println(sysData.getInstrumentReferenceNo());
				System.out.println(sysData.getInstrumentRemainNo());
				System.out.println(sysData.getSampleNo());
				System.out.println(sysData.getGender());
				System.out.println(sysData.getAnimalNumber());
				System.out.println(sysData.getAnimalCode());
				stmt = (PreparedStatement) con.prepareStatement(sql);
				stmt.setString(1, sysData.getInstumentModelNo());
				stmt.setString(2, sysData.getInstumentYear());
				stmt.setString(3, sysData.getInstrumentSno());
				stmt.setString(4, sysData.getInstrumentReferenceNo());
				stmt.setString(5, sysData.getInstrumentRemainNo());
				stmt.setString(6, sysData.getSampleNo());
				stmt.setString(7, sysData.getGender());
				stmt.setString(8, sysData.getAnimalNumber());
				stmt.setString(9, sysData.getAnimalCode());
				stmt.setString(10, sysData.getStudyNumber());
				stmt.setString(11, sysData.getTimePoint());
				stmt.setTimestamp(12, new java.sql.Timestamp(sysData.getStartTime().getTime()));
				stmt.setTimestamp(13, new java.sql.Timestamp(sysData.getEndTime().getTime()));
//				stmt.setString(12, dateTimeSecondsFormat.format(sysData.getStartTime()));
//				stmt.setString(13, dateTimeSecondsFormat.format(sysData.getEndTime()));
				if (animalId != 0)
					stmt.setLong(14, animalId);
				else
					stmt.setNull(14, Types.NULL);
				if (studyId != 0)
					stmt.setLong(15, studyId);
				else
					stmt.setNull(15, Types.NULL);
				if (studyId != null && studyId != 0)
					stmt.setLong(15, studyId);
				else
					stmt.setNull(15, Types.NULL);
				try {
					stmt.setLong(16, SysmexThread.animalCodes.get(sysData.getAnimalCode()));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					stmt.setNull(16, Types.NULL);
				}
				stmt.setBoolean(17, true);
				stmt.setString(18, testRuntype);
				stmt.setLong(19, sysData.getId());
				stmt.setLong(20, sysData.getObservationInturmentConfiguration().getId());
				if(sysData.getObservationInturmentConfiguration().getStudyAcclamatizationDates() != null) {
					stmt.setString(21, "Acclamatization");
				}else if(sysData.getObservationInturmentConfiguration().getStudyTreatmentDataDates() != null) {
					stmt.setString(21, "Treatment");
				}
				
				System.out.println(stmt.toString());
				int i = stmt.executeUpdate();
				stmt.close();
				if (i > 0) {
					sql = "select id from tox.SYSMEX_DATA where studyNumber=? and animalNumber= ? and startTime= ?";
					stmt = (PreparedStatement) con.prepareStatement(sql);
					stmt.setString(1, sysData.getStudyNumber());
					stmt.setString(2, sysData.getAnimalNumber());
					stmt.setTimestamp(3, new java.sql.Timestamp(sysData.getStartTime().getTime()));
//					stmt.setString(3, dateTimeSecondsFormat.format(sysData.getStartTime()));
					rs = stmt.executeQuery();
					while (rs.next()) {
						id = rs.getLong(1);
					}
				}
			} else {
				id = 0l;
			}
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 0l;
		}
		return id;
	}

	public int saveSysmexTestCodeDatas(List<SysmexTestCodeData> sysmexTestCodeDatas, Long sysDataId) {
		int id = 0;
		try {
			String sql = "insert into tox.SYSMEX_TESTCODE_DATA (sysmexDataId, code, seqNo, testCode,"
					+ "value,units, code1, code2, runTime, studyTestCodeId, orderNo, id, finalValue) values(?,?,?,?,?, ?,?,?,?,? ,?,?,?)";
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
			for (SysmexTestCodeData stcd : sysmexTestCodeDatas) {
				stmt.setLong(1, sysDataId);
				stmt.setString(2, stcd.getCode());
				stmt.setInt(3, stcd.getSeqNo());
				stmt.setString(4, stcd.getTestCode());
				stmt.setString(5, stcd.getValue());
				stmt.setString(6, stcd.getUnits());
				stmt.setString(7, stcd.getCode1());
				stmt.setString(8, stcd.getCode2());
				stmt.setDate(9, new java.sql.Date(stcd.getRunTime().getTime()));
				if (stcd.getStudyTestCode() != null) {
					stmt.setLong(10, stcd.getStudyTestCode().getId());
				} else {
					stmt.setNull(10, Types.NULL);
				}
				stmt.setInt(11, stcd.getOrderNo());
				System.out.println(stcd.getId());
				stmt.setLong(12, stcd.getId());
				stmt.setBoolean(13, true);
				stmt.addBatch();
			}
			System.out.println(stmt.toString());
			int[] ids = stmt.executeBatch();
			stmt.close();
			id = ids.length;
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 0;
		}
		return id;
	}

	public int saveSysmexRawData(SysmexRawData sysmexRawData, Long sysDataId) {
		int id = 0;
		try {
			String sql = "insert into tox.SYSMEX_RAW_DATA (sysmexDataId, rawData, id) values(?,?,?)";
			PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);

			stmt.setLong(1, sysDataId);
			stmt.setString(2, sysmexRawData.getRawData());
			stmt.setLong(3, sysmexRawData.getId());
			id = stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			id = 0;
		}
		return id;
	}

	public void saveSysmexDto(SysmexDto dto) {
		int id = 0;
		try {
			con = DBConnection.dbConnection();
			if (con != null) {
				con.setAutoCommit(false);
//				Long studyId = sutydIdByNumber(dto.getSysData().getStudyNumber());
				Long studyId = SysmexThread.studyId;
				Long animalId = animalIdByByanimalPerminentId(dto.getSysData().getAnimalNumber(), studyId, dto.getObservationInturmentConfiguration());
//				if(animalId > 0) {
					Long sysDataId = saveSysmexData(dto.getSysData(), studyId, animalId);
					if (sysDataId > 0l) {
						int sysmexTestCodeDatasCount = saveSysmexTestCodeDatas(dto.getSysmexTestCodeDatas(), sysDataId);
						int sysmexRawDataCount = saveSysmexRawData(dto.getSysmexRawData(), sysDataId);
					}					
//				}
				con.commit();
				con.setAutoCommit(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			id = 0;
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}

	}

	public Long sysmexDataMaxId() {
		Long id = 0l;
		try {
			con = DBConnection.dbConnection();
			if (con != null) {
				String sql = "select max(id) from tox.SYSMEX_DATA";
				PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					id = rs.getLong(1);
				}
				if (id != null)
					return id;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return 0l;
	}

	public Long sysmaxRawDtaMaxId() {
		Long id = 0l;
		try {
			con = DBConnection.dbConnection();
			if (con != null) {
				String sql = "select max(id) from tox.SYSMEX_RAW_DATA";
				PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					id = rs.getLong(1);
				}
				if (id != null)
					return id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return 0l;
	}

	public Long sysmexTestCodeDataMaxId() {
		Long id = 0l;
		try {
			con = DBConnection.dbConnection();
			if (con != null) {
				String sql = "select max(id) from tox.SYSMEX_TESTCODE_DATA";
				PreparedStatement stmt = (PreparedStatement) con.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					id = rs.getLong(1);
				}
				if (id != null)
					return id;
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		}
		return 0l;
	}

}
