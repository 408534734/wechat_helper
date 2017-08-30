<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Database" %>
<%@ page import="pub.Result_table" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信后台小助手</title>
</head>
<body>
	<%
	try {
		String password = request.getParameter("password");
		//链接中的get参数只有一个为password，其组成如下：
		//1.加密的时间，13位
		//2.混淆码，8位
		//3.加密的公众号ID，剩余所有
		String mother = password.substring(21);
		password = password.substring(0, 21);
		Database database = new Database("mother");
		Result_table result = database.select("_super_manager_link");
		
	}catch (Exception e) {
		%>无效的链接或链接已失效（链接有效时间为30分钟）<%
	}
	%>
</body>
</html>