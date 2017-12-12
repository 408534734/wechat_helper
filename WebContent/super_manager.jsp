<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Database" %>
<%@ page import="pub.Result_table" %>
<%@ page import="pub.Encrypt" %>
<%@ page import="pub.Record_error" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信后台小助手</title>
</head>
<body>
<jsp:useBean id="editor" scope="session" class="pub.Editor" />
<% request.setCharacterEncoding("UTF-8"); %>

<%try { %>

<%//用于校验用户，若校验通过，则连接数据库
	if (editor.verified == true && editor.previlege == 1) {
		//如果已经初始化，则检查参数是否变化，如果缺省，则继续为原来的页面，如果更改则重新校验
		String password = null;
		try {
			password = request.getParameter("password");
		} catch (Exception e) {}
		if (password != null &&
				password.isEmpty() == false &&
				password.equals(editor.password) == false) {
			//检查到password发生改变,重新校验
			editor.verified = false;
			String mother = null;
			boolean parameter_correct = true;
			try {
				mother = new Encrypt().decode(password.substring(21));
				password = password.substring(0, 21);
			} catch (Exception e) {
				parameter_correct = false;
				%>
				<script type="text/javascript">
				alert("链接参数不足！请检查是否使用了完整的链接");
				</script>
				<%
			}
			if (parameter_correct == true) {
				if (editor.check_password(2, mother, "", "", password) == false) {
					new Record_error(mother+" & "+password);
					%>
					<script type="text/javascript">
					alert("错误的链接或链接已失效（链接有效时间为30秒）");
					</script>
					<%
				}
			}
		}
		else {
			//没有改变，使用上次的session的参数即可
			editor.build_connection();
		}
	}
	else {
		//如果没有初始化过，则进行校验
		editor.verified = false;
		String password = null, mother = null;
		boolean parameter_correct = true;
		try {
			password = request.getParameter("password");
			mother = new Encrypt().decode(password.substring(21));
			//password = password.substring(0, 21);
		} catch (Exception e) {
			parameter_correct = false;
			%>
			<script type="text/javascript">
			alert("链接参数不足！请检查是否使用了完整的链接");
			</script>
			<%
		}
		if (parameter_correct == true) {
			if (editor.check_password(1, mother, "", "", password) == false) {
				%>
				<script type="text/javascript">
				alert("错误的链接或链接已失效（链接有效时间为30秒）");
				</script>
				<%
			}
		}
	}
%>

<%
//用于检测是否有删除
	if (editor.verified == true && request.getParameter("delete") != null) {
		editor.database.execute("DROP TABLE " + request.getParameter("delete"));
		out.print(request.getParameter("delete") + "删除成功！<br>");
	}
%>

<%
//用于检测是否有弹幕功能开启或关闭
	if (editor.verified == true && request.getParameter("bilibili") != null) {
		if (request.getParameter("bilibili").equals("on")) {
			editor.database.execute("CREATE TABLE `_bilibili` ( `message` varchar(255) DEFAULT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8");
		}
		else editor.database.execute("DROP TABLE _bilibili");
	}
%>

<%
//用于生成网页
	if (editor.verified == true) {
		Result_table sheets = editor.database.get_all_sheet_name(editor.mother);
		%>
		您已成功进入超级管理员模式<br></br>
		<table border="1"> <%
		if (sheets.num_of_row == 0)
			out.print("您还没有建立任何通讯录呢！<br>");
		else {
			%>
			<tr>
			<th>表名（点击即可编辑）</th>
			<th>点击定制表头</th>
			<th>慎点！没有二次确认且无法恢复！</th>
			</tr>
			<%
		}
		for (int i = 0; i < sheets.num_of_row; i++) {
			if (sheets.data[i][0].startsWith("_"))
				continue;
			%>
			<tr>
			<th><a href="manager.jsp?sheet=<%=sheets.data[i][0] %>"><%=sheets.data[i][0] %></a></th>
			<th><a href="edit_sheet.jsp?sheet=<%=sheets.data[i][0] %>">定制</a></th>
			<th><a href="super_manager.jsp?delete=<%=sheets.data[i][0] %>">删除！</a></th>
			</tr>
			<%
		}
		%></table>
		<a href="edit_sheet.jsp?new=yes">点击这里新建一张表</a>
		<br></br>
		<hr />
		<%
		boolean bilibili_exist = false;
		for (int i = 0; i < sheets.num_of_row; i++) {
			if (sheets.data[i][0].equals("_bilibili"))
				bilibili_exist = true;
		}
		if (bilibili_exist)
		{%>
		<a href="super_manager.jsp?bilibili=off">关闭弹幕功能</a>
		<br>
		你的用户账号是：<%=new Encrypt().encode(editor.mother) %>
		<%}
		else
		{%>
		<a href="super_manager.jsp?bilibili=on">开启弹幕功能</a>
		<%}
	}
%>
<% } catch (Exception e) {%>
				<script type="text/javascript">
				alert("未知错误！");
				</script>
				<%}%>
</body>
</html>