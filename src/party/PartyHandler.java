package party;

import Main.Server;

public class PartyHandler {
    public static int tryJoinParty(User client, String targetName){
        // Return unknow user
        int response = 0;
        if(!client.getName().equals(targetName))
        {
            User target = new User(targetName);
            if(Server.userList.contains(target)) {
                // Return party join success
                response = 2;
                target = Server.userList.get(Server.userList.indexOf(target));

                // Setting user party
                target.getParty().addUser(client);
                client.setParty(target.getParty());
            }
        }
        else
            // Return invite myself
            response = 1;
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
