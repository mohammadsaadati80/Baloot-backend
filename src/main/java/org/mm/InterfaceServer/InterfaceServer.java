package org.mm.InterfaceServer;

import org.mm.Baloot.Baloot;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;
import org.mm.Baloot.Commodity;
import org.mm.Baloot.Provider;
import org.mm.Baloot.User;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Arrays;

public class InterfaceServer {
    private Javalin app;
    private Baloot baloot = new Baloot();
    private HTMLHandler HTMLHandler = new HTMLHandler();

    public void start(final String USERS_URL, final String COMMODITIES_URL, final String PROVIDERS_URL, final String COMMENTS_URL, int port) {
        try {
            System.out.println("Importing Users...");
            importUsersFromWeb(USERS_URL);
            System.out.println("Importing Commodities...");
            importCommoditiesFromWeb(COMMODITIES_URL);
            System.out.println("Importing Providers...");
            importProvidersFromWeb(PROVIDERS_URL);
//            System.out.println("Importing Comments...");
//            importCommentsFromWeb(COMMENTS_URL);
            runServer(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runServer(int port) {
    }

    private void importUsersFromWeb(String usersUrl) throws Exception{
        String UsersJsonString = HTTPRequestHandler.getRequest(usersURL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<User> users = gson.fromJson(UsersJsonString, new TypeToken<List<User>>() {}.getType());
        for (User user : users) {
            try {
                baloot.addUser(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void importCommoditiesFromWeb(String commoditiesUrl) throws Exception{
        String CommoditiesJsonString = HTTPRequestHandler.getRequest(commoditiesURL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Commodity> commodities = gson.fromJson(CommoditiesJsonString, new TypeToken<List<Commodity>>() {}.getType());
        for (Commodity commodity : commodities) {
            try {
                baloot.addCommodity(commodity);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void importProvidersFromWeb(String providersUrl) throws Exception{
        String ProvidersJsonString = HTTPRequestHandler.getRequest(providersURL);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Provider> providers = gson.fromJson(ProvidersJsonString, new TypeToken<List<Commodity>>() {}.getType());
        for (Provider provider : providers) {
            try {
                baloot.addProvider(provider);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

//    private void importCommentsFromWeb(String commentsURL) throws Exception{
//        String CommentsJsonString = HTTPRequestHandler.getRequest(commentsURL);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Commodity>>() {}.getType());
//        for (Comment comment : comments) {
//            try {
//                baloot.addComment(comment);
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//        }
//    }

    private String readTemplateFile(String fileName) throws Exception{
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public void stop() {
        app.stop();
    }

}
