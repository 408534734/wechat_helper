package pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	
	Connection mysql_connection; //建立连接，这是连接名
	Statement mysql_sender; //发送出去的数据包
	ResultSet mysql_receiver; //用于接收发送回来的数据包
	
	public Database(String database_name) throws Exception{
		Configure configure = new Configure();
		Class.forName("com.mysql.jdbc.Driver");
	    mysql_connection=DriverManager.getConnection(
	    		"jdbc:mysql://" + configure.get_ip_address() + "/" + database_name + "?useUnicode=true&characterEncoding=utf-8",
	    		configure.get_database_username(),configure.get_database_password());//建立连接
		mysql_sender=mysql_connection.createStatement();//定向数据包
		//mysql_receiver=mysql_sender.executeQuery("");//发送命令，并接收返回结果
	}
	
	public void break_connect(){
		try {
			mysql_connection.close();
			mysql_sender.close();
			mysql_sender.close();

		}catch (Exception e) {
			new Record_error("数据库断开连接失败");
		}
	}
	
	public ResultSet select(String sheet_name) throws Exception {
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name);
		return this.mysql_receiver;
	}
	
	public ResultSet select(String sheet_name, String key, String value) throws Exception {
		value = this.add_quotes(value);
		//System.out.println("SELECT * FROM " + sheet_name + " WHERE " + key + "=" + value);
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
				" WHERE " + key + "=" + value);
		return this.mysql_receiver;
	}
	
	public ResultSet select(String dest, String sheet_name, String key, String value) throws Exception {
		value = this.add_quotes(value);
		//System.out.println("SELECT " + dest + " FROM " + sheet_name + " WHERE " + key + "=" + value);
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
				" WHERE " + key + "=" + value);
		return this.mysql_receiver;
	}
	
	public void update (String sheet_name, String set_key, String set_value, String dest_key, String dest_value) throws Exception {
		set_value = this.add_quotes(set_value);
		dest_value = this.add_quotes(dest_value);
		//System.out.println("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
		this.mysql_sender.execute("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
	}
	
	public void insert(String sheet_name, String[] keys, String[] values) throws Exception{
		String keys_string = "(";
		for (int i = 0; i < keys.length-1; i++)
			keys_string += keys[i] + ",";
		keys_string += keys[keys.length-1] + ")";
		String values_string = "(";
		for (int i = 0; i < values.length-1; i++)
			values_string += "'" + values[i] + "',";
		values_string += "'" + values[values.length-1] + "')";
		//System.out.println("INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
		this.mysql_sender.execute("INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
	}
	
	private final String add_quotes(String ori) {
		return "'" + ori + "'";
	}
	
	public static void main(String[] args) {
		//add mail details for all
		/*try {
			Database database1 = new Database("Sysu_Sdcs_Su_Home");
			Database database2 = new Database("Sysu_Sdcs_Su_Home");
			database1.select("user");
			database2.select("第2016届");
			while (database1.mysql_receiver.next()) {
				if ("null".equals("" + database1.mysql_receiver.getString("tel")) == false) {
					System.out.println(database1.mysql_receiver.getString("tel"));
					database2.select("第2016届", "tel", database1.mysql_receiver.getString("tel"));
					database2.mysql_receiver.next();
					System.out.println(database2.mysql_receiver.getString("mail"));
					database2.update("user", "mail", database2.mysql_receiver.getString("mail"), "tel", database1.mysql_receiver.getString("tel"));
				}
			}
			database1.break_connect();
			database2.break_connect();
		}catch (Exception e) {
			System.out.println("出错！");
			new Record_error("出错！");
		}*/
		// add default sheet for all
		/*try {
			Database database1 = new Database("Sysu_Sdcs_Su_Home");
			Database database2 = new Database("Sysu_Sdcs_Su_Home");
			database1.select("user");
			database2.select("第2016届");
			while (database1.mysql_receiver.next()) {
				if ("null".equals("" + database1.mysql_receiver.getString("tel")) == false) {
					System.out.println(database1.mysql_receiver.getString("tel"));
					database2.update("user", "default_sheet", "第2016届", "tel", database1.mysql_receiver.getString("tel"));
				}
			}
			database1.break_connect();
			database2.break_connect();
		}catch (Exception e) {
			System.out.println("出错！");
			new Record_error("出错！");
		}*/
		//test insert function
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			String[] keys = new String[2];
			String[] values = new String[2];
			keys[0] = "word";
			keys[1] = "reply";
			values[0] = "test_word";
			values[1] = "test_reply";
			database.insert("ai", keys, values);
		} catch (Exception e) {
			System.out.println("error!");
		}*/
	}

}
