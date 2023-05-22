package org.mm.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.*;
@Entity
@Table(name = "commodity")
public class Commodity {
    @Id
    private Integer id;
    private String name;

    @Column
    @ElementCollection(targetClass=String.class)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Integer price;
    private String[] categories;
    private float rating;
    private Integer inStock;
    private String image;

    @ManyToMany
    @JoinTable(name="commodity_provider", joinColumns = @JoinColumn(name = "COMMODITY_ID"), inverseJoinColumns = @JoinColumn(name = "PROVIDER_ID"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private Integer providerId;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Rate> rates = new HashSet<>();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Comment> comments;

    private float score;

//    public Commodity(Integer _id, String _name, Integer _providerId, Integer _price, String[] _categories, float _rating, Integer _inStock) {
//        id = _id;
//        name = _name;
//        providerId = _providerId;
//        price = _price;
//        categories = _categories;
//        rating = _rating;
//        inStock = _inStock;
//    }

    public void update(Commodity commodity) {
        id = commodity.getId();
        name = commodity.getName();
        providerId = commodity.getProviderId();
        price = commodity.getPrice();
        categories = commodity.getCategories();
        rating = commodity.getRating();
        inStock = commodity.getInStock();
        image = commodity.getImage();
    }

    public boolean isValidCommand() {
        if (id==null || name==null || providerId==null || price==null || categories==null || rating==0.0 || inStock==null)
            return false;
        else
            return true;
    }

    public void toJson(ObjectMapper mapper, ObjectNode commodity, boolean all) {
        commodity.put("id", id);
        commodity.put("name", name);
        commodity.put("providerId", providerId);
        commodity.put("price", price);
        ArrayNode categoryArrayNode = mapper.valueToTree(getCategories());
        commodity.putArray("genres").addAll(categoryArrayNode);
        commodity.put("rating", rating);
        if (all)
            commodity.put("inStock", inStock);
    }

    public void addRate(Rate rate) {
        rates.add(rate);
        rating = (float) rates.stream().mapToDouble(Rate::getScore).average().orElse(0);
    }

    public boolean isInCategory(String inputCategory) {
        for (String category: getCategories())
            if (category.equals(inputCategory))
                return true;
        return false;
    }

    public boolean isPriceInRange(Integer startPrice, Integer endPrice) {
        return ((startPrice <= price) && (price <= endPrice));
    }

    public void updateScore(String[] similarCategories) {
        score = 0;
        ArrayList<String> tempCategories = new ArrayList<>(Arrays.asList(categories));
        Integer is_in_similar_category = 0;
        for(String category : similarCategories)
            if (tempCategories.contains(category)) {
                is_in_similar_category = 1;
                break;
            }
        score = 11 * is_in_similar_category + rating;
    }

    public void addComment(Comment comment) {comments.add(comment);}


    public void buy(Integer number) {
        inStock -= number;
    }

    public Integer getId() { return id; }

    public String getName() { return name; }

    public Integer getProviderId() { return providerId;}

    public Integer getPrice() { return price;}

    public String[] getCategories() { return categories;}

    public float getRating() { return rating;}

    public Integer getInStock() { return inStock;}

//    public Integer getUserRate(String username) {
//        return rates.get(username);
//    }

    public float getScore() { return score;}

    public String getImage() {return image;}

    public Set<Rate> getRates() {return rates;}

    public Set<Comment> getComments() {return comments;}

    public void setProviderId(Integer _providerId) {providerId = _providerId;}
}
