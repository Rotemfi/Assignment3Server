package bgu.spl.net.srv.messages;

public class Logout extends Message{

    public Logout(byte[] arr) {
        super(arr);
    }


    public void process(){
        if(!dataBase.isUserLogin(clientID))
            sendError((short)3);
        else {
            User user = dataBase.getUser(clientID);
            user.setLogIn(false);
            dataBase.removeUser(clientID);
            sendAck((short) 3);
            //delete from all the places necessary (database, connections etc)
            //reminder: to kill the thread in thread per client implementation
        }
    }
}
