package org.mm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.*;

public class CommandHandler {
    private Scanner input;
    private Baloot baloot;
    private static ObjectMapper mapper;

    public CommandHandler() {
        baloot = new Baloot();
        input = new Scanner(System.in);
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) throws IOException {
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.run();
    }

    private void run() throws IOException {
        while (true) {
            try {
                String[] commandList = input.nextLine().split(" ", 2);
                List<String> inputList = new ArrayList<String>(Arrays.asList(commandList));
                String command = inputList.size() > 0 ? inputList.get(0) : "";
                String data = inputList.size() > 1 ? inputList.get(1) : "";
                commandHandler(command, data);
            }
            catch (Exception exception) {
                CommandHandler.printOutput(new Response(false, "Invalid command"));
            }
        }
    }

    public void commandHandler(String command,  String data) throws IOException {
        switch (command) {
            case "addUser": {
                baloot.addUser(data);
                break;
            }
            case "addProvider": {
                baloot.addProvider(data);
                break;
            }
            case "addCommodity": {
                baloot.addCommodity(data);
                break;
            }
            case "getCommoditiesList": {
                baloot.getCommoditiesList(data);
                break;
            }
            case "rateCommodity": {
                baloot.rateCommodity(data);
                break;
            }
            case "addToBuyList": {
                baloot.addToBuyList(data);
                break;
            }
            case "removeFromBuyList": {
                baloot.removeFromBuyList(data);
                break;
            }
            case "getCommodityById": {
                baloot.getCommodityById(data);
                break;
            }
            case "getCommoditiesByCategory": {
                baloot.getCommoditiesByCategory(data);
                break;
            }
            case "getBuyList": {
                baloot.getBuyList(data);
                break;
            }
            default: {
                CommandHandler.printOutput(new Response(false, "Invalid command"));
                break;
            }
        }
    }

    public static void printOutput(Response response) throws JsonProcessingException {
        String output = mapper.writeValueAsString(response);
        output = output.replace("\\", "");
        System.out.println(output);
    }

}
