package bgu.spl.net.srv.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.messages.Message;

public class Register extends Message {

    private String Username;
    private String Password;
    private String birthday;


    public Register(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0') {
            if (getCount() == 0)//UserName
                Username = popString();
            if (getCount() == 1)//Password
                Password = popString();
            else
                birthday = popString();
        }
        pushByte(nextByte);
    }

    public void process(){
        Message msg =
                getConnections().send(clientID, msg);
    }

    public void sendAck(){
        short OpCode = 10;
        short msgOpCode = 1;
        byte[] msg = new byte[4];// = {0,10,0,1}
        msg[0] = shortToBytes(OpCode)[0];
        msg[1] = shortToBytes(OpCode)[1];
        msg[2] = shortToBytes(msgOpCode)[0];
        msg[3] = shortToBytes(msgOpCode)[1];
        getConnections().send(clientID, msg);

    }

}
