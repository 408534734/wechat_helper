<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Encrypt" %>
<%@ page import="pub.Database" %>
<%@ page import="pub.Result_table" %>
<%@ page import="pub.Link" %>
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
	if (editor.verified == true && editor.previlege <= 2) {
		//如果已经初始化，则
		if (editor.previlege <= 1) {
			//检查是否是由更高权限的修改页面跳转来的，并确定要修改的sheet
			if (request.getParameter("sheet") != null)
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
					//password = password.substring(0, 21);
					sheet = request.getParameter("sheet_name");
				} catch (Exception e) {
					parameter_correct = false;
					%>
					<script type="text/javascript">
					alert("链接参数不足！请检查是否使用了完整的链接");
					</script>
					<%
				}
				if (parameter_correct == true) {
					if (editor.check_password(2, mother, sheet, "", password) == false) {
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
	else {
		//如果没有初始化过，则进行校验
		editor.verified = false;
		String password = null, mother = null, sheet = null;
		boolean parameter_correct = true;
		try {
			password = request.getParameter("password");
			mother = new Encrypt().decode(password.substring(21));
			//password = password.substring(0, 21);
			sheet = request.getParameter("sheet_name");
		} catch (Exception e) {
			parameter_correct = false;
			%>
			<script type="text/javascript">
			alert("链接参数不足！请检查是否使用了完整的链接");
			</script>
			<%
		}
		if (parameter_correct == true) {
			if (editor.check_password(2, mother, sheet, "", password) == false) {
				new Record_error("password = " + password + "\nsheet = " + sheet);
				%>
				<script type="text/javascript">
				alert("错误的链接或链接已失效（链接有效时间为30秒）");
				</script>
				<%
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
			%>
			<script type="text/javascript">
			alert("修改出错！请重试！");
			</script>
			<%
		}
	}
%>

<%//用于检验是否有删除成员
	if (editor.verified && request.getParameter("delete")!=null && request.getParameter("delete").equals("yes")) {
		try {
			String openid = request.getParameter("openid");
			editor.database.delete(editor.sheet, "_openid", openid);
			out.print("修改成功！<br></br>");
		} catch (Exception e) {
			%>
			<script type="text/javascript">
			alert("修改出错！请重试！");
			</script>
			<%
		}
	}
%>

<%//用于生成网页
	try {
		if (editor.verified) {
			Result_table result = editor.database.select_in_order(editor.sheet, "_order");
			%><table border="1"> <%
				%><tr> <th></th><th></th><%
				for (int j = 0; j < result.num_of_column; j++) {
					if (result.column[j].startsWith("_"))
						continue;
					%><th><%=result.column[j] %></th> <%
				}
				%><th></th></tr> <%
			for (int i = 0; i < result.num_of_row; i++) {
				%><tr>
				<!-- 修改按钮 -->
				<td>
				<form action="edit_person.jsp" method="post">
				<input type="hidden" name="user" value="<%=new Encrypt().encode(result.data[i][0]) %>">
				<input type="submit" value="修改">
				</form>
				</td>
				<!-- 设置与取消管理员按钮 -->
				<td>
				<form action="manager.jsp" method="post">
				<input type="hidden" name="openid" value="<%=result.data[i][0] %>">
				<%
				if (result.data[i][result.num_of_column-1].equals("否")) {%>
					<input type="hidden" name="operation" value="是">
					<input type="submit" value="设为管理员">
				<%}
				else {%>
					<input type="hidden" name="operation" value="否">
					<input type="submit" value="取消管理员">
				<%} %>
				</form>
				</td>
				
				<!-- 信息部分 -->
				<%
				for (int j = 0; j < result.num_of_column; j++) {
					if (result.column[j].startsWith("_"))
						continue;
					%><td><%=result.data[i][j]!=null?result.data[i][j]:"" %></td> <%
				}
				%>
							
				<!-- 删除按钮 -->
				<td>
				<form action="manager.jsp" method="post">
				<input type="hidden" name="openid" value="<%=result.data[i][0] %>">
				<input type="hidden" name="delete" value="yes">
				<input type="submit" value="删除">
				</form>
				</td>
				
				</tr> <%
			}
			%></table> <%
			%>
			为此表添加虚拟成员请点击
			<%=new Link().get_add_person_link(editor.sheet, "这里") %>
			<br>
			复制下面的验证码给你的小伙伴发送到公众号后台，即能让他们也加入通讯录
			<br>
			<%=new Link().get_add_person_identify_code(editor.database, editor.sheet) %>
			<%
		}
	} catch (Exception e) {
		%>
		<script type="text/javascript">
		alert("生成编辑页面出错！");
		</script>
		<%
	}
%>

<% } catch (Exception e) {%>
				<script type="text/javascript">
				alert("未知错误！");
				</script>
				<%}%>
</body>
</html>