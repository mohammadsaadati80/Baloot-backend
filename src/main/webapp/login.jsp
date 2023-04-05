<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/5/2023
  Time: 5:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
</head>
<body>
<form action="/login" method="POST">
    <label>Username:</label>
    <input type="text" name="username" value="" required>
    <br>
    <label>Password:</label>
    <input type="text" name="password" value="" required>
    <br>
    <button type="submit">Login!</button>
</form>
</body>
</html>
