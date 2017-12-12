package pub;
import pub.XML_worker;
import pub.Database;
import pub.Valid_time;

//���ݿ�ĵ�һ��һ��Ϊ_openid, ���һλһ��Ϊ_manager

public class Mother_reply {

	private String reply = ""/*, sorry = null*/;
	private Database database;
	private XML_worker message;
	
	//gh_9f7d8b8c9473
	
	public Mother_reply(XML_worker message) {
		this.message = message;
		if (this.message.content.startsWith("spmgpwd")) {
			handle_spmnpwd();//�����Ȩ��
		}
		else {
			//���ӵ����ں�ר�����ݿ�
			try {
				database = new Database(this.message.mother);
			}catch (Exception e) {
				reply = "ϵͳ��δʹ�ó�������Ա��Կ��ʼ���������ݿ����ӳ���";
				return;
			}
			if (this.message.content.equals("���յ���֧�ֵ���Ϣ���ͣ����޷���ʾ��"))
				reply = "��ʱ��֧�ֽ���������ϢŶ��";
			else if (this.message.content.startsWith("apid")) {
				get_add_person_page();
			}
			else if (if_bilibili()) {
				push_bilibili();
			}
			else if (this.message.content.equals("����")) {
				get_manager_page();
			}
			else {
				search_information();
			}
			database.break_connect();
		}
	}
	
	private void handle_spmnpwd() {
		try {
			database = new Database("configure");
			Result_table result = database.select("super_manager_password", "mother", message.mother);
			if (result.num_of_row == 0) {
				result = database.select("super_manager_password", "mother", "");
				for (int i = 0; i < result.num_of_row; i++) {
					Time time_calculator = new Time();
					if (time_calculator.get_time()
							- time_calculator.decode_time(result.data[i][0].substring(7, 20))
							> new Valid_time().super_manager_password()) {
						database.delete("super_manager_password", "password", result.data[i][0]);
						continue;
					}
					if (result.data[i][0].equals(message.content)) {
						database.update("super_manager_password", "mother", message.mother, "password", message.content);
						if (init_mother_database())
							reply = "���ѳɹ���ͨ��΢�ź�̨С���ֵĹ��ܣ�\n�Ժ�ÿ���������Կ���ɽ��볬������Ա��̨��\n" + 
									"���" + new Link().get_super_manager_link(database, message.mother, "������") + 
									"���볬������Ա����ҳ�棨ҳ��30s����Ч��";
						else reply = "��ʼ��ʧ�ܣ������Ի���ϵ������408534734@qq.com";
						return;
					}
				}
				reply = "��Ч�����ѹ��ڵĳ�������Ա��Կ����������Ա��Կ��Чʱ��Ϊ5���ӣ�";
			}
			else {
				if (result.data[0][0].equals(message.content)) {
					database.break_connect();
					database = new Database(message.mother);
					reply = "����" + new Link().get_super_manager_link(database, message.mother, "������") + "���볬������Ա����ҳ��" +
					"�������ӽ���30s��ʧЧ��";
				}
				else reply = "��������ȷ�ĳ�������Ա��Կ";
			}
		}catch (Exception e) {
			reply_sorry("��֤��������Ա��Կ�������ɳ�������Ա����ҳ�����");
		}
	}
	
	private boolean if_bilibili() {
		try {
			Result_table sheets = database.get_all_sheet_name(message.mother);
			for (int i = 0; i < sheets.num_of_row; i++) {
				if (sheets.data[i][0].equals("_bilibili"))
					return true;
			}
			return false;
		} catch (Exception e) {
			reply = "ϵͳ�������GG��";
		}
		
		
		return false;
	}
	
