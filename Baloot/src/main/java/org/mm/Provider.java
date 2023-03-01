package org.mm;

import java.util.HashMap;
import java.util.Map;

public class Provider {

    private Integer id;
    private String name;
    private String registryDate;

    private Map<Integer, Commodity> commodities = new HashMap<>();

    private float averageCommoditiesRates = 0;

    public void update(Provider provider) {
        id = provider.getId();
        name = provider.getName();
        registryDate = provider.getRegistryDate();
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

    public String getRegistryDate() {
        return registryDate;
    }

    public float getAverageCommoditiesRates() {
        updateAverageCommoditiesRates();
        return averageCommoditiesRates;
    }

    public Map<Integer, Commodity> getCommodities() {return commodities;}
}
