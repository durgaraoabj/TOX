import java.util.Date;

public class DateAndTimeFromDate {
	public static void main(String[] args) {
		Date date = new Date();
		System.out.println(date.getHours());
		System.out.println(date.getMinutes());
		String h = ""+ date.getHours();
		String m = "" + date.getMinutes();
		if(h.length() == 1) h = "0"+h;
		if(m.length() == 1) m = "0"+m;
	}
}
