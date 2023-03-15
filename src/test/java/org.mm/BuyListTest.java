package org.mm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mm.Baloot.Baloot;
import org.mm.Baloot.Exceptions.*;
import org.mm.InterfaceServer.InterfaceServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BuyListTest {
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
    void addToBuyList_InvalidCommand() throws Exception {
        try {
            baloot.addToBuyList(null, (int)0.0f);
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void addToBuyList_UsernameNotFound() throws Exception {
        try {
            baloot.addToBuyList("karim", 1);
        } catch (Exception e) {
            Exception expected = new UserNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void addToBuyList_CommodityNotFound() throws Exception {
        try {
            baloot.addToBuyList("amir", 50);
        } catch (Exception e) {
            Exception expected = new CommodityNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void getBuyList_InvalidCommand() throws Exception {
        try {
            baloot.getBuyList(null);
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void getBuyList_UsernameNotFound() throws Exception {
        try {
            baloot.getBuyList("karim");
        } catch (Exception e) {
            Exception expected = new UserNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void addToBuyList_CommodityAddedToBuyListSuccessfully() throws Exception {
        baloot.addToBuyList("amir", 2);
        assertEquals(1, baloot.getBuyList("amir").size());
        assertEquals(2, baloot.getBuyList("amir").get(0).getId());
    }

    @Test
    void addToBuyList_CommodityAlreadyInBuyListError() throws Exception {
        baloot.addToBuyList("amir", 1);
        try {
            baloot.addToBuyList("amir", 1);
        } catch (Exception e) {
            Exception expected = new CommodityAlreadyInBuyListError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void addToBuyList_CommodityNotInStuckError() throws Exception {
        baloot.addToBuyList("mahdi", 18);
        baloot.addToBuyList("soroosh", 18);
        baloot.addToBuyList("parsa", 18);
        try {
            baloot.addToBuyList("ali", 18);
        } catch (Exception e) {
            Exception expected = new CommodityNotInStuckError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void removeFromBuyList_InvalidCommand() throws Exception {
        try {
            baloot.removeFromBuyList(null, (int)0.0f);
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void removeFromBuyList_UsernameNotFound() throws Exception {
        try {
            baloot.removeFromBuyList("karim", 1);
        } catch (Exception e) {
            Exception expected = new UserNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void removeFromBuyList_CommodityNotFound() throws Exception {
        try {
            baloot.removeFromBuyList("amir", 50);
        } catch (Exception e) {
            Exception expected = new CommodityNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void removeFromBuyList_CommodityIsNotInBuyListError() throws Exception {
        try {
            baloot.removeFromBuyList("amir", 1);
        } catch (Exception e) {
            Exception expected = new CommodityIsNotInBuyListError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void removeFromBuyList_CommodityRemovedFromBuyListSuccessfully() throws Exception {
        baloot.addToBuyList("amir", 2);
        baloot.removeFromBuyList("amir", 2);
        assertEquals(0, baloot.getBuyList("amir").size());
    }

    @Test
    void userBuyListPayment_InvalidCommand() throws Exception {
        try {
            baloot.userBuyListPayment(null);
        } catch (Exception e) {
            Exception expected = new InvalidCommandError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void userBuyListPayment_UsernameNotFound() throws Exception {
        try {
            baloot.userBuyListPayment("karim");
        } catch (Exception e) {
            Exception expected = new UserNotFoundError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void userBuyListPayment_UserBuyListIsEmptyError() throws Exception {
        try {
            baloot.userBuyListPayment("amir");
        } catch (Exception e) {
            Exception expected = new UserBuyListIsEmptyError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void userBuyListPayment_UserNotHaveEnoughCreditError() throws Exception {
        baloot.addToBuyList("amir", 18);
        try {
            baloot.userBuyListPayment("amir");
        } catch (Exception e) {
            Exception expected = new UserNotHaveEnoughCreditError();
            assertEquals(e.getMessage(), expected.getMessage());
        }
    }

    @Test
    void userBuyListPayment_UserBuyListPaymentSuccessfully() throws Exception {
        baloot.addToBuyList("amir", 1);
        assertEquals(1, baloot.getBuyList("amir").size());
        assertEquals(0, baloot.getUserById("amir").getPurchasedList().size());
        baloot.userBuyListPayment("amir");
        assertEquals(0, baloot.getBuyList("amir").size());
        assertEquals(49, baloot.getCommodityById(1).getInStock());
        assertEquals(1, baloot.getUserById("amir").getPurchasedList().size());
    }

}
