package pub;

public class Make_random_string {

	public Make_random_string() {}
	
	public String get() {
		return String.valueOf((int)(Math.random()*1000000000));//TODO
	}
	
	public static void main(String[] args) {
		System.out.println(new Make_random_string().get());

	}

}
