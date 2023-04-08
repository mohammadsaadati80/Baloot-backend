<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/6/2023
  Time: 4:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.mm.Baloot.Baloot" %>
<%@ page import="org.mm.Baloot.Commodity" %>
<%@ page import="org.mm.Baloot.User" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<%
    User logged_in_user = Baloot.getInstance().getUserById(Baloot.getInstance().getLoginUsername());
%>
<ul>
    <li id="username">Username: <%= logged_in_user.getUsername() %></li>
    <li id="email">Email: <%= logged_in_user.getEmail() %></li>
    <li id="birthDate">Birth Date: <%= logged_in_user.getBirthDate() %></li>
    <li id="address">Address: <%= logged_in_user.getAddress() %></li>
    <li id="credit">Credit: <%= logged_in_user.getCredit() %></li>
    <li>Current Buy List Price: <%= logged_in_user.getCurrentBuyListPrice() %></li>
    <li>Discount Percent: <%= logged_in_user.getDiscount() %></li>
    <li>Buy List Price After Discount: <%= logged_in_user.applyDiscountOnBuyListPrice() %></li>
    <li>
        <a href="/credit">Add Credit</a>
    </li>
    <li>
        <form action="/buylist" method="POST">
            <label>Submit & Pay</label>
            <input type="hidden" name="action" value="payment">
            <input type="hidden" name="user_id" value="<%= logged_in_user.getUsername() %>">
            <button type="submit">Payment</button>
        </form>
    </li>
</ul>
<br><br>
<form action="/buylist" method="POST">
    <label>Add Discount Code:</label>
    <input type="text" name="discount" value="">
    <input type="hidden" name="action" value="discount">
    <input type="hidden" name="user_id" value="<%= logged_in_user.getUsername() %>">
    <button type="submit">submit</button>
</form>
<br><br>
<table>
    <caption>
        <h2>Buy List</h2>
    </caption>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <%
        List<Commodity> buylist = new ArrayList<>();;
        try {
            buylist = Baloot.getInstance().getBuyList(logged_in_user.getUsername());
        } catch (Exception ignored) {}
        for (Commodity commodity : buylist) {
    %>
    <tr>
        <td><%= String.valueOf(commodity.getId()) %></td>
        <td><%= commodity.getName() %></td>
        <td><%= Baloot.getInstance().getProviderById(commodity.getProviderId()).getName() %></td>
        <td><%= String.valueOf(commodity.getPrice()) %></td>
        <td><%= Baloot.getInstance().convertListOfStringsToString(commodity.getCategories()) %></td>
        <%
            String rating;
            try { rating = String.valueOf(commodity.getRating()); }
            catch (ArithmeticException e) { rating = "null"; }
        %>
        <td><%= rating%></td>
        <td><%= String.valueOf(commodity.getInStock()) %></td>
        <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
        <td>
            <form action="/buylist" method="POST" >
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="commodity_id" value="<%= String.valueOf(commodity.getId()) %>">
                <input type="hidden" name="user_id" value="<%= logged_in_user.getUsername() %>">
                <button type="submit">Remove</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
<br><br>
<table>
    <caption>
        <h2>Purchased List</h2>
    </caption>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th></th>
        <th></th>
    </tr>
    <%
        Map<Integer, Commodity> purchaseList = new HashMap<>();
        try {
            purchaseList = Baloot.getInstance().getUserById(logged_in_user.getUsername()).getPurchasedList();
        } catch (Exception ignored) {}
        for (Map.Entry<Integer, Commodity> entry : purchaseList.entrySet()) {
            Commodity commodity = entry.getValue();
    %>
    <tr>
        <td><%= String.valueOf(commodity.getId()) %></td>
        <td><%= commodity.getName() %></td>
        <td><%= Baloot.getInstance().getProviderById(commodity.getProviderId()).getName() %></td>
        <td><%= String.valueOf(commodity.getPrice()) %></td>
        <td><%= Baloot.getInstance().convertListOfStringsToString(commodity.getCategories()) %></td>
        <%
            String rating;
            try { rating = String.valueOf(commodity.getRating()); }
            catch (ArithmeticException e) { rating = "null"; }
        %>
        <td><%= rating%></td>
        <td><%= String.valueOf(commodity.getInStock()) %></td>
        <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
