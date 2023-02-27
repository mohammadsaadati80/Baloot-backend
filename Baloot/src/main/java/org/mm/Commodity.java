package org.mm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Commodity {
    private Integer id;
    private String name;
    private Integer providerId;
    private Integer price;
    private String[] categories;
    private float rating;
    private Integer inStock;

    public void update(Commodity commodity) {
        id = commodity.getId();
        name = commodity.getName();
        providerId = commodity.getProviderId();
        price = commodity.getPrice();
        categories = commodity.getCategories();
        rating = commodity.getRating();
        inStock = commodity.getInStock();
    }

    public boolean isValidCommand() {
        if (id==null || name==null || providerId==null || price==null || categories==null || rating==0.0 || inStock==null)
            return false;
        else
            return true;
    }

    public void toJson(ObjectMapper mapper, ObjectNode commodity) {
        commodity.put("id", id);
        commodity.put("name", name);
        commodity.put("providerId", providerId);
        commodity.put("price", price);
        ArrayNode categoryArrayNode = mapper.valueToTree(getCategories());
        commodity.putArray("genres").addAll(categoryArrayNode);
        commodity.put("rating", rating);
        commodity.put("inStock", inStock);
    }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public Integer getProviderId() { return providerId;}

    public Integer getPrice() { return price;}

    public String[] getCategories() { return categories;}

    public float getRating() { return rating;}

    public Integer getInStock() { return inStock;}
}
