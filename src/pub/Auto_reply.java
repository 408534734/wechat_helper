package pub;
import pub.Main_mother_reply;
import pub.XML_worker;

public class Auto_reply {
	//用于与message.jsp相连接，处理微信服务器发送来的XML数据，并且通过get_reply返回XML字符串
	//Main_mother_reply和Mother_reply模块处理好要回复的内容后均将要回复的字符串存入其模块的reply变量中
	String content;
	XML_worker message;
	
	String main_mother = "unknown";
	public Auto_reply(String post_str) {
		try {
			message = new XML_worker(post_str);
		}catch (Exception e) {
			new Record_error("XML解析器出错，文本内容如下：/n" + post_str);
		}
		//判断是不是文本消息
		if (message.msgtype.equals("text") == false) {
			message.content = "目前我还只能识别文本消息，无法识别其他消息哟~";
			return;
		}
		//判断是不是主号发来的消息
		if (message.mother.equals(main_mother))
			message.content = new Main_mother_reply(message).reply;
		else {//不是主号发来的消息
			message.content = new Mother_reply(message).get_reply();
		}
	}
	
	public String get_reply() {
		return message.trans_parameter_to_string();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
