package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;

public class Post extends Message {

    private String content;
    private int msgLen = 1<<10;//1KB
    byte[] msgToSend;

    public Post(byte[] arr) {
        super(arr);
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
        if (!isUserNameLoggedIn(//METHOD_TO_GET_USERNAME_BY_CLIENT_ID_FROM_DATABASE))
            sendError(5);
        else{
            LinkedList<String> users = getUsers();
            for (String user : users){
                byte[] byteMsg = encoder();
                int connectionId = user.getConnection() // METHOD_FROM_DATABASE
                getConnections().send(connectionId, byteMsg);
            }
        }
    }

    public byte[] encoder(){
        //COPY_FROM_PM
    }

    public void pushByte(byte nextByte) {
        if (msgLen >= msgToSend.length)
            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
        msgToSend[msgLen] = nextByte;
        msgLen++;
    }

}
