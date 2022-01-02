package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;

import java.util.Arrays;
import java.util.LinkedList;

public class Logstat extends Message {

    private int msgLen = 1<<10;//1KB
    byte[] msgToSend;

    public Logstat(int clientId) {
        super(clientId);
    }

    public void process(){
        if (!isUserNameLoggedIn(getDatabase().getUserByUserConnectionId(clientID).getUsername())||
                !isUserNameRegister(getDatabase().getUserByUserConnectionId(clientID).getUsername()))
            sendError((short)7);
        else{
            for (User user : getDatabase().getMapByUserName().values()){
                if (!user.getBlockedBy().contains(getDatabase().getUserByUserConnectionId(clientID))){
                byte[] byteMsg = encode(user.getUsername());
                getConnections().send(clientID, byteMsg);}
            }
        }
    }

    public byte[] encode(String username){
        User user = getDatabase().getUserByUserName(username);
        short ackCode = 10;
        short opCode = 7;
        short age = user.getAge();
        short numPosts = user.getMessagesSize();
        short numOfFollowers = user.getFollowersSize();
        short numFollowing = user.getFollowingSize();
        byte[] opBytes = shortToBytes(opCode);
        byte[] ackBytes = shortToBytes(ackCode);
        byte[] ageBytes = shortToBytes(age);
        byte[] numPostsBytes = shortToBytes(numPosts);
        byte[] numOfFollowersBytes = shortToBytes(numOfFollowers);
        byte[] numFollowingBytes = shortToBytes(numFollowing);
        pushByte(ackBytes[0]);
        pushByte(ackBytes[1]);

        pushByte(opBytes[0]);
        pushByte(opBytes[1]);

        pushByte(ageBytes[0]);
        pushByte(ageBytes[1]);

        pushByte(numPostsBytes[0]);
        pushByte(numPostsBytes[1]);

        pushByte(numOfFollowersBytes[0]);
        pushByte(numOfFollowersBytes[1]);

        pushByte(numFollowingBytes[0]);
        pushByte(numFollowingBytes[1]);

        return msgToSend;
    }

    public void pushByte(byte nextByte) {
        if (msgLen >= msgToSend.length)
            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
        msgToSend[msgLen] = nextByte;
        msgLen++;
    }
}

