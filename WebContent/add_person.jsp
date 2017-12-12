<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Encrypt" %>
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
<%try { %>
<%//用于校验用户，若校验通过，则连接数据库
	if (editor.verified == false) {
		//如果没有初始化过，则进行校验
		String password = null, mother = null, sheet = null, openid = null;
		boolean parameter_correct = true;
		try {
			password = request.getParameter("password");
			mother = new Encrypt().decode(password.substring(21));
			//password = password.substring(0, 21);
			sheet = request.getParameter("sheet_name");
			openid = new Encrypt().decode(request.getParameter("user"));
		} catch (Exception e) {
			parameter_correct = false;
			%>
			<script type="text/javascript">
			alert("链接参数不足！请检查是否使用了完整的链接");
			</script>
			<%
		}
		if (parameter_correct == true) {
			if (editor.check_password(4, mother, sheet, openid, password) == false) {
				%>
				<script type="text/javascript">
				alert("错误的链接或链接已失效（链接有效时间为30秒）");
				</script>
				<%
			}
		}
	}
	else {
		//如果已经初始化，则
		if (editor.previlege <= 2) {
			//检查是否是由更高权限的修改页面跳转来的，并确定要修改的openid
			if (request.getParameter("user") != null)
			editor.openid = new Encrypt().decode(request.getParameter("user"));
			editor.build_connection();
			%>
			<form action="manager.jsp">
			<input type="submit" value="返回">
			</form>
			<br>
			<%
		}
		else {
			//不是由更高权限的修改页面跳转来的，则检查参数是否变化，如果缺省，则继续为原来的页面，如果更改则重新校验
			String password = null;
			try {
				password = request.getParameter("password");
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
					//password = password.substring(0, 21);
					sheet = request.getParameter("sheet_name");
					openid = new Encrypt().decode(request.getParameter("user"));
				} catch (Exception e) {
					parameter_correct = false;
					%>
					<script type="text/javascript">
					alert("链接参数不足！请检查是否使用了完整的链接");
					</script>
					<%
				}
				if (parameter_correct == true) {
					if (editor.check_password(4, mother, sheet, openid, password) == false) {
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
		
	}
%>

<%//用于检验是否有添加表单提交，如果有则修改数据库数据
	if (editor.verified && request.getParameter("added")!=null && request.getParameter("added").equals("yes")) {
		try {
			Result_table result = editor.database.select(editor.sheet, "_openid", editor.openid);
			if (result.num_of_row != 0) {
				out.print("您已经注册，无法重复注册，可以点击<a href=\"edit_person.jsp\">这里</a>来修改你的信息");
			}
			else {
				String[] keys = new String[result.num_of_column];
				String[] values = new String[result.num_of_column];
				int p = 0;
				keys[p] = "_openid";
				values[p] = editor.openid;
				p++;
				for (int i = 1; i < result.num_of_column-2; i++) {
					keys[p] = result.column[i];
					if (!result.column[i].startsWith("_"))
						values[p] = request.getParameter(""+i);
					else values[p] = "";
					p++;
				}
				keys[p] = "_manager";
				values[p] = "否";
				p++;
				keys[p] = "_order";
				values[p] = "500";
				p++;
				editor.database.insert(editor.sheet, keys, values);
				out.print("新建成功，你的信息如下<br>");
				for (int i = 1; i < p-2; i++) {
					if (keys[i].startsWith("_"))
						continue;
					out.print(keys[i] + " : " + values[i] + "<br>");
				}
				out.print("点击<a href=\"edit_person.jsp\">这里</a>继续修改你的信息");
			}
		} catch (Exception e) {
			//out.print("修改出错！请重试<br></br>");
			%>
			<script type="text/javascript">
			alert("添加出错！请返回重试！");
			</script>
			<%
		}
		editor.break_connection();
	}

	//用于生成网页
	else if (editor.verified) {
		try {
			Result_table result = editor.database.select(editor.sheet, "_openid", editor.openid);
			if (result.num_of_row != 0) {
				out.print("您已经注册，可以点击<a href=\"edit_person.jsp\">这里</a>来修改你的信息");
			}
			else {
				%><form action="<%="add_person.jsp" %>" method="post">
				<input type="hidden" name="user" value="<%=new Encrypt().encode(editor.openid) %>">
				<input type="hidden" name="added" value="yes"> <%
				for (int i = 1; i < result.num_of_column; i++) {
					if (!result.column[i].startsWith("_")) { %>
						<%=result.column[i]+":"%>
						<input type="text" name="<%=""+i%>" maxlength="40" size="40"><br></br>
					<%}
				}
				%>
				<input type="submit" value="确认">
				</form>
				<%
				editor.break_connection();
			}
		}
		catch (Exception e) {
			%>
			<script type="text/javascript">
			alert("生成添加页面出错！");
			</script>
			<%
		}
	}
%>
<% } catch (Exception e) {%>
				<script type="text/javascript">
				alert("未知错误！");
				</script>
				<%}%>
</body>
</html>