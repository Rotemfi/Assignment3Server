package bgu.spl.net.srv.messages;

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

    public Message(byte[] arr){
        OP = bytesToShort(arr);
        bytes = new byte[1 << 10]; // 1KB byte array
        len = 0;
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

    public int getCount(){
        return count;
    }
}
