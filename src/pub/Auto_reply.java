package pub;
import pub.Main_mother_reply;
import pub.XML_worker;

public class Auto_reply {
	
	String content;
	XML_worker message;
	
	String main_mother = "";
	public Auto_reply(String post_str) {
		try {
			message = new XML_worker(post_str);
		}catch (Exception e) {
			new Record_error("XML解析器出错");
		}
		//判断是不是文本消息
		if (message.msgtype.equals("text") == false) {
			message.content = "我只能识别文本消息，无法识别表情哟~";
			return;
		}
		//判断是不是主号发来的消息
		if (message.mother.equals(main_mother))
			message.content = new Main_mother_reply(message).reply;
		else {//不是主号发来的消息
			message.content = new Mother_reply(message).reply;
		}
	}
	
	public String get_reply() {
		return message.trans_parameter_to_string();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
