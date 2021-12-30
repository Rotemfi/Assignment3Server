package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.ConnectionsImpl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class Database {
    private ConcurrentHashMap<String, User> usersByUserName;
    private ConcurrentHashMap<Integer, User> usersByConnectionId;

    private static class DatabaseHolder{
        private static Database Database_instance = new Database();
    }

    public static Database getInstance()
    {
        return DatabaseHolder.Database_instance;
    }

    private Database(){
        usersByUserName = new ConcurrentHashMap<>();
        usersByConnectionId = new ConcurrentHashMap<>();
    }

    public boolean isUserExist(String username){
        return usersByUserName.containsKey(username);
    }

    public boolean isUserExist(int connectionId){
        return usersByConnectionId.containsKey(connectionId);
    }

    public User getUserByUserName(String username){
        return usersByUserName.get(username);
    }

    public User getUserByUserConnectionId(int connectionId){
        return usersByConnectionId.get(connectionId);
    }

    public void addUser(String username){
        User user = usersByUserName.get(username);
        usersByUserName.put(username, user);
    }

    public void deleteUser(String username){
        User user = usersByUserName.remove(username);
    }

}
