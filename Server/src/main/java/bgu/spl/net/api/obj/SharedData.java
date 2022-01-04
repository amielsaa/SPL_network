package bgu.spl.net.api.obj;

import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.msg.NotificationMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedData {

    private ConcurrentHashMap<String,User> usersMap;

    //a hashmap to map each clientownerid to a username
    private ConcurrentHashMap<Integer,String> clientIdUsernameMap;

    private ConcurrentHashMap<String,List<Post>> usersPostsMap;

    private List<String> pendingNotificationUsers;


    private static class  SharedDataHolder {
        private static SharedData instance = new SharedData() ;
    }
    private SharedData() {
        usersMap = new ConcurrentHashMap<>();
        clientIdUsernameMap = new ConcurrentHashMap<>();
        usersPostsMap = new ConcurrentHashMap<>();
        pendingNotificationUsers = new CopyOnWriteArrayList<>();
    }
    public static SharedData getInstance() {
        return SharedDataHolder.instance;
    }

    public boolean post(String post,String type,int clientOwner) {
        User user = getUser(clientIdUsernameMap.get(clientOwner));
        if(user != null && user.isConnected()){
            //creating a post obj and addign to the map
            Post newPost = new Post(user.getUsername(),post,type);
            addToPostMap(newPost,user.getUsername());
            return true;
        }
        return false;
    }



    public List<Integer> getClientIdsToPost(List<String> usernames,String username,Message notification) {
        List<Integer> clientIds = new ArrayList<>();
        usernames.removeIf(s -> !usersMap.containsKey(s));
        usernames.addAll(getUser(username).getFollowers());
        for(String s : usernames) {
            User currUser = usersMap.get(s);
            if(currUser.isConnected())
                clientIds.add(currUser.getClientOwnerId());
            else {
                pendingNotificationUsers.add(s);
                currUser.addPendingNotification(notification);
            }
        }
        return clientIds;
    }

    //checking with login to send a notifaction

    public void addToPostMap(Post post, String username) {
        if(!usersPostsMap.containsKey(username))
            usersPostsMap.put(username,new ArrayList<>());
        usersPostsMap.get(username).add(post);
    }

    public boolean follow(int op,String username,int clientOwner) {
        User user = usersMap.get(clientIdUsernameMap.get(clientOwner));
        if(user!=null && user.isConnected()) {
            return user.follow(op,username);
        }
        return false;
    }

    public boolean logout(int clientOwnerId) {
        User user = getUser(clientIdUsernameMap.get(clientOwnerId));
        if(user!=null){
            user.setConnected(false,clientOwnerId);
            return true;
        }
        return false;
    }

    public boolean login(String username, String pw, String captcha,int clientOwnerId) {
        if(captcha.equals("1") && userExists(username) && getUser(username).getPw().equals(pw)){
            getUser(username).setConnected(true,clientOwnerId);
            if(!clientIdUsernameMap.containsKey(clientOwnerId))
                clientIdUsernameMap.put(clientOwnerId,username);
            return true;
        }
        return false;
    }


    public boolean register(String username,String pw, String birthday) {
        if(userExists(username)) {
            return false;
        }
        usersMap.put(username,new User(username,pw,birthday));
        return true;
    }

    public String getUsernameById(int clientOwner) {
        return clientIdUsernameMap.get(clientOwner);
    }

    private User getUser(String username) {
        return usersMap.get(username);
    }

    private boolean userExists(String username) {
        return usersMap.containsKey(username);
    }


}
