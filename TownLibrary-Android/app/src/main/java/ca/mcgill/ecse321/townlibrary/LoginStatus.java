package ca.mcgill.ecse321.townlibrary;

/**
 * Holds information required to login. It's a singleton!
 */
public final class LoginStatus {

    public static final LoginStatus INSTANCE = new LoginStatus();

    // if userId is null, then no one is logged in (see isLoggedIn())
    private Integer userId;
    private String username;
    private String password;
    private String displayName;
    private String emailAddress;
    private Boolean inTown;
    private String address;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Boolean getInTown() {
        return inTown;
    }

    public void setInTown(Boolean inTown) {
        this.inTown = inTown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private LoginStatus() {

    }

    /**
     * Checks to see if we have login info. The info could be invalid though!
     *
     * @return true if we have info, false otherwise
     */
    public boolean isLoggedIn() {
        return userId != null;
    }

    /**
     * Sets the status to login, assumes the supplied inputs are valid.
     *
     * @param userId user id
     * @param username username
     * @param password password
     */
    public void login(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
    }

    /**
     * Sets the status to logout
     */
    public void logout() {
        userId = null;
        username = null;
        password = null;

        // clear it for sanity...
        displayName = null;
    }

    /**
     * Sets the preferred display name
     *
     * @param name display name, null clears it
     */
    public void setPreferredDisplayName(String name) {
        displayName = name;
    }

    /**
     * If there is a preferred display name, return that. Otherwise, just use
     * the original username.
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName != null ? displayName : username;
    }

    /**
     * Getter for the user id
     *
     * @return user id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Getter for the username
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for the password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

}
