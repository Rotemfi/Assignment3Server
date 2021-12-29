package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

public class Post extends Message {

    private String content;


    public Post(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if (nextByte == '\0') {
                content = popString();
        }
        pushByte(nextByte);
    }

}
