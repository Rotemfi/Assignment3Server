
package bgu.spl.net.api;

import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl<Message> implements MessageEncoderDecoder<Message>{
    private boolean first=false;
    private boolean second=false;
    private byte[] opCode = new byte[2];
    private short OP;

    private byte[] bytes;
    private int len;
    private byte[] partBytes;
    protected int clientID;
    private ConnectionsImpl connections;
    private String Username;
    private String Password;
    private String birthday;

    private byte Captcha;
    private boolean byteTime=false;

    int registerCount=0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if(!first||!second)
            decodeOp(nextByte);
        switch (OP){
            case 1:
                return  decRegister(nextByte);
            case 2:
                return decLogin(nextByte);
            case 3:
                return (Message) new Logout();
            case 4:
                return decFollow(nextByte);
            case 5:
                return decPost(nextByte);
            case 6:
                return decPM(nextByte);
            case 7:
                return new decLogstat(nextByte);
            case 8:
                return new decStat(nextByte);
            case 12:
                return new Block(nextByte);

        }
    }

    public void decodeOp(byte b) {
        if (first == false) {
            opCode[0] = b;
            first = true;
        } else {
            if (second == false) {
                opCode[1] = b;
                second = true;
                short ans = bytesToShort(opCode);
                OP=ans;
            }
        }
    }
    //check
    public short bytesToShort(byte[] byteArr)
    {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    @Override
    public byte[] encode(Object message) {
        return new byte[0];
    }

    public Message decRegister(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0') {
            if (registerCount == 0)//UserName
                Username = popString();
            if (registerCount == 1)//Password
                Password = popString();
            else {
                birthday = popString();
                registerCount=0;
                return (Message) new Register(Username,Password,birthday);
            }
        }
        pushByte(nextByte);
        return null;
    }


    public Message decLogin(byte nextByte) {
        if ((char)(nextByte&0xFF) == '\0') {
            if (registerCount== 0)//UserName
                Username = popString();
            if (registerCount == 1) {//Password
                Password = popString();
                byteTime = true;
            }
        }
        else {
            if (byteTime == true) {
                Captcha = popByte();
                return (Message) new Login(Username,Password,Captcha);
            }
            else
                pushByte(nextByte);
        }
        return null;
    }


    public String popString() {
        String result = new String(partBytes, 0, len, StandardCharsets.UTF_8);
        len = 0;
        registerCount++;
        partBytes = new byte[1 << 10];
        return result;
    }

    public byte popByte(){
        byte result = partBytes[0];
        len=0;
        registerCount++;
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

    public void pushByte(byte nextByte) {
        if (len >= partBytes.length)
            partBytes = Arrays.copyOf(partBytes, len * 2);
        partBytes[len] = nextByte;
        len++;
    }

}