	private void push_bilibili() {
		try {
			if (message.msgtype.equals("subscribe")) {
				reply = "���ڷ��͵�Ļ������ǽŶ��";
				return;
			}
			else if (!message.msgtype.equals("text")) {
				reply = "Ŀǰ���޷�ʵ������������Ϣ��ǽŶ��";
				return;
			}
			database.insert("_bilibili", new String[]{"message"}, new String[]{message.content});
			reply = "��ǽ�ɹ�";
		} catch (Exception e) {
			reply = "ϵͳ�������GG����ǽʧ�ܣ�";
		}
	}
	
	private void get_manager_page() {
		try {
			Result_table sheets = database.get_all_sheet_name(message.mother);
			boolean if_register = false, if_find = false;
			for (int i = 0; i < sheets.num_of_row; i++) {
				if (sheets.data[i][0].startsWith("_"))
					continue;
				Result_table result = database.select(sheets.data[i][0], "_openid", message.user);
				if (result.num_of_row != 0) {
					if_register = true;
					if (result.data[0][result.num_of_column-1] != null 
							&& result.data[0][result.num_of_column-1].equals("��")) {
						if_find = true;
						reply += "���" + new Link().get_manager_link(database, message.mother, sheets.data[i][0], 
								"������") + "�����Ա༭��" + sheets.data[i][0] + "��\n";
					}
				}
			}
			if (if_register == false) {
				reply = "����δ��ϵͳ��ע�ᣬ�������Ա��Ҫע���롣";
			}
			else if (if_find == false) {
				reply = "��Ǹ������û�б���ȨΪ����Ա��";
			}
		} catch (Exception e) {
			new Record_error("�����û�����ԱȨ�޻����ɹ���Աҳ��ʱ����");
		}
	}
	
	private void get_add_person_page() {
		try {
			Result_table sheet = database.select("_add_person_code");
			for (int i = 0; i < sheet.num_of_row; i++) {
				Time time = new Time();
				if (time.get_time() - time.decode_time(message.content.substring(4, 17)) > new Valid_time().add_person()) {
					//��Ч��Ϊһ�죡
					database.delete("_add_person_code", "password", sheet.data[i][0]);
					continue;
				}
				else if (sheet.data[i][0].equals(message.content.substring(4))) {
					reply = "����" + new Link().get_add_person_link(database, message.mother, sheet.data[i][1], message.user, "������") +
							"����ע��";
					return;
				}
			}
			reply = "��֤���ѹ��ڻ���Ч����֤��";
		} catch (Exception e) {
			new Record_error("��������û�����ʱ����");
		}
	}
	
	private void search_information() {
		try {
			if (!message.msgtype.equals("text"))
				reply = "Ŀǰ���޷����շ��ı���ϢŶ��";
			Result_table sheets = database.get_all_sheet_name(message.mother);
			boolean if_register = false, if_find = false;
			StringBuilder collector = new StringBuilder();
			for (int i = 0; i < sheets.num_of_row; i++) {
				if (sheets.data[i][0].startsWith("_"))
					continue;
				Result_table result = database.select("����", sheets.data[i][0], "_openid", message.user);
				if (result.num_of_row != 0) {
					if_register = true;
					//������ѯ
					result = database.select_like(sheets.data[i][0], "����", message.content);
					if (result.num_of_row != 0) {
						if_find = true;
						collector.append("Ϊ����[" + sheets.data[i][0] + "]����[" + message.content + 
								"]��Ϊ�����ؼ����ҵ������½����\n");
						result.person_express(collector, this.database, message.mother, sheets.data[i][0],message.user);
					}
					
					//���Ų�ѯ
					result = database.select_like_in_order("����,����,�ֻ�", sheets.data[i][0], "����", message.content, "_order");
					if (result.num_of_row != 0) {
						if_find = true;
						collector.append("Ϊ����[" + sheets.data[i][0] + "]����[" + message.content + 
								"]��Ϊ���Źؼ����ҵ������½����\n");
						result.department_express(collector);
						collector.append("\n");
					}
				}
			}
			if (if_register == false) {
				reply = "����δ��ϵͳ��ע�ᣬ�������Ա��Ҫע���롣";
			}
			else if (if_find == false) {
				reply = "��Ǹ��δ�����ݿ�����������" + message.content + "������ؼ�¼��";
			}
			else reply = collector.toString();
		} catch (Exception e) {
			new Record_error("�������ݿ����");
		}
	}
	
