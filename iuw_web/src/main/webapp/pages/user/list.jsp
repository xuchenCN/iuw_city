<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>消息页</title>
<style type="text/css">
<!--
#Layer1 {
	position: absolute;
	width: 201px;
	height: 202px;
	z-index: 1;
	background-color: #FAFAFA;
	left: 82px;
	top: 25px;
}

#Layer2 {
	position: absolute;
	width: 595px;
	height: 706px;
	z-index: 2;
	left: 350px;
	top: 23px;
	background-color: #FAFAFA;
}
-->
</style>
</head>

<body>
	<div id="Layer1">
		<table width="203" height="320" border="0">
			<tr>
				<td height="182" colspan="2"><img src="${user.headImg}"></img></td>
			</tr>
			<tr>
				<td height="35" colspan="2">${user.name}</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>
	</div>
	<div id="Layer2">
	<c:forEach items="${feeds }" var="feed">
		<p><img height="20" width="20" src="${user.headImg}"></img>${feed.userName} : ${feed.content} </p>
	</c:forEach>
	
	
	</div>
</body>
</html>
