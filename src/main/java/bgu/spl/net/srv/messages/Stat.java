package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class Stat extends Message {

    private int msgLen = 1<<10;//1KB
    byte[] msgToSend;
    private String listOfUsernames;
    private LinkedList<String> actualListOfUsernames;

    public Stat(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if (nextByte == '\0') {
            listOfUsernames = popString();
        }
        pushByte(nextByte);
    }

    public void process(){
        createList();
        if (!isUserNameLoggedIn(//METHOD_TO_GET_USERNAME_BY_CLIENT_ID_FROM_DATABASE)||
                !isUserNameRegister(//) )
                        sendError(5));
        else{
            for (String user : actualListOfUsernames){
                byte[] byteMsg = encode(user);
                getConnections().send(clientID, byteMsg);
            }
        }
    }

    public byte[] encode(String username){
            short ackCode = 10;
            short opCode = 8;
            short age = // DATABASE_FUNCTION_TO_ADD
            short numPosts = // DATABASE_FUNCTION_TO_ADD
            short numOfFollowers = // DATABASE_FUNCTION_TO_ADD
            short numFollowing = // DATABASE_FUNCTION_TO_ADD
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

    public void createList(){
        for (int i = 0; i < listOfUsernames.length(); i++){
            char c = listOfUsernames.charAt(i);
            String username="";
            while (c != '|'){
                username = username + c;
                i++;
                c = listOfUsernames.charAt(i);
            }

            actualListOfUsernames.add(username);
            i++;
        }
    }

    public void pushByte(byte nextByte) {
        if (msgLen >= msgToSend.length)
            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
        msgToSend[msgLen] = nextByte;
        msgLen++;
    }
}
