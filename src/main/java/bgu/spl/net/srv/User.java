package bgu.spl.net.srv;

import javax.xml.crypto.Data;

public class User {
    private String username;
    private short age;
    private short numPosts;
    private short numFollowers;
    private short numFollowing;
    private boolean loggedIn;
    private Database database;
    private int connectionId;

    public User(String username, short age, short numPosts,
                short numFollowers, short numFollowing, int connectionId){
        this.username = username;
        this.age = age;
        this.numPosts = numPosts;
        this.numFollowers = numFollowers;
        this.numPosts = numPosts;
        loggedIn = false;
        this.connectionId = connectionId;
    }

    public void setLoggedIn(boolean bool){
        loggedIn = bool;
    }

}
