<%--
  Created by IntelliJ IDEA.
  User: SAADATI-PC
  Date: 4/5/2023
  Time: 8:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.mm.Baloot.Baloot" %>
<%@ page import="org.mm.Baloot.Commodity" %>
<%@ page import="org.mm.Baloot.Provider" %>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Commodities</title>
    <style>
        table{
            width: 100%;
            text-align: center;
        }
        li, td, th {
            padding: 5px;
        }
    </style>
</head>
<body>
<a href="/">Home</a>
<p id="username">username: <%= Baloot.getInstance().getLoginUsername() %></p>
<br><br>
<form action="commodities" method="POST">
    <label>Search:</label>
    <input type="text" name="search" value="">
    <input type="hidden" name="hidden_sort_by_id" value="<%= (String) request.getAttribute("sort_by_id") %>" >
    <button type="submit" name="action" value="search_by_category">Search By Category</button>
    <button type="submit" name="action" value="search_by_name">Search By Name</button>
    <button type="submit" name="action" value="clear">Clear Search</button>
</form>
<br><br>
<form action="commodities" method="POST">
    <label>Sort By:</label>
    <input type="hidden" name="hidden_search_type" value="<%= (String) request.getAttribute("searched_type") %>" >
    <input type="hidden" name="hidden_search" value="<%= (String) request.getAttribute("searched_commodity") %>" >
    <button type="submit" name="action" value="sort_by_rate">Rate</button>
</form>
<br><br>
<table>
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
        List<Commodity> commodityList;
        if(!request.getAttribute("search_by_name").equals("false") && request.getAttribute("clear").equals("false")){
            commodityList = baloot.getCommoditiesByName(request.getAttribute("searched_commodity").toString());
        } else if(!request.getAttribute("search_by_category").equals("false") && request.getAttribute("clear").equals("false")){
            commodityList = baloot.getCommoditiesByCategory(request.getAttribute("searched_commodity").toString());
        } else{
            commodityList = baloot.getCommoditiesList();
        }
        if(request.getAttribute("sort_by_rate").equals("true")){
            commodityList = commodityList.stream().sorted(Comparator.comparing(Commodity::getRating).reversed()).toList();
        }
        for (Commodity commodity : commodityList) {
    %>
    <tr>
        <td><%= String.valueOf(commodity.getId())%></td>
        <td><%= commodity.getName()%></td>
        <td><%= Baloot.getInstance().getProviderById(commodity.getProviderId()).getName()%></td>
        <td><%= String.valueOf(commodity.getPrice())%></td>
        <td><%= Baloot.getInstance().convertListOfStringsToString(commodity.getCategories())%></td>
        <td><%= String.valueOf(commodity.getRating())%></td>
        <td><%= String.valueOf(commodity.getInStock())%></td>
        <td><a href="<%= "/commodities/" + commodity.getId()%>">Link</a></td>
    </tr>
    <%
        }
    %>
</table>
</body>
</html>
