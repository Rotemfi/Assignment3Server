package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;

import java.util.LinkedList;

public class Block extends Message{
    private String username;

    public Block(int clientId, byte[] arr) {
        super(clientId, arr);
    }

    public void process(){
        if(!getDatabase().isUserExist(username))
            sendError((short) 12);
        else{
            User thisUser = getDatabase().getUserByUserConnectionId(clientID);
            User toBlock = getDatabase().getUserByUserName(username);
            LinkedList<User> whoAmBlocking = thisUser.getAmBlocking();
            LinkedList<User> whoBlockedOther = thisUser.getAmBlocking();
            if(!whoAmBlocking.contains(toBlock)) {
                thisUser.addToAmBlocking(toBlock);
                thisUser.removeFollower(toBlock);

            if (!whoBlockedOther.contains(thisUser))
                thisUser.addToBlockedBy(thisUser);

        }
    }

    public void decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0')  {
                username = popString();
        }
        pushByte(nextByte);
    }
}
