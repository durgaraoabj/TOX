
public class Test2 {
	public static void main(String[] args) {
		String s = "\r\nDate & Time      : 19.11.2021 12:53:05\r\nBatch No         : BYT65\r\nNozzle No        : 1\r\nGross Wt         : 1.196kg\r\nTare Wt          : 0.198kg\r\nNet Wt           : 0.998kg\r\nStatus           : OK\r\n\r\n";
		System.out.println(s.indexOf("\r\n"));
		
				s = s.replaceAll("\r\n", "___");
		System.out.println(s);
//		String[] ss = s.split("___");
//		for(String o : ss) {ṣ
//			System.out.println(o);ṣ
//		}
	}
}
