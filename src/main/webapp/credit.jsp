<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/5/2023
  Time: 11:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.mm.Baloot.Baloot" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Credit</title>
</head>
<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoginUsername() %></p>
<br>
<form action="/credit" method="POST">
    <label>Credit:</label>
    <input type="number" name="credit" value="" min="0" required>
    <br>
    <button type="submit">Add credits</button>
</form>
</body>
</html>
