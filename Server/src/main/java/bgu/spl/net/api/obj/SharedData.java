package bgu.spl.net.api.obj;

import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.msg.NotificationMsg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedData {

    private final ConcurrentHashMap<String,User> usersMap;

    //a hashmap to map each clientownerid to a username
    private final ConcurrentHashMap<Integer,String> clientIdUsernameMap;

    private final ConcurrentHashMap<String,List<Post>> usersPostsMap;

    private final List<String> pendingNotificationUsers;
    private final List<String> filterWords = Arrays.asList("spl","shit","trump","war","ass","fuck","splassignment2");


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

    public String filter(String line) {
        String filteredString = line;
        String[] splittedLine = line.split(" ");
        for(int i =0;i<splittedLine.length;i++) {
            String currentWord = splittedLine[i].trim();
            if(filterWords.contains(currentWord))
                filteredString = filteredString.replaceAll(currentWord,"****");
        }
        return filteredString;
    }

    public boolean block(int clientOwner, String userToBlock) {
        User user = getUser(getUsernameById(clientOwner));
        if(user!=null && user.isConnected() && user.addBlocked(userToBlock)) {
            User blockedUser = getUser(userToBlock);
            user.follow(1,blockedUser);
            return true;
        }
        return false;
    }

    public boolean pm(String post,String recipient,String date,int clientOwner) {
        User user = getUser(clientIdUsernameMap.get(clientOwner));
        User recipentUser = getUser(recipient);
        if(user!=null && user.isConnected() && recipentUser != null && recipentUser.getFollowers().contains(user.getUsername()) && !recipentUser.checkIfUserBlocked(user.getUsername())) {
            Post newPm = new Post(user.getUsername(),recipient,post +" " +date,"PM");
            addToPostMap(newPm,user.getUsername());
            return true;
        }
        return false;
    }

    private int getPostsNum(String username) {
        int counter = 0;
        if(usersPostsMap.get(username) == null)
            return 0;
        List<Post> posts = usersPostsMap.get(username);
        for(int i=0; i<posts.size();i++) {
            if(posts.get(i).getType().equals("Public"))
                counter++;
        }
        return counter;
    }

    public List<String> getUsersStat(int clientOwner,String[] usernames) {
        List<String> userStats = null;
        User user = getUser(getUsernameById(clientOwner));
        if(user!=null && user.isConnected()) {
            userStats = new ArrayList<>();
            for(int i=0;i<usernames.length;i++) {
                User currentUser = usersMap.get(usernames[i]);
                if(currentUser!=null && !user.checkIfUserBlocked(currentUser.getUsername())) {
                    currentUser.updatePostsNum(getPostsNum(currentUser.getUsername()));
                    userStats.add(currentUser.getStats());
                } else if(currentUser==null) {
                    System.out.println("failed");
                    return null;
                }
            }
        }
        return userStats;
    }


    public List<String> getActiveUsersForLogstat(int clientOwner) {
        User user = getUser(getUsernameById(clientOwner));
        List<String> activeUsers = null;
        if(user!=null && user.isConnected()){
            activeUsers = new ArrayList<>();
            for(Map.Entry<String,User> entry : usersMap.entrySet()) {
                User currentUser = entry.getValue();
                if(currentUser.isConnected() && !( currentUser.getUsername().equals(user.getUsername()) ) && !user.checkIfUserBlocked(currentUser.getUsername()) ){
                    currentUser.updatePostsNum(getPostsNum(currentUser.getUsername()));
                    activeUsers.add(currentUser.getStats());
                }
            }
        }

        return activeUsers;
    }

    public int getClientIdToPm(String recipient,Message notification ) {
        User recipientUser = getUser(recipient);
        if(recipientUser.isConnected()) {
            return recipientUser.getClientOwnerId();
        } else{
            pendingNotificationUsers.add(recipient);
            recipientUser.addPendingNotification(notification);
            return -1;
        }
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
        usernames.removeIf(s -> !usersMap.containsKey(s) && !getUser(username).checkIfUserBlocked(s));
        usernames.addAll(getUser(username).getFollowers());
        for(String s : usernames) {
            User currUser = usersMap.get(s);
            if(currUser.isConnected())
                clientIds.add(currUser.getClientOwnerId());
            else {
                pendingNotificationUsers.add(s);
                currUser.addPendingNotification(notification);
                System.out.println(notification.getVars().toString());
            }
        }
        return clientIds;
    }



    private void addToPostMap(Post post, String username) {
        if(!usersPostsMap.containsKey(username))
            usersPostsMap.put(username,new ArrayList<>());
        usersPostsMap.get(username).add(post);
    }

    public List<Message> checkForPendingNotification(int clientOwner) {
        User user = getUser(clientIdUsernameMap.get(clientOwner));
        List<Message> notifications = new ArrayList<>();
        if(user!=null && pendingNotificationUsers.contains(user.getUsername())) {
            notifications = user.getPendingNotifications();
            pendingNotificationUsers.remove(user.getUsername());
        }
        return notifications;
    }

    public boolean follow(int op,String username,int clientOwner) {
        User userToFollow = getUser(username);
        User user;
        if(clientIdUsernameMap.get(clientOwner) == null)
            user = null;
        else
            user = usersMap.get(clientIdUsernameMap.get(clientOwner));
        if(user!=null && userToFollow!=null && user.isConnected()) {
            return userToFollow.follow(op,user);
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
        if(captcha.equals("1") && userExists(username) && getUser(username).getPw().equals(pw) && !getUser(username).isConnected() && !clientIdUsernameMap.containsKey(clientOwnerId)){
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

    public User getUser(String username) {
        if(username==null)
            return null;
        return usersMap.get(username);
    }


    private boolean userExists(String username) {
        return usersMap.containsKey(username);
    }


}
