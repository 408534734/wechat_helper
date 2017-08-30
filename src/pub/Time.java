package pub;

import java.util.Date;

public class Time {
	
	public Time(){
		//�չ��캯��
	}
	
	private void swap(char a[], int x, int y) {
		//�ڲ���������
		char tmp = a[x];
		a[x] = a[y];
		a[y] = tmp;
	}
	
	public String get_encode_time() {
		//��õ�ǰʱ�䲢����
		String time = String.valueOf(new Date().getTime());
		char mix_time[] = time.toCharArray();
		swap(mix_time, 11, 12);
		swap(mix_time,  0, 10);
		swap(mix_time,  1,  8);
		swap(mix_time,  2,  7);
		swap(mix_time,  3,  9);
		swap(mix_time,  4,  6);
		//����һ�����ܵ�ʱ���ַ���
		return new String(mix_time);
	}
	
	public long decode_time(String time) {
		//�����ܵ�ʱ���ַ��������long
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
		//��ȡ��ǰʱ��
		return new Date().getTime();
	}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
		System.out.println(new Time().get_encode_time());
		System.out.println(new Time().decode_time(new Time().get_encode_time()));

	}

}
