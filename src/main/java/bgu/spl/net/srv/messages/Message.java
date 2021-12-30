package bgu.spl.net.srv.messages;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.Database;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Message { //abstract?
    private byte[] bytes;
    private int len;
    private short OP;
    private byte[] partBytes;
    private int count=0;
    protected int clientID;
    protected byte[] msgByteArr;
    private ConnectionsImpl connections;
    private Database database;

    public ConnectionsImpl getConnections() {
        return connections;
    }

    public Database getDatabase() { return database; }

    public Message(int clientId, byte[] arr){
        msgByteArr = arr;
        OP = bytesToShort(arr);
        bytes = new byte[1 << 10]; // 1KB byte array
        len = 0;
        this.clientID=clientID;
        connections = connections.getInstance();
        database = database.getInstance();

    }

    public boolean isUserNameRegister(String username){
        return database.isUserExist(username);
    }

    public boolean isUserNameLoggedIn(String username){
        return database.getUserByUserName(username).getLoggedIn();
    }

    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public short getOP() {
        return OP;
    }

    public void pushByte(byte nextByte) {
        if (len >= partBytes.length)
            partBytes = Arrays.copyOf(partBytes, len * 2);
        partBytes[len] = nextByte;
        len++;
    }

    public String popString() {
        String result = new String(partBytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        count++;
        partBytes = new byte[1 << 10];
        return result;
    }

    public byte popByte(){
        byte result = partBytes[0];
        len=0;
        count++;
        partBytes = new byte[1 << 10];
        return result;
    }

    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public int getCount(){
        return count;
    }

 //   public int getClientID(){
        return clientID;
    }

    public void sendError(short msgOpCode){
        short OpCode = 11;
        byte[] msg = new byte[4];// = {0,11,0,1}
        msg[0] = shortToBytes(OpCode)[0];
        msg[1] = shortToBytes(OpCode)[1];
        msg[2] = shortToBytes(msgOpCode)[0];
        msg[3] = shortToBytes(msgOpCode)[1];
        getConnections().send(clientID, msg);
    }

    public void sendAck(short msgOpCode){
        short OpCode = 10;
        byte[] msg = new byte[4];// = {0,10,0,1}
        msg[0] = shortToBytes(OpCode)[0];
        msg[1] = shortToBytes(OpCode)[1];
        msg[2] = shortToBytes(msgOpCode)[0];
        msg[3] = shortToBytes(msgOpCode)[1];
        getConnections().send(clientID, msg);
    }

}
