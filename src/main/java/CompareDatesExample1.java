import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CompareDatesExample1 {
	public static void main(String[] args) throws ParseException {
//object of SimpleDateFormat class  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//dates to be compare  
		
		Date ne = sdf.parse(sdf.format(new Date()));
		Date old = sdf.parse("2023-06-30");
		System.out.println(ne.compareTo(old));
	}
}