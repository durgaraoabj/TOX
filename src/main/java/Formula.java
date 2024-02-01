import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Formula {
	
	public static void main(String[] args) {
		String string = "round(2+round(5*(6/2)+round(123.1234567))+round(10*2))";
		Formula f = new Formula();
		System.out.println(string);
		
		if(string.contains("round(")) {
			string = string.replaceAll("round", "R");
		}
		System.out.println(string);
		while(string.contains("R(")) {
			string = f.recFun(string);
			System.out.println(string);
		}
		System.out.println(string);
		String finalResult = f.caliculate(string);
		System.out.println(finalResult);
	}

	private String recFun(String s) {
		String su = inBracket(s.substring(s.indexOf('R')+1));
		String tempTotal = "R"+su;
		String s1 = "";
		if(su.contains("R(")) {
			s1 = recFun(su);
			if(!s1.contains("R("))
				s1  = caliculate(s1);
		}else
			s1  = caliculate(su);
		s1 = this.round(s1);
		s = s.replace(tempTotal, s1);
		return s;
	}

	private String round(String s) {
		s = s.substring(6, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = (double) Math.round(Double.parseDouble(s));
		}catch (Exception e) {}
		return d+ "";

	}
	private String caliculate(String s) {
		while (s.contains("avg(")) {
			String su = s.substring(s.indexOf("avg("), s.indexOf(")")+1);
			String result = avgof(su);
			s = s.replace(su, result);
			System.out.println(s);
		}
		while (s.contains("sum(")) {
			
			String su = s.substring(s.indexOf("sum("), s.indexOf(")")+1);
			String result = sumof(su);
			s = s.replace(su, result);
			System.out.println(s);
		}
		if(s.contains("(")) {
			String su = inBracket2(s.substring(s.indexOf('(')));
			String tempTotal = "("+su+")";
			if(su.contains("(")) {
				String tempTotal2 = "("+su+")";
				su = caliculate(su);
				s = s.replace(tempTotal2, su);
				
				return this.matFun(s, tempTotal, su);
			}else {
				return this.matFun(s, tempTotal, su);	
				
			}
//			s = s.replace(tempTotal, su);
		}else {
			return this.matFun(s);	
		}
	}
	private String sumof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return d + "";
	}
	private String avgof(String s) {
		s = s.substring(4, s.indexOf(")"));
		String[] ss = s.split(",");
		Double d = 0.0d;
		for(String sss : ss)	d = d + Double.parseDouble(sss);
		return (d/ss.length) + "";
	}
	private String matFun(String s, String tempTotal, String su) {
		 // create a script engine manager
	    ScriptEngineManager factory = new ScriptEngineManager();
	    // create a JavaScript engine
	    ScriptEngine engine = factory.getEngineByName("JavaScript");
	    // evaluate JavaScript code from String
	    Object obj;
		try {
			obj = engine.eval(su);
			System.out.println(obj);
			String r  = obj.toString();
			s = s.replace(tempTotal, r);
			return s;
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
		return s;
	}
	private String matFun(String s) {
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
		return s;
	}
	private String inBracket(String s) {
		int open = 0;
		int close = 0;
//		int count = 0;
		System.out.println(s);
		for(int i = 0; i<s.length(); i++) {
//			System.out.print(s.charAt(i));
			if(s.charAt(i) == '(') {
//				count ++;
				open ++;
			}else if(s.charAt(i) == ')') {
				close ++;
				if(open == close) {
						s = s.substring(0, i+1);
//						System.out.println();
//						System.out.println(s);
						return s;
								
				}
				
			}
		}
		return s;
	}
	private String inBracket2(String s) {
		int open = 0;
		int close = 0;
//		int count = 0;
		System.out.println(s);
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
	
}
