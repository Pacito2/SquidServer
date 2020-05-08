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
                        case "leave" :
                            PartyHandler.sendMessageToParty(client.getParty(), client.getName() + " left the chat");
                            client.createParty();
                            System.out.println(client.getName() + " leave : " + client.getSocket());
                            client.getSocket().close();
                            return;
                        case "invite" :
                            if(args.length == 2) {
                                // Try to invite a client
                                String responseMessage = PartyHandler.tryInviteParty(client, args[1]);
                                client.getWriter().writeUTF(responseMessage);
                                System.out.println("[" + client.getName() + " " + args[0] + "] : " + responseMessage);
                            }
                            else
                                client.getWriter().writeUTF("Invalid syntax : /" + args[1] + " <username>");
                            break;
                        case "join" :
                            if(args.length == 2) {
                                // Try to join a client
                                String responseMessage = PartyHandler.tryJoinUser(client, args[1]);
                                client.getWriter().writeUTF(responseMessage);
                                System.out.println("[" + client.getName() + " " + args[0] + "] : " + responseMessage);
                            }
                            else
                                client.getWriter().writeUTF("Invalid syntax : /accept <username>");
                            break;
                        case "party":
                            String users = "";
                            int number = 0;
                            for(User user : client.getParty().getUserList()){
                                users += user.getName() + ", ";
                                number++;
                            }
                            users = users.substring(0, users.length() - 2);
                            System.out.println("[" + client.getName() + " " + args[0] + "] : " + users);
                            client.getWriter().writeUTF(number + " party member(s) : " + users);
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