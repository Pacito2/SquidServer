package Manager;

import Party.PartyHandler;
import Party.User;

import java.io.IOException;

public class ClientHandler extends Thread
{
    final User client;

    public ClientHandler(User client)
    {
        this.client = client;
        start();
    }

    @Override
    public void run()
    {
        try {
            client.getWriter().writeUTF("Welcome " + client.getName() + " to this Squid server !");

            while(client.isConnected())
            {
                String clientMessage = client.getReader().readUTF();

                // Handling client command
                if(clientMessage.charAt(0) == '/'){
                    String[] args = clientMessage.substring(1).split("\\s+");
                    System.out.println(client.getName() + " : " + clientMessage);
                    switch (args[0]){
                        case "commands" :
                            client.getWriter().writeUTF("Command list : quit, leave, party");
                            break;
                        case "quit" :
                            PartyHandler.sendMessageToParty(client.getParty(), client.getName() + " left the chat");
                            client.createParty();
                            System.out.println(client.getName() + " disconnected : " + client.getSocket());
                            client.getSocket().close();
                            return;
                        case "party" :
                            if(args.length > 1){
                                switch (args[1]){
                                    case "invite" :
                                        if(args.length == 3) {
                                            // Try to invite a client
                                            String responseMessage = PartyHandler.tryInviteParty(client, args[2]);
                                            client.getWriter().writeUTF(responseMessage);
                                            System.out.println("[" + client.getName() + " " + args[0] + "] : " + responseMessage);
                                        }
                                        else
                                            client.getWriter().writeUTF("Invalid syntax : /" + args[0] + " " + args[1] + " <username>");
                                        break;
                                    case "join" :
                                        if(args.length == 3) {
                                            // Try to join a client
                                            String responseMessage = PartyHandler.tryJoinUser(client, args[2]);
                                            client.getWriter().writeUTF(responseMessage);
                                            System.out.println("[" + client.getName() + " " + args[0] + " " + args[1] + "] : " + responseMessage);
                                        }
                                        else
                                            client.getWriter().writeUTF("Invalid syntax : /join <username>");
                                        break;
                                    case "list":
                                        String users = "";
                                        int number = 0;
                                        for(User user : client.getParty().getUserList()){
                                            users += user.getName() + ", ";
                                            number++;
                                        }
                                        users = users.substring(0, users.length() - 2);
                                        System.out.println("[" + client.getName() + " " + args[0] + args[1] + "] : " + users);
                                        client.getWriter().writeUTF(number + " party member(s) : " + users);
                                        break;
                                }
                                break;
                            }
                            else
                                client.getWriter().writeUTF("Invalid syntax : /party <invite/join/list>");
                            break;
                        case "leave" :
                            if(client.getParty().getUserList().size() > 1){
                                PartyHandler.sendMessageToParty(client.getParty(), client.getName() + " left the chat!");
                                client.createParty();
                                System.out.println(client.getName() + " left his chat");
                            }
                            else
                                client.getWriter().writeUTF("You're already alone!");
                            break;
                    }
                }
                // Handling chat message
                else{
                    String chatMessage = client.getName() + " : " + clientMessage;
                    System.out.println(chatMessage);

                    PartyHandler.sendMessageToParty(client.getParty(), chatMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 