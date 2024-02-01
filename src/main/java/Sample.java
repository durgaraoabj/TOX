
public class Sample {
	public static void main(String[] args) {
		String string = "2+R(2+s(5*(6/2)+C(123.1234567))+R(10*2))";
		
		int index = 0;
		while(index < string.length()) {
			if(string.charAt(index) == 'R' || string.charAt(index) == 'S' || string.charAt(index) == 'A' || 
					string.charAt(index) == 'P' || string.charAt(index) == 's' || string.charAt(index) == 'C') {
				System.out.println(string.substring(index+1));
			}				
				index++;
		}
	}
}
