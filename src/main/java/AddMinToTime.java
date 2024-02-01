
public class AddMinToTime {
	public static void main(String[] args) {
		String timePoint = "07:00"; int min = 2885; //1440
//		sdds
		String[] hm = timePoint.split("\\:");
		int h = Integer.parseInt(hm[0]);
		int m = Integer.parseInt(hm[1]) + min;
		if(m > 59) {
			h += m/60;
			m = m%60;
		}
		int days = 0;
		if(h > 24) {
			days = h/24;
			h = h%24;
		}
		String hh = h + "";
		String mm = m + "";
		if(hh.length() == 1) hh = "0"+hh;
		if(mm.length() == 1) mm = "0"+mm;
		
		
		String s = hh+":"+mm+","+days;
		String[] re = s.split("\\,");
		System.out.println(re[0]);
		System.out.println(re[1]);
	} 
	
}
