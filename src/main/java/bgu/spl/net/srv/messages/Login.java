package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

public class Login extends Message {
    private String Username;
    private String Password;
    private byte Captcha;
    private boolean byteTime=false;

    public Login(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0') {
            if (getCount() == 0)//UserName
                Username = popString();
            if (getCount() == 1) {//Password
                Password = popString();
                byteTime = true;
            }
        }
        else {
            if (byteTime == true)
                Captcha = popByte();
            else
                pushByte(nextByte);
        }
    }

}
