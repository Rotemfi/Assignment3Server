package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

public class Stat extends Message {

    private String listOfUsernames;

    public Stat(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if (nextByte == '\0') {
            listOfUsernames = popString();
        }
        pushByte(nextByte);
    }

    public byte[] encode(){
        ///
    }

}
