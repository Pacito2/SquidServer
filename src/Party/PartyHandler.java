package Party;

import Main.Server;

public class PartyHandler {
    public static String tryInviteParty(User client, String targetName){
        // Return unknow user
        String response = "Can't found user \"" + targetName + "\"";
        if(!client.getName().equals(targetName)){
            User target = new User(targetName);
            if(Server.userList.contains(target)){
                target = Server.userList.get(Server.userList.indexOf(target));
                Party party = client.getParty();
                if(!party.getInvitedUsers().contains(target)){
                    if(!party.getUserList().contains(target))
                    {
                        try{
                            // Adding client to the invite list
                            party.addInvitedUser(target);

                            target.getWriter().writeUTF(client.getName() + " invited you to his chat!");

                            // Return invite success
                            response = client.getName() + " invited " + targetName + "!";
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else
                        // Return client already in the party
                        response = targetName + " is already in the chat!";
                }
                else
                    // Return client already in invite list
                    response = targetName + " is already on the invitation list!";
            }
        }
        else
            // Return invite myself
            response = "You can't invite yourself!";
        return response;
    }

    public static String tryJoinUser(User client, String targetName){
        // Return unknow user
        String response = "Can't found user \"" + targetName + "\"";
        if(!client.getName().equals(targetName)){
            User target = new User(targetName);
            if(Server.userList.contains(target)){
                target = Server.userList.get(Server.userList.indexOf(target));
                Party newParty = target.getParty();

                if(newParty.getInvitedUsers().contains(client)){
                    // Return join success
                    response = client.getName() + " joined the chat!";
                    sendMessageToParty(newParty, response);

                    // Setting user party
                    newParty.addUser(client);
                    client.getParty().removeUser(client);
                    client.setParty(newParty);
                    newParty.removeInvitedUser(client);
                }
                else
                    // Return insufficient permission
                    response = "You were not invited by " + targetName + "!";
            }
        }
        else
            // Return join myself
            response = "You can't join yourself!";
        return response;
    }

    public static void sendMessageToParty(Party party, String message){
        try{
            for(User user : party.getUserList())
                user.getWriter().writeUTF(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
