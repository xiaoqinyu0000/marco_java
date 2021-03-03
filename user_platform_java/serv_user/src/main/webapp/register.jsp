<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<html>
<head>
    <title>首页</title>
</head>
<body>
<div style="text-align: center;">
    <h3>欢迎注册</h3>
    <form action="${pageContext.request.contextPath}/register.do" method="post">
        <table>
            <tr>
                <td>用户名</td>
                <td><input type="text" name="name"></td>
            </tr>
            <tr>
                <td>密码</td>
                <td><input type="password" name="password"></td>
            </tr>
            <tr>
                <td><input type="submit" value="注册"></td>
                <td><input type="reset" value="取消"></td>
            </tr>
        </table>
    </form>
</div>
</body>
