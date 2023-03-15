package org.mm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mm.Baloot.Baloot;
import org.mm.Baloot.Commodity;
import org.mm.Baloot.Exceptions.InvalidCommandError;
import org.mm.Baloot.Exceptions.InvalidPriceRangeError;
import org.mm.InterfaceServer.InterfaceServer;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SearchCommodityByPriceTest {
    private InterfaceServer interfaceServer;
    private Baloot baloot;

    @BeforeEach
    void setUp() throws IOException {
        final String USERS_URL = "http://5.253.25.110:5000/api/users";
        final String COMMODITIES_URL = "http://5.253.25.110:5000/api/commodities";
        final String PROVIDERS_URL = "http://5.253.25.110:5000/api/providers";
        final String COMMENTS_URL = "http://5.253.25.110:5000/api/comments";
        final int PORT = 8080;
        interfaceServer = new InterfaceServer();
        interfaceServer.start(USERS_URL, COMMODITIES_URL, PROVIDERS_URL, COMMENTS_URL, PORT);
        baloot = interfaceServer.getBaloot();
    }

    @AfterEach
    void tearDown() {
        interfaceServer.stop();
        interfaceServer = null;
        baloot = null;
    }

    @Test
    void getCommoditiesByPrice_InvalidCommand() throws Exception {
        try {
            baloot.getCommoditiesByPrice(null, null);
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void getCommoditiesByPrice_InvalidPriceRangeError() throws Exception {
        try {
            baloot.getCommoditiesByPrice(3, 1);
        } catch (Exception e) {
            Exception expected = new InvalidPriceRangeError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void getCommoditiesByPrice_ReturnSuccessfully() throws Exception {
        List<Integer> expectedId = Arrays.asList(1, 2, 4, 5, 29);
        List<Commodity> result = baloot.getCommoditiesByPrice(10000, 20000);
        assertEquals(5, result.size());
        for(Commodity commodity : result)
            assertTrue(expectedId.contains(commodity.getId()));
    }

}
