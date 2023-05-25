package org.mm.Entity;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "provider")
public class Provider {

    @Id
    private Integer id;
    private String name;
    private Date registryDate;
    private String text;
    @Column(name = "image", columnDefinition = "text")
    private String image;
    @SuppressWarnings("JpaAttributeTypeInspection")
//    private Map<Integer, Commodity> commodities = new HashMap<>();

    @ManyToMany
    @JoinTable(name="provider_commodities", joinColumns = @JoinColumn(name = "PROVIDER_ID"), inverseJoinColumns = @JoinColumn(name = "COMMODITY_ID"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private final Set<Commodity> commodities = new HashSet<>();

    private float averageCommoditiesRates = 0;

//    public Provider(Integer _id, String _name, Date _registryDate, String _text) {
//        id = _id;
//        name = _name;
//        registryDate = _registryDate;
//        text = _text;
//    }

    public void update(Provider provider) {
        id = provider.getId();
        name = provider.getName();
        registryDate = provider.getRegistryDate();
        text = provider.getText();
        image = provider.getImage();
    }

    public boolean isValidCommand() {
        if (id==null || name==null || registryDate==null)
            return false;
        else
            return true;
    }

    public void addCommodity(Commodity commodity) {
        commodities.add(commodity);
        updateAverageCommoditiesRates();
    }

    public void updateAverageCommoditiesRates() {
        averageCommoditiesRates = 0;
        for (Commodity set : commodities)
            averageCommoditiesRates += set.getRating();
        averageCommoditiesRates /= commodities.size();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getRegistryDate() {
        return registryDate;
    }

    public String getText() { return text;}

    public String getImage() { return image;}

    public float getAverageCommoditiesRates() {
        updateAverageCommoditiesRates();
        return averageCommoditiesRates;
    }

    public Set<Commodity> getCommodities() {return commodities;}
}
