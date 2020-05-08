package Manager;

import party.Party;
import party.PartyHandler;
import party.User;

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
            client.getWriter().writeUTF("Welcome " + client.getName() + " to this Squirtle server !");

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
                            System.out.println("Client leave : " + client.getSocket());
                            client.getSocket().close();
                            return;
                        case "join" :
                            if(args.length == 2)
                            {
                                System.out.println("Client " + client.getName() + " want to chat with " + args[1]);

                                // Try to connect client to an other session
                                switch (PartyHandler.tryJoinParty(client, args[1]))
                                {
                                    case 0 :
                                        System.out.println("Can't found user \"" + args[1] + "\"");
                                        break;
                                    case 1 :
                                        System.out.println("You can't invite yourself!");
                                        break;
                                    case 2 :
                                        System.out.println("Connecting " + client.getName() + " to " + args[1] + "...");
                                        PartyHandler.sendMessageToParty(client.getParty(), client.getName() + " joined " + args[1]);
                                        break;
                                }
                            }
                            else
                                client.getWriter().writeUTF("Invalid syntax : /join <username>");
                            break;
                        case "party" :
                            String users = "";
                            int number = 0;
                            for(User user : client.getParty().getUserList()){
                                users += user.getName() + ", ";
                                number++;
                            }
                            users = users.substring(0, users.length() - 2);
                            System.out.println(client.getName() + " requested to show party list : " + users);
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