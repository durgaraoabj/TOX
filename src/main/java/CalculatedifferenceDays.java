import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class CalculatedifferenceDays {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int daysdiff = 0;
		try {
			Date curDate = new Date();
			Date d1 = sdf.parse("2022-9-19 00:00:00");
			Date d2 = sdf.parse("2022-9-19 00:00:00");
			
			Calendar c = Calendar.getInstance(); 
			c.setTime(new Date());
			c.add(Calendar.DATE, 1);
			System.out.println("calculated Date :"+c.getTime());
			long difference = d1.getTime() - d2.getTime();
			int days = (int) (difference / (1000 * 60 * 60 * 24));

	        System.out.println("differ days is :"+days);
	        
	        if(-(Integer.parseInt("3")) == -3)
	        	System.out.println("true");
	        else
	        	System.out.println("condition false");
	        
	        if(curDate.after(d1) && (curDate.before(d2)))
	        	System.out.println("in between");
	        else System.out.println("not in betweeen");
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
