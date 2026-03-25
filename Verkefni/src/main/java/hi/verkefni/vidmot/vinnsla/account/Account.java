package hi.verkefni.vidmot.vinnsla.account;

// IO imports
import java.io.File;
import java.io.IOException;

// Util imports
import java.util.Iterator;
import java.util.Map;

// fasterxml imports
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Account {
    private final ObjectMapper map = new ObjectMapper();
    private final File file = new File("src/main/resources/data.json");

    private ObjectNode accounts;
    private JsonNode accountRoot;
    private JsonNode currentSignedAccount;

    private boolean isAccountSignedIn = false;

    /**
     * Constructs a default account.
     * @throws IOException
     */
    public Account() throws IOException {
        accountRoot = map.readTree(file);
        accounts = (ObjectNode) accountRoot.get("Accounts");
        currentSignedAccount = null;
    }

    /**
     * Gets the size of the entire json account system
     * @return accounts
     */
    public int accountSize() {
        return accounts.size();
    }

    /**
     * This method is used for the program to check if the current account name given is in the system or not.
     * @param name of the username to check
     * @return true or false of the account
     */
    public boolean accountExists(String name) {
        name = name.toLowerCase();
        for (JsonNode acc : accounts) {
            String checker = acc.get("AccountInfo").get("name").asText().toLowerCase();
            if (checker.equals(name))
                return true;
        }
        return false;
    }

    public boolean signIn(String username, String password) {
        for (JsonNode acc : accounts) {
            JsonNode info = acc.get("AccountInfo");
            String storedUser = info.get("name").asText();
            String storedPass = info.get("password").asText();

            if (storedUser.equals(username) && storedPass.equals(password)) {
                currentSignedAccount = acc;
                isAccountSignedIn = true;
                return true;
            }
        }
        isAccountSignedIn = false;
        return false;
    }

    public void logOut() {
        if (currentSignedAccount != null && isAccountSignedIn) {
            currentSignedAccount = null;
            isAccountSignedIn = false;
            return;
        } else {
            System.out.println("System error: attempted to sign out but account cannot be found");
            return;
        }
    }

    /**
     * Adds a brand new account to the system. There is a security system added to make sure that the password passes
     * the current security regulations.
     * @param account of the account to add
     * @param password of the account to add
     * @throws IOException
     */
    public boolean newAccount(String account, String password) throws IOException {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
        String numbers = "0123456789";
        boolean hasSpecial = false;
        boolean hasNumber = false;

        // prevent dupe accounts
        if(accountExists(account)) {
            System.out.println("Account already exists");
            return false;
        }

        if (password.length() >= 6) {
            for (int i = 0; i < password.length(); i++) {
                if (specialChars.indexOf(password.charAt(i)) != -1) {
                    hasSpecial = true;
                }
                if (numbers.indexOf(password.charAt(i)) != -1) {
                    hasNumber = true;
                }
            }

            if (hasSpecial && hasNumber) {
                System.out.println("Valid password created");

                ObjectNode accountInfo = map.createObjectNode();
                accountInfo.put("name", account);
                accountInfo.put("password", password);

                ObjectNode newAccountInfo = map.createObjectNode();
                newAccountInfo.put("AccountInfo", accountInfo);
                newAccountInfo.put("Trips", map.createObjectNode());

                String nextId = String.valueOf(accounts.size() + 1);
                accounts.put(nextId, newAccountInfo);

                map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
                // Did the account get added?
                if(!accountExists(account)) {
                    System.err.println("CRITICAL ERROR: Account creation failed!");
                    throw new IOException("Account creation failed!");
                }

                signIn(account, password);
                return true;
            } else {
                System.out.println("Invalid password created");
                return false;
            }
        } else {
            System.out.println("Password too short");
            return false;
        }
    }

    /**
     * Checks if the account is currently signed in or not. Used for some scenarios where we wanna for example add an
     * account.
     * @return to see if the account is in or not
     */
    public String getSignedAccount() {
        if (currentSignedAccount == null || !isAccountSignedIn) {
            return "No account found";
        }
        return currentSignedAccount.get("AccountInfo").get("name").asText();
    }

    /**
     * Deletes the current user account upon request.
     * @param account to delete
     * @throws IOException
     */
    public void deleteAccount(String account) throws IOException {
        Iterator<Map.Entry<String, JsonNode>> fields = accounts.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            JsonNode accountInfo = entry.getValue().get("AccountInfo");
            if(accountInfo == null || accountInfo.get("name") == null) return;
            String storedUser = accountInfo.get("name").asText();

            if(storedUser.equals(account)) {
                accounts.remove(entry.getKey());
                map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
                System.out.println("Account deleted");
                return;
            }
        }
    }
}