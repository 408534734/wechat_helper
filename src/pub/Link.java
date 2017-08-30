package pub;

public class Link {
	
	public Link(){
		//�չ��캯��
	}
	
	public String get_super_manager_link(Database database, String mother, String herf) throws Exception{
		//���ɳ�������Ա����ҳ������ӵ�ַ
		//�����е�get����ֻ��һ��Ϊpassword����������£�
		//1.���ܵ�ʱ�䣬13λ
		//2.�����룬8λ
		//3.���ܵĹ��ں�ID��ʣ������
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_super_manager_link", new String[]{"password"}, new String[]{password});
			String link = new Configure().get_web_domain() + "super_manager.jsp?password=" + password + new Encrypt().encode(mother);
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("���ɳ�������Ա����ҳ�����");
			throw new Exception();
		}
	}
	
	public String get_manager_link(Database database, String mother, 
			String sheet_name, String herf) throws Exception {
		/*���ɹ���Ա����ҳ������ӵ�ַ
		�����е�get������������password��sheet_name
		password���������
		1.���ܵ�ʱ�䣬13λ
		2.�����룬8λ
		3.���ܵĹ��ں�ID��ʣ������
		sheet_nameֱ��Ϊ����
		���ݿ��е�password��¼��password+sheet_name*/
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_manager_link", new String[]{"password"}, new String[]{password+sheet_name});
			String link = new Configure().get_web_domain() + "manager.jsp?password=" + password + new Encrypt().encode(mother) +
					"&sheet_name=" + sheet_name;
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("���ɹ���Ա����ҳ�����");
			throw new Exception();
		}
	}
	
	public String get_edit_person_link(Database database, String mother, 
			String sheet_name, String openid, String herf) throws Exception {
		/*���ɱ༭�����û�����Ϣ����ҳ��ַ
		�����е�get������������password��sheet_name��user
		password���������
		1.���ܵ�ʱ�䣬13λ
		2.�����룬8λ
		3.���ܵĹ��ں�ID��ʣ������
		sheet_nameֱ��Ϊ����
		userΪ���ܵ�openid
		���ݿ��е�password��¼��password+sheet_name+user*/
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_edit_person_link", new String[]{"password"}, new String[]{password + sheet_name + openid});
			String link = new Configure().get_web_domain() + "edit_person.jsp?password=" + password + new Encrypt().encode(mother) +
					"&sheet_name=" + sheet_name + 
					"&user=" + new Encrypt().encode(openid);
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("���ɳ�Ա�༭ҳ�����");
			throw new Exception();
		}
	}
	
	public String get_add_person_link(Database database, String mother, 
			String sheet_name, String herf) throws Exception {
		
		
	}
	
	public static void main(String[] args) {
		try {
			Database database = new Database("gh_9f7d8b8c9473");
			/**/System.out.println(new Link().get_super_manager_link(database, "gh_9f7d8b8c9473", ""));
			System.out.println(new Link().get_manager_link(database, "gh_9f7d8b8c9473", "�ڶ���ѧ����ͨѶ¼", ""));
			System.out.println(new Link().get_edit_person_link(database, "gh_9f7d8b8c9473", "�ڶ���ѧ����ͨѶ¼", "ovVwzwIB6qfjDMAaz8PCOLwBz5lk", ""));
			/**/
			/*if (new Link().check_and_clean(database, "_edit_person_link", "496208805215054130303"))
				System.out.println("vaild");
			else System.out.println("invalid");*/
		} catch (Exception e) {
			System.out.println("error!");
		}
		// TODO Auto-generated method stub

	}

}
