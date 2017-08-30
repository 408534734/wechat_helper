<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Send_mail" %>
<%@ page import="pub.Time" %>
<%@ page import="pub.Make_random_string" %>
<%@ page import="pub.Database" %>
<%@ page import="pub.Record_error" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信后台小助手</title>
</head>
<body>
<%
	/*
		超级管理员秘钥组成：  
			part1："spmgpwd" 即 super_manager_password作为识别关键字
			part2：一个13数字组成的加密时间字符串，表示生成时的系统时间
			part3：一个随机的八位数，作为特征混淆码
	*/
	try {
		String mail_address = request.getParameter("mail_address");
		String super_manager_code = "spmgpwd" + (new Time()).get_encode_time() + (new Make_random_string()).get();
		Database database = new Database("configure");
		database.insert("super_manager_password", new String[]{"password", "mother"}, new String[]{super_manager_code, ""});
		database.break_connect();
		Send_mail send_mail = new Send_mail();
		if ((new Send_mail()).send(mail_address, "欢迎使用微信后台管理小助手，这是您的超级管理员秘钥", 
				"您的超级管理员秘钥为：" + super_manager_code + 
				"<br></br>该秘钥终身唯一，且无法更改，建议您不要删除此邮件。<br></br>" +
				"接下来请您将该秘钥通过您的微信发送给您的公众号后台，即可激活使用！<br></br>" +
				"如果您没有进行触发发送此邮件的操作，请您无视此邮件。")
			== false) {
			%>请您输入有效的邮校地址<%
		}
		else {
			%>邮件发送成功，请您前往邮箱查收<%
		}
	} catch (Exception e) {
		System.out.println(e.getMessage());
		new Record_error("生成超级管理员秘钥出现错误！");
		%>系统生成超级管理员秘钥出现错误!<%
	}
%>
</body>
</html>