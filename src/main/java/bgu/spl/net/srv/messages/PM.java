package bgu.spl.net.srv.messages;

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

    public PM(int clientId, byte[] arr) {
        super(clientId, arr);
        //get the reciverId from the database;
        receiverId = DataBase.getClientId(Username);
    }

    public void decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0')  {
            if (getCount() == 0)//UserName
                Username = popString();
            if (getCount() == 1)//Password
                content = popString();
            else
                sending_date_and_time = popString();
        }
        pushByte(nextByte);
    }

    public boolean isUserReceiverRegister(){
        //checks in the database if user is register
    }


    public boolean theSenderFollowReceiver(){
        //check in database if reciever is followed by sender
    }

    public void processReceieve() {

    }

    //checks if all conditions OK
    public void processSend(){
        if(isUserReceiverRegister()=false||theSenderFollowReceiver()=false) { //the server should send ERROR
            return false;
            sendAck(6);
        }
        byte[] byteMsg = encoder();
        getConnections().send(receiverId, byteMsg);
        return true; //the server should send ACK
    }

    public byte[] encoder(){
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] date_Time_Bytes = sending_date_and_time.getBytes(StandardCharsets.UTF_8);

        for(byte b: contentBytes){
            pushByte(b);
        }
        contentBytes[msgLen] = '\0';
        for(byte b: date_Time_Bytes){
            pushByte(b);
        }
        return msgToSend;
    }

    public void pushByte(byte nextByte) {
        if (msgLen >= msgToSend.length)
            msgToSend = Arrays.copyOf(msgToSend, msgLen * 2);
        msgToSend[msgLen] = nextByte;
        msgLen++;
    }


}
