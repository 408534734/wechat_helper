package pub;

import java.io.FileWriter;

public class Record_error {

	public Record_error(String error_function) {
		try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter("C:\\0_error_log\\error.txt", true);
            writer.write(error_function + "\r\n");
            writer.close();
        } catch (Exception e) {}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
