package Party;

import Manager.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Objects;

public class User {
    final String name;

    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;

    private boolean connected;
    private Thread session;
    private User requestedUser;
    private Party party;

    public User(String name){
        this.name = name;
    }

    public User(String name, Socket socket, DataInputStream reader, DataOutputStream writer){
        this.name = name;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.connected = true;
        this.session = new ClientHandler(this);
        this.requestedUser = new User(null);
        this.createParty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, socket, reader, writer, connected, session);
    }

    public void createParty(){
        if(this.party != null)
            this.party.removeUser(this);
        this.party = new Party();
        this.party.addUser(this);
    }

    public String getName() {
        return name;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getReader() {
        return reader;
    }

    public DataOutputStream getWriter() {
        return writer;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setSession(Thread session) {
        this.session = session;
    }

    public Thread getSession() {
        return session;
    }

    public void setRequestedUser(User requestedUser) {
        this.requestedUser = requestedUser;
    }

    public User getRequestedUser() {
        return requestedUser;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Party getParty() {
        return party;
    }
}
