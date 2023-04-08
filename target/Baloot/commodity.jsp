<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/6/2023
  Time: 12:53 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.mm.Baloot.Baloot" %>
<%@ page import="org.mm.Baloot.Commodity" %>
<%@ page import="org.mm.Baloot.Comment" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Commodity</title>
    <style>
        li, td, th {
            padding: 5px;
        }
        table {
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoginUsername() %></p>
<% Commodity commodity = (Commodity) request.getAttribute("commodity"); %>
<ul>
    <li id="id">Id: <%= commodity.getId() %></li>
    <li id="name">Name: <%= commodity.getName() %></li>
    <li id="providerName">Provider Name: <%= Baloot.getInstance().getProviderById(commodity.getProviderId()).getName() %></li>
    <li id="price">Price: <%= commodity.getPrice() %></li>
    <li id="categories">Categories: <%= Baloot.getInstance().convertListOfStringsToString(commodity.getCategories()) %></li>
    <li id="rating">Rating: <%= commodity.getRating() %></li>
    <li id="inStock">In Stock: <%= commodity.getInStock() %></li>
</ul>
<br><br>
<form action="<%= "/commodities/" + commodity.getId()%>" method="POST">
    <label>Add Your Comment:</label>
    <input type="text" name="comment" value="" required>
    <input type="hidden" name="action" value="comment">
    <input type="hidden" name="commodity_id" value="<%=commodity.getId()%>">
    <button type="submit">submit</button>
</form>
<br><br>
<h3>Rate This Commodity!</h3>
<form action="<%= "/commodities/" + commodity.getId()%>" method="POST">
    <label>Rate(between 1 and 10):</label>
    <input type="number" name="quantity" min="1" max="10" required>
    <input type="hidden" name="action" value="rate">
    <input type="hidden" name="commodity_id" value="<%=commodity.getId()%>">
    <button type="submit">Rate</button>
</form>
<br>
<h3>Add This Commodity To Your BuyList!</h3>
<form action="<%= "/commodities/" + commodity.getId()%>" method="POST">
    <input type="hidden" name="action" value="add">
    <input type="hidden" name="commodity_id" value="<%=commodity.getId()%>">
    <button type="submit">Add to BuyList</button>
</form>
<br />
<table>
    <caption><h2>Comments</h2></caption>
    <tr>
        <th>username</th>
        <th>comment</th>
        <th>date</th>
        <th>likes</th>
        <th>dislikes</th>
    </tr>
    <%
        List<Comment> comments = Baloot.getInstance().getCommentByCommodity(commodity.getId());
        for(Comment comment : comments){
    %>
    <tr>
        <td><%=comment.getUsername()%></td>
        <td><%=comment.getText()%></td>
        <td><%=comment.getDate()%></td>
        <td>
            <form action="<%= "/commodities/" + commodity.getId()%>" method="POST">
                <label><%=comment.getLike()%></label>
                <input
                        type="hidden"
                        name="comment_id"
                        value="<%=comment.getId()%>"
                />
                <input type="hidden" name="action" value="like">
                <input type="hidden" name="commodity_id" value="<%=commodity.getId()%>">
                <button type="submit">like</button>
            </form>
        </td>
        <td>
            <form action="<%= "/commodities/" + commodity.getId()%>" method="POST">
                <label><%=comment.getDislike()%></label>
                <input
                        type="hidden"
                        name="comment_id"
                        value="<%=comment.getId()%>"
                />
                <input type="hidden" name="action" value="dislike">
                <input type="hidden" name="commodity_id" value="<%=commodity.getId()%>">
                <button type="submit">dislike</button>
            </form>
        </td>
    </tr>
    <%
        }
    %>
</table>
<table>
    <caption><h2>Suggested Commodities</h2></caption>
    <tr>
        <th>Id</th>
        <th>Name</th>
        <th>Provider Name</th>
        <th>Price</th>
        <th>Categories</th>
        <th>Rating</th>
        <th>In Stock</th>
        <th>Links</th>
    </tr>
    <%
        Baloot baloot = Baloot.getInstance();
        List<Commodity> commodityList = baloot.getSuggestedCommodities(commodity);
        for (Commodity sug_commodity : commodityList) {
    %>
    <tr>
        <td><%= String.valueOf(sug_commodity.getId())%></td>
        <td><%= sug_commodity.getName()%></td>
        <td><%= Baloot.getInstance().getProviderById(sug_commodity.getProviderId()).getName()%></td>
        <td><%= String.valueOf(sug_commodity.getPrice())%></td>
        <td><%= Baloot.getInstance().convertListOfStringsToString(sug_commodity.getCategories())%></td>
        <td><%= String.valueOf(sug_commodity.getRating())%></td>
        <td><%= String.valueOf(sug_commodity.getInStock())%></td>
        <td><a href="<%= "/commodities/" + sug_commodity.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
<br>



</body>
</html>
