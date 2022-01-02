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

    public EchoClientTest(String host, int port) throws IOException {
        sock = new Socket(host, port);
        encdec = new LineMessageEncoderDecoder();
        in = new BufferedInputStream(sock.getInputStream());
        out = new BufferedOutputStream(sock.getOutputStream());
    }

    public void send(String msg) throws IOException {

        out.write(encdec.encode(msg));
        out.flush();
    }

    public void receive() throws IOException {
        int read;
        while ((read = in.read()) >= 0) {
            String msg = encdec.decodeNextByte((byte) read);
            if (msg != null) {
                System.out.println(">"+msg);
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
    }

}