	private boolean init_mother_database(){
		try {
			database.execute("create database " + message.mother);
			database.break_connect();
			database = new Database(message.mother);
			database.execute("CREATE TABLE `_add_person_code` (" +
							 " `password` varchar(255) DEFAULT NULL," +
							 " `sheet_name` varchar(255) DEFAULT NULL" +
							 ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			database.execute("CREATE TABLE `_add_person_link` (" +
							 " `password` varchar(255) DEFAULT NULL" +
							 " ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			database.execute("CREATE TABLE `_bilibili` (" +
							 " `message_id` varchar(255) NOT NULL," +
							 " `message` varchar(255) DEFAULT NULL" +
							 " ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			database.execute("CREATE TABLE `_edit_person_link` (" +
							 " `password` varchar(255) NOT NULL" +
							 " ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			database.execute("CREATE TABLE `_manager_link` (" +
							 " `password` varchar(255) DEFAULT NULL" +
							 " ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			database.execute("CREATE TABLE `_super_manager_link` (" +
							 " `password` varchar(255) DEFAULT NULL" +
							 " ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
			return true;
		} catch (Exception e) {
			try{
				database.execute("drop database " + message.mother);
			} catch (Exception ee){}
			return false;
		}
		
	}
	
	private void reply_sorry(String error_type) {
		//��ϵͳ���ִ����ʱ��ʹ�ô˹��ܣ����������´���
		//1.���ڿ���̨����������ͱ��ڵ��ԣ����ڿ���ɾ��
		//2.����־�м�¼�������ͺͱ����û�����������Ϣ������
		//3.���û���Ǹ�����Ҳ���֪�û����ִ����ʵ��ϸ��
		System.out.println(error_type);
		String error_details = error_type + "\r\n"
							+ "[΢�ź�]" + message.mother + "\r\n"
							+ "[�û�] " + message.user + "\r\n"
							+ "[ʱ��]" + message.createtime + "\r\n"
							+ "[��Ϣ]" + message.content + "\r\n"
							+ "[����]" + message.msgtype + "\r\n"
							+ "[ԭ��]" + message.XML_string + "\r\n";
		new Record_error(error_details);
		//sorry = "�ܱ�Ǹ������һ��BUG��������ܽ�����BUG�ĳ�����ͼ���͸�408534734@qq.com��������Ա���м�������";
	}
	
	public String get_reply() {
		/*if (sorry == null)*/
			return reply;
		/*else return sorry;*/
	}
	
	private void debug(String tmp) {
		System.out.println(tmp);
	}
	/*private void debug() {
		System.out.println("run to here");
	}*/
	
	public static void main(String[] args) {
		try {
			XML_worker xml = new XML_worker("<xml>"+
				   "<ToUserName><![CDATA[gh_9f7d8b8c9473]]></ToUserName>"+
				   "<FromUserName><![CDATA[ovVwzwIB6qfjDMAaz8PCOLwBz5lk]]></FromUserName>"+
				   "<CreateTime>1471676501</CreateTime>"+
				   "<MsgType><![CDATA[image]]></MsgType>"+
				   "<Content><![CDATA[��������]]></Content>"+
				   "<MsgId>6320802442496304621</MsgId>"+
				   "</xml>");
			// test if_register
			Mother_reply test = new Mother_reply(xml);
			test.debug(test.reply);
			test = new Mother_reply(xml);
			test.debug(test.reply);
			test = new Mother_reply(xml);
			test.debug(test.reply);
		}catch (Exception e) {
			System.out.println("error!");
		}

	}

}
