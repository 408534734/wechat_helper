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
			new Record_error("XML����������");
		}
		//�ж��ǲ����ı���Ϣ
		if (message.msgtype.equals("text") == false) {
			message.content = "��ֻ��ʶ���ı���Ϣ���޷�ʶ�����Ӵ~";
			return;
		}
		//�ж��ǲ������ŷ�������Ϣ
		if (message.mother.equals(main_mother))
			message.content = new Main_mother_reply(message).reply;
		else {//�������ŷ�������Ϣ
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
