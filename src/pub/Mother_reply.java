package pub;
import pub.XML_worker;
import pub.Database;

//���ݿ�ĵ�һ��һ��Ϊ_openid, ���һλһ��Ϊ_manager

public class Mother_reply {

	private String reply = "", sorry = null;
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
				reply_sorry("ϵͳ��δʹ�ó�������Ա��Կ��ʼ���������ݿ����ӳ���");
				return;
			}
			if (if_bilibili()) {
				
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
				result = database.select("super_manager_password", "mother", "");
				for (int i = 0; i < result.num_of_row; i++) {
					Time time_calculator = new Time();
					if (time_calculator.get_time()
							- time_calculator.decode_time(result.data[i][0].substring(7, 20))
							> 5*60*1000) {
						database.delete("super_manager_password", "password", result.data[i][0]);
						continue;
					}
					if (result.data[i][0].equals(message.content)) {
						database.update("super_manager_password", "mother", message.mother, "password", message.content);
						init_mother_database();
						reply = "���ѳɹ���ͨ��΢�ź�̨С���ֵĹ��ܣ�\n�Ժ�ÿ���������Կ���ɽ��볬������Ա��̨��\n" + 
								"���" + new Link().get_super_manager_link(database, message.mother, "") + "���볬������Ա����ҳ�棨ҳ��30��������Ч��";
						return;
					}
				}
				reply = "��Ч�����ѹ��ڵĳ�������Ա��Կ����������Ա��Կ��Чʱ��Ϊ5���ӣ�";
			}
			else {
				if (result.data[0][0].equals(message.content)) {
					database.break_connect();
					database = new Database(message.mother);
					reply = "����" + new Link().get_super_manager_link(database, message.mother, "") + "���볬������Ա����ҳ�棨ҳ��30��������Ч��" +
					"\n�������ӽ���30s��ʧЧ��";
				}
				else reply = "��������ȷ�ĳ�������Ա��Կ";
			}
		}catch (Exception e) {
			reply_sorry("��֤��������Ա��Կ�������ɳ�������Ա����ҳ�����");
		}
	}
	
	private boolean if_bilibili() {
		//�Ƿ�������ǽ����
		//TODO
		return false;
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
							|| result.data[0][result.num_of_column-1].equals("��")) {
						if_find = true;
						reply += "���" + new Link().get_manager_link(database, message.mother, sheets.data[i][0], 
								"�����ӿ��Ա༭��" + sheets.data[i][0] + "��\n");
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
	
	private void search_information() {
		try {
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
					result = database.select_like("����,����,�ֻ�", sheets.data[i][0], "����", message.content);
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
	
	private void init_mother_database() throws Exception {
		//TODO
		//��ʼ��һ�����ݿ��ȫ������������
		database.execute("create database " + message.mother);
	}
	
	/*private boolean if_register(String user) throws Exception{
		database.select("mail", "user", "openid", user);
		if (database.mysql_receiver.next()) {
			this.user_mail = new String(database.mysql_receiver.getString(1) + "");
			if (this.user_mail.isEmpty() || "null".equals(user_mail))
				return false;
			return true;
		}
		else {
			String[] keys = new String[1];
			String[] values = new String[1];
			keys[0] = "openid";
			values[0] = user;
			database.insert("user", keys, values);
			return false;
		}
	}
	
	private boolean register_by_mail() {
		try {
			if (!message.content.contains("@"))
				return false;
			database.select("user", "mail", message.content);
			if (database.mysql_receiver.next()) {
				reply = "�������Ѿ���������΢���û���������Ҫ����ϵ�����߽��";
				return true;
			}
			database.select("sheet_list");
			Database database_loop = new Database(this.message.mother);
			while (database.mysql_receiver.next()) {
				database_loop.select(database.mysql_receiver.getString(1), "mail", message.content);
				if (database_loop.mysql_receiver.next()) {
					database_loop.update("user", "default_sheet", database.mysql_receiver.getString(1), "openid", message.user);
					String identify_code;
					do {
						identify_code = new Make_random_string().get();
						database_loop.select("identify_code", "identify_code", identify_code);
					}while (database_loop.mysql_receiver.next());
					String[] keys = {"identify_code", "openid", "mail"};
					String[] values = {identify_code, message.user, message.content};
					database_loop.insert("identify_code", keys, values);
					String identify_url = "http://sysustudentunion.cn/wechat_helper/identify.jsp?identify_code=" + identify_code;
					new Send_mail(message.content, "����һ�⡾����ʼ�",
							"    ����һ��΢�Ź��ں�ͨѶ¼���ܡ�����ʼ���������������˽��еĲ��������������ʼ���\n" + 
					"����������˵Ĳ����������·������ӽ��м���\n" + identify_url);
					reply = "�����Ѿ����������з�����һ�⼤���ʼ��������ʼ��е��������ע�ᡣ";
					database_loop.break_connect();
					return true;
				}
			}
			database_loop.break_connect();
			return false;
		}catch(Exception e) {
			this.reply_sorry("register_by_mail_error");
			return true;
		}
	}
	
	private boolean register_by_add() {
		return false;//TODO
	}
	
	private boolean had_register_but_again() {
		return false;//TODO
	}
	
	private boolean check_department() {
		return false;//TODO
	}
	
	private boolean check_name() {
		return false;//TODO
	}
	
	private boolean check_edit() {
		return false;//TODO
	}
	
	private boolean check_ai() {
		return false;//TODO
	}
	*/
	
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
		sorry = "�ܱ�Ǹ������һ��BUG��������ܽ�����BUG�ĳ�����ͼ���͸�408534734@qq.com��������Ա���м�������";
	}
	
	public String get_reply() {
		if (sorry == null)
			return reply;
		else return sorry;
	}
	
	private void debug(String tmp) {
		System.out.println(tmp);
	}
	private void debug() {
		System.out.println("run to here");
	}
	
	public static void main(String[] args) {
		try {
			XML_worker xml = new XML_worker("<xml>"+
				   "<ToUserName><![CDATA[gh_9f7d8b8c9473]]></ToUserName>"+
				   "<FromUserName><![CDATA[ovVwzwIB6qfjDMAaz8PCOLwBz5lk]]></FromUserName>"+
				   "<CreateTime>1471676501</CreateTime>"+
				   "<MsgType><![CDATA[text]]></MsgType>"+
				   "<Content><![CDATA[����]]></Content>"+
				   "<MsgId>6320802442496304621</MsgId>"+
				   "</xml>");
			// test if_register
			Mother_reply test = new Mother_reply(xml);
			test.debug(test.reply);
		}catch (Exception e) {
			System.out.println("error!");
		}

	}

}
