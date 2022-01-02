package bgu.spl.net.api.bidi;

import bgu.spl.net.api.msg.Message;
import bgu.spl.net.api.obj.SharedData;

import java.util.ArrayList;
import java.util.List;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {

    private SharedData data;
    private Connections<T> connections;
    private int clientOwnerId;

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
        if(opCode == 0) {
            boolean success = data.register(vars.get(0),vars.get(1),vars.get(2));
            List<String> msgArray = new ArrayList<>();
            msgArray.add(String.valueOf(opCode));
            connections.send(clientOwnerId,(T)(new Message((short)10,msgArray)));
        }
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }



}
