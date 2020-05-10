package manager;

import party.Party;
import party.PartyHandler;
import party.User;

import java.io.IOException;

public class ClientHandler extends Thread {
    /**
     * The communicating user
     */
    final User client;

    public ClientHandler(User client) {
        this.client = client;
        start();
    }

    @Override
    public void run() {
        try {
            String name = client.getName();
            Party party = client.getParty();
            client.getWriter().writeUTF("Welcome " + name + " to this Squid server !");

            while (client.isConnected()) {
                String clientMessage = client.getReader().readUTF();

                // Handling client command
                if (clientMessage.charAt(0) == '/') {
                    String[] args = clientMessage.substring(1).split("\\s+");
                    System.out.println(name + " : " + clientMessage);
                    switch (args[0]) {
                        case "commands":
                            client.getWriter().writeUTF("Command list : quit, leave, party");
                            break;
                        case "quit":
                            PartyHandler.sendMessageToParty(party, name + " left the chat!");
                            System.out.println(name + " disconnected : " + client.getSocket());
                            client.setConnected(false);
                            client.getSocket().close();
                            return;
                        case "party":
                            if (args.length > 1) {
                                switch (args[1]) {
                                    case "invite":
                                        if (args.length == 3) {
                                            // Try to invite a client
                                            String response = PartyHandler.checkUserCommand(client, args[2]);
                                            if (response.isEmpty())
                                                response = PartyHandler.tryInviteParty(client, args[2]);

                                            client.getWriter().writeUTF(response);
                                            System.out.println("[" + name + " " + args[0] + "] : " + response);
                                        } else
                                            client.getWriter().writeUTF("Invalid syntax : /" + args[0] + " " + args[1] + " <username>");
                                        break;
                                    case "join":
                                        if (args.length == 3) {
                                            // Try to join a client
                                            String response = PartyHandler.checkUserCommand(client, args[2]);
                                            if (response.isEmpty())
                                                response = PartyHandler.tryJoinUser(client, args[2]);

                                            client.getWriter().writeUTF(response);
                                            System.out.println("[" + name + " " + args[0] + "] : " + response);
                                        } else
                                            client.getWriter().writeUTF("Invalid syntax : /join <username>");
                                        break;
                                    case "kick":
                                        if (args.length == 3) {
                                            // Try to kick a client
                                            String response = PartyHandler.checkUserCommand(client, args[2]);
                                            if (response.isEmpty())
                                                response = PartyHandler.tryToKick(client, args[2]);

                                            client.getWriter().writeUTF(response);
                                            System.out.println("[" + name + " " + args[0] + "] : " + response);
                                        }
                                        break;
                                    case "permissions":
                                        if (args.length == 3) {
                                            String response = PartyHandler.checkUserCommand(client, args[2]);
                                            if (response.isEmpty())
                                                response = PartyHandler.trySetPartyPermissions(client, args[2]);

                                            client.getWriter().writeUTF(response);
                                            System.out.println("[" + name + " " + args[0] + "] : " + response);
                                        }
                                        break;
                                    case "list":
                                        String users = "";
                                        int number = 0;
                                        for (User user : party.getUserList().keySet()) {
                                            users += user.getName() + ", ";
                                            number++;
                                        }
                                        users = users.substring(0, users.length() - 2);
                                        System.out.println("[" + name + " " + args[0] + args[1] + "] : " + users);
                                        client.getWriter().writeUTF(number + " party member(s) : " + users);
                                        break;
                                    default:
                                        client.getWriter().writeUTF("Invalid syntax : /party <invite/join/list>");
                                        break;
                                }
                                break;
                            } else
                                client.getWriter().writeUTF("Invalid syntax : /party <invite/join/list>");
                            break;
                        case "leave":
                            if (party.getUserList().size() > 1) {
                                party.readjustHierarchy(client);
                                PartyHandler.sendMessageToParty(party, name + " left the chat!");
                                client.createParty();
                                System.out.println("[" + name + " " + args[0] + "] : left his chat!");
                            } else
                                client.getWriter().writeUTF("You're already alone!");
                            break;
                    }
                }
                // Handling chat message
                else {
                    String chatMessage = name + " : " + clientMessage;
                    System.out.println(chatMessage);

                    PartyHandler.sendMessageToParty(party, chatMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 