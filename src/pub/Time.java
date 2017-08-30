package pub;

import java.util.Date;

public class Time {
	
	public Time(){
		//空构造函数
	}
	
	private void swap(char a[], int x, int y) {
		//内部交换函数
		char tmp = a[x];
		a[x] = a[y];
		a[y] = tmp;
	}
	
	public String get_encode_time() {
		//获得当前时间并编码
		String time = String.valueOf(new Date().getTime());
		char mix_time[] = time.toCharArray();
		swap(mix_time, 11, 12);
		swap(mix_time,  0, 10);
		swap(mix_time,  1,  8);
		swap(mix_time,  2,  7);
		swap(mix_time,  3,  9);
		swap(mix_time,  4,  6);
		//返回一个加密的时间字符串
		return new String(mix_time);
	}
	
	public long decode_time(String time) {
		//将加密的时间字符串解码成long
		char mix_time[] = time.toCharArray();
		swap(mix_time, 11, 12);
		swap(mix_time,  0, 10);
		swap(mix_time,  1,  8);
		swap(mix_time,  2,  7);
		swap(mix_time,  3,  9);
		swap(mix_time,  4,  6);
		return Long.parseLong(new String(mix_time));
	}
	
	public long get_time() {
		//获取当前时间
		return new Date().getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
		System.out.println(new Time().get_encode_time());
		System.out.println(new Time().decode_time(new Time().get_encode_time()));

	}

}
