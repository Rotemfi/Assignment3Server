package bgu.spl.net.srv.messages;


public class Notification extends Message {

    private byte NotificationType;
    private String PostingUser;
    private String content;
    private boolean first = true;

    public Notification(int clientId) {
        super(clientId);
    }

    public void decodeNextByte(byte nextByte) {
        if (first) {
            NotificationType = popByte();
            first = false;
        } else {
            if ((char) (nextByte & 0xFF) == '\0') {
                if (getCount() == 1)//UserName
                    PostingUser = popString();
                else
                    content = popString();
            }
        }
    }

    public void process() {
        User thisUser = dataBase.getUserByName(clientID);
        if(thisUser.isLoggedIn()){
            getConnections().send(clientID, msgByteArr);
        }
    }

}