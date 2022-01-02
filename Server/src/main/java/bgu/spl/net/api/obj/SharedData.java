package bgu.spl.net.api.obj;

import java.util.concurrent.ConcurrentHashMap;

public class SharedData {

    private ConcurrentHashMap<String,User> usersMap;

    private static class  SharedDataHolder {
        private static SharedData instance = new SharedData() ;
    }
    private SharedData() {
        usersMap = new ConcurrentHashMap<>();
    }
    public static SharedData getInstance() {
        return SharedDataHolder.instance;
    }

    public boolean register(String username,String pw, String birthday) {
        if(usersMap.containsKey(username)) {
            return false;
        }
        usersMap.put(username,new User(username,pw,birthday));
        return true;
    }


}
