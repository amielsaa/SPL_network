package bgu.spl.net.api.obj;

import java.util.concurrent.ConcurrentHashMap;

public class SharedData {

    private ConcurrentHashMap<String,User> usersMap;

    //a hashmap to map each clientownerid to a username
    private ConcurrentHashMap<Integer,String> clientIdUsernameMap;

    private static class  SharedDataHolder {
        private static SharedData instance = new SharedData() ;
    }
    private SharedData() {
        usersMap = new ConcurrentHashMap<>();
        clientIdUsernameMap = new ConcurrentHashMap<>();
    }
    public static SharedData getInstance() {
        return SharedDataHolder.instance;
    }


    public boolean logout(int clientOwnerId) {
        User user = getUser(clientIdUsernameMap.get(clientOwnerId));
        if(user!=null){
            user.setConnected(false);
            return true;
        }
        return false;
    }

    public boolean login(String username, String pw, String captcha,int clientOwnerId) {
        if(captcha.equals("1") && userExists(username) && getUser(username).getPw().equals(pw)){
            getUser(username).setConnected(true);
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

    private User getUser(String username) {
        return usersMap.get(username);
    }

    private boolean userExists(String username) {
        return usersMap.containsKey(username);
    }


}
