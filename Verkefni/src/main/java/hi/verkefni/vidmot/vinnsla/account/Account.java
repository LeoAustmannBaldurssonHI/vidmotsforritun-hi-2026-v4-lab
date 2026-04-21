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

// Date import
import java.time.LocalDate;

// Alert import
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Account {
    private final ObjectMapper map = new ObjectMapper();
    private final File file = new File("src/main/resources/data.json");

    private ArrayNode accounts;
    private JsonNode accountRoot;

    private static JsonNode currentSignedAccount;
    private static boolean AccountSignedIn = false;
    private static Account currentAccount;
    private TimeManager tm = new TimeManager();

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
     *     <li>Must contain an uppercase & lowercase letter</li>
     *   <li>At least 6 characters long</li>
     *   <li>Contains at least one special character</li>
     *   <li>Contains at least one number</li>
     * </ul>
     * @param password to validate
     * @return password validation result
     */
    private boolean passwordValidator(String password) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:'\",.<>/?";
        String numbers = "0123456789";

        // Password requirements, assume that they're all not in the String itself
        boolean hasSpecial = false;
        boolean hasNumber = false;
        boolean hasLower = false;
        boolean hasUpper = false;

        if(password.length() < 6) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Password error");
            alert.setHeaderText("Length error");
            alert.setContentText("Your password is too short to be valid!");

            alert.showAndWait();
            return false;
        }

        // Is the user password apart of the common password pattern? Won't put every single one, only the most common one
        String lowerPass = password.toLowerCase();
        System.out.println(lowerPass);
        if(lowerPass.matches(".*password.*") || lowerPass.matches(".*qwerty.*") || lowerPass.matches(".*admin.*") || lowerPass.matches(".*1234.*") || lowerPass.matches(".*abcdef.*")) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setWidth(400);

            alert.setTitle("Password error");
            alert.setHeaderText("Common password");
            alert.setContentText("The password you gave is apart of a common pattern.\nThis password is considered " +
                            "weak, pick a new password to use for your account.\n" +
                    "Common passwords examples: password, admin, 1234, qwerty"
            );

            alert.getDialogPane().setStyle("-fx-font-weight: bold;");

            alert.showAndWait();
            return false;
        }

        boolean validPassword = false;
        for(int charCheck = 0; charCheck < password.length(); charCheck++) {
            if (specialChars.indexOf(password.charAt(charCheck)) != -1) {
                hasSpecial = true;
            }
            if (numbers.indexOf(password.charAt(charCheck)) != -1) {
                hasNumber = true;
            }
            if(password.matches(".*[a-z].*")) {
                hasLower = true;
            }
            if(password.matches(".*[A-Z].*")) {
                hasUpper = true;
            }
            validPassword = hasUpper && hasLower && hasNumber && hasSpecial;
        }

        if(validPassword) {
            return true;
        } else if(!hasSpecial && !hasSpecial) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Account creation error");
            alert.setHeaderText("Error: Missing special characters in the password");
            alert.setContentText("Your password is missing special characters in it!");

            alert.showAndWait();
            return false;
        } else if(!hasNumber && !hasNumber){
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Account creation error");
            alert.setHeaderText("Error: Missing number in the password");
            alert.setContentText("Your password is missing numbers in it!");

            alert.showAndWait();
            return false;
        } else if(!hasLower){
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Account creation error");
            alert.setHeaderText("Error: Missing a letter in the password");
            alert.setContentText("Your password is missing a lowercase letter in it!");

            alert.showAndWait();
            return false;
        } else if(!hasUpper && (!hasLower || !hasUpper)) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Account creation error");
            alert.setHeaderText("Error: Missing a letter in the password");
            alert.setContentText("Your password is missing a uppercase letter in it!");

            alert.showAndWait();
            return false;
        }
        return false;
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
                    System.out.println("Account found: " + AccountSignedIn);
                    return true;
                }
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
            System.exit(1); // assuming there is a major system failure, we will kill the program and make the user restart
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
        if(account.isBlank() || account.matches("admin")) {
            Alert alert = new Alert(AlertType.ERROR);

            alert.setTitle("Account creation error");
            alert.setHeaderText("Invalid username given");
            alert.setContentText("The username you put for your account is invalid.\nThe username:" + account + " is not allowed");

            alert.showAndWait();
        } else if (passwordValidator(password)) {
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
        return false;
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
        ObservableList<Trip> tripsToRemove = FXCollections.observableArrayList(); // this list will be used to throw all trips that are past the end date
        TimeManager tm = new TimeManager();

        JsonNode currentAccount = getSignedAccountNode();
        if (currentAccount == null) return userTrips;

        JsonNode tripNodes = currentAccount.get("Trips");
        if (tripNodes == null) return userTrips;

        for(JsonNode accTrips : tripNodes) {
            Trip trip = new Trip();

            System.out.println("getting trip");

            LocalDate startDate = tm.parseDate(accTrips.get("startDate").asText());
            LocalDate endDate = tm.parseDate(accTrips.get("endDate").asText());

            trip.setTitle(accTrips.get("title").asText());
            trip.setDestination(accTrips.get("destination").asText());
            trip.setStartDate(startDate);
            trip.setEndDate(endDate);

            // Is the current date ahead of the end date of the selected trip?
            if(endDate.isBefore(tm.getCurrentDate())) {
                System.out.println("Trip past the expiry date, deleting");

                // If so, we'll need to remove it. Adding to a list to hold onto it for now.
                tripsToRemove.add(trip);
                continue;
            }

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

        // Deletion
        for (Trip expiredTrip : tripsToRemove) {
            try {
                removeTripFromAccount(expiredTrip);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return userTrips;
    }

    /**
     * Adds an trip to the user's json file
     * @param trip
     * @throws IOException
     */
    public void addTripToAccount(Trip trip) throws IOException {
        JsonNode signedAccount = getSignedAccountNode();
        if (signedAccount == null) throw new IOException("Signed account not found");

        JsonNode tripsNode = signedAccount.get("Trips");
        if (!(tripsNode instanceof ArrayNode tripsArray)) {
            throw new IllegalStateException("Trips node is not an array.");
        }

        ObjectNode tripNode = map.createObjectNode();
        tripNode.put("title", trip.getTitle());
        tripNode.put("destination", trip.getDestination());
        tripNode.put("startDate", tm.formatDate(trip.getStartDate()));
        tripNode.put("endDate", tm.formatDate(trip.getEndDate()));

        ObjectNode optionals = map.createObjectNode();
        optionals.put("hotel", trip.getHotel());
        optionals.put("car", trip.getCar());
        optionals.put("flights", trip.getFlight());
        optionals.put("work", trip.getWork());
        optionals.put("groupSize", trip.getGroupSize() == null ? "" : trip.getGroupSize());

        ObjectNode cost = map.createObjectNode();
        cost.put("hotel cost", trip.getHotelCost() == null ? "0kr" : trip.getHotelCost());
        cost.put("flight cost", trip.getFlightCost() == null ? "0kr" : trip.getFlightCost());
        cost.put("car cost", trip.getCarCost() == null ? "0kr" : trip.getCarCost());

        optionals.set("cost", cost);
        tripNode.set("optionals", optionals);

        tripsArray.add(tripNode);

        map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
        System.out.println("trip added to the database");
    }

    /**
     * Removes a trip from the user's json file
     * @param trip
     * @throws IOException
     */
    public void removeTripFromAccount(Trip trip) throws IOException {
        JsonNode signedAccount = getSignedAccountNode();
        if (signedAccount == null) {
            throw new IllegalStateException("No signed-in account found.");
        }

        JsonNode tripsNode = signedAccount.get("Trips");
        if (!(tripsNode instanceof ArrayNode tripsArray)) {
            throw new IllegalStateException("Trips node is not an array.");
        }

        String tripStartDate = tm.formatDate(trip.getStartDate());
        String tripEndDate = tm.formatDate(trip.getEndDate());

        for (int i = 0; i < tripsArray.size(); i++) {
            JsonNode tripNode = tripsArray.get(i);

            String storedTitle = tripNode.get("title").asText();
            String storedDestination = tripNode.get("destination").asText();
            String storedStartDate = tripNode.get("startDate").asText();
            String storedEndDate = tripNode.get("endDate").asText();

            if (storedTitle.equals(trip.getTitle())
                    && storedDestination.equals(trip.getDestination())
                    && storedStartDate.equals(tripStartDate)
                    && storedEndDate.equals(tripEndDate)) {

                tripsArray.remove(i);
                map.writerWithDefaultPrettyPrinter().writeValue(file, accountRoot);
                System.out.println("trip removed from the database");
                return;
            }
        }
    }
}