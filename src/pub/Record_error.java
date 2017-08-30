package pub;

import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Record_error {

	public Record_error(String error_function) {
		//在日志文件中记录错误类型
		try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter("C:\\0_error_log\\error.txt", true);
            String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//设置日期格式
            writer.write(date + " : " + error_function + "\r\n\r\n");
            System.out.println(date + ":" + error_function);
            writer.close();
        } catch (Exception e) {}
	}

	public static void main(String[] args) {
		new Record_error("test error fuction");
	}

}
