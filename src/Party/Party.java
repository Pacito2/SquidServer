package Party;

import java.util.ArrayList;

public class Party {
    private ArrayList<User> userList;

    public Party(){
        this.userList = new ArrayList<>();
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
}
