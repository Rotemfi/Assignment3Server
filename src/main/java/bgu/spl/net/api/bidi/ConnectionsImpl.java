package bgu.spl.net.api.bidi;

import bgu.spl.net.srv.ConnectionHandler;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private static class ConnectionsHolder{
        private static ConnectionsImpl Connections_instance = new ConnectionsImpl();
    }

    public static ConnectionsImpl getInstance()
    {
        return ConnectionsHolder.Connections_instance;
    }

    private ConcurrentHashMap<Integer, ConnectionHandler> connectionHandlers;

    public ConnectionsImpl(){
        connectionHandlers = new ConcurrentHashMap<>();
    }

    public void addClient(int connectionId, ConnectionHandler<T> connectionHandler) {
        //Put if absent instead of put?
        connectionHandlers.put(connectionId,connectionHandler);
    }

    @Override
    //Do we need to Sync?
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> ch = connectionHandlers.get(connectionId);
        if(ch!=null) {
            ch.send(msg);
            return true;
        }
        return false;
    }

    //Do we need to Sync?
    @Override
    public void broadcast(T msg) {
        for (Map.Entry<Integer,ConnectionHandler> ch :connectionHandlers.entrySet()) {
            ConnectionHandler thisCH = ch.getValue();
            if(thisCH!=null)
                thisCH.send(msg);
        }
    }
    //Do we need to Sync?
    @Override
    public void disconnect(int connectionId) {
        connectionHandlers.remove(connectionId);

    }
}
