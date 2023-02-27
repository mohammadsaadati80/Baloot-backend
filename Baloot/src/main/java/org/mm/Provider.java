package org.mm;

public class Provider {

    private Integer id;
    private String name;
    private String registryDate;

    //TODO commodity list and avg rating

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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRegistryDate() {
        return registryDate;
    }
}
