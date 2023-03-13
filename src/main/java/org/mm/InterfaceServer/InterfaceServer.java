package org.mm.InterfaceServer;

import Baloot.Baloot;

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
            System.out.println("Importing Commodities...");
            importCommoditiesFromWeb(COMMODITIES_URL);
            System.out.println("Importing Providers...");
            importProvidersFromWeb(PROVIDERS_URL);
            System.out.println("Importing Comments...");
            importCommentsFromWeb(COMMENTS_URL);
            runServer(port);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void runServer(int port) {
    }

    private void importUsersFromWeb(String providers_url) {
    }

    private void importCommoditiesFromWeb(String providers_url) {
    }

    private void importProvidersFromWeb(String providers_url) {
    }

    private void importCommentsFromWeb(String comments_url) {

    }


}
