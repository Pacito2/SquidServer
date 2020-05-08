package Party;

import Main.Server;

public class PartyHandler {
    public static String tryJoinParty(User client, String targetName){
        // Return unknow user
        String response = "Can't found user \"" + targetName + "\"";
        if(!client.getName().equals(targetName)){
            User target = new User(targetName);
            if(Server.userList.contains(target)){
                target = Server.userList.get(Server.userList.indexOf(target));

                try{
                    // Setting new join request
                    if(client.getRequestedUser().getName() != null){
                        client.getRequestedUser().getWriter().writeUTF(client.getName() + " abandoned his invitation request!");
                        client.setRequestedUser(new User(null));
                    }
                    client.setRequestedUser(target);

                    target.getWriter().writeUTF(client.getName() + " want to join this chat. /accept " + client.getName());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                // Return party join success
                response = "Waiting for " + targetName + " chat...";
            }
        }
        else
            // Return invite myself
            response = "You can't invite yourself!";
        return response;
    }

    public static String tryAcceptUser(User client, String targetName){
        // Return unknow user
        String response = "Can't found user \"" + targetName + "\"";
        if(!client.getName().equals(targetName)){
            User target = new User(targetName);
            if(Server.userList.contains(target)){
                target = Server.userList.get(Server.userList.indexOf(target));

                if(target.getRequestedUser().equals(client)){
                    target.setRequestedUser(new User(null));

                    // Setting user party
                    client.getParty().addUser(target);
                    target.getParty().removeUser(target);
                    target.setParty(client.getParty());

                    // Return party accept success
                    response = targetName + " joined " + client.getName() + "!";
                }
                else
                    // Return invalid
                    response = targetName + " doesn't want to join you!";
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
