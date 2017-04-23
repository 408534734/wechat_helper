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
	
	public void break_connect() {
		try {
			mysql_connection.close();
			mysql_sender.close();
			mysql_sender.close();
		} catch(Exception e) {}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
