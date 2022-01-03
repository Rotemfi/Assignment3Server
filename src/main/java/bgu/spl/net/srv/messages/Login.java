package bgu.spl.net.srv.messages;

import bgu.spl.net.srv.User;
import bgu.spl.net.srv.messages.Message;

import java.security.KeyPair;

public class Login extends Message {
    private String Username;
    private String Password;
    private byte Captcha;

    public Login(String Username,String Password,byte Captcha) {
        this.Username=Username;
        this.Password=Password;
        this.Captcha=Captcha;
    }

//    public boolean isPasswordMatch(){
//        //checks if the username matches the password in the database
//    }

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
                while(!user.getNotificationsList().isEmpty()) {
                    byte[] notificationToSend = user.getNotificationsList().poll();
                    getConnections().send(clientID,notificationToSend);
                }
            }
        }
    }

}
