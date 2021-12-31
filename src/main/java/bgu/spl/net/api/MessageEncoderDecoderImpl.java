package bgu.spl.net.api;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder{
    boolean first=false;
    boolean second=false;
    byte[] opCode = new byte[2];

    @Override
    public Object decodeNextByte(byte nextByte) {
        if(first==false){
            opCode[0]=nextByte;
            first=true;
        }
        else {
            if (second == false) {
                opCode[1] = nextByte;
                second = true;
            } else{//its not the first 2 bytes
                short opCode1 = bytesToShort(opCode);


                if (nextByte == (byte) ';')
                    process();
            return null;
        }
        }
    }

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
