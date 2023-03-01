package org.mm;

import java.util.HashMap;
import java.util.Map;

public class Provider {

    private Integer id;
    private String name;
    private String registryDate;

    private Map<Integer, Commodity> commodities = new HashMap<>();

    private float averageCommoditiesRates = 0;

    //TODO commodity list and avg rating and updates

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
        averageCommoditiesRates = ((commodities.size()-1)*averageCommoditiesRates + commodity.getRating())/commodities.size();
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

    public float getAverageCommoditiesRates() {return averageCommoditiesRates;}

    public Map<Integer, Commodity> getCommodities() {return commodities;}
}
