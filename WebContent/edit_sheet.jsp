<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="pub.Database" %>
<%@ page import="pub.Result_table" %>
<%@ page import="pub.Encrypt" %>
<%@ page import="pub.Time" %>
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
<%
//处理修改
	try {
		if (editor.verified == true && request.getParameter("edit") != null && request.getParameter("edit").equals("yes")) {
			boolean check = false;
			Result_table result = editor.database.select(editor.sheet, "_openid", "empty_id");
			for (int i = 4; i < result.num_of_column-2; i++) {
				if (request.getParameter(""+i).equals(result.column[i]) == false) {
					editor.database.execute("ALTER TABLE " + editor.sheet + " CHANGE " + 
						result.column[i] + " " + request.getParameter(""+i) + " VARCHAR(255)");
					check = true;
				}
			}
			if (request.getParameter("sheet_name").equals(editor.sheet) == false) {
				editor.database.execute("RENAME TABLE " + editor.sheet + " TO " + request.getParameter("sheet_name"));
				editor.sheet = request.getParameter("sheet_name");
				check = true;
			}
			if (check) {
				out.print("修改成功！");
			}
		}
	} catch (Exception e) {
		out.print("修改出错！");
	}
	
%>

<%
//新建表或者生成网页
	if (editor.verified == false) {
		out.print("登录已过期！");
	}
	else {
		%>
		<form action="super_manager.jsp">
		<input type="submit" value="返回">
		</form>
		<br>
		<%
		if (request.getParameter("new") != null && request.getParameter("new").equals("yes")) {
			out.print("新建成功！<br></br>");
			editor.sheet = "请更改表名" + new Time().get_encode_time();
			editor.database.execute("CREATE TABLE " + editor.sheet +
					" ( " + 
					//这里第一个和倒数第一二个必须固定！！！
					"_openid VARCHAR(255), " + 
					"姓名 VARCHAR(255), " + 
					"部门 VARCHAR(255), " + 
					"手机 VARCHAR(255), " + 
					"职位 VARCHAR(255), " + 
					"性别 VARCHAR(255), " + 
					"邮箱 VARCHAR(255), " + 
					"微信号 VARCHAR(255), " + 
					"QQ VARCHAR(255), " + 
					"班级 VARCHAR(255), " + 
					"学号 VARCHAR(255), " + 
					"宿舍 VARCHAR(255), " + 
					"生日 VARCHAR(255), " + 
					"_保留项1 VARCHAR(255), " + 
					"_保留项2 VARCHAR(255), " + 
					"_order VARCHAR(255), " + 
					"_manager VARCHAR(255) " + ")");
		}
		else {
			if (request.getParameter("sheet") != null)
			editor.sheet = request.getParameter("sheet");
		}
		%>
		<form action="<%="edit_sheet.jsp" %>" method="post">
		<input type="hidden" name="edit" value="yes">
		<input type="text" name="<%="sheet_name"%>" value="<%=editor.sheet %>" maxlength="40" size="40">
		表项如下：<br></br>
		姓名（此项不可更改）<br></br>
		部门（此项也不可更改）<br></br>
		手机（此项也不可更改）<br></br>
		<%
		Result_table result = editor.database.select(editor.sheet, "_openid", "empty_id");
		for (int i = 4; i < result.num_of_column-2; i++) {
			%>
			<input type="text" name="<%=""+i%>" value="<%=result.column[i] %>" maxlength="40" size="40"><br></br>
			<%
		}
		%>
		tips.如果想要把某一表项隐藏，只需要在前面加个下划线“_”就可以了哦！<br></br>
		<input type="submit" value="提交">
		</form>
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