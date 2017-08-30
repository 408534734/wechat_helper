<!-- 邮件发送模块，获取超级管理员的邮箱，用于向用户输入的邮箱中发送一个超级超级管理员秘钥 -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>微信后台小助手</title>
</head>
<body>
<form action="new_system_send_mail.jsp" method="post">
请输入您的邮箱便于接收超级管理员秘钥<br></br>
<input type="text" name="mail_address" maxlength="40" size="40"><br></br>
<input type="submit" value="发送"><br></br>
发送邮件需要若干秒的时间，请耐心等待一下下~
</form>
</body>
</html>