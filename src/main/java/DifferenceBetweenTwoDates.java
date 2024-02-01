import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DifferenceBetweenTwoDates {
	public static void main(String[] args) {
		SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
		String inputString1 = "21-05-2021";
		String inputString2 = "26-05-2021";

		try {
			
		    Date date1 = myFormat.parse(inputString1);
		    Date date2 = myFormat.parse(inputString2);
//		    long diff = date2.getTime() - date1.getTime();
//		    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		    
//		    System.out.println(date1.before(date2) );
		    System.out.println(date1.compareTo(date2) );
		} catch (ParseException e) {
		    e.printStackTrace();
		}
	}
}
