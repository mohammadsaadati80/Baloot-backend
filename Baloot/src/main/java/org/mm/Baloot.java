package org.mm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

@JsonIgnoreProperties(ignoreUnknown = true)
public class Baloot {

    private ObjectMapper mapper;
    private Map<String, User> users;
    private Map<Integer, Provider> providers;
    private Map<Integer, Commodity> commodities;

    public Baloot() {
        mapper = new ObjectMapper();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        mapper.setDateFormat(df);
        users = new HashMap<>();
        providers = new HashMap<>();
        commodities = new HashMap<>();
    }

    public void addUser(String data) throws IOException {
        User user = mapper.readValue(data, User.class);

        if (!user.isValidCommand())
            CommandHandler.printOutput(new Response(false, "InvalidCommand"));
        else {
            if (users.containsKey(user.getUsername())) {
                users.get(user.getUsername()).update(user);
                CommandHandler.printOutput(new Response(true, "User updated successfully"));
            }
            else {
                user.initialValues();
                users.put(user.getUsername(), user);
                CommandHandler.printOutput(new Response(true, "User added successfully"));
            }

        }
    }

    public void addProvider(String data) throws IOException {
        Provider provider = mapper.readValue(data, Provider.class);

        if (!provider.isValidCommand())
            CommandHandler.printOutput(new Response(false, "InvalidCommand"));
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
            CommandHandler.printOutput(new Response(false, "InvalidCommand"));
        else if (providers.containsKey(commodity.getProviderId())) //TODO
            CommandHandler.printOutput(new Response(false, "Provider not found"));
        else {
            if (commodities.containsKey(commodity.getId())) {
                commodities.get(commodity.getId()).update(commodity);
                CommandHandler.printOutput(new Response(true, "Commodity updated successfully"));
            }
            else {
                commodities.put(commodity.getId(), commodity);
                CommandHandler.printOutput(new Response(true, "Commodity added successfully"));
            }
        }
    }

    public void getCommoditiesList(String inputData) throws JsonProcessingException {
        if (inputData.length() > 0)
            CommandHandler.printOutput(new Response(false, "InvalidCommand"));

        List<ObjectNode> objects = new ArrayList<>();
        for (Map.Entry<Integer, Commodity> entry : commodities.entrySet()) {
            ObjectNode commodity = mapper.createObjectNode();
            entry.getValue().toJson(mapper, commodity);
            objects.add(commodity);
        }
        ArrayNode arrayNode = mapper.valueToTree(objects);
        ObjectNode commodityList = mapper.createObjectNode();
        commodityList.putArray("commoditiesList").addAll(arrayNode);
        String data = mapper.writeValueAsString(commodityList); // TODO
        CommandHandler.printOutput(new Response(true, data));
    }

}
