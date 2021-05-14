<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
<head>
    <title>注册页面</title>
</head>
<body>
<div style="text-align: center;">
    <h3>欢迎注册</h3>
    <form action="${pageContext.request.contextPath}/register.do" method="post">
        <table>
            <tr>
<%--                <td><input type="button" value="登      陆" onclick="window.location.href('login.jsp')"></td>--%>
                <td><input type="button" value="注      册" onclick="window.open('register.jsp')"></td>
            </tr>
        </table>
    </form>
</div>
</body>
