package org.mm;

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

@JsonIgnoreProperties(ignoreUnknown = true)
public class Baloot {

    private ObjectMapper mapper;
    private Map<String, User> users;
//    private Map<Integer, Provider> providers;
//    private Map<Integer, Commodity> commodities;

    public Baloot() {
        mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        users = new HashMap<>();
    }

    public void addUser(String data) throws IOException {
        User user = mapper.readValue(data, User.class);
        int a = 2;
        if (! user.isValidCommand())
            CommandHandler.printOutput(new Response(false, "InvalidCommand"));
        else {
            if (users.containsKey(user.getUsername()))
                users.get(user.getUsername()).update(user);
            else {
//                user.initialValues();
                users.put(user.getUsername(), user);
            }
            CommandHandler.printOutput(new Response(true, "User added successfully"));
        }
    }

}
