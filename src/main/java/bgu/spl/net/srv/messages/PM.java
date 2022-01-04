package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PM extends Message {

    private String Username;
    private String content;
    private String sending_date_and_time;
    private int receiverId;
    private int msgLen = 1<<10;//1KB
    byte[] msgToSend;

    public PM(String Username, String content,String sending_date_and_time) {
        this.Username = Username;
        this.content = content;
        this.sending_date_and_time = sending_date_and_time;
        //get the reciverId from the database;
        User user = getDatabase().getUserByUserName(Username);
        receiverId = user.getConnectionId();
    }



    public boolean isUserReceiverRegister(){
        return getDatabase().isUserExist(receiverId);
    }

    public boolean theSenderFollowReceiver(){
        return getDatabase().isUserExist(clientID);
    }

    public void process(int connectionId){
        this.clientID = connectionId;
        if(isUserReceiverRegister()==false||theSenderFollowReceiver()==false
        || getDatabase().getUserByUserConnectionId(receiverId).getAmBlocking().contains(getDatabase().getUserByUserConnectionId(clientID)))
        { //the server should send ERROR
            sendError((short) 6);
        }
        else {
            byte[] byteMsg = encoder();
            sendAck((short) 6);
            User receiverUser = getDatabase().getUserByUserConnectionId(receiverId);
            if (receiverUser.getLoggedIn()) {
                getConnections().send(receiverId, byteMsg);
            } else {
                receiverUser.addToMessages(byteMsg);
            }
            sendAck((short)6);
        }
    }

    //create notification
    public byte[] encoder(){
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        User thisClientId = getDatabase().getUserByUserConnectionId(clientID);
        String thisUserName = thisClientId.getUsername();
        byte[] sender_Bytes = thisUserName.getBytes(StandardCharsets.UTF_8);

       short opCode = 9;
       byte[] opBytes = shortToBytes(opCode);
       pushByte(opBytes[0]);
       pushByte(opBytes[1]);

       pushByte((byte)0);//PM message

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

    public void pushByte(byte nextByte) {
        if (msgLen >= msgToSend.length)
            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
        msgToSend[msgLen] = nextByte;
        msgLen++;
    }


}
