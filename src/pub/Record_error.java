package pub;

import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Record_error {

	public Record_error(String error_function) {
		//����־�ļ��м�¼��������
		try {
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter("C:\\0_error_log\\error.txt", true);
            String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//�������ڸ�ʽ
            writer.write(date + " : " + error_function + "\r\n\r\n");
            System.out.println(date + ":" + error_function);
            writer.close();
        } catch (Exception e) {}
	}

	public static void main(String[] args) {
		new Record_error("test error fuction");
	}

}
