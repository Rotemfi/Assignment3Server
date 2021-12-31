package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.security.KeyPair;

public class Login extends Message {
    private String Username;
    private String Password;
    private byte Captcha;
    private boolean byteTime=false;

    public Login(int clientId, byte[] arr) {
        super(clientId, arr);
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

    public boolean isPasswordMatch(){
        //checks if the username matches the password in the database
    }

    public void process(){
        if(!getDatabase().isUserExist(Username)){
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
                while(!user.getNotificationList().isEmpty()) {
                    byte[] notificationToSend = user.getNotificationList.pull();
                    getConnections().send(clientID,notificationToSend);
                }
            }
        }
    }

}
