package bgu.spl.net.api;

import bgu.spl.net.srv.messages.Login;
import bgu.spl.net.srv.messages.Register;

public class MessageEncoderDecoderImpl<Message> implements MessageEncoderDecoder<Message>{
    boolean first=false;
    boolean second=false;
    byte[] opCode = new byte[2];


    @Override
    public Message decodeNextByte(byte nextByte) {
        return null;
    }

    public short decodeOp(byte b) {
        if (first == false) {
            opCode[0] = b;
            first = true;
        } else {
            if (second == false) {
                opCode[1] = b;
                second = true;
                short ans = bytesToShort(opCode);
                return ans;
            }
        }
        return 0;
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
}
