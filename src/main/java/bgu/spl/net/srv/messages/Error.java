package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.messages.Message;

public class Error extends Message {

    private String listOfUsernames;

    public Error(byte[] arr) {
        super(arr);
    }

    public byte[] encode(){
        ///
    }

}
