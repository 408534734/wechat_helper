package pub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {
	//用于连接数据库，并且向数据库发出指令，返回的结果均用Result_table封装后返回
	//当出现错误的时候将在错误日志中记录，并且再次抛出异常给上一层记录详细错误
	Connection mysql_connection; //建立连接，这是连接名
	Statement mysql_sender; //发送出去的数据包
	ResultSet mysql_receiver; //用于接收发送回来的数据包
	
	public Database(String database_name) throws Exception{
	//构造函数，此时已经完成与数据库的连接
		try {
			Configure configure = new Configure();
			Class.forName("com.mysql.jdbc.Driver");
		    mysql_connection=DriverManager.getConnection(
		    		"jdbc:mysql://" + configure.get_ip_address() + "/" + database_name + "?useUnicode=true&characterEncoding=utf-8",
		    		configure.get_database_username(),configure.get_database_password());//建立连接
			mysql_sender=mysql_connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);//定向数据包,此处的参数让返回表中的指针可以移动
			//mysql_receiver=mysql_sender.executeQuery("");//发送命令，并接收返回结果
		} catch (Exception e) {
			new Record_error("数据库连接出错，要连接到的数据库是：" + database_name);
			throw new Exception();
		}
	}
	
	public void break_connect(){
	//在结束使用前一定记得断开与数据库的连接
		try {
			mysql_connection.close();
			mysql_sender.close();
		}catch (Exception e) {
			new Record_error("数据库断开连接失败");
		}
	}
	
	public Result_table select(String sheet_name) throws Exception {
		//用于提取整个表
		try {
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT * FROM " + sheet_name);
			throw new Exception();
		}
	}
	
	public Result_table select(String sheet_name, String key, String value) throws Exception {
		//用于提取某一个表中某个信息等于某个值的内容
		try {
			value = this.add_quotes(value);
			//System.out.println("SELECT * FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT * FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			throw new Exception();
		}
	}
	
	public Result_table select(String dest, String sheet_name, String key, String value) throws Exception {
		//用于提取某个一个表中某个信息等于某个内容的某一项值
		try {
			value = this.add_quotes(value);
			//System.out.println("SELECT " + dest + " FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + "=" + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like(String sheet_name, String key, String value) throws Exception {
		//用于提取某一个表中某个信息等于某个值的内容
		try {
			value = this.add_quotes("%" + value + "%");
			//System.out.println("SELECT * FROM " + sheet_name + " WHERE " + key + "=" + value);
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT * FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT * FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like(String dest, String sheet_name, String key, String value) throws Exception {
		//用于提取某个一个表中某个信息包含某个内容的某一项值
		try {
			value = this.add_quotes("%" + value + "%");
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value);
			throw new Exception();
		}
	}
	
	public Result_table select_like_in_order(String dest, String sheet_name, String key, String value, String order) throws Exception {
		//用于提取某个一个表中某个信息包含某个内容的某一项值
		try {
			value = this.add_quotes("%" + value + "%");
			this.mysql_receiver = this.mysql_sender.executeQuery("SELECT " + dest + " FROM " + sheet_name + 
					" WHERE " + key + " like " + value + " order by " + order);
			return new Result_table(this.mysql_receiver);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "SELECT " + dest + " FROM " + sheet_name + 
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
			new Record_error("sql语句出错：" + "UPDATE " + sheet_name + " set " + set_key + " = " + set_value + " WHERE " + dest_key + " = " + dest_value);
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
			new Record_error("sql语句出错：" + "INSERT INTO " + sheet_name + " " + keys_string + " VALUES " + values_string);
			throw new Exception();
		}
	}
	
	public void delete(String sheet_name, String key, String value) throws Exception{
		try {
			value = this.add_quotes(value);
			this.mysql_sender.execute("DELETE FROM " + sheet_name + " WHERE " + key + " = " + value);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + "DELETE FROM " + sheet_name + " WHERE " + key + " = " + value);
			throw new Exception();
		}
	}
	
	public void execute(String command) throws Exception {
		try {
			this.mysql_sender.execute(command);
		} catch (Exception e) {
			new Record_error("sql语句出错：" + command);
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
			Result_table result = database.select_like("姓名,手机", "第二届学生会通讯录", "部门", "秘书处");
			StringBuilder tmp = new StringBuilder();
			result.department_express(tmp);
			System.out.println(tmp.toString());
			System.out.println("correct!");
		}catch (Exception e) {
			System.out.println("error!");
		}
		
		//加上OPENID
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("第二届学生会通讯录");
			for (int i = 0; i < result.num_of_row; i++) {
				Result_table result_detail = database.select("user", "mail", result.data[i][3]);
				if (result_detail.num_of_row != 0) {
					database.update("第二届学生会通讯录", "_openid", result_detail.data[0][0], "mail", result.data[i][3]);
					System.out.println("finish " + i);
				}
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}*/
		//删除无OPENID的
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("第二届学生会通讯录");
			for (int i = 0; i < result.num_of_row; i++) {
				if (result.data[i][0]==null) {
					database.delete("第二届学生会通讯录", "mail", result.data[i][3]);
				}
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}*/
		//删除重复OPENID
		/*try {
			Database database = new Database("gh_9f7d8b8c9473");
			Result_table result = database.select("第二届学生会通讯录");
			for (int i = 0; i < result.num_of_row; i++) {
				for (int j = i-1; j >= 0; j--) {
					if (result.data[i][0].equals(result.data[j][0])) {
						database.update("第二届学生会通讯录", "部门", result.data[j][5]+"、"+result.data[i][5], "_openid", result.data[i][0]);
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
