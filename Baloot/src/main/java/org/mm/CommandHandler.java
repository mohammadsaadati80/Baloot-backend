package org.mm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
                List<String> inputArr = getInput();
                String command = inputArr.get(0);
                String data = "";
                if (inputArr.size() > 1)
                    data = inputArr.get(1);
                commandHandler(command, data);
            }
            catch (Exception exception) {
                CommandHandler.printOutput(new Response(false, "InvalidCommand"));
            }
        }
    }

    private List<String> getInput() {
        List <String> inputCommand = new ArrayList<>();
        String s = input.nextLine();
        String[] commandArray = s.split(" ", 2);
        for (int i=0; i<commandArray.length; i++)
            inputCommand.add(commandArray[i]);

        return inputCommand;
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

            default: {
                CommandHandler.printOutput(new Response(false, "InvalidCommand"));
                break;
            }
        }
    }

    public static void printOutput(Response output) throws JsonProcessingException {
        String print = mapper.writeValueAsString(output);
        print = print.replace("\\", "");
        System.out.println(print);
    }

}
