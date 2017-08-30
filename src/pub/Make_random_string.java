package pub;

public class Make_random_string {

	public Make_random_string() {}
	
	public String get() {
		//获得一个随机字符串，由纯数字组成，共8位
		String random_string;
		do {
			long random_num = (long)(Math.random()*1000000000);
			random_num %= 100000000;
			random_string = String.valueOf(random_num);
		}while (random_string.length() != 8);
		return random_string;
	}
	
	public static void main(String[] args) {
		for (int i = 1; i < 100; i++)
			System.out.println(String.valueOf(i) + new Make_random_string().get());
	}

}
