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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        app.get("/commodities", ctx -> {
            try {
                ctx.html(generateCommodities());
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("404.html"));
                ctx.status(404);
            }
        });

        app.get("/commodities/:commodity_id", ctx -> {
            String commodity_id = ctx.pathParam("commodity_id");
            try {
                ctx.html(generateCommodityById(Integer.valueOf(commodity_id)));
            } catch (CommodityNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(403).result(" : " + e.getMessage());
            }
        });

        app.get("/providers/:provider_id", ctx -> {
            String provider_id = ctx.pathParam("provider_id");
            try {
                ctx.html(generateProviderById(Integer.valueOf(provider_id)));
            } catch (ProviderNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(403).result(" : " + e.getMessage());
            }
        });

        app.get("/users/:user_id", ctx -> {
            String user_id = ctx.pathParam("user_id");
            try {
                ctx.html(generateUserById(user_id));
            } catch (UserNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.status(403).result(" : " + e.getMessage());
            }
        });

        app.post("/addCredit" , ctx -> {
            String redirection_path = "/addCredit/" + ctx.formParam("username") + "/" +
                    ctx.formParam("credit");
            ctx.redirect(redirection_path);
        });

        app.get("/addCredit/:username/:credit", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String credit = ctx.pathParam("credit");
                baloot.addCredit(username, Integer.valueOf(credit));
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (InvalidCreditValue e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.post("/addToBuyList" , ctx -> {
            String redirection_path = "/addToBuyList/" + ctx.formParam("username") + "/" +
                    ctx.formParam("commodityId");
            ctx.redirect(redirection_path);
        });

        app.get("/addToBuyList/:username/:commodityId", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commodityId = ctx.pathParam("commodityId");
                baloot.addToBuyList(username, Integer.valueOf(commodityId));
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError | CommodityNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (CommodityNotInStuckError | CommodityAlreadyInBuyListError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.post("/removeFromBuyList" , ctx -> {
            String redirection_path = "/removeFromBuyList/" + ctx.formParam("username") + "/" +
                    ctx.formParam("commodityId");
            ctx.redirect(redirection_path);
        });

        app.get("/removeFromBuyList/:username/:commodityId", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commodityId = ctx.pathParam("commodityId");
                baloot.removeFromBuyList(username, Integer.valueOf(commodityId));
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError | CommodityNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (CommodityIsNotInBuyListError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.post("/rateCommodity" , ctx -> {
            String redirection_path = "/rateCommodity/" + ctx.formParam("username") + "/" +
                    ctx.formParam("commodityId") + "/" + ctx.formParam("rate");
            ctx.redirect(redirection_path);
        });

        app.get("/rateCommodity/:username/:commodityId/:rate", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commodityId = ctx.pathParam("commodityId");
                String rate = ctx.pathParam("rate");
                Rate userRate = new Rate(username, Integer.valueOf(commodityId), Float.valueOf(rate));
                baloot.rateCommodity(userRate);
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError | CommodityNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (InvalidRateScoreError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.post("/voteComment/like" , ctx -> {
            String redirection_path = "/voteComment/" + ctx.formParam("username") + "/" +
                    ctx.formParam("commentId") + "/" + ctx.formParam("vote");
            ctx.redirect(redirection_path);
        });

        app.post("/voteComment/dislike" , ctx -> {
            String redirection_path = "/voteComment/" + ctx.formParam("username") + "/" +
                    ctx.formParam("commentId") + "/" + ctx.formParam("vote");
            ctx.redirect(redirection_path);
        });

        app.get("/voteComment/:username/:commentId/:vote", ctx -> {
            try {
                String username = ctx.pathParam("username");
                String commentId = ctx.pathParam("commentId");
                String vote = ctx.pathParam("vote");
                baloot.voteComment(username, Integer.valueOf(commentId), Integer.valueOf(vote));
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError | CommentNotFound e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (InvalidVoteScoreError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.get("/commodities/search/:start_price/:end_price", ctx -> {
            try {
                String start_price = ctx.pathParam("start_price");
                String end_price = ctx.pathParam("end_price");
                ctx.html(generateCommodityByPriceOrCategory(Integer.valueOf(start_price), Integer.valueOf(end_price), ""));
            } catch (InvalidPriceRangeError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
                ctx.status(403);
            }
        });

        app.get("/commodities/search/:categories", ctx -> {
            try {
                String categories = ctx.pathParam("categories");
                ctx.html(generateCommodityByPriceOrCategory(-1, -1, categories));
            }catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

        app.post("/buyListPayment" , ctx -> {
            String redirection_path = "/buyListPayment/" + ctx.formParam("username");
            ctx.redirect(redirection_path);
        });

        app.get("/buyListPayment/:username", ctx -> {
            try {
                String username = ctx.pathParam("username");
                baloot.userBuyListPayment(username);
                ctx.html(readTemplateFile("200.html"));
                ctx.status(200);
            } catch (UserNotFoundError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("404.html"));
                ctx.status(404).result("404\n" + e.getMessage());
            } catch (UserBuyListIsEmptyError | UserNotHaveEnoughCreditError e) {
                System.out.println(e.getMessage());
//                ctx.html(readTemplateFile("403.html"));
                ctx.status(403).result("403\n" + e.getMessage());
            } catch (Exception e){
                System.out.println(e.getMessage());
                ctx.html(readTemplateFile("403.html"));
            }
        });

    }

    public String generateCommodities() throws Exception{
        String commoditiesHTML = readTemplateFile("commoditiesBefore.html");
        String commodityItem = readTemplateFile("commodityItem.html");

        List<Commodity> CommodityList = baloot.getCommoditiesList();
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

    public String generateCommodityById(Integer commodity_id) throws Exception{
        String commentItem = readTemplateFile("commodityCommentItem.html");

        Commodity commodity = baloot.getCommodityById(commodity_id);
        HashMap<String, String> result = new HashMap<>();
        result.put("id", Integer.toString(commodity.getId()) );
        result.put("name", commodity.getName());
        result.put("providerId", Integer.toString(commodity.getProviderId()) );
        result.put("price", Integer.toString(commodity.getPrice()) );
        result.put("categories", String.join(",", commodity.getCategories()));
        result.put("rating", Float.toString(commodity.getRating()) );
        result.put("inStock", Integer.toString(commodity.getInStock()) );
        String commodityHTML = HTMLHandler.fillTemplate(readTemplateFile("commodityIdPage.html"), result);

        List<Comment> commentList = baloot.getCommentByCommodity(commodity_id);
        HashMap<String, String> result_comment = new HashMap<>();
        for (Comment comment: commentList) {
            result_comment.put("commentId", Integer.toString(comment.getId()));
            result_comment.put("userEmail",comment.getUserEmail());
            result_comment.put("text",comment.getText());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result_comment.put("date",dateFormat.format(comment.getDate()));
            result_comment.put("like", Integer.toString(comment.getLike()));
            result_comment.put("dislike", Integer.toString(comment.getDislike()));
            commodityHTML += HTMLHandler.fillTemplate(commentItem, result_comment);
        }

        commodityHTML += readTemplateFile("commodityCommentAfter.html");
        return commodityHTML;
    }

    public String generateProviderById(Integer provider_id) throws Exception{
        String commodityItem = readTemplateFile("ProviderCommodityItem.html");

        Provider provider = baloot.getProviderById(provider_id);
        HashMap<String, String> result = new HashMap<>();
        result.put("id", Integer.toString(provider.getId()) );
        result.put("name", provider.getName());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.put("registryDate",dateFormat.format(provider.getRegistryDate()));
        String providerHTML = HTMLHandler.fillTemplate(readTemplateFile("ProviderBefore.html"), result);

        Map<Integer, Commodity> commodityList = provider.getCommodities();
        HashMap<String, String> result_commodity = new HashMap<>();
        for (Map.Entry<Integer, Commodity> entry : commodityList.entrySet()) {
            Commodity commodity = entry.getValue();
            result_commodity.put("id", Integer.toString(commodity.getId()) );
            result_commodity.put("name", commodity.getName());
            result_commodity.put("price", Integer.toString(commodity.getPrice()) );
            result_commodity.put("categories", String.join(",", commodity.getCategories()));
            result_commodity.put("rating", Float.toString(commodity.getRating()) );
            result_commodity.put("inStock", Integer.toString(commodity.getInStock()) );

            providerHTML += HTMLHandler.fillTemplate(commodityItem, result_commodity);
        }

        providerHTML += readTemplateFile("providerAfter.html");
        return providerHTML;
    }
    public String generateUserById(String user_id) throws Exception{
        String userBuyItem = readTemplateFile("userBuyList.html");
        String userPurchaseItem = readTemplateFile("userPurchaseListItem.html");

        User user = baloot.getUserById(user_id);
        HashMap<String, String> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("email", user.getEmail());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.put("birthDate",dateFormat.format(user.getBirthDate()));
        result.put("address", user.getAddress());
        result.put("credit", Integer.toString(user.getCredit()));
        String userHTML = HTMLHandler.fillTemplate(readTemplateFile("UserBefore.html"), result);

        Map<Integer, Commodity> buyList = user.getBuyList();
        HashMap<String, String> result_1 = new HashMap<>();
        for (Map.Entry<Integer, Commodity> entry : buyList.entrySet()) {
            Commodity commodity = entry.getValue();
            result_1.put("username", user.getUsername());
            result_1.put("id", Integer.toString(commodity.getId()) );
            result_1.put("name", commodity.getName());
            result_1.put("providerId", Integer.toString(commodity.getProviderId()));
            result_1.put("price", Integer.toString(commodity.getPrice()) );
            result_1.put("categories", String.join(",", commodity.getCategories()));
            result_1.put("rating", Float.toString(commodity.getRating()) );
            result_1.put("inStock", Integer.toString(commodity.getInStock()) );

            userHTML += HTMLHandler.fillTemplate(userBuyItem, result_1);
        }

        userHTML += readTemplateFile("userPurchaseList.html");

        Map<Integer, Commodity> purchaseList = user.getPurchasedListList();
        HashMap<String, String> result_2 = new HashMap<>();
        for (Map.Entry<Integer, Commodity> entry : purchaseList.entrySet()) {
            Commodity commodity = entry.getValue();
            result_2.put("id", Integer.toString(commodity.getId()) );
            result_2.put("name", commodity.getName());
            result_2.put("providerId", Integer.toString(commodity.getProviderId()));
            result_2.put("price", Integer.toString(commodity.getPrice()) );
            result_2.put("categories", String.join(",", commodity.getCategories()));
            result_2.put("rating", Float.toString(commodity.getRating()) );
            result_2.put("inStock", Integer.toString(commodity.getInStock()) );

            userHTML += HTMLHandler.fillTemplate(userPurchaseItem, result_2);
        }

        userHTML += readTemplateFile("userAfter.html");
        return userHTML;
    }

    public String generateCommodityByPriceOrCategory(Integer startPrice, Integer endPrice, String category) throws Exception{
        String commoditiesHTML = readTemplateFile("commoditiesBefore.html");
        String commodityItem = readTemplateFile("commodityItem.html");

        List<Commodity> CommodityList = baloot.getCommoditiesList();
        if (startPrice != -1 && endPrice != -1)
            CommodityList = baloot.getCommoditiesByPrice(startPrice, endPrice);
        else if (!category.equals(""))
            CommodityList = baloot.getCommoditiesByCategory(category);

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

    public Baloot getBaloot() { return baloot;}

    public void stop() {
        app.stop();
    }

}
