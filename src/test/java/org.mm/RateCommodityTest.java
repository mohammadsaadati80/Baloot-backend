package org.mm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mm.Baloot.Baloot;
import org.mm.Baloot.Exceptions.*;
import org.mm.Baloot.Rate;
import org.mm.InterfaceServer.InterfaceServer;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class RateCommodityTest {
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
    void rateCommodity_InvalidCommand() throws Exception {
        try {
            baloot.rateCommodity(new Rate(null, null, 0.0f));
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_UsernameNotFound() throws Exception {
        try {
            baloot.rateCommodity(new Rate("karim", 1, 5));
        } catch (Exception e) {
            Exception expected = new UserNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_CommodityNotFound() throws Exception {
        try {
            baloot.rateCommodity(new Rate("amir", 50, 5));
        } catch (Exception e) {
            Exception expected = new CommodityNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_InvalidRateScore_LessThan1() throws Exception {
        try {
            baloot.rateCommodity(new Rate("amir", 1, -1));
        } catch (Exception e) {
            Exception expected = new InvalidRateScoreError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_InvalidRateScore_MoreThan10() throws Exception {
        try {
            baloot.rateCommodity(new Rate("amir", 1, 11));
        } catch (Exception e) {
            Exception expected = new InvalidRateScoreError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_InvalidRateScore_NotInteger() throws Exception {
        try {
            baloot.rateCommodity(new Rate("amir", 1, (float)5.5));
        } catch (Exception e) {
            Exception expected = new InvalidRateScoreError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void rateCommodity_CommodityRatedSuccessfully() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        assertEquals(5, baloot.getCommodityById(1).getRating());
    }

    @Test
    void rateCommodity_AddUserRate() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        Integer rate = baloot.getCommodities().get(1).getUserRate("amir");
        assertEquals(5, rate);
    }

    @Test
    void rateCommodity_UpdateUserRate() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        baloot.rateCommodity(new Rate("amir", 1, 7));
        Integer rate = baloot.getCommodities().get(1).getUserRate("amir");
        assertEquals(7, rate);
    }

    @Test
    void rateCommodity_CommodityRate_Average() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        baloot.rateCommodity(new Rate("hamid", 1, 7));
        float rate = baloot.getCommodities().get(1).getRating();
        assertEquals(6, rate);
    }

    @Test
    void rateCommodity_CommodityRate_UpdateAverage() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        baloot.rateCommodity(new Rate("hamid", 1, 7));
        baloot.rateCommodity(new Rate("amir", 1, 8));
        float rate = baloot.getCommodities().get(1).getRating();
        assertEquals(7.5, rate);
    }

    @Test
    void rateCommodity_CommodityRate_ProviderAverage() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        baloot.rateCommodity(new Rate("hamid", 1, 1));
        float rate = baloot.getProviders().get(1).getAverageCommoditiesRates();
        assertEquals(6.4375, rate);
    }

    @Test
    void rateCommodity_CommodityRate_UpdateProviderAverage() throws Exception {
        baloot.rateCommodity(new Rate("amir", 1, 5));
        baloot.rateCommodity(new Rate("hamid", 1, 7));
        baloot.rateCommodity(new Rate("amir", 1, 9));
        float rate = baloot.getProviders().get(1).getAverageCommoditiesRates();
        assertEquals(7.0625, rate);
    }

}
