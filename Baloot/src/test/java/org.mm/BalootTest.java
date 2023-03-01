package org.mm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BalootTest {

    private CommandHandler commandHandler;
    private Baloot baloot;
    private ObjectMapper mapper;

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() throws IOException {
        commandHandler = new CommandHandler();
        baloot = new Baloot();
        mapper = new ObjectMapper();

        String user = "{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", \"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}";
        baloot.addUser(user);
        String provider = "{\"id\": 1, \"name\": \"provider1\", \"registryDate\": \"2023-09-15\"}";
        baloot.addProvider(provider);
        String commodity = "{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 1, \"price\": 35000, \"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 50}";
        baloot.addCommodity(commodity);
        String commodity2 = "{\"id\": 2, \"name\": \"Mouse\", \"providerId\": 1, \"price\": 6000, \"categories\": [\"Technology\"], \"rating\": 4, \"inStock\": 0}";
        baloot.addCommodity(commodity2);

        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    void tearDown() {
        commandHandler = null;
        baloot = null;
        mapper = null;

        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void rateCommodity_InvalidCommand() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Invalid command\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_UsernameNotFound() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Username not found\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user2\", \"commodityId\": 1, \"score\": 7}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_CommodityNotFound() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Commodity not found\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 3, \"score\": 7}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_InvalidRateScore_LessThan1() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"The score must be between 1 and 10\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 0.5}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_InvalidRateScore_MoreThan10() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"The score must be between 1 and 10\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 11}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_InvalidRateScore_NotInteger() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"The score must be an integer\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5.5}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_CommodityRatedSuccessfully() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"Commodity rated successfully\"}" + "\r\n";
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void rateCommodity_AddUserRate() throws IOException {
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5}");
        Integer rate = baloot.getCommodities().get(1).getUserRate("user1");
        assertEquals(5, rate);
    }

    @Test
    void rateCommodity_UpdateUserRate() throws IOException {
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5}");
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 7}");
        Integer rate = baloot.getCommodities().get(1).getUserRate("user1");
        assertEquals(7, rate);
    }

    @Test
    void rateCommodity_CommodityRate_Average() throws IOException {
        String user2 = "{\"username\": \"user2\", \"password\": \"1234\", \"email\": \"user@gmail.com\", \"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}";
        baloot.addUser(user2);
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5}");
        baloot.rateCommodity("{\"username\": \"user2\", \"commodityId\": 1, \"score\": 7}");
        float rate = baloot.getCommodities().get(1).getRating();
        assertEquals(6, rate);
    }

    @Test
    void rateCommodity_CommodityRate_UpdateAverage() throws IOException {
        String user2 = "{\"username\": \"user2\", \"password\": \"1234\", \"email\": \"user@gmail.com\", \"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}";
        baloot.addUser(user2);
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 5}");
        baloot.rateCommodity("{\"username\": \"user2\", \"commodityId\": 1, \"score\": 7}");
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 8}");
        float rate = baloot.getCommodities().get(1).getRating();
        assertEquals(7.5, rate);
    }

    @Test
    void addToBuyList_InvalidCommand() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Invalid command\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": null}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void addToBuyList_UsernameNotFound() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Username not found\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user2\", \"commodityId\": 1}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void addToBuyList_CommodityNotFound() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Commodity not found\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 3}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void addToBuyList_CommodityIsNotAvailableInStock() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Commodity is not available in stock\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 2}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void addToBuyList_CommodityAddedToBuyListSuccessfully() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"Commodity added to BuyList successfully\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void addToBuyList_CommodityAlreadyInBuyList() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"Commodity added to BuyList successfully\"}" + "\r\n" +
                "{\"success\":false,\"data\":\"Commodity already in BuyList\"}" + "\r\n";
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommodityById_InvalidCommand() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Invalid command\"}" + "\r\n";
        baloot.getCommodityById("{\"id\": \"\"}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommodityById_CommodityNotFound() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Commodity not found\"}" + "\r\n";
        baloot.getCommodityById("{\"id\": 3}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommodityById_CommodityFoundByIdSuccessfully() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"{\"id\":1,\"name\":\"Headphone\",\"providerId\":1,\"price\":35000,\"genres\":[\"Technology\",\"Phone\"],\"rating\":8.8}\"}" + "\r\n";
        baloot.getCommodityById("{\"id\": 1}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommoditiesByCategory_InvalidCommand() throws IOException {
        String expectedOutput = "{\"success\":false,\"data\":\"Invalid command\"}" + "\r\n";
        baloot.getCommoditiesByCategory("{\"category\": \"\"}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommoditiesByCategory_EmptyResult() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"{\"commoditiesListByCategory\":[]}\"}" + "\r\n";
        baloot.getCommoditiesByCategory("{\"category\": \"Vegetables\"}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommoditiesByCategory_SingleResult() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"{\"commoditiesListByCategory\":[{\"id\":1,\"name\":\"Headphone\",\"providerId\":1,\"price\":35000,\"genres\":[\"Technology\",\"Phone\"],\"rating\":8.8}]}\"}" + "\r\n";
        baloot.getCommoditiesByCategory("{\"category\": \"Phone\"}");
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    void getCommoditiesByCategory_MultipleResult() throws IOException {
        String expectedOutput = "{\"success\":true,\"data\":\"{\"commoditiesListByCategory\":[{\"id\":1,\"name\":\"Headphone\",\"providerId\":1,\"price\":35000,\"genres\":[\"Technology\",\"Phone\"],\"rating\":8.8}," +
                "{\"id\":2,\"name\":\"Mouse\",\"providerId\":1,\"price\":6000,\"genres\":[\"Technology\"],\"rating\":4.0}]}\"}" + "\r\n";
        baloot.getCommoditiesByCategory("{\"category\": \"Technology\"}");
        assertEquals(expectedOutput, out.toString());
    }
    // TODO Combined tests
}
