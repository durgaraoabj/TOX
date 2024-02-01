
public class DiffrenceBetweenTwoTimes {
	public static void main(String[] args) {
		String t1 = "06:00";
		String t2 = "05:58";
		String[] t1a = t1.split("\\:");
		String[] t2a = t2.split("\\:");
		
		int dh = Integer.parseInt(t2a[0]) - Integer.parseInt(t1a[0]);
		int dm = Integer.parseInt(t2a[1]) - Integer.parseInt(t1a[1]);
		
		int dff =  ((dh* 60) + dm);
		System.out.println(dff);
	}
}	
