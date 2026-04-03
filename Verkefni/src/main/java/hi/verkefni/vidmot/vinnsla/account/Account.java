package hi.verkefni.vidmot.vinnsla.account;

// IO imports
import java.io.File;
import java.io.IOException;

// Util imports
import java.util.Iterator;
import java.util.Map;

// Collections import
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

// fasterxml imports
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

// package imports
import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

public class Account {
    private final ObjectMapper map = new ObjectMapper();
    private final File file = new File("src/main/resources/data.json");

    private ObjectNode accounts;
    private JsonNode accountRoot;
    private JsonNode currentSignedAccount;

    private boolean AccountSignedIn = false;

    /**
     * Constructs a default account.
     * @throws IOException
     */
    public Account() throws IOException {
        accountRoot = map.readTree(file);
        accounts = (ObjectNode) accountRoot.get("Accounts");
    }

    /**
     * User specified constructor that allows us to specify which user is actually signed in.
     * Should only be used if the program is confident on who is supposed to be logged in.
     * @param user
     * @throws IOException
     */
    public Account(String user) throws IOException {
        this();

        for (JsonNode acc : accounts) {
            String storedUser = acc.get("AccountInfo").get("name").asText();
            if (storedUser.equals(user)) {
                currentSignedAccount = acc;
                AccountSignedIn = true;
                return;
            }
        }

        currentSignedAccount = null;
        AccountSignedIn = false;
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
            if (checker.equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if the account is currently signed in or not. Used for some scenarios where we wanna for example add an
     * account.
     * @return to see if the account is in or not
     */
    public String getSignedAccount() {
        if (currentSignedAccount == null || !AccountSignedIn) {
            return "No account found";
        }
        return currentSignedAccount.get("AccountInfo").get("name").asText();
    }

    /**
     * Gets the signed account JsonNode, helper method for trips
     * @return JsonNode of the signed account
     */
    private JsonNode getSignedAccountNode() {
        if(currentSignedAccount == null || !AccountSignedIn) {
            return null;
        }
        return currentSignedAccount;
    }

    /**
     * <p>
     * The password must meet the following security requirements: </p>
     * <ul>
     *   <li>At least 6 characters long</li>
     *   <li>Contains at least one special character</li>
     *   <li>Contains at least one number</li>
     * </ul>
     * @param password
     * @return true or false
     */
    private boolean passwordValidator(String password) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
        String numbers = "0123456789";
        boolean hasSpecial = false;
        boolean hasNumber = false;

        if(password.length() < 6) return false;
        for(int charCheck = 0; charCheck < password.length(); charCheck++) {
            if (specialChars.indexOf(password.charAt(charCheck)) != -1) {
                hasSpecial = true;
            }
            if (numbers.indexOf(password.charAt(charCheck)) != -1) {
                hasNumber = true;
            }
        }

        if(hasSpecial && hasNumber){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Authneticates a user information to be able to access the system
     * @param username of the account
     * @param password of the account
     * @return user being logged in (true or false)
     */
        public boolean signIn(String username, String password) {
            for (JsonNode acc : accounts) {
                JsonNode info = acc.get("AccountInfo");
                String storedUser = info.get("name").asText();
                String storedPass = info.get("password").asText();

                if (storedUser.equals(username) && storedPass.equals(password)) {
                    currentSignedAccount = acc;
                    AccountSignedIn = true;
                    System.out.println(AccountSignedIn);
                    return true;
                }
                System.out.println("could not find account");
            }
            AccountSignedIn = false;
            return false;
        }

    /**
     * Logs the current user out of the system
     */
    public void logOut() {
        System.out.println("currentSignedAccount = " + currentSignedAccount);
        System.out.println("isAccountSignedIn = " + AccountSignedIn);

        if (currentSignedAccount != null && AccountSignedIn) {
            currentSignedAccount = null;
            AccountSignedIn = false;
            return;
        } else {
            System.out.println("System error: attempted to sign out but account cannot be found");
            return;
        }
    }

    /**
     * Adds a new account to the system.
     * @param account of the account to add
     * @param password of the account to add
     * @throws IOException
     */
    public boolean newAccount(String account, String password) throws IOException {
        // prevent dupe accounts
        if(accountExists(account)) {
            System.out.println("Account already exists");
            return false;
        }

        boolean validPassoword = passwordValidator(password);

            if (validPassoword) {
                System.out.println("Valid password created");

                ObjectNode accountInfo = map.createObjectNode();
                accountInfo.put("name", account);
                accountInfo.put("password", password);

                ObjectNode newAccountInfo = map.createObjectNode();
                newAccountInfo.put("AccountInfo", accountInfo);
                newAccountInfo.put("Trips", map.createArrayNode());

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

                AccountSignedIn = false;

                return;
            }
        }
    }

    /**
     * Gets the user current trip lists
     * @return the trips of the account
     */
    public ObservableList<Trip> getAccountTrips() {
        ObservableList<Trip> userTrips = FXCollections.observableArrayList();
        TimeManager tm = new TimeManager();

        JsonNode currentAccount = getSignedAccountNode();
        if (currentAccount == null) return userTrips;

        JsonNode tripNodes = currentAccount.get("Trips");
        if (tripNodes == null) return userTrips;

        for(JsonNode accTrips : tripNodes) {
            Trip trip = new Trip();

            System.out.println("getting trip");

            trip.setTitle(accTrips.get("title").asText());
            trip.setDestination(accTrips.get("destination").asText());
            trip.setStartDate(tm.parseDate(accTrips.get("startDate").asText()));
            trip.setEndDate(tm.parseDate(accTrips.get("endDate").asText()));

            JsonNode optionals = accTrips.get("optionals");
            if(optionals != null) {
                // boolean setters
                trip.setHotel(optionals.get("hotel").asBoolean());
                trip.setCar(optionals.get("car").asBoolean());
                trip.setFlight(optionals.get("flights").asBoolean());
                trip.setWork(optionals.get("work").asBoolean());

                // text setters
                trip.setSize(optionals.get("groupSize").asText());
                trip.setCost(optionals.get("cost").asText());

                // media setter (to do later)
            }

            System.out.println("trip added");
            userTrips.add(trip);
        }
        if(userTrips.isEmpty()) {
            System.out.println("No user trips found");
            return userTrips;
        }
        return userTrips;
    }
}