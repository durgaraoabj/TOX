import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class StringTest {
	public static void main(String[] args) throws ScriptException {
		String sr  = "g_54_elsdf_4";
		System.out.println(sr.substring(0, sr.indexOf("_")+1));
		
		if(sr.substring(0, sr.indexOf("_")+1).equals("g_")) {
			sr = sr.replace("g_", "");
			sr = sr.substring(sr.indexOf("_")+1);
			sr = "g_"+sr;
		}else {
			sr = sr.substring(sr.indexOf("_")+1);
		}
		System.out.println(sr);
		
		Map<String, Double> eleValues  = new HashMap<>();
		eleValues.put("ELE[e1],", 32.0);
		eleValues.put("ELE[e13],", 333.0);
		eleValues.put("ELE[e2],", 32.0);
		
		
		String a = "sum(ELE[e1],ELE[e2],)";
		String s = "(sum(ELE[e1],,ELE[e2],,ELE[e13],)-avg(ELE[e1],,ELE[e2],)+pow(ELE[e1],,ELE[e2],))+sqrt(3)/2" ;
		//String s = "((ELE[e1],+ELE[e2],*10))/2" ; 
		for(Map.Entry<String, Double> d : eleValues.entrySet()) {
			s = s.replace(d.getKey(), ""+d.getValue());		
		}
		
		System.out.println(s);
		while (s.contains("sum(")) {

			String su = s.substring(s.indexOf("sum("), s.indexOf(")")+1);
			String result = sumof(su);
			s = s.replace(su, result);
			System.out.println(s);
		}
		while (s.contains("avg(")) {
			String su = s.substring(s.indexOf("avg("), s.indexOf(")")+1);
			String result = avgof(su);
			s = s.replace(su, result);
			System.out.println(s);
		}
		while (s.contains("pow(")) {
			String su = s.substring(s.indexOf("pow("), s.indexOf(")")+1);
			String result = powof(su);
			s = s.replace(su, result);
			System.out.println(s);
		}
		while (s.contains("sqrt(")) {
			String temp = s.substring(s.indexOf("sqrt("));
			String temp2 = temp.substring(0, temp.indexOf(")")+1);
			String result = sqrtof(temp2);
			s = s.replace(temp2, result);
			System.out.println(s);
		}
		while (s.contains("cbrt(")) {
			String temp = s.substring(s.indexOf("cbrt("));
			String temp2 = temp.substring(0, temp.indexOf(")")+1);
			String result = cbrtof(temp2);
			s = s.replace(temp2, result);
			System.out.println(s);
		}
		System.out.println(s);
		
		
		
		 // create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
	    Object obj = engine.eval(s);
	    System.out.println( obj );
		    
	}

	private static String cbrtof(String s) {
		s = s.substring(5, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = Math.cbrt(Double.parseDouble(s));
		}catch (Exception e) {
			// TODO: handle exception
		}
		return d+ "";
	}
	private static String sqrtof(String s) {
		s = s.substring(5, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = Math.sqrt(Double.parseDouble(s));
		}catch (Exception e) {
			// TODO: handle exception
		}
		return d+ "";
	}
	
	private static String powof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		try {
			d = Math.pow(Double.parseDouble(ss[0]), Double.parseDouble(ss[0]));
		}catch (Exception e) {
			// TODO: handle exception
		}
		return d+ "";
	}
	
	private static String avgof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return (d/ss.length) + "";
	}
	
	private static String sumof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		
		return d + "";
	}
}
