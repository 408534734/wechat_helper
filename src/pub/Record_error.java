package pub;

import java.io.FileWriter;

public class Record_error {

	public Record_error(String error_function) {
		try {
            //��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
            FileWriter writer = new FileWriter("C:\\0_error_log\\error.txt", true);
            writer.write(error_function + "\r\n");
            writer.close();
        } catch (Exception e) {}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
