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
		Class.forName("com.mysql.jdbc.Driver");
	    mysql_connection=DriverManager.getConnection(
	    		"jdbc:mysql://" + new Configure().get_ip_address() + "/" + database_name + "?useUnicode=true&characterEncoding=utf-8",
	    		new Configure().get_database_username(),new Configure().get_database_password());//建立连接
		mysql_sender=mysql_connection.createStatement();//定向数据包
		//mysql_receiver=mysql_sender.executeQuery("");//发送命令，并接收返回结果
	}
	
	public void break_connect() throws Exception{
		mysql_connection.close();
		mysql_sender.close();
		mysql_sender.close();
	}
	
	public ResultSet select(String sheet_name) throws Exception {
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name);
		return this.mysql_receiver;
	}
	
	public ResultSet select(String sheet_name, String key, String value) throws Exception {
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
				" WHERE " + key + "=" + value);
		return this.mysql_receiver;
	}
	
	public ResultSet select(String dest, String sheet_name, String key, String value) throws Exception {
		this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
				" WHERE " + key + "=" + value);
		return this.mysql_receiver;
	}
	
	public void update (String sheet_name, String set_key, String set_value, String dest_key, String dest_value) /*throws Exception*/ {
		try {
			if (this.mysql_sender.execute("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value)
					== false);
				System.out.println("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
		}catch (Exception e) {
			System.out.println("UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			Database database1 = new Database("Sysu_Sdcs_Su_Home");
			Database database2 = new Database("Sysu_Sdcs_Su_Home");
			ResultSet result = database1.select("user");
			while (result.next()) {
				System.out.println(result.getString("tel"));
				if ("null".equals(result.getString("tel")) == false) {
					database2.select("2016", "tel", result.getString("tel"));
					database2.update("user", "mail", database2.mysql_receiver.getString("mail"), "tel", result.getString("tel"));
				}
			}
		}catch (Exception e) {
			System.out.println("出错！");
			new Record_error("出错！");
		}
	}

}
