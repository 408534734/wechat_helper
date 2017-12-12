package pub;

public class Link {
	//这里留下了一个BUG，就是用户私自更改链接中的OPENID将导致GG！
	public Link(){
		//空构造函数
	}
	
	public String get_super_manager_link(Database database, String mother, String herf) throws Exception{
		//生成超级管理员管理页面的链接地址
		//链接中的get参数只有一个为password，其组成如下：
		//1.加密的时间，13位
		//2.混淆码，8位
		//3.加密的公众号ID，剩余所有
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get() + new Encrypt().encode(mother);
			database.insert("_super_manager_link", new String[]{"password"}, new String[]{password});
			String link = new Configure().get_web_domain() + "super_manager.jsp?password=" + password;
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("生成超级管理员管理页面出错！");
			throw new Exception();
		}
	}
	
	public String get_manager_link(Database database, String mother, 
			String sheet_name, String herf) throws Exception {
		/*生成管理员管理页面的链接地址
		链接中的get参数有两个：password、sheet_name
		password的组成如下
		1.加密的时间，13位
		2.混淆码，8位
		3.加密的公众号ID，剩余所有
		sheet_name直接为表名
		数据库中的password记录了password+sheet_name*/
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get() + new Encrypt().encode(mother);
			database.insert("_manager_link", new String[]{"password"}, new String[]{password+sheet_name});
			String link = new Configure().get_web_domain() + "manager.jsp?" + 
					"password=" + password +
					"&sheet_name=" + sheet_name;
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("生成管理员管理页面出错！");
			throw new Exception();
		}
	}
	
	public String get_edit_person_link(Database database, String mother, 
			String sheet_name, String openid, String herf) throws Exception {
		/*生成编辑单个用户的信息的网页地址
		链接中的get参数有两个：password、sheet_name、user
		password的组成如下
		1.加密的时间，13位
		2.混淆码，8位
		3.加密的公众号ID，剩余所有
		sheet_name直接为表名
		user为加密的openid
		数据库中的password记录了password+sheet_name+user*/
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get() + new Encrypt().encode(mother);
			database.insert("_edit_person_link", new String[]{"password"}, new String[]{password + sheet_name + openid});
			String link = new Configure().get_web_domain() + "edit_person.jsp?password=" + password +
					"&sheet_name=" + sheet_name + 
					"&user=" + new Encrypt().encode(openid);
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("生成成员编辑页面出错！");
			throw new Exception();
		}
	}
	
	public String get_add_person_identify_code(Database database, String sheet_name) throws Exception {
		//用于生成一个验证码，任何用户向对应的后台发送该验证码即可获得一个能加入登记信息的链接
		//验证码组成：
		//1.识别关键字apid
		//2.加密的时间13位
		//3.混淆码8位
		//ps.这里没有加公众号ID和数据表名是因为在数据库中有记录即可
		//这里记录的数据表名称为_add_person_code
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_add_person_code", new String[]{"password", "sheet_name"}, new String[]{password, sheet_name});
			return "apid" + password;
		} catch (Exception e) {
			new Record_error("生成添加用户验证码出错");
			throw new Exception();
		}
	}
	
	public String get_add_person_link(Database database, String mother, 
			String sheet_name, String openid, String herf) throws Exception {
		/*生成添加单个用户的信息的网页地址
		链接中的get参数有两个：password、sheet_name、user
		password的组成如下
		1.加密的时间，13位
		2.混淆码，8位
		3.加密的公众号ID，剩余所有
		sheet_name直接为表名
		user为加密的openid
		数据库中的password记录了password+sheet_name+user*/
		try {
			String password = new Time().get_encode_time() + new Make_random_string().get() + new Encrypt().encode(mother);
			database.insert("_add_person_link", new String[]{"password"}, new String[]{password + sheet_name + openid});
			String link = new Configure().get_web_domain() + "add_person.jsp?password=" + password +
					"&sheet_name=" + sheet_name + 
					"&user=" + new Encrypt().encode(openid);
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("生成成员添加页面出错！");
			throw new Exception();
		}
	}
	
	public String get_add_person_link(String sheet_name, String herf) throws Exception {
		/*生成由管理员添加虚拟用户的信息的网页地址
		链接中的get参数有一个：sheet_name
		sheet_name直接为表名
		*/
		try {
			String link = new Configure().get_web_domain() + "add_person.jsp?" +
					"user=" + new Time().get_encode_time() + new Make_random_string().get();;
			if (herf.isEmpty()) 
				return link;
			else 
				return "<a href=\"" + link + "\">" + herf + "</a>";
		} catch (Exception e) {
			new Record_error("生成虚拟成员添加页面出错！");
			throw new Exception();
		}
	}
	
	public static void main(String[] args) {
		try {
			Database database = new Database("gh_9f7d8b8c9473");
			/**/System.out.println(new Link().get_super_manager_link(database, "gh_9f7d8b8c9473", ""));
			System.out.println(new Link().get_manager_link(database, "gh_9f7d8b8c9473", "第二届学生会通讯录", ""));
			System.out.println(new Link().get_edit_person_link(database, "gh_9f7d8b8c9473", "第二届学生会通讯录", "ovVwzwIB6qfjDMAaz8PCOLwBz5lk", ""));
			/**/
			/*if (new Link().check_and_clean(database, "_edit_person_link", "496208805215054130303"))
				System.out.println("vaild");
			else System.out.println("invalid");*/
		} catch (Exception e) {
			System.out.println("error!");
		}
		// TODO Auto-generated method stub

	}

}
