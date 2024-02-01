import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class SubStringTest {
	public static void main(String[] args) {
//		String days = "2*1, 3, 5, 7**3*2,4,6";
//		Map<Long, String> daysMap = new HashMap<>();
//		if(days != null && !days.trim().equals("")) {
//			String[] dayArray = days.split("\\*\\*");
//			for(String d : dayArray) {
//				String[] idDay = d.split("\\*");
//				daysMap.put(Long.parseLong(idDay[0]), idDay[1]);
//			}
//		}
//		
//		String barcode = "010000411001000000021";
//		System.out.println(barcode);
//		System.out.println(barcode.substring(2, 20));
//		System.out.println(barcode.substring(20, 21));
//		System.out.println("Study = " + barcode.substring(2, 8));
//		System.out.println("Period= " + barcode.substring(8, 9));
//		System.out.println("Subj  = " + barcode.substring(9, 12));
//		System.out.println(barcode);
//		System.out.println(barcode.substring(0, 20));
//		System.out.println(barcode.substring(2, 20));
//		
//		String s = "ScheduleClockTime-105";
//		String[] ss = s.split("\\-");
//		System.out.println(ss[1]);
		
//		String t = "2 + (R(60.12340544)/(160.0*160.0))*1000";
//		t = t.replace("R(60.12340544)", "60.1234054");
//		System.out.println(t);
		String s = "24+round(2 + (round(60.12340544)/(160.0*160.0))*10000)";
		if(s.contains("sum(")) {
			s = s.replaceAll("sum", "S");
		}
		if(s.contains("avg(")) {
			s = s.replaceAll("avg", "A");
		}
		if(s.contains("pow(")) {
			s = s.replaceAll("pow", "P");
		}
		if(s.contains("sqrt(")) {
			s = s.replaceAll("sqrt", "s");
		}
		if(s.contains("cbrt(")) {
			s = s.replaceAll("cbrt", "C");
		}
		if(s.contains("round(")) {
			s = s.replaceAll("round", "R");
		}
		System.out.println(s);
		s=rec(s);
		System.out.println(s);
		s=caliculate(s);
		System.out.println(s);
	}
	public static String caliculate(String s) {
		String s1 = "";
		if(s.contains("(")) {
			String su = inBracket(s.substring(s.indexOf('(')+1));
			String tempTotal = "("+su+")";
			s1 = caliculate(su);
			s = s.replace(tempTotal, s1);
		}else {
				 // create a script engine manager
			    ScriptEngineManager factory = new ScriptEngineManager();
			    // create a JavaScript engine
			    ScriptEngine engine = factory.getEngineByName("JavaScript");
			    // evaluate JavaScript code from String
			    Object obj;
				try {
					obj = engine.eval(s);
					System.out.println(obj);
					return obj.toString();
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
		}
		return s;
	}
	public static String rec(String s) {
		String s1 = "";
		if(s.contains("S(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "S("+su+")";
			s1 = rec(su);
			s1 = sumof(s1);
			s = s.replace(tempTotal, s1);
			return s;
		}else if(s.contains("A(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "S("+su+")";
			s1 = rec(s);
			s1 = avgof(s1);
			s = s.replace(tempTotal, s1);
			return s;
		}else if(s.contains("P(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "S("+su+")";
			s1 = rec(s);
			s1 = powof(s1); 
			s = s.replace(tempTotal, s1);
			return s;
		}else if(s.contains("s(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "C("+su+")";
			s1 = rec(su);
			s1 = sqrtof(s1);
			s = s.replace(tempTotal, s1);
			return s;
		}else if(s.contains("C(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "C("+su+")";
			s1 = rec(su);
			s1 = cbrtof(s1);
			s = s.replace(tempTotal, s1);
			return s;
		}else if(s.contains("R(")) {
			String su = inBracket(s.substring(s.indexOf('R')+1));
			String tempTotal = "R("+su+")";
			s1 = rec(su);
			s = s.replace(tempTotal, s1);
			return s;
		}else {
				 // create a script engine manager
			    ScriptEngineManager factory = new ScriptEngineManager();
			    // create a JavaScript engine
			    ScriptEngine engine = factory.getEngineByName("JavaScript");
			    // evaluate JavaScript code from String
			    Object obj;
				try {
					obj = engine.eval(s);
					System.out.println(obj);
					return obj.toString();
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
		}
		return "";
	}
	public static String sqrtof(String s) {
		s = s.substring(5, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = Math.sqrt(Double.parseDouble(s));
		}catch (Exception e) {}
		return d+ "";
	}
	public static String cbrtof(String s) {
		s = s.substring(5, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = Math.cbrt(Double.parseDouble(s));
		}catch (Exception e) {}
		return d+ "";
	}
	public static String powof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		try {
			d = Math.pow(Double.parseDouble(ss[0]), Double.parseDouble(ss[0]));
		}catch (Exception e) {}
		return d+ "";
	}
	public static String avgof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return (d/ss.length) + "";
	}
	
	public static String sumof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return d + "";
	}
	
	public static String inBracket(String s) {
		int open = 0;
		int close = 0;
		int count = 0;
		System.out.println(s);
		for(int i = 0; i<s.length(); i++) {
			if(s.charAt(i) == '(') {
				count ++;
				open ++;
			}else if(s.charAt(i) == ')') {
				close ++;
				if(open == close) {
					-- count;
					if(count == 0) {
//						i--;
						s = s.substring(1, i);
						System.out.println(s);
						return s;
					}					
				}

			}
		}
		
//		System.out.println(s);
//		int count = 0;
//		for(int i = 0; i<s.length(); i++) {
//			if(s.charAt(i) == '(') {
//				count ++;
//			}else if(s.charAt(i) == ')') {
//				-- count;
//				if(count == 0) {
////					i--;
//					s = s.substring(1, i);
//					System.out.println(s);
//					return s;
//				}
//			}
//		}
//		System.out.println(s);
		return s;
	}
}
