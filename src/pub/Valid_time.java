package pub;

public class Valid_time {
	
	public Valid_time() {}
	
	public long add_person() {
		//添加用户码有效期一天
		return 24*60*60*1000;
	}
	
	public long super_manager_password() {
		//未使用的超级管理员秘钥有效期有效期1分钟
		return 60*1000;
	}
	
	public long link() {
		//链接有效时间30s
		return 30*1000;
	}
	
	public static void main(String[] args) {
	}

}
