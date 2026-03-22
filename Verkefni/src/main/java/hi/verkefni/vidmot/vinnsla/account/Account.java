package hi.verkefni.vidmot.vinnsla;

import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Account {
    private final ObjectMapper map = new ObjectMapper();
    private final File file = new File("src/main/resources/data.json");

    private ArrayNode accounts;
    private JsonNode accountRoot;

    public Account() throws IOException {
        accountRoot = map.readTree(file);
        accounts = (ArrayNode) accountRoot.get("Accounts");
    }

    public int accountSize() {
        return accounts.size();
    }

    public boolean isAccountAvailable(String name) {
        name = name.toLowerCase();
        for(JsonNode acc : accounts) {
            String checker = acc.get("AccountInfo").get("name").asText().toLowerCase();
            if(checker.equals(name))
                return true;
        }
        return false;
    }

    /**
     * Bætir til nýja reiking í kerfinu okkar
     * @param account
     * @param password
     * @throws IOException
     */
    public void newAccount(String account, String password) throws IOException {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
        String numbers = "0123456789";
        ObjectNode newUser = map.createObjectNode();
        boolean hasSpecial = false;
        boolean hasNumber = false;

        if(password.length() >= 6) {
            for (int i = 0; i < password.length(); i++) {
                if(specialChars.indexOf(password.charAt(i)) != -1) {
                    hasSpecial = true;
                }
                if(numbers.indexOf(password.charAt(i)) != -1) {
                    hasNumber = true;
                }
            }

            if(hasSpecial && hasNumber) {
                System.out.println("Valid password created");

                newUser.put("name", account);
                newUser.put("password", password);

                accounts.add(newUser);
                map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
                // direct to main-view with the current account
                return;
            } else {
                System.out.println("Invalid password created");
                return;
            }
        } else {
            System.out.println("Password too short");
        }
    }

    /**
     * Checks if the user can be found in the system
     * @param username
     */
    public void checkAccount(String username) {
        // add later
    }
}