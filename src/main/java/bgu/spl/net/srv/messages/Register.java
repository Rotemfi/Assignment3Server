package bgu.spl.net.srv.messages;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.messages.Message;

public class Register extends Message {

    private String Username;
    private String Password;
    private String birthday;


    public Register(String Username,String Password,String birthday) {
        this.Username = Username;
        this.Password = Password;
        this.birthday = birthday;

       // clientID = getConnections().addClient(new ConnectionHandler<T>);
    }

//    public int decodeNextByte(byte nextByte) {
//        if ((char)(nextByte&0xFF) == '\0') {
//            if (getCount() == 0)//UserName
//                Username = popString();
//            if (getCount() == 1)//Password
//                Password = popString();
//            else
//                birthday = popString();
//            return 0;
//        }
//        pushByte(nextByte);
//        return 1;
//    }

    public void process(){
        //server check if in the database this username is already in use
        //Enter to the database
    }



}
