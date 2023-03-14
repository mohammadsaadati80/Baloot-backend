package org.mm.InterfaceServer;

import org.mm.Baloot.*;
import org.mm.Baloot.Exceptions.*;
import org.mm.HTTPRequestHandler.HTTPRequestHandler;

import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.javalin.Javalin;
import org.apache.commons.io.FileUtils;

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
            System.out.println("Importing Providers...");
            importProvidersFromWeb(PROVIDERS_URL);
            System.out.println("Importing Commodities...");
            importCommoditiesFromWeb(COMMODITIES_URL);
            System.out.println("Importing Comments...");
            importCommentsFromWeb(COMMENTS_URL);
            runServer(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runServer(int port) {
        app = Javalin.create().start(port);
//        app.get("/", ctx -> ctx.html(readResourceFile("homePage.html")));
//        app.get("/hello/:name", ctx -> {
//            ctx.result("Hello: " + ctx.pathParam("name"));
//        });

        app.get("/commodities", ctx -> {
            try {
                ctx.html(generateCommodities());
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(502);
            }
        });

//        app.get("/commodities/:commodity_id", ctx -> {
//            String commodity_id = ctx.pathParam("commodity_id");
//            try {
//                ctx.html(generateCommoditiesById(commodity_id));
//            } catch (CommodityNotFoundError e) {
//                ctx.html(readTemplateFile("404.html"));
//            } catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502).result(":| " + e.getMessage());
//            }
//        });


//        app.get("profile/:email", ctx -> {
//            try {
//                ctx.html(generateGetUserProfile());
//            }catch (Exception e){
//                System.out.println(e.getMessage());
//                ctx.status(502).result(Integer.toString(ctx.status()) + ":| " + e.getMessage());
//            }
//        });
    }


    public String generateCommodities() throws Exception{
        String commoditiesHTML = readTemplateFile("commoditiesBefore.html");
        List<Commodity> CommodityList = baloot.getCommoditiesList();
        String commodityItem = readTemplateFile("commodityItem.html");
        int counter = 1;
        for(Commodity commodity: CommodityList){
            HashMap<String, String> result = new HashMap<>();
            result.put("id", Integer.toString(commodity.getId()) );
            result.put("name", commodity.getName());
            result.put("providerId", Integer.toString(commodity.getProviderId()) );
            result.put("price", Integer.toString(commodity.getPrice()) );
            result.put("categories", String.join(",", commodity.getCategories()));
            result.put("rating", Float.toString(commodity.getRating()) );
            result.put("inStock", Integer.toString(commodity.getInStock()) );

            commoditiesHTML += HTMLHandler.fillTemplate(commodityItem, result);
        }
        commoditiesHTML += readTemplateFile("commoditiesAfter.html");
        return commoditiesHTML;
    }

//    private String generateCommoditiesById(String commodity_id) throws Exception {
//        Student student = bolbolestan.getStudentById(studentId);
//        String profileHTML = readHTMLPage("profile_start.html");
//
//        HashMap<String, String> studentProfile = new HashMap<>();
//        studentProfile.put("id", student.getId());
//        studentProfile.put("name", student.getName());
//        studentProfile.put("secondName", student.getSecondName());
//        studentProfile.put("birthDate", student.getBirthDate());
//        studentProfile.put("GPA", Float.toString(student.getGPA()));
//        studentProfile.put("totalUnits", Integer.toString(bolbolestan.getUnitsPassed(studentId)));
//
//        profileHTML = HTMLHandler.fillTemplate(profileHTML, studentProfile);
//        String profileItem = readHTMLPage("profile_item.html");
//        for (Grade grade : student.getGrades()) {
//            studentProfile = new HashMap<>();
//            studentProfile.put("code", grade.getCode());
//            studentProfile.put("grade", Integer.toString(grade.getGrade()));
//            profileHTML += HTMLHandler.fillTemplate(profileItem, studentProfile);
//        }
//        return profileHTML + readHTMLPage("profile_end.html");
//    }

    private void importUsersFromWeb(String usersUrl) throws Exception{
        String UsersJsonString = HTTPRequestHandler.getRequest(usersUrl);
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
        String CommoditiesJsonString = HTTPRequestHandler.getRequest(commoditiesUrl);
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
        String ProvidersJsonString = HTTPRequestHandler.getRequest(providersUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Provider> providers = gson.fromJson(ProvidersJsonString, new TypeToken<List<Provider>>() {}.getType());
        for (Provider provider : providers) {
            try {
                baloot.addProvider(provider);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void importCommentsFromWeb(String commentsUrl) throws Exception{
        String CommentsJsonString = HTTPRequestHandler.getRequest(commentsUrl);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Comment> comments = gson.fromJson(CommentsJsonString, new TypeToken<List<Comment>>() {}.getType());
        for (Comment comment : comments) {
            try {
                baloot.addComment(comment);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private String readTemplateFile(String fileName) throws Exception{
        File file = new File(Resources.getResource("templates/" + fileName).toURI());
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public void stop() {
        app.stop();
    }

}
