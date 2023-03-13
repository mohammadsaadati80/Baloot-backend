package org.mm.Baloot;

import org.mm.Baloot.Exceptions.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

public class Baloot {

    private ObjectMapper mapper;
    private Map<String, User> users;
    private Map<Integer, Provider> providers;
    private Map<Integer, Commodity> commodities;

    public Baloot() {
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        users = new HashMap<>();
        providers = new HashMap<>();
        commodities = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public Map<Integer, Provider> getProviders() {
        return providers;
    }

    public Map<Integer, Commodity> getCommodities() {
        return commodities;
    }

    public void addUser(User user) throws Exception {
        if (!user.isValidCommand())
            throw new InvalidCommandError();
        else if (user.haveSpecialCharacter())
            throw new UsernameShouldNotContainSpecialCharactersError();
        else {
            if (users.containsKey(user.getUsername())) {
                users.get(user.getUsername()).update(user);
            }
            else {
                users.put(user.getUsername(), user);
            }
        }
    }

    public void addProvider(Provider provider) throws Exception {
        if (!provider.isValidCommand())
            throw new InvalidCommandError();
        else {
            if (providers.containsKey(provider.getId())) {
                providers.get(provider.getId()).update(provider);
            }
            else {
                providers.put(provider.getId(), provider);
            }
        }
    }

    public void addCommodity(Commodity commodity) throws Exception {
        if (!commodity.isValidCommand())
            throw new InvalidCommandError();
        else if (!providers.containsKey(commodity.getProviderId()))
            throw new ProviderNotFoundError();
        else {
            if (commodities.containsKey(commodity.getId())) {
                commodities.get(commodity.getId()).update(commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
            }
            else {
                commodities.put(commodity.getId(), commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
            }
        }
    }

    public List<Commodity> getCommoditiesList(String data) throws Exception {
        if (data.length() > 0)
            throw new InvalidCommandError();

        List<Commodity> commodityList = new ArrayList<>();
        for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
            commodityList.add(entry.getValue());
        }
        return commodityList;
    }

    public void rateCommodity(Rate rate) throws Exception {
        if (!rate.isValidCommand())
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(rate.getUsername()))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(rate.getCommodityId()))
                throw new CommodityNotFoundError();
            else if (!rate.isValidScoreRange())
                throw new InvalidRateScoreError();
            else if (!rate.isValidScoreType())
                throw new InvalidRateScoreError();
            else {
                commodities.get(rate.getCommodityId()).addRate(rate);
            }
        }
    }

    public void addToBuyList(String username, Integer commodityId) throws Exception {
        if (username==null || commodityId==null || commodityId==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(commodityId))
                throw new CommodityNotFoundError();
            else if (commodities.get(commodityId).getInStock() == 0)
                throw new CommodityNotInStuckError();
            else {
                if (users.get(username).isInBuyList((commodityId)))
                    throw new CommodityAlreadyInBuyListError();
                else {
                    users.get(username).addToBuyList(commodityId);
                }
            }
        }
    }

    public void removeFromBuyList(String username, Integer commodityId) throws Exception {
        if (username==null || commodityId==null || commodityId==0.0f)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else if (!commodities.containsKey(commodityId))
                throw new CommodityNotFoundError();
            else {
                if (! users.get(username).isInBuyList((commodityId)))
                    throw new CommodityIsNotInBuyListError();
                else {
                    users.get(username).removeFromBuyList(commodityId);
                }
            }
        }
    }

    public Commodity getCommodityById(Integer id) throws Exception {
        if (id==null || id==0.0f)
            throw new InvalidCommandError();
        else {
            if (!commodities.containsKey(id))
                throw new CommodityNotFoundError();
            else {
                return commodities.get(id);
            }
        }
    }

    public List<Commodity> getCommoditiesByCategory(String category) throws Exception {
        if (category==null || category=="")
            throw new InvalidCommandError();
        else {
            List<Commodity> commoditiesList = new ArrayList<>();
            for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
                if (entry.getValue().isInCategory(category)) {
                    commoditiesList.add(entry.getValue());
                }
            }
            return commoditiesList;
        }
    }

    public List<Commodity> getBuyList(String username) throws Exception {
        if (username==null)
            throw new InvalidCommandError();
        else {
            if (!users.containsKey(username))
                throw new UserNotFoundError();
            else {
                Set<Integer> userBuyList = users.get(username).getBuyList();
                List<Commodity> buyList = new ArrayList<>();
                for (Integer commodityId : userBuyList) {
                    buyList.add(commodities.get(commodityId));
                }
                return buyList;
            }
        }
    }



}
