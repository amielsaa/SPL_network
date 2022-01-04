package bgu.spl.net.api.bidi;

import bgu.spl.net.api.msg.AckMsg;
import bgu.spl.net.api.msg.ErrorMsg;
import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.msg.NotificationMsg;
import bgu.spl.net.api.obj.SharedData;

import java.util.ArrayList;
import java.util.List;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private SharedData data;
    private Connections<T> connections;
    private int clientOwnerId;
    private boolean terminate = false;

    public BidiMessagingProtocolImpl(){

    }

    @Override
    public void start(int connectionId, Connections<T> connections) {
        this.connections = connections;
        this.clientOwnerId = connectionId;
        this.data = SharedData.getInstance();
    }

    @Override
    public void process(T message) {
        short opCode = ((Message)message).getOpCode();
        List<String> vars = ((Message)message).getVars();
        act(opCode,vars);
    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }

    private void act(short opCode,List<String> vars) {
        switch (opCode) {
            case 1:
                register(vars);
                break;
            case 2:
                login(vars);
                break;
            case 3:
                logout();
                break;
            case 4:
                follow(vars);
                break;
            case 5:
                post(vars);
                break;
        }
    }

    private void post(List<String> vars) {
        Message res;
        String post = vars.get(0);
        List<String> usernames = new ArrayList<>();
        String[] splittedBySpace = post.split(" ");
        for(String s : splittedBySpace) {
            if(s.contains("@"))
                usernames.add(s.substring(s.indexOf("@")+1));
        }

        String currentUsername = data.getUsernameById(clientOwnerId);
        boolean success = data.post(post,vars.get(1),clientOwnerId);
        if(success) {
            res = new AckMsg((short)5);
        } else {
            res = new ErrorMsg((short)5);
        }
        connections.send(clientOwnerId,(T)res);
        if(success) {
            List<String> notifVars = new ArrayList<>();
            notifVars.add(currentUsername);
            notifVars.add(post);
            notifVars.add(vars.get(1));
            Message notification = new NotificationMsg(notifVars);
            List<Integer> clientIdToPost = data.getClientIdsToPost(usernames, currentUsername,notification);
            for(Integer in : clientIdToPost) {
                connections.send(in,(T)notification);
            }
        }
    }

    private void follow(List<String> vars) {
        Message res;
        int op = Integer.parseInt(vars.get(0));
        String username = vars.get(1);
        boolean success = data.follow(op,username,clientOwnerId);
        if(success) {
            res = new AckMsg((short)4,vars);
        } else{
            res = new ErrorMsg((short)4);
        }
        connections.send(clientOwnerId,(T)res);
    }

    private void logout() {
        Message res;
        if(data.logout(clientOwnerId)) {
            res = new AckMsg((short)3);
            connections.send(clientOwnerId,(T)res);
            connections.disconnect(clientOwnerId);
            terminate = true;
        } else{
            res = new ErrorMsg((short)3);
            connections.send(clientOwnerId,(T)res);
        }

    }

    private void login(List<String> vars) {
        Message res;
        if(data.login(vars.get(0),vars.get(1),vars.get(2),clientOwnerId)) {
            res = new AckMsg((short)2);
            //delete - for debugging
            System.out.println(vars.get(0)+" logged in: true");
        } else
            res = new ErrorMsg((short)2);
        connections.send(clientOwnerId,(T)res);
    }

    private void register(List<String> vars) {
        System.out.println(vars.toString());
        boolean success = data.register(vars.get(0),vars.get(1),vars.get(2));
        Message res;
        //delete - for debugging
        System.out.println(vars.get(0)+" registered: "+success);

        List<String> resArray = new ArrayList<>();
        resArray.add("1");
        if(success)
            res = new AckMsg((short)1,resArray);
        else
            res = new ErrorMsg((short)1);
        connections.send(clientOwnerId,(T)res);
    }



}
