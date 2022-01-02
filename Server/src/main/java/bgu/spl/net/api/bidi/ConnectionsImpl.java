package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    //MAY NEED TO CHANGE MAP TYPE TO weakMap
    ConcurrentHashMap<Integer, ConnectionHandler<T>> activeClients;

    public ConnectionsImpl() {
        activeClients = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        //check if client exists first
        if(activeClients.containsKey(connectionId)){
            activeClients.get(connectionId).send(msg);
            return true;
        }
        return false;
    }

    @Override
    public void broadcast(T msg) {
        for(Map.Entry<Integer,ConnectionHandler<T>> entry : activeClients.entrySet()) {
            entry.getValue().send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        activeClients.remove(connectionId);
    }

    @Override
    public void register(int conId, ConnectionHandler<T> client) {
        activeClients.put(conId,client);
    }

}
