package pub;
import pub.XML_worker;
import pub.Database;

public class Mother_reply {

	public String reply;
	private Database database;
	
	public Mother_reply(XML_worker message) {
		
		try {
			database = new Database(message.mother);
		}catch (Exception e) {
			reply_sorry("数据库连接出错");
			new Record_error("数据库连接出错");
			return;
		}
		
		if (if_register(message.mother, message.user) == false) {
			if (register_by_name()) return;
			if (register_by_add()) return;
			reply = "如果你的姓名已经被公众号管理员添加进了数据库，请输入正确的姓名；\n" + 
					"如果没有，请向管理员申请使用注册用专用序列码。\n";
		}
		else {
			if (check_department()) return;
			if (check_name()) return;
			if (check_edit()) return;
			if (check_ai()) return;
			if (had_register_but_again()) return;
			reply = "我在数据库中没有查询到相关信息，我已经将这条消息记录在了微信后台，管理员会尽快回复你的哟~";
		}
		
		try {
			database.break_connect();
		}catch (Exception e) {
			reply += "数据库断开连接失败！";
			new Record_error("数据库断开连接失败");
		}
	}
	
	private boolean if_register(String mother, String user) {
		//TODO
	}
	
	private boolean register_by_name() {
		//TODO
	}
	
	private boolean register_by_add() {
		//TODO
	}
	
	private boolean had_register_but_again() {
		//TODO
	}
	
	private boolean check_department() {
		//TODO
	}
	
	private boolean check_name() {
		//TODO
	}
	
	private boolean check_edit() {
		//TODO
	}
	
	private boolean check_ai() {
		//TODO
	}
	
	private void reply_sorry(String error_function) {
		new Record_error(error_function);
		reply = "很抱歉出现了一个BUG，如果你能将出现BUG的场景截图发送给408534734@qq.com，我将感激不尽！";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
