package pub;

public class Link {
	
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
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_super_manager_link", new String[]{"password"}, new String[]{password});
			String link = new Configure().get_web_domain() + "super_manager.jsp?password=" + password + new Encrypt().encode(mother);
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
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_manager_link", new String[]{"password"}, new String[]{password+sheet_name});
			String link = new Configure().get_web_domain() + "manager.jsp?password=" + password + new Encrypt().encode(mother) +
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
			String password = new Time().get_encode_time() + new Make_random_string().get();
			database.insert("_edit_person_link", new String[]{"password"}, new String[]{password + sheet_name + openid});
			String link = new Configure().get_web_domain() + "edit_person.jsp?password=" + password + new Encrypt().encode(mother) +
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
	
	public String get_add_person_link(Database database, String mother, 
			String sheet_name, String herf) throws Exception {
		
		
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
