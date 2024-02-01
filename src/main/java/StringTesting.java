import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.covide.dto.CalculateStringValue;
import com.covide.dto.MeanCalculation;

public class StringTesting {

	public static void main(String[] args) {
		Map<String, String> dataList = new HashMap<>();
		dataList.put("valOf2", "2");
		dataList.put("valOf3", "3");
		dataList.put("valOf4", "4");
		dataList.put("valOf5", "5");
		dataList.put("valOf6", "6");
		String str = "MEAN(valOf2,valOf3,valOf4,valOf5)+MEAN(valOf2,valOf3,valOf4,valOf6)";
		String[] meanArr = str.split("MEAN");
		List<String> meanList = new ArrayList<>();
		double meanVal = 0;
		for(int i=0; i<meanArr.length; i++) {
			if(!meanArr[i].equals("")) {
				meanList.add(meanArr[i]);
				System.out.println("meanArr["+i+"]  is :"+meanArr[i]);
				String st = meanArr[i];
				st = st.replaceAll("\\(", "");
				st = st.replaceAll("\\)", "");
				st = st.replaceAll("[-+^*/]", " ");
				System.out.println("final String of "+i+" is :"+st);
				String[] tempArr = st.split("\\,");
				double[] inputArr = new double[tempArr.length];
				String rst = "";
				if(tempArr.length > 0) {
					for(int j=0; j<tempArr.length; j++) {
						double val = Double.parseDouble(dataList.get(tempArr[j].trim()));
						inputArr[j] = val;
						if(rst.equals(""))
							rst = tempArr[j].trim();
						else rst = rst+","+tempArr[j].trim();
					}
				}
				System.out.println("Replaced str is :"+st);
				MeanCalculation meanCalc = new MeanCalculation();
				double finalVal = meanCalc.calculateMeanValue(inputArr);
				System.out.println("Final Mean Val is : "+finalVal);
				str = str.replaceAll(rst, finalVal+"");
				if(meanList.size() == 1)
					meanVal = finalVal;
			}
		}
		System.out.println("Final String is : "+str);
		str = str.replaceAll("MEAN", "");
		int finalNo = 0;
		if(meanList.size() > 1)
			finalNo = CalculateStringValue.getCalculateStringValue(str);
		else
			finalNo = (int)(Math.round(meanVal));
		System.out.println("finalNo is : "+finalNo);
		
		
	}

}
