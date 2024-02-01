package com.covide.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SysmexTestCodeNos implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3127289777124920733L;
	public static Map<Integer, String> noAndTcWithUnits  = new HashMap<>();
	public static Map<Integer, String> noAndTc  = new HashMap<>();
	public static Map<String, Integer> tcAndNo  = new HashMap<>();
	static {
		
		// TODO Auto-generated constructor stub
		noAndTc.put(1, "RBC");
		noAndTcWithUnits.put(1, "RBC10*6/uL");
		tcAndNo.put("RBC", 1);
		noAndTc.put(2, "WBC");
		noAndTcWithUnits.put(2, "WBC(10*3/uL)");
		tcAndNo.put("WBC", 2);
		noAndTc.put(3, "HGB");
		noAndTcWithUnits.put(3, "HGB(g/dL)");
		tcAndNo.put("HGB", 3);
		noAndTc.put(4, "HCT");
		noAndTcWithUnits.put(4, "HCT(%)");
		tcAndNo.put("HCT", 4);
		noAndTc.put(5, "MCV");
		noAndTcWithUnits.put(5, "MCV(fL)");
		tcAndNo.put("MCV", 5);
		noAndTc.put(6, "MCH");
		noAndTcWithUnits.put(6, "MCH(pg)");
		tcAndNo.put("MCH", 6);
		noAndTc.put(7, "MCHC");
		noAndTcWithUnits.put(7, "MCHC(g/dL)");
		tcAndNo.put("MCHC", 7);
		 
		noAndTc.put(8, "PLT");  
		noAndTcWithUnits.put(8, "PLT(10*3/uL)");
		tcAndNo.put("PLT", 8);
		noAndTc.put(9, "NEUT%");
		noAndTcWithUnits.put(9, "NEUT(%)");
		tcAndNo.put("NEUT%", 9);
		noAndTc.put(10, "LYMPH%");
		noAndTcWithUnits.put(10, "LYMPH(%)");
		tcAndNo.put("LYMPH%", 10);
		noAndTc.put(11, "MONO%");
		noAndTcWithUnits.put(11, "MONO(%)");
		tcAndNo.put("MONO%", 11);
		noAndTc.put(12, "EO%");
		noAndTcWithUnits.put(12, "EO(%)");
		tcAndNo.put("EO%", 12);
		noAndTc.put(13, "BASO%");
		noAndTcWithUnits.put(13, "BASO(%)");
		tcAndNo.put("BASO%", 13);
		noAndTc.put(14, "NEUT#");
		noAndTcWithUnits.put(14, "NEUT#(10*3/uL)");
		tcAndNo.put("NEUT#", 14);
		noAndTc.put(15, "LYMPH#");
		noAndTcWithUnits.put(15, "LYMPH#(10*3/uL)");
		tcAndNo.put("LYMPH#", 15);
		noAndTc.put(16, "MONO#");
		noAndTcWithUnits.put(16, "MONO#(10*3/uL)");
		tcAndNo.put("MONO#", 16);
		noAndTc.put(17, "EO#");
		noAndTcWithUnits.put(17, "EO#(10*3/uL)");
		tcAndNo.put("EO#", 17);
		noAndTc.put(18, "BASO#");
		noAndTcWithUnits.put(18, "BASO#(10*3/uL)");
		tcAndNo.put("BASO#", 18);
		noAndTc.put(19, "NRBC%");
		noAndTcWithUnits.put(19, "NRBC%(/100WBC)");
		tcAndNo.put("NRBC%", 19);
		noAndTc.put(20, "NRBC#");
		noAndTcWithUnits.put(20, "NRBC#((10*3/uL))");
		tcAndNo.put("NRBC#", 20);
		noAndTc.put(21, "RDW-SD");
		noAndTcWithUnits.put(21, "RDW-SD(fL)");
		tcAndNo.put("RDW-SD", 21);
		noAndTc.put(22, "RDW-CV");
		noAndTcWithUnits.put(22, "RDW-CV(%)");
		tcAndNo.put("RDW-CV", 22);
		noAndTc.put(23, "PDW");
		noAndTcWithUnits.put(23, "PDW(fL)");
		tcAndNo.put("PDW", 23);
		noAndTc.put(24, "MPV");
		noAndTcWithUnits.put(24, "MPV(fL)");
		tcAndNo.put("MPV", 24);
		noAndTc.put(25, "P-LCR");
		noAndTcWithUnits.put(25, "P-LCR(%)");
		tcAndNo.put("P-LCR", 25);
		noAndTc.put(26, "PCT");
		noAndTcWithUnits.put(26, "PCT(%)");
		tcAndNo.put("PCT", 26);
		noAndTc.put(27, "RET%");
		noAndTcWithUnits.put(27, "RET(%)");
		tcAndNo.put("RET%", 27);
		noAndTc.put(28, "RET#");
		noAndTcWithUnits.put(28, "RET#(10*6/uL)");
		tcAndNo.put("RET#", 28);
		noAndTc.put(29, "IRF");
		noAndTcWithUnits.put(29, "IRF(%)");
		tcAndNo.put("IRF", 29);
		noAndTc.put(30, "LFR");
		noAndTcWithUnits.put(30, "LFR(%)");
		tcAndNo.put("LFR", 30);
		noAndTc.put(31, "MFR");
		noAndTcWithUnits.put(31, "MFR(%)");
		tcAndNo.put("MFR", 31);
		noAndTc.put(32, "HFR");
		noAndTcWithUnits.put(32, "HFR(%)");
		tcAndNo.put("HFR", 32);
		noAndTc.put(33, "RET-HE");
		noAndTcWithUnits.put(33, "RET-HE(pg)");
		tcAndNo.put("RET-HE", 33);
		noAndTc.put(34, "WBC-N");
		noAndTcWithUnits.put(34, "WBC-N(10*3/uL)");
		tcAndNo.put("WBC-N", 34);
		noAndTc.put(35, "WBC-D");
		noAndTcWithUnits.put(35, "WBC-D(10*3/uL)");
		tcAndNo.put("WBC-D", 35);
		noAndTc.put(36, "PLT-I");
		noAndTcWithUnits.put(36, "PLT-I(10*3/uL)");
		tcAndNo.put("PLT-I", 36);
		noAndTc.put(37, "PLT-O");
		noAndTcWithUnits.put(37, "PLT-O(10*3/uL)");
		tcAndNo.put("PLT-O", 37);
	}
	
}
