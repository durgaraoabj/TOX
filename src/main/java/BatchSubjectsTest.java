import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BatchSubjectsTest {

	public static void main(String[] args) throws ParseException {
//		Map<Integer, List<Integer>> batchInfo = batchWiseSujects(11, 5);  // 11, 5
//		for(Map.Entry<Integer, List<Integer>> mp : batchInfo.entrySet()) {
//			System.out.println("BATCH "+mp.getKey());
//			System.out.println(mp.getValue());
//		}
//		BatchSubjectsTest.subtractTimePointToTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date doseDate = sdf.parse("2019-12-11");
		Date d2 = sdf.parse("2019-12-11");
		System.out.println(daysBetween(doseDate, d2));
	}
	
	public static Map<Integer, List<Integer>> batchWiseSujects(int sujects, int batches){
		Map<Integer, List<Integer>> batchInfo = new HashMap<>();
		int sb = sujects/batches;
		int count = 1;
		if((sb*batches) == sujects) {
			for(int i=1; i<=batches; i++) {
				List<Integer> subs = new ArrayList<>();
				for(int j = 1; j <= sb; j++ ) {
					subs.add(count++);
				}
				batchInfo.put(i, subs);
			}
		}else {
			sb = sb +1;
			for(int i=1; i<=batches; i++) {
				List<Integer> subs = new ArrayList<>();
				for(int j = 1; j <= sb; j++ ) {
					subs.add(count++);
					if(count > sujects) break;
				}
				batchInfo.put(i, subs);
			}
		}
		return batchInfo;
	}
	
	private static void subtractTimePointToTime() throws ParseException {
		String timePoint = "01.30";
		decimalToMin(timePoint);
		long min = 361;
		String doseDate = "2017-10-19"; // yyyy-MM-dd
		String doseTime = "06:00";
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		 
        String currentTime= doseDate + " " + doseTime;

 
        System.out.println("Before subtraction of seconds from date time: "+currentTime);
 
        LocalDateTime datetime = LocalDateTime.parse(currentTime,formatter);
        datetime = datetime.minusMinutes(min);
        String aftersubtraction=datetime.format(formatter);
        System.out.println("After 30 seconds subtraction from date time: "+aftersubtraction);
        String[] s = aftersubtraction.split(" ");
        System.out.println(s[0]);
        System.out.println(s[1]);
	}
	
	private static void decimalToMin(String time) {
		String[] s = time.split("\\.");
		long  hmin = Long.parseLong(s[0])*60;
		hmin += ((Long.parseLong(s[1]) *3)/5);
		
	}
	
	private static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }
}
