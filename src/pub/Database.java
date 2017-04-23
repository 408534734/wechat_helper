package pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	
	Connection mysql_connection; //�������ӣ�����������
	Statement mysql_sender; //���ͳ�ȥ�����ݰ�
	ResultSet mysql_receiver; //���ڽ��շ��ͻ��������ݰ�
	
	public Database(String database_name) throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
	    mysql_connection=DriverManager.getConnection(
	    		"jdbc:mysql://" + new Configure().get_ip_address() + "/" + database_name + "?useUnicode=true&characterEncoding=utf-8",
	    		new Configure().get_database_username(),new Configure().get_database_password());//��������
		mysql_sender=mysql_connection.createStatement();//�������ݰ�
		//mysql_receiver=mysql_sender.executeQuery("");//������������շ��ؽ��
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
