package org.mm;

import InterfaceServer.InterfaceServer;

public class Main {
    public static void main(String[] args) {
        final String USERS_URL = "http://5.253.25.110:5000/api/users";
        final String COMMODITIES_URL = "http://5.253.25.110:5000/api/commodities";
        final String PROVIDERS_URL = "http://5.253.25.110:5000/api/providers";
        final String COMMENTS_URL = "http://5.253.25.110:5000/api/comments";
        final int PORT = 8080;

        InterfaceServer interfaceServer = new InterfaceServer();
        interfaceServer.start(USERS_URL, COMMODITIES_URL, PROVIDERS_URL, COMMENTS_URL, PORT);
    }
}
