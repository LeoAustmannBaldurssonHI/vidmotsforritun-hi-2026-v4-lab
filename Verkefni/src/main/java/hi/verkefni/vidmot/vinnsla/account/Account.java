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
import com.fasterxml.jackson.databind.node.*;

// package imports
import hi.verkefni.vidmot.vinnsla.Trips.Trip;
import hi.verkefni.vidmot.vinnsla.TimeManagement.TimeManager;

public class Account {
    private final ObjectMapper map = new ObjectMapper();
    private final File file = new File("src/main/resources/data.json");

    private ArrayNode accounts;
    private JsonNode accountRoot;

    private static JsonNode currentSignedAccount;
    private static boolean AccountSignedIn = false;
    private static Account currentAccount;

    /**
     * Constructs a default account.
     * @throws IOException
     */
    public Account() throws IOException {
        accountRoot = map.readTree(file);
        accounts = (ArrayNode) accountRoot.get("Accounts");
    }

    /**
     * This method is used for the program to check if the current account name given is in the system or not.
     * @param name of the username to check
     * @return true or false of the account
     */
    public boolean accountExists(String name) {
        for (JsonNode acc : accounts) {
            String checker = acc.get("AccountInfo").get("name").asText();
            if (checker.equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if the account is currently signed in or not. Used for some scenarios where we wanna for example add an
     * account.
     * @return to see if the account is in or not
     */
    public static String getSignedAccountName() {
        if (currentSignedAccount == null || !AccountSignedIn) {
            return "No account found";
        }
        return currentSignedAccount.get("AccountInfo").get("name").asText();
    }

    /**
     * Gets the signed account JsonNode, helper method for trips
     * @return JsonNode of the signed account
     */
    public static JsonNode getSignedAccountNode() {
        if(currentSignedAccount == null || !AccountSignedIn) {
            return null;
        }
        return currentSignedAccount;
    }

    /**
     * Check's if the user had an authenticated session before going to a different window.
     * @return true or false
     */
    public static boolean activeSession() {
        if(currentSignedAccount == null || !AccountSignedIn) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the current user that is signed in for the session
     * @return current signed account
     */
    public static Account getCurrentAccount() {
        return currentAccount;
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
                    currentAccount =  this;
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
        if (currentSignedAccount != null && AccountSignedIn && currentAccount != null) {
            currentSignedAccount = null;
            AccountSignedIn = false;
            currentAccount = null;
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
        if (passwordValidator(password)) {
            System.out.println("Valid password created");

            ObjectNode accountInfo = map.createObjectNode();
            accountInfo.put("name", account);
            accountInfo.put("password", password);

            ObjectNode newAccountInfo = map.createObjectNode();
            newAccountInfo.put("AccountInfo", accountInfo);
            newAccountInfo.put("Trips", map.createArrayNode());

            accounts.add(newAccountInfo);

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
        for(int i = 0; i < accounts.size(); i++) {
            JsonNode accNode = accounts.get(i);
            JsonNode accInfo = accNode.get("AccountInfo");

            String user = accInfo.get("name").asText();
            if(user.equals(account)) {
                logOut();
                accounts.remove(i);
                map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
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

                JsonNode costNode = optionals.get("cost");
                if (costNode != null) {
                    trip.setFlightCost(costNode.get("flight cost").asText());
                    trip.setHotelCost(costNode.get("hotel cost").asText());
                    trip.setCarCost(costNode.get("car cost").asText());
                }
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