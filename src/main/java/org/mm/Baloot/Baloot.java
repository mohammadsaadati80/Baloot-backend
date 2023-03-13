package org.mm.Baloot;

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

    public void addUser(String data) throws IOException {
        User user = mapper.readValue(data, User.class);

        if (!user.isValidCommand())
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else if (user.haveSpecialCharacter())
            CommandHandler.printOutput(new Response(false, "Username should not contain special characters"));
        else {
            if (users.containsKey(user.getUsername())) {
                users.get(user.getUsername()).update(user);
                CommandHandler.printOutput(new Response(true, "User updated successfully"));
            }
            else {
                users.put(user.getUsername(), user);
                CommandHandler.printOutput(new Response(true, "User added successfully"));
            }
        }
    }

    public void addProvider(String data) throws IOException {
        Provider provider = mapper.readValue(data, Provider.class);

        if (!provider.isValidCommand())
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (providers.containsKey(provider.getId())) {
                providers.get(provider.getId()).update(provider);
                CommandHandler.printOutput(new Response(true, "Provider updated successfully"));
            }
            else {
                providers.put(provider.getId(), provider);
                CommandHandler.printOutput(new Response(true, "Provider added successfully"));
            }
        }
    }

    public void addCommodity(String data) throws IOException {
        Commodity commodity = mapper.readValue(data, Commodity.class);

        if (!commodity.isValidCommand())
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else if (!providers.containsKey(commodity.getProviderId()))
            CommandHandler.printOutput(new Response(false, "Provider not found"));
        else {
            if (commodities.containsKey(commodity.getId())) {
                commodities.get(commodity.getId()).update(commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
                CommandHandler.printOutput(new Response(true, "Commodity updated successfully"));
            }
            else {
                commodities.put(commodity.getId(), commodity);
                providers.get(commodity.getProviderId()).addCommodity(commodity);
                CommandHandler.printOutput(new Response(true, "Commodity added successfully"));
            }
        }
    }

    public void getCommoditiesList(String data) throws JsonProcessingException {
        if (data.length() > 0)
            CommandHandler.printOutput(new Response(false, "Invalid command"));

        List<ObjectNode> objects = new ArrayList<>();
        for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
            ObjectNode commodity = mapper.createObjectNode();
            entry.getValue().toJson(mapper, commodity, true);
            objects.add(commodity);
        }
        ArrayNode arrayNode = mapper.valueToTree(objects);
        ObjectNode commodityList = mapper.createObjectNode();
        commodityList.putArray("commoditiesList").addAll(arrayNode);
        String outputData = mapper.writeValueAsString(commodityList); //TODO
        CommandHandler.printOutput(new Response(true, outputData));
    }

    public void rateCommodity(String data) throws IOException {
        Rate rate = mapper.readValue(data, Rate.class);
        if (!rate.isValidCommand())
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (!users.containsKey(rate.getUsername()))
                CommandHandler.printOutput(new Response(false, "Username not found"));
            else if (!commodities.containsKey(rate.getCommodityId()))
                CommandHandler.printOutput(new Response(false, "Commodity not found"));
            else if (!rate.isValidScoreRange())
                CommandHandler.printOutput(new Response(false, "The score must be between 1 and 10"));
            else if (!rate.isValidScoreType())
                CommandHandler.printOutput(new Response(false, "The score must be an integer"));
            else {
                commodities.get(rate.getCommodityId()).addRate(rate);
                CommandHandler.printOutput(new Response(true, "Commodity rated successfully"));
            }
        }
    }

    public void addToBuyList(String data) throws IOException {
        JsonNode jsonNode = mapper.readTree(data);
        String username = jsonNode.get("username").asText();
        Integer commodityId = jsonNode.get("commodityId").asInt();

        if (username==null || commodityId==null || commodityId==0.0f)
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (!users.containsKey(username))
                CommandHandler.printOutput(new Response(false, "Username not found"));
            else if (!commodities.containsKey(commodityId))
                CommandHandler.printOutput(new Response(false, "Commodity not found"));
            else if (commodities.get(commodityId).getInStock() == 0)
                CommandHandler.printOutput(new Response(false, "Commodity is not available in stock"));
            else {
                if (users.get(username).isInBuyList((commodityId)))
                    CommandHandler.printOutput(new Response(false, "Commodity already in BuyList"));
                else {
                    users.get(username).addToBuyList(commodityId);
                    CommandHandler.printOutput(new Response(true, "Commodity added to BuyList successfully"));
                }
            }
        }
    }

    public void removeFromBuyList(String data) throws IOException {
        JsonNode jsonNode = mapper.readTree(data);
        String username = jsonNode.get("username").asText();
        Integer commodityId = jsonNode.get("commodityId").asInt();

        if (username==null || commodityId==null || commodityId==0.0f)
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (!users.containsKey(username))
                CommandHandler.printOutput(new Response(false, "Username not found"));
            else if (!commodities.containsKey(commodityId))
                CommandHandler.printOutput(new Response(false, "Commodity not found"));
            else {
                if (! users.get(username).isInBuyList((commodityId)))
                    CommandHandler.printOutput(new Response(false, "Commodity is not in BuyList"));
                else {
                    users.get(username).removeFromBuyList(commodityId);
                    CommandHandler.printOutput(new Response(true, "Commodity removed from Buylist successfully"));
                }
            }
        }
    }

    public void getCommodityById(String data) throws IOException {
        Integer id = mapper.readTree(data).get("id").asInt();
        if (id==null || id==0.0f)
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (!commodities.containsKey(id))
                CommandHandler.printOutput(new Response(false, "Commodity not found"));
            else {
                ObjectNode commodity = mapper.createObjectNode();
                commodities.get(id).toJson(mapper, commodity, false);
                String outputData = mapper.writeValueAsString(commodity);
                CommandHandler.printOutput(new Response(true, outputData));
            }
        }
    }

    public void getCommoditiesByCategory(String data) throws IOException {
        String category = mapper.readTree(data).get("category").asText();

        if (category==null || category=="")
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            List<ObjectNode> commoditiesObjectNode = new ArrayList<>();
            for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
                if (entry.getValue().isInCategory(category)) {
                    ObjectNode commodity = mapper.createObjectNode();
                    entry.getValue().toJson(mapper, commodity, false);
                    commoditiesObjectNode.add(commodity);
                }
            }
            ObjectNode commoditiesListByCategory = mapper.createObjectNode();
            ArrayNode commoditiesArrayNode = mapper.valueToTree(commoditiesObjectNode);
            commoditiesListByCategory.putArray("commoditiesListByCategory").addAll(commoditiesArrayNode);
            String outputData = mapper.writeValueAsString(commoditiesListByCategory); //TODO
            CommandHandler.printOutput(new Response(true, outputData));
        }
    }

    public void getBuyList(String data) throws IOException {
        String username = mapper.readTree(data).get("username").asText();
        if (username==null)
            CommandHandler.printOutput(new Response(false, "Invalid command"));
        else {
            if (!users.containsKey(username))
                CommandHandler.printOutput(new Response(false, "Username not found"));
            else {
                Set<Integer> buyList = users.get(username).getBuyList();
                ObjectNode buyListNode = mapper.createObjectNode();
                List<ObjectNode> buyListObjectNode = new ArrayList<>();
                for (Integer commodityId : buyList) {
                    ObjectNode commodity = mapper.createObjectNode();
                    commodities.get(commodityId).toJson(mapper, commodity, false);
                    buyListObjectNode.add(commodity);
                }
                ArrayNode arrayNode = mapper.valueToTree(buyListObjectNode);
                buyListNode.putArray("buyList").addAll(arrayNode);
                String outputData = mapper.writeValueAsString(buyListNode); // TODO
                CommandHandler.printOutput(new Response(true, outputData));
            }
        }
    }

}
