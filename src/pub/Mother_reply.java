package pub;
import pub.XML_worker;
import pub.Database;

public class Mother_reply {

	public String reply;
	private Database database;
	
	public Mother_reply(XML_worker message) {
		
		try {
			database = new Database(message.mother);
		}catch (Exception e) {
			reply_sorry("���ݿ����ӳ���");
			new Record_error("���ݿ����ӳ���");
			return;
		}
		
		if (if_register(message.mother, message.user) == false) {
			if (register_by_name()) return;
			if (register_by_add()) return;
			reply = "�����������Ѿ������ںŹ���Ա��ӽ������ݿ⣬��������ȷ��������\n" + 
					"���û�У��������Ա����ʹ��ע����ר�������롣\n";
		}
		else {
			if (check_department()) return;
			if (check_name()) return;
			if (check_edit()) return;
			if (check_ai()) return;
			if (had_register_but_again()) return;
			reply = "�������ݿ���û�в�ѯ�������Ϣ�����Ѿ���������Ϣ��¼����΢�ź�̨������Ա�ᾡ��ظ����Ӵ~";
		}
		
		try {
			database.break_connect();
		}catch (Exception e) {
			reply += "���ݿ�Ͽ�����ʧ�ܣ�";
			new Record_error("���ݿ�Ͽ�����ʧ��");
		}
	}
	
	private boolean if_register(String mother, String user) {
		//TODO
	}
	
	private boolean register_by_name() {
		//TODO
	}
	
	private boolean register_by_add() {
		//TODO
	}
	
	private boolean had_register_but_again() {
		//TODO
	}
	
	private boolean check_department() {
		//TODO
	}
	
	private boolean check_name() {
		//TODO
	}
	
	private boolean check_edit() {
		//TODO
	}
	
	private boolean check_ai() {
		//TODO
	}
	
	private void reply_sorry(String error_function) {
		new Record_error(error_function);
		reply = "�ܱ�Ǹ������һ��BUG��������ܽ�����BUG�ĳ�����ͼ���͸�408534734@qq.com���ҽ��м�������";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
