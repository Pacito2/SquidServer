package party;

import java.util.HashMap;

/**
 * Chat session
 *
 * @author Pacito2
 */
public class Party {
    /**
     * List of users present in the chat
     */
    private final HashMap<User, User> userList;
    /**
     * List of users invited to the chat
     */
    private final HashMap<User, User> invitedUsers;

    public Party() {
        this.userList = new HashMap<>();
        this.invitedUsers = new HashMap<>();
    }

    /**
     * Recreate the chat hierarchy by ignoring a user
     *
     * @param userToIgnore the user to ignore
     */
    public void readjustHierarchy(User userToIgnore) {
        if (this.userList.size() > 1 && this.userList.containsValue(userToIgnore)) {
            for (User user : this.userList.keySet()) {
                // If the host of the user is userToIgnore
                if (this.userList.get(user) == userToIgnore)
                    this.userList.put(user, this.userList.get(userToIgnore));
            }
        }
    }

    /**
     * Set the maximal permission to a user
     *
     * @param newHost the user to promote
     */
    public void setPartyHostPermissions(User newHost) {
        if (this.userList.containsKey(newHost) && this.userList.get(newHost) != null) {
            readjustHierarchy(newHost);
            this.userList.put(newHost, null);
        }
    }

    /**
     * Add a user to the chat
     *
     * @param invitedUser the user to add
     * @param hostUser    the parent user
     */
    public void addUser(User invitedUser, User hostUser) {
        this.userList.put(invitedUser, hostUser);
    }

    /**
     * Remove a user to the chat
     *
     * @param user the user to remove
     */
    public void removeUser(User user) {
        this.userList.remove(user);
    }

    /**
     * Get the chat users list
     *
     * @return the chat users list
     */
    public HashMap<User, User> getUserList() {
        return userList;
    }

    /**
     * Add a user to the chat invite list
     *
     * @param invitedUser the user to add
     * @param hostUser    the parent user
     */
    public void addInvitedUser(User invitedUser, User hostUser) {
        this.invitedUsers.put(invitedUser, hostUser);
    }

    /**
     * Remove a user to the chat invite list
     *
     * @param user the user to remove
     */
    public void removeInvitedUser(User user) {
        this.invitedUsers.remove(user);
    }

    /**
     * Get the chat invited users list
     *
     * @return the chat invited users list
     */
    public HashMap<User, User> getInvitedUsers() {
        return invitedUsers;
    }
}
