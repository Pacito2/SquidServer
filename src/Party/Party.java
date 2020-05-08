package Party;

import java.util.ArrayList;

public class Party {
    private ArrayList<User> userList;
    private ArrayList<User> invitedUsers;

    public Party(){
        this.userList = new ArrayList<>();
        this.invitedUsers = new ArrayList<>();
    }

    public void addUser(User user){
        this.userList.add(user);
    }

    public void removeUser(User user){
        this.userList.remove(user);
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void addInvitedUser(User user){
        this.invitedUsers.add(user);
    }

    public void removeInvitedUser(User user){
        this.invitedUsers.remove(user);
    }

    public ArrayList<User> getInvitedUsers() {
        return invitedUsers;
    }
}
