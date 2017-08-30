package pub;
import pub.Database;

public class Editor {
	
	public Database database;
	public int previlege;
	/* previlege���ڴ��浱ǰ�û���Ȩ��
	 * �����µȼ���
	 * 1����������Ա�����Բٿ�����
	 * 2����ͨ����Ա�����Բٿ������ڵı�
	 * 3����ͨ��Ա��ֻ�����޸��Լ�����Ϣ
	 * 4��δͨ����֤��Ĭ��״̬
	 */
	public String mother, sheet, openid, password;
	//public String super_manager_link, manager_link, edit_person_link;
	public boolean verified, database_valid;
	
	public Editor() {
		previlege = 4;
		verified = false;
		database = null;
		database_valid = false;
	}
	
	public boolean check_password(int previlege, 
			String mother, String sheet, String openid, String password) {
		//�鿴���ݿ����Ƿ���Ч
		try {
			database = new Database(mother);
		} catch (Exception e) {
			database = null;
			return false;
		}
		//���password�Ƿ���Ч��ͬʱɾ�����ݿ��е���Ч����
		//password��Чʱ��Ϊ30����
		String type = null;
		if (previlege == 3)
			type = "_edit_person_link";
		else if (previlege == 2)
			type = "_manager_link";
		else type = "_super_manager_link";
		try {
			Result_table result = database.select(type);
			password = password + sheet + openid;
			for (int i = result.num_of_row-1; i >=0; i--) {
				Time time_calculator = new Time();
				if (time_calculator.get_time()
						- time_calculator.decode_time(result.data[i][0].substring(0, 13))
						> 3000*60*1000) {
					database.delete(type, "password", result.data[i][0]);
					continue;
				}
				if (result.data[i][0].equals(password)) {
					pass_verification(previlege, mother, sheet, openid, password);
					return true;
				}
			}
			database.break_connect();
			return false;
		} catch (Exception e) {
			database.break_connect();
			return false;
		}
	}
	
	private void pass_verification(int previlege, 
			String mother, String sheet, String openid, String password) {
		verified = true;
		this.previlege = previlege;
		this.mother = mother;
		this.sheet = sheet;
		this.openid = openid;
		this.password = password;
		this.database_valid = true;
		/*if (previlege == 1) {
			this.super_manager_link = new Configure().get_web_domain() + 
					"super_manager.jsp?password=" + password + new Encrypt().encode(mother);
		}
		if (previlege == 2) {
			this.manager_link = new Configure().get_web_domain() + 
					"manager.jsp?password=" + password + new Encrypt().encode(mother) +
					"&sheet_name=" + sheet;
		}
		if (previlege == 3) {
			this.edit_person_link = new Configure().get_web_domain() + 
					"edit_person.jsp?password=" + password + new Encrypt().encode(mother) +
					"&sheet_name=" + sheet + 
					"&user=" + new Encrypt().encode(openid);
		}*/
	}
	
	public void break_connection() {
		database.break_connect();
		database_valid = false;
	}
	
	public void build_connection() 
	{
		try {
			database = new Database(mother);
			database_valid = true;
		}catch (Exception e) {
			new Record_error("����Editor.build_connection() mother="+mother);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
