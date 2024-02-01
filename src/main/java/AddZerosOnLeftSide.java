
public class AddZerosOnLeftSide {
	public static void main(String[] args) {
		String value = "40"; 
		int i = 6;
		while(value.length() < i) {
			value = "0"+value;
		}
		System.out.println(value); 
		
	}
}
