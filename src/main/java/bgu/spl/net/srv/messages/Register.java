package bgu.spl.net.srv.messages;

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

}
