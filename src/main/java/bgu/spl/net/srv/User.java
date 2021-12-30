package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.Message;

import javax.xml.crypto.Data;
import java.util.LinkedList;

public class User {
    private String username;
    private short age;
    private boolean loggedIn;
    private Database database;
    private int connectionId;
    private LinkedList<User> followers;
    private LinkedList<User> following;
    private LinkedList<byte[]> messagesSent;

    public User(String username, short age, int connectionId){
        this.username = username;
        this.age = age;
        loggedIn = false;
        this.connectionId = connectionId;
    }

    public void setLoggedIn(boolean bool){
        loggedIn = bool;
    }

    public boolean getLoggedIn(){
        return loggedIn;
    }

    public void addFollowMe(User user){
        if (!followers.contains(user))
        followers.add(user);
    }

    public void addToFollow(User user){
        if (!following.contains(user))
        following.add(user);
    }

    public void addToMessages(byte[] msg){
        addToMessages(msg);
    }

    public int getConnectionId(){
        return connectionId;
    }

    public short getAge(){
        return age;
    }

    public short getFollowers(){
        return (short)followers.size();
    }

    public short getFollowing(){
        return (short)following.size();
    }

}
