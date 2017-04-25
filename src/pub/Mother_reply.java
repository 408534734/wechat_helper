package pub;
import pub.XML_worker;
import pub.Database;

public class Mother_reply {

	public String reply;
	private Database database;
	private String user_mail;
	private XML_worker message;
	
	//gh_9f7d8b8c9473
	
	public Mother_reply(XML_worker message) {
		this.message = message;
		try {
			database = new Database(this.message.mother);
		}catch (Exception e) {
			reply_sorry("数据库连接出错");
			return;
		}
		
		try {
			if (if_register(this.message.user) == false) {
				if (!register_by_mail())
				if (!register_by_add())
				reply = "如果你的信息已经被公众号管理员添加进了数据库，请输入管理员添加信息中你的邮箱；\n" + 
						"如果没有，请向管理员申请使用注册用专用序列码。\n";
			}
			else {
				if (check_department())
				if (!check_name())
				if (!check_edit())
				if (!check_ai())
				if (!had_register_but_again())
				reply = "我在数据库中没有查询到相关信息，我已经将这条消息记录在了微信后台，管理员会尽快回复你的哟~";
			}
		}catch (Exception e) {
			reply_sorry("if_register出错");
			database.break_connect();
			return;
		}
		database.break_connect();
	}
	
	private boolean if_register(String user) throws Exception{
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
				reply = "该邮箱已经绑定了其他微信用户，如有需要请联系开发者解绑。";
				return true;
			}
			database.select("sheet_list");
			Database database_loop = new Database(this.message.mother);
			while (database.mysql_receiver.next()) {
				database_loop.select(database.mysql_receiver.getString(1), "mail", message.content);
				if (database_loop.mysql_receiver.next()) {
					database_loop.update("user", "default_sheet", database.mysql_receiver.getString(1), "openid", message.user);
					String identify_code = new Make_random_string().get();
					String[] keys = {"identify_code", "openid", "mail"};
					String[] values = {identify_code, message.user, message.content};
					database_loop.insert("identify_code", keys, values);
					String identify_url = "http://sysustudentunion.cn/wechat_helper/identify.jsp?identify_code=" + identify_code;
					new Send_mail(message.content, "这是一封【激活】邮件",
							"这是一封微信公众号通讯录功能【激活】邮件，如果不是您本人进行的操作，请忽略这封邮件。\n" + 
					"如果是您本人的操作，请点击下方的链接进行激活\n" + identify_url);
					reply = "我们已经往该邮箱中发送了一封激活邮件，请点击邮件中的链接完成注册。";
					break;
				}
			}
			database_loop.break_connect();
		}catch(Exception e) {
			this.reply_sorry("register_by_mail_error");
		}
		return true;//TODO
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
	
	private void reply_sorry(String error_type) {
		System.out.println(error_type);
		String error_details = error_type + "\r\n"
							+ "[微信号]" + message.mother + "\r\n"
							+ "[用户] " + message.user + "\r\n"
							+ "[时间]" + message.createtime + "\r\n"
							+ "[消息]" + message.content + "\r\n"
							+ "[类型]" + message.msgtype + "\r\n"
							+ "[原文]" + message.XML_string + "\r\n";
		new Record_error(error_details);
		reply = "很抱歉出现了一个BUG，如果你能将出现BUG的场景截图发送给408534734@qq.com，我将感激不尽！";
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
				   "<FromUserName><![CDATA[ovVwzwNud2B0Ket1cvFIlVxu9Ctc]]></FromUserName>"+
				   "<CreateTime>1471676501</CreateTime>"+
				   "<MsgType><![CDATA[text]]></MsgType>"+
				   "<Content><![CDATA[408534734@qq.com]]></Content>"+
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
