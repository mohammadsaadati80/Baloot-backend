package org.mm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BalootValidTest {

    Baloot baloot;

    @BeforeEach
    void setUp() throws IOException {
        baloot = new Baloot();
        String user = "{\"username\": \"user1\", \"password\": \"1234\", \"email\": \"user@gmail.com\", \"birthDate\": \"1977-09-15\", \"address\": \"address1\", \"credit\": 1500}";
        String provider = "{\"id\": 1, \"name\": \"provider1\", \"registryDate\": \"2023-09-15\"}";
        String commodity = "{\"id\": 1, \"name\": \"Headphone\", \"providerId\": 3, \"price\": 35000, \"categories\": [\"Technology\", \"Phone\"], \"rating\": 8.8, \"inStock\": 50}";
        baloot.addUser(user);
        baloot.addProvider(provider);
        baloot.addCommodity(commodity);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void rateCommodity() throws IOException {
        baloot.rateCommodity("{\"username\": \"user1\", \"commodityId\": 1, \"score\": 8}");
        //check commodity, getter lazeme ezafe she?
    }

    @Test
    void addToBuyList() throws IOException {
        baloot.addToBuyList("{\"username\": \"user1\", \"commodityId\": 1}");
        //check buyList, getter lazeme ezafe she?
    }

    @Test
    void getCommodityById() throws IOException {
        baloot.getCommodityById("{\"id\": 1}");
        //check get?? return lazeme??
    }

    @Test
    void getCommoditiesByCategory() throws IOException {
        baloot.getCommoditiesByCategory("{\"category\": \"Technology\"}");
        //check get?? return lazeme??
    }
}

//balayi copy paste she fqt setupesh nabashe ke valid nabashan va error ha check shavand