package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

public class PM extends Message {

    private String Username;
    private String content;
    private String sending_date_and_time;

    public PM(byte[] arr) {
        super(arr);
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

    public byte[] encoder(String msg){
        ////
    }


}
