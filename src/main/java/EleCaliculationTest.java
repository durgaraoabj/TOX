
public class EleCaliculationTest {
	public static void main(String[] args) {
		String s = "round(5";
		System.out.println(s.substring(s.lastIndexOf("(")));
	}
	
	public static void onlyElementes() {
		String value = "((ELE[e1],,ELE[e2],,ELE[e3],,)/ELE[e4],),*ELE[e5],";	
		StringBuilder sb = new StringBuilder();
		sb.append("");
		boolean flag = true;
		String[] ss = value.split("],");
		for(String e : ss) {
			System.out.println(e);
			if(e.contains("ELE[")) {
				String se =  e.substring(e.lastIndexOf("[")+1);
				System.out.println(se);
				if(!se.equals("")) {
					if(flag) {
						sb.append(se); flag  = false;
					}else sb.append(",").append(se);
				}
			}
		}
		System.out.println(sb.toString());
	}
}
