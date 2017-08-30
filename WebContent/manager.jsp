<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Encrypt" %>
<%@ page import="pub.Configure" %>
<%@ page import="pub.Database" %>
<%@ page import="pub.Result_table" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信后台小助手</title>
</head>
<body>
<jsp:useBean id="editor" scope="session" class="pub.Editor" />
<% request.setCharacterEncoding("UTF-8"); %>

<%//用于校验用户，若校验通过，则连接数据库
	if (editor.verified == false) {
		//如果没有初始化过，则进行校验
		String password = null, mother = null, sheet = null;
		boolean parameter_correct = true;
		try {
			password = request.getParameter("password");
			mother = new Encrypt().decode(password.substring(21));
			password = password.substring(0, 21);
			sheet = request.getParameter("sheet_name");
		} catch (Exception e) {
			parameter_correct = false;
			out.print("链接参数不足！请检查是否使用了完整的链接");
		}
		if (parameter_correct == true) {
			if (editor.check_password(2, mother, sheet, "", password) == false) {
				out.print("错误的链接或链接已失效（链接有效时间为30分钟）");
			}
		}
	}
	else {
		//如果已经初始化，则
		if (editor.previlege <= 1) {
			//检查是否是由更高权限的修改页面跳转来的，并确定要修改的sheet
			editor.sheet = request.getParameter("sheet");
			editor.build_connection();
			%>
			<form action="super_manager.jsp">
			<input type="submit" value="返回">
			</form>
			<%
		}
		else {
			//不是由更高权限的修改页面跳转来的，则检查参数是否变化，如果缺省，则继续为原来的页面，如果更改则重新校验
			String password = null;
			try {
				password = request.getParameter("password");
				editor.build_connection();
			} catch (Exception e) {}
			if (password != null &&
					password.isEmpty() == false &&
					password.equals(editor.password) == false) {
				//检查到password发生改变,重新校验
				editor.verified = false;
				String mother = null, sheet = null;
				boolean parameter_correct = true;
				try {
					mother = new Encrypt().decode(password.substring(21));
					password = password.substring(0, 21);
					sheet = request.getParameter("sheet_name");
				} catch (Exception e) {
					parameter_correct = false;
					out.print("链接参数不足！请检查是否使用了完整的链接");
				}
				if (parameter_correct == true) {
					if (editor.check_password(2, mother, sheet, "", password) == false) {
						out.print("错误的链接或链接已失效（链接有效时间为30分钟）");
					}
				}
			}
			else {
				//没有改变，使用上次的session的参数即可
				editor.build_connection();
			}
		}
	}
%>

<%//用于检验是否有修改管理员表单，有则修改
	if (editor.verified && request.getParameter("operation")!=null) {
		try {
			String openid = request.getParameter("openid");
			String operation = request.getParameter("operation");
			editor.database.update(editor.sheet, "_manager", operation, "_openid", openid);
			out.print("修改成功！<br></br>");
		} catch (Exception e) {
			out.print("修改出错！请重试<br></br>");
		}
	}
%>

<%//用于生成网页
	if (editor.verified) {
		Result_table result = editor.database.select(editor.sheet);
		%><table border="1"> <%
			%><tr> <%
			for (int j = 0; j < result.num_of_column; j++) {
				if (result.column[j].startsWith("_"))
					continue;
				%><th><%=result.column[j] %></th> <%
			}
			%><th></th></tr></th></tr> <%
		for (int i = 0; i < result.num_of_row; i++) {
			%><tr> <%
			for (int j = 0; j < result.num_of_column; j++) {
				if (result.column[j].startsWith("_"))
					continue;
				%><td><%=result.data[i][j]!=null?result.data[i][j]:"" %></td> <%
			}
			%><td>
			<form action="edit_person.jsp" method="post">
			<input type="hidden" name="openid" value="<%=result.data[i][0] %>">
			<input type="submit" value="修改">
			</form>
			</td>
			<td>
			<form action="manager.jsp" method="post">
			<input type="hidden" name="openid" value="<%=result.data[i][0] %>">
			<%if (result.data[i][result.num_of_column-1].equals("否")) {%>
				<input type="hidden" name="operation" value="是">
				<input type="submit" value="设为管理员">
			<%}
			else {%>
				<input type="hidden" name="operation" value="否">
				<input type="submit" value="取消管理员">
			<%} %>
			</form>
			</td> 
			<%
			%></tr> <%
		}
		%></table> <%
		%>
		为此表添加虚拟成员请点击
		复制下面的验证码给你的小伙伴发送到公众号后台，即能让他们也加入通讯录
		//TODO
		<%
	}
%>
</body>
</html>