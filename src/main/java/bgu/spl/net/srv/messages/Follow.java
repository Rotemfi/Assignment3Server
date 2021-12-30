package bgu.spl.net.srv.messages;


import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.User;

import java.nio.charset.StandardCharsets;

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

    public void process(){
        if(!getDatabase().isUserExist(username) || !getDatabase().isUserExist(clientID))
            sendError((short)4);
        else {
            User followingUser = getDatabase().getUserByUserConnectionId(clientID);
            User userToFollow = getDatabase().getUserByUserName(username);

            if (!followingUser.getLoggedIn()) {
                sendError((short) 4);
            } else {
                if (follow == (byte) 0) { //FollowAction
                    userToFollow.addFollowMe(followingUser);
                    followingUser.addToFollow(userToFollow);
                    sendAck((short) 4);

                } else {//UnfollowAction
                    userToFollow.removeFollower(followingUser);
                    followingUser.removeFromMyFollowList(userToFollow);
                    sendAck((short) 4);
                }
            }
        }
    }

    public void sendAck(){
        byte[] msg = new byte[1<<10];

        short msgOpCode = 4;
        short OpCode = 10;
        msg[0] = shortToBytes(OpCode)[0];
        msg[1] = shortToBytes(OpCode)[1];
        msg[2] = shortToBytes(msgOpCode)[0];
        msg[3] = shortToBytes(msgOpCode)[1];

        byte[] stringBytes = username.getBytes(StandardCharsets.UTF_8);
        int index=4;
        for(byte b: stringBytes) {
            msg[index] = b;
            index++;
        }
        msg[index] = (byte)'\0';

        getConnections().send(clientID, msg);
    }

}
