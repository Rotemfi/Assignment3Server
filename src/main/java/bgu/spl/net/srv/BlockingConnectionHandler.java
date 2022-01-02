package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.srv.messages.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BlockingConnectionHandler<T> implements Runnable, ConnectionHandler<T> {

    private final BidiMessagingProtocolImpl<Message> protocol;
    private final MessageEncoderDecoderImpl<T> encdec;
    private final Socket sock;
    private BufferedInputStream in;
    private BufferedOutputStream out;
    private volatile boolean connected = true;

    public BlockingConnectionHandler(Socket sock, MessageEncoderDecoderImpl<T> reader, BidiMessagingProtocolImpl<Message> protocol) {
        this.sock = sock;
        this.encdec = reader;
        this.protocol = protocol;
    }

    @Override
    public void run() {
        try (Socket sock = this.sock) { //just for automatic closing
            int read;
            boolean doneOP=false;
            short OP;
            Message message = new Register(1);//
            ConnectionsImpl connections = ConnectionsImpl.getInstance();
            int clientId = connections.getIdByHandler(this);

            in = new BufferedInputStream(sock.getInputStream());
            out = new BufferedOutputStream(sock.getOutputStream());

            while (!protocol.shouldTerminate() && connected && (read = in.read()) >= 0) {
                if(!doneOP){
                    OP = encdec.decodeOp((byte) read);
                    if(OP!=0) {
                        doneOP = true;
                        switch (OP){
                            case 1:
                                message = new Register(clientId);
                            case 2:
                                message = new Login(clientId);
                            case 3:
                                message = new Logout(clientId);
                            case 4:
                                message = new Follow(clientId);
                            case 5:
                                message = new Post(clientId);
                            case 6:
                                message = new PM(clientId);
                            case 7:
                                message = new Logstat(clientId);
                            case 8:
                                message = new Stat(clientId);
                            case 12:
                                message = new Block(clientId);

                        }
                    }
                }
                else {
                    int done = message.decodeNextByte((byte)read);
                    if (done != 0) {//we decode all the line
                         protocol.process(message);
//                        if (response != null)
//                            out.write(encdec.encode(response));
//                            out.flush();
//
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void close() throws IOException {
        connected = false;
        sock.close();
    }

    @Override
    //sends to his own client
    public void send(T msg) {
        try {
            out = new BufferedOutputStream(sock.getOutputStream());
            out.write((byte[]) msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
