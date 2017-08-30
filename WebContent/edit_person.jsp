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
		String password = null, mother = null, sheet = null, openid = null;
		boolean parameter_correct = true;
		try {
			password = request.getParameter("password");
			mother = new Encrypt().decode(password.substring(21));
			password = password.substring(0, 21);
			sheet = request.getParameter("sheet_name");
			openid = new Encrypt().decode(request.getParameter("user"));
		} catch (Exception e) {
			parameter_correct = false;
			out.print("链接参数不足！请检查是否使用了完整的链接");
		}
		if (parameter_correct == true) {
			if (editor.check_password(3, mother, sheet, openid, password) == false) {
				out.print("错误的链接或链接已失效（链接有效时间为30分钟）");
			}
		}
	}
	else {
		//如果已经初始化，则
		if (editor.previlege <= 2) {
			//检查是否是由更高权限的修改页面跳转来的，并确定要修改的openid
			editor.openid = request.getParameter("openid");
			editor.build_connection();
			%>
			<form action="manager.jsp">
			<input type="submit" value="返回">
			</form>
			</br>
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
				String mother = null, sheet = null, openid = null;
				boolean parameter_correct = true;
				try {
					mother = new Encrypt().decode(password.substring(21));
					password = password.substring(0, 21);
					sheet = request.getParameter("sheet_name");
					openid = new Encrypt().decode(request.getParameter("user"));
				} catch (Exception e) {
					parameter_correct = false;
					out.print("链接参数不足！请检查是否使用了完整的链接");
				}
				if (parameter_correct == true) {
					if (editor.check_password(3, mother, sheet, openid, password) == false) {
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

<%//用于检验是否有修改表单提交，如果有则修改数据库数据
	if (editor.verified && request.getParameter("edit")!=null) {
		try {
			Result_table result = editor.database.select(editor.sheet, "_openid", editor.openid);
			for (int i = 1; i < result.num_of_column; i++) {
				//out.println(result.column[i]+":"+request.getParameter(""+i)+"<br></br>");
				if (result.data[0][i].equals(request.getParameter(""+i)) == false) {
					editor.database.update(editor.sheet, 
							result.column[i], request.getParameter(""+i), "_openid", editor.openid);
				}
			}
			out.print("修改成功！<br></br>");
		} catch (Exception e) {
			out.print("修改出错！请重试<br></br>");
		}
	}
%>

<%//用于生成网页
	if (editor.verified) {
		try {
			Result_table result = editor.database.select(editor.sheet, "_openid", editor.openid);
			%><form action="<%=new Configure().get_web_domain() + "edit_person.jsp" %>" method="post">
			<input type="hidden" name="edit" value="yes">
			<input type="hidden" name="openid" value="<%=result.data[0][0]%>"><%
			for (int i = 1; i < result.num_of_column; i++) {
				if (result.column[i].startsWith("_")) {%>
					<input type="hidden" name="<%=""+i%>" value="<%=result.data[0][i]%>">
				<%}
				else {%>
					<%=result.column[i]+":"%>
					<input type="text" name="<%=""+i%>" value="<%=result.data[0][i]%>" maxlength="40" size="40"><br></br>
				<%}
			}
			%>
			<input type="submit" value="确认">
			</form>
			<%
			editor.break_connection();
		}
		catch (Exception e) {
			out.print("生成编辑页面出错！");
		}
	}
%>
</body>
</html>