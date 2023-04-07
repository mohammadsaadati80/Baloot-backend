<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/4/2023
  Time: 10:46 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.mm.Baloot.Baloot" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>
    <body>
    <ul>
        <li id="username">username: <%= Baloot.getInstance().getLoginUsername() %></li>
        <li>
            <a href="/commodities">Commodities</a>
        </li>
        <li>
            <a href="/buyList">Buy List</a>
        </li>
        <li>
            <a href="/credit">Add Credit</a>
        </li>
        <li>
            <a href="/logout">Log Out</a>
        </li>
    </ul>
</body>
</html>

