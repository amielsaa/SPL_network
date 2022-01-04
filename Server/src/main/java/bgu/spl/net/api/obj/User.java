package bgu.spl.net.api.obj;

import bgu.spl.net.api.msg.Message;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String pw;
    private String birthday;
    private boolean connected;
    private List<String> followers;
    private List<Message> pendingNotifications;
    private int clientOwnerId = -1;

    public User(String username, String pw, String birthday) {
        this.username = username;
        this.pw = pw;
        this.birthday = birthday;
        this.followers = new ArrayList<>();
        this.connected=false;
        this.pendingNotifications = new ArrayList<>();
    }

    public void addPendingNotification(Message notification) {
        pendingNotifications.add(notification);
    }

    public int getClientOwnerId() {
        return clientOwnerId;
    }

    public boolean follow(int op, String username) {
        if(op==1) { //unfollow
            if(followers.contains(username)){
                followers.remove(username);
                return true;
            }
        } else{ // follow
            if(!followers.contains(username)){
                followers.add(username);
                return true;
            }
        }
        return false;
    }

    public void setConnected(boolean connect,int clientOwnerId) {
        if(connect==false)
            this.clientOwnerId=-1;
        else
            this.clientOwnerId = clientOwnerId;
        this.connected=connect;
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getPw() {
        return pw;
    }

    public String getBirthday() {
        return birthday;
    }

    public List<String> getFollowers() {
        return followers;
    }
}
