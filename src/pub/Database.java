package pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	//�����������ݿ⣬���������ݿⷢ��ָ����صĽ������Result_table��װ�󷵻�
	//�����ִ����ʱ���ڴ�����־�м�¼�������ٴ��׳��쳣����һ���¼��ϸ����
	Connection mysql_connection; //�������ӣ�����������
	Statement mysql_sender; //���ͳ�ȥ�����ݰ�
	ResultSet mysql_receiver; //���ڽ��շ��ͻ��������ݰ�
	
	public Database(String database_name) throws Exception{
	//���캯������ʱ�Ѿ���������ݿ������
		try {
			Configure configure = new Configure();
			Class.forName("com.mysql.jdbc.Driver");
		    mysql_connection=DriverManager.getConnection(
		    		"jdbc:mysql://" + configure.get_ip_address() + "/" + database_name + "?useUnicode=true&characterEncoding=utf-8",
		    		configure.get_database_username(),configure.get_database_password());//��������
			mysql_sender=mysql_connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);//�������ݰ�,�˴��Ĳ����÷��ر��е�ָ������ƶ�
			//mysql_receiver=mysql_sender.executeQuery("");//������������շ��ؽ��
		} catch (Exception e) {
			new Record_error("���ݿ����ӳ���Ҫ���ӵ������ݿ��ǣ�" + database_name);
			throw new Exception();
		}
	}
	
	public void break_connect(){
	//�ڽ���ʹ��ǰһ���ǵöϿ������ݿ������
		try {
			mysql_connection.close();
			mysql_sender.close();
		}catch (Exception e) {
			new Record_error("���ݿ�Ͽ�����ʧ��");
		}
	}
	
	public Result_table select(String sheet_name) throws Exception {
		//������ȡ������
		try {
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT * FROM " + sheet_name);
			throw new Exception();
		}
	}
	
	public Result_table select(String sheet_name, String key, String value) throws Exception {
		//������ȡĳһ������ĳ����Ϣ����ĳ��ֵ������
		try {
			value = this.add_quotes(value);
			//System.out.println("SELECT * FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT * FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			throw new Exception();
		}
	}
	
	public Result_table select(String dest, String sheet_name, String key, String value) throws Exception {
		//������ȡĳ��һ������ĳ����Ϣ����ĳ�����ݵ�ĳһ��ֵ
		try {
			value = this.add_quotes(value);
			//System.out.println("SELECT " + dest + " FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like(String sheet_name, String key, String value) throws Exception {
		//������ȡĳһ������ĳ����Ϣ����ĳ��ֵ������
		try {
			value = this.add_quotes("%" + value + "%");
			//System.out.println("SELECT * FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT * FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like(String dest, String sheet_name, String key, String value) throws Exception {
		//������ȡĳ��һ������ĳ����Ϣ����ĳ�����ݵ�ĳһ��ֵ
		try {
			value = this.add_quotes("%" + value + "%");
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like_in_order(String dest, String sheet_name, String key, String value, String order) throws Exception {
		//������ȡĳ��һ������ĳ����Ϣ����ĳ�����ݵ�ĳһ��ֵ
		try {
			value = this.add_quotes("%" + value + "%");
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value + " order by " + order);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql������" + "SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value + " order by " + order);
			throw new Exception();
		}
	}

	public void update (String sheet_name, String set_key, String set_value, String dest_key, String dest_value) throws Exception {
		try {
			set_value = this.add_quotes(set_value);
			dest_value = this.add_quotes(dest_value);
			//System.out.println("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
			this.mysql_sender.execute("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
		} catch (Exception e) {
			new Record_error("sql������" + "UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
			throw new Exception();
		}
	}
	
	public void insert(String sheet_name, String[] keys, String[] values) throws Exception{
		String keys_string = "(";
		String values_string = "(";
		try {
			for (int i = 0; i < keys.length-1; i++)
				keys_string += keys[i] + ",";
			keys_string += keys[keys.length-1] + ")";
			for (int i = 0; i < values.length-1; i++)
				values_string += "'" + values[i] + "',";
			values_string += "'" + values[values.length-1] + "')";
			//System.out.println("INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
			this.mysql_sender.execute("INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
		} catch (Exception e) {
			new Record_error("sql������" + "INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
			throw new Exception();
		}
	}
	
	public void delete(String sheet_name, String key, String value) throws Exception{
		try {
			value = this.add_quotes(value);
			this.mysql_sender.execute("DELETE FROM " + sheet_name + " WHERE " + key + " = " + value);
		} catch (Exception e) {
			new Record_error("sql������" + "DELETE FROM " + sheet_name + " WHERE " + key + " = " + value);
			throw new Exception();
		}
	}
	
	public void execute(String command) throws Exception {
		try {
			this.mysql_sender.execute(command);
		} catch (Exception e) {
			new Record_error("sql������" + command);
			throw new Exception();
		}
	}
	
	public Result_table get_all_sheet_name(String database_name) throws Exception {
		try {
			database_name = this.add_quotes(database_name);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = " + database_name);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = " + database_name);
			throw new Exception();
		}
	}
	
	private final String add_quotes(String ori) {
		return "'" + ori + "'";
	}
	
	public static void main(String[] args) {
		//test
		try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select_like("����,�ֻ�", "�ڶ���ѧ����ͨѶ¼", "����", "���鴦");
			StringBuilder tmp = new StringBuilder();
			result.department_express(tmp);
			System.out.println(tmp.toString());
			System.out.println("correct!");
		}catch (Exception e) {
			System.out.println("error!");
		}
		
		//����OPENID
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("�ڶ���ѧ����ͨѶ¼");
			for (int i = 0; i < result.num_of_row; i++) {
				Result_table result_detail = database.select("user", "mail", result.data[i][3]);
				if (result_detail.num_of_row != 0) {
					database.update("�ڶ���ѧ����ͨѶ¼", "_openid", result_detail.data[0][0], "mail", result.data[i][3]);
					System.out.println("finish " + i);
				}
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}*/
		//ɾ����OPENID��
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("�ڶ���ѧ����ͨѶ¼");
			for (int i = 0; i < result.num_of_row; i++) {
				if (result.data[i][0]==null) {
					database.delete("�ڶ���ѧ����ͨѶ¼", "mail", result.data[i][3]);
				}
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}*/
		//ɾ���ظ�OPENID
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("�ڶ���ѧ����ͨѶ¼");
			for (int i = 0; i < result.num_of_row; i++) {
				for (int j = i-1; j >= 0; j--) {
					if (result.data[i][0].equals(result.data[j][0])) {
						database.update("�ڶ���ѧ����ͨѶ¼", "����", result.data[j][5]+"��"+result.data[i][5], "_openid", result.data[i][0]);
						System.out.println(result.data[i][1]);
					}
				}
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}*/
	}

}
