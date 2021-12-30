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
    public void process(){
        if(isUserReceiverRegister()=false||theSenderFollowReceiver()=false) { //the server should send ERROR
            sendError((short) 6);
        }
        byte[] byteMsg = encoder();
        getConnections().send(receiverId, byteMsg);
        sendAck((short) 6);

    }

    //create notification
    public byte[] encoder(){
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] sender_Bytes = Username.getBytes(StandardCharsets.UTF_8);

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
