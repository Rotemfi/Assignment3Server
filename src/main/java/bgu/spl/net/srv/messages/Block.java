package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;

import java.util.LinkedList;

public class Block extends Message{
    private String username;

    public Block(int clientId) {
        super(clientId);
    }

    public void process(){
        if(!getDatabase().isUserExist(username))
            sendError((short) 12);
        else{
            User thisUser = getDatabase().getUserByUserConnectionId(clientID);
            User toBlock = getDatabase().getUserByUserName(username);

            thisUser.addToAmBlocking(toBlock);
            thisUser.removeFollowing(toBlock);

        }
    }

    public int decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0')  {
                username = popString();
                return 1;
        }
        pushByte(nextByte);
        return 0;
    }
}
