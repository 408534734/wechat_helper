package pub;
import pub.XML_worker;
import pub.Database;

public class Mother_reply {

	public String reply;
	private Database database;
	private String user_mail;
	
	//gh_9f7d8b8c9473
	
	public Mother_reply(XML_worker message) {
		
		try {
			database = new Database(message.mother);
		}catch (Exception e) {
			reply_sorry("数据库连接出错", message);
			return;
		}
		
		try {
			if (if_register(message.user) == false) {
				if (!register_by_name())
				if (!register_by_add())
				reply = "如果你的姓名已经被公众号管理员添加进了数据库，请输入正确的姓名；\n" + 
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
			reply_sorry("if_register出错", message);
			database.break_connect();
			return;
		}
		database.break_connect();
	}
	
	private boolean if_register(String user) throws Exception{
		database.select("mail", "user", "openid", user);
		if (database.mysql_receiver.next()) {
			this.user_mail = new String(database.mysql_receiver.getString(1));
			if (this.user_mail.isEmpty())
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
	
	private boolean register_by_name() {
		return false;//TODO
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
	
	private void reply_sorry(String error_type, XML_worker message) {
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
	
	public static void main(String[] args) {
		try {
			XML_worker xml = new XML_worker("<xml>"+
				   "<ToUserName><![CDATA[gh_9f7d8b8c9473]]></ToUserName>"+
				   "<FromUserName><![CDATA[ovVwzwOM9orSh3Qse7mwKjWgIbmA]]></FromUserName>"+
				   "<CreateTime>1471676501</CreateTime>"+
				   "<MsgType><![CDATA[text]]></MsgType>"+
				   "<Content><![CDATA[邦哥]]></Content>"+
				   "<MsgId>6320802442496304621</MsgId>"+
				   "</xml>");
			// test if_register
			Mother_reply test = new Mother_reply(xml);
		}catch (Exception e) {
			System.out.println("error!");
		}

	}

}
