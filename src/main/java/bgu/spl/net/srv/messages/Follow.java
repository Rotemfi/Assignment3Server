package bgu.spl.net.srv.messages;


public class Follow extends Message {

    private byte follow;
    private String username;
    boolean first=true;

    public Follow(byte[] arr) {
        super(arr);
    }

    public void decodeNextByte(byte nextByte) {
        if(first) {
            follow = popByte();
            first=false;
        }
        else {
            if ((char) (nextByte & 0xFF) == ';') {
                username = popString();
            }
            else
                pushByte(nextByte);
        }
    }

    public void encodeNextByte(byte nextByte) {
        //
    }

}
