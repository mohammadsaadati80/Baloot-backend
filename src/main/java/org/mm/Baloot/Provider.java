package org.mm.Baloot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Provider {

    private Integer id;
    private String name;
    private Date registryDate;
    private String text;

    private Map<Integer, Commodity> commodities = new HashMap<>();

    private float averageCommoditiesRates = 0;

    public Provider(Integer _id, String _name, Date _registryDate, String _text) {
        id = _id;
        name = _name;
        registryDate = _registryDate;
        text = _text;
    }

    public void update(Provider provider) {
        id = provider.getId();
        name = provider.getName();
        registryDate = provider.getRegistryDate();
        text = provider.getText();;
    }

    public boolean isValidCommand() {
        if (id==null || name==null || registryDate==null)
            return false;
        else
            return true;
    }

    public void addCommodity(Commodity commodity) {
        commodities.put(commodity.getId(), commodity);
        updateAverageCommoditiesRates();
    }

    public void updateAverageCommoditiesRates() {
        averageCommoditiesRates = 0;
        for (Map.Entry<Integer, Commodity> set : commodities.entrySet())
            averageCommoditiesRates += set.getValue().getRating();
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

    public float getAverageCommoditiesRates() {
        updateAverageCommoditiesRates();
        return averageCommoditiesRates;
    }

    public Map<Integer, Commodity> getCommodities() {return commodities;}
}
