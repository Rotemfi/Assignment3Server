package bgu.spl.net.srv.messages;

public class Logout extends Message{

    public Logout(byte[] arr) {
        super(arr);
    }


    public void process(){
        User user = dataBase.getUser();
        user.setLogIn(false);
        //delete from all the places necessary (database, connections etc)
        //reminder: to kill the thread in thread per client implementation
    }
}
