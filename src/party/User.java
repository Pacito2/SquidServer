package party;

import manager.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Objects;

/**
 * Client information
 *
 * @author Pacito2
 */
public class User {
    /**
     * The user detection address
     */
    final String name;

    /**
     * The connection socket
     */
    private Socket socket;
    /**
     * Data reader
     */
    private DataInputStream reader;
    /**
     * Data writer
     */
    private DataOutputStream writer;

    /**
     * Connection state
     */
    private boolean connected;
    /**
     * Action manager
     */
    private Thread handler;
    /**
     * Chat group instance
     */
    private Party party;

    public User(String name) {
        this.name = name;
    }

    public User(String name, Socket socket, DataInputStream reader, DataOutputStream writer) {
        this.name = name;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.connected = true;
        this.handler = new ClientHandler(this);
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
        return Objects.hash(name, socket, reader, writer, connected, handler);
    }

    /**
     * Create new chat and set user permissions
     */
    public void createParty() {
        if (this.party != null) {
            this.party.readjustHierarchy(this);
            this.party.getUserList().remove(this);
        }
        this.party = new Party();
        this.party.addUser(this, null);
    }

    /**
     * Get the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the socket
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Get the data reader
     *
     * @return the data reader
     */
    public DataInputStream getReader() {
        return reader;
    }

    /**
     * Get the data writer
     *
     * @return the data writer
     */
    public DataOutputStream getWriter() {
        return writer;
    }

    /**
     * Get the connection state
     *
     * @return the connection state
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Set the connection state
     *
     * @param connected connection state
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * Get the chat group instance
     *
     * @return the chat group instance
     */
    public Party getParty() {
        return party;
    }

    /**
     * Set the chat group instance
     *
     * @param party the new chat group instance
     */
    public void setParty(Party party) {
        this.party = party;
    }
}
