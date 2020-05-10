package party;

import main.Server;

/**
 * Chat management
 *
 * @author Pacito2
 */
public class PartyHandler {

    /**
     * Check if the target exist and if it's the same person as the sender
     *
     * @param sender     the command sender
     * @param targetName the name of the target
     * @return the reply message
     */
    public static String checkUserCommand(User sender, String targetName) {
        String response = "";
        User target = new User(targetName);
        if (Server.userList.contains(target)) {
            if (!sender.getName().equals(targetName))
                // Return check success
                response = "";
            else
                // Return can't apply this on user
                response = "You can't apply this on you!";
        } else
            // Return unknow user
            response = "Can't found user \"" + targetName + "\"!";

        return response;
    }

    /**
     * Try to invite a user to the chat
     *
     * @param sender     the user who wants to invite
     * @param targetName the name of the user to invite
     * @return the reply message
     */
    public static String tryInviteParty(User sender, String targetName) {
        String response = "";
        User target = Server.userList.get(Server.userList.indexOf(new User(targetName)));
        Party party = sender.getParty();
        if (!party.getInvitedUsers().containsKey(target)) {
            if (!party.getUserList().containsKey(target)) {
                try {
                    // Adding sender to the invite list
                    party.addInvitedUser(target, sender);

                    target.getWriter().writeUTF(sender.getName() + " invited you to his chat!");

                    // Return invite success
                    response = sender.getName() + " invited " + targetName + "!";
                    sendMessageToPartyExceptOne(party, response, sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                // Return sender already in the party
                response = targetName + " is already in the chat!";
        } else
            // Return sender already in invite list
            response = targetName + " is already on the invitation list!";

        return response;
    }

    /**
     * Try to join a chat
     *
     * @param sender     the user who wants to join
     * @param targetName the name of the user to join
     * @return the reply message
     */
    public static String tryJoinUser(User sender, String targetName) {
        String response = "";
        User target = Server.userList.get(Server.userList.indexOf(new User(targetName)));
        Party newParty = target.getParty();

        if (newParty.getInvitedUsers().containsKey(sender)) {
            // Return join success
            response = sender.getName() + " joined the chat!";
            sendMessageToParty(newParty, response);

            // Setting user party
            newParty.addUser(sender, newParty.getInvitedUsers().get(sender));
            sender.getParty().removeUser(sender);
            sender.setParty(newParty);
            newParty.removeInvitedUser(sender);
        } else
            // Return insufficient permission
            response = "You were not invited by " + targetName + "!";

        return response;
    }

    /**
     * Try to kick a user
     *
     * @param sender     the user who wants to kick
     * @param targetName the name of the user to kick
     * @return the reply message
     */
    public static String tryToKick(User sender, String targetName) {
        String response = "";
        User target = Server.userList.get(Server.userList.indexOf(new User(targetName)));
        Party party = sender.getParty();

        // Check if target is in the party
        if (party.getUserList().containsKey(target)) {

            User parentHost = party.getUserList().get(target);
            while (sender != parentHost && parentHost != null)
                parentHost = party.getUserList().get(parentHost);

            if (parentHost == null && party.getUserList().get(sender) != null)
                // Return insufficient permission
                response = "Insufficient permission!";
            else {
                try {
                    target.createParty();
                    target.getWriter().writeUTF("You have been kicked!");
                    // Return kick success
                    response = sender.getName() + " kicked " + targetName + "!";
                    sendMessageToPartyExceptOne(party, response, sender);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // Check if target is in the invitation list
        else if (party.getInvitedUsers().containsKey(target)) {
            // Check if target is in the invitation list
            if (party.getInvitedUsers().containsKey(target)) {

                User parentHost = party.getInvitedUsers().get(target);
                while (sender != parentHost && parentHost != null)
                    parentHost = party.getUserList().get(parentHost);

                if (parentHost == null)
                    // Return insufficient permission
                    response = "Insufficient permission!";
                else {
                    party.getInvitedUsers().remove(target);
                    // Return kicked invited user
                    response = "Invited user " + targetName + " was kicked!";
                    sendMessageToPartyExceptOne(party, response, sender);
                }
            }
        } else
            // Return insufficient permission
            response = "Insufficient permission! 1";

        return response;
    }

    /**
     * Try to set the permissions of a user
     *
     * @param sender     the user who want to promote
     * @param targetName the name of the user to promote
     * @return the reply message
     */
    public static String trySetPartyPermissions(User sender, String targetName) {
        String response = "";

        User target = Server.userList.get(Server.userList.indexOf(new User(targetName)));
        Party party = sender.getParty();

        party.setPartyHostPermissions(target);

        // Return set permissions success
        response = sender.getName() + " gave chat permissions to " + targetName + "!";
        sendMessageToPartyExceptOne(party, response, sender);

        return response;
    }

    /**
     * Send a message to users of the chat
     *
     * @param party   the session
     * @param message the message to send
     */
    public static void sendMessageToParty(Party party, String message) {
        try {
            for (User user : party.getUserList().keySet())
                user.getWriter().writeUTF(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to users of the chat except one
     *
     * @param party       the session
     * @param message     the message to send
     * @param ignoredUser the user to ignore
     */
    public static void sendMessageToPartyExceptOne(Party party, String message, User ignoredUser) {
        try {
            for (User user : party.getUserList().keySet())
                if (user != ignoredUser)
                    user.getWriter().writeUTF(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
