package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.security.KeyPair;

public class Login extends Message {
    private String Username;
    private String Password;
    private byte Captcha;
    private boolean byteTime=false;

    public Login(int clientId) {
        super(clientId);
    }

    public int decodeNextByte(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0') {
            if (getCount() == 0)//UserName
                Username = popString();
            if (getCount() == 1) {//Password
                Password = popString();
                byteTime = true;
            }
        }
        else {
            if (byteTime == true) {
                Captcha = popByte();
                return 1;
            }
            else
                pushByte(nextByte);
        }
        return 0;
    }

//    public boolean isPasswordMatch(){
//        //checks if the username matches the password in the database
//    }

    public void process(int connectionId){
        this.clientID = connectionId;
        if(!getDatabase().isUserExist(Username) || Captcha == '0') {
            sendError((short) 2);
        }
        else {
            User user = getDatabase().getUserByUserName(Username);
            if(user.getPassword()!=Password)
                sendError((short) 2);
            else {
                user.setLoggedIn(true);
                //add the user to the login database(update the register to be logged in)
                sendAck((short) 2);
                while(!user.getNotificationsList().isEmpty()) {
                    byte[] notificationToSend = user.getNotificationsList().poll();
                    getConnections().send(clientID,notificationToSend);
                }
            }
        }
    }

}
