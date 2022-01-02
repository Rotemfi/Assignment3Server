package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;

public class Logout extends Message{
    private boolean logOutSucceeded=false;

    public Logout(int clientId) {
        super(clientId);
    }

    public boolean logoutSucceeded(){
        return logOutSucceeded;
    }

    public int decodeNextByte(byte nextByte) {
        return 1;
    }

    public void process(){
        if(!getDatabase().isUserExist(clientID))
            sendError((short)3);
        else {
            User user = getDatabase().getUserByUserConnectionId(clientID);
            user.setLoggedIn(false);
            // getDatabase().removeUser(clientID);
            logOutSucceeded=true;
            sendAck((short) 3);
            //delete from all the places necessary (database, connections etc)
            //reminder: to kill the thread in thread per client implementation
        }
    }
}
