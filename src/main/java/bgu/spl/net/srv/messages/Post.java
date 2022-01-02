package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class Post extends Message {

    private String content;
    private int msgLen = 1<<10;//1KB
    byte[] msgToSend;

    public Post(int clientId) {
        super(clientId);
    }

    public void decodeNextByte(byte nextByte) {
        if (nextByte == '\0') {
                content = popString();
        }
        pushByte(nextByte);
    }

    public LinkedList<String> getUsers(){
        LinkedList<String> users = new LinkedList<>();
        int i = 0;
        char a = content.charAt(i);
        String user = "";
        while (i < content.length()){
            if (a != '@') // Make sure == works that way
                i++;
            else{
                    while (a != ' '){ // Make sure == works that way
                        user = user + a;
                        i++;
                      }
                    users.add(user);
                    user = "";
            }
        }
        return users;
    }

    public void process(){
        if (!(getDatabase().getUserByUserConnectionId(clientID).getLoggedIn()))
                sendError((short)5);
        else{
            LinkedList<User> followersUsers = getDatabase().getUserByUserConnectionId(clientID).getFollowers();
            for (User user : followersUsers) {
                    byte[] byteMsg = encoder();
                    int connectionId = user.getConnectionId();
                    getConnections().send(connectionId, byteMsg);
            }
            LinkedList<String> taggedUsers = getUsers();
            for (String user : taggedUsers){
                User user1 = getDatabase().getUserByUserName(user);
                if (!user1.getBlockedBy().contains(getDatabase().getUserByUserConnectionId(clientID))
                        && !user1.getFollowing().contains(getDatabase().getUserByUserConnectionId(clientID))) {
                    byte[] byteMsg = encoder();
                    int connectionId = user1.getConnectionId();
                    getConnections().send(connectionId, byteMsg);
                }
            }
        }
    }

    public byte[] encoder(){
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            User thisClientId = getDatabase().getUserByUserConnectionId(clientID);
            String thisUserName = thisClientId.getUsername();
            byte[] sender_Bytes = thisUserName.getBytes(StandardCharsets.UTF_8);

            short opCode = 9;
            byte[] opBytes = shortToBytes(opCode);
            pushByte(opBytes[0]);
            pushByte(opBytes[1]);

            pushByte((byte)0);//Post message

            for(byte b: sender_Bytes){
                pushByte(b);
            }
            pushByte((byte)'\0');

            for(byte b: contentBytes){
                pushByte(b);
            }
            pushByte((byte)'\0');

            return msgToSend;
        }

//    public void pushByte(byte nextByte) {
//        if (msgLen >= msgToSend.length)
//            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
//        msgToSend[msgLen] = nextByte;
//        msgLen++;
//    }

}
