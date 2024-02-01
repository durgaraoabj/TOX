import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;


public class FormulaList {
	public static void main(String[] args) {
//		String string = "round(2+round(5*(6/2)+round(123.1234567))+round(10*2))";
		String string = "2+R(2+s(5*(6/2)+C(123.1234567))+R(10*2))";
		
		FormulaList f = new FormulaList();
		string  = f.replaceChars(string);
		string  = f.splitFormaula(string);
	}
	

	private String splitFormaula(String string) {
		int count = 1;
		Map<Integer, String> map = new HashMap<>();
		map.put(count, string);
		int index = 0;
		while(index < string.length()) {
			if(string.charAt(index) == 'R' || string.charAt(index) == 'S' || string.charAt(index) == 'A' || 
					string.charAt(index) == 'P' || string.charAt(index) == 's' || string.charAt(index) == 'C') {
				System.out.println(string.substring(index+2));
				String s = string.substring(index);
				int open = 0;
				int close = 0;
				int endIndex = 0;
				System.out.println(s);
				for(int i = 0; i<s.length(); i++) {
					if(s.charAt(i) == '(') {
//						count ++;
						open ++;
					}else if(s.charAt(i) == ')') {
						close ++;
						if(open == close) {
								s = s.substring(1, i);
								endIndex = i+1;
								break;
						}
					}
				}
				
				String tempTotal = string.charAt(index) + s+")";
				map.put(++count, tempTotal);
				index += endIndex; 
			}else				
				index++;
		}
		
		
		int listSize = 1;
		while(listSize < map.size()) {
			listSize ++;
			map = splitFormaulaList(map, listSize);
			
		}
		
		Map<Integer, String> map2 = new HashMap<>(map);
//		System.out.println(map.size());
//		for(Map.Entry<Integer, String> m : map.entrySet()) {
//			System.out.println(m.getKey() + "  -  " + m.getValue());
//		}
		String result = caliculateValue(map, map2);
		return result;
	}
	
	private String caliculateValue(Map<Integer, String> map, Map<Integer, String> mapt) {
		int index = map.size();
		while(index > 0) {
			String prs0 = mapt.get(index);
			String ps = map.get(index);
			System.out.println(ps);
			String value = findValue(ps);
			map.put(index, value);
			index--;
			if(index > 0) {
				String prs = mapt.get(index);
				if(prs.contains(prs0)) {
					prs = prs.replace(prs0, value);
					map.put(index, prs);
				}
				
				for(Map.Entry<Integer, String> m : mapt.entrySet()) {
					if(prs.contains(m.getValue())) {
						prs = prs.replace(m.getValue(), map.get(m.getKey()));
						map.put(index, prs);
					}
				}
			}
		}
		System.out.println(map.size());
		for(Map.Entry<Integer, String> m : map.entrySet()) {
			System.out.println(m.getKey() + "  -  " + m.getValue());
		}
		return map.get(1);
	}


	private String findValue(String s) {
		 // create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
	    
		while (s.contains("S(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			String result = sumof(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("A(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			String result = avgof(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("P(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			String result = powof(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("s(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			String result = sqrtof(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("C(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			
			String result = cbrtof(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		while (s.contains("R(")) {
			String su = s.substring(2, s.length()-1);
			su = convertToString(su);
			String result = round(su);
			s = s.replace(s, result);
			System.out.println(s);
		}
		Object obj;
		try {
			obj = engine.eval(s);
			 System.out.println( obj );
		    String result =  obj.toString();
			return result;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
	   
	}
	private String simpleCaliculate(String s) {
		String old = s;
		if(s.contains("(")) {
			int open = 0;
			int close = 0;
			System.out.println(s);
			for(int i = 0; i<s.length(); i++) {
				if(s.charAt(i) == '(') {
//					count ++;
					open ++;
				}else if(s.charAt(i) == ')') {
					close ++;
					if(open == close) {
							s = s.substring(s.indexOf('(')+1, i);
							break;
					}
				}
			}
			String s1 = simpleCaliculate(s);
			old = old.replace("("+s+")", s1);
		}else {
			return convertToString(s);
		}
		return old;
	}
	private String convertToString(String s) {
		if(s.contains("(")) {
			s = simpleCaliculate(s);
		}
		// create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
		Object obj;
		try {
			obj = engine.eval(s);
			 System.out.println( obj );
		    String result =  obj.toString();
			return result;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return s;
		}
	}
	
	private String round(String s) {
		Double d = 0.0d;
		try {
			d = (double) Math.round(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";

	}
	
	private String cbrtof(String s) {
		Double d = 0.0d;
		try {
			d = Math.cbrt(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	private String sqrtof(String s) {
		Double d = 0.0d;
		try {
			d = Math.sqrt(Double.parseDouble(s));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	
	private String powof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		try {
			d = Math.pow(Double.parseDouble(ss[0]), Double.parseDouble(ss[0]));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return d+ "";
	}
	
	private String avgof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return (d/ss.length) + "";
	}
	
	private String sumof(String s) {
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return d + "";
	}

	private Map<Integer, String> splitFormaulaList(Map<Integer, String> map, int listSize) {
		int count =map.size();
		String string = map.get(listSize);
		string = string.substring(2, string.length() -1);
		int index = 0;
		while(index < string.length()) {
			if(string.charAt(index) == 'R' || string.charAt(index) == 'S' || string.charAt(index) == 'A' || 
					string.charAt(index) == 'P' || string.charAt(index) == 's' || string.charAt(index) == 'C') {
				String s = string.substring(index);
				int open = 0;
				int close = 0;
				int endIndex = 0;
				for(int i = 0; i<s.length(); i++) {
					if(s.charAt(i) == '(') {
//						count ++;
						open ++;
					}else if(s.charAt(i) == ')') {
						close ++;
						if(open == close) {
								s = s.substring(1, i);
								endIndex = i+1;
								break;
						}
					}
				}
				
				String tempTotal = string.charAt(index) + s+")";
//				System.out.println(tempTotal);
//				System.out.println((count+1)  + "   " + tempTotal);
				if(!map.containsValue(tempTotal)) {
					map.put(map.size()+1, tempTotal);
				}
				String temp = tempTotal.substring(2);
				if(temp.contains("R") || temp.contains("S") || temp.contains("A") ||
						temp.contains("p") || temp.contains("s") || temp.contains("C")) {
					map = splitFormaulaList(map, count);
				}
				index += endIndex; 
			}else				
				index++;
		}
		return map;
	}

	private String inBracket2(String s) {
		int open = 0;
		int close = 0;
//		int count = 0;
//		System.out.println(s);
		for(int i = 0; i<s.length(); i++) {
//			System.out.print(s.charAt(i));
			if(s.charAt(i) == '(') {
//				count ++;
				open ++;
			}else if(s.charAt(i) == ')') {
				close ++;
				if(open == close) {
						s = s.substring(1, i);
//						System.out.println();
//						System.out.println(s);
						
						return s;
								
				}
				
			}
		}
		return s;
	}

	private String replaceChars(String string) {
		System.out.println(string);
		if(string.contains("round(")) {
			string = string.replaceAll("round", "R");
		}
		if(string.contains("sum(")) {
			string = string.replaceAll("sum", "S");
		}
		if(string.contains("avg(")) {
			string = string.replaceAll("avg", "A");
		}
		if(string.contains("pow(")) {
			string = string.replaceAll("pow", "P");
		}
		if(string.contains("sqrt(")) {
			string = string.replaceAll("sqrt", "s");
		}
		if(string.contains("cbrt(")) {
			string = string.replaceAll("cbrt", "C");
		}
		System.out.println(string);
		return string;
	}
}
