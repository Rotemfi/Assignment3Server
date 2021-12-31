package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.Message;

import javax.xml.crypto.Data;
import java.util.LinkedList;
import java.util.function.LongUnaryOperator;

public class User {
    private String username;
    private short age;
    private boolean loggedIn;
    private Database database;
    private int connectionId;
    private LinkedList<User> followers;
    private LinkedList<User> following;
    private LinkedList<byte[]> messagesSent;
    private LinkedList<User> blockedBy;
    private LinkedList<User> amBlocking;

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

    public void addToBlockedBy(User user){
        blockedBy.add(user);
    }

    public void addToAmBlocking(User user){
        user.addToAmBlocking(this);
    }

    public int getConnectionId(){
        return connectionId;
    }

    public short getAge(){
        return age;
    }

    public short getFollowersSize(){
        return (short)followers.size();
    }

    public short getMessagesSize(){
        return (short)messagesSent.size();
    }

    public LinkedList<User> getFollowing(){
        return following;
    }

    public LinkedList<User> getFollowers(){
        return followers;
    }

    public short getFollowingSize(){
        return (short)following.size();
    }

    public LinkedList<User> getAmBlocking(){
        return amBlocking;
    }

    public LinkedList<User> getBlockedBy(){
        return blockedBy;
    }

    public String getUsername(){
        return this.username;
    }

    public void removeFollower(User user){
        user.followers.remove(this);
        this.following.remove(user);
    }

}
