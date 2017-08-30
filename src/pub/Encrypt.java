package pub;

public class Encrypt {
	
	public Encrypt() {
		//¿Õ¹¹Ôìº¯Êý
	}
	
	public String encode(String ori) {
		char tmp[] = ori.toCharArray();
		for (int i = ori.length()-1; i >= 0; i--) {
			if ('0' <= tmp[i] && tmp[i] <='4')
				tmp[i] += 5;
			else if ('5' <= tmp[i] && tmp[i] <= '9')
				tmp[i] -= 5;
			else if ('a' <= tmp[i] && tmp[i] <= 'm')
				tmp[i] += 13;
			else if ('n' <= tmp[i] && tmp[i] <= 'z')
				tmp[i] -= 13;
		}
		return new String(tmp);
	}
	
	public String decode(String obj) {
		char tmp[] = obj.toCharArray();
		for (int i = obj.length()-1; i >= 0; i--) {
			if ('0' <= tmp[i] && tmp[i] <='4')
				tmp[i] += 5;
			else if ('5' <= tmp[i] && tmp[i] <= '9')
				tmp[i] -= 5;
			else if ('a' <= tmp[i] && tmp[i] <= 'm')
				tmp[i] += 13;
			else if ('n' <= tmp[i] && tmp[i] <= 'z')
				tmp[i] -= 13;
		}
		return new String(tmp);
	}
	
	public static void main(String[] args) {
		System.out.println(new Encrypt().encode("gh_9f7d8b8c9473"));
		System.out.println(new Encrypt().encode(new Encrypt().encode("gh_9f7d8b8c9473")));
		// TODO Auto-generated method stub

	}

}
