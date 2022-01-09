package bgu.spl.net.srv;

import bgu.spl.net.api.BGSEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.echo.LineMessageEncoderDecoder;

public class ServerMain {
    public static void main(String[] args) {
        Server.threadPerClient(Integer.parseInt(args[0]),
                ()->new BidiMessagingProtocolImpl<>(),
                BGSEncoderDecoder::new).serve();
    }
}
