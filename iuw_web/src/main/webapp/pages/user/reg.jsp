<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style type="text/css">
<!--
#Layer1 {
	position: absolute;
	width: 638px;
	height: 422px;
	z-index: 1;
	left: 266px;
	top: 129px;
	background-color: #FAFAFA;
}

.STYLE5 {
	font-family: "宋体";
	color: #3399FF;
	font-weight: bold;
}

#Layer2 {
	position: absolute;
	width: 173px;
	height: 25px;
	z-index: 1;
	left: 267px;
	top: 318px;
	background-color: #3399FF;
}

.STYLE6 {
	font-size: 36px;
	font-weight: bold;
}

.STYLE7 {
	color: #FFFFFF;
	font-size: 24px;
}
-->
</style>

<script type="text/javascript" src="/res/js/jquery1.4.2.js"
	charset="utf-8"></script>

<script type="text/javascript" src="/res/js/jquery-calendar.js"
	charset="utf-8"></script>
	
	
<script type="text/javascript" 	charset="utf-8">
	function submit_reg(){
		var uid = $.trim($("#uid").val());
		var pwd = $.trim($("#pwd").val());
		var pwd2 = $.trim($("#pwd2").val());
		var sex = $('input:radio[name="sex"]:checked').val();
		var age = $.trim($("#age").val());
		var email = $.trim($("#email").val());
		var uname = $.trim($("#uname").val());
		
		if(uid.length <= 0){
			alert("您的账号不能为空");
			$("#uid").focus();
			return;
		}
		

		if(pwd.length <= 0){
			alert("密码不能为空");
			$("#pwd").focus();
			return;
		}
		
		if(pwd2.length <= 0){
			alert("密码不能为空");
			$("#pwd2").focus();
			return;
		}
		
		if(pwd != pwd2){
			alert("两次输入密码不一致");
			$("#pwd").focus();
			return;
		}
		
		if(age.length <= 0){
			alert("年龄不能为空");
			$("#age").focus();
			return;
		}
		
		if(email.length <= 0){
			alert("邮箱不能为空");
			$("#email").focus();
			return;
		}
		
		$("#reg_form").submit();
		
	}
	
</script>

</head>

<body>
	<div id="Layer1">
		<form action="/user/reg.do" method="post"  id="reg_form">
		<table width="518" height="278" border="0" align="center">
			<tr>
				<td width="229" align="right">&nbsp;</td>
				<td width="513">&nbsp;</td>
			</tr>
			<tr>
				<td width="229" align="right"><span class="STYLE5">您的账号：</span></td>
				<td width="513"><input type="text" name="uid"  id="uid" class="STYLE5" /></td>
			</tr>
			<tr>
				<td align="right"><span class="STYLE5">您的密码：</span></td>
				<td><input name="pwd"  id="pwd" type="password" class="STYLE5" /></td>
			</tr>
			<tr>
				<td align="right"><span class="STYLE5">确认密码：</span></td>
				<td><input name="pwd2"  id="pwd2" type="password" class="STYLE5" /></td>
			</tr>
			<tr>
				<td align="right"><span class="STYLE5">您的新别：</span></td>
				<td><input name="sex" type="radio" value="1" />男 &nbsp;<input
					name="sex" type="radio" value="2" />女 &nbsp;<input name="sex"
					type="radio" value="0" />保密</td>
			</tr>
			<tr>
				<td align="right"><span class="STYLE5">Email：</span></td>
				<td><input name="email"  id="email" type="text" class="STYLE5" /></td>
			</tr>
			<tr>
				<td align="right"><span class="STYLE5">您的年龄：</span></td>
				<td><input name="age"  id="age" type="text" class="STYLE5" /></td>
			</tr>
			<tr>
				<td><div align="right">
						<span class="STYLE5">验证码：</span>
					</div></td>
				<td><input name="code" id="code" type="text" class="STYLE5" /></td>
			</tr>
			<tr>
				<td><div align="right">
						<span class="STYLE5">上传头像：</span>
					</div></td>
				<td><input name="head_url" id="head_url" type="file" /></td>
			</tr>
		</table>
		
		</form>
		<div class="STYLE6" id="Layer2">
			<div align="center" class="STYLE7" onclick="javascript:submit_reg();">注册</div>
		</div>
	</div>
</body>

</html>
