package pub;
import pub.XML_worker;
import pub.Database;
import pub.Valid_time;

//数据库的第一列一定为_openid, 最后一位一定为_manager

public class Mother_reply {

	private String reply = ""/*, sorry = null*/;
	private Database database;
	private XML_worker message;
	
	//gh_9f7d8b8c9473
	
	public Mother_reply(XML_worker message) {
		this.message = message;
		if (this.message.content.startsWith("spmgpwd")) {
			handle_spmnpwd();//检测特权码
		}
		else {
			//连接到公众号专属数据库
			try {
				database = new Database(this.message.mother);
			}catch (Exception e) {
				reply = "系统尚未使用超级管理员秘钥初始化或者数据库连接出错！";
				return;
			}
			if (this.message.content.equals("【收到不支持的消息类型，暂无法显示】"))
				reply = "暂时不支持接收这种消息哦！";
			else if (this.message.content.startsWith("apid")) {
				get_add_person_page();
			}
			else if (if_bilibili()) {
				push_bilibili();
			}
			else if (this.message.content.equals("管理")) {
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
							reply = "您已成功开通了微信后台小助手的功能！\n以后每次输入该秘钥即可进入超级管理员后台：\n" + 
									"点击" + new Link().get_super_manager_link(database, message.mother, "此链接") + 
									"进入超级管理员管理页面（页面30s内有效）";
						else reply = "初始化失败，请重试或联系开发者408534734@qq.com";
						return;
					}
				}
				reply = "无效或者已过期的超级管理员秘钥（超级管理员秘钥有效时间为5分钟）";
			}
			else {
				if (result.data[0][0].equals(message.content)) {
					database.break_connect();
					database = new Database(message.mother);
					reply = "请点击" + new Link().get_super_manager_link(database, message.mother, "此链接") + "进入超级管理员管理页面" +
					"（此链接将在30s后失效）";
				}
				else reply = "请输入正确的超级管理员秘钥";
			}
		}catch (Exception e) {
			reply_sorry("验证超级管理员秘钥或者生成超级管理员管理页面出错！");
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
			reply = "系统出错并打出GG！";
		}
		
		
		return false;
	}
	
	private void push_bilibili() {
		try {
			if (message.msgtype.equals("subscribe")) {
				reply = "现在发送弹幕即可上墙哦！";
				return;
			}
			else if (!message.msgtype.equals("text")) {
				reply = "目前还无法实现其他类型消息上墙哦！";
				return;
			}
			database.insert("_bilibili", new String[]{"message"}, new String[]{message.content});
			reply = "上墙成功";
		} catch (Exception e) {
			reply = "系统出错并打出GG！上墙失败！";
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
							&& result.data[0][result.num_of_column-1].equals("是")) {
						if_find = true;
						reply += "点击" + new Link().get_manager_link(database, message.mother, sheets.data[i][0], 
								"此链接") + "即可以编辑【" + sheets.data[i][0] + "】\n";
					}
				}
			}
			if (if_register == false) {
				reply = "您还未在系统中注册，请向管理员索要注册码。";
			}
			else if (if_find == false) {
				reply = "抱歉！您还没有被授权为管理员。";
			}
		} catch (Exception e) {
			new Record_error("检索用户管理员权限或生成管理员页面时出错");
		}
	}
	
	private void get_add_person_page() {
		try {
			Result_table sheet = database.select("_add_person_code");
			for (int i = 0; i < sheet.num_of_row; i++) {
				Time time = new Time();
				if (time.get_time() - time.decode_time(message.content.substring(4, 17)) > new Valid_time().add_person()) {
					//有效期为一天！
					database.delete("_add_person_code", "password", sheet.data[i][0]);
					continue;
				}
				else if (sheet.data[i][0].equals(message.content.substring(4))) {
					reply = "请点击" + new Link().get_add_person_link(database, message.mother, sheet.data[i][1], message.user, "此链接") +
							"进行注册";
					return;
				}
			}
			reply = "验证码已过期或无效的验证码";
		} catch (Exception e) {
			new Record_error("生成添加用户链接时出错");
		}
	}
	
	private void search_information() {
		try {
			if (!message.msgtype.equals("text"))
				reply = "目前还无法接收非文本消息哦！";
			Result_table sheets = database.get_all_sheet_name(message.mother);
			boolean if_register = false, if_find = false;
			StringBuilder collector = new StringBuilder();
			for (int i = 0; i < sheets.num_of_row; i++) {
				if (sheets.data[i][0].startsWith("_"))
					continue;
				Result_table result = database.select("姓名", sheets.data[i][0], "_openid", message.user);
				if (result.num_of_row != 0) {
					if_register = true;
					//姓名查询
					result = database.select_like(sheets.data[i][0], "姓名", message.content);
					if (result.num_of_row != 0) {
						if_find = true;
						collector.append("为您在[" + sheets.data[i][0] + "]中以[" + message.content + 
								"]作为姓名关键字找到了如下结果：\n");
						result.person_express(collector, this.database, message.mother, sheets.data[i][0],message.user);
					}
					
					//部门查询
					result = database.select_like_in_order("姓名,部门,手机", sheets.data[i][0], "部门", message.content, "_order");
					if (result.num_of_row != 0) {
						if_find = true;
						collector.append("为您在[" + sheets.data[i][0] + "]中以[" + message.content + 
								"]作为部门关键字找到了如下结果：\n");
						result.department_express(collector);
						collector.append("\n");
					}
				}
			}
			if (if_register == false) {
				reply = "您还未在系统中注册，请向管理员索要注册码。";
			}
			else if (if_find == false) {
				reply = "抱歉！未在数据库中搜索到【" + message.content + "】的相关记录。";
			}
			else reply = collector.toString();
		} catch (Exception e) {
			new Record_error("检索数据库出错！");
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
		//当系统出现错误的时候使用此功能，竟作出以下处理：
		//1.将在控制台输出错误类型便于调试，后期可以删除
		//2.在日志中记录错误类型和本次用户发送来的消息的内容
		//3.向用户道歉，并且不告知用户出现错误的实际细节
		System.out.println(error_type);
		String error_details = error_type + "\r\n"
							+ "[微信号]" + message.mother + "\r\n"
							+ "[用户] " + message.user + "\r\n"
							+ "[时间]" + message.createtime + "\r\n"
							+ "[消息]" + message.content + "\r\n"
							+ "[类型]" + message.msgtype + "\r\n"
							+ "[原文]" + message.XML_string + "\r\n";
		new Record_error(error_details);
		//sorry = "很抱歉出现了一个BUG，如果你能将出现BUG的场景截图发送给408534734@qq.com，开发人员将感激不尽！";
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
				   "<Content><![CDATA[哈哈哈哈]]></Content>"+
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
