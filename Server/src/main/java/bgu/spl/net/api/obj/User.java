package bgu.spl.net.api.obj;

import bgu.spl.net.api.msg.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
    private String username;
    private String pw;
    private String birthday;
    private boolean connected;
    private List<String> followers;
    private List<String> following;
    private List<Message> pendingNotifications;
    private int clientOwnerId = -1;
    private int postsNum = 0;
    private List<String> blockedUsers;


    public User(String username, String pw, String birthday) {
        this.username = username;
        this.pw = pw;
        this.birthday = birthday;
        this.following = new ArrayList<>();
        this.followers = new ArrayList<>();
        this.connected=false;
        this.pendingNotifications = new ArrayList<>();
        this.blockedUsers = new ArrayList<>();
    }



    public void updatePostsNum(int num) { this.postsNum = num; }

    public void addPendingNotification(Message notification) {
        pendingNotifications.add(notification);
    }

    public int getClientOwnerId() {
        return clientOwnerId;
    }

    public boolean checkIfUserBlocked(String user) { return blockedUsers.contains(user);}

    public boolean addBlocked(String user) {
        if(!blockedUsers.contains(user)){
            blockedUsers.add(user);
            following.remove(user);
            return true;
        }
        return false;
    }

    public boolean follow(int op, User user) {
        if(op==1) { //unfollow
            if(followers.contains(user.getUsername())){
                followers.remove(user.getUsername());
                user.getFollowing().remove(username);
                return true;
            }
        } else{ // follow
            if(!followers.contains(user.getUsername()) & !checkIfUserBlocked(user.getUsername())){
                followers.add(user.getUsername());
                user.getFollowing().add(username);
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

    public String getStats() {
        String stats = "";
        stats += getAge() + " ";
        stats += postsNum + " ";
        stats += followers.size() + " ";
        stats += following.size() + " ";
        return stats;
    }

    public int getAge() {
        String[] splitted = birthday.split("-");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = formatter.format(date);
        String[] splittedCurrentDate = currentDate.split("-");
        return Integer.parseInt(splittedCurrentDate[2]) - Integer.parseInt(splitted[2]);
    }

    public List<Message> getPendingNotifications() {
        return pendingNotifications;
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

    public List<String> getFollowing() {
        return following;
    }

    public List<String> getFollowers() {
        return followers;
    }
}
