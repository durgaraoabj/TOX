import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCompare {
	public static void main(String[] args) {
		try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setLenient(false);

            Date d1 = sdf.parse("11-12-2015");
            Date d2 = sdf.parse("15-12-2015");
            Date d3 = sdf.parse("18-12-2015");

            if (d2.compareTo(d1) >= 0) {
                  if (d2.compareTo(d3) <= 0) {
                         System.out.println("d2 is in between d1 and d3");
                  } else {
                         System.out.println("d2 is NOT in between d1 and d3");
                  }
            } else {
                  System.out.println("d2 is NOT in between d1 and d2");
            }

     } catch (Exception pe) {
            pe.printStackTrace();
     }

	}
}
