import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Test22 {
	public static void main(String[] args) throws ParserConfigurationException {
		System.out.println(Math.round(4567.9874f));
		String s = "round(23.62444749276025)";
		int r = s.indexOf("round(");
		System.out.println(r);
		
		
		String temp = s.substring(s.indexOf("round("));
		String temp2 = temp.substring(0, temp.indexOf(")")+1);
		String result = sqrtof(temp2);
		s = s.replace(temp2, result);
		System.out.println(s);
	}

	private static String sqrtof(String s) {
		s = s.substring(6, s.indexOf(")"));
		Double d = 0.0d;
		try {
			d = (double) Math.round(Double.parseDouble(s));
		}catch (Exception e) {}
		return d+ "";
	}
}
