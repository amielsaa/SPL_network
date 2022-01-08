package bgu.spl.net.impl.echo;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;

import java.io.*;
import java.net.Socket;

public class EchoClientTest implements Closeable {

    private final LineMessageEncoderDecoder encdec;
    private final Socket sock;
    private final BufferedInputStream in;
    private final BufferedOutputStream out;
    private boolean connected = true;
    private int zeroCounter = 0;

    public EchoClientTest(String host, int port) throws IOException {
        sock = new Socket(host, port);
        encdec = new LineMessageEncoderDecoder();
        in = new BufferedInputStream(sock.getInputStream());
        out = new BufferedOutputStream(sock.getOutputStream());
    }

    public void send(String msg) throws IOException {
        if(sock.isConnected()) {
            out.write(encdec.encode(msg));
            out.flush();
        }
    }

    public void receive() throws IOException {
        int read;
        while (sock.isConnected() && (read = in.read()) >= 0) {
            String msg = encdec.decodeNextByte((byte) read);
//            System.out.println(read);
//            if((byte)read == 0)
//                zeroCounter++;
//            else
//                zeroCounter=0;
//            if(zeroCounter>10){
//                zeroCounter=0;
//                break;
//            }

            if (msg != null) {
                if(msg.equals("ACK3"))
                    close();
                System.out.println(msg);
                break;
            }
        }


        //throw new IOException("disconnected before complete reading message");
    }

    @Override
    public void close() throws IOException {
        out.close();
        in.close();
        sock.close();
        connected=false;
    }

    public boolean shouldRun() {
        return connected;
    }

}